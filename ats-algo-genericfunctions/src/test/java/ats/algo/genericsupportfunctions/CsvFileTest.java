package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ats.algo.genericsupportfunctions.csvable.CsvTestClass;

public class CsvFileTest {


    @Test
    public void testWriteThenRead() {
        List<CsvTestClass> rows = new ArrayList<>(2);
        rows.add(new CsvTestClass(3, 5));
        rows.add(new CsvTestClass(4, 6));
        rows.add(new CsvTestClass(7, 8));
        CsvFile.writeCsvfile("testCsv.csv", rows);
        List<CsvAble> rows2 = CsvFile.readCsvFile("testCsv.csv", CsvTestClass.class);
        assertEquals(rows, rows2);
    }


}
