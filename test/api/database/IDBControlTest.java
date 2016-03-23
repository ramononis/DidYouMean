package api.database;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the IDBControl.
 *
 * @author Tim
 */
public class IDBControlTest {

    @Test
    public void testCalcWeight() throws Exception {
        assertThat(IDBControl.calcWeight(10,75.0), is(750));
        assertThat(IDBControl.calcWeight(10,12.3), is(123));
    }
}
