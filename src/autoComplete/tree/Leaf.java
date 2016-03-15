package autoComplete.tree;

import autoComplete.AutoCompleter;

/**
 * This class models a leaf in the tree.
 *
 * @author Frans
 */
public class Leaf extends Element {

    /**
     * Initializes a new Leaf.
     * The new Leaf is not added as child to the given parent.
     *
     * @param parent the parent of this Leaf, may not be <code>null</code> use {@link Root} as root
     */
    Leaf(Node parent) {
        super(AutoCompleter.TERM, parent);
    }

    /**
     * Indicates that this {@link Element} is a Leaf.
     *
     * @return <code>true</code>
     */
    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String toString() {
        return "Leaf(" + getWeight() + ")";
    }
}
