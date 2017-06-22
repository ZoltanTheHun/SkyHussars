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

import java.util.function.BiFunction;
import java.util.function.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class WingTable {
     
    public TableView wingTable(){
        TableView table = new TableView();
        table.setEditable(true);
        
        table.getColumns().addAll(column("Wing Name","name",Wing::setName), 
                column("Direction","direction",Wing::setDirection),
                column("CoG","cog",Wing::setCog),
                column("Wing Area","wingArea",Wing::setWingArea),
                column("Incidence","incidence",Wing::setIncidence), 
                column("Aspect Ratio","aspectRatio",Wing::setAspectRatio),
                column("Damper","damper",Wing::setDamper),
                column("Dehidral Degree","dehidralDegree",Wing::setDehidralDegree));
       ObservableList<Wing> data = FXCollections.observableArrayList(new Wing("TT", "TT", "TT", "TT", "TT", "TT", "TT", "TT"));
       table.setItems(data);
       return table;
    }
    
    private TableColumn column(String name,String method,final BiFunction<Wing,String,Wing> f){
        TableColumn column = new TableColumn(name);
        column.setCellValueFactory(new PropertyValueFactory<>(method));  
        column.setEditable(true);
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(new EventHandler<CellEditEvent<Wing, String>>() {
            @Override
            public void handle(CellEditEvent<Wing, String> t) {
                Wing wing = ((Wing) t.getTableView().getItems().get(
                    t.getTablePosition().getRow()));
                f.apply(wing,t.getNewValue());
            }
        });
        return column;
    }

}
