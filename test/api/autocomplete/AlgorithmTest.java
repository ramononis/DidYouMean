package api.autocomplete;

import api.tree.Element;
import api.tree.Root;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static api.autocomplete.Algorithm.*;
import static matchers.CollectionMatchers.sizeIs;
import static matchers.TreeMatchers.wordIs;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the auto complete algorithm functions
 * 
 * @author Frans
 */
public class AlgorithmTest {

    Root root1;
    Root root2;

    Set<Element> set1;
    Set<Element> set2;
    Set<Element> set12;

    @Before
    public void setUp() {
        root1 = new Root();
        root2 = new Root();

        root1.addOrIncrementWord("apple", 6);
        root1.addOrIncrementWord("apple juice", 36);
        root1.addOrIncrementWord("apple pie", 42);
        root1.addOrIncrementWord("applesauce", 24);
        root1.addOrIncrementWord("strawberry", 9001);

        root2.addOrIncrementWord("AARON", 1671);
        root2.addOrIncrementWord("ABIGAIL", 1546);
        root2.addOrIncrementWord("ADRIAN", 1592);
        root2.addOrIncrementWord("AIDEN", 1910);
        root2.addOrIncrementWord("ALEXA", 865);
        root2.addOrIncrementWord("ALEXANDER", 2521);
        root2.addOrIncrementWord("ALLISON", 914);
        root2.addOrIncrementWord("AMELIA", 811);
        root2.addOrIncrementWord("ANDREW", 1929);
        root2.addOrIncrementWord("ANGEL", 1674);
        root2.addOrIncrementWord("ANTHONY", 2114);
        root2.addOrIncrementWord("ARIA", 853);
        root2.addOrIncrementWord("ARIANA", 911);
        root2.addOrIncrementWord("AUBREY", 803);
        root2.addOrIncrementWord("AUDREY", 920);
        root2.addOrIncrementWord("AVA", 1483);
        root2.addOrIncrementWord("AVERY", 904);

        set1 = new HashSet<>();
        set2 = new HashSet<>();
        set12 = new HashSet<>();

        set1.add(root1);
        set2.add(root2);
        set12.add(root1);
        set12.add(root2);
    }

    @Test
    public void testMaxNode() throws Exception {
        assertThat(maxNode(set1), equalTo("strawberry" + Element.TERM));
        assertThat(maxNode(set2), equalTo("ALEXANDER" + Element.TERM));
        assertThat(maxNode(set12), equalTo("strawberry" + Element.TERM));

        Set<Element> set = new HashSet<>();
        set.add(root1.searchElement("apple"));
        assertThat(maxNode(set), equalTo("apple pie" + Element.TERM));
    }

    @Test
    public void testExcludeKeyword() throws Exception {
        assertThat(excludeKeyword(root1, "applesauce"),
                allOf(sizeIs(3), hasItem(wordIs("s")), hasItem(wordIs("apple ")), hasItem(wordIs("apple" + Element.TERM))));

        assertThat(excludeKeyword(root1, "apples"),
                allOf(sizeIs(3), hasItem(wordIs("s")), hasItem(wordIs("apple ")), hasItem(wordIs("apple" + Element.TERM))));

        assertThat(excludeKeyword(root1, "apple"),
                allOf(sizeIs(1), hasItem(wordIs("s"))));

        assertThat(excludeKeyword(root1, "apple" + Element.TERM),
                allOf(sizeIs(3), hasItem(wordIs("s")), hasItem(wordIs("apple ")), hasItem(wordIs("apples"))));
    }

    @Test
    public void testGetTopKeywords() throws Exception {
        List<String> list = Arrays.asList("strawberry", "apple pie", "apple juice", "applesauce", "apple");

        assertThat(getTopKeywords(root1, 5, ""), equalTo(list));
        assertThat(getTopKeywords(root1, 9, ""), equalTo(list));
        assertThat(getTopKeywords(root1, 3, ""), equalTo(list.subList(0, 3)));
        assertThat(getTopKeywords(root1, 3, "a"), equalTo(list.subList(1, 4)));
        assertThat(getTopKeywords(root1, 7, "a"), equalTo(list.subList(1, 5)));
    }
}
