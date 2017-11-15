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

import skyhussars.PlaneEd;
import skyhussars.SkyHussars;
import static skyhussars.planeed.UiHelpers.*;
import java.io.File;
import static java.util.Arrays.asList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static skyhussars.utility.Collections.zList;

public class EditorView {

    private final Scene scene; 
    private final VBox mainView;
    private final AirfoilTable airfoilTable;
    
    private final Stage stage;
    public EditorView(Stage stage,int sizeX,int sizeY,AirfoilTable airfoilTable){
        mainView = new VBox();
        this.stage = stage;
        this.airfoilTable = airfoilTable;
        scene = new Scene(mainView, sizeX, sizeY);
        scene.getStylesheets().add("editor/editor.css");
    }
    
    public Scene scene(PlaneEd planeEd,PlaneProperties props){
        zList(mainView.getChildren())
                .add(menubar(planeEd))
                .add(planePropertyGrid(props))
                .add(splitView());
        return scene;
    }
    
    private Node splitView(){
        HBox splitView = new HBox();
        zList(splitView.getChildren())
                .add(airfoilTable.airfoilTable())
                .add(simulations());
        return splitView;
    }
    
    private Node simulations(){
        VBox verticalView = new VBox();
        verticalView.getChildren().addAll(createChart());
        return verticalView;
    }
    private final FileChooser openPlaneChooser = new FileChooser();
    {openPlaneChooser.setTitle("Open a plane definition");}
    {openPlaneChooser.setInitialDirectory(new File(SkyHussars.APP_ROOT));}

    private final FileChooser saveAsPlaneChooser = new FileChooser();
    {saveAsPlaneChooser.setTitle("Save a plane definition");}
    {saveAsPlaneChooser.setInitialDirectory(new File(SkyHussars.APP_ROOT));}
    {saveAsPlaneChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json","*.json"));}

    public MenuBar menubar(PlaneEd planeEd) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        ObservableList<MenuItem> mis = fileMenu.getItems();
        mis.add(loadMenu(planeEd));
        mis.add(saveMenu(planeEd));
        mis.add(saveAsMenu(planeEd));
        menuBar.getMenus().add(fileMenu);
        return menuBar;
    }

    private MenuItem loadMenu(PlaneEd planeEd) {
        MenuItem loadMenu = new MenuItem("Load plane");
        loadMenu.setOnAction(t -> planeEd.loadPlane(openPlaneChooser.showOpenDialog(stage)));
        return loadMenu;
    }

    private MenuItem saveMenu(PlaneEd planeEd) {
        MenuItem saveMenu = new MenuItem("Save");
        saveMenu.setOnAction(t -> planeEd.save());
        return saveMenu;
    }
    
    private MenuItem saveAsMenu(PlaneEd planeEd) {
        MenuItem saveAsMenu = new MenuItem("Save As");
        saveAsMenu.setOnAction(t -> planeEd.saveAs(saveAsPlaneChooser.showSaveDialog(stage)));
        return saveAsMenu;
    }

    public GridPane planePropertyGrid(PlaneProperties planeProperties) {
        return populateGrid(2, new Label("Name:"), textFieldFor(planeProperties.getName()),
                new Label("Mass, TakeOff Max: "), numberFieldFor(planeProperties.getMassTakeOffMax()),
                new Label("Mass, Gross: "), numberFieldFor(planeProperties.getMassTakeOffMax()),
                new Label("Mass, Empty: "), numberFieldFor(planeProperties.getMassEmpty())
        );
    }

    NumberAxis xAxis1 = new NumberAxis();
    NumberAxis yAxis1 = new NumberAxis();
    NumberAxis xAxis2 = new NumberAxis();
    NumberAxis yAxis2 = new NumberAxis();
    NumberAxis xAxis3 = new NumberAxis();
    NumberAxis yAxis3 = new NumberAxis();
    final LineChart<Number, Number> heightChart = new LineChart<>(xAxis1, yAxis1);
    final LineChart<Number, Number> aoaChart = new LineChart<>(xAxis2, yAxis2);
    final LineChart<Number, Number> velocityChart = new LineChart<>(xAxis3, yAxis3);
    
    public List<LineChart> createChart() {
        xAxis1.setLabel("Samples");
        xAxis2.setLabel("Samples");
        heightChart.setTitle("Level flight simulation");
        heightChart.setCreateSymbols(false);
        aoaChart.setTitle("Level flight simulation");
        aoaChart.setCreateSymbols(false);
        velocityChart.setTitle("Level flight simulation");
        velocityChart.setCreateSymbols(false);
        return asList(heightChart,aoaChart,velocityChart);
    }
    
    public EditorView clearChart(){
        heightChart.getData().clear();
        aoaChart.getData().clear();
        velocityChart.getData().clear();
        return this;
    }
    
    public EditorView heightChart(List<Double> data){
        chart(heightChart,"Height",data);
        return this;
    }
    public EditorView aoaChart(List<Float> data){
        chart(aoaChart,"AOA",data);
        return this;
    }
    
    public EditorView velocityChart(List<Float> data){
        chart(velocityChart,"Velocity (Kmh)",data);
        return this;
    }
    private static <T> LineChart chart(LineChart chart,String name,List<T> data){
        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        for(int i=0; i<data.size();i++) series.getData().add(new XYChart.Data(i, data.get(i)));
        chart.getData().add(series);
        return chart;
    }

}
