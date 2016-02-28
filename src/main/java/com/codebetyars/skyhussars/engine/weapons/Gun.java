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
package com.codebetyars.skyhussars.engine.weapons;

import com.codebetyars.skyhussars.engine.plane.GunLocationDescriptor;
import com.jme3.math.Vector3f;

import java.util.LinkedList;
import java.util.List;

public class Gun {
    //M3 shoots 1200 bullets per minute -> 20 per second
    //rate of fire is in second

    private GunLocationDescriptor gunLocation;
    private int ammo;

    public Gun(GunLocationDescriptor gunLocation) {
        this.gunLocation = gunLocation;
        this.ammo = gunLocation.getRoundsMax();
    }
    /*bullets procuded since last update*/
    private float bpslu = 0;

    //refine it later on, it is not enough to pass velocity only
    public List<Projectile> update(float tpf, boolean firing, Vector3f noseDirection,
            Vector3f vPlaneVelocity, Vector3f vPlaneLocation) {
        bpslu += gunLocation.getGunDescriptor().getRateOfFire() * tpf;
        /*gunLocation.getGunDescriptor().getMuzzleVelocity();*/
        List<Projectile> projectiles = new LinkedList<>();
        while (bpslu-- > 1) {
            Bullet bullet = new Bullet(Vector3f.ZERO, Vector3f.ZERO, null);
            projectiles.add(bullet);
            bpslu -= 1;
        }
        return projectiles;

    }
}
