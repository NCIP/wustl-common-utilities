package edu.wustl.common.hibernate;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.EntityMode;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

public class Metadata {
    private static final SessionFactory sessionFactory = DBUtil.getSessionFactory();

    private static final EntityMode ENTITY_MODE = EntityMode.POJO;

    private final Set<String> primitiveValues;

    private final Set<String> collectionsValues;

    private final Set<String> associatedValues;

    private final ClassMetadata classMetadata;

    private final Object obj;

    public Metadata(Object obj) {
        primitiveValues = new HashSet<String>();
        collectionsValues = new HashSet<String>();
        associatedValues = new HashSet<String>();
        this.obj = obj;
        classMetadata = getClassMetadata(obj.getClass());
        String[] names = classMetadata.getPropertyNames();
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            Type type = classMetadata.getPropertyType(name);
            Set<String> targetSet;
            if (type.isCollectionType()) {
                targetSet = collectionsValues;
            } else if (type.isEntityType()) {
                // TODO check this
                targetSet = associatedValues;
            } else {
                targetSet = primitiveValues;
            }
            targetSet.add(name);
        }
    }

    private static ClassMetadata getClassMetadata(Class<?> klass) {
        return sessionFactory.getClassMetadata(klass);
    }

    public Set<String> getAssociations() {
        return associatedValues;
    }

    public Set<String> getCollections() {
        return collectionsValues;
    }

    public Set<String> getPrimitives() {
        return primitiveValues;
    }

    public Object getValue(String name) {
        return classMetadata.getPropertyValue(obj, name, ENTITY_MODE);
    }

    public Type getType(String name) {
        return classMetadata.getPropertyType(name);
    }

    public void setValue(String name, Object value) {
        classMetadata.setPropertyValue(obj, name, value, ENTITY_MODE);
    }

    public void nullifyId() {
        classMetadata.setIdentifier(obj, null, ENTITY_MODE);
    }
}
