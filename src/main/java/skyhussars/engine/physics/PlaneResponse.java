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

import com.jme3.math.*;
import com.jme3.scene.plugins.blender.math.Vector3d;
import static java.util.Objects.requireNonNull;

public final class PlaneResponse {
    public final Quaternion rotation;
    public final Vector3d translation;
    public final Vector3f velocity;
    public final Vector3f angularAcceleration; 
    public final Vector3f angularVelocity; 
    public final float aoa;
    public final double height(){return translation.y;}
    public final float velocityMs() {return velocity.length();}
    public final float velicityKmh() { return velocity.length()*3.6f;}
    public final Vector3f forwardNorm () {return rotation.mult(Vector3f.UNIT_Z).normalize();}

    public PlaneResponse(){
        rotation = new Quaternion();
        translation = new Vector3d();
        velocity = new Vector3f();
        angularAcceleration = new Vector3f();
        angularVelocity = new Vector3f();
        aoa = 0;
    }
    
    public PlaneResponse(Quaternion rotation,Vector3d translation,
            Vector3f velocity, float aoa,
            Vector3f angularAcceleration,Vector3f angularVelocity){
        //this.rotation = requireNonNull(rotation).normalizeLocal();
        this.rotation = requireNonNull(rotation);
        this.translation = requireNonNull(translation);
        this.velocity = requireNonNull(velocity);
        this.aoa = aoa;
        this.angularAcceleration = requireNonNull(angularAcceleration);
        this.angularVelocity = requireNonNull(angularVelocity);
    }
        
    @Override
    public String toString() {
        return "PlaneResponse{"
                + "rotation=" + rotation
                + ", translation=" + translation 
                + ", velocity=" + velocity 
                + ", angularAcceleration=" + angularAcceleration
                + ", angularVelocity=" + angularVelocity 
                + ", aoa=" + aoa + '}';
    }
    
    public PlaneResponse velocity(Vector3f velocity){
        return new PlaneResponse(rotation,translation, 
                velocity,aoa,angularAcceleration,angularVelocity);
    }
    public PlaneResponse velocity(float velocity){
        return new PlaneResponse(rotation,translation, 
                forwardNorm().mult(velocity),aoa,angularAcceleration,angularVelocity);
    }
    
    public PlaneResponse height(double height){
        return new PlaneResponse(rotation,
                translation.set(translation.x,height,translation.z),
                velocity, aoa,angularAcceleration,angularVelocity); 
    }
    public PlaneResponse translation(Vector3d translation){
        return new PlaneResponse(rotation,translation,
                velocity, aoa,angularAcceleration,angularVelocity); 
    }
}
