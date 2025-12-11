package ats.algo.genericsupportfunctions;

import java.util.List;

/**
 * if a class supports this i/f then a List of them can be written/read in csv file format
 * 
 * Note the class also MUST have a constructor that takes no params
 * 
 * @author gicha
 *
 */
public interface CsvAble {



    /**
     * gets the list of column hdrs to insert into the csv file
     * 
     * @return
     */
    public List<String> hdrs();

    /**
     * gets the content of the row to insert into the csv file, properties must be in the same order as the headers
     * 
     * @return
     */
    public List<String> rowContent();

    /**
     * initialises object properties from the row contents. Only needed if going to reading a csv file. Properties must
     * be int he same order as the headers
     * 
     * @param row
     * @return
     */
    public void initialiseFromRowContent(List<String> row);

}
