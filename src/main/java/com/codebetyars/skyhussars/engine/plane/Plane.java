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
package com.codebetyars.skyhussars.engine.plane;

import com.codebetyars.skyhussars.engine.mission.PlaneMissionDescriptor;
import com.codebetyars.skyhussars.engine.physics.AdvancedPlanePhysics;
import com.codebetyars.skyhussars.engine.physics.Airfoil;
import com.codebetyars.skyhussars.engine.physics.Engine;
import com.codebetyars.skyhussars.engine.physics.SymmetricAirfoil;
import com.codebetyars.skyhussars.engine.weapons.ProjectileManager;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingVolume;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Plane {

    private final static Logger logger = LoggerFactory.getLogger(Plane.class);

    private final PlaneDescriptor planeDescriptor;
    private PlaneMissionDescriptor planeMissionDescriptor;
    private String name;
    private final AdvancedPlanePhysics physics;
    private final AudioNode engineSound;
    private final AudioNode gunSound;
    private List<GunGroup> gunGroups;
    private final List<Engine> engines = new ArrayList<>();
    private boolean firing = false;
    private final ProjectileManager projectileManager;
    private boolean crashed = false;
    private boolean shotdown = false;
    private ParticleEmitter fireEffect;
    private final PlaneGeometry geom;

    public void updatePlanePhysics(float tpf) {
        physics.update(tpf);
        logger.debug(getInfo());
    }

    Vector3f accG = new Vector3f(0f, -10f, 0f);

    public String getInfo() {
        return physics.getInfo();
    }

    public PlaneGeometry planeGeometry() {
        return geom;
    }

    public void fireEffect(ParticleEmitter fireEffect) {
        this.fireEffect = fireEffect;
    }

    public void planeMissinDescriptor(PlaneMissionDescriptor planeMissionDescriptor) {
        this.planeMissionDescriptor = planeMissionDescriptor;
    }

    public PlaneMissionDescriptor planeMissionDescriptor() {
        return planeMissionDescriptor;
    }
    private float wingArea = 22.07f; //m2
    private float aspectRatio = 6.37f;
    private SymmetricAirfoil leftWing = new SymmetricAirfoil("WingA", new Vector3f(-2.0f, 0, -0.2f), wingArea / 2, 1f, aspectRatio, true, 0f);
    private SymmetricAirfoil rightWing = new SymmetricAirfoil("WingB", new Vector3f(2.0f, 0, -0.2f), wingArea / 2, 1f, aspectRatio, true, 0f);
    private SymmetricAirfoil horizontalStabilizer = new SymmetricAirfoil("HorizontalStabilizer", new Vector3f(0, 0, -6.0f), 5f, -3f, aspectRatio / 1.5f, false, 0f);
    private SymmetricAirfoil verticalStabilizer = new SymmetricAirfoil("VerticalStabilizer", new Vector3f(0, 0, -6.0f), 5.0f, 0f, aspectRatio / 1.5f, false, 90f);

    public Plane(Spatial model, PlaneDescriptor planeDescriptor, AudioNode engineSound, AudioNode gunSound, ProjectileManager projectileManager, Geometry cockpit) {
        this.planeDescriptor = planeDescriptor;
        this.engineSound = engineSound;
        engineSound.setLocalTranslation(0, 0, - 5);
        //engineSound.setPositional(true);
        this.gunSound = gunSound;
        //test model is backwards
        model.rotate(0, 0, 0 * FastMath.DEG_TO_RAD);
        this.projectileManager = projectileManager;
        initializeGunGroup();
        geom = new PlaneGeometry();
        geom.attachSpatialToCockpitNode(cockpit);
        geom.attachSpatialToModelNode(model);
        geom.attachSpatialToRootNode(engineSound);
        geom.attachSpatialToRootNode(gunSound);
        List<Airfoil> airfoils = new ArrayList<>();
        airfoils.add(leftWing);
        airfoils.add(rightWing);
        airfoils.add(horizontalStabilizer);
        airfoils.add(verticalStabilizer);
        for (EngineLocation engineLocation : planeDescriptor.getEngineLocations()) {
            engines.add(new Engine(engineLocation, 1.0f));
        }
        Quaternion rotation = Quaternion.IDENTITY.clone();//geom.root() .getLocalRotation(); 

        Vector3f translation = geom.root().getLocalTranslation();
        this.physics = new AdvancedPlanePhysics(rotation, translation, planeDescriptor.getMassGross(), engines, airfoils);
        this.physics.setSpeedForward(model, 300f);
    }

    private void initializeGunGroup() {
        gunGroups = new ArrayList<>();
        for (GunGroupDescriptor gunGroupDescriptor : planeDescriptor.getGunGroupDescriptors()) {
            gunGroups.add(new GunGroup(gunGroupDescriptor, projectileManager));
        }
    }

    public BoundingVolume getHitBox() {
        return geom.root().getWorldBound();
    }

    public void hit() {
        if (!shotdown) {
            geom.attachSpatialToRootNode(fireEffect);
            fireEffect.emitAllParticles();
            for (Engine engine : engines) {
                engine.damage(1.0f);
            }
        }
        shotdown = true;
    }

    public void update(float tpf) {
        physics.updateScene(geom.root());
        if (!crashed) {
            gunGroups.parallelStream().forEach(gunGroup -> {
                gunGroup.firing(firing, geom.root().getLocalTranslation(),
                        physics.getVVelovity(), geom.root().getWorldRotation());
            });
        }
    }

    public void updateSound() {
        if (!crashed) {
            engineSound.play();
            if (firing) {
                gunSound.play();
            } else {
                gunSound.stop();
            }
        } else {
            engineSound.pause();
            gunSound.stop();
        }
    }

    /**
     * This method provides throttle controls for the airplane
     *
     * @param throttle - Amount of throttle applied, should be between 0.0f and
     * 1.0f
     */
    public void setThrottle(float throttle) {
        /* maybe it would be better to normalize instead of throwing an exception*/
        if (throttle < 0.0f || throttle > 1.0f) {
            throw new IllegalArgumentException();
        }
        for (Engine engine : engines) {
            engine.setThrottle(throttle);
        }
        engineSound.setPitch(0.5f + throttle);
    }

    public void setAileron(float aileron) {
        leftWing.controlAileron(aileron);
        rightWing.controlAileron(-1f * aileron);

    }

    public void setElevator(float elevator) {
        horizontalStabilizer.controlAileron(5f * elevator);
    }

    public void setRudder(float rudder) {
        verticalStabilizer.controlAileron(rudder);
    }

    public void setHeight(int height) {
        Vector3f translation = geom.root().getLocalTranslation();
        setLocation((int) translation.getX(), height, (int) translation.getZ());
    }

    public void setLocation(int x, int z) {
        setLocation(x, (int) geom.root().getLocalTranslation().y, z);
    }

    public void setLocation(int x, int y, int z) {
        setLocation(new Vector3f(x, y, z));
    }

    public void setLocation(Vector3f location) {
        geom.root().setLocalTranslation(location);
        physics.setTranslation(location);

    }

    public float getHeight() {
        return geom.root().getLocalTranslation().y;
    }

    public Vector3f getLocation() {
        return geom.root().getLocalTranslation();
    }

    public Vector2f getLocation2D() {
        return new Vector2f(geom.root().getLocalTranslation().x, geom.root().getLocalTranslation().z);
    }

    public String getSpeedKmH() {
        return physics.getSpeedKmH();
    }

    public void setFiring(boolean trigger) {
        firing = trigger;
    }

    public void crashed(boolean crashed) {
        this.crashed = crashed;
    }

    public boolean crashed() {
        return crashed;
    }
}
