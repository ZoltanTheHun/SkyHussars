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
package com.codebetyars.skyhussars.engine.physics;

import com.codebetyars.skyhussars.engine.plane.EngineLocation;
import com.codebetyars.skyhussars.engine.plane.PlaneDescriptor;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AdvancedPlanePhysics implements PlanePhysics {

    private final static Logger logger = LoggerFactory.getLogger(AdvancedPlanePhysics.class);

    private static final Vector3f GRAVITY = new Vector3f(0f, -10f, 0f);
    private float airDensity = 1.2745f;
    private float planeFactor = 0.2566f; // cross section and drag coeff together
    private float wingArea = 22.07f; //m2
    //private float mass = 57380;//loaded: 5,738emtpy:38190; //N
    private float mass; //actually the loaded weight is  57380N, the empty weight is 38190N
    private Vector3f vWeight;
    private float aspectRatio = 6.37f;
    private float pi = 3.14f;
    private float angleOfAttack;
    private Vector3f vVelocity = new Vector3f(0f, 0f, 0f);
    private Vector3f vAcceleration = new Vector3f(0f, 0f, 0f);
    private Vector3f vDrag = new Vector3f(0f, 0f, 0f);
    private Vector3f vLift = new Vector3f(0f, 0f, 0f);
    private Vector3f vAngularAcceleration = new Vector3f(0, 0, 0);
    private Vector3f vAngularVelocity = new Vector3f(0, 0, 0);
    private float height;
    private float length = 10.49f;
    private float rPlane = 1.3f;
    private Matrix3f momentOfInertiaTensor;
    private List<Airfoil> airfoils = new ArrayList<>();
    private List<Engine> engines = new ArrayList<>();
    SymmetricAirfoil leftWing = new SymmetricAirfoil("WingA", new Vector3f(-2.0f, 0, -0.2f), wingArea / 2, 1f, aspectRatio, true, 0f);
    SymmetricAirfoil rightWing = new SymmetricAirfoil("WingB", new Vector3f(2.0f, 0, -0.2f), wingArea / 2, 1f, aspectRatio, true, 0f);
    SymmetricAirfoil horizontalStabilizer = new SymmetricAirfoil("HorizontalStabilizer", new Vector3f(0, 0, -6.0f), 5f, -3f, aspectRatio / 1.5f, false, 0f);
    SymmetricAirfoil verticalStabilizer = new SymmetricAirfoil("VerticalStabilizer", new Vector3f(0, 0, -6.0f), 5.0f, 0f, aspectRatio / 1.5f, false, 90f);

    public AdvancedPlanePhysics(Spatial model, PlaneDescriptor planeDescriptor) {
        mass = planeDescriptor.getMassGross();
        vWeight = GRAVITY.mult(mass);
        momentOfInertiaTensor = new Matrix3f((mass / 12) * (3 * rPlane * rPlane + length * length), 0f, 0f,
                0f, (mass / 12) * (3 * rPlane * rPlane + length * length), 0f,
                0f, 0f, (mass / 2) * (rPlane * rPlane));

        airfoils.add(leftWing);
        airfoils.add(rightWing);
        airfoils.add(horizontalStabilizer);
        airfoils.add(verticalStabilizer);
        for (EngineLocation engineLocation : planeDescriptor.getEngineLocations()) {
            engines.add(new Engine(engineLocation));
        }
    }

    @Override
    public void update(float tpf, Spatial model) {
        updateAuxiliary(model);
        Vector3f vLinearAcceleration = Vector3f.ZERO;
        logger.debug("Plane roll: " + (model.getLocalRotation().mult(Vector3f.UNIT_X).cross(Vector3f.UNIT_Z.negate()).angleBetween(Vector3f.UNIT_Y) * FastMath.RAD_TO_DEG));
        ActingForces engineForces = calculateEngineForces(model.getLocalRotation());
        ActingForces airfoilForces = calculateAirfoilForces(model.getLocalRotation(), vVelocity.negate());
        logger.debug("Airfoil linear: " + airfoilForces.vLinearComponent.length() + ", torque: " + airfoilForces.vTorqueComponent.length());

        vLinearAcceleration = vLinearAcceleration.add(vWeight);
        vLinearAcceleration = vLinearAcceleration.add(engineForces.vLinearComponent);
        vLinearAcceleration = vLinearAcceleration.add(airfoilForces.vLinearComponent);
        vLinearAcceleration = vLinearAcceleration.add(calculateParasiticDrag());
        vLinearAcceleration = vLinearAcceleration.divide(mass);

        vVelocity = vVelocity.add(vLinearAcceleration.mult(tpf));
        model.move(vVelocity.mult(tpf));

        vAngularAcceleration = momentOfInertiaTensor.invert().mult(airfoilForces.vTorqueComponent);
        vAngularVelocity = vAngularVelocity.add(vAngularAcceleration.mult(tpf));
        logger.debug("Angular velocity: " + vAngularVelocity);

        moderateRoll();
        model.rotate(vAngularVelocity.x * tpf, vAngularVelocity.y * tpf, vAngularVelocity.z * tpf);
    }

    private void moderateRoll() {
        if (vAngularVelocity.x > 2) {
            vAngularVelocity.x = 2;
        } else if (vAngularVelocity.x < -2) {
            vAngularVelocity.x = -2;
        }
        if (vAngularVelocity.y > 2) {
            vAngularVelocity.y = 2;
        } else if (vAngularVelocity.y < -2) {
            vAngularVelocity.y = -2;
        }
        if (vAngularVelocity.z > 2) {
            vAngularVelocity.z = 2;
        } else if (vAngularVelocity.z < -2) {
            vAngularVelocity.z = -2;
        }
    }

    private ActingForces calculateEngineForces(Quaternion situation) {
        Vector3f vLinearAcceleration = Vector3f.ZERO;
        for (Engine engine : engines) {
            vLinearAcceleration = vLinearAcceleration.add(situation.mult(engine.getThrust()));
        }
        return new ActingForces(vLinearAcceleration, Vector3f.ZERO);
    }

    private ActingForces calculateAirfoilForces(Quaternion situation, Vector3f vFlow) {
        Vector3f vLinearAcceleration = Vector3f.ZERO;
        Vector3f vTorque = Vector3f.ZERO;
        for (Airfoil airfoil : airfoils) {
            Vector3f airfoilForce = airfoil.calculateResultantForce(airDensity, vFlow, situation, vAngularVelocity);
            logger.debug("Airfoilforce points to: " + airfoilForce.toString()/*.normalize().dot(situation.mult(Vector3f.UNIT_Y))*/);
            vLinearAcceleration = vLinearAcceleration.add(airfoilForce);
            airfoilForce = situation.inverse().mult(airfoilForce);
            Vector3f distFromCenter = airfoil.getCenterOfGravity();
            logger.debug("Airfoilforce points to: " + airfoilForce.toString());
            vTorque = vTorque.add(distFromCenter.cross(airfoilForce));
        }
        return new ActingForces(vLinearAcceleration, vTorque);
    }

    private Vector3f calculateParasiticDrag() {
        return vVelocity.negate().normalize().mult(airDensity * planeFactor * vVelocity.lengthSquared());
    }

    private void updateAuxiliary(Spatial model) {
        updateHelpers(model);
        updateAngleOfAttack(model);
        updatePlaneFactor();
    }

    private void updateAngleOfAttack(Spatial model) {
        angleOfAttack = model.getLocalRotation().mult(Vector3f.UNIT_Z).angleBetween(vVelocity.normalize()) * FastMath.RAD_TO_DEG;
        float np = model.getLocalRotation().mult(Vector3f.UNIT_Y).dot(vVelocity.negate().normalize());
        if (np < 0) {
            angleOfAttack = -angleOfAttack;
        }
    }

    public void updatePlaneFactor() {
        planeFactor = 0.2566f;
        /*if (angleOfAttack > 30 || angleOfAttack < - 30) {
         planeFactor = wingArea * 1.2f;     // let's pretend rest of the body area is half of body area
         } else {
         planeFactor = 0.2566f;
         }*/
    }

    private void updateHelpers(Spatial model) {
        height = model.getWorldTranslation().getY();
        airDensity = WorldPhysicsData.getAirDensity((int) height);
    }

    @Override
    public void setThrust(float throttle) {
        for (Engine engine : engines) {
            engine.setThrottle(throttle);
        }
    }

    @Override
    public String getSpeedKmH() {
        NumberFormat fractionless = NumberFormat.getInstance();
        fractionless.setMaximumFractionDigits(0);
        fractionless.setMinimumIntegerDigits(3);
        NumberFormat bigFractionless = NumberFormat.getInstance();
        bigFractionless.setMaximumFractionDigits(0);
        bigFractionless.setMinimumIntegerDigits(6);
        return fractionless.format(vVelocity.length() * 3.6);
    }

    @Override
    public String getInfo() {
        NumberFormat fraction2Format = NumberFormat.getInstance();
        fraction2Format.setMaximumFractionDigits(2);
        fraction2Format.setMinimumFractionDigits(2);
        NumberFormat accF = NumberFormat.getInstance();
        accF.setMaximumFractionDigits(2);
        accF.setMinimumFractionDigits(2);
        accF.setMaximumIntegerDigits(3);
        accF.setMinimumIntegerDigits(3);
        NumberFormat fractionless = NumberFormat.getInstance();
        fractionless.setMaximumFractionDigits(0);
        fractionless.setMinimumIntegerDigits(3);
        NumberFormat bigFractionless = NumberFormat.getInstance();
        bigFractionless.setMaximumFractionDigits(0);
        bigFractionless.setMinimumIntegerDigits(6);
        return "Thrust: " + engines.get(0).getThrust().length()
                + ", Acceleration: " + accF.format(vAcceleration.length())
                + ", CurrentSpeed: " + fractionless.format(vVelocity.length())
                + ", CurrentSpeed km/h: " + fractionless.format(vVelocity.length() * 3.6)
                + ", Drag: " + bigFractionless.format(vDrag.length())
                + ", Height: " + fractionless.format(height)
                + ", Lift: " + bigFractionless.format(vLift.length())
                //+ ", AirSpeed: " + fractionless.format(airSpeed)
                + ", AOA: " + fractionless.format(angleOfAttack)
                + ", AngularVelocity: " + fraction2Format.format(vAngularVelocity.length())
                + ", AngularAcceleration: " + fraction2Format.format(vAngularAcceleration.length());
    }

    @Override
    public void setElevator(float aileron) {
        horizontalStabilizer.controlAileron(5f * aileron);
    }

    @Override
    public void setSpeedForward(Spatial model, float kmh) {
        vVelocity = model.getLocalRotation().mult(Vector3f.UNIT_Z).normalize().mult(kmh / 3.6f);
    }

    @Override
    public void setAileron(float aileron) {
        leftWing.controlAileron(aileron);
        rightWing.controlAileron(-1f * aileron);
    }

    public void setRudder(float aileron) {
        verticalStabilizer.controlAileron(aileron);
    }

    @Override
    public Vector3f getVVelovity() {
        return vVelocity;
    }
}
