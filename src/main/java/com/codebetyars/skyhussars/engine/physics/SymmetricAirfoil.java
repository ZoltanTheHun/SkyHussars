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
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymmetricAirfoil implements Airfoil {

    private final static Logger logger = LoggerFactory.getLogger(SymmetricAirfoil.class);

    public SymmetricAirfoil(String name, Vector3f cog, float wingArea, float incidence, float aspectRatio, boolean damper, float dehidralDegree) {
        this.wingArea = wingArea;
        this.incidence = incidence;// * FastMath.DEG_TO_RAD;
        this.cog = cog;
        this.name = name;
        this.aspectRatio = aspectRatio;
        this.qIncidence = qIncidence.fromAngles((-incidence) * FastMath.DEG_TO_RAD, 0, 0);
        this.dehidral = new Quaternion().fromAngleAxis(dehidralDegree * FastMath.DEG_TO_RAD, Vector3f.UNIT_Z);//vertical ? Vector3f.UNIT_X : Vector3f.UNIT_Y;
        wingRotation = qIncidence.mult(dehidral);
        logger.debug(name + " pointing to " + qIncidence.mult(dehidral).mult(Vector3f.UNIT_Y));
        this.damper = damper;
        if (damper) {
            if (this.cog.dot(Vector3f.UNIT_X) < 0) {
                leftDamper = true;
            } else {
                leftDamper = false;
            }
        }
    }
    private int[] aoa = {0, 2, 4, 6, 8, 10, 12, 30};
    private float[] clm05 = {0f, 0.246f, 0.475f, 0.68f, 0.775f, 0.795f, 0.778f, 0.8f};
    // private float[]
    private float wingArea;
    private float incidence;
    private Vector3f cog;
    private String name;
    private float aspectRatio;
    private Quaternion qIncidence = new Quaternion();
    private Quaternion dehidral;
    private Quaternion qAileron = new Quaternion();
    private Quaternion wingRotation;
    private float dampingFactor = 1f;
    private boolean damper;
    private boolean leftDamper;

    @Override
    public Vector3f calculateResultantForce(float airDensity, Vector3f vFlow, Quaternion situation, Vector3f vAngularVelocity) {
        //Quaternion foil = wingRotation.mult(qAileron).mult(situation);//situation.mult(wingRotation).mult(qAileron);//situation.mult(qIncidence).mult(qAileron).mult(dehidral);
        Quaternion foil = situation.mult(wingRotation).mult(qAileron);//situation.mult(qIncidence).mult(qAileron).mult(dehidral);
        Vector3f vUp = foil.mult(Vector3f.UNIT_Y).normalize();
        vFlow = addDamping(vFlow, vAngularVelocity, vUp);
        float angleOfAttack = calculateAngleOfAttack(vUp, vFlow.normalize());
        Vector3f vLift = calculateLift(angleOfAttack, airDensity, vFlow, vUp);
        Vector3f vInducedDrag = calculateInducedDrag(airDensity, vFlow, vLift);
        logging(vLift, vUp, angleOfAttack, vInducedDrag);

        return vLift.add(vInducedDrag);
    }

    private void logging(Vector3f vLift, Vector3f vUp, float angleOfAttack, Vector3f vInducedDrag) {
        String direction = "up";
        if (vLift.normalize().dot(vUp) < 0) {
            direction = "down";
        }
        logger.debug(name + " at " + angleOfAttack + " degrees generated " + direction + "forces: vLift " + vLift.length() + ", induced drag " + vInducedDrag.length());
    }

    public Vector3f addDamping(Vector3f vFlow, Vector3f vAngularVelocity, Vector3f vUp) {
        float zDamping = vAngularVelocity.z * cog.length() * dampingFactor;
        float xDamping = vAngularVelocity.x * cog.length() * 2;
        float yDamping = vAngularVelocity.y * cog.length() * 1;
        if (damper && leftDamper) {
            vFlow = vFlow.add(vUp.mult(zDamping));
        } else if (damper && !leftDamper) {
            vFlow = vFlow.add(vUp.mult(zDamping).negate());
        }
        if (name.equals("HorizontalStabilizer")) {
            vFlow = vFlow.add(vUp.mult(xDamping).negate());
        }
        if (name.equals("VerticalStabilizer")) {
            vFlow = vFlow.add(vUp.mult(yDamping).negate());
        }
        return vFlow;
    }

    public Vector3f calculateLift(float angleOfAttack, float airDensity, Vector3f vFlow, Vector3f vUp) {
        float scLift = calculateLift(angleOfAttack, airDensity, vFlow);
        Vector3f liftDirection = vFlow.cross(vUp).cross(vFlow).normalize();
        if (angleOfAttack < 0) {
            liftDirection = liftDirection.negate();
        }
        return liftDirection.mult(scLift);
    }

    public float calculateLift(float angleOfAttack, float airDensity, Vector3f vFlow) {
        //abs is used for symmetric wings? not perfect
        return 0.5f * airDensity * getLiftCoefficient(FastMath.abs(angleOfAttack)) * wingArea * vFlow.lengthSquared();
    }

    public float getLiftCoefficient(float angleOfAttack) {
        float liftCoefficient = 0f;
        for (int i = 1; i < aoa.length; i++) {
            if (angleOfAttack < aoa[i]) {
                /*let's approximate the real  values with interpolation*/
                float diff = aoa[i] - aoa[i - 1];
                float real = angleOfAttack - aoa[i - 1];
                float a = real / diff;
                float b = 1f - a;
                liftCoefficient = clm05[i] * a + clm05[i - 1] * b;
                break;
            }
        }
        return liftCoefficient;
    }

    public Vector3f calculateInducedDrag(float airDensity, Vector3f vFlow, Vector3f vLift) {
        float dividened = (0.5f * airDensity * aspectRatio * vFlow.lengthSquared() * FastMath.PI * wingArea);
        //logger.debug("Airdensity: " + airDensity + ", Velocity: " + vVelocity.length() + ", lift: " + vLift.length() + );
        if (dividened == 0) {
            return Vector3f.ZERO;
        }
        float scInducedDrag = (vLift.lengthSquared()) / dividened;
        return vFlow.normalize().mult(scInducedDrag);
    }

    public float calculateInducedDrag(float airDensity, Vector3f vVelocity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Vector3f getCenterOfGravity() {
        return cog;
    }

    public String getName() {
        return name;
    }

    private float calculateAngleOfAttack(Vector3f vUp, Vector3f vFlow) {
        float angleOfAttack = vFlow.cross(vUp).cross(vFlow).normalize().angleBetween(vUp) * FastMath.RAD_TO_DEG;
        float np = vUp.dot(vFlow);
        if (np < 0) {
            angleOfAttack = -angleOfAttack;
        }
        return angleOfAttack;
    }

    public void controlAileron(float aileron) {
        Quaternion q = new Quaternion();
        qAileron = q.fromAngles(aileron * FastMath.DEG_TO_RAD, 0, 0);
    }
}
