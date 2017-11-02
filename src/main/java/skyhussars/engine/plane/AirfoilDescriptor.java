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

import skyhussars.engine.loader.converters.Point3fToVector3fConverter;
import skyhussars.engine.loader.converters.Vector3fToPoint3fConverter;
import skyhussars.engine.physics.Aileron;
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
    private float dihedralDegree;
    private Aileron.ControlDir direction;

    public String getName() {
        return name;
    }

    public AirfoilDescriptor setName(String name) {
        this.name = name;
        return this;
    }

    @JsonSerialize(converter = Vector3fToPoint3fConverter.class)
    public Vector3f getCog() {
        return cog;
    }

    @JsonDeserialize(converter = Point3fToVector3fConverter.class)
    public AirfoilDescriptor setCog(Vector3f cog) {
        this.cog = cog;
        return this;
    }

    public float getWingArea() {return wingArea;}
    public AirfoilDescriptor setWingArea(float wingArea) {this.wingArea = wingArea; return this;}
    
    public float getIncidence() {return incidence;}
    public AirfoilDescriptor setIncidence(float incidence) {this.incidence = incidence;return this;}
    
    public float getAspectRatio() {return aspectRatio;}
    public AirfoilDescriptor setAspectRatio(float aspectRatio) {this.aspectRatio = aspectRatio;return this;}
    
    public boolean isDamper() {return damper;}
    public AirfoilDescriptor setDamper(boolean damper) {this.damper = damper;return this;}
    
    public float getDehidralDegree() {return dihedralDegree; }
    public AirfoilDescriptor setDehidralDegree(float dehidralDegree) {this.dihedralDegree = dehidralDegree;return this;}
    
    public Aileron.ControlDir getDirection() {return direction; }
    public AirfoilDescriptor setDirection(Aileron.ControlDir direction) {this.direction = direction;return this;}

}
