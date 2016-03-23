package api.didyoumean.levenshteinautomata;

public abstract class State {

    /**
     * Checks if not this State is an accepting State.
     * @param w The length of the word of the DFA
     * @return Whether or not this State is an accepting one.
     */
    public abstract boolean isAcceptingState(int w);

    /**
     * Returns the next state for a given symbol and word length.
     * @param c the next symbol
     * @param w the word length
     * @return the next state for the given parameters, or {@code null} if these would lead to a failure state.
     */
    public abstract State outState(char c, String w);

    /**
     * Gets the Levenshtein distance between the word of this Levenshtein-automata and the word corresponding to the
     * symbols that are used to get to this state.
     * @param w the length of the wordt corresponding to the Levenshtein-automata
     * @return the Levenshtein-automata
     */
    public abstract int getDistance(int w);

}
