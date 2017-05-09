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
package com.codebetyars.skyhussars.planeed;

import com.codebetyars.skyhussars.PlaneEd;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class EditorView {

    private final FileChooser fileChooser = new FileChooser();
    { fileChooser.setTitle("Open Plane Definition"); }

    public MenuBar createMenuBar(Stage stage, PlaneEd planeEd) {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        fileMenu.getItems().add(createLoadMenuItem(stage, planeEd));
        menuBar.getMenus().add(fileMenu);

        return menuBar;
    }

    private MenuItem createLoadMenuItem(Stage stage, PlaneEd planeEd) {
        MenuItem loadMenu = new MenuItem("Load plane");
        loadMenu.setOnAction((ActionEvent t) -> {
            planeEd.loadPlane(fileChooser.showOpenDialog(stage));
        });
        return loadMenu;
    }

    public HBox items(PlaneProperties planeProperties) {
        Label lName = new Label("Name:");
        TextField tfName = new TextField();
        tfName.textProperty().bindBidirectional(planeProperties.getName());

        Label lModelName = new Label("Model Name:");
        TextField tfModelName = new TextField();
        tfModelName.textProperty().bindBidirectional(planeProperties.getModelName());

        Label lMassTakeOffMax = new Label("Mass, Take Off Max:");
        TextField tfMassTakeOffMax = new TextField();
        tfMassTakeOffMax.textProperty().bindBidirectional(planeProperties.getMassTakeOffMax(), new NumberStringConverter());

        Label lMassGross = new Label("Mass, Gross: ");
        TextField tfMassGross = new TextField();
        tfMassGross.textProperty().bindBidirectional(planeProperties.getMassGross(), new NumberStringConverter());

        Label lMassEmpty = new Label("Mass, Gross: ");
        TextField tfMassEmpty = new TextField();
        tfMassEmpty.textProperty().bindBidirectional(planeProperties.getMassEmpty(), new NumberStringConverter());

        HBox hb = new HBox();
        
        hb.getChildren().addAll(lName, tfName);
        hb.getChildren().addAll(lModelName, tfModelName);
        hb.getChildren().addAll(lMassTakeOffMax, tfMassTakeOffMax);
        hb.getChildren().addAll(lMassGross, tfMassGross);
        hb.getChildren().addAll(lMassEmpty, tfMassEmpty);

        hb.setSpacing(10);
        return hb;
    }

}
