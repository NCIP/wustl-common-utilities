package edu.wustl.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectCloner {
    @SuppressWarnings("unchecked")
    public static <T> T clone(T obj) {
        if (!(obj instanceof Serializable)) {
            throw new IllegalArgumentException("Can't clone object of type " + obj.getClass()
                    + " since the class is not serializable.");
        }
        try {
            ByteArrayOutputStream f = new ByteArrayOutputStream();
            ObjectOutput s = new ObjectOutputStream(f);
            s.writeObject(obj);
            s.flush();
            s.close();
            ByteArrayInputStream in = new ByteArrayInputStream(f.toByteArray());
            f.close();
            ObjectInputStream sin = new ObjectInputStream(in);
            T res = (T) sin.readObject();
            in.close();
            sin.close();
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            // won't occur.
            throw new RuntimeException("Can't occur.");
        }
    }
}
