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

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.text.NumberFormat;

public class SimplePlanePhysics implements PlanePhysics {
    private static final Vector3f GRAVITY = new Vector3f(0f, -10f, 0f);
    private float maxThrust = 20460;
    private float currentThrust = 0;
    private float airDensity = 1.2745f;
    private float planeFactor = 0.2566f; // cross section and drag coeff together
    private float wingArea = 22.07f; //m2
    private float inducedDragCoefficient = 0.0134f;
    private float liftCoefficient = 0.2f;
    //private float mass = 57380;//loaded: 5,738emtpy:38190; //N
    private float mass = 5738; //actually the loaded weight is  57380N, the empty weight is 38190N
    private Vector3f vWeight = GRAVITY.mult(mass);
    private float aspectRatio = 6.37f;
    private float pi = 3.14f;
    private float angleOfAttack;
    private Vector3f vVelocity = new Vector3f(0f, 0f, 0f);
    private Vector3f vAcceleration = new Vector3f(0f, 0f, 0f);
    private Vector3f vDrag = new Vector3f(0f, 0f, 0f);
    private Vector3f vLift = new Vector3f(0f, 0f, 0f);
    private float vAngularAcceleration;
    private float vAngularVelocity;
    private float height;
    private float airSpeed;
    private float length = 10.49f;
    private float rPlane = 1.5f;
    private boolean upward;
    private float momentOfInertia = (mass / 12) * (3 * rPlane * rPlane + length * length);
    private float elevator = 0;

    public SimplePlanePhysics(Spatial model) {
        vVelocity = model.getLocalRotation().mult(Vector3f.UNIT_Z).normalize().mult(100);
    }

    private float calculateParasiticDrag() {
        return airDensity * planeFactor * vVelocity.lengthSquared();
    }

    private Vector3f calculateLift(Spatial model) {
        float lift = 0.5f * airDensity * (liftCoefficient) * wingArea * vVelocity.lengthSquared();
        return model.getLocalRotation().mult(Vector3f.UNIT_X).cross(vVelocity).normalize().negate().mult(lift);
    }

    private float calculateInducedDrag() {
        float dividened = (0.5f * airDensity * aspectRatio * vVelocity.lengthSquared() * pi * wingArea);
        if (dividened == 0) {
            return 0;
        }
        return (vLift.lengthSquared()) / dividened;
    }
   

    private Vector3f calculateDrag() {
        return vVelocity.normalize().negate().mult(calculateParasiticDrag() + calculateInducedDrag());
    }

    private Vector3f calculateAccelerationVector(Spatial model) {
        Vector3f vThrust = calculateThrust(model);
        vLift = calculateLift(model);
        vDrag = calculateDrag();
        Vector3f vF = vWeight.add(vThrust).add(vDrag).add(vLift);
        return vF.divide(mass);
    }

    private Vector3f calculateThrust(Spatial model) {
        return model.getLocalRotation().mult(new Vector3f(0f, 0f, (float) currentThrust));
    }

    public void update(float tpf, Spatial model) {
        updateAuxiliary(model);
        updateLinearMovement(tpf, model);
       // updateAngularMovement(tpf, model);
    }

    private void updateLinearMovement(float tpf, Spatial model) {
        vAcceleration = calculateAccelerationVector(model);
        vVelocity = vVelocity.add(vAcceleration.mult(tpf));
        model.move(vVelocity.mult(tpf));
    }

    private void updateAngularMovement(float tpf, Spatial model) {
        float degrees = 10;
        boolean tooLow = elevator < 0 && angleOfAttack > 30;
        boolean tooHigh = elevator > 0 && angleOfAttack < -30; 
        if( tooLow || tooHigh){
            return;
        }
        model.rotate(elevator * degrees * FastMath.DEG_TO_RAD * tpf, 0, 0);
    }

    private void updateAuxiliary(Spatial model) {
        updateHelpers(model);
        updateAngleOfAttack(model);
        updatePlaneFactor();
        updateLiftCoefficient();
    }

    private void updateAngleOfAttack(Spatial model) {
        angleOfAttack = model.getLocalRotation().mult(Vector3f.UNIT_Z).normalize().angleBetween(vVelocity.normalize()) * FastMath.RAD_TO_DEG;
        float np = model.getLocalRotation().mult(Vector3f.UNIT_Y).dot(vVelocity.normalize());
        if (np < 0) {
            angleOfAttack = -angleOfAttack;
        }
    }

    public void updateLiftCoefficient() {
        if (angleOfAttack < - 5) {
            liftCoefficient = 0;
        } else if (angleOfAttack < 0) {
            liftCoefficient = 0.1f;
        } else if (angleOfAttack < 5) {
            liftCoefficient = 0.5f;
        } else if (angleOfAttack < 10) {
            liftCoefficient = 1.0f;
        } else if (angleOfAttack < 15) {
            liftCoefficient = 1.5f;
        } else if (angleOfAttack < 30) {
            liftCoefficient = 1.2f;
        } else if (angleOfAttack > 30) {
            liftCoefficient = 0;
        }
    }

    public void updatePlaneFactor() {
        if (angleOfAttack > 30 || angleOfAttack < - 5) {
            planeFactor = wingArea * 1.2f;     // let's pretend rest of the body area is half of body area
        } else {
            planeFactor = 0.2566f;
        }
    }

    private void updateHelpers(Spatial model) {
        height = model.getWorldTranslation().getY();
        airDensity = WorldPhysicsData.getAirDensity((int)height);
        determineUpDirection(model);
    }

    private void determineUpDirection(Spatial model) {
        float angle = model.getLocalRotation().mult(Vector3f.UNIT_Y).normalize().angleBetween(Vector3f.UNIT_Y) * FastMath.RAD_TO_DEG;
        System.out.println("Angle: " + angle);
        if (angle > 90) {
            upward = false;
        } else {
            upward = true;
        }
    }

    public void setThrust(float thrust) {
        currentThrust = thrust;
    }


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
        return "Thrust: " + currentThrust
                + ", Acceleration: " + accF.format(vAcceleration.length())
                + ", CurrentSpeed: " + fractionless.format(vVelocity.length())
                + ", CurrentSpeed km/h: " + fractionless.format(vVelocity.length() * 3.6)
                + ", Drag: " + bigFractionless.format(vDrag.length())
                + ", Height: " + fractionless.format(height)
                + ", Lift: " + bigFractionless.format(vLift.length())
                //+ ", AirSpeed: " + fractionless.format(airSpeed)
                + ", AOA: " + fractionless.format(angleOfAttack)
                + ", AngularVelocity: " + fraction2Format.format(vAngularVelocity)
                + ", AngularAcceleration: " + fraction2Format.format(vAngularAcceleration);
    }

    public void setElevator(float elevator) {
        if(elevator < -0.1){
            this.elevator = -1;
        }else if(elevator > 0.1){
            this.elevator = 1;
        }else{
            this.elevator = 0;
        }
    }

    public void setSpeedForward(Spatial model, float kmh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setAileron(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getSpeedKmH(){
        return "";
    }
}
