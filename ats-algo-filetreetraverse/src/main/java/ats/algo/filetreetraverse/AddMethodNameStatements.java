package ats.algo.filetreetraverse;


public class AddMethodNameStatements extends AbstractProcessFile {

    private boolean fileModified;
    private boolean testFound;
    private boolean importStatementWritten;

    @Override
    public String processLine(String s) {

        if (!importStatementWritten) {
            int n = s.indexOf("import");
            if (n > -1) {
                s = "import ats.algo.genericsupportfunctions.MethodName;" + "\n" + s;
                importStatementWritten = true;
            }
            return s;
        }

        if (!testFound) {
            /*
             * look for row containing @Test
             */
            int n = s.indexOf("@Test");
            if (n > -1) {
                if (!fileModified)
                    System.out.println("Modifying: " + super.fileName);
                fileModified = true;
                testFound = true;
            }
        } else {
            /*
             * found @Test. add the extra statement in after the next line
             */
            s = s + "\n" + "        MethodName.print();";
            testFound = false;
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
