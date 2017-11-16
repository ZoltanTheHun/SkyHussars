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
package skyhussars.planeed;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.ERROR;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;


public class UiHelpers {
    
    public static TextField textFieldFor(Property p){
        TextField tf = new TextField();
        tf.setMinWidth(300);
        tf.textProperty().bindBidirectional(p);
        return tf;
    }
    
    public static TextField numberFieldFor(Property p){
        TextField tf = new TextField();
        tf.textProperty().bindBidirectional(p, new NumberStringConverter());
        return tf;
    }

    public static GridPane populateGrid(int nElementInRow, Node... nodes) {
        GridPane gp = new GridPane();
        int cRow = 0;
        for(int i=0;i<nodes.length;i=i+nElementInRow){
            Node[] n = new Node[nElementInRow];
            for(int h = 0;h<nElementInRow && h+i < nodes.length ;h++){
                n[h] = nodes[h+i];
            }
            gp.addRow(cRow++,n);
        }
        return gp;
    }
    
    public static float kmhToMs(float kmh){ return kmh / 3.6f;}
    public static float msToKmh(float ms) { return ms * 3.6f; }
    
    public static void displayError(String error){
        Alert alert = new Alert(ERROR);
        alert.setTitle("An error occured");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }
}
