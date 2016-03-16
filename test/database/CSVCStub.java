package database;

import java.util.HashMap;

/**
 * Created by Tim on 15-3-2016.
 */
public class CSVCStub implements IDBControl{
    private HashMap<String, Integer> data;

    public void setData(HashMap data){
        this.data = data;
    }


    @Override
    public HashMap<String, Integer> getData() {
        return data;
    }
}
