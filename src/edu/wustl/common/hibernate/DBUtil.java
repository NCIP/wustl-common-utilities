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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.util.XMLHelper;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * <p>
 * Title: DBUtil Class>
 * <p>
 * Description: Utility class provides database specific utilities methods
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 */
public class DBUtil {
    // A factory for DB Session which provides the Connection for client.
    private static SessionFactory m_sessionFactory;

    // ThreadLocal to hold the Session for the current executing thread.
    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

    // Initialize the session Factory in the Static block.
    // Initialize the session Factory in the Static block.
    private static EntityResolver entityResolver = XMLHelper.DEFAULT_DTD_RESOLVER;

    static {

        Configuration cfg = new Configuration();

        InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream(
                                                                                    "dbutil.properties");
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
            fileNames = new String[] { "hibernate.cfg.xml" };
        }
        // get all configuration files
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            fileName = fileName.trim();
            System.out.println(fileName + ": fileName");
            addConfigurationFile(fileName, cfg, entityResolver);
        }

        m_sessionFactory = cfg.buildSessionFactory();

    }

    /**
     * This method adds configuration file to Hibernate Configuration.
     * @param fileName
     *            name of the file that needs to be added
     * @param cfg
     *            Configuration to which this file is added.
     */
    private static void addConfigurationFile(String fileName,
                                             Configuration cfg,
                                             EntityResolver entityResolver) {
        try {
            InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream(
                                                                                        fileName);
            List errors = new ArrayList();
            // hibernate api to read configuration file and convert it to
            // Document(dom4j) object.
            XMLHelper xmlHelper = new XMLHelper();
            Document document = xmlHelper.createSAXReader(fileName, errors,
                                                          entityResolver).read(
                                                                               new InputSource(
                                                                                       inputStream));
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
     * @return Returns the current db session.
     */
    public static Session currentSession() throws HibernateException {
        Session s = (Session) threadLocal.get();

        // Open a new Session, if this Thread has none yet
        if (s == null) {
            s = m_sessionFactory.openSession();
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
}