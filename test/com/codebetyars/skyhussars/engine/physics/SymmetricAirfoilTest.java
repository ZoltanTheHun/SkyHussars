/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codebetyars.skyhussars.engine.physics;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Zolko
 */
public class SymmetricAirfoilTest {
    
    public SymmetricAirfoilTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of calculateResultantForce method, of class SymmetricAirfoil.
     */
    @Test
    public void testCalculateResultantForce() {
        System.out.println("calculateResultantForce");
        float airDensity = 0.0F;
        Vector3f vVelocity = null;
        Quaternion horizontal = null;
        Vector3f angularVelocity = null;
        SymmetricAirfoil instance = null;
        Vector3f expResult = null;
        Vector3f result = instance.calculateResultantForce(airDensity, vVelocity, horizontal, angularVelocity);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    
    /**
     * Test of calculateLift method, of class SymmetricAirfoil.
     */
    @Test
    public void testCalculateLift_4args() {
        System.out.println("calculateLift");
        float angleOfAttack = 0.0F;
        float airDensity = 0.0F;
        Vector3f vFlow = null;
        Quaternion situation = null;
        SymmetricAirfoil instance = null;
        Vector3f expResult = null;
        Vector3f result = instance.calculateLift(angleOfAttack, airDensity, vFlow, situation.mult(Vector3f.UNIT_Y));
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calculateLift method, of class SymmetricAirfoil.
     */
    @Test
    public void testCalculateLift_3args() {
        System.out.println("calculateLift");
        float angleOfAttack = 0.0F;
        float airDensity = 0.0F;
        Vector3f vFlow = null;
        SymmetricAirfoil instance = null;
        float expResult = 0.0F;
        float result = instance.calculateLift(angleOfAttack, airDensity, vFlow);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLiftCoefficient method, of class SymmetricAirfoil.
     */
    @Test
    public void testGetLiftCoefficient() {
        System.out.println("getLiftCoefficient");
        float angleOfAttack = 0.0F;
        SymmetricAirfoil instance = null;
        float expResult = 0.0F;
        float result = instance.getLiftCoefficient(angleOfAttack);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calculateInducedDrag method, of class SymmetricAirfoil.
     */
    @Test
    public void testCalculateInducedDrag_3args() {
        System.out.println("calculateInducedDrag");
        float airDensity = 0.0F;
        Vector3f vFlow = null;
        Vector3f vLift = null;
        SymmetricAirfoil instance = null;
        Vector3f expResult = null;
        Vector3f result = instance.calculateInducedDrag(airDensity, vFlow, vLift);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calculateInducedDrag method, of class SymmetricAirfoil.
     */
    @Test
    public void testCalculateInducedDrag_float_Vector3f() {
        System.out.println("calculateInducedDrag");
        float airDensity = 0.0F;
        Vector3f vVelocity = null;
        SymmetricAirfoil instance = null;
        float expResult = 0.0F;
        float result = instance.calculateInducedDrag(airDensity, vVelocity);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCenterOfGravity method, of class SymmetricAirfoil.
     */
    @Test
    public void testGetCenterOfGravity() {
        System.out.println("getCenterOfGravity");
        SymmetricAirfoil instance = null;
        Vector3f expResult = null;
        Vector3f result = instance.getCenterOfGravity();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class SymmetricAirfoil.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        SymmetricAirfoil instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of controlAileron method, of class SymmetricAirfoil.
     */
    @Test
    public void testControlAileron() {
        System.out.println("controlAileron");
        int aileron = 0;
        SymmetricAirfoil instance = null;
        instance.controlAileron(aileron);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}