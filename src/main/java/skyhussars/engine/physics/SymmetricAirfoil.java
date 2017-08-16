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

import static com.jme3.math.FastMath.*;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymmetricAirfoil implements Airfoil {

    private final static Logger logger = LoggerFactory.getLogger(SymmetricAirfoil.class);
    public static class Builder{
        private String name; 
        private Vector3f cog; 
        private float wingArea,incidence,aspectRatio,dehidralDegree,dampingFactor = 2;
        private boolean damper;
        private Aileron.ControlDir direction;
        public Builder name(String name){ this.name = name; return this;}
        public Builder cog(Vector3f cog){ this.cog = cog; return this;}
        public Builder wingArea(float wingArea){ this.wingArea = wingArea; return this;}
        public Builder incidence(float incidence){ this.incidence = incidence; return this;}
        public Builder aspectRatio(float aspectRatio){ this.aspectRatio = aspectRatio; return this;}
        public Builder dehidralDegree(float dehidralDegree){ this.dehidralDegree = dehidralDegree; return this;}
        public Builder damper(boolean damper){ this.damper = damper; return this;}
        public Builder direction(Aileron.ControlDir direction){ this.direction = direction; return this;} 
        public Builder dampingFactor(float dampingFactor){ this.dampingFactor = dampingFactor; return this;} 
        public SymmetricAirfoil build(){
            return new SymmetricAirfoil(name,cog,wingArea,incidence,aspectRatio,damper,dehidralDegree,direction,dampingFactor);
        }
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
    private final Vector3f upNorm;
    private float dampCf;
    private final boolean damper;
    private Aileron.ControlDir direction;
    private Vector3f linear = Vector3f.ZERO;
    private Vector3f torque = Vector3f.ZERO;
    private Quaternion damp = new Quaternion();
    private Vector3f inducedDrag;
    private Vector3f lift;
    private float aoa;
    private float dampDir;
    
    public float aoa(){return aoa;}
    public Vector3f lift(){return lift;}
    public Vector3f inducedDrag(){return inducedDrag;}
    @Override public Vector3f cog() { return cog; }
    @Override public String name() { return name; }
    public Quaternion damp(){return damp;}
    @Override public synchronized Vector3f linear() {return linear;}
    @Override public synchronized Vector3f torque() {return torque;}
    public SymmetricAirfoil(String name, Vector3f cog, 
            float wingArea, float incidence, float aspectRatio, 
            boolean damper, float dehidralDegree, Aileron.ControlDir direction) {
        this( name,  cog, wingArea,  incidence,  aspectRatio, damper, dehidralDegree,direction,1f);
    }
    public SymmetricAirfoil(String name, Vector3f cog, 
            float wingArea, float incidence, float aspectRatio, 
            boolean damper, float dehidralDegree, Aileron.ControlDir direction,float dampingFactor) {
        this.wingArea = wingArea;
        this.incidence = incidence;
        this.cog = cog;
        this.name = name;
        this.aspectRatio = aspectRatio;
        this.qIncidence = qIncidence.fromAngles((-incidence) * DEG_TO_RAD, 0, 0);
        this.dehidral = new Quaternion().fromAngleAxis(dehidralDegree * DEG_TO_RAD, Vector3f.UNIT_Z);//vertical ? Vector3f.UNIT_X : Vector3f.UNIT_Y;
        wingRotation = qIncidence.mult(dehidral);
        upNorm = wingRotation.mult(Vector3f.UNIT_Y).normalize();
        logger.debug(name + " pointing to " + qIncidence.mult(dehidral).mult(Vector3f.UNIT_Y));
        this.damper = damper;
        this.dampCf = dampingFactor;
        if (damper) dampDir = this.cog.dot(Vector3f.UNIT_X) < 0 ? 1 : -1; 
        this.direction = direction;
    }

    @Override
    public Aileron.ControlDir direction(){return direction;}
    
    private void logging(Vector3f vLift, Vector3f vUp, float angleOfAttack, Vector3f vInducedDrag) {
        String dir = vLift.normalize().dot(vUp) > 0 ? "up" : "down";
        logger.debug(name + " at " + angleOfAttack + " degrees generated " + dir + "forces: vLift " + vLift.length() + ", induced drag " + vInducedDrag.length());
    }

    private Quaternion damp(Vector3f vAngularVelocity) {
        float zDamp = 0;
        if(damper) zDamp = dampDir * vAngularVelocity.z * dampCf;
        float xDamp = 0;
        float yDamp = 0;
        switch(direction){
            case HORIZONTAL_STABILIZER : xDamp = vAngularVelocity.x * 1f ; break;
            case VERTICAL_STABILIZER : yDamp = vAngularVelocity.y * 1f; break;
        }
        float[] angles = new float[]{zDamp,-yDamp,-xDamp};
        return new Quaternion(angles);
    }

    
    private Vector3f lift(float airDensity, Vector3f vFlow) {
        float scLift = calculateLift(airDensity, vFlow);
        Vector3f dir = vFlow.cross(upNorm).cross(vFlow).normalize();
        if (aoa < 0) dir = dir.negate();
        return dir.mult(scLift);
    }


    private float calculateLift(float airDensity, Vector3f vFlow) {
        return 0.5f * airDensity * getLiftCoefficient() * wingArea * vFlow.lengthSquared();
    }

    private float getLiftCoefficient() {
        //abs is used for symmetric wings? not perfect
        float absAoa = abs(aoa);
        float liftCoefficient = 0f;
        for (int i = 1; i < constAoa.length; i++) {
            if (absAoa < constAoa[i]) {
                float diff = constAoa[i] - constAoa[i - 1];
                float real = absAoa - constAoa[i - 1];
                float a = real / diff;
                float b = 1f - a;
                liftCoefficient = clm05[i] * a + clm05[i - 1] * b;
                break;
            }
        }
        return liftCoefficient;
    }

    private  Vector3f inducedDrag(float airDensity, Vector3f vFlow) {
        float dividened = (0.5f * airDensity * aspectRatio * vFlow.lengthSquared() * PI * wingArea);
        if (abs(dividened) < 0.0001f) return Vector3f.ZERO;
        float scInducedDrag = lift.lengthSquared() / dividened;
        return vFlow.normalize().mult(scInducedDrag);
    }

    private float calcAoa(Vector3f flow) {
        flow = flow.normalize();
        float locAoa = flow.cross(upNorm).cross(flow).normalize().angleBetween(upNorm) * RAD_TO_DEG;
        return upNorm.dot(flow) > 0 ? locAoa : - locAoa;
    }
    
    @Override
    public synchronized Airfoil tick(float airDensity, Vector3f flow, Vector3f angV) {
        damp = damp(angV);
        flow = damp.mult(flow);
        aoa = calcAoa(flow);
        lift = lift(airDensity, flow);
        inducedDrag = inducedDrag(airDensity, flow);
        linear =  lift.add(inducedDrag);
        torque = cog.cross(linear);      
        return this;
    }

}
