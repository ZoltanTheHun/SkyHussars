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

import java.util.List;

public class PlaneDescriptor {

    private String name;
    private String modelName;
    private List<EngineLocation> engineLocations;
    private float massEmpty;
    private float massTakeOffMax;
    private float massGross;
    //lets use liter for now, density: 	775.0-840.0 g/L average: 0.8 kg/l
    private float internalTank;
    private List<GunGroupDescriptor> gunGroupDescriptors;

    public List<GunGroupDescriptor> getGunGroupDescriptors() {
        return gunGroupDescriptors;
    }

    public void setGunGroupDescriptors(List<GunGroupDescriptor> gunGroupDescriptors) {
        this.gunGroupDescriptors = gunGroupDescriptors;
    }

    public float getInternalTank() {
        return internalTank;
    }

    public void setInternalTank(float internalTank) {
        this.internalTank = internalTank;
    }

    public float getMassEmpty() {
        return massEmpty;
    }

    public void setMassEmpty(float emptyMass) {
        this.massEmpty = emptyMass;
    }

    public float getMassTakeOffMax() {
        return massTakeOffMax;
    }

    public void setMassTakeOffMax(float takeOffMassMax) {
        this.massTakeOffMax = takeOffMassMax;
    }

    public float getMassGross() {
        return massGross;
    }

    public void setMassGross(float grossMass) {
        this.massGross = grossMass;
    }

    public List<EngineLocation> getEngineLocations() {
        return engineLocations;
    }

    public void setEngineLocations(List<EngineLocation> engineLocations) {
        this.engineLocations = engineLocations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
