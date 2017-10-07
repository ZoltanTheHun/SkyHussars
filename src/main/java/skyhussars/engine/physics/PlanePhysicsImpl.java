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
import com.jme3.math.*;
import static com.jme3.math.Vector3f.*;
import static com.jme3.math.FastMath.RAD_TO_DEG;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static skyhussars.utility.Streams.*;


public class PlanePhysicsImpl implements PlanePhysics {

    private final static Logger logger = LoggerFactory.getLogger(PlanePhysicsImpl.class);

    private final float planeFactor = 0.2566f; // cross section and drag coeff together
    private final float mass; //actually the loaded weight is  57380N, the empty weight is 38190N
    
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
    private Vector3f parasiticDrag(float airDensity, Vector3f velocity) {
        return velocity.negate().normalize().mult(airDensity * planeFactor * velocity.lengthSquared());}
    
    @Override
    public PlaneResponse update(float tpf, Environment environment, PlaneResponse planeRsp) {
        float airDensity = environment.airDensity(planeRsp.height());//1.2745f;
        Quaternion rotation = planeRsp.rotation;
        Vector3f translation = planeRsp.translation;
        Vector3f velocity = planeRsp.velocity;
        Vector3f angularVelocity = planeRsp.angularVelocity;
        /* flow to local coordinate space*/
        Vector3f flow = rotation.inverse().mult(velocity.negate());
        
        /*plane related values*/
        float aoa = calcAoa(flow);
        final Vector3f angVel = angularVelocity;
        /* Airfoil calculation*/
        List<AirfoilResponse> afps = list(pm(airfoils,a -> a.tick(airDensity, flow, angVel)));
        Vector3f airfoilLinear = rotation.mult(sum(sm(afps,a -> a.linear)));
        Vector3f airfoilTorque = sum(sm(afps,a -> a.torque));
        
        /* Engine calculations */
        Vector3f engineLinear = rotation.mult(sum(sm(engines,Engine::thrust)));
        Vector3f engineTorque = rotation.mult(sum(sm(engines,Engine::torque)));
        
        Vector3f parasiticDrag =  parasiticDrag(airDensity,velocity); 
        
        /* later on world space rotation of vectors could happen once*/
        Vector3f linearAcc = Vector3f.ZERO
                .add(environment.gravity().mult(mass))
                .add(engineLinear)   
                .add(airfoilLinear)
                .add(parasiticDrag).divide(mass);

        velocity = velocity.add(linearAcc.mult(tpf));
        
        Vector3f angularAcceleration = momentOfInertiaTensor.invert()
                                                             .mult(Vector3f.ZERO.add(airfoilTorque)
                                                                                .add(engineTorque));
        angularVelocity = angularVelocity.add(angularAcceleration.mult(tpf));
        
        Quaternion rotationQuaternion = new Quaternion().fromAngles(angularVelocity.x * tpf, angularVelocity.y * tpf, angularVelocity.z * tpf);

        rotation = rotation.mult(rotationQuaternion);
        translation = translation.add(velocity.mult(tpf));
        
        return new PlaneResponse(rotation,translation,velocity,aoa,angularAcceleration,angularVelocity);
    }
    
    /* this function uses localized flow, hence calculation uses UNIT_Y vector */
    private float calcAoa(Vector3f flow) {
        flow = flow.normalize();
        float locAoa = flow.cross(UNIT_Y).cross(flow).normalize().angleBetween(UNIT_Y) * RAD_TO_DEG;
        return UNIT_Y.dot(flow) > 0 ? locAoa : - locAoa;
    }
    
}
