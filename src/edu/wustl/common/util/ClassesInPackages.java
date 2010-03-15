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

public class ClassesInPackages {

    public static List<Class<?>> classesInPackages(boolean oneOnly, String... packageNames) {
        try {
            return findAllClassLocations(oneOnly, packageNames);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Class<?>> classesInPackages(String... packageNames) {
        return classesInPackages(true, packageNames);
    }

    private static List<Class<?>> findAllClassLocations(boolean oneOnly, String... packageNames)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<Class<?>> classes = new LinkedList<Class<?>>();

        for (String packageName : packageNames) {
            String path = packageName.replace('.', '/');
            if (oneOnly) {
                URL resource = classLoader.getResource(path);
                if (resource == null) {
                    throw new ClassNotFoundException("No resource for " + path);
                }
                classes.addAll(getClasses(resource, packageName));
            } else {
                Enumeration<URL> resources = classLoader.getResources(path);
                if (resources == null || !resources.hasMoreElements()) {
                    throw new ClassNotFoundException("No resource for " + path);
                }

                while (resources.hasMoreElements()) {
                    URL resource = resources.nextElement();
                    classes.addAll(getClasses(resource, packageName));
                }
            }
        }

        return classes;
    }

    private static List<Class<?>> getClasses(URL resource, String packageName) throws IOException,
            ClassNotFoundException {
        List<Class<?>> classes = new LinkedList<Class<?>>();
        if (resource.getProtocol().equalsIgnoreCase("FILE")) {
            classes.addAll(loadDirectory(packageName, resource.getFile()));
        } else if (resource.getProtocol().equalsIgnoreCase("JAR")) {
            classes.addAll(loadJar(packageName, resource));
        } else {
            throw new ClassNotFoundException("Unknown protocol on class resource: " + resource.toExternalForm());
        }
        return classes;
    }

    private static List<Class<?>> loadJar(String packageName, URL resource) throws IOException, ClassNotFoundException {
        JarURLConnection conn = (JarURLConnection) resource.openConnection();
        JarFile jarFile = conn.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        String packagePath = packageName.replace('.', '/');

        List<Class<?>> classes = new LinkedList<Class<?>>();
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
                classes.add(Class.forName(className));
            }
        }
        return classes;

    }

    private static List<Class<?>> loadDirectory(String packageName, String fullPath) throws IOException,
            ClassNotFoundException {
        File directory = new File(fullPath);
        List<Class<?>> classes = new LinkedList<Class<?>>();
        if (!directory.isDirectory())
            throw new IOException("Invalid directory " + directory.getAbsolutePath());

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory())
                classes.addAll(loadDirectory(packageName + '.' + file.getName(), file.getAbsolutePath()));
            else if (file.getName().endsWith(".class")) {
                String simpleName = file.getName();
                simpleName = simpleName.substring(0, simpleName.length() - ".class".length());
                String className = String.format("%s.%s", packageName, simpleName);
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }
}
