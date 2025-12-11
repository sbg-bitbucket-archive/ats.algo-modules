package ats.algo.genericsupportfunctions.csvable;

import java.util.ArrayList;
import java.util.List;

import ats.algo.genericsupportfunctions.CsvAble;

/**
 * used by CSV unit test
 * 
 * @author gicha
 *
 */
public class CsvTestClass implements CsvAble {
    int a;
    int b;

    public CsvTestClass() {
        super();
    }

    public CsvTestClass(int a, int b) {
        super();
        this.a = a;
        this.b = b;
    }

    @Override
    public List<String> hdrs() {
        List<String> l = new ArrayList<>(2);
        l.add("a");
        l.add("b");
        return l;
    }

    @Override
    public List<String> rowContent() {
        List<String> l = new ArrayList<>(2);
        l.add(Integer.toString(a));
        l.add(Integer.toString(b));
        return l;
    }

    @Override
    public void initialiseFromRowContent(List<String> row) {
        a = Integer.parseInt(row.get(0));
        b = Integer.parseInt(row.get(1));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + a;
        result = prime * result + b;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CsvTestClass other = (CsvTestClass) obj;
        if (a != other.a)
            return false;
        if (b != other.b)
            return false;
        return true;
    }

}
