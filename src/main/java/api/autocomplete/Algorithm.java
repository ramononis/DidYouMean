package api.autocomplete;

import api.tree.Element;
import api.tree.Root;

import java.util.*;

/**
 * Contains all algorithm methods for auto complete
 *
 * @author Tim Blok, Frans van Dijk, Yannick Mijsters, Ramon Onis, Tim Sonderen; University of Twente
 */
public final class Algorithm {

    private Algorithm() {
    }

    /**
     * Recursively searches for the {@link String}-value of the node with the highest weight in {@code ns} (including their subtrees).
     *
     * @param ns The subtrees to search in.
     * @return The {@link String} corresponding to the value of {@link Element#getWord getWord} of the highest weighed element.
     */
    public static String maxNode(Set<Element> ns) {
        String result;

        Element maxN = Collections.max(ns, (o1, o2) -> o1.getWeight() - o2.getWeight());

        if (maxN.isLeaf()) {
            result = maxN.getWord();
        } else {
            result = maxNode(maxN.getChildren());
        }
        return result;
    }

    /**
     * Returns the smallest possible set of (sub)trees that do not contain the element corresponding to {@code k}
     * (using {@code n} as starting point)
     *
     * @param n The element to be used as root.
     * @param k The keyword to exclude from the subtree of {@code n}.
     * @return The smallest possible {@link Set} containing all elements whose
     * subtrees does not contain the element corresponding to {@code k}
     */
    public static Set<Element> excludeKeyword(Element n, String k) {
        Set<Element> result = new HashSet<>();
        if (!k.isEmpty()) {
            for (Element child : n.getChildren()) {
                if (child.getLetter() == k.charAt(0)) {
                    result.addAll(excludeKeyword(child, k.substring(1)));
                } else {
                    result.add(child);
                }
            }
        }
        return result;
    }

    /**
     * Searches for the {@code c} keywords in {@code r} with the highest score beginning with {@code p}.
     *
     * @param r The root of the api.tree to search in
     * @param c The amount of keywords to be return
     * @param p The prefix all the resulting keywords must have
     * @return A list with at most {@code c} keywords (less than {@code c} if no more could be found).
     */
    public static List<String> getTopKeywords(Root r, int c, String p) {
        p = p.toLowerCase();
        List<String> result = new ArrayList<>(c);
        Element n = r.searchElement(p);
        if (n == null) {
            return result;
        }
        Set<Element> searchNodes = new HashSet<>();
        searchNodes.addAll(n.getChildren());
        int i = 0;
        while (i++ < c && !searchNodes.isEmpty()) {
            String keyword = maxNode(searchNodes);
            result.add(keyword.replace(String.valueOf(Element.TERM), ""));
            for (Element e : searchNodes) {
                String word = e.getWord();
                if (keyword.startsWith(word)) {
                    searchNodes.remove(e);
                    searchNodes.addAll(excludeKeyword(e, keyword.replaceFirst(word, "")));
                    break;
                }
            }
        }
        return result;
    }
}
