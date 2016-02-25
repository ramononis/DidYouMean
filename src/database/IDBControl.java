package database;

import util.TupleStringInt;

import java.util.List;

/**
 * Interface for database control classes. Interacts with the database.
 *
 * @Author Tim
 */
public interface IDBControl {
    List<TupleStringInt> getData();

    /**
     * Calculates the weight that a search term should have.
     *
     * @param n the number of times the search term is used.
     * @param p the percentage of successful uses of this search term.
     * @return appropriate weight for the search term.
     */
    static int calcWeight(int n, double p) {
        return (int) Math.round(n * p);
    }
}
