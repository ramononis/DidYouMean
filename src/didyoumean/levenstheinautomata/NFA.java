package didyoumean.levenstheinautomata;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Tim on 3/9/2016.
 */
public class NFA extends FiniteAutomata {


    /**
     * Creates a new NFA with a provided initial state.
     *
     * @param initState
     */
    public NFA(State initState) {
        super(initState);
    }

    /**
     * Creates a new NFA with a provided n (consumed letters) and e (current errors).
     *
     * @param n The number of consumed letters.
     * @param e The number of errors.
     */
    public NFA(int n, int e) {
        super(n, e);
    }


    /**
     * Adds a Transition to this NFA.
     *
     * @param transition The Transition to be added.
     * @return Whether the addition was successful or not.
     */
    public boolean addTransition(Transition transition) {
        return getTransitions().add(transition);
    }

    /**
     * Returns the λ-closure of state {@code s}.
     *
     * @param s The state to use
     * @return The λ-closure of the state
     */
    public Set<State> lambdaClosure(State s) {
        Set<State> ss = new HashSet<State>();
        ss.add(s);
        return lambdaClosure(ss);
    }


    /**
     * Returns union of the λ-closures of states {@code ss}.
     *
     * @param ss The states to use
     * @return The Λ-closure of the states
     */
    public Set<State> lambdaClosure(Set<State> ss) {
        Set<State> result = new HashSet<State>();
        State s = null;
        while ((s = ss.iterator().next()) != null) {
            ss.remove(s);
            result.add(s);
            Set<State> set = getTransitions()
                    .getTransitionsByState(s)
                    .stream()
                    .filter(t -> t.getToken() == Token.LAMBDA)
                    .map(Transition::getToState)
                    .collect(Collectors.toSet());
            set.removeAll(result);
            ss.addAll(set);
        }
        return result;
    }

    /**
     * Returns the Δ-transition of the states {@code ss} using symbol {@code l}.
     * If {@code lambda} is {@code true}, the Λ-closure will first be calculated of {@code ss}
     * , otherwise, this step is skipped.
     * @param ss The states to apply the transition to.
     * @param l The symbol to use.
     * @param lambda Whether or not to first apply Λ-closure
     * @return The Δ-transition of the states.
     */
    public Set<State> deltaTransition(Set<State> ss, char l, boolean lambda) {
        Set<State> toStates = new HashSet<State>();
        if (lambda) {
            ss = lambdaClosure(ss);
        }
        ss.stream().map(s -> getTransitions().getStatesByEdge(s, l)).forEach(set -> toStates.addAll(set));
        return lambdaClosure(toStates);
    }
}
