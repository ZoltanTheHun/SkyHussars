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
package skyhussars;

import skyhussars.engine.loader.PlaneDescriptorMarshal;
import skyhussars.engine.plane.PlaneDescriptor;
import skyhussars.planeed.EditorView;
import skyhussars.planeed.PlaneEdState;
import skyhussars.planeed.PlaneProperties;
import skyhussars.planeed.AirfoilTable;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import skyhussars.engine.physics.PlanePhysicsImpl;
import skyhussars.engine.physics.PlaneResponse;
import skyhussars.engine.physics.environment.AtmosphereImpl;
import skyhussars.engine.physics.environment.Environment;
import skyhussars.engine.plane.PlaneFactory;

public class PlaneEd extends Application {

    private final PlaneProperties planeProperties = new PlaneProperties();

    public static void main(String[] args) {
        launch(args);
    }

    private PlaneEdState state = new PlaneEdState();
    private final PlaneDescriptorMarshal pdl = new PlaneDescriptorMarshal();
    private File openFile;
    private final AirfoilTable airfoilTable = new AirfoilTable();
    private PlanePhysicsImpl planePhysics;
    private final Environment env = new Environment(10, new AtmosphereImpl());
    private final List<Float> heights = new LinkedList<>();
    private EditorView ev = new EditorView();
    
    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("SkyHussars PlaneEd");
        VBox root = new VBox();
        root.getChildren().add(ev.createMenuBar(stage, this));
        root.getChildren().add(ev.items(planeProperties));
        root.getChildren().add(airfoilTable.wingTable());
        root.getChildren().add(ev.createChart());
        Scene scene = new Scene(root, 500, 400);
        scene.getStylesheets().add("editor/editor.css");
        stage.setScene(scene);
        
        stage.show();
    }

    public void loadPlane(File file) {
        state = state.planeDescriptor(pdl.unmarshal(file)).openFile(file);
        planePhysics = new PlaneFactory().createPlane(state.planeDescriptor().get());
        PlaneResponse rsp = new PlaneResponse();
        rsp = rsp.velocity(rsp.forwardNorm().mult(300)).height(1000);
        ev.clearChart();
        ev.addChartElement(0, rsp.height());
        heights.add(rsp.height());
        for(int i = 0;i< /*18000*/6000;i++){
            rsp = planePhysics.update(1f/60f, env, rsp); 
            
            if(i%60 == 0/* i>6240 && i< 6360*/) {
               // System.out.println(rsp.toString());
                /*6240 : Velocity: 1327.6056, aoa: 2.6079698, height: 2255.194
6300 : Velocity: 1376.2584, aoa: 3.086545, height: 1916.9574
6360 : Velocity: 1409.0471, aoa: 50.701897, height: 1641.993*/
                System.out.println(i + " : Velocity: " + rsp.velicityKmh() 
                + ", aoa: " + rsp.aoa
                + ", height: " + rsp.height());
                ev.addChartElement(i/60, rsp.height());
            }
        }

        heights.add(rsp.height());
        planeProperties.getName().setValue(state.planeDescriptor().map(p -> p.getName()).orElse(""));
        planeProperties.getMassTakeOffMax().setValue(state.planeDescriptor().map(p -> p.getMassTakeOffMax()).orElse(0.f));
        planeProperties.getMassGross().setValue(state.planeDescriptor().map(p -> p.getMassGross()).orElse(0.f));
        planeProperties.getMassEmpty().setValue(state.planeDescriptor().map(p -> p.getMassEmpty()).orElse(0.f));
        state.planeDescriptor().map(pd -> airfoilTable.airfoils(pd.getAirfolDescriptors()));
        openFile = file; // we set this only if no issue unmarshalling the file      
    }
    

    public void save() {
        PlaneDescriptor pd = state.planeDescriptor().orElseThrow(IllegalStateException::new);
        PlaneProperties pp = planeProperties;
        pd.setName(pp.getName().getValue());
        pd.setMassTakeOffMax(pp.getMassTakeOffMax().getValue());
        pd.setMassGross(pp.getMassGross().getValue());
        pd.setMassEmpty(pp.getMassEmpty().getValue());
        if(openFile != null) pdl.marshal(pd, openFile); //this is fine for now
    }
}
