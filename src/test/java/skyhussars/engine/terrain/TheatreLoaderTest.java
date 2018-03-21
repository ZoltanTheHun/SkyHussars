package skyhussars.engine.terrain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TheatreLoaderTest {
    @Rule
    public ExpectedException expected = ExpectedException.none();
    
    @Test 
    public void testInstantiation(){
        expected.expect(NullPointerException.class);
        TheatreLoader theatreLoader = new TheatreLoader(null);
    }
}
