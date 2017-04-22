package api.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Controls the api.database. Uses CSV files generated by google analytics. These CSV files should contain the following in the following order:
 * Search Term,Total Unique Searches,Results Pageviews / Search,% Search Exits,% Search Refinements,Time after Search,Average Search Depth.
 * These CSV files are edited in such a way that only the actual data is left. the comments are out, as well as the names of the columns and the day index, total unique searches part.
 *
 * @author Tim Blok, Frans van Dijk, Yannick Mijsters, Ramon Onis, Tim Sonderen; University of Twente
 */
public class CSVControl implements IDBControl {
    private String[] paths;

    /**
     * Initializes a new CSVControl.
     *
     * @param paths a {@link String[]} that represent paths to (cleaned) CSV files
     */
    public CSVControl(String[] paths) {
        this.paths = paths;
    }

    /**
     * Provides the data from the CSV files in the correct format using {@link #readCSV(String)}, {@link #filter(Set)} and {@link #process(Set)}
     *
     * @return a Hashmap with the search term as key and its weight as value.
     */
    @Override
    public Map<String, Integer> getData() throws IOException {
        Set<String> rawData = new HashSet<>();

        for (String file : paths) {
            rawData.addAll(readCSV(file));
        }

        Set<String[]> filteredData = filter(rawData);

        return process(filteredData);
    }

    /**
     * Processes the data provided by {@link #filter(Set)}.
     *
     * @param filteredData a set of string arrays which have a length of 3. Of the array the first element is the search term,
     *                     the second the number of times it was searched and the third the percentage of search refinements.
     * @return a {@link Map Map} with the search term (null-terminated string) as key and its weight as value.
     */
    private Map<String, Integer> process(Set<String[]> filteredData) {
        Map<String, Integer> data = new HashMap<>();

        int skipped = 0;

        for (String[] dl : filteredData) {
            try {
                int n = Integer.parseInt(dl[1].replace("\"", "").replace(",", ""));
                double r = Double.parseDouble(dl[2].replace("%", ""));
                double p = 100 - r;
                int weight = IDBControl.calcWeight(n, p);
                dl[0] = dl[0].toLowerCase();
                if (dl[0].contains("\"")) {
                    dl[0] = dl[0].substring(1, dl[0].length() - 1).replace("\"\"", "\"");
                }
                if (data.containsKey(dl[0])) {
                    weight += data.get(dl[0]);
                }
                data.put(dl[0], weight);
            } catch (Exception e) {
                skipped++;
            }
        }

        System.out.println(skipped + " lines skipped while processing data.");

        return data;
    }

    /**
     * Filters the data provided by {@link #readCSV(String)}.
     *
     * @param rawData a set of strings where each string is one line of the CSV file.
     * @return a set of string arrays which have a length of 3. Of the array the first element is the search term, the second the number of times it was searched and the third the percentage of search refinements.
     */
    private Set<String[]> filter(Set<String> rawData) {
        final String SPLITTER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"; // CSV = Comma Separated Values
        Set<String[]> filteredData = new HashSet<>();
        int skipped = 0;

        for (String line : rawData) {
            String[] splitLine = line.split(SPLITTER);
            if (splitLine.length == 7) filteredData.add(new String[]{splitLine[0], splitLine[1], splitLine[4]});
            else skipped++;
        }

        System.out.println(skipped + " lines skipped while filtering data");

        return filteredData;
    }

    /**
     * Reads a CSV file to a list of strings.
     *
     * @param path the path to the CSV file that should be read.
     * @return a set of strings where each string is one line of the CSV file.
     * @throws IOException if reading the file raisen an exception
     */
    private Set<String> readCSV(String path) throws IOException {
        String line;
        Set<String> rawData = new HashSet<>();

        BufferedReader br = new BufferedReader(new FileReader(path));
        while (null != (line = br.readLine())) {
            rawData.add(line);
        }
        return rawData;
    }
}