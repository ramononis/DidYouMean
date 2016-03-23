package database;

import java.util.Map;

/**
 * Created by Tim on 15-3-2016.
 */
public class CSVCStub implements IDBControl{
    private Map<String, Integer> data;

    public void setData(Map<String, Integer> data){
        this.data = data;
    }


    @Override
    public Map<String, Integer> getData() {
        return data;
    }
}
