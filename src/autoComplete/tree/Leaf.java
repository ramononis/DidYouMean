package autoComplete.tree;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Frans on 2016-02-23.
 */
public class Leaf extends Element {

    public Leaf(int weight, Node parent) {
        super((char)0, weight, parent);
    }

    @Override
    public Set<Element> getChildren() {
        return new HashSet<>();
    }
}
