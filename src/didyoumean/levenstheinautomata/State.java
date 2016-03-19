package didyoumean.levenstheinautomata;

/**
 * Created by Tim on 3/8/2016.
 */
public class State {

    private int n;
    private int e;
    private int score;
    private boolean acceptingState;

    public State(){}
    /**
     * Creates a new State.
     * @param n The number of consumed letters.
     * @param e The number of current errors.
     * @param score The score of this state.
     * @param acceptingState Whether or not this state is an accepting
     *                       state (this State is the end of a word of our dictionary).
     */
    public State(int n, int e, int score, boolean acceptingState){
        this.score = score;
        this.n = n;
        this.e = e;
        this.acceptingState = acceptingState;
    }

    /**
     * Creates a new State. Assumes this is not an accepting state.
     * @param n The number of consumed letters.
     * @param e The number of current errors.
     * @param score The score of this state.
     */
    public State(int n, int e, int score){
        this(n, e, score, false);
    }

    /**
     * Creates a new State. Assumes this is not an accepting state, and has score 0.
     * @param n The number of consumed letters.
     * @param e The number of current errors.
     */
    public State(int n, int e){
        this(n, e, 0, false);
    }

    /**
     * Gets the score of this State.
     * @return The score of this State.
     */
    public int getScore(){
        return score;
    }

    /**
     * Sets the score of this State.
     * @param score The new score of this State.
     */
    public void setScore(int score){
        this.score = score;
    }

    /**
     * Checks whether 2 states are equal. This is only true iff n1 == n2 and p1 == p2.
     * @param argState the State to compare.
     * @return Whether this State and {@code argState} are equal.
     */
    public boolean equals(State argState){
        return (argState.getE() == getE() && argState.getN() == getN());
    }

    /**
     * Returns the amount of consumed letters of this State.
     * @return The amount of consumed letters.
     */
    public int getN() {
        return n;
    }

    /**
     * Sets a new amount of consumed letters for this State.
     * @param n The new amount of consumed letters.
     */
    public void setN(int n) {
        this.n = n;
    }

    /**
     * Gets the current errors of this State.
     * @return The number of errors of this State.
     */
    public int getE() {
        return e;
    }

    /**
     * Changes the current errors of this State.
     * @param e The new number of errors of this State.
     */
    public void setE(int e) {
        this.e = e;
    }

    /**
     * Checks if not this State is an accepting State.
     * @return Whether or not this State is an accepting one.
     */
    public boolean isAcceptingState() {
        return acceptingState;
    }

    /**
     * Sets the current acceptance of this State.
     * @param acceptingState The new acceptance of this State.
     */
    public void setAcceptingState(boolean acceptingState) {
        this.acceptingState = acceptingState;
    }

    public String toString(){
        return "(" + getN() + ", " + getE() +")";
    }
}
