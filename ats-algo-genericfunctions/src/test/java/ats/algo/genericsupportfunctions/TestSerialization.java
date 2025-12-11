package ats.algo.genericsupportfunctions;


import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestSerialization {

    /**
     * 
     */

    @Test
    public void test1() {
        List<TestClassToSerialize> obj;
        obj = new ArrayList<TestClassToSerialize>();
        TestClassToSerialize x;
        for (int i = 0; i < 3; i++) {
            x = new TestClassToSerialize(i, "Test_" + i);
            obj.add(x);
        }
        try {
            FileOutputStream fileOut = new FileOutputStream("/aatmp/test.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(obj);
            out.close();
            fileOut.close();
            System.out.println("\nSerialization Successful... Checkout your specified output file..\n");

        } catch (FileNotFoundException e) {
            System.out.println("\nFile not found exception.\n");
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            System.out.println("\nIO exception.\n");
            e.printStackTrace();
            fail();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test2() {
        ObjectInputStream objectinputstream = null;
        List<TestClassToSerialize> matchTable = null;
        try {
            FileInputStream streamIn = new FileInputStream("/aatmp/test.ser");
            objectinputstream = new ObjectInputStream(streamIn);
            matchTable = (List<TestClassToSerialize>) objectinputstream.readObject();

        } catch (Exception e) {
            System.out.println("Exception");
            fail();
        } finally {
            if (objectinputstream != null) {
                try {
                    objectinputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        TestClassToSerialize x = matchTable.get(1);
        assertEquals(1, x.n);
    }
}


