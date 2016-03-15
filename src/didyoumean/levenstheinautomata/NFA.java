package didyoumean.levenstheinautomata;

/**
 * Created by Tim on 3/9/2016.
 */
public class NFA extends FiniteAutomata{


    /**
     * Creates a new NFA with a provided initial state.
     * @param initState
     */
    public NFA(State initState){
        super(initState);
    }

    /**
     * Creates a new NFA with a provided n (consumed letters) and e (current errors).
     * @param n The number of consumed letters.
     * @param e The number of errors.
     */
    public NFA(int n, int e){
        super(n, e);
    }

    /**
     * Adds a Transition to this NFA.
     * @param transition The Transition to be added.
     * @return Whether the addition was successful or not.
     */
    public boolean addTransition(Transition transition){
        return getTransitions().add(transition);
     }
}
