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

package com.codebetyars.skyhussars.engine.physics;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import org.junit.Test;

public class GeneralTest {

    private float aspectRatio = 6.37f;
    private float wingArea = 11f;
    private float airDensity = 1.2745f;

    @Test
    public void general0AoATest() {
        //first test: simple airfoil test against generated lift
        //if aoa = 0, symmetric airfoil should not generate flow
        //Vector3f wingLocation =  new Vector3f(-2.0f, 0, -0.05f);
        
        Vector3f wingLocation =  new Vector3f(0, 0, 0);
        float incidence = 0;
        float dehidralDegree = 0;
        SymmetricAirfoil test = new SymmetricAirfoil("WingA", wingLocation, wingArea, incidence, aspectRatio,  true,dehidralDegree);
        Vector3f flow = Vector3f.UNIT_Z.mult(300).negate();
        Quaternion situation = new Quaternion();
        Vector3f angularVelocity = Vector3f.ZERO;
        test.calculateResultantForce(airDensity, flow, angularVelocity);
    }
    
    @Test 
    public void generalIncidenceTestAt0AoA(){
        Vector3f wingLocation =  new Vector3f(0, 0, 0);
        float incidence = 1;
        float dehidralDegree = 0;
        SymmetricAirfoil test = new SymmetricAirfoil("WingA", wingLocation, wingArea, incidence, aspectRatio,  true,dehidralDegree);
        Vector3f flow = Vector3f.UNIT_Z.mult(300).negate();
        Quaternion situation = new Quaternion();
        Vector3f angularVelocity = Vector3f.ZERO;
        test.calculateResultantForce(airDensity, flow, angularVelocity);
    }
    
    @Test
    public void general90DegreeRotationTestAt0AoA(){
        Vector3f wingLocation =  new Vector3f(0, 0, 0);
        float incidence = 1;
        float dehidralDegree = 0;
        SymmetricAirfoil test = new SymmetricAirfoil("WingA", wingLocation, wingArea, incidence, aspectRatio,  true,dehidralDegree);
        Vector3f flow = Vector3f.UNIT_Z.mult(300).negate();
        Quaternion situation = new Quaternion().fromAngles(0, 0, 90*FastMath.DEG_TO_RAD);
        Vector3f angularVelocity = Vector3f.ZERO;
        test.calculateResultantForce(airDensity, flow, angularVelocity);
    }
    
    @Test
    public void general180DegreeRotationTestAt0AoA(){
        Vector3f wingLocation =  new Vector3f(0, 0, 0);
        float incidence = 1;
        float dehidralDegree = 0;
        SymmetricAirfoil test = new SymmetricAirfoil("WingA", wingLocation, wingArea, incidence, aspectRatio,  true,dehidralDegree);
        Vector3f flow = Vector3f.UNIT_Z.mult(300).negate();
        Quaternion situation = new Quaternion().fromAngles(0, 0, 180*FastMath.DEG_TO_RAD);
        Vector3f angularVelocity = Vector3f.ZERO;
        test.calculateResultantForce(airDensity, flow,angularVelocity);
    }
    
    @Test
    public void generalRudderTest(){
        Vector3f wingLocation =  new Vector3f(0, 0, 0);
        float incidence = 0;
        float dehidralDegree = 90;
        SymmetricAirfoil test = new SymmetricAirfoil("VerticalStabilizer", wingLocation, wingArea, incidence, aspectRatio,  true,dehidralDegree);
        /*Vector3f flow = new Quaternion().fromAngles(-1*FastMath.DEG_TO_RAD,0,0).mult(Vector3f.UNIT_Z).mult(300).negate();*/
        Vector3f flow = new Quaternion().fromAngles(0,0,0).mult(Vector3f.UNIT_Z).mult(300).negate();

        Quaternion situation = new Quaternion().fromAngles(10, 0, 0);
        Vector3f angularVelocity = Vector3f.ZERO;
        test.calculateResultantForce(airDensity, flow, angularVelocity);
    }
    
    
}
