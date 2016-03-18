package didyoumean.levenstheinautomata;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ramon on 17-3-2016.
 */
public class LevenshteinAutomataFactory {
    private final int maxN;
    private Set<ParametricState> pStates;

    public LevenshteinAutomataFactory(int n) {
        maxN = n;
    }

    public static void main(String[] args) {
        LevenshteinAutomataFactory laf = new LevenshteinAutomataFactory(7);
        long start = System.currentTimeMillis();
        laf.calculateParametricStates();
        System.out.println(laf.pStates.size());
        System.out.println((System.currentTimeMillis() - start));
    }

    public void precalculate() {
        calculateParametricStates();
        //TODO: calculateFinalStates()
        //TODO: calculateTransitionTable()
    }

    private void calculateParametricStates() {
        ParametricPosition init = new ParametricPosition(0,0);
        List<ParametricPosition> triangle = init.subsumptionTriangle();
        pStates = calculateParametricStates(new ParametricState(), triangle);
    }
    private Set<ParametricState>  calculateParametricStates(ParametricState state, List<ParametricPosition> ps) {
        if(ps.isEmpty()) {
            Set<ParametricState> result = new HashSet<ParametricState>();
            result.add(state.reduce());
            return result;
        }
        List<ParametricPosition> ps2 = new ArrayList<ParametricPosition>(ps);
        ParametricPosition p = ps2.iterator().next();
        ps2.remove(p);
        Set<ParametricState> result = calculateParametricStates(state, ps2);
        if(!state.subsumes(p)) {
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

    private class ParametricState {

        final Set<ParametricPosition> positions;

        ParametricState() {
            positions = new HashSet<ParametricPosition>();
        }

        boolean addPosition(ParametricPosition p) {
            return positions.add(p);
        }

        int maxOffset() {
            if(positions.isEmpty()) {
                return 0;
            }
            return Collections.max(positions, (o1, o2) -> o1.indexOffset - o2.indexOffset).indexOffset;
        }

        int minOffset() {
            if(positions.isEmpty()) {
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
            for(ParametricPosition p2 : positions) {
                if(p2.subsumes(p)) {
                    return true;
                }
            }
            return false;
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
            if(positions.isEmpty()) {
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

        Position toPosition(int i) {
            return new Position(edits, indexOffset + i);
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

        Set<ParametricState> outStates() {
            Set<ParametricState> result = new HashSet<ParametricState>();
            int e = edits;
            int i = indexOffset;
            if (0 <= e && e <= maxN - 1) {
                result.add(constructPState(new int[][]{{i + 1, e}}));
                for (int j = 2; j <= maxN + e + 1; j++) {
                    result.add(constructPState(new int[][]{{i, e + 1}
                            , {1 + 1, e + 1}
                            , {1 + j, e + j - 1}}));
                }
                result.add(constructPState(new int[][]{{i, e + 1}
                        , {i + 1, e + 1}}));
                result.add(constructPState(new int[][]{{i, e + 1}}));
            } else {
                result.add(constructPState(new int[][]{{i + 1, e}}));
            }
            return result;
        }
        List<ParametricPosition> subsumptionTriangle() {
            List<ParametricPosition> result = new ArrayList<ParametricPosition>();
            for(int e = 0; e <= maxN - edits; e++) {
                for(int i = -e; i <= e; i++) {
                    result.add(new ParametricPosition(i+indexOffset, e+edits));
                }
            }
            return result;
        }
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

    private class Position {
        int edits;
        int index;

        Position(int edits, int index) {
            this.edits = edits;
            this.index = index;
        }

        boolean isAccepting(int w) {
            return w - index <= maxN - edits;
        }

        @Override
        public int hashCode() {
            return edits * 31 + index;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Position
                    && edits == ((Position) o).edits
                    && index == ((Position) o).index;
        }

        @Override
        public String toString() {
            return index + "#" + edits;
        }
    }
}
