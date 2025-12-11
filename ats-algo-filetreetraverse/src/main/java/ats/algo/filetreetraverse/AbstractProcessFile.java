package ats.algo.filetreetraverse;

public abstract class AbstractProcessFile {

    protected String fileName;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * called for each line in the file. returns the possibly modified outline line, or null if line is to be deleted.
     * 
     * @param s the input line from a file
     * @return the possibly modified output line, or null if line is to be deleted
     */
    public abstract String processLine(String s);

    /**
     * should return true if this file might be modified. False if the subclass will not modify the file content
     * 
     * @return
     */
    public abstract boolean mightModifyFile();

    /**
     * returns true if any changes have been made to the file currently being processed
     * 
     * @return
     */
    public abstract boolean fileModified();

}
