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

import com.codebetyars.skyhussars.engine.plane.AirfoilDescriptor;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class AirfoilTable {
     
    private  ObservableList<Airfoil> data = FXCollections.observableArrayList();
    private  TableView table = new TableView();
    
    public TableView wingTable(){
        table.setEditable(true);
        table.getColumns().addAll(column("Wing Name","name",Airfoil::setName), 
                column("Direction","direction",Airfoil::setDirection),
                column("CoG","cog",Airfoil::setCog),
                column("Wing Area","wingArea",Airfoil::setWingArea),
                column("Incidence","incidence",Airfoil::setIncidence), 
                column("Aspect Ratio","aspectRatio",Airfoil::setAspectRatio),
                column("Damper","damper",Airfoil::setDamper),
                column("Dehidral Degree","dehidralDegree",Airfoil::setDehidralDegree)); 
       table.setItems(data);
       return table;
    }
            
    private TableColumn column(String name,String method,final BiFunction<Airfoil,String,Airfoil> f){
        TableColumn column = new TableColumn(name);
        column.setCellValueFactory(new PropertyValueFactory<>(method));  
        column.setEditable(true);
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(new EventHandler<CellEditEvent<Airfoil, String>>() {
            @Override
            public void handle(CellEditEvent<Airfoil, String> t) {
                Airfoil wing = ((Airfoil) t.getTableView().getItems().get(
                    t.getTablePosition().getRow()));
                f.apply(wing,t.getNewValue());
            }
        });
        return column;
    }

    public AirfoilTable airfoils(List<AirfoilDescriptor> afDescs){
        data.clear();
        data.addAll(toAirfoils(afDescs));
        return this;
    }
    
    private List<Airfoil> toAirfoils(List<AirfoilDescriptor> afDescs){
        return afDescs.stream().map( afDesc -> 
                new Airfoil().setName(afDesc.getName())
                            .setAspectRatio(Float.toString(afDesc.getAspectRatio()))
                            .setCog(afDesc.getCog().toString())
                            .setDehidralDegree(Float.toString(afDesc.getDehidralDegree()))
                            .setIncidence(Float.toString(afDesc.getIncidence()))
                            .setWingArea(Float.toString(afDesc.getWingArea()))
                            .setDamper(Boolean.toString(afDesc.isDamper()))
                            .setDirection(afDesc.getDirection().toString())
        ).collect(Collectors.toList());
    }
}
