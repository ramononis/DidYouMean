package database;

import java.util.HashMap;

/**
 * Interface for database control classes. Interacts with some type of database.
 *
 * @author Tim
 */
public interface IDBControl {
    /**
     * Used to get the data from the database.
     *
     * @return a HashMap with the search term (null-terminated String) as key and the weight (int) as the value.
     */
    HashMap<String, Integer> getData();

    /**
     * Calculates the weight that a search term should have.
     * This weight is calculated as the number of times a search term has been used multiplied by the percentage that that search term was successful.
     *
     * @param n the number of times the search term is used.
     * @param p the percentage of successful uses of this search term.
     * @return appropriate weight for the search term.
     */
    static int calcWeight(int n, double p) {
        return (int) Math.round(n * p);
    }
}
