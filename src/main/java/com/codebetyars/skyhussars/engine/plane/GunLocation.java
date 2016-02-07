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
package com.codebetyars.skyhussars.engine.plane;

import com.codebetyars.skyhussars.engine.weapons.Bullet;
import com.codebetyars.skyhussars.engine.weapons.ProjectileManager;
import com.jme3.math.Vector3f;

public class GunLocation {

    private GunLocationDescriptor gunLocationDescriptor;
    private int rounds;
    private ProjectileManager projectileManager;

    public GunLocation(GunLocationDescriptor gunLocationDescriptor, int rounds, ProjectileManager projectileManager) {
        this.gunLocationDescriptor = gunLocationDescriptor;
        /*should check if rounnds > maxRounds*/
        this.rounds = rounds;
        this.projectileManager = projectileManager;
    }

    public void firing(boolean firing, Vector3f vLocation, Vector3f vVelocity, Vector3f vOrientation) {
        if (firing) {
            /*actually this should be here the plane orientation corrected with gun orientation*/
            Vector3f vBulletVelocity = vOrientation.negate().normalize().mult(gunLocationDescriptor.
                    getGunDescriptor().getMuzzleVelocity());
            vVelocity = vVelocity.add(vBulletVelocity);
            Bullet bullet = new Bullet(vLocation, vVelocity, null);
            projectileManager.addProjectile(bullet);
        }
    }
}
