package api.database;

import java.io.IOException;
import java.util.Map;

/**
 * Interface for api.database control classes. Interacts with some type of api.database.
 *
 * @author Tim Blok, Frans van Dijk, Yannick Mijsters, Ramon Onis, Tim Sonderen; University of Twente
 */
public interface IDBControl {
    /**
     * Calculates the weight that a search term should have.
     * This weight is calculated as the number of times a search term has been used multiplied by the percentage that that search term was successful.
     *
     * @param n the number of times the search term is used.
     * @param p the percentage of successful uses of this search term.
     * @return appropriate weight for the search term.
     */
    static int calcWeight(int n, double p) {
        return (int) (n * p);
    }

//    static HashMap<String, Integer> optionize(HashMap<String, Integer> singleData) { // TODO: check if scores are still realistic after this
//        HashMap<String, Integer> optionizedData = new HashMap<>();
//        for (String s : singleData.keySet()) {
//            String[] words = s.split(" ");
//            String[][] temp = {};
//            for (int i = 0; i < words.length; i++){
//                for (int j = 0; i < words.length; i++){
//
//                }
//            }
//
//        }
//    }

    /**
     * Used to get the data from the api.database.
     *
     * @return a HashMap with the search term (null-terminated String) as key and the weight (int) as the value.
     * @throws if reading the data raised an exception
     */
    Map<String, Integer> getData() throws IOException;

}
