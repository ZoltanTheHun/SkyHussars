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
package skyhussars.engine.plane;

import skyhussars.engine.weapons.Bullet;
import com.jme3.math.Quaternion;
import com.jme3.math.Ring;
import com.jme3.math.Vector3f;

import java.util.Random;

public class GunLocation {

    private int rounds;
    private final Random random;

    private final Vector3f location;
    private final float muzzleVelocity;
    private final float spread;
    private final float rof;

    public GunLocation(GunLocationDescriptor gunLocationDescriptor, int rounds) {
        /*should check if rounnds > maxRounds*/
        this.rounds = rounds;
        this.random = new Random();
        this.location = gunLocationDescriptor.getLocation();
        this.muzzleVelocity = gunLocationDescriptor.getGunDescriptor().getMuzzleVelocity();
        this.spread = gunLocationDescriptor.getGunDescriptor().getSpread();
        this.rof = gunLocationDescriptor.getGunDescriptor().getRateOfFire();
    }

    public Vector3f addSpread(Vector3f vVelocity) {
        float locSpread = spread * (float) random.nextGaussian() * vVelocity.length() / 100f;
        return new Ring(vVelocity, vVelocity.normalize(), locSpread, locSpread).random();
    }

    public Bullet firing(boolean firing, Vector3f vLocation, Vector3f vVelocity, Quaternion vOrientation) {
        Bullet bullet = null;
        if (firing) {
            Vector3f vBulletLocation = vLocation.add(vOrientation.mult(location));
            Vector3f vMuzzleVelocity = vOrientation.mult(Vector3f.UNIT_Z).mult(muzzleVelocity);
            Vector3f vBulletVelocity = addSpread(vVelocity.add(vMuzzleVelocity));
            bullet = new Bullet(vBulletLocation, vBulletVelocity, vOrientation);
        }
        return bullet;
    }
}
