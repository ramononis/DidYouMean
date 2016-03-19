package utils;

/**
 * Created by ramon on 18-3-2016.
 */
public class Pair<L, R> {

    private final L left;
    private final R right;
    private transient int hash;
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
        hash = left.hashCode() * 31 + right.hashCode();
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair) || o.hashCode() != hashCode()) return false;
        Pair pairo = (Pair) o;
        return this.left.equals(pairo.getLeft()) &&
                this.right.equals(pairo.getRight());
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ", " + right.toString() + ")";
    }

}