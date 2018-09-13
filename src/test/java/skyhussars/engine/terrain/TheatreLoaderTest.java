package skyhussars.engine.terrain;

import java.io.File;
import java.io.IOException;
import static java.util.Arrays.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public class TheatreLoaderTest {
    @Rule
    public ExpectedException expected = ExpectedException.none();
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test 
    public void testInstantiation(){
        expected.expect(NullPointerException.class);
        TheatreLoader theatreLoader = new TheatreLoader(null);
    }
    
    @Test 
    public void testPlanesAreLoaded(){
        try {
            File assets = folder.newFolder("test");
            File theatres = new File(assets,"Theatres");
            theatres.mkdir();
            File adria = new File(theatres,"Adria");
            adria.mkdir();
            File himalaya = new File(theatres,"Himalaya");
            himalaya.mkdir();
            TheatreLoader theatreLoader = new TheatreLoader(assets);
            assertEquals(asList("Adria","Himalaya"),theatreLoader.theatres());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }  
    
    @Test 
    public void testTheatresFolderNotFound(){
        expected.expect(IllegalStateException.class);
        try {
            File assets = folder.newFolder("test");
            File theatres = new File(assets,"Theaters");
            theatres.mkdir();
            TheatreLoader theatreLoader = new TheatreLoader(assets);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }  
    
    @Test 
    public void testTheatresIsFile(){
        expected.expect(IllegalStateException.class);
        try {
            File assets = folder.newFolder("test");
            File theatres = new File(assets,"Theatres");
            theatres.createNewFile();
            TheatreLoader theatreLoader = new TheatreLoader(assets);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }  


}
