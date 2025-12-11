package ats.algo.filetreetraverse;

public class FindIfs extends AbstractProcessFile {

    private boolean foundIf;
    private String ifLine;
    int n;

    public FindIfs() {
        foundIf = false;
        n = 1;
    }

    @Override
    public String processLine(String s) {
        if (!foundIf) {
            foundIf = s.indexOf("if (") > -1;
            if (foundIf)
                ifLine = s;
        } else {
            if (s.indexOf("// System.out") > -1) {
                System.out.println(fileName + " line no: " + n);
                System.out.println(ifLine);
                System.out.println(s);
                System.out.println("");
            }
            foundIf = false;
        }
        n++;
        return s;
    }

    @Override
    public boolean mightModifyFile() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean fileModified() {
        return false;
    }



}
