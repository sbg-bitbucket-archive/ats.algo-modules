package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import org.junit.Test;

public class SplitCamelCaseTest {

    @Test
    public void test() {
        String[] tests = {"lowercase", // [lowercase]
                "Class", // [Class]
                "MyClass", // [My Class]
                "HTML", // [HTML]
                "PDFLoader", // [PDF Loader]
                "AString", // [A String]
                "SimpleXMLParser", // [Simple XML Parser]
                "GL11Version", // [GL 11 Version]
                "99Bottles", // [99 Bottles]
                "May5", // [May 5]
                "BFG9000", // [BFG 9000]
        };
        for (String test : tests) {
            System.out.println("[" + SplitCamelCase.split(test) + "]");
        }
        String s = SplitCamelCase.split("onServePctA1");
        System.out.println(s);
        assertEquals(s, "on Serve Pct A 1");
    }

}
