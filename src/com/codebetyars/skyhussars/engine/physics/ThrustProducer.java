package com.codebetyars.skyhussars.engine.physics;

import com.jme3.math.Vector3f;

public interface ThrustProducer {
    
    public Vector3f getThrust();
    public void setThrottle(float throttle);

}
