package database;

import util.TupleStringInt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Controls the database. Uses CSV files generated by google analytics. These CSV files should contain the following in the following order:
 * Search Term,Total Unique Searches,Results Pageviews / Search,% Search Exits,% Search Refinements,Time after Search,Average Search Depth.
 * These CSV files are edited in such a way that only the actual data is left. the comments are out, as well as the names of the columns and the day index, total unique searches part.
 *
 * @author Tim
 */
public class CSVControl implements IDBControl {
    private static final String DATALOCATION = "./csv/";
    private static final String[] FILENAMES = {"Data1.csv", "Data2.csv", "Data3.csv", "Data4.csv"};

    /**
     * Provides the data from the CSV files in the correct format.
     *
     * @return a hashmap<String, Integer> with the search term as key and its weight as value.
     */
    @Override
    public HashMap<String, Integer> getData() {
        Set<String> rawData = new HashSet<String>();

        for (String file : FILENAMES) {
            rawData.addAll(readCSV(DATALOCATION + file));
        }

        Set<String[]> filteredData = filter(rawData);

        HashMap<String, Integer> data = process(filteredData);

        return data;
    }

    /**
     * Processes the data provided by filter().
     *
     * @param filteredData a set of string arrays which have a length of 3. Of the array the first element is the search term, the second the number of times it was searched and the third the percentage of search refinements.
     * @return a hashmap<String, Integer> with the search term as key and its weight as value.
     */
    private HashMap<String, Integer> process(Set<String[]> filteredData) {
        HashMap<String, Integer> data = new HashMap<>();

        for (String[] dl : filteredData) {
            int n = 0;
            try {
                n = Integer.parseInt(dl[1].replace("\"", "").replace(",", ""));
            } catch (NumberFormatException e) {
                System.out.println();
            }
            double r = Double.parseDouble(dl[2].replace("%", ""));
            double p = 100 - r;
            int weight = IDBControl.calcWeight(n, p);

            data.put(dl[0].replace("\"", ""), weight);
        }

        return data;
    }

    /**
     * Filters the data provided by readCSV().
     *
     * @param rawData a set of strings where each string is one line of the CSV file.
     * @return a set of string arrays which have a length of 3. Of the array the first element is the search term, the second the number of times it was searched and the third the percentage of search refinements.
     */
    private Set<String[]> filter(Set<String> rawData) {
        final String SPLITTER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"; // CSV = Comma Separated Values
        Set<String[]> filteredData = new HashSet<>();

        for (String line : rawData) {
            String[] splitLine = line.split(SPLITTER);
            filteredData.add(new String[]{splitLine[0], splitLine[1], splitLine[4]});
        }

        return filteredData;
    }

    /**
     * Reads a CSV file to a list of strings.
     *
     * @param path the path to the CSV file that should be read.
     * @return a set of strings where each string is one line of the CSV file.
     */
    private Set<String> readCSV(String path) {
        BufferedReader br = null;
        String line = "";
        Set<String> rawData = new HashSet<>();

        try {
            br = new BufferedReader(new FileReader(path));

            while ((line = br.readLine()) != null) {
                rawData.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error in CSVControl#readCSV");
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("Error in CSVControl#readCSV");
                    e.printStackTrace();
                }
            }
        }
        return rawData;
    }
}
