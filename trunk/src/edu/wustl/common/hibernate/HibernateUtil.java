package edu.wustl.common.hibernate;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.DOMWriter;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.util.XMLHelper;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Utility for creating a hibernate session-factory and managing thread-local
 * sessions. This class expects a file called "dbutil.properties" to be present
 * in the classpath; this file should contain all the hibernate configuration
 * files to be loaded. A sample "dbutil.properties" file is shown below:<br>
 * 
 * <pre>
 * hibernate.configuration.files = washuCommonsHibernate.cfg.xml,dehibernate.cfg.xml,metadataHibernate.cfg.xml,queryhibernate.cfg.xml,hibernate.cfg.xml
 * </pre>
 * 
 * <br>
 * The property file contains a single entry with all the configuration files to
 * be loaded; the file names are comma-separated.<br>
 * <b>Note:</b> It is highly recommended that only one of the configuration
 * files contains connection details; if conflicting hibernate configuration
 * properties are found, then the resulting configuration is undefined. Multiple
 * configuration files are intended only to logically separate the set of hbm
 * mappings, caching details etc...
 * 
 * @author Kapil Kaveeshwar
 * @see HibernateDatabaseOperations
 */
public class HibernateUtil {
    // A factory for DB Session which provides the Connection for client.
    private static final SessionFactory m_sessionFactory;

    // ThreadLocal to hold the Session for the current executing thread.
    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

    // Initialize the session Factory in the Static block.
    // Initialize the session Factory in the Static block.
    private static final EntityResolver entityResolver = XMLHelper.DEFAULT_DTD_RESOLVER;

    private static final Configuration cfg;

    static {
        cfg = new Configuration();

        InputStream inputStream = HibernateUtil.class.getClassLoader().getResourceAsStream("dbutil.properties");
        Properties p = new Properties();
        try {
            p.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String configurationFileNames = p.getProperty("hibernate.configuration.files");

        String[] fileNames = null;
        if (configurationFileNames != null) {
            fileNames = configurationFileNames.split(",");
        }

        // if no configuraiton file found, get the default one.
        if (fileNames == null) {
            fileNames = new String[]{"hibernate.cfg.xml"};
        }
        // get all configuration files
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            fileName = fileName.trim();
            System.out.println(fileName + ": fileName");
            addConfigurationFile(fileName, cfg);
        }

        m_sessionFactory = cfg.buildSessionFactory();

    }

    /**
     * This method adds configuration file to Hibernate Configuration.
     * 
     * @param fileName name of the file that needs to be added
     * @param cfg Configuration to which this file is added.
     */
    private static void addConfigurationFile(String fileName, Configuration cfg) {
        try {
            InputStream inputStream = HibernateUtil.class.getClassLoader().getResourceAsStream(fileName);
            List errors = new ArrayList();
            // hibernate api to read configuration file and convert it to
            // Document(dom4j) object.
            XMLHelper xmlHelper = new XMLHelper();
            Document document = xmlHelper.createSAXReader(fileName, errors, entityResolver).read(
                    new InputSource(inputStream));
            // convert to w3c Document object.
            DOMWriter writer = new DOMWriter();
            org.w3c.dom.Document doc = writer.write(document);
            // configure
            cfg.configure(doc);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Follows the singleton pattern and returns only current opened session.
     * 
     * @return Returns the current db session.
     */
    public static Session currentSession() throws HibernateException {
        Session s = (Session) threadLocal.get();

        // Open a new Session, if this Thread has none yet
        if (s == null) {
            s = m_sessionFactory.openSession();
            s.setFlushMode(FlushMode.COMMIT);
            try {
                s.connection().setAutoCommit(false);
            } catch (SQLException ex) {
                throw new HibernateException(ex.getMessage(), ex);
            }
            threadLocal.set(s);
        }
        return s;
    }

    /**
     * Close the currently opened session.
     */
    public static void closeSession() throws HibernateException {
        Session s = (Session) threadLocal.get();
        threadLocal.set(null);
        if (s != null)
            s.close();
    }

    public static Connection getConnection() throws HibernateException {
        return currentSession().connection();
    }

    public static void closeConnection() throws HibernateException {
        closeSession();
    }

    public static SessionFactory getSessionFactory() {
        return m_sessionFactory;
    }

    /**
     * Intended to be read-only. Changes to this Configuration do NOT affect the
     * sessionFactory.
     */
    public static Configuration getConfiguration() {
        return cfg;
    }

    public static Session newSession() {
        return m_sessionFactory.openSession();
    }
}