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

import com.codebetyars.skyhussars.engine.data.Engine;
import com.codebetyars.skyhussars.engine.DataManager;
import com.codebetyars.skyhussars.engine.TerrainManager;
import com.codebetyars.skyhussars.engine.weapons.Missile;
import com.codebetyars.skyhussars.engine.weapons.Gun;
import com.codebetyars.skyhussars.engine.physics.AdvancedPlanePhysics;
import com.codebetyars.skyhussars.engine.physics.PlanePhysics;
import com.codebetyars.skyhussars.engine.weapons.Bomb;
import com.jme3.audio.AudioNode;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;

public class Plane {

    private PlaneDescriptor planeDescriptor;
    private String name;
    private Spatial model;
    private PlanePhysics physics;
    private AudioNode engineSound;
    private boolean firing;
    private List<Gun> guns;
    private List<Missile> missiles;
    private List<Bomb> bombs;
    private List<Engine> engines;
    private boolean fireTriggerPushed = false;

    public void updatePlanePhysics(float tpf) {
        physics.update(tpf, model);
        System.out.println(getInfo());

    }
    Vector3f accG = new Vector3f(0f, -10f, 0f);

    public String getInfo() {
        return physics.getInfo();
    }

    public Plane(DataManager dataManager) {
        this.model = dataManager.modelManager().model("p80", "p80_material");
        this.physics = new AdvancedPlanePhysics(model);
        this.engineSound = dataManager.soundManager().sound("engine");
        this.physics.setThrust(1.0f);
        this.physics.setSpeedForward(model, 300f);
        this.model.rotate(0, 0, 0 * FastMath.DEG_TO_RAD);
    }

    public Spatial getModel() {
        return model;
    }

    public void update(float tpf) {
        updatePlanePhysics(tpf);
    }

    public AudioNode getEngineSound() {
        return engineSound;
    }

    /**
     * This method provides throttle controls for the airplane
     * 
     * @param throttle - Amount of throttle applied, should be between 0.0f and
     * 1.0f
     */
    public void setThrottle(float throttle) {
        /* maybe it would be better to normalize instead of throwing an exception*/
        if(throttle < 0.0f || throttle > 1.0f){
            throw new IllegalArgumentException();
        }
        physics.setThrust(throttle);
        engineSound.setPitch(0.5f + throttle);
    }
    
    public void setAileron(float aileron){
         physics.setAileron(aileron);
    }
    
    public void setElevator(float elevator){
        physics.setElevator(elevator);
    }
    
    public void setRudder(float rudder){
    }
       
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            System.out.println(name + isPressed);
            if (name.equals("NoseUp") && isPressed) {
                physics.setElevator(-1f);
            } else if (name.equals("NoseUp") && !isPressed) {
                physics.setElevator(0f);
            }
            if (name.equals("NoseDown") && isPressed) {
                physics.setElevator(1f);
            } else if (name.equals("NoseDown") && !isPressed) {
                physics.setElevator(0f);
            }
            if (name.equals("RotateLeft") && isPressed) {
                physics.setAileron(-1);
            } else if (name.equals("RotateLeft") && !isPressed) {
                physics.setAileron(0);
            }
            if (name.equals("RotateRight") && isPressed) {
                physics.setAileron(1);
            } else if (name.equals("RotateRight") && !isPressed) {
                physics.setAileron(0);
            }
            if (name.equals("Fire") && isPressed) {
                firing = true;
            } else if (name.equals("Fire") && !isPressed) {
                firing = false;
            }
        }
    };

    public ActionListener getActionListener() {
        return actionListener;
    }

    public void setHeight(int height) {
        model.getLocalTranslation().setY(height);
    }

    public void setLocation(int x, int z) {
        model.move(x, model.getLocalTranslation().y, z);
    }

    public float getHeight() {
        return model.getLocalTranslation().y;
    }

    public Vector3f getLocation() {
        return model.getLocalTranslation();
    }

    public Vector2f getLocation2D() {
        return new Vector2f(model.getLocalTranslation().x, model.getLocalTranslation().z);
    }

    public String getSpeedKmH() {
        return physics.getSpeedKmH();
    }
}
