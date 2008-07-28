package edu.wustl.common.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.type.BagType;
import org.hibernate.type.ListType;
import org.hibernate.type.SetType;
import org.hibernate.type.Type;

public class HibernateCleanser {
    private final Object obj;

    private final Map<Collection<Object>, Collection<Object>> oldToNew;

    private final Set<Object> cleanedObjects;

    private final Metadata metadata;

    public HibernateCleanser(Object obj) {
        this(obj, new HashMap<Collection<Object>, Collection<Object>>(), new HashSet<Object>());
    }

    private HibernateCleanser(Object obj, Map<Collection<Object>, Collection<Object>> oldToNew,
            Set<Object> cleanedObjects) {
        this.obj = obj;
        this.oldToNew = oldToNew;
        this.cleanedObjects = cleanedObjects;
        this.metadata = new Metadata(obj);
    }

    public void clean() {
        if (obj instanceof Collection) {
            throw new IllegalArgumentException("can't clean a collection.");
        }
        if (cleanedObjects.contains(obj)) {
            return;
        }
        cleanedObjects.add(obj);
        processIdentifier();
        processCollections();
        processAssociations();
    }

    private void processIdentifier() {
        metadata.nullifyId();
    }

    private void processAssociations() {
        for (String name : metadata.getAssociations()) {
            // TODO check proxies??
            recursiveClean(metadata.getValue(name));
        }
    }

    private void processCollections() {
        for (String name : metadata.getCollections()) {
            Collection<Object> newColl = newCollection(name);
            metadata.setValue(name, newColl);
        }
    }

    private Collection<Object> newCollection(String name) {
        Collection<Object> old = (Collection<Object>) metadata.getValue(name);
        if (oldToNew.containsKey(old)) {
            return oldToNew.get(old);
        }
        Collection<Object> res = createCollectionForType(metadata.getType(name));
        oldToNew.put(res, old);
        for (Object e : old) {
            recursiveClean(e);
            res.add(e);
        }
        return res;
    }

    private Collection<Object> createCollectionForType(Type collType) {
        if (collType instanceof SetType) {
            return new HashSet<Object>();
        } else if (collType instanceof ListType || collType instanceof BagType) {
            return new ArrayList<Object>();
        }
        throw new UnsupportedOperationException("Collections of type " + collType + " cannot be cleansed.");
    }

    private void recursiveClean(Object childObj) {
        new HibernateCleanser(childObj, oldToNew, cleanedObjects).clean();
    }

}
