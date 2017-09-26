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

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import skyhussars.engine.weapons.Bullet;
import static skyhussars.utility.Streams.list;
import static skyhussars.utility.Streams.pm;

public class GunGroup {
    public GunGroup(GunGroupDescriptor gunGroupDescriptor) {
        name = gunGroupDescriptor.getName();
        gunLocations = new ArrayList<>();
        for (GunLocationDescriptor gunLocationDescriptor : gunGroupDescriptor.getGunLocations()) {
            gunLocations.add(new GunLocation(gunLocationDescriptor,
                    gunLocationDescriptor.getRoundsMax()));
        }
    }
    private String name;
    private List<GunLocation> gunLocations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GunLocation> getGunLocations() {
        return gunLocations;
    }

    public void setGunLocations(List<GunLocation> gunLocations) {
        this.gunLocations = gunLocations;
    }

    public List<Bullet> firing(boolean firing,Vector3f vLocation, Vector3f vVelocity,Quaternion vOrientation) {
        return list(pm(gunLocations,gunLocation -> gunLocation.firing(firing,vLocation,vVelocity,vOrientation))
                .filter(Objects::nonNull));
    }
}
