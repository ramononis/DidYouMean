package DidYouMean.levenstheinAutomata;

/**
 * Created by Tim on 3/8/2016.
 */
public class Transition {

    private State fromPos;
    private Token token;
    private State toPos;
    private String letter;

    public Transition(State fromPos, Token token, State toPos) {
        this(fromPos, token, toPos, null);
    }

    public Transition(State fromPos, Token token, State toPos, String letter) {

        this.fromPos = fromPos;
        this.token = token;
        this.toPos = toPos;
        this.letter = letter;
    }

    public boolean hasLetter() {
        return letter == null;
    }

    public State getFromPos() {
        return fromPos;
    }

    public void setFromPos(State fromPos) {
        this.fromPos = fromPos;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public State getToPos() {
        return toPos;
    }

    public void setToPos(State toPos) {
        this.toPos = toPos;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }


}
