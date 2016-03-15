package database;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Tim on 15-3-2016.
 */
public class IDBControlTest {

    @Test
    public void testCalcWeight() throws Exception {
        assertThat(IDBControl.calcWeight(10,75.0), is(750));
        assertThat(IDBControl.calcWeight(10,12.3), is(123));
    }
}