package api.didyoumean.bktree;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static api.didyoumean.bktree.BKTree.calculateDistance;

/**
 * The Class representing a Node in a BKtree.
 * Created by Yannick on 10-3-2016.
 */
public class Node {
    private String name;
    private Map<Integer, Node> children;
    private int score;

    /**
     * Creates a new Node with a certain word. Has no children and a score of 0.
     *
     * @param word The word corresponding to this Node.
     */
    public Node(String word) {
        this(word, 0);
    }

    /**
     * Creates a new Node with a certain word and score. Has no children.
     *
     * @param word  The word corresponding to this Node.
     * @param score The score this Node should have.
     * @throws IllegalArgumentException if {@code word} is {@code null} or {@code score} is negative.
     */
    public Node(String word, int score) {
        if (word == null) {
            throw new IllegalArgumentException("Tried to make a Node with a null String.");
        }
        if (score < 0) {
            throw new IllegalArgumentException("Tried to give a negative score to a Node.");
        }
        this.name = word;
        this.score = score;
        children = new ConcurrentHashMap<>();
    }

    /**
     * Adds a new child to this node.
     *
     * @param child The Node that this Node should be the parent of.
     * @throws IllegalArgumentException if {@code child} is null.
     */
    protected void addChild(Node child) {
        if (child == null) {
            throw new IllegalArgumentException("Given Node is null in Node.addChild");
        }
        int distance = calculateDistance(child.getName(), name);
        if (children.get(distance) != null) {
            children.get(distance).addChild(child);
        } else {
            children.put(distance, child);
        }
    }

    /**
     * Returns the word of this Node.
     *
     * @return The word of this Node.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the children of this node.
     *
     * @return The children of this node.
     */
    public Map<Integer, Node> getChildren() {
        return children;
    }

    /**
     * Returns the score of this Node.
     *
     * @return the score of this Node.
     */
    public int getScore() {
        return score;
    }

    /**
     * Changes the score of this Node.
     *
     * @param score The new score of this Node.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Retrieves all the Nodes within a certain lD of a word from this (sub)api.tree.
     *
     * @param term       The term that Nodes should be compared with.
     * @param errorRange The maximum LD a Node should have compared to {#code term}.
     * @return The list of Nodes with maximum LD to term.
     * @throws IllegalArgumentException if {@code root} or {@code term} is null, or {@code errorRange} is negative.
     */
    protected Map<Node, Integer> searchTreeForNodes(String term, int errorRange) {
        if (term == null) {
            throw new IllegalArgumentException("Null term in searchTreeForNodes.");
        } else if (errorRange < 0) {
            throw new IllegalArgumentException("Negative errorRange in searchTreeForNodes.");
        }
        Map<Node, Integer> result = new HashMap<>();
        int distance = calculateDistance(term, getName());
        if (distance <= errorRange) {
            result.put(this, distance);
        }
        getChildren().values()
                .parallelStream()
                .filter(n -> Math.abs(calculateDistance(n.getName(), getName()) - distance) <= errorRange)
                .forEach(n -> result.putAll(n.searchTreeForNodes(term, errorRange)));
        return result;
    }

    /**
     * Gets the Node {@code node} such that node.getName().equals(word) somewhere in its tree.
     * Returns {@code null} if there is no such node.
     *
     * @param word The word to be checked.
     * @return The node that corresponds to the given word, or null if there is no such Node.
     */
    public Node getWordInChildren(String word) {
        for (Node node : getChildren().values()) {
            if (node.getName().equals(word)) {
                return node;
            }
            Node childNode = node.getWordInChildren(word);
            if (childNode != null) {
                return childNode;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            return ((Node) o).getName().equals(getName());
        } else {
            return super.equals(o);
        }
    }

    public String toString() {
        return getName() + " (" + getScore() + ")";
    }
}
