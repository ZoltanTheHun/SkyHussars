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
        test.calculateResultantForce(airDensity, flow, situation, angularVelocity);
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
        test.calculateResultantForce(airDensity, flow, situation, angularVelocity);
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
        test.calculateResultantForce(airDensity, flow, situation, angularVelocity);
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
        test.calculateResultantForce(airDensity, flow, situation, angularVelocity);
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
        test.calculateResultantForce(airDensity, flow, situation, angularVelocity);
    }
    
    
}
