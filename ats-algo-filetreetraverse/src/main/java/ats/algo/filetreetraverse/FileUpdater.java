package ats.algo.filetreetraverse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUpdater {
    /**
     * 
     * @param file - the source file which will be modified
     * @param fileType - the filter on fileType i.e. the file extension
     * @param processLine - the method to apply to each line
     * @throws IOException
     */
    /**
     * 
     * @param file
     * @param fileType
     * @param fileProcessor
     * @throws IOException
     */
    public static void update(File file, String fileType, AbstractProcessFile fileProcessor) throws IOException {
        String thisExt = "";
        String fName = file.getName();
        int i = fName.lastIndexOf('.');
        if (i > 0) {
            thisExt = fName.substring(i + 1);
        }
        if (!thisExt.equals(fileType))
            return;
        fileProcessor.setFileName(file.getName());

        BufferedReader br = new BufferedReader(new FileReader(file));
        if (fileProcessor.mightModifyFile()) {
            // Construct a new file that may later be renamed to the original
            // filename.
            File tempFile = new File(file.getCanonicalPath() + ".temp");
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;
            // Read from the original file and write to the new
            while ((line = br.readLine()) != null) {
                String updatedLine = fileProcessor.processLine(line);
                if (updatedLine != null) {
                    pw.println(updatedLine);
                    pw.flush();
                }
            }
            pw.close();
            br.close();
            if (fileProcessor.fileModified()) {
                // Delete the original file
                if (!file.delete()) {
                    System.out.println("Could not delete file");
                    return;
                }
                // Rename the new file to the original filename
                if (!tempFile.renameTo(file))
                    System.out.println("Could not rename file");
            } else {
                if (!tempFile.delete()) {
                    System.out.println("Could not delete temp file");
                }
            }
        } else {
            /*
             * read-only process so just ivoke the line method
             */
            String line;
            while ((line = br.readLine()) != null) {
                fileProcessor.processLine(line);
            }
            br.close();
        }
    }

}
