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
import javafx.event.Event;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import skyhussars.SkyHussars;

/**
 * Controller class for TerrainEd. The controller is defined from SceneBuilder.
 * This class is not thread safe
 */
@Component
public class TerrainEdController implements Initializable,EditorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerrainEdController.class);

    @FXML
    private TextField terrainName;
    @FXML
    private TextField terrainSize;
    @FXML
    private TextField terrainLocation;
    
    private final OpenTheatrePopup theatreSelectorPopup;
    private final NewTheatrePopup newTheatrePopup;
    private Stage stage;
    private final AlertService alertService;
    private final TerrainEdService terrainEdService;

    @Autowired
    public TerrainEdController(OpenTheatrePopup theatreSelectorPopup, NewTheatrePopup newTheatrePopup, AlertService alertService,TerrainEdService terrainEdService) {
        this.theatreSelectorPopup = theatreSelectorPopup;
        this.newTheatrePopup = newTheatrePopup;
        this.alertService = alertService;
        this.terrainEdService = terrainEdService;
    }

    /**
     * This method handles the event when the user clicks on the About item in
     * the menu
     */
    public void handleAboutAction() {
        alertService.info("TerrainEd - A terrain editor for SkyHussars",
                "Thank you for using SkyHussars and SkyHussars TerrainEd. \n Greetings from ZoltanTheHun");
    }

    private void unableToSaveAlert() {
        alertService.info("Unable to save, not all properties set.",
                "Please set all properties before trying to save a new terrain.");
    }
    
    private void unableToDeleteAlert() {
        alertService.info("Unable to delete the current Theatre",
                "An unexpected error happened when trying to delete the theater. Please try restarting the application.");
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
    
    /**
     * This method handles the event when the user clicks on the Save Theater
     * item in the menu
     */
    public void handleSaveTheatreAction() {
        boolean saveSuccesful = terrainEdService.saveToFile();
        if(!saveSuccesful) unableToSaveAlert();
    }
    
        
    /**
     * This method handles the event when the user clicks on the Delete Theater
     * item in the menu
     */
    public void handleDeleteTheatreAction() {
        boolean deleteSuccesful = terrainEdService.delete();
        if(!deleteSuccesful) unableToDeleteAlert();
    }

    public void handleOnHeightMapDropped(DragEvent e) {
    }
    
            
    public void handleOnHeightMapEntered(DragEvent e) {   
    }
    
    public void handleOnHeightMapExited(DragEvent e) {
    }
    
    private FileChooser fileChooser(String title) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(SkyHussars.APP_ROOT));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json", "*.json"));
        return chooser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TerrainProperties props = terrainEdService.getTerrainProperties();
        terrainName.textProperty().bindBidirectional(props.name);
        terrainSize.textProperty().bindBidirectional(props.size, new NumberStringConverter());
        terrainLocation.textProperty().bindBidirectional(props.location);
    }

    @Override
    public EditorController with(Stage stage) {
        this.stage = stage;
        return this;
    }
}
