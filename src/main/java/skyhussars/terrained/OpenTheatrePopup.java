/*
 * Copyright (c) 2019, ZoltanTheHun
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
    private static final Logger LOGGER = Logger.getLogger(TerrainEdController.class.getName());
    private final TheatreLoader theatreLoader;
    private final TerrainProperties terrainProperties;

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
            LOGGER.log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
    }
    
}
