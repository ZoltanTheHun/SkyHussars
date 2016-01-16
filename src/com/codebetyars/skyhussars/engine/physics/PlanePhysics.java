package com.codebetyars.skyhussars.engine.physics;

import com.jme3.scene.Spatial;

public interface PlanePhysics {
    public void update(float tpf, Spatial model);
    public void setElevator(float elevator);
    public String getInfo();
    public void setThrust(float thrust);
    public void setSpeedForward(Spatial model,float kmh);
    public void setAileron(int i);
    public String getSpeedKmH();
}
