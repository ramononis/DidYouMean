package util;

/**
 * A simple tuple class that contains a string and an int.
 *
 * @author Tim
 */
public class TupleStringInt {
    String str;
    int i;

    /**
     * Initializes a TupleStringInt.
     *
     * @param str the string value it should have.
     * @param i   the int value it should have.
     */
    public TupleStringInt(String str, int i) {
        this.str = str;
        this.i = i;
    }

    /**
     * Gets the integer value.
     *
     * @return the integer value.
     */
    public int getI() {
        return i;
    }

    /**
     * Gets the string value.
     *
     * @return the string value.
     */
    public String getStr() {
        return str;
    }

    /**
     * Sets the int value.
     *
     * @param i the new int value.
     */
    public void setI(int i) {
        this.i = i;
    }

    /**
     * Sets the string value.
     *
     * @param str the new string value.
     */
    public void setStr(String str) {
        this.str = str;
    }
}
