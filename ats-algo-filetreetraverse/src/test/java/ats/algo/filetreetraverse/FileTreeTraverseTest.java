package ats.algo.filetreetraverse;

import java.io.File;
import java.io.IOException;

import org.junit.Test;


public class FileTreeTraverseTest {

    // @Test
    public void printTreeToConsole() {
        String path = "C:\\Users\\gicha\\Dropbox\\GCFiles\\Eclipse\\algoBase";
        // System.out.println("**********************************************");
        // System.out.println("Files from main directory : " + path);
        // System.out.println("**********************************************");
        FileTreeTraverse.traverse(path, (f, l) -> printName(f, l));
    }

    @Test
    public void updateJunitfileTest() {
        String path = "C:\\aatmp\\junit2.java";
        File file = new File(path);
        JUnit4to5ProcessFile converter = new JUnit4to5ProcessFile();
        try {
            FileUpdater.update(file, "java", converter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void jUnitCleanerTest() {
        String path = "C:\\aatmp\\junit2.java";
        File file = new File(path);
        JUnitCleaner converter = new JUnitCleaner();
        try {
            FileUpdater.update(file, "java", converter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private static void printName(File file, int level) {
        for (int i = 0; i < level; i++)
            System.out.print("  ");
        if (file.isFile())
            System.out.println(file.getName());
        else {
            System.out.println("[" + file.getName() + "]");
        }
    }

}
