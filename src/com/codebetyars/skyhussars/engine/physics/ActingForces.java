package com.codebetyars.skyhussars.engine.physics;

import com.jme3.math.Vector3f;

public class ActingForces {
    
    public final Vector3f vLinearComponent;
    public final Vector3f vTorqueComponent;
    
    public ActingForces(Vector3f vLinearComponent,Vector3f vTorqueComponent){
        this.vLinearComponent = vLinearComponent;
        this.vTorqueComponent = vTorqueComponent;
    }
    
}
