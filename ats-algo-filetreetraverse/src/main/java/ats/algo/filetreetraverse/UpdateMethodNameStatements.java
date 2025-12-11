package ats.algo.filetreetraverse;

public class UpdateMethodNameStatements extends AbstractProcessFile {

    private boolean fileModified;

    @Override
    public String processLine(String s) {

        int n = s.indexOf("");
        if (n > -1) {
            s = s.replace("", "");
            fileModified = true;
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
