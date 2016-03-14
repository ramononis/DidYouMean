package autoComplete.tree;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests all tree classes
 *
 * @author Frans
 */
public class TreeTest {

    Root root;
    Node node;
    Leaf leaf;

    @Before
    public void setUp() throws Exception {
        root = new Root();
        node = new Node('n', root); // node is not added as child to root!
        leaf = new Leaf(node); // leaf is not added as child node!
    }

    //****** First some simple tests ******

    //------ Invalid constructor arguments
    @Test(expected = IllegalArgumentException.class)
    public void testNodeInvalidConstructor1() throws Exception {
        // parent may not be null
        new Node('n', null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeInvalidConstructor2() throws Exception {
        // -1 is reserved for Root
        new Node((char) -1, root);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeInvalidConstructor3() throws Exception {
        // 0 is reserved for Leaf
        new Node((char) 0, root);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLeafInvalidConstructor() throws Exception {
        // parent may not be null
        new Leaf(null);
    }

    //------ getChildren()
    @Test
    public void testRootGetChildren() throws Exception {
        testGetChildren(root);
        root.addOrIncrementWord("a" + (char) 0, 1);
        assertThat(root.getChildren().size(), is(1));
    }

    @Test
    public void testNodeGetChildren() throws Exception {
        testGetChildren(node);
        node.addOrIncrementWord("a" + (char) 0, 1);
        assertThat(node.getChildren().size(), is(1));
    }

    @Test
    public void testLeafGetChildren() throws Exception {
        testGetChildren(leaf);
    }

    private void testGetChildren(Element e) throws Exception {
        assertThat(e.getChildren().size(), is(0));
        e.getChildren().add(e);
        e.getChildren().add(root);
        e.getChildren().add(node);
        e.getChildren().add(leaf);
        assertThat(e.getChildren().size(), is(0));
    }

    //------ hasChild()
    @Test
    public void testRootHasChild() throws Exception {
        testHasChild(root);
        root.addOrIncrementWord("a" + (char) 0, 1);
        testHasChild(root);
        assertThat(root.hasChild('a'), is(true));
    }

    @Test
    public void testNodeHasChild() throws Exception {
        testHasChild(node);
        node.addOrIncrementWord("a" + (char) 0, 1);
        testHasChild(node);
        assertThat(node.hasChild('a'), is(true));
    }

    @Test
    public void testLeafHasChild() throws Exception {
        testHasChild(leaf);
    }

    private void testHasChild(Element e) throws Exception {
        assertThat(e.hasChild((char) 0), is(false));
        assertThat(e.hasChild('n'), is(false));
        assertThat(e.hasChild('m'), is(false));
    }

    //------ getChild()
    @Test
    public void testRootGetChild() throws Exception {
        testGetChild(root);
        root.addOrIncrementWord("a" + (char) 0, 1);
        assertThat(root.getChild('a').getLetter(), is('a'));
        assertThat(root.getChild('a').getWeight(), is(1));
    }

    @Test
    public void testNodeGetChild() throws Exception {
        testGetChild(node);
        node.addOrIncrementWord("a" + (char) 0, 1);
        assertThat(node.getChild('a').getLetter(), is('a'));
        assertThat(node.getChild('a').getWeight(), is(1));
    }

    @Test
    public void testLeafGetChild() throws Exception {
        testGetChild(leaf);
    }

    private void testGetChild(Element e) throws Exception {
        assertThat(e.getChild((char) 0), nullValue());
        assertThat(e.getChild('n'), nullValue());
        assertThat(e.getChild('m'), nullValue());
    }

    //------ getLetter()
    @Test(expected = RuntimeException.class)
    public void testRootGetLetter() throws Exception {
        root.getLetter();
    }

    @Test
    public void testNodeGetLetter() throws Exception {
        assertThat(node.getLetter(), is('n'));
    }

    @Test
    public void testLeafGetLetter() throws Exception {
        assertThat(leaf.getLetter(), is((char) 0));
    }

    //------ getWord()
    @Test
    public void testRootGetWord() throws Exception {
        assertThat(root.getWord(), is(""));
    }

    @Test
    public void testNodeGetWord() throws Exception {
        assertThat(node.getWord(), is("n"));
    }

    @Test
    public void testLeafGetWord() throws Exception {
        assertThat(leaf.getWord(), is("n" + (char) 0));
    }

    //------ isLeaf()
    @Test
    public void testRootIsLeaf() throws Exception {
        assertThat("Root is not leaf", root.isLeaf(), is(false));
    }

    @Test
    public void testNodeIsLeaf() throws Exception {
        assertThat("Node is not leaf", node.isLeaf(), is(false));
    }

    @Test
    public void testLeafIsLeaf() throws Exception {
        assertThat("Leaf is leaf", leaf.isLeaf(), is(true));
    }

    //------ isRoot()
    @Test
    public void testRootIsRoot() throws Exception {
        assertThat("Root is root", root.isRoot(), is(true));
    }

    @Test
    public void testNodeIsRoot() throws Exception {
        assertThat("Node is not root", node.isRoot(), is(false));
    }

    @Test
    public void testLeafIsRoot() throws Exception {
        assertThat("Leaf is not root", leaf.isRoot(), is(false));
    }

    //------ toString()
    @Test
    public void testRootToString() throws Exception {
        assertThat("toString should return something", root.toString(), not(""));
    }

    @Test
    public void testNodeToString() throws Exception {
        assertThat("toString should return something", node.toString(), not(""));
    }

    @Test
    public void testLeafToString() throws Exception {
        assertThat("toString should return something", leaf.toString(), not(""));
    }


    //****** More complex tests ******
    @Test
    public void testWithChildren() throws Exception {
        int sum = 0;
        for (int i = 1; i < 5; i++) {
            root.addOrIncrementWord("ab" + (char) 0, i);
            sum += i;

            assertThat(root.getWeight(), is(sum));
            assertThat(root.getChildren().size(), is(1));
            assertThat(root.hasChild('a'), is(true));

            Element a = root.getChild('a');

            assertThat(a, instanceOf(Node.class));
            assertThat(a.getLetter(), is('a'));
            assertThat(a.getWeight(), is(sum));
            assertThat(a.getChildren().size(), is(1));
            assertThat(a.hasChild('b'), is(true));

            Element b = a.getChild('b');

            assertThat(b, instanceOf(Node.class));
            assertThat(b.getLetter(), is('b'));
            assertThat(b.getWeight(), is(sum));
            assertThat(b.getChildren().size(), is(1));
            assertThat(b.hasChild((char) 0), is(true));

            Element l = b.getChild((char) 0);

            assertThat(l, instanceOf(Leaf.class));
            assertThat(l.getLetter(), is((char) 0));
            assertThat(l.getWeight(), is(sum));
        }

        root.addOrIncrementWord("ac" + (char) 0, 100);

        assertThat(root.getWeight(), is(100));
        assertThat(root.getChildren().size(), is(1));
        assertThat(root.hasChild('a'), is(true));

        Element a = root.getChild('a');

        assertThat(a, instanceOf(Node.class));
        assertThat(a.getLetter(), is('a'));
        assertThat(a.getWeight(), is(100));
        assertThat(a.getChildren().size(), is(2));
        assertThat(a.hasChild('b'), is(true));
        assertThat(a.hasChild('c'), is(true));

        Element b = a.getChild('b');

        assertThat(b, instanceOf(Node.class));
        assertThat(b.getLetter(), is('b'));
        assertThat(b.getWeight(), is(sum));
        assertThat(b.getChildren().size(), is(1));
        assertThat(b.hasChild((char) 0), is(true));

        Element ab = b.getChild((char) 0);

        assertThat(ab, instanceOf(Leaf.class));
        assertThat(ab.getLetter(), is((char) 0));
        assertThat(ab.getWeight(), is(sum));

        Element c = a.getChild('c');

        assertThat(c, instanceOf(Node.class));
        assertThat(c.getLetter(), is('c'));
        assertThat(c.getWeight(), is(100));
        assertThat(c.getChildren().size(), is(1));
        assertThat(c.hasChild((char) 0), is(true));

        Element ac = c.getChild((char) 0);

        assertThat(ac, instanceOf(Leaf.class));
        assertThat(ac.getLetter(), is((char) 0));
        assertThat(ac.getWeight(), is(100));
    }
}
