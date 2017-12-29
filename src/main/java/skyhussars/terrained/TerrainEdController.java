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

package skyhussars.terrained;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import skyhussars.SkyHussars;


/**
 * Controller class for TerrainEd. The controller is defined from SceneBuilder.
 * This class is not thread safe
 */
public class TerrainEdController implements Initializable{
    
    @FXML
    private TextField terrainName;
    @FXML
    private TextField terrainSize;
    @FXML
    private TextField terrainLocation;
    
    private Stage stage;
    /**
     *  This method handles the event when the user clicks on the About item in the menu
     */
    public void handleAboutAction(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("SkyHussars - TerrainEd");
        alert.setHeaderText("TerrainEd - A terrain editor for SkyHussars");
        alert.setContentText("Thank you for using SkyHussars and SkyHussars TerrainEd. \n Greetings from ZoltanTheHun");
        alert.showAndWait();  
    }
    
    /**
     * This method handles the event when the user clicks on the Save item in the menu
     */
    public void handleSaveAction(){
        File file = saveAsChooser.showOpenDialog(stage);
    }
    
    private final FileChooser saveAsChooser = new FileChooser();
    {saveAsChooser.setTitle("Save a terrain definition");}
    {saveAsChooser.setInitialDirectory(new File(SkyHussars.APP_ROOT));}
    {saveAsChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json","*.json"));}

    public  TerrainEdController stage(Stage stage){this.stage = stage; return this;}
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
