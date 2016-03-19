package didyoumean.levenstheinautomata;

import utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Definitions that are being reffered to are from:<br>
 * Fast string correction with Levenshtein automata (KU Schulz & S Mihov, 2002)
 *
 * @author Ramon Onis
 * @see <a href="http://goo.gl/v28nA8">Fast string correction with Levenshtein automata</a>
 */
public class LevenshteinAutomataFactory {
    /**
     * Maximal alowed single-character edits
     */
    private final int maxN;
    /**
     * Set of possible parametric states
     */
    private Set<ParametricState> pStates;
    private Map<ParametricState, Pair<Integer, Integer>> acceptingIntervals;
    private Map<Integer, TransitionTable> transitionTables;
    public LevenshteinAutomataFactory(int n) {
        maxN = n;
    }

    public static void main(String[] args) {
        LevenshteinAutomataFactory laf = new LevenshteinAutomataFactory(2);
        laf.precalculate();
    }


    public void precalculate() {
        calculateParametricStates();
        calculateFinalStates();
        calculateTransitionTables();
    }

    private void calculateTransitionTables() {
    }

    private void calculateFinalStates() {
        if (pStates == null) {
            return;
        }
        acceptingIntervals = new HashMap<>();
        pStates.stream().filter(p -> !p.positions.isEmpty()).forEach(p -> acceptingIntervals.put(p, p.acceptingInterval()));
    }

    private void calculateParametricStates() {
        ParametricPosition init = new ParametricPosition(0, 0);
        List<ParametricPosition> triangle = init.subsumptionTriangle();
        pStates = calculateParametricStates(new ParametricState(), triangle);
    }

    private Set<ParametricState> calculateParametricStates(ParametricState state, List<ParametricPosition> ps) {
        if (ps.isEmpty()) {
            Set<ParametricState> result = new HashSet<ParametricState>();
            result.add(state.reduce());
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


    private ParametricState constructPState(int[][] data) {
        ParametricState result = new ParametricState();
        for (int[] ie : data) {
            if (ie.length < 2) {
                throw new IllegalArgumentException("data must contain elements of arrays of at least size 2");
            }
            result.addPosition(new ParametricPosition(ie[0], ie[1]));
        }
        return result;
    }

    private Set<CharacteristicVector> vectorCombinations(int l) {
        Set<CharacteristicVector> result = new HashSet<>(1 << l);
        int i = -1;
        while(++i < (1 << l)) {
            Boolean[] bits = new Boolean[l];
            for (int b = 0; b < l; b++) {
                bits[b] = (i & (1 << b)) != 0;
            }
            result.add(new CharacteristicVector(bits));
        }
        return result;
    }

    private class ParametricState {

        final Set<ParametricPosition> positions;

        ParametricState() {
            positions = new HashSet<>();
        }

        boolean addPosition(ParametricPosition p) {
            return positions.add(p);
        }

        int maxOffset() {
            if (positions.isEmpty()) {
                return 0;
            }
            return Collections.max(positions, (o1, o2) -> o1.indexOffset - o2.indexOffset).indexOffset;
        }

        int minOffset() {
            if (positions.isEmpty()) {
                return 0;
            }
            return Collections.min(positions, (o1, o2) -> o1.indexOffset - o2.indexOffset).indexOffset;
        }

        ParametricState reduce() {
            int min = minOffset();
            ParametricState result = new ParametricState();
            result.positions.addAll(positions
                    .parallelStream()
                    .map(p -> new ParametricPosition(p.indexOffset - min, p.edits))
                    .collect(Collectors.toSet()));
            return result;
        }

        boolean subsumes(ParametricPosition p) {
            for (ParametricPosition p2 : positions) {
                if (p2.subsumes(p)) {
                    return true;
                }
            }
            return false;
        }

        int maxBaseOffset() {
            if (positions.isEmpty()) {
                return 0;
            }
            return Collections.max(positions.stream().map(p -> p.indexOffset - p.edits).collect(Collectors.toSet()));
        }

        /**
         * Returns the accepting interval relative to the word length.<br>
         *
         * @return
         */
        Pair<Integer, Integer> acceptingInterval() {
            if (positions.isEmpty()) {
                return null;
            }
            return new Pair<>(-maxBaseOffset() - maxN, -maxOffset());
        }


        public int hashCode() {
            return positions.hashCode();
        }


        public boolean equals(Object o) {
            return o instanceof ParametricState
                    && ((ParametricState) o).positions.equals(positions);
        }

        @Override
        public String toString() {
            if (positions.isEmpty()) {
                return "∅";
            }
            StringBuilder result = new StringBuilder("{");
            positions.forEach(p -> result.append(p.toString() + ", "));
            result.delete(result.length() - 2, result.length());
            result.append("} ");
            result.append("(0 <= i <= w");
            if (maxOffset() > 0) {
                result.append(" - " + maxOffset());
            }
            result.append(")");
            return result.toString();
        }

        ParametricState copy() {
            ParametricState result = new ParametricState();
            result.positions.addAll(positions);
            return result;
        }
    }

    private class ParametricPosition {
        final int edits;
        final int indexOffset;

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

        List<ParametricPosition> subsumptionTriangle() {
            List<ParametricPosition> result = new ArrayList<ParametricPosition>();
            for (int e = 0; e <= maxN - edits; e++) {
                for (int i = -e; i <= e; i++) {
                    result.add(new ParametricPosition(i + indexOffset, e + edits));
                }
            }
            return result;
        }

        /**
         * As defined in 4.0.15
         *
         * @param o
         * @return
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
    }

    private class TransitionTable {
        Map<Pair<ParametricState, CharacteristicVector>, Pair<ParametricState, Integer>> table;
        TransitionTable(int l) {
            table = new HashMap<>();

        }
        void constructTable(int l) {
            Set<CharacteristicVector> vectors = vectorCombinations(l);
            table.clear();
            for(ParametricState p : pStates) {
                for(CharacteristicVector v : vectors) {
                }
            }
        }

    }
    private class CharacteristicVector {
        Boolean[] value;
        CharacteristicVector(char x, String w) {
            value = (Boolean[]) w.chars().mapToObj(i -> new Boolean(((char) i == x))).toArray();
        }
        CharacteristicVector(Boolean[] v) {
            value = v;
        }
        @Override
        public String toString() {
            return "<" + Arrays.stream(value).reduce("", (s, b) -> s + (b ? '1' : '0'), ((s, s2) -> s + s2)) + ">";
        }
        @Override
        public int hashCode() {
            return Arrays.stream(value).reduce(0, (i, b) -> i*2 + (b ? 1 : 0), (i,j) -> i+j);
        }
        @Override
        public boolean equals(Object o) {
            return o instanceof CharacteristicVector && Arrays.equals(value, ((CharacteristicVector) o).value);
        }
    }

}
