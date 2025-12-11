package ats.algo.filetreetraverse;

import java.io.File;
import java.io.IOException;

public class MainProgram {

    public static void main(String[] args) {
        String path = "C:\\Users\\gicha\\Dropbox\\GCFiles\\Eclipse\\algo20";
        // String path = "C:\\aatmp";
        System.out.println("*** Processing files ***");
        // FileTreeTraverse.traverse(path, (f, l) -> printName(f, l));
        // FileTreeTraverse.traverse(path, (f, l) -> updateJunitTestFile(f));
        // FileTreeTraverse.traverse(path, (f, l) -> cleanJUnitFiles(f));
        // FileTreeTraverse.traverse(path, (f, l) -> findIfs(f));
        FileTreeTraverse.traverse(path, (f, l) -> updateMethodNames(f));
        System.out.println("*** Finished ***");
    }

    @SuppressWarnings("unused")
    private static void updateJunit4To5(File file) {
        JUnit4to5ProcessFile converter = new JUnit4to5ProcessFile();
        if (file.isFile())
            try {
                FileUpdater.update(file, "java", converter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        System.out.println(file.getName());
    }

    @SuppressWarnings("unused")
    private static void cleanJUnitFiles(File file) {
        JUnitCleaner cleaner = new JUnitCleaner();
        if (file.isFile())
            try {
                FileUpdater.update(file, "java", cleaner);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @SuppressWarnings("unused")
    private static void findIfs(File file) {
        FindIfs cleaner = new FindIfs();
        if (file.isFile())
            try {
                FileUpdater.update(file, "java", cleaner);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private static void updateMethodNames(File file) {
        UpdateMethodNameStatements processor = new UpdateMethodNameStatements();
        if (file.isFile())
            try {
                FileUpdater.update(file, "java", processor);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


}
