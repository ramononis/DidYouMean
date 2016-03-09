package DidYouMean.levenstheinAutomata;

/**
 * Created by Tim on 3/8/2016.
 */
public class State {

    private int n;
    private int e;
    private int score;
    private boolean acceptingState;

    public State(int n, int e, int score, boolean acceptingState){
        this.score = score;
        this.n = n;
        this.e = e;
        this.acceptingState = acceptingState;
    }

    public State(int n, int e, int score){
        this(n, e, score, false);
    }

    public State(int n, int e){
        this(n, e, 0, false);
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public boolean equals(State p){
        return (p.getE() == getE() && p.getN() == p.getN());
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public boolean isAcceptingState() {
        return acceptingState;
    }

    public void setAcceptingState(boolean acceptingState) {
        this.acceptingState = acceptingState;
    }
}
