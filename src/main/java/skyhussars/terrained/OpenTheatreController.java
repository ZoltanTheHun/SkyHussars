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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import skyhussars.engine.terrain.TheatreLoader;

/**
 * Controller class for the Open Theatre menu window. 
 */
public class OpenTheatreController implements Initializable {
    
    @FXML
    private ComboBox<String> theatreDropdown;

    private TheatreLoader theatreLoader;
    private TerrainProperties terrainProperties;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {}
    
    public OpenTheatreController theatreLoader(TheatreLoader theatreLoader){
        if(!theatreLoader.theatres().isEmpty()){
            theatreDropdown.setItems(FXCollections.observableArrayList(theatreLoader.theatres()));
            theatreDropdown.setValue(theatreLoader.theatres().get(0));
        }
        this.theatreLoader = theatreLoader;
        return this;
    }
    
    public OpenTheatreController terrainProperties(TerrainProperties terrainProperties){
        this.terrainProperties = terrainProperties;
        return this;
    }
    
    public void loadTheatre(){
        this.terrainProperties.name.set("This is a test load");
    }
    
    public void cancel(){
        
    }
}
