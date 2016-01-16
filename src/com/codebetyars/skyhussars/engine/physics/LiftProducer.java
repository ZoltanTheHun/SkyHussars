package com.codebetyars.skyhussars.engine.physics;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public interface LiftProducer {
        public Vector3f calculateResultantForce(float airDensity, Vector3f vVelocity, Quaternion situation, Vector3f angularVelocity);
}
