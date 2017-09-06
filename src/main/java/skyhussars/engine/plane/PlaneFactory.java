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
package skyhussars.engine.plane;

import skyhussars.engine.DataManager;
import skyhussars.engine.ModelManager;
import skyhussars.engine.physics.Aileron;
import skyhussars.engine.physics.Airfoil;
import skyhussars.engine.physics.Engine;
import skyhussars.engine.physics.SymmetricAirfoil;
import skyhussars.engine.plane.instruments.BarometricAltimeter;
import skyhussars.engine.plane.instruments.Instruments;
import skyhussars.engine.sound.AudioHandler;
import skyhussars.engine.sound.SoundManager;
import skyhussars.engine.weapons.ProjectileManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import skyhussars.engine.physics.LiftCoefficient;

@Component
public class PlaneFactory {

    @Autowired
    private DataManager dataManager;
    
    @Autowired
    private ModelManager modelManager;

    @Autowired
    private SoundManager soundManager;

    @Autowired
    private ProjectileManager projectileManager;
    
    private LiftCoefficient testCoef = new LiftCoefficient("Testshape",  new float[]{0, 2, 4, 6, 8, 10, 15, 30}, new float[]{0.5f}, new float[][]{{0f, 0.246f, 0.475f, 0.68f, 0.775f, 0.795f, 0.82f, 0.8f}});

    public Plane createPlane(String planeType) {
        PlaneDescriptor planeDescriptor = dataManager.planeRegistry().planeDescriptor(planeType);

        AudioHandler engineSound = soundManager.sound("engine");
        AudioHandler gunSound = soundManager.sound("gun");
        Box box = new Box(6f, 1f, 4f);
        Instruments instruments = new Instruments(new BarometricAltimeter(0));
        Plane plane = new Plane(
                        airfoils(planeDescriptor.getAirfolDescriptors()),
                        engineSound,
                        gunSound, projectileManager,
                        planeGeometry(),
                        instruments,
                        engines(planeDescriptor.getEngineLocations()),
                        gunGroups(planeDescriptor.getGunGroupDescriptors()),
                        planeDescriptor.getMassGross());
        plane.fireEffect(dataManager.fireEffect());
        return plane;
    }
    
    private List<Engine> engines(List<EngineLocation> engineLocations){
        return engineLocations.stream().map(el -> new Engine(el, 1.0f)).collect(Collectors.toList());
    }

    private List<Airfoil> airfoils(List<AirfoilDescriptor> afs){
        return afs.stream().map(af -> 
            new Aileron(new SymmetricAirfoil.Builder()
                    .name(af.getName())
                    .cog(af.getCog())
                    .wingArea(af.getWingArea())
                    .incidence(af.getIncidence())
                    .aspectRatio(af.getAspectRatio())
                    .damper(af.isDamper())
                    .dehidralDegree(af.getDehidralDegree())
                    .direction(af.getDirection())
                    .liftCoefficient(testCoef)
                    .dampingFactor(0.01f).build(),af.getDirection(),1f)
        ).collect(Collectors.toList());
    }
    
    private PlaneGeometry planeGeometry(){
        Spatial model = modelManager.model("p80", "p80_material").clone();
        model.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        Spatial cockpitModel = modelManager.model("p80cabin2", "p80_material").clone();
        //cockpitModel.setCullHint(Spatial.CullHint.Always);
        cockpitModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        return new PlaneGeometry()
                .attachSpatialToCockpitNode(cockpitModel)
                .attachSpatialToCockpitNode(dataManager.getCockpit())
                .attachSpatialToModelNode(model);
    }
    
    private List<GunGroup> gunGroups(List<GunGroupDescriptor> gunGroupDescriptors) {
         return gunGroupDescriptors.stream().map(ggd  ->new GunGroup(ggd, projectileManager)).collect(Collectors.toList());
    }

}

