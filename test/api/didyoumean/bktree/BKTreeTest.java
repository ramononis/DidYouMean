package api.didyoumean.bktree;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static api.didyoumean.bktree.BKTree.calculateDistance;
import static matchers.CollectionMatchers.sizeIs;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * The Class that tests everything in the bktree package.
 * Created by Tim on 3/21/2016.
 */
public class BKTreeTest {

    Node node;
    BKTree tree;

    @Before
    public void setup() {
        node = new Node("setup");
        tree = new BKTree();
    }

    // ---- Node.java test, 87% coverage. ----
    // ---- Only doesn't cover Object.equals() and the toString() method ----
    // ---- Constructors of Node ----

    @Test(expected = IllegalArgumentException.class)
    public void testNodeInvalidConstructor1() throws Exception {
        // word of a Node must not be null.
        new Node(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeInvalidConstructor2() throws Exception {
        // parent may not be null
        new Node("test", -1);
    }

    // ---- getters of Node ----

    @Test
    public void testGetters() {
        assertThat("getName() should equal to the initial value set in setup().", node.getName(), equalTo("setup"));
        assertThat("getScore() should be equal to the initial value of a node.", node.getScore(), is(0));
        assertThat("getChildren() should be equal to the initial value of a node.", node.getChildren(), equalTo(new ConcurrentHashMap<>()));
    }

    // ---- toString() of Node ----

    @Test
    public void testToString() {
        Node testNode = new Node("test", 16);
        assertThat(testNode.toString(), equalTo("test (16)"));
    }

    // ---- addChild() of Node ----

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildException() {
        node.addChild(null);
    }

    @Test
    public void testAddChild() {
        Node child1 = new Node("setup1"); //LD == 1
        node.addChild(child1);
        assertThat("Setup1 got successfully added to node",
                node.getChildren().containsValue(child1));
        Node child2 = new Node("setup12"); //LD == 2
        node.addChild(child2);
        assertThat("Setup1 and Setup12 got successfully added to node",
                node.getChildren().containsValue(child1) && node.getChildren().containsValue(child2));
        Node child3 = new Node("setup3"); //LD == 1
        node.addChild(child3);
        assertThat("Setup3 got successfully added to a node which already has an edge with distance 1",
                node.getChildren().get(1).getChildren().containsValue(child3));
    }

    // ---- searchTree() ----

    @Test(expected = IllegalArgumentException.class)
    public void testSearchTreeIllegalArg1() {
        Node node = new Node("node");
        node.searchTreeForNodes(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchTreeIllegalArg2() {
        Node node = new Node("node");
        node.searchTreeForNodes("other node", -22);
    }

    @Test
    public void testSearchTree() {
        Node root = new Node("child");
        Node child1 = new Node("child1");
        Node child2 = new Node("child12");
        Node child3 = new Node("child3");
        root.addChild(child1);
        root.addChild(child2);
        root.addChild(child3);
        assertThat(root.searchTreeForNodes("child", 1).entrySet(), sizeIs(3));
        assertThat(root.searchTreeForNodes("child12345", 1).entrySet(), sizeIs(0));
        assertThat(root.searchTreeForNodes("child", 2).entrySet(), sizeIs(4));
    }


    // ---- BKTree.java tests ----

    // buildTree() test

    @Test(expected = IllegalArgumentException.class)
    public void testBuildTreeInvalidArg() throws Exception {
        tree.buildTree(null);
    }

    @Test
    public void testBuildTreeAndAddOrIncrement() {
        Map<String, Integer> data = new ConcurrentHashMap<>();
        data.put("setup", 10);
        data.put("setup1", 30);
        data.put("setup12", 50);
        data.put("setup3", 100);
        tree.buildTree(data);

        assertThat(verifyTree(tree.getRoot()), equalTo(data));
        tree.addOrIncrement("setup3", 75);
        assertThat(tree.getRoot().getWordInChildren("setup3").getScore(), is(175));
        tree.addOrIncrement("newWord", 33);
        assertThat(tree.getRoot().getWordInChildren("newWord").getScore(), is(33));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOrSetIllegalArg1() {
        tree.addOrIncrement(null, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOrSetIllegalArg2() {
        tree.addOrIncrement("new", -110);
    }

    private Map<String, Integer> verifyTree(Node n) {
        Map<String, Integer> result = new HashMap<>();

        for (Map.Entry<Integer, Node> c : n.getChildren().entrySet()) {
            assertThat("Levenshtein distance", calculateDistance(n.getName(), c.getValue().getName()), is(c.getKey()));
            result.putAll(verifyTree(c.getValue()));
        }

        result.put(n.getName(), n.getScore());

        return result;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateLDIllegalArgument1() {
        calculateDistance(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateLDIllegalArgument2() {
        calculateDistance("", null);
    }

    @Test
    public void testCalculateLievenshteinDistance() {
        assertThat("Empty string and any other word should have LD equal to word.length()",
                calculateDistance("", "four"), is(4));
        assertThat(calculateDistance("", ""), is(0));
        assertThat(calculateDistance("food", "food"), is(0));
        assertThat(calculateDistance("food", "fxod"), is(1));
        assertThat(calculateDistance("fxd", "food"), is(2));
        assertThat(calculateDistance("abcfood", "food"), is(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDYMIllegalArg() {
        tree.getDYM(null, 6);
    }

    private Map<String, Integer> testData() {
        Map<String, Integer> data = new ConcurrentHashMap<>();
        data.put("setup", 10);
        data.put("setup1", 30);
        data.put("setup12", 50);
        data.put("setup3333", 1000);

        return data;
    }

    @Test
    public void testGetDYM() {
        tree.buildTree(testData());

        assertThat("LD of 1 should be chosen above other ones, as score is not that different",
                tree.getDYM("setup5", 6), equalTo("setup1"));
        assertThat("Correct word should return the same String.",
                tree.getDYM("setup", 6), equalTo("setup"));
        assertThat("Nothing found within the valid error range should return an empty string",
                tree.getDYM("not within 3 LD", 6), equalTo(""));
        assertThat("Choice between different Nodes with the same LD",
                tree.getDYM("setup134", 6), equalTo("setup3333"));
    }
}
