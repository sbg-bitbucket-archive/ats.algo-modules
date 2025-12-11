package ats.algo.filetreetraverse;

public class JUnitCleaner extends AbstractProcessFile {

    private boolean containsUnitTests;
    private boolean fileModified;
    private boolean multiLineStatementInProgress;

    @Override
    public String processLine(String s) {
        if (!containsUnitTests) {
            /*
             * do nothing until we find a line which indicates this file contains unit tests
             */
            int n = s.indexOf("import org.junit");
            containsUnitTests = n > -1;
            return s;
        }
        /*
         * below only applies if this is a unit test file
         */
        if (multiLineStatementInProgress) {
            /*
             * look for continuation lines and comment them out until find a line with a ";"
             */
            s = "//" + s;
            int m = s.indexOf(";");
            multiLineStatementInProgress = m == -1;
        } else {
            int n = s.indexOf("System.out");
            if (n > -1) {
                if (!fileModified)
                    System.out.println("Modifying: " + super.fileName);
                fileModified = true;
                s = s.replace("System.out", "// System.out");
                int m = s.indexOf(";");
                multiLineStatementInProgress = m == -1;
            }
            int m = s.indexOf("LogUtil.initConsoleLogging");
            if (m > -1) {
                if (!fileModified)
                    System.out.println("Modifying: " + super.fileName);
                fileModified = true;
                s = s.replace("LogUtil.initConsoleLogging", "// LogUtil.initConsoleLogging");
            }
        }
        return s;
    }

    @Override
    public boolean fileModified() {
        return fileModified;
    }

    @Override
    public boolean mightModifyFile() {
        return true;
    }

}
