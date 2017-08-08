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

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymmetricAirfoil implements Airfoil {

    private final static Logger logger = LoggerFactory.getLogger(SymmetricAirfoil.class);

    public SymmetricAirfoil(String name, Vector3f cog, float wingArea, float incidence, float aspectRatio, boolean damper, float dehidralDegree, Aileron.Direction direction) {
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
                leftDamper = 1;
            } else {
                leftDamper = -1;
            }
        }
        this.direction = direction;
    }
    private final int[] constAoa = {0, 2, 4, 6, 8, 10, 15, 30};
    private final float[] clm05 = {0f, 0.246f, 0.475f, 0.68f, 0.775f, 0.795f, 0.82f, 0.8f};
    // private float[]
    private final float wingArea;
    private final float incidence;
    private final Vector3f cog;
    private final String name;
    private final float aspectRatio;
    private Quaternion qIncidence = new Quaternion();
    private final Quaternion dehidral;
    private final Quaternion wingRotation;
    private final float dampingFactor = 2f;
    private final boolean damper;
    private int leftDamper;
    private Aileron.Direction direction;

    @Override
    public Aileron.Direction direction(){return direction;}
    
    private void logging(Vector3f vLift, Vector3f vUp, float angleOfAttack, Vector3f vInducedDrag) {
        String direction = "up";
        if (vLift.normalize().dot(vUp) < 0) {
            direction = "down";
        }
        logger.debug(name + " at " + angleOfAttack + " degrees generated " + direction + "forces: vLift " + vLift.length() + ", induced drag " + vInducedDrag.length());
    }

    public Vector3f damp(Vector3f vFlow, Vector3f vAngularVelocity, Vector3f vUp) {
        float zDamping = vAngularVelocity.z * cog.length() * dampingFactor;
        float xDamping = vAngularVelocity.x * cog.length() * 1f;
        float yDamping = vAngularVelocity.y * cog.length() * 1f;
        if (damper) vFlow = vFlow.add(vUp.mult(zDamping).mult(leftDamper));
        switch(direction){
            case HORIZONTAL_STABILIZER : vFlow = vFlow.add(vUp.mult(xDamping).negate()); break;
            case VERTICAL_STABILIZER : vFlow = vFlow.add(vUp.mult(yDamping).negate()); break;
        }
        return vFlow;
    }

    public Vector3f lift(float airDensity, Vector3f vFlow, Vector3f vUp) {
        float aoa = aoa(vUp, vFlow.normalize());
        float scLift = calculateLift(aoa, airDensity, vFlow);
        Vector3f direction = vFlow.cross(vUp).cross(vFlow).normalize();
        if (aoa < 0) {
            direction = direction.negate();
        }
        return direction.mult(scLift);
    }

    public float calculateLift(float angleOfAttack, float airDensity, Vector3f vFlow) {
        //abs is used for symmetric wings? not perfect
        return 0.5f * airDensity * getLiftCoefficient(FastMath.abs(angleOfAttack)) * wingArea * vFlow.lengthSquared();
    }

    public float getLiftCoefficient(float angleOfAttack) {
        float liftCoefficient = 0f;
        for (int i = 1; i < constAoa.length; i++) {
            if (angleOfAttack < constAoa[i]) {
                /*let's approximate the real  values with interpolation*/
                float diff = constAoa[i] - constAoa[i - 1];
                float real = angleOfAttack - constAoa[i - 1];
                float a = real / diff;
                float b = 1f - a;
                liftCoefficient = clm05[i] * a + clm05[i - 1] * b;
                break;
            }
        }
        return liftCoefficient;
    }

    public Vector3f inducedDrag(float airDensity, Vector3f vFlow, Vector3f vLift) {
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

    @Override
    public Vector3f cog() {
        return cog;
    }

    public String getName() {
        return name;
    }

    private float aoa(Vector3f vUp, Vector3f vFlow) {
        float angleOfAttack = vFlow.cross(vUp).cross(vFlow).normalize().angleBetween(vUp) * FastMath.RAD_TO_DEG;
        float np = vUp.dot(vFlow);
        if (np < 0) {
            angleOfAttack = -angleOfAttack;
        }
        return angleOfAttack;
    }

    Vector3f linearAcceleration = Vector3f.ZERO;
    Vector3f torque = Vector3f.ZERO;

    @Override
    public Airfoil tick(float airDensity, Vector3f vFlow, Vector3f vAngularVelocity) {
        Vector3f vUp = wingRotation.mult(Vector3f.UNIT_Y).normalize();
        vFlow = damp(vFlow, vAngularVelocity, vUp);
        Vector3f lift = lift(airDensity, vFlow, vUp);
        Vector3f inducedDrag = inducedDrag(airDensity, vFlow, lift);
        synchronized(this) {
            linearAcceleration =  lift.add(inducedDrag);
            torque = cog.cross(linearAcceleration);
        }
        return this;
    }

    @Override public synchronized Vector3f linearForce() {return linearAcceleration;}
    @Override public synchronized Vector3f torque() {return torque;}

}
