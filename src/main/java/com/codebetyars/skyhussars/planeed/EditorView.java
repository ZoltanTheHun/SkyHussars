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
package com.codebetyars.skyhussars.planeed;

import com.codebetyars.skyhussars.PlaneEd;
import static com.codebetyars.skyhussars.planeed.UiHelpers.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditorView {

    private final FileChooser fileChooser = new FileChooser();
    {fileChooser.setTitle("Open Plane Definition");}

    public MenuBar createMenuBar(Stage stage, PlaneEd planeEd) {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        fileMenu.getItems().add(loadMenuItem(stage, planeEd));
        menuBar.getMenus().add(fileMenu);

        return menuBar;
    }

    private MenuItem loadMenuItem(Stage stage, PlaneEd planeEd) {
        MenuItem loadMenu = new MenuItem("Load plane");
        loadMenu.setOnAction((ActionEvent t) -> {
            planeEd.loadPlane(fileChooser.showOpenDialog(stage));
        });
        return loadMenu;
    }

    public GridPane items(PlaneProperties planeProperties) {       
        return populateGrid(2,  new Label("Name:"), textFieldFor(planeProperties.getName()),
                new Label("Model Name:"), textFieldFor(planeProperties.getModelName()),
                new Label("Mass, TakeOff Max: "), numberFieldFor(planeProperties.getMassTakeOffMax()),
                new Label("Mass, Gross: "), numberFieldFor(planeProperties.getMassTakeOffMax()), 
                new Label("Mass, Empty: "), numberFieldFor(planeProperties.getMassEmpty())
        );
    }
    
}
