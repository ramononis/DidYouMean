package util;

/**
 * Created by Tim on 23-2-2016.
 */
public class TupleStringInt {
    String str;
    int i;

    public TupleStringInt(String str, int i){
        this.str = str;
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public String getStr() {
        return str;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
