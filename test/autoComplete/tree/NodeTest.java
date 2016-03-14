package autoComplete.tree;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests Node specific methods.
 *
 * @author Frans
 */
public class NodeTest {

    Node node;

    @Before
    public void setUp() throws Exception {
        node = new Node('n');

    }

    @Test
    public void testGetChildren() throws Exception {

    }

    @Test
    public void testHasChild() throws Exception {

    }

    @Test
    public void testGetChild() throws Exception {

    }

    @Test
    public void testAddChild() throws Exception {
        Element e1 = new Leaf(node);
        assertEquals(e1, node.getChild((char) 0));

        Element e2 = new Node('a', node);
        assertEquals(e2, node.getChild('a'));

        Element e3 = new Node('a', node);
        assertEquals(e2, node.getChild('a'));

        node.addChild(e3);
        assertEquals(e2, node.getChild('a'));
    }

    @Test
    public void testAddNewChild() throws Exception {
        Element e1 = node.addNewChild((char) 0);
        assertTrue("Should be a leaf because null char", e1.isLeaf());
        assertTrue("Should be a leaf because null char", e1 instanceof Leaf);
        assertEquals(e1, node.getChild((char) 0));

        Element e2 = node.addNewChild('a');
        assertTrue("Should be a node because not null char", e2 instanceof Node);
        assertEquals(e2, node.getChild('a'));

        Element e3 = node.addNewChild('a');
        assertEquals(e2, node.getChild('a'));
        assertEquals(e2, e3);
    }

    @Test
    public void testRemoveChild() throws Exception {

    }

    @Test
    public void testIsLeaf() throws Exception {
        assertFalse("Node is not leaf", node.isLeaf());
    }

    @Test
    public void testIsRoot() throws Exception {
        assertFalse("Node is not root", node.isRoot());
    }
}
