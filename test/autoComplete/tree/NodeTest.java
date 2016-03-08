package autoComplete.tree;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

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

    }

    @Test
    public void testAddNewChild() throws Exception {

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
