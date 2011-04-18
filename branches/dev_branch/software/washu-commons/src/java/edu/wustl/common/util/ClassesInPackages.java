package edu.wustl.common.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Finds all classes that belong to specified packages.
 *
 * (Code is modification of source that was found at
 * http://forums.sun.com/thread.jspa?threadID=341935&start=15. See comment by
 * jonthefathead on Jul 26, 2007 5:42 PM).
 *
 * @author srinath_k
 */
public class ClassesInPackages {

    /**
     * @param oneOnly specifies whether to look only in one resource (true) or
     *            ALL resources (false) of the {@link ClassLoader} (determines
     *            whether {@link ClassLoader#getResource(String)} or
     *            {@link ClassLoader#getResources(String)}, respectively, is to
     *            be called)
     * @param packageNames packages whose classes are needed.
     * @return all classes in specified packages.
     */
    public static List<Class<?>> classesInPackages(boolean oneResourceOnly, String... packageNames) {
        try {
            List<Class<?>> res = new LinkedList<Class<?>>();
            findAllClassLocations(oneResourceOnly, res, packageNames);
            return res;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calls {@link ClassesInPackages#classesInPackages(boolean, String[])} with
     * <code>oneResourceOnly = true</code>
     */
    public static List<Class<?>> classesInPackages(String... packageNames) {
        return classesInPackages(true, packageNames);
    }

    private static void findAllClassLocations(boolean oneOnly, List<Class<?>> res, String[] packageNames)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        for (String packageName : packageNames) {
            String path = packageName.replace('.', '/');
            if (oneOnly) {
                URL resource = classLoader.getResource(path);
                if (resource == null) {
                    throw new ClassNotFoundException("No resource for " + path);
                }
                searchResource(packageName, resource, res);
            } else {
                Enumeration<URL> resources = classLoader.getResources(path);
                if (resources == null || !resources.hasMoreElements()) {
                    throw new ClassNotFoundException("No resource for " + path);
                }

                while (resources.hasMoreElements()) {
                    URL resource = resources.nextElement();
                    searchResource(packageName, resource, res);
                }
            }
        }
    }

    private static void searchResource(String packageName, URL resource, List<Class<?>> res) throws IOException,
            ClassNotFoundException {

    	if(resource.getProtocol().equalsIgnoreCase("vfszip"))
    	{
    		  loadVfszip(packageName, resource, res);
//    		URI uri = null;
//    		if (!resource.toString().startsWith("vfsfile:/")) {
//    		try {
//				uri = resource.toURI();
//			} catch (URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//    		try {
//				uri=new  URI(resource.toString().substring(3));
//				new URI(resource.toString().substring(3));
//			} catch (URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    		}
//    		 String file=  resource.getFile().substring(1);
//    		loadDirectory(packageName,  file , res);
            // Used with JBoss 5.x: trim prefix "vfs"

//
//    		 try {
//				URI uri = new  URI(resource.toString().substring(3));
//			} catch (URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    	}
    	else if (resource.getProtocol().equalsIgnoreCase("FILE")) {
            loadDirectory(packageName, resource.getFile(), res);
        } else if (resource.getProtocol().equalsIgnoreCase("JAR")) {
            loadJar(packageName, resource, res);
        } else {
            throw new ClassNotFoundException("Unknown protocol on class resource: " + resource.toExternalForm());
        }
    }

    private static void loadVfszip(String packageName, URL resource,
			List<Class<?>> res) throws IOException, ClassNotFoundException {

    	String[] file=  resource.getFile().substring(1).split("/WEB-INF/classes/edu/wustl/catissuecore/bizlogic/uidomain/");
        ZipFile zipFile = new  ZipFile(file[0]);
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if ((entry.getName().startsWith("edu/wustl/catissuecore/bizlogic/uidomain") || entry.getName()
                    .startsWith("WEB-INF/classes/" + "edu/wustl/catissuecore/bizlogic/uidomain"))
                    && entry.getName().endsWith(".class")) {

                String className = entry.getName();
                if (className.startsWith("/"))
                    className = className.substring(1);
                className = className.replace('/', '.');

                className = className.substring(0, className.length() - ".class".length());
                String[] classes=className.split("WEB-INF.classes.");
                res.add(Class.forName(classes[1]));
            }
        }

//        JarURLConnection conn = (JarURLConnection) resource.openConnection();
//        JarFile jarFile = conn.getJarFile();
//        Enumeration<JarEntry> entries = jarFile.entries();
//        String packagePath = packageName.replace('.', '/');
//
//        while (entries.hasMoreElements()) {
//            JarEntry entry = entries.nextElement();
//            if ((entry.getName().startsWith(packagePath) || entry.getName()
//                    .startsWith("WEB-INF/classes/" + packagePath))
//                    && entry.getName().endsWith(".class")) {
//
//                String className = entry.getName();
//                if (className.startsWith("/"))
//                    className = className.substring(1);
//                className = className.replace('/', '.');
//
//                className = className.substring(0, className.length() - ".class".length());
//                res.add(Class.forName(className));
//            }
//        }

	}

	private static void loadJar(String packageName, URL resource, List<Class<?>> res) throws IOException,
            ClassNotFoundException {
        JarURLConnection conn = (JarURLConnection) resource.openConnection();
        JarFile jarFile = conn.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        String packagePath = packageName.replace('.', '/');

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if ((entry.getName().startsWith(packagePath) || entry.getName()
                    .startsWith("WEB-INF/classes/" + packagePath))
                    && entry.getName().endsWith(".class")) {

                String className = entry.getName();
                if (className.startsWith("/"))
                    className = className.substring(1);
                className = className.replace('/', '.');

                className = className.substring(0, className.length() - ".class".length());
                res.add(Class.forName(className));
            }
        }

    }

    private static void loadDirectory(String packageName, String fullPath, List<Class<?>> res) throws IOException,
            ClassNotFoundException {
        File directory = new File(fullPath);
        if (!directory.isDirectory())
            throw new IOException("Invalid directory " + directory.getAbsolutePath());

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory())
                loadDirectory(packageName + '.' + file.getName(), file.getAbsolutePath(), res);
            else if (file.getName().endsWith(".class")) {
                String simpleName = file.getName();
                simpleName = simpleName.substring(0, simpleName.length() - ".class".length());
                String className = String.format("%s.%s", packageName, simpleName);
                res.add(Class.forName(className));
            }
        }
    }
}
