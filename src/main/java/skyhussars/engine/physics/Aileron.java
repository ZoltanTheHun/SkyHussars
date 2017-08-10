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

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class Aileron implements Airfoil {

    private final Airfoil airfoil;
    private final Direction side;
    private Quaternion qAileron = new Quaternion();
    
    @Override
    public Airfoil tick(float airDensity, Vector3f vVelocity, Vector3f angularVelocity){
        airfoil.tick(airDensity, qAileron.inverse().mult(vVelocity), angularVelocity);
        return this;
    }

    @Override
    public Vector3f linear(){
        return airfoil.linear();
    }
    
    @Override
    public Vector3f torque(){
        return airfoil.torque();
    }
    
    
    @Override
    public Vector3f cog() {
        return airfoil.cog();
    }

    @Override
    public String getName() {
        return airfoil.getName();
    }

    public enum Direction {

        LEFT(1), RIGHT(-1), HORIZONTAL_STABILIZER(1), VERTICAL_STABILIZER(1);
        private final float direction;

        Direction(float direction) {
            this.direction = direction;
        }

    }

    public Aileron(Airfoil airfoil, Direction side) {
        this.airfoil = airfoil;
        this.side = side;
    }
    
    @Override
    public Aileron.Direction direction() {return this.side;}

    public void controlAileron(float aileron) {
        Quaternion q = new Quaternion();
        qAileron = q.fromAngles(side.direction * aileron * FastMath.DEG_TO_RAD * 2f, 0, 0);
    }

}
