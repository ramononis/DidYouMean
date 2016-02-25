package autoComplete.tree;

/**
 * This class models the root of the tree.
 * The Root is a Node with no parent and no letter.
 * @author Frans
 */
public class Root extends Node {

    /**
     * Initializes a new Root.
     */
    public Root() {
        super((char)-1, 0);
    }

    /**
     * Throws a RuntimeException because the Root had no letter.
     * @return nothing because a RuntimeException will be thrown.
     * @throws RuntimeException always throws a RuntimeException because the root had no letter.
     */
    @Override
    public char getLetter() {
        // TODO: throw better exception
        throw new RuntimeException("I'm root, I has no letter!");
    }

    /**
     * Indicates that this Element is the Root.
     * @return <code>true</code>
     */
    @Override
    public boolean isRoot() {
        return true;
    }
}
