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
    private final Vector3f cog;
    private final String name;
    private final float aspectRatio;
    private final Quaternion qIncidence;
    private final Quaternion dehidral;
    private final Quaternion wingRotation;
    private final Vector3f upNorm;
    private float dampCf;
    private final boolean damper;
    private final Aileron.ControlDir direction;
    private final float dampDir;
   
    @Override public Vector3f cog() { return cog; }
    @Override public String name() { return name; }
    
    public SymmetricAirfoil(String name, Vector3f cog, 
            float wingArea, float incidence, float aspectRatio, 
            boolean damper, float dehidralDegree, Aileron.ControlDir direction) {
        this( name,  cog, wingArea,  incidence,  aspectRatio, damper, dehidralDegree,direction,1f);
    }
    public SymmetricAirfoil(String name, Vector3f cog, 
            float wingArea, float incidence, float aspectRatio, 
            boolean damper, float dehidralDegree, Aileron.ControlDir direction,float dampingFactor) {
        this.wingArea = wingArea;
        this.cog = cog;
        this.name = name;
        this.aspectRatio = aspectRatio;
        this.qIncidence = new Quaternion().fromAngles((-incidence) * DEG_TO_RAD, 0, 0);
        //vertical ? Vector3f.UNIT_X : Vector3f.UNIT_Y;
        this.dehidral = new Quaternion().fromAngleAxis(dehidralDegree * DEG_TO_RAD, Vector3f.UNIT_Z);
        wingRotation = qIncidence.mult(dehidral);
        upNorm = wingRotation.mult(Vector3f.UNIT_Y).normalize();
        this.damper = damper;
        this.dampCf = dampingFactor;
        dampDir = damper ? ( this.cog.dot(Vector3f.UNIT_X) < 0 ? 1 : -1 ) : 0; 
        this.direction = direction;
    }

    @Override
    public Aileron.ControlDir direction(){return direction;}

    private Quaternion damp(Vector3f vAngularVelocity) {
        float zDamp = 0;
        float xDamp = 0;
        float yDamp = 0;
        if(damper) zDamp = dampDir * vAngularVelocity.z * dampCf;
        switch(direction){
            case HORIZONTAL_STABILIZER : xDamp = vAngularVelocity.x * 1f ; break;
            case VERTICAL_STABILIZER : yDamp = vAngularVelocity.y * 1f; break;
        }
        float[] angles = new float[]{zDamp,-yDamp,-xDamp};
        return new Quaternion(angles);
    }

    
    private Vector3f lift(float airDensity, Vector3f vFlow,float aoa) {
        float scLift = calculateLift(airDensity, vFlow, aoa);
        Vector3f dir = vFlow.cross(upNorm).cross(vFlow).normalize();
        if (aoa < 0) dir = dir.negate();
        return dir.mult(scLift);
    }


    private float calculateLift(float airDensity, Vector3f vFlow,float aoa) {
        return 0.5f * airDensity * getLiftCoefficient(aoa) * wingArea * vFlow.lengthSquared();
    }

    private float getLiftCoefficient(float aoa) {
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

    private  Vector3f inducedDrag(float airDensity, Vector3f vFlow, Vector3f lift) {
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
    public AirfoilResponse tick(float airDensity, Vector3f flow, Vector3f angV) {
        Quaternion damp = damp(angV);
        flow = damp.mult(flow);
        float aoa = calcAoa(flow);
        Vector3f lift = lift(airDensity, flow,aoa);
        Vector3f inducedDrag = inducedDrag(airDensity, flow,lift);
        Vector3f linear =  lift.add(inducedDrag);
        Vector3f torque = cog.cross(linear);      
        return new AirfoilResponse(damp, aoa, lift, inducedDrag, linear, torque);
    }

}
