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
package skyhussars.engine.physics;

import skyhussars.engine.physics.environment.Environment;
import skyhussars.utility.NumberFormats;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PlanePhysicsImpl implements PlanePhysics {

    private final static Logger logger = LoggerFactory.getLogger(PlanePhysicsImpl.class);

    private float airDensity = 1.2745f;
    private float planeFactor = 0.2566f; // cross section and drag coeff together
    //private float mass = 57380;//loaded: 5,738emtpy:38190; //N
    private final float mass; //actually the loaded weight is  57380N, the empty weight is 38190N
    private float aoa;

    private Vector3f vVelocity = new Vector3f(0f, 0f, 0f);
    private Vector3f vAngularAcceleration = new Vector3f(0, 0, 0);
    private Vector3f vAngularVelocity = new Vector3f(0, 0, 0);

    private float height;

    private Quaternion rotation;
    private Vector3f translation;
    
    public synchronized void setRotation(Quaternion rotation) {
        this.rotation = new Quaternion(rotation);
    }

    public synchronized void setTranslation(Vector3f translation) {
        this.translation = new Vector3f(translation);
    }
    
    private final float length = 10.49f;
    private final float rPlane = 1.3f;
    private final Matrix3f momentOfInertiaTensor;
        
    private List<Airfoil> airfoils = new ArrayList<>();
    private List<Engine> engines = new ArrayList<>();

    private final float tick = (float) 1 / (float) 60;

    public PlanePhysicsImpl(Quaternion rotation,
                            Vector3f translation,
                            float mass,
                            List<Engine> engines, 
                            List<Airfoil> airfoils) {
        this.mass = mass;
        momentOfInertiaTensor = new Matrix3f((mass / 12) * (3 * rPlane * rPlane + length * length), 0f, 0f,
                0f, (mass / 12) * (3 * rPlane * rPlane + length * length), 0f,
                0f, 0f, (mass / 2) * (rPlane * rPlane));
        this.airfoils = airfoils;
        this.engines = engines;
        this.rotation = new Quaternion(rotation);
        this.translation = new Vector3f(translation);
    }
    
    private final Quaternion helperQuaternion = new Quaternion();
    
    /* Airfoil calculations */
    private Vector3f localFlow(){ return rotation.inverse().mult(vVelocity.negate()); } // localized to plane coordinate space
    private void updateAirfoils() { airfoils.stream().forEach(a -> a.tick(airDensity, localFlow(), vAngularVelocity));}
    private Vector3f airfoilLinear() {return rotation.mult(airfoils.stream().map(Airfoil::linearForce).reduce(Vector3f.ZERO,Vector3f::add));}
    private Vector3f airfoilTorque() {return (airfoils.stream().map(Airfoil::torque).reduce(Vector3f.ZERO,Vector3f::add));}
    
    /* Engine calculations */
    private Vector3f engineLinear() {return rotation.mult(engines.stream().map(Engine::thrust).reduce(Vector3f.ZERO,Vector3f::add));}
    private Vector3f engineTorque() {return rotation.mult(engines.stream().map(Engine::torque).reduce(Vector3f.ZERO,Vector3f::add));}  // to be added later
    
    /* Other calculations */
    private Vector3f parasiticDrag() {return vVelocity.negate().normalize().mult(airDensity * planeFactor * vVelocity.lengthSquared());}
    
    @Override
    public void update(float tpf, Environment environment) {
        updateAuxiliary(rotation, translation, environment);
        updateAirfoils();
        
        /* later on world space rotation of vectors could happen once*/
        Vector3f linearAcc = Vector3f.ZERO
                .add(environment.gravity().mult(mass))
                .add(engineLinear())   
                .add(airfoilLinear())
                .add(parasiticDrag()).divide(mass);

        vVelocity = vVelocity.add(linearAcc.mult(tpf));
        
        vAngularAcceleration = momentOfInertiaTensor.invert().mult(
                Vector3f.ZERO.add(airfoilTorque())
                             .add(engineTorque()));
        vAngularVelocity = vAngularVelocity.add(vAngularAcceleration.mult(tpf));
        //fromangles is selfmodifying
        Quaternion rotationQuaternion = helperQuaternion.fromAngles(vAngularVelocity.x * tpf, vAngularVelocity.y * tpf, vAngularVelocity.z * tpf);
        synchronized (this) {
            rotation = rotation.mult(rotationQuaternion);
            translation = translation.add(vVelocity.mult(tpf));
        }
    }
    
    /* model can only be updated from main thread atm */
    public synchronized void updateScene(Node model) {
        model.setLocalRotation(rotation);
        model.setLocalTranslation(translation);
    }

    private void updateAuxiliary(Quaternion rotation, Vector3f translation, Environment environment) {
        updateHelpers(translation, environment);
        updateAngleOfAttack(rotation);
        updatePlaneFactor();
    }

    private void updateAngleOfAttack(Quaternion rotation) {
        aoa = rotation.mult(Vector3f.UNIT_Z).angleBetween(vVelocity.normalize()) * FastMath.RAD_TO_DEG;
        float np = rotation.mult(Vector3f.UNIT_Y).dot(vVelocity.negate().normalize());
        if (np < 0) {
            aoa = -aoa;
        }
    }

    public void updatePlaneFactor() {
        planeFactor = 0.2566f;
    }

    private void updateHelpers(Vector3f translation, Environment environment) {
        height = translation.getY();
        airDensity = environment.airDensity((int) height);
    }

    @Override
    public String getSpeedKmH() {
        return NumberFormats.toMin3Integer0Fraction(vVelocity.length() * 3.6);
    }

    @Override
    public String getInfo() {
        return "Thrust: " + engines.get(0).thrust().length()
                + ", CurrentSpeed: " + NumberFormats.toMin3Integer0Fraction(vVelocity.length())
                + ", CurrentSpeed km/h: " + NumberFormats.toMin3Integer0Fraction(vVelocity.length() * 3.6)
                + ", Height: " + NumberFormats.toMin3Integer0Fraction(height)
                //+ ", AirSpeed: " + fractionless.format(airSpeed)
                + ", AOA: " + NumberFormats.toMin3Integer0Fraction(aoa)
                + ", AngularVelocity: " + NumberFormats.toFix2Fraction(vAngularVelocity.length())
                + ", AngularAcceleration: " + NumberFormats.toFix2Fraction(vAngularAcceleration.length());
    }

    @Override
    public Vector3f getVVelovity() {
        return vVelocity;
    }

    @Override
    public void setSpeedForward(Quaternion rotation, float kmh) {
        vVelocity = rotation.mult(Vector3f.UNIT_Z).normalize().mult(kmh / 3.6f);
    }
}
