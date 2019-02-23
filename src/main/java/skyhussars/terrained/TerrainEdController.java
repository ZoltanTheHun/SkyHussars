/*
 * Copyright (c) 2017, ZoltanTheHun
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

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.slf4j.LoggerFactory;
import skyhussars.SkyHussars;
import skyhussars.persistence.base.Marshal;
import skyhussars.persistence.terrain.TerrainDescriptor;

/**
 * Controller class for TerrainEd. The controller is defined from SceneBuilder.
 * This class is not thread safe
 */
public class TerrainEdController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerrainEdController.class);

    @FXML
    private TextField terrainName;
    @FXML
    private TextField terrainSize;
    @FXML
    private TextField terrainLocation;

    private OpenTheatrePopup theatreSelectorPopup;
    private NewTheatrePopup newTheatrePopup;
    private Stage stage;
    private TerrainProperties terrainProperties;

    /**
     * This method handles the event when the user clicks on the About item in
     * the menu
     */
    public void handleAboutAction() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("SkyHussars - TerrainEd");
        alert.setHeaderText("TerrainEd - A terrain editor for SkyHussars");
        alert.setContentText("Thank you for using SkyHussars and SkyHussars TerrainEd. \n Greetings from ZoltanTheHun");
        alert.showAndWait();
    }
    
    private void unableToSaveAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Unable to save");
        alert.setHeaderText("Unable to save, not all properties set.");
        alert.setContentText("Please set all properties before trying to save a new terrain.");
        alert.showAndWait();
    }

    /**
     * This method handles the event when the user clicks on the Save item in
     * the menu
     */
    public void handleSaveAction() {
        if(canSave()) save(); else unableToSaveAlert();
    }
    
    private void save(){
        File file = fileChooser("Save a terrain definition").showSaveDialog(stage);
        if (file != null) {
            LOGGER.info("Saving " + terrainProperties.name.get() + " to " + file.getAbsolutePath());
            new Persistence().persist(terrainProperties, file);
        }
    }

    private boolean canSave(){
        return terrainProperties.name.get() != null &&  
                terrainProperties.size.get() >= 1 &&
                terrainProperties.location.get() != null;
    }
    /**
     * This method handles the event when the user clicks on the Open item in
     * the menu
     */
    public void handleOpenAction() {
        File file = fileChooser("Open a terrain definition").showOpenDialog(stage);
        if(file != null) loadTheatre(file);
    }
    
    private void loadTheatre(File file){
        try { 
            LOGGER.info("Loading theatre " + file.getAbsolutePath());
            TerrainDescriptor td = Marshal.unmarshal(file, TerrainDescriptor.class);
            terrainProperties.from(td);
        } catch (Exception ex){
            LOGGER.error("Error loading " + file.getAbsolutePath(), ex);
        }
    }
    
    /**
     * This method handles the event when the user clicks on the New Theater
     * item in the menu
     */
    public void handleNewTheatreAction() {  
        newTheatrePopup.show();
    }

    /**
     * This method handles the event when the user clicks on the Open Theater
     * item in the menu
     */
    public void handleOpenTheatreAction() {
        theatreSelectorPopup.show();
    }

    private FileChooser fileChooser(String title) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(SkyHussars.APP_ROOT));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json", "*.json"));
        return chooser;
    }

    public TerrainEdController stage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public TerrainEdController popups(OpenTheatrePopup otp,NewTheatrePopup np) {
        this.theatreSelectorPopup = otp;
        this.newTheatrePopup = np;
        return this;
    }

    public TerrainEdController terrainProperties(TerrainProperties terrainProperties) {
        this.terrainProperties = terrainProperties;
        terrainName.textProperty().bindBidirectional(terrainProperties.name);
        terrainSize.textProperty().bindBidirectional(terrainProperties.size, new NumberStringConverter());
        terrainLocation.textProperty().bindBidirectional(terrainProperties.location);
        return this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
