package didyoumean.levenstheinautomata;

import utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.max;

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
    private boolean reduced = false;

    public LevenshteinAutomataFactory(int n) {
        maxN = n;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        LevenshteinAutomataFactory laf = new LevenshteinAutomataFactory(1);
        laf.precalculate();
        System.out.println(System.currentTimeMillis() - start);
    }

    public void precalculate() {
        calculateParametricStates();
        calculateFinalStates();
        calculateTransitionTables();
        reduceStates();
    }

    private void reduceStates() {
        Map<ParametricState, Object> stateReductions = new HashMap<>();
        Integer i = 0;
        for (ParametricState pState : pStates) {
            Integer o = i++;
            stateReductions.put(pState, o);
            pState.o = o;
            pState.hash = pState.hashCode();
        }
        transitionTables.values().forEach(t -> t.reduce(stateReductions));
        pStates.forEach(p -> p.positions.clear());
        pStates.clear();
        reduced = true;
    }

    private void calculateTransitionTables() {
        transitionTables = new HashMap<>();
        for (int l = 0; l <= maxN * 2 + 1; l++) {
            transitionTables.put(l, new TransitionTable(l));
        }
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


    private Set<ParametricPosition> constructPositions(int[][] data) {
        Set<ParametricPosition> result = new HashSet<>();
        for (int[] ie : data) {
            if (ie.length < 2) {
                throw new IllegalArgumentException("data must contain elements of arrays of at least size 2");
            }
            result.add(new ParametricPosition(ie[0], ie[1]));
        }
        return result;
    }

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

    private class StateWrapper extends State {
        Pair<ParametricState, Integer> state;
        StateWrapper(Pair<ParametricState, Integer> s) {
            s = state;
        }
    }
    public State outState(State s, char c, String w) {
        if(!(s instanceof StateWrapper))
            return null;
        int l = Math.min(w.length(), 2*maxN+1);
        w = w.substring(0,l);
        CharacteristicVector cv = new CharacteristicVector(c,w);
        return transitionTables.get(l).get((StateWrapper) s, cv);
    }
    private class ParametricState {

        final Set<ParametricPosition> positions;
        int hash;
        Object o;

        ParametricState() {
            positions = new HashSet<>();
        }

        private ParametricState(Set<ParametricPosition> ps) {
            positions = ps;
        }

        boolean addPosition(ParametricPosition p) {
            return positions.add(p);
        }

        int maxOffset() {
            if (positions.isEmpty()) {
                return 0;
            }
            return max(positions, (o1, o2) -> o1.indexOffset - o2.indexOffset).indexOffset;
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
            return max(positions.stream().map(p -> p.indexOffset - p.edits).collect(Collectors.toSet()));
        }

        /**
         * Returns ithe accepting interval relative to the word length.<br>
         *
         * @return
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


        public boolean equals(Object o) {
            return o instanceof ParametricState && ((reduced && ((ParametricState) o).o == this.o)
                    || ((ParametricState) o).positions.equals(positions));
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

        ParametricState applyVector(CharacteristicVector v, int wordOffset) {
            Set<ParametricPosition> ps = new HashSet<>();
            positions.forEach(p -> ps.addAll(p.applyVector(v, wordOffset)));
            return new ParametricState(ps);
        }

        public boolean isEmpty() {
            return positions.isEmpty();
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
                    } else if (j == -1) {
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
    }

    private class TransitionTable {
        Map<Pair<ParametricState, CharacteristicVector>, Pair<ParametricState, Integer>> table;

        TransitionTable(int l) {
            constructTable(l);
        }

        void reduce(Map<ParametricState, Object> mapping) {
            for (Pair<ParametricState, Integer> pair : table.values()) {
                ParametricState pState = pair.getLeft();
                Object o = mapping.get(pState);
                pState.o = o;
                pState.hash = pState.hashCode();
                pState.positions.clear();
            }
        }
        State get(StateWrapper s, CharacteristicVector cv) {
            Pair<ParametricState, Integer> value =  table.get(new Pair<>(s.state.getLeft(), cv));
            return new StateWrapper(new Pair<>(value.getLeft(), value.getRight() + s.state.getRight()));
        }
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
                    out = out.reduce();
                    table.put(new Pair<>(p, v), new Pair<>(out, offset));
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


        int minimalIndex(int startAt) {
            for (int i = startAt; i < value.length; i++) {
                if (value[i]) {
                    return i - startAt;
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