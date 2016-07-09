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

import com.codebetyars.skyhussars.engine.loader.converters.Point3fToVector3fConverter;
import com.codebetyars.skyhussars.engine.loader.converters.Vector3fToPoint3fConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jme3.math.Vector3f;

public class AirfoilDescriptor {

    private String name;
    private Vector3f cog;
    private float wingArea;
    private float incidence;
    private float aspectRatio;
    private boolean damper;
    private float dehidralDegree;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonSerialize(converter = Vector3fToPoint3fConverter.class)
    public Vector3f getCog() {
        return cog;
    }

    @JsonDeserialize(converter = Point3fToVector3fConverter.class)
    public void setCog(Vector3f cog) {
        this.cog = cog;
    }

    public float getWingArea() {
        return wingArea;
    }

    public void setWingArea(float wingArea) {
        this.wingArea = wingArea;
    }

    public float getIncidence() {
        return incidence;
    }

    public void setIncidence(float incidence) {
        this.incidence = incidence;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public boolean isDamper() {
        return damper;
    }

    public void setDamper(boolean damper) {
        this.damper = damper;
    }

    public float getDehidralDegree() {
        return dehidralDegree;
    }

    public void setDehidralDegree(float dehidralDegree) {
        this.dehidralDegree = dehidralDegree;
    }

}
