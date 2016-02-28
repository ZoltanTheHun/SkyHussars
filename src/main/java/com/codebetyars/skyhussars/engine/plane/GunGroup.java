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

import com.codebetyars.skyhussars.engine.weapons.ProjectileManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GunGroup {

    public GunGroup(GunGroupDescriptor gunGroupDescriptor, ProjectileManager projectileManager) {
        name = gunGroupDescriptor.getName();
        gunLocations = new ArrayList<>();
        for (GunLocationDescriptor gunLocationDescriptor : gunGroupDescriptor.getGunLocations()) {
            gunLocations.add(new GunLocation(gunLocationDescriptor,
                    gunLocationDescriptor.getRoundsMax(), projectileManager));
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

    public void firing(boolean firing,Vector3f vLocation, Vector3f vVelocity,Quaternion vOrientation) {
        for (GunLocation gunLocation : gunLocations) {
            gunLocation.firing(firing,vLocation,vVelocity,vOrientation);
        }
    }
}
