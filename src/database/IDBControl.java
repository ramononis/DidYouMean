package database;

import util.TupleStringInt;

import java.util.List;

/**
 * Created by Tim on 23-2-2016.
 */
public interface IDBControl {
    List<TupleStringInt> getData();

    static int calcWeight (int n, double p){
        return (int) Math.round(n * p);
    }
}
