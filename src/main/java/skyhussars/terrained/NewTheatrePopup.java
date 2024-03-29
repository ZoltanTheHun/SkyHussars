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

import java.io.IOException;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to manage the New Theatre popup available from
 * File Menu
 *
 */
@Component
public class NewTheatrePopup {

    private static final Logger LOGGER = Logger.getLogger(NewTheatrePopup.class.getName());
    
    private final Stage dialog = new Stage();
    private final FXMLSceneFactory sceneFactory;
    private final AlertService alertService;
    
    @Autowired
    public NewTheatrePopup(FXMLSceneFactory sceneFactory, AlertService alertService){
        this.sceneFactory = sceneFactory;
        this.alertService = alertService;
    }

    public void show() {
        try {          
            sceneFactory.load("/terrained/newtheatrecombo.fxml", this::prepareStage, dialog);
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            alertService.info("An error occured.", "Unable to show New Theatre dialog.");
        }
    }
    
    private Parent prepareStage(Parent root) {
        /* initialize the scene */
        Scene scene = new Scene(root);
        dialog.setTitle("New Theatre");
        dialog.setScene(scene);
        dialog.show();      
        return root;
    }
    
}
