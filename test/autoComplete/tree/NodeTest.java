package autoComplete.tree;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
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
    public void testWithChildren() throws Exception {
        Node a = new Node('a', node);
        Node b = new Node('b', node);
        Node c = new Node('c', node);
        Node d = new Node('d', node);
        Node e = new Node('e', node);
        Node f = new Node('f', node);
        Leaf leaf = new Leaf(node);

        Set<Element> children = node.getChildren();
        assertThat(children, hasItems(a, b, c, d, e, f, leaf));
        assertThat(children.size(), is(7));

        assertTrue(node.hasChild('a'));
        assertTrue(node.hasChild('b'));
        assertTrue(node.hasChild('c'));
        assertTrue(node.hasChild('d'));
        assertTrue(node.hasChild('e'));
        assertTrue(node.hasChild('f'));
        assertTrue(node.hasChild((char) 0));

        assertFalse(node.hasChild('z'));

        assertThat(node.getChild('a'), is(a));
        assertThat(node.getChild('b'), is(b));
        assertThat(node.getChild('c'), is(c));
        assertThat(node.getChild('d'), is(d));
        assertThat(node.getChild('e'), is(e));
        assertThat(node.getChild('f'), is(f));
        assertThat(node.getChild((char) 0), is(leaf));
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
    public void testIsLeaf() throws Exception {
        assertFalse("Node is not leaf", node.isLeaf());
    }

    @Test
    public void testIsRoot() throws Exception {
        assertFalse("Node is not root", node.isRoot());
    }
}
