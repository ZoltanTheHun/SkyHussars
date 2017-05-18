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

import com.codebetyars.skyhussars.engine.physics.environment.Environment;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class PlanePhysicsImpl implements PlanePhysics {

    private final static Logger logger = LoggerFactory.getLogger(PlanePhysicsImpl.class);

    private float airDensity = 1.2745f;
    private float planeFactor = 0.2566f; // cross section and drag coeff together
    //private float mass = 57380;//loaded: 5,738emtpy:38190; //N
    private final float mass; //actually the loaded weight is  57380N, the empty weight is 38190N
    private float pi = 3.14f;
    private float angleOfAttack;

    private Vector3f vVelocity = new Vector3f(0f, 0f, 0f);
    private Vector3f vAngularAcceleration = new Vector3f(0, 0, 0);
    private Vector3f vAngularVelocity = new Vector3f(0, 0, 0);

    private float height;
    private final float length = 10.49f;
    private final float rPlane = 1.3f;
    private final Matrix3f momentOfInertiaTensor;
    private List<Airfoil> airfoils = new ArrayList<>();
    private List<Engine> engines = new ArrayList<>();

    private Quaternion rotation;

    public void setRotation(Quaternion rotation) {
        this.rotation = new Quaternion(rotation);
    }

    public void setTranslation(Vector3f translation) {
        this.translation = new Vector3f(translation);
    }
    private Vector3f translation;
    private final float tick = (float) 1 / (float) 60;

    public PlanePhysicsImpl(Quaternion rotation,
            Vector3f translation,
            float mass,
            List<Engine> engines, List<Airfoil> airfoils) {
        this.mass = mass;
        momentOfInertiaTensor = new Matrix3f((mass / 12) * (3 * rPlane * rPlane + length * length), 0f, 0f,
                0f, (mass / 12) * (3 * rPlane * rPlane + length * length), 0f,
                0f, 0f, (mass / 2) * (rPlane * rPlane));
        this.airfoils = airfoils;
        this.engines = engines;
        this.rotation = new Quaternion(rotation);
        this.translation = new Vector3f(translation);
    }

    Quaternion tempQuaternion = new Quaternion();

    @Override
    public void update(float tpf, Environment environment) {
        updateAuxiliary(rotation, translation, environment);
        logger.debug("Plane roll: " + (rotation.mult(Vector3f.UNIT_X).cross(Vector3f.UNIT_Z.negate()).angleBetween(Vector3f.UNIT_Y) * FastMath.RAD_TO_DEG));
        ActingForces engineForces = engineForces(rotation);
        ActingForces airfoilForces = airfoilForces(rotation, vVelocity.negate());
        logger.debug("Airfoil linear: " + airfoilForces.vLinearComponent.length() + ", torque: " + airfoilForces.vTorqueComponent.length());
        airfoils.stream().forEach(a -> a.tick(airDensity,  vVelocity.negate(), rotation, vAngularVelocity));
        airfoils.stream().map(a -> a.linearAcceleration());

        Vector3f vLinearAcceleration = Vector3f.ZERO
                .add(environment.gravity().mult(mass))
                .add(engineForces.vLinearComponent)
                .add(airfoilForces.vLinearComponent)
                .add(calculateParasiticDrag()).divide(mass);
        logger.debug("Linear velocity: " + vVelocity + ", linear acceleration: " + vLinearAcceleration);

        vVelocity = vVelocity.add(vLinearAcceleration.mult(tpf));
        vAngularAcceleration = momentOfInertiaTensor.invert().mult(airfoilForces.vTorqueComponent);
        vAngularVelocity = vAngularVelocity.add(vAngularAcceleration.mult(tpf));
        moderateRoll();
        //fromangles is selfmodifying
        Quaternion rotationQuaternion = tempQuaternion.fromAngles(vAngularVelocity.x * tpf, vAngularVelocity.y * tpf, vAngularVelocity.z * tpf);
        synchronized (this) {
            rotation = rotation.mult(rotationQuaternion);
            translation = translation.add(vVelocity.mult(tpf));
        }
    }

    public synchronized void updateScene(Node model) {
        model.setLocalRotation(rotation);
        model.setLocalTranslation(translation);
    }

    private void moderateRoll() {
        /*  if (vAngularVelocity.x > 2) {
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
         }*/
    }

    private ActingForces engineForces(Quaternion situation) {
        Vector3f vLinearAcceleration = engines.stream().map(e -> situation.mult(e.getThrust())).
                reduce(Vector3f.ZERO,(e1,e2) -> e1.add(e2));
        return new ActingForces(vLinearAcceleration, Vector3f.ZERO);
    }

    private ActingForces airfoilForces(Quaternion rotation, Vector3f vFlow) {
        airfoils.stream().forEach(a -> a.tick(airDensity, vFlow, rotation, vAngularVelocity));
        Vector3f vLinearAcceleration = airfoils.stream().map(Airfoil::linearAcceleration).reduce(Vector3f.ZERO,(a,b) -> a.add(b));
        Vector3f vTorque = (airfoils.stream().map(a ->a.torque(rotation.inverse())).reduce(Vector3f.ZERO,(a,b) -> a.add(b)));
        return new ActingForces(vLinearAcceleration, vTorque);
    }

    private Vector3f calculateParasiticDrag() {
        return vVelocity.negate().normalize().mult(airDensity * planeFactor * vVelocity.lengthSquared());
    }

    private void updateAuxiliary(Quaternion rotation, Vector3f translation, Environment environment) {
        updateHelpers(translation, environment);
        updateAngleOfAttack(rotation);
        updatePlaneFactor();
    }

    private void updateAngleOfAttack(Quaternion rotation) {
        angleOfAttack = rotation.mult(Vector3f.UNIT_Z).angleBetween(vVelocity.normalize()) * FastMath.RAD_TO_DEG;
        float np = rotation.mult(Vector3f.UNIT_Y).dot(vVelocity.negate().normalize());
        if (np < 0) {
            angleOfAttack = -angleOfAttack;
        }
        /*logger.info("Angle of attack: {}", angleOfAttack);*/
    }

    public void updatePlaneFactor() {
        planeFactor = 0.2566f;

        /*if (angleOfAttack > 30 || angleOfAttack < - 30) {
         planeFactor = wingArea * 1.2f;     // let's pretend rest of the body area is half of body area
         } else {
         planeFactor = 0.2566f;
         }*/
    }

    private void updateHelpers(Vector3f translation, Environment environment) {
        height = translation.getY();
        airDensity = environment.airDensity((int) height);
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
                + ", CurrentSpeed: " + fractionless.format(vVelocity.length())
                + ", CurrentSpeed km/h: " + fractionless.format(vVelocity.length() * 3.6)
                + ", Height: " + fractionless.format(height)
                //+ ", AirSpeed: " + fractionless.format(airSpeed)
                + ", AOA: " + fractionless.format(angleOfAttack)
                + ", AngularVelocity: " + fraction2Format.format(vAngularVelocity.length())
                + ", AngularAcceleration: " + fraction2Format.format(vAngularAcceleration.length());
    }

    @Override
    public Vector3f getVVelovity() {
        return vVelocity;
    }

    @Override
    public void setSpeedForward(Spatial model, float kmh) {
        vVelocity = model.getLocalRotation().mult(Vector3f.UNIT_Z).normalize().mult(kmh / 3.6f);
    }
}
