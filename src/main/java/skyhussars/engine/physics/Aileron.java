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

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import static com.jme3.math.FastMath.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Aileron implements Airfoil {
    private final static Logger logger = LoggerFactory.getLogger(Aileron.class);
    private final Airfoil airfoil;
    private final ControlDir side;
    private Quaternion qAileron = new Quaternion();

    @Override
    public AirfoilResponse tick(float airDensity, Vector3f vVelocity, Vector3f angularVelocity) {
        return airfoil.tick(airDensity, qAileron.inverse().mult(vVelocity), angularVelocity);
    }


    @Override
    public Vector3f cog() {
        return airfoil.cog();
    }

    @Override
    public String name() {
        return airfoil.name();
    }

    public enum ControlDir {

        LEFT(1), RIGHT(-1), HORIZONTAL_STABILIZER(1), VERTICAL_STABILIZER(1);
        private final float direction;

        ControlDir(float direction) { this.direction = direction; }

    }

    public Aileron(Airfoil airfoil, ControlDir side) {
        this.airfoil = airfoil;
        this.side = side;
    }
    
    public Aileron(Airfoil airfoil, ControlDir side,float maxDeflection) {
        this.airfoil = airfoil;
        this.side = side;
        this.maxDeflection = maxDeflection;
    }

    @Override
    public Aileron.ControlDir direction() {
        return this.side;
    }
    private float maxDeflection = 2f;   //degree
    public void controlAileron(float aileron) {
        aileron = abs(aileron) > 0.15 ? aileron : 0; /* magical value until proper joystick sentitivity is added */ 
        float deflection = side.direction * aileron * DEG_TO_RAD * maxDeflection;
        qAileron = new Quaternion().fromAngles(deflection, 0, 0);
    }

}
