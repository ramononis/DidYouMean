package autoComplete.tree;

/**
 * Created by Frans on 2016-02-23.
 */
public class Root extends Node {
    public Root() {
        super((char)-1, 0);
    }

    @Override
    public char getLetter() {
        // TODO: throw better exception
        throw new RuntimeException("I'm root, I has no letter!");
    }
}
