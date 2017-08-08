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
package skyhussars.engine.weapons;

import skyhussars.engine.plane.BulletDescriptor;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;

public class Bullet extends Projectile {

    private Vector3f vStartLocation;
    private Vector3f vLocation;
    private Vector3f vVelocity;
    private BulletDescriptor bulletDescriptor;

    public Bullet(Vector3f vLocation, Vector3f vVelocity, BulletDescriptor bulletDescriptor) {
        this.vStartLocation = vLocation.clone();
        /*temp fix to avoid self shooting*/
        this.vLocation = vLocation.add(vVelocity.normalize().mult(10));
        this.vVelocity = vVelocity.clone();
        this.bulletDescriptor = bulletDescriptor;
    }

    @Override
    public void update(float tpf) {
        vLocation = vLocation.add(vVelocity.mult(tpf));
    }

    @Override
    public Vector3f getLocation() {
        return vLocation;
    }

    @Override
    public Vector3f getVelocity() {
        return vVelocity;
    }

    @Override
    public BoundingVolume getHitbox() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override 
    public boolean isLive() {
        return vStartLocation.distance(vLocation) <= 2000;
    }
}
