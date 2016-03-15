package DidYouMean.levenstheinAutomata;

/**
 * Created by Tim on 3/8/2016.
 */
public class Transition {

    private State fromState;
    private Token token;
    private State toState;
    private char letter;

    /**
     * Creates a new Transition. Sets the letter to a default value ('\0').
     * @param fromState The state the Transition starts from.
     * @param token The Token that accompanies this Transition.
     * @param toState The State this Transition goes to.
     */
    public Transition(State fromState, Token token, State toState) {
        this(fromState, token, toState, '\0');
    }

    /**
     * Creates a new Transition.
     * @param fromState The state the Transition starts from.
     * @param token The Token that accompanies this Transition.
     * @param toState The State this Transition goes to.
     * @param letter The letter accompanying this Transition
     */
    public Transition(State fromState, Token token, State toState, char letter) {

        this.fromState = fromState;
        this.token = token;
        this.toState = toState;
        this.letter = letter;
    }

    /**
     * Checks if this Transition has a letter.
     * @return  Whether or not this Transition has a letter. When returning false, the Token of this class should be
     *          either Token.EPSILON or Token.ANY.
     */
    public boolean hasLetter() {
        return getToken() == Token.LETTER;
    }

    /**
     * Gets the State this Transition starts from.
     * @return The State this Transition starts from.
     */
    public State getFromState() {
        return fromState;
    }

    /**
     * Changes the State the Transition starts from.
     * @param fromState The State this Transition starts from.
     */
    public void setFromState(State fromState) {
        this.fromState = fromState;
    }

    /**
     * Returns the Token type accompanying this Transition.
     * @return the Token type accompanying this Transition.
     */
    public Token getToken() {
        return token;
    }

    /**
     * Sets Token.
     * @param token The new Token.
     */
    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * gets the State this Transition goes to.
     * @return The State this Transition goes to.
     */
    public State getToState() {
        return toState;
    }

    /**
     * Sets the new State this Transition goes to.
     * @param toState The new State this Transition goes to.
     */
    public void setToState(State toState) {
        this.toState = toState;
    }

    /**
     * Gets the letter of this Transition.
     * @return The letter of this Transition.
     */
    public char getLetter() {
        return letter;
    }

    /**
     * Changes the letter of this Transition.
     * @param letter The new letter of this Transition.
     */
    public void setLetter(char letter) {
        this.letter = letter;
    }


}
