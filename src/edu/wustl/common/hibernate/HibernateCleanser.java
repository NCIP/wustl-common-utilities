package edu.wustl.common.hibernate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionFactory;

public class HibernateCleanser {
    private Object obj;

    private Map<Collection<Object>, Collection<Object>> oldToNew;

    private Set<Object> cleanedObjects;

    private static final SessionFactory sessionFactory = DBUtil.getSessionFactory();

    public HibernateCleanser(Object obj) {
        this(obj, new HashMap<Collection<Object>, Collection<Object>>(), new HashSet<Object>());
    }

    private HibernateCleanser(Object obj, Map<Collection<Object>, Collection<Object>> oldToNew,
            Set<Object> cleanedObjects) {
        this.obj = obj;
        this.oldToNew = oldToNew;
        this.cleanedObjects = cleanedObjects;
    }

    public void clean() {
        if (obj instanceof Collection) {
            throw new IllegalArgumentException("can't clean a collection.");
        }
        if (cleanedObjects.contains(obj)) {
            return;
        }
        cleanedObjects.add(obj);
        Class<?> klass = obj.getClass();
        if (isPrimitive(klass)) {
            return;
        }

        while (klass != null) {
            processClass(klass);
            klass = klass.getSuperclass();
            break;
        }
    }

    private void processClass(Class<?> klass) {
        for (Field field : klass.getDeclaredFields()) {
            processField(field);
        }
    }

    private void processField(Field field) {
        Class<?> fType = field.getType();
        field.setAccessible(true);
        if (isPrimitive(fType)) {
            return;
        }
        if (fType.isArray()) {
            processArray();
            return;
        }
        Object member = getMemberValue(field);
        if (isCollection(fType)) {
            Collection<Object> newColl = processCollection((Collection<Object>) member);
            setMemberValue(field, newColl);
            return;
        }
        recursiveClean(member);
    }

    private boolean isPrimitive(Class<?> klass) {
        return klass.getName().startsWith("java.lang") || klass.isPrimitive() || klass.isEnum();
    }

    private boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    private Collection<Object> processCollection(Collection<Object> memberColl) {
        if (oldToNew.containsKey(memberColl)) {
            return (Collection<Object>) oldToNew.get(memberColl);
        }
        Collection<Object> coll = createCollection(memberColl.getClass());
        oldToNew.put(memberColl, coll);
        for (Object e : memberColl) {
            recursiveClean(e);
            coll.add(e);
        }
        return coll;
    }

    private Collection<Object> createCollection(Class<? extends Collection> collType) {
        if (Set.class.isAssignableFrom(collType)) {
            return new HashSet<Object>();
        } else if (List.class.isAssignableFrom(collType)) {
            return new ArrayList<Object>();
        }
        throw new UnsupportedOperationException("Collections of type " + collType + " cannot be cleansed.");
    }

    private void processArray() {
        throw new UnsupportedOperationException("array's are too dirty to be cleansed.");
    }

    private Object getMemberValue(Field field) {
        try {
            return field.get(obj);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setMemberValue(Field field, Collection<Object> newColl) {
        try {
            field.set(obj, newColl);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void recursiveClean(Object childObj) {
        new HibernateCleanser(childObj, oldToNew, cleanedObjects).clean();
    }
}
