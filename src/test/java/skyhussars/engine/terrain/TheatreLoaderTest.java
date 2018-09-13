package skyhussars.engine.terrain;

import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.File;
import java.io.IOException;
import static java.util.Arrays.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import skyhussars.persistence.base.Marshal;
import skyhussars.persistence.terrain.TerrainDescriptor;

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
    public void testTheatresAreLoaded(){
        try {
            File assets = folder.newFolder("test");
            createFolderStructure(assets);
            TheatreLoader theatreLoader = new TheatreLoader(assets);
            assertEquals(asList("Adria","Himalaya"),theatreLoader.theatres());
        } catch (IOException ex) { throw new RuntimeException(ex);}
    }  
    
    private void createFolderStructure(File assets) throws IOException{
        File theatres = new File(assets,"Theatres");
        theatres.mkdir();
        File adria = new File(theatres,"Adria");
        adria.mkdir();
        File theatre1 = new File(adria,"theatre.json");
        theatre1.createNewFile();
        File himalaya = new File(theatres,"Himalaya");
        himalaya.mkdir();
        File theatre2 = new File(himalaya,"theatre.json");
        theatre2.createNewFile();
        TerrainDescriptor t = new TerrainDescriptor("test", 1, "test");
        Marshal.marshal(t, theatre1);
        Marshal.marshal(t, theatre2);
    }
    
    @Test 
    public void testInvalidTheatreJson(){
        expected.expect(RuntimeException.class);
        try {
            File assets = folder.newFolder("test");
            File theatres = new File(assets,"Theatres");
            theatres.mkdir();
            File adria = new File(theatres,"Adria");
            adria.mkdir();
            File theatre1 = new File(adria,"theatre.json");
            theatre1.createNewFile();
            TheatreLoader theatreLoader = new TheatreLoader(assets);
        } catch (IOException ex) { throw new RuntimeException(ex);}
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
