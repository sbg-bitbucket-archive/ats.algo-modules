package ats.algo.genericsupportfunctions;

import java.io.Serializable;

public class TestClassToSerialize implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public int n;
    public String s;

    TestClassToSerialize(int n, String s) {
        this.n = n;
        this.s = s;
    }
}

