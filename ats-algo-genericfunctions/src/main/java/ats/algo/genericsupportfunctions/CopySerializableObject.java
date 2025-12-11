package ats.algo.genericsupportfunctions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Utility method for copying any serializable object
 * 
 * @author Geoff
 *
 */
public class CopySerializableObject {

    /**
     * Makes a copy of any object that is serializable
     * 
     * @param object the object to copy
     * @return the copy of the object
     * @throws IOException if can't write object - presumably because of a serialization error
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T copy(T object) {
        if (object == null)
            return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream out;
        T deserializedObject = null;
        try {
            out = new ObjectOutputStream(outputStream);
            out.writeObject(object);
            out.close();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream ois;
            ois = new ObjectInputStream(inputStream);
            deserializedObject = (T) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            /*
             * should be impossible for these exceptions to occur, given that we already have a source object of the
             * correct class
             */
            throw new IllegalArgumentException(String.format("Can't copy object of type %s.\n %s",
                            object.getClass().toString(), e.toString()));
        }
        return deserializedObject;
    }
}
