package autoComplete.tree;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Frans on 2016-02-23.
 */
public class Root extends Node {
    private final Set<Element> children;

    public Root() {
        super((char)-1, 0);
        children = new HashSet<>();
    }

    public Set<Element> getChildren() {
        return new HashSet<>(children);
    }

    public boolean addChild(Node node) {
        return children.add(node);
    }

    public boolean removeChild(Node node) {
        return children.remove(node);
    }

    @Override
    public char getLetter() {
        // TODO: throw better exception
        throw new RuntimeException("I'm root, I has no letter!");
    }
}
