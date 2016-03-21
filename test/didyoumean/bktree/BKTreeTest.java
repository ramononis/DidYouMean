package didyoumean.bktree;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Tim on 3/21/2016.
 */
public class BKTreeTest {

    Node node;

    @Before
    public void setup(){
        node = new Node("setup");
    }

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
    public void testGetters(){
        assertThat("getName() should equal to the initial value set in setup().", node.getName().equals("setup"));
        assertThat("getScore() should be equal to the initial value of a node.", node.getScore() == 0);
        assertThat("getChildren() should be equal to the initial value of a node.", node.getChildren().equals(new ConcurrentHashMap<>()));
    }

    // ---- addChild() of Node ----

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildException(){
        node.addChild(null);
    }

    @Test
    public void testAddChild(){
        Node child1 = new Node("setup1"); //LD == 1
        node.addChild(child1);
        assertThat("Setup1 got successfully added to node", node.getChildren().containsValue(child1));
        Node child2 = new Node("setup12"); //LD == 2
        node.addChild(child2);
        assertThat("Setup1 and Setup12 got successfully added to node",
                node.getChildren().containsValue(child1) && node.getChildren().containsValue(child2));
        Node child3 = new Node("setup3"); //LD == 1
        node.addChild(child3);
        assertThat("Setup3 got successfully", node.getChildren().get(1).getChildren().containsValue(child3));
    }
}
