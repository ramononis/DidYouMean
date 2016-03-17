package autocomplete.tree;

import org.junit.Before;
import org.junit.Test;

import static matchers.TreeMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

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
        new Node(Element.TERM, root);
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
        root.addOrIncrementWord("a", 1);
        assertThat(root, hasNumberOfChildren(1));
    }

    @Test
    public void testNodeGetChildren() throws Exception {
        testGetChildren(node);
        node.addOrIncrementWord("a" + Element.TERM, 1);
        assertThat(node, hasNumberOfChildren(1));
    }

    @Test
    public void testLeafGetChildren() throws Exception {
        testGetChildren(leaf);
    }

    private void testGetChildren(Element e) throws Exception {
        assertThat(e, hasNumberOfChildren(0));
        e.getChildren().add(e);
        e.getChildren().add(root);
        e.getChildren().add(node);
        e.getChildren().add(leaf);
        assertThat(e, hasNumberOfChildren(0));
    }

    //------ hasChild()
    @Test
    public void testRootHasChild() throws Exception {
        testHasChild(root);
        root.addOrIncrementWord("a", 1);
        testHasChild(root);
        assertThat(root, hasChildWithLetter('a'));
    }

    @Test
    public void testNodeHasChild() throws Exception {
        testHasChild(node);
        node.addOrIncrementWord("a" + Element.TERM, 1);
        testHasChild(node);
        assertThat(node, hasChildWithLetter('a'));
    }

    @Test
    public void testLeafHasChild() throws Exception {
        testHasChild(leaf);
    }

    private void testHasChild(Element e) throws Exception {
        assertThat(e, allOf(not(hasChildWithLetter(Element.TERM)), not(hasChildWithLetter('n')), not(hasChildWithLetter('m'))));
    }

    //------ getChild()
    @Test
    public void testRootGetChild() throws Exception {
        testGetChild(root);
        root.addOrIncrementWord("a", 1);
        assertThat(root.getChild('a').getLetter(), is('a'));
        assertThat(root.getChild('a').getWeight(), is(1));
    }

    @Test
    public void testNodeGetChild() throws Exception {
        testGetChild(node);
        node.addOrIncrementWord("a" + Element.TERM, 1);
        assertThat(node.getChild('a').getLetter(), is('a'));
        assertThat(node.getChild('a').getWeight(), is(1));
    }

    @Test
    public void testLeafGetChild() throws Exception {
        testGetChild(leaf);
    }

    private void testGetChild(Element e) throws Exception {
        assertThat(e.getChild(Element.TERM), nullValue());
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
        assertThat(leaf.getLetter(), is(Element.TERM));
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
        assertThat(leaf.getWord(), is("n" + Element.TERM));
    }

    //------ searchElement()
    @Test
    public void testRootSearchElement() throws Exception {
        testSearchElementWithoutChildren(root);

        root.addOrIncrementWord("abc", 5);

        assertThat(root.searchElement(""), is(root));
        assertThat(root.searchElement(String.valueOf(Element.TERM)), nullValue());
        assertThat(root.searchElement("a"), allOf(hasLetter('a'), wordIs("a"), instanceOf(Node.class)));
        assertThat(root.searchElement("ab"), allOf(hasLetter('b'), wordIs("ab"), instanceOf(Node.class)));
        assertThat(root.searchElement("abc"), allOf(hasLetter('c'), wordIs("abc"), instanceOf(Node.class)));
        assertThat(root.searchElement("d"), nullValue());
    }

    @Test
    public void testNodeSearchElement() throws Exception {
        testSearchElementWithoutChildren(node);
    }

    @Test
    public void testLeafSearchElement() throws Exception {
        testSearchElementWithoutChildren(leaf);
    }

    private void testSearchElementWithoutChildren(Element e) throws Exception {
        assertThat(e.searchElement(""), is(e));
        assertThat(e.searchElement(String.valueOf(Element.TERM)), nullValue());
        assertThat(e.searchElement("a"), nullValue());
        assertThat(e.searchElement("ab"), nullValue());
        assertThat(e.searchElement("abc"), nullValue());
        assertThat(e.searchElement("d"), nullValue());
        assertThat(e.searchElement("a" + Element.TERM), nullValue());
        assertThat(e.searchElement("ab" + Element.TERM), nullValue());
        assertThat(e.searchElement("abc" + Element.TERM), nullValue());
        assertThat(e.searchElement("d" + Element.TERM), nullValue());
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
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAddOrIncrementWord1() throws Exception {
        root.addOrIncrementWord("Weight must be at least 0", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAddOrIncrementWord2() throws Exception {
        root.addOrIncrementWord("Contains termination character" + Element.TERM, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAddOrIncrementWord3() throws Exception {
        root.addOrIncrementWord("Contains termination character" + Element.TERM + ".", 1);
    }

    @Test
    public void testWithChildren() throws Exception {
        int sum = 0;
        for (int i = 1; i < 5; i++) {
            root.addOrIncrementWord("ab", i);
            sum += i;

            assertThat(root, allOf(hasWeight(sum), hasNumberOfChildren(1), hasChildWithLetter('a')));

            Element a = root.getChild('a');

            assertThat(a, instanceOf(Node.class));
            assertThat(a, allOf(hasLetter('a'), hasWeight(sum), hasNumberOfChildren(1), hasChildWithLetter('b')));

            Element b = a.getChild('b');

            assertThat(b, instanceOf(Node.class));
            assertThat(b, allOf(hasLetter('b'), hasWeight(sum), hasNumberOfChildren(1), hasChildWithLetter(Element.TERM)));

            Element l = b.getChild(Element.TERM);

            assertThat(l, instanceOf(Leaf.class));
            assertThat(l, both(hasLetter(Element.TERM)).and(hasWeight(sum)));
        }

        root.addOrIncrementWord("ac", 100);

        assertThat(root, allOf(hasWeight(100), hasNumberOfChildren(1), hasChildWithLetter('a')));

        Element a = root.getChild('a');

        assertThat(a, instanceOf(Node.class));
        assertThat(a, allOf(hasLetter('a'), hasWeight(100), hasNumberOfChildren(2), hasChildWithLetter('b'), hasChildWithLetter('c')));

        Element b = a.getChild('b');

        assertThat(b, instanceOf(Node.class));
        assertThat(b, allOf(hasLetter('b'), hasWeight(sum), hasNumberOfChildren(1), hasChildWithLetter(Element.TERM)));

        Element ab = b.getChild(Element.TERM);

        assertThat(ab, instanceOf(Leaf.class));
        assertThat(ab, both(hasLetter(Element.TERM)).and(hasWeight(sum)));

        Element c = a.getChild('c');

        assertThat(c, instanceOf(Node.class));
        assertThat(c, allOf(hasLetter('c'), hasWeight(100), hasNumberOfChildren(1), hasChildWithLetter(Element.TERM)));

        Element ac = c.getChild(Element.TERM);

        assertThat(ac, instanceOf(Leaf.class));
        assertThat(ac, both(hasLetter(Element.TERM)).and(hasWeight(100)));
    }
}
