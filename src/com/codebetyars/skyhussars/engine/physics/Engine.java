package com.codebetyars.skyhussars.engine.physics;

import com.jme3.math.Vector3f;

public class Engine implements ThrustProducer,RigidBody{
    private Vector3f centerOfGravity;
    private Vector3f vMaxThrust;
    private float throttle = 0.0f;
    public Engine(Vector3f centerOfGravity,Vector3f vMaxThrust){
        this.centerOfGravity = centerOfGravity;
        this.vMaxThrust = vMaxThrust;
    }
    public Vector3f getCenterOfGravity() {
        return centerOfGravity;
    }
    /*throttle between 0 and 1*/
    public Vector3f getThrust() {
        return vMaxThrust.mult(throttle);
    }
    
    public void setThrottle(float throttle){
        this.throttle = throttle;
    }
    
}
