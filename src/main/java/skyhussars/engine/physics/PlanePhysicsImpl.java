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
import com.jme3.math.*;
import static com.jme3.math.Vector3f.*;
import static com.jme3.math.FastMath.RAD_TO_DEG;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static skyhussars.utility.Streams.*;


public class PlanePhysicsImpl implements PlanePhysics {

    private final static Logger logger = LoggerFactory.getLogger(PlanePhysicsImpl.class);

    private float planeFactor = 0.2566f; // cross section and drag coeff together
    private final float mass; //actually the loaded weight is  57380N, the empty weight is 38190N
    private float aoa;

    private Vector3f vVelocity = new Vector3f(0f, 0f, 0f);
    private Vector3f vAngularAcceleration = new Vector3f(0, 0, 0);
    private Vector3f vAngularVelocity = new Vector3f(0, 0, 0);

    private float height;
/*
    private Quaternion rotation;
    private Vector3f translation;
        */
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
    }
           
    /* Other calculations */
    private Vector3f parasiticDrag(float airDensity) {return vVelocity.negate().normalize().mult(airDensity * planeFactor * vVelocity.lengthSquared());}
    
    @Override
    public PlaneResponse update(float tpf, Environment environment, PlaneResponse planeRsp) {
        float airDensity = environment.airDensity(height);//1.2745f;
        Quaternion rotation = planeRsp.rotation;
        Vector3f translation = planeRsp.translation;
        /* flow to local coordinate space*/
        Vector3f flow = rotation.inverse().mult(vVelocity.negate());
        
        /*plane related values*/
        updatePlaneFactor();
        height = translation.getY();
        aoa = calcAoa(flow);
        
        /* Airfoil calculation*/
        List<AirfoilResponse> afps = list(pm(airfoils,a -> a.tick(airDensity, flow, vAngularVelocity)));
        Vector3f airfoilLinear = rotation.mult(sum(sm(afps,a -> a.linear)));
        Vector3f airfoilTorque = sum(sm(afps,a -> a.torque));
        
        /* Engine calculations */
        Vector3f engineLinear = rotation.mult(sum(sm(engines,Engine::thrust)));
        Vector3f engineTorque = rotation.mult(sum(sm(engines,Engine::torque)));
        
        /* later on world space rotation of vectors could happen once*/
        Vector3f linearAcc = Vector3f.ZERO
                .add(environment.gravity().mult(mass))
                .add(engineLinear)   
                .add(airfoilLinear)
                .add(parasiticDrag(airDensity)).divide(mass);

        vVelocity = vVelocity.add(linearAcc.mult(tpf));
        
        vAngularAcceleration = momentOfInertiaTensor.invert()
                                                    .mult(Vector3f.ZERO.add(airfoilTorque)
                                                                       .add(engineTorque));
        vAngularVelocity = vAngularVelocity.add(vAngularAcceleration.mult(tpf));
        
        Quaternion rotationQuaternion = new Quaternion().fromAngles(vAngularVelocity.x * tpf, vAngularVelocity.y * tpf, vAngularVelocity.z * tpf);

        rotation = rotation.mult(rotationQuaternion);
        translation = translation.add(vVelocity.mult(tpf));
        
        return new PlaneResponse(rotation,translation,aoa);
    }
    
    /*this function uses localized flow, hence calculation uses UNIT_Y vector*/
    private float calcAoa(Vector3f flow) {
        flow = flow.normalize();
        float locAoa = flow.cross(UNIT_Y).cross(flow).normalize().angleBetween(UNIT_Y) * RAD_TO_DEG;
        return UNIT_Y.dot(flow) > 0 ? locAoa : - locAoa;
    }
    
    private void updatePlaneFactor() { planeFactor = 0.2566f; }

    @Override
    public String getSpeedKmH() {
        return NumberFormats.toMin3Integer0Fraction(vVelocity.length() * 3.6);
    }
    public float speedKmH() {return vVelocity.length() * 3.6f;}

    @Override
    public String getInfo() {
        return "Thrust: " + engines.get(0).thrust().length()
                + ", CurrentSpeed: " + NumberFormats.toMin3Integer0Fraction(vVelocity.length())
                + ", CurrentSpeed km/h: " + NumberFormats.toMin3Integer0Fraction(vVelocity.length() * 3.6)
                + ", Height: " + NumberFormats.toMin3Integer0Fraction(height)
                + ", AOA: " + NumberFormats.toMin3Integer0Fraction(aoa)
                + ", AngularVelocity: " + NumberFormats.toFix2Fraction(vAngularVelocity.length())
                + ", AngularAcceleration: " + NumberFormats.toFix2Fraction(vAngularAcceleration.length());
    }

    @Override
    public Vector3f getVVelovity() {return vVelocity;}

    @Override
    public void speedForward(Quaternion rotation, float kmh) {
        vVelocity = rotation.mult(Vector3f.UNIT_Z).normalize().mult(kmh / 3.6f);
    }
    
}
