package skyhussars.terrained;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import skyhussars.engine.terrain.TheatreLoader;

/***
 * This class displays the Theatre selector when loading a new theatre
 * 
 */
public class OpenTheatrePopup {
    private static final Logger logger = Logger.getLogger(TerrainEdController.class.getName());
    private final TheatreLoader theatreLoader;
    private TerrainProperties terrainProperties;

    public OpenTheatrePopup(TheatreLoader theatreLoader, TerrainProperties terrainProperties){
        this.theatreLoader = theatreLoader;
        this.terrainProperties = terrainProperties;
    }
    
    public void show(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/terrained/loadtheatre_combo.fxml"));
        try {
            Parent root = loader.load();
            /* initialize the scene */
            Scene scene = new Scene(root);
            loader.<OpenTheatreController>getController().
                    theatreLoader(theatreLoader).
                    terrainProperties(terrainProperties);
            final Stage dialog = new Stage();
            dialog.setTitle("Load Theatre");
            dialog.setScene(scene);
            dialog.show();
        } catch (IOException ex) { 
            logger.log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
    }
    
}
