/*
 * Copyright (c) 2016, ZoltanTheHun
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
package skyhussars;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import skyhussars.engine.SettingsManager;
import skyhussars.engine.terrain.TheatreLoader;
import skyhussars.terrained.NewTheatrePopup;
import skyhussars.terrained.TerrainEdController;
import skyhussars.terrained.OpenTheatrePopup;
import skyhussars.terrained.TerrainProperties;

/**
 * Terrain Editor application for SkyHussars
 * 
 */
public class TerrainEd extends Application  {

    private SettingsManager settingsManager;
    private OpenTheatrePopup theatreSelectorPopup;
    private NewTheatrePopup newTheatrePopup;
    private final TerrainProperties terrainProperties = new TerrainProperties();
    private final ApplicationContext context = new AnnotationConfigApplicationContext(TerrainEdConfig.class);
    
    @Override
    public void start(Stage stage) throws Exception {
        /* prepare the resource */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/terrained/terrained_main.fxml"));
        Parent root = loader.load();
        /* initializes resources */
        initializeResources();
        /* setup the controller */
        loader.<TerrainEdController>getController().
                stage(stage).
                popups(theatreSelectorPopup,newTheatrePopup).
                terrainProperties(terrainProperties);
        /* initialize the scene */
        Scene scene = new Scene(root);
        stage.setTitle("SkyHussars - TerrainEd");
        stage.setScene(scene);
        stage.show();
    }
    
    private void initializeResources(){
        settingsManager = new SettingsManager(System.getProperty("user.dir"));
        TheatreLoader theatreLoader = new TheatreLoader(settingsManager.assetDirectory());
        theatreSelectorPopup = new OpenTheatrePopup(theatreLoader,terrainProperties);
        newTheatrePopup = new NewTheatrePopup();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
