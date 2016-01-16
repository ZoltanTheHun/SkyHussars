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

package com.codebetyars.skyhussars.engine;

import com.codebetyars.skyhussars.engine.physics.AdvancedPlanePhysics;
import com.codebetyars.skyhussars.engine.physics.PlanePhysics;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class Plane {

    private String name;
    private Spatial model;
    private PlanePhysics physics;
    private AudioNode engineSound;

    public void updatePlanePhysics(float tpf) {
        physics.update(tpf, model);
        System.out.println(getInfo());
        
    }
    Vector3f accG = new Vector3f(0f, -10f, 0f);

    public String getInfo() {
        return physics.getInfo();
    }

    public Plane(DataManager dataManager) {
        this.model = dataManager.modelManager().model("p80","p80_material");
        this.physics = new AdvancedPlanePhysics(model);
        this.engineSound = dataManager.soundManager().sound("engine");
        this.physics.setThrust(1.0f);
        this.physics.setSpeedForward(model, 300f);
        this.model.rotate(0,0,0*FastMath.DEG_TO_RAD);
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
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("Speed0")) {
                physics.setThrust(0.0f);
                engineSound.setPitch(0.5f);
            }
            if (name.equals("Speed20")) {
                physics.setThrust(0.2f);
                engineSound.setPitch(0.7f);
            }
            if (name.equals("Speed40")) {
                physics.setThrust(0.4f);
                engineSound.setPitch(0.8f);
            }
            if (name.equals("Speed60")) {
                physics.setThrust(0.6f);
                engineSound.setPitch(1.0f);
            }
            if (name.equals("Speed80")) {
                physics.setThrust(0.8f);
                engineSound.setPitch(1.3f);
            }
            if (name.equals("Speed100")) {
                physics.setThrust(1.0f);
                engineSound.setPitch(1.5f);
            }

        }
    };

    public AnalogListener getAnalogListener() {
        return analogListener;
    }

    public void tempTerrainCollisionMechanics(TerrainManager terrain) {
        //terrain.collideWith(model, null); 
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
        }
    };
            
    public ActionListener getActionListener(){
        return actionListener;
    }
    
    public void setHeight(int height){
        model.getLocalTranslation().setY(height);
    }
    
    public void setLocation(int x, int z){
        model.move(x, model.getLocalTranslation().y, z);
    }
    public float getHeight(){
       return  model.getLocalTranslation().y;
    }
    public Vector3f getLocation(){
        return model.getLocalTranslation();
    }
    public Vector2f getLocation2D(){
        return new Vector2f(model.getLocalTranslation().x,model.getLocalTranslation().z);
    }
    
    public String getSpeedKmH(){
        return physics.getSpeedKmH();
    }
}
