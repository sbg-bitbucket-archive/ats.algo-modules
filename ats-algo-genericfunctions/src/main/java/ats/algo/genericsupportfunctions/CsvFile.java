package ats.algo.genericsupportfunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CsvFile {

    private static final String csvSplitBy = ",";

    /**
     * writes the contents of the list to a csv file
     * 
     * @param path
     * @param rows
     * @return
     */
    public static boolean writeCsvfile(String path, List<? extends CsvAble> rows) {
        /*
         * get the header contents by using the getHdrs method on any instance of the csvable object
         */
        CsvAble anyRow = rows.get(0);
        if (anyRow == null)
            return false; // nothing to write
        List<String> hdrs = anyRow.hdrs();
        StringBuilder b = new StringBuilder();
        for (String hdr : hdrs) {
            b.append(hdr).append(",");
        }
        b.deleteCharAt(b.length() - 1);
        b.append("\n");
        String hdrRow = b.toString();

        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            bw.write(hdrRow);
            for (CsvAble row : rows) {
                StringBuilder b2 = new StringBuilder();
                for (String cell : row.rowContent()) {
                    b2.append(cell).append(",");
                }
                b2.deleteCharAt(b2.length() - 1);
                b2.append("\n");
                bw.write(b2.toString());
            }
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                return false;
            }
        }
        return true;
    }

    public static List<CsvAble> readCsvFile(String path, Class<? extends CsvAble> clazz) {
        BufferedReader br = null;
        List<CsvAble> list = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(path));
            br.readLine(); // discard the header row
            String row;
            while ((row = br.readLine()) != null) {
                List<String> rowContent = Arrays.asList(row.split(csvSplitBy));
                for (String cell : rowContent)
                    cell = cell.trim();
                CsvAble newObject = clazz.newInstance();
                newObject.initialiseFromRowContent(rowContent);
                list.add(newObject);
            }

        } catch (IOException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

}
