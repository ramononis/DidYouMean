package api.didyoumean.levenshteinautomata;

import api.utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.max;
import static java.util.Collections.min;

/**
 * A class for simulating a Levenshtein automata.<br>
 * The Levenshtein automata factory(LAF) precalculates a parametric list of states and a transition table
 * in order to provide simulation of Levenshtein automata, able to calculate Levenshtein distance(up to a given maximum)
 * of a word W in linear time in |W|.<p>
 * <p>
 * After providing the {@link #getInit() initial state}, successive states can be determine in constant time.<p>
 * <p>
 * The approach used in this class is based on:<br>
 * <i>Fast string correction with Levenshtein automata (KU Schulz {@literal &} S Mihov, 2002)</i><br>
 * Definitions that are being referred to are from this paper.
 *
 * @author Tim Blok, Frans van Dijk, Yannick Mijsters, Ramon Onis, Tim Sonderen; University of Twente
 * @see <a href="http://goo.gl/v28nA8">Fast string correction with Levenshtein automata</a>
 */
public class LevenshteinAutomataFactory {
    /**
     * Maximal allowed Levenshtein distance
     */
    private final int maxN;
    /**
     * Parametric list of states
     */
    private Set<ParametricState> pStates;
    /**
     * Parametric list of final states
     */
    private Map<ParametricState, Pair<Integer, Integer>> acceptingIntervals;
    /**
     * Transition tables containing parametric description of the transition function.
     */
    private Map<Integer, TransitionTable> transitionTables;
    private ParametricState initState;
    private boolean reduced = false;

    /**
     * Initializes a new {@link LevenshteinAutomataFactory} using a given maximal distance.<br>
     * Upon initialisation the precalculations are done which, depending on {@code n}, take a lot of time.<br>
     * Note that memory usage grows extremely fast when increasing {@code n}. It is not recommended to use values
     * greater than 4.
     *
     * @param n The maximal distance that the simulated automata accept.
     */
    public LevenshteinAutomataFactory(int n) {
        maxN = n;
        precalculate();
    }

    /**
     * Return the universal initial state of any word with any length.
     *
     * @return the initial state of any Levenshtein automata for the given max distance.
     */
    public State getInit() {
        return new StateWrapper(new Pair<>(initState, 0));
    }

    /**
     * Perform precalculations as described in 5.2 with an additional reduction stage to clear redundant data.
     */
    private void precalculate() {
        calculateParametricStates();
        calculateFinalStates();
        calculateTransitionTables();
        reduceStates();//skip line this to make debugging easier
    }

    /**
     * Reduces the information that is stored in {@link ParametricState}.<br>
     * Specifically, {@link ParametricState#positions positions} is cleared as it is redundant after the transition tables
     * are computed.
     * Before clearing, information that is needed afterwards is calculated and stored(hashCode, maxBaseIndex)<br>
     * To keep {@link Object#equals(Object) equals()} correct, each {@link ParametricState} is given a {@link Object}
     * which will serve as its identity.
     */
    private void reduceStates() {
        Map<ParametricState, Object> stateReductions = new HashMap<>();
        Integer i = 0;
        for (ParametricState pState : pStates) {
            Integer o = i++;
            stateReductions.put(pState, o);
            pState.o = o;
            pState.hash = pState.hashCode();
            pState.maxBase = pState.maxBaseOffset();
            pState.minEdits = pState.getMinEdits();
        }

        transitionTables.values().forEach(t -> t.reduce(stateReductions));
        initState.o = stateReductions.get(initState);
        initState.hash = initState.hashCode();
        initState.positions.clear();
        initState.maxBase = initState.maxBaseOffset();
        initState.minEdits = initState.getMinEdits();
        pStates.forEach(p -> p.positions.clear());
        pStates.clear();
        reduced = true;
    }

    /**
     * Calculate the transition tables for every possible length of relevant subwords(see 4.0.33)
     */
    private void calculateTransitionTables() {
        transitionTables = new HashMap<>();
        for (int l = 0; l <= maxN * 2 + 1; l++) {
            transitionTables.put(l, new TransitionTable(l));
        }
    }

    /**
     * Precalculates the interval(relative to the word length) in which the index of a state is accepting.
     */
    private void calculateFinalStates() {
        if (pStates == null) {
            return;
        }
        acceptingIntervals = new HashMap<>();
        pStates.stream().filter(p -> !p.positions.isEmpty()).forEach(p -> acceptingIntervals.put(p, p.acceptingInterval()));
    }

    /**
     * Calculates all possible parametric states with accordance to definition 4.0.18.
     */
    private void calculateParametricStates() {
        ParametricPosition init = new ParametricPosition(0, 0);
        initState = new ParametricState();
        initState.positions.add(init);
        List<ParametricPosition> triangle = init.subsumptionTriangle();
        pStates = calculateParametricStates(new ParametricState(), triangle);
    }


    /**
     * Recursively calcualtes the set of possible parametric states.
     *
     * @param state the base state to add in more positions
     * @param ps    the remaining positions to add
     * @return a {@link Set} of possible parametric states
     */
    private Set<ParametricState> calculateParametricStates(ParametricState state, List<ParametricPosition> ps) {
        if (ps.isEmpty()) {
            Set<ParametricState> result = new HashSet<>();
            result.add(state.shiftToLeft());
            return result;
        }
        List<ParametricPosition> ps2 = new ArrayList<>(ps);
        ParametricPosition p = ps2.iterator().next();
        ps2.remove(p);
        Set<ParametricState> result = calculateParametricStates(state, ps2);
        if (!state.subsumes(p)) {
            ParametricState state2 = state.copy();
            state2.addPosition(p);
            Set<ParametricState> result2 = calculateParametricStates(state2, ps2);
            result.addAll(result2);
        }
        return result;
    }


    /**
     * A helper function to create a set of positions in a convenient way.<br>
     * Every element in {@code data} is interpreted as: [index offset, number of edits]
     *
     * @param data A array containing arrays of size 2.
     * @return A set of parametric positions with offsets/edits according to {@code data}.
     * @throws IllegalArgumentException if data contains a element without length 2.
     */
    private Set<ParametricPosition> constructPositions(int[][] data) {
        Set<ParametricPosition> result = new HashSet<>();
        for (int[] ie : data) {
            if (ie.length != 2) {
                throw new IllegalArgumentException("data must contain elements of arrays of at least size 2");
            }
            result.add(new ParametricPosition(ie[0], ie[1]));
        }
        return result;
    }


    /**
     * Returns all possible characteristic vectors with length {@code l}.<br>
     * Note that this results in {@code 2^l} results.
     *
     * @param l The length of the vectors
     * @return All possible vectors with length {@code l}.
     */
    private Set<CharacteristicVector> vectorCombinations(int l) {
        Set<CharacteristicVector> result = new HashSet<>(1 << l);
        int i = -1;
        while (++i < (1 << l)) {
            Boolean[] bits = new Boolean[l];
            for (int b = 0; b < l; b++) {
                bits[b] = (i & (1 << b)) != 0;
            }
            result.add(new CharacteristicVector(bits));
        }
        return result;
    }

    /**
     * A class corresponding to a state in a deterministic Levenshtein automata.
     */
    private class StateWrapper extends State {
        Pair<ParametricState, Integer> state;

        /**
         * Initializes a new state.
         *
         * @param s the parametric state, along with an index defining this state.
         */
        StateWrapper(Pair<ParametricState, Integer> s) {
            state = s;
        }

        /**
         * Gets the Levenshtein distance corresponding to this state and a given word length
         *
         * @param w the length of the wordt corresponding to the Levenshtein-automata
         * @return The calculated Levenshtein distance.
         */
        @Override
        public int getDistance(int w) {
            return w - state.getRight() - state.getLeft().maxBaseOffset();
        }

        @Override
        public int getMinEdits() {
            return state.getLeft().getMinEdits();
        }

        /**
         * Indicates whether or not this is a accepting state.
         *
         * @param w The length of the word of the DFA
         * @return {@code true} if accepting, otherwise {@code} false
         */
        @Override
        public boolean isAcceptingState(int w) {
            Pair<Integer, Integer> i = acceptingIntervals.get(state.getLeft());
            return (i.getLeft() + w) <= state.getRight() && state.getRight() <= (i.getRight() + w);
        }

        /**
         * Determines(in near constant time) the next state, given a symbol and the word of the simulated automata.
         *
         * @param c the next symbol
         * @param w the word length
         * @return The next state, or {@code null} if this transition results in a failure state.
         */
        @Override
        public State outState(char c, String w) {
            int l = Math.min(w.length() - state.getRight(), 2 * maxN + 1);
            w = w.substring(state.getRight(), state.getRight() + l);
            CharacteristicVector cv = new CharacteristicVector(c, w);
            return transitionTables.get(l).get(this, cv);
        }

        @Override
        public String toString() {
            if (reduced) {
                return state.toString();
            } else {
                return state.getLeft().toString(state.getRight());
            }
        }
    }

    /**
     * A parametric description of a set of possible states.
     */
    private class ParametricState {

        private final Set<ParametricPosition> positions;
        int hash;
        Object o;
        int maxBase;
        int minEdits;

        /**
         * Creates a empty state.
         */
        ParametricState() {
            positions = new HashSet<>();
        }


        /**
         * Creates a states with positions {@code ps}.<br>
         * If {@code isChecked} is false, the positions are only added
         * if they are not subsumed by any other position in {@code ps}.
         *
         * @param parametricPositions The positions to include
         * @param isChecked           If {@code true}, the positions are only added if they are not subsumed by others.
         */
        public ParametricState(Set<ParametricPosition> parametricPositions, boolean isChecked) {
            if (!isChecked) {
                positions = new HashSet<>();
                parametricPositions.stream().sorted().forEach(p -> {
                    if (!subsumes(p)) {
                        positions.add(p);
                    }
                });
            } else {
                positions = parametricPositions;
            }
        }

        /**
         * Adds a position to this state.<br>
         * Note that it is not checked if this position is subsumed by this state.
         *
         * @param p The position to add
         */
        void addPosition(ParametricPosition p) {
            positions.add(p);
        }


        /**
         * Calculates the biggest offset in this state.
         */
        int maxOffset() {
            if (positions.isEmpty()) {
                return 0;
            }
            return max(positions, (o1, o2) -> o1.indexOffset - o2.indexOffset).indexOffset;
        }

        /**
         * Calculates the biggest offset in this state.
         */
        int minOffset() {
            if (positions.isEmpty()) {
                return 0;
            }
            return Collections.min(positions, (o1, o2) -> o1.indexOffset - o2.indexOffset).indexOffset;
        }

        /**
         * Shifts the whole state to the left so that {@code minOffset} equals {@code 0}.
         *
         * @return The resulting state after the shift.
         */
        ParametricState shiftToLeft() {
            int min = minOffset();
            ParametricState result = new ParametricState();
            result.positions.addAll(positions
                    .parallelStream()
                    .map(p -> new ParametricPosition(p.indexOffset - min, p.edits))
                    .collect(Collectors.toSet()));
            return result;
        }


        /**
         * Checks whether or not the position {@code p} is subsumed by a position in this state.
         *
         * @param p the position to check.
         * @return True if {@code p} is subsumed by a position in this state.
         */
        boolean subsumes(ParametricPosition p) {
            for (ParametricPosition p2 : positions) {
                if (p2.subsumes(p)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns the maximal possible base position(see 4.0.18) for this state.
         *
         * @return the maximal possible base position.
         */
        int maxBaseOffset() {
            if (reduced) {
                return maxBase;
            }
            if (positions.isEmpty()) {
                return 0;
            }
            return max(positions.stream().map(p -> p.indexOffset - p.edits).collect(Collectors.toSet()));
        }

        /**
         * Returns the accepting interval relative to the word length.<br>
         *
         * @return A {@link Pair} of non-positive integers corresponding to the relative accepting interval
         */
        Pair<Integer, Integer> acceptingInterval() {
            if (positions.isEmpty()) {
                return null;
            }
            return new Pair<>(-maxBaseOffset() - maxN, -maxOffset());
        }


        @Override
        public int hashCode() {
            if (reduced) {
                return hash;
            }
            return positions.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ParametricState) {
                if (reduced) {
                    return ((ParametricState) o).o == this.o;
                } else {
                    return ((ParametricState) o).positions.equals(positions);
                }
            }
            return false;
        }

        @Override
        public String toString() {
            if (reduced) {
                return o.toString();
            }
            if (positions.isEmpty()) {
                return "∅";
            }
            StringBuilder result = new StringBuilder("{");
            positions.forEach(p -> result.append(p.toString()).append(", "));
            result.delete(result.length() - 2, result.length());
            result.append("} ");
            result.append("(0 <= i <= w");
            if (maxOffset() > 0) {
                result.append(" - ").append(maxOffset());
            }
            result.append(")");
            return result.toString();
        }

        public String toString(int i) {
            if (reduced) {
                return o.toString();
            }
            if (positions.isEmpty()) {
                return "∅";
            }
            StringBuilder result = new StringBuilder("{");
            positions.forEach(p -> result.append(p.toString(i)).append(", "));
            result.delete(result.length() - 2, result.length());
            result.append("} ");
            return result.toString();
        }

        ParametricState copy() {
            ParametricState result = new ParametricState();
            result.positions.addAll(positions);
            return result;
        }

        /**
         * Applies a characteristic vector to this state, given the offset of this state relative to the word length.
         *
         * @param v          the vector to apply.
         * @param wordOffset Offset relative to the word length.
         * @return the resulting state.
         */
        ParametricState applyVector(CharacteristicVector v, int wordOffset) {
            List<ParametricPosition> ps = new ArrayList<>();
            positions.forEach(p -> ps.addAll(p.applyVector(v, wordOffset)));
            return new ParametricState(new HashSet<>(ps), false);
        }

        public boolean isEmpty() {
            return positions.isEmpty();
        }

        public int getMinEdits() {
            if (reduced) {
                return minEdits;
            }
            if (positions.isEmpty()) {
                return Integer.MAX_VALUE;
            }
            return min(positions).edits;
        }
    }

    /**
     * A parametric description of position(see 4.0.12).<br>
     * The {@code edits} field stands for the amount of edits up to this position(as for normal positions).<br>
     * The offset denotes the shift that is applied to a (unknown) base index {@code i}.<br>
     * Example: {@code new ParametricPosition(3, 1)} represents a position (i+3)<sup>#1</sup>.
     */
    private class ParametricPosition implements Comparable<ParametricPosition> {
        final int edits;
        final int indexOffset;

        /**
         * Creates a new parametric position.
         *
         * @param i it's index offset
         * @param e it's amount of edits
         */
        ParametricPosition(int i, int e) {
            edits = e;
            indexOffset = i;
        }


        @Override
        public int hashCode() {
            return edits * 31 + indexOffset;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof ParametricPosition
                    && edits == ((ParametricPosition) o).edits
                    && indexOffset == ((ParametricPosition) o).indexOffset;
        }

        /**
         * Generates the subsumption triangle for this position(see 4.0.15).<br>
         * Note that, contrary to the referred definition, this method includes this position in the result.
         *
         * @return The subusumption triangle, plus this position, sorted by amount of edits.
         */
        List<ParametricPosition> subsumptionTriangle() {
            List<ParametricPosition> result = new ArrayList<>();
            for (int e = 0; e <= maxN - edits; e++) {
                for (int i = -e; i <= e; i++) {
                    result.add(new ParametricPosition(i + indexOffset, e + edits));
                }
            }
            return result;
        }

        /**
         * Checks whether of not a this position subsumes an other position(see 4.0.15)
         *
         * @param o the position to check
         * @return {@code true} if {@code o} is subsumed by {@code this}
         */
        boolean subsumes(ParametricPosition o) {
            return edits < o.edits
                    && Math.abs(indexOffset - o.indexOffset) <= o.edits - edits;
        }

        @Override
        public String toString() {
            String i = indexOffset == 0 ? "i" : ("(i " + (indexOffset > 0 ? "+ " : "- ") + Math.abs(indexOffset) + ")");
            return i + "#" + edits;
        }

        /**
         * Applies a vector to this position. <br>
         * Specifically, it applies a elementary transition(as defined in 4.0.24 and specified in table 4.1) to assign
         * a state to this position/vector.<br>
         *
         * @param v      the vector to apply
         * @param offset the offset of the containing {@link ParametricState} relative to the word length.
         * @return the assigned state, in the form of a {@link Set} of positions.
         */
        public Set<ParametricPosition> applyVector(CharacteristicVector v, int offset) {
            offset += indexOffset;
            Set<ParametricPosition> result = new HashSet<>();
            int j = v.minimalIndex(indexOffset) + 1;
            int i = indexOffset;

            int e = edits;
            if (0 <= edits && edits <= maxN - 1) {
                if (offset <= -2) {
                    if (j == 1) {
                        result.addAll(constructPositions(new int[][]{{i + 1, e}}));
                    } else if (j == 0 || (j - 1 + e > maxN)) {
                        result.addAll(constructPositions(new int[][]{{i, e + 1}, {i + 1, e + 1}}));
                    } else {
                        result.addAll(constructPositions(new int[][]{{i, e + 1}, {i + 1, e + 1}, {i + j, e + j - 1}}));
                    }
                } else if (offset == -1) {
                    if (j == 1) {
                        result.addAll(constructPositions(new int[][]{{i + 1, e}}));
                    } else {
                        result.addAll(constructPositions(new int[][]{{i, e + 1}, {i + 1, e + 1}}));
                    }
                } else {
                    //offset == 0
                    result.addAll(constructPositions(new int[][]{{i, e + 1}}));
                }
            } else if (offset <= -1 && j == 1) {
                result.addAll(constructPositions(new int[][]{{i + 1, e}}));
            }
            return result;
        }

        @Override
        public int compareTo(ParametricPosition o) {
            return edits - o.edits;
        }

        public String toString(int i) {
            return (indexOffset + i) + "#" + edits;
        }
    }

    /**
     * A transition table for a particular length of the relative subword.
     */
    private class TransitionTable {
        Map<Pair<ParametricState, CharacteristicVector>, Pair<ParametricState, Integer>> table;

        /**
         * Constructs a transition table for a given vector length
         *
         * @param l the given vector length.
         */
        TransitionTable(int l) {
            constructTable(l);
        }

        /**
         * Reduces the states in this table.
         *
         * @param mapping object mapping to be used.
         */
        void reduce(Map<ParametricState, Object> mapping) {
            for (Pair<ParametricState, Integer> pair : table.values()) {
                ParametricState pState = pair.getLeft();
                pState.o = mapping.get(pState);
                pState.hash = pState.hashCode();
                pState.maxBase = pState.maxBaseOffset();
                pState.minEdits = pState.getMinEdits();
                pState.positions.clear();
            }
        }

        /**
         * Applies the transition function to retrieve the next state.
         *
         * @param s  The previous state
         * @param cv the vector to apply
         * @return the next state
         */
        State get(StateWrapper s, CharacteristicVector cv) {
            Pair<ParametricState, Integer> value = table.get(new Pair<>(s.state.getLeft(), cv));
            if (value == null) {
                return null;
            }
            return new StateWrapper(new Pair<>(value.getLeft(), value.getRight() + s.state.getRight()));
        }

        /**
         * Constructs this transtition table
         *
         * @param l the length of characteristic vector to include
         */
        void constructTable(int l) {
            Set<CharacteristicVector> vectors = vectorCombinations(l);
            table = new HashMap<>();
            for (ParametricState p : pStates) {
                if (p.isEmpty() || p.maxOffset() > l) {
                    continue;
                }
                for (CharacteristicVector v : vectors) {
                    ParametricState out = p.applyVector(v, -l);
                    if (out.isEmpty()) {
                        continue;
                    }
                    int offset = out.minOffset();// offset >= 0?
                    out = out.shiftToLeft();
                    table.put(new Pair<>(p, v), new Pair<>(out, offset));
                }
            }
        }

    }

    /**
     * Represents a characteristic vector as defined in 4.0.10
     */
    private class CharacteristicVector {
        Boolean[] value;

        /**
         * Creates the characteristic vector for a given string and character.<br>
         * According to 4.0.10, the resulting instance represents χ(x,w).
         *
         * @param x the char to use
         * @param w the string to use
         */
        CharacteristicVector(char x, String w) {
            value = w.chars().mapToObj(i -> (char) i == x).toArray(Boolean[]::new);
        }

        /**
         * Creates a vector with a given value.
         *
         * @param v the given value
         */
        CharacteristicVector(Boolean[] v) {
            value = v;
        }


        /**
         * Starting at the specified starting index, this method returns the value {@code j}
         * in the notation(as specified in 4.0.24): &lt;b<sub>k</sub>, b<sub>k+1</sub>...&gt; : j, or 0 if this vector
         * doesn't contain a 1.
         */
        int minimalIndex(int k) {
            for (int i = k; i < value.length; i++) {
                if (value[i]) {
                    return i - k;
                }
            }
            return -1;
        }

        @Override
        public String toString() {
            return "<" + Arrays.stream(value).reduce("", (s, b) -> s + (b ? '1' : '0'), ((s, s2) -> s + s2)) + ">";
        }

        @Override
        public int hashCode() {
            return Arrays.stream(value).reduce(0, (i, b) -> i * 2 + (b ? 1 : 0), (i, j) -> i + j);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof CharacteristicVector && Arrays.equals(value, ((CharacteristicVector) o).value);
        }
    }

}
