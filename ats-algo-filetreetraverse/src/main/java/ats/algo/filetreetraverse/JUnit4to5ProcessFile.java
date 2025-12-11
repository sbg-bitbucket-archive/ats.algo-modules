package ats.algo.filetreetraverse;

public class JUnit4to5ProcessFile extends AbstractProcessFile {

    boolean importStatementsWritten;


    @Override
    public String processLine(String line) {
        /*
         * look for org.junit import statements
         */
        if (line.indexOf("import") > -1 && line.indexOf("org.junit") > -1) {
            if (importStatementsWritten)
                return null;
            importStatementsWritten = true;
            System.out.println("Modifying: " + super.fileName);
            return "import org.junit.jupiter.api.*;\nimport static org.junit.jupiter.api.Assertions.*;";
        }
        if (importStatementsWritten) {
            String opLine = line;
            opLine = opLine.replace("@Before", "@BeforeEach");
            opLine = opLine.replace("@After", "@BeforeEach");
            opLine = opLine.replace("@BeforeClass", "@BeforeAll");
            opLine = opLine.replace("@AfterClass", "@AfterAll");
            opLine = opLine.replace("@Ignore", "@Disabled");
            return opLine;
        }
        return line;
    }

    @Override
    public boolean mightModifyFile() {
        return true;
    }

    @Override
    public boolean fileModified() {
        return importStatementsWritten;
    }



}
