package autoComplete.tree;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests Leaf specific methods.
 *
 * @author Frans
 */
public class LeafTest {

    Leaf leaf;

    @Before
    public void setUp() throws Exception {
        leaf = new Leaf(null);
    }

    @Test
    public void testGetLetter() throws Exception {
        assertEquals((char) 0, leaf.getLetter());
    }

    @Test
    public void testIsLeaf() throws Exception {
        assertTrue("Leaf is leaf", leaf.isLeaf());
    }

    @Test
    public void testIsRoot() throws Exception {
        assertFalse("Leaf is not root", leaf.isRoot());
    }
}
