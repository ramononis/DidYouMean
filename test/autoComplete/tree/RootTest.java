package autoComplete.tree;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests Root specific methods.
 *
 * @author Frans
 */
public class RootTest {

    Root root;

    @Before
    public void setUp() throws Exception {
        root = new Root();
    }

    @Test(expected = RuntimeException.class)
    public void testGetLetter() throws Exception {
        root.getLetter();
    }

    @Test
    public void testIsRoot() throws Exception {
        assertTrue("Root is root", root.isRoot());
    }

    @Test
    public void testIsLeaf() throws Exception {
        assertFalse("Root is not leaf", root.isLeaf());
    }
}
