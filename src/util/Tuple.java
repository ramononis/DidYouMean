package util;

/**
 * A better (immutable) tuple class that contains two values.
 *
 * @author Ramon
 */
public class Tuple<X, Y> {
    public final X x;
    public final Y y;

    /**
     * Initializes a Tuple.
     *
     * @param x   the first value it should have.
     * @param y   the secnd value it should have.
     */
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
}
