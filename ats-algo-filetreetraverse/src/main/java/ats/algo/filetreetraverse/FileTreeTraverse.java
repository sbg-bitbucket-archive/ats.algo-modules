package ats.algo.filetreetraverse;

import java.util.function.BiConsumer;
import java.io.File;

public class FileTreeTraverse {

    private static void traverseFileTree(File[] arr, int index, int level, BiConsumer<File, Integer> action) {
        // terminate condition
        if (index == arr.length)
            return;
        action.accept(arr[index], level);
        if (arr[index].isDirectory()) {
            traverseFileTree(arr[index].listFiles(), 0, level + 1, action);
        }
        traverseFileTree(arr, ++index, level, action);
    }

    /**
     * examines every file and folder in the tree within the stated path and executes the supplied action
     * 
     * @param path Starting point for the traverse
     * @param action function to apply to each file/folder
     */
    public static void traverse(String path, BiConsumer<File, Integer> action) {
        File maindir = new File(path);
        if (maindir.exists() && maindir.isDirectory()) {
            File arr[] = maindir.listFiles();
            traverseFileTree(arr, 0, 0, action);
        }
    }



}
