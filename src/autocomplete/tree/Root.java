package autocomplete.tree;

import autocomplete.Algorithm;

/**
 * This class models the root of the tree.
 * The Root is a {@link Node} with no parent and no letter.
 *
 * @author Frans
 */
public class Root extends Node {

    /**
     * Initializes a new Root.
     */
    public Root() {
        super((char) -1, null);
    }

    /**
     * Throws a {@link RuntimeException} because the Root had no letter.
     *
     * @return nothing because a {@link RuntimeException} will be thrown.
     * @throws RuntimeException , always, because the root had no letter.
     */
    @Override
    public char getLetter() throws RuntimeException {
        // TODO: throw better exception
        throw new RuntimeException("I'm root, I has no letter!");
    }

    @Override
    public String getWord() {
        return "";
    }

    /**
     * Adds the given (sub)keyword to this Node,
     * when already present the given weight is added to the current weight.
     *
     * @param k the keyword, must be terminated with a null char.
     * @param w the weight
     */
    @Override
    public synchronized void addOrIncrementWord(String k, int w) {
        if (k.indexOf(Algorithm.TERM) != k.length() - 1) {
            throw new IllegalArgumentException("Keyword must end with termination character.");
        } else if (w < 0) {
            throw new IllegalArgumentException("Weight must be at least 0.");
        }
        super.addOrIncrementWord(k, w);
    }

    /**
     * Indicates that this {@link Element} is a Root.
     *
     * @return <code>true</code>
     */
    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public String toString() {
        return "Root(" + getWeight() + ")";
    }
}
