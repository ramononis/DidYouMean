package autoComplete.tree;

import java.io.Serializable;

/**
 * This class models the root of the tree.
 * The Root is a {@link Node} with no parent and no letter.
 *
 * @author Frans
 */
public class Root extends Node{

    /**
     * Initializes a new Root.
     */
    public Root() {
        super((char) -1);
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
    public String getWord() {
        return "";
    }

    @Override
    public String toString() {
        return "Root(" + getWeight() + ")";
    }
}
