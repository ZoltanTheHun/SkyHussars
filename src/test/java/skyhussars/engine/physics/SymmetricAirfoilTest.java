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

import com.jme3.math.Vector3f;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymmetricAirfoilTest {

    private final static Logger logger = LoggerFactory.getLogger(SymmetricAirfoilTest.class);
    
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
    
    @Test
    public void symmetricAirfoilTest(){
        SymmetricAirfoil af = new SymmetricAirfoil.Builder()
                .name("Simple right wing")
                .aspectRatio(6.37f)
                .damper(true)
                .dehidralDegree(0.0f)
                .direction(Aileron.Direction.RIGHT)
                .incidence(1f)
                .wingArea(5.5175f)
                .cog(new Vector3f(4, 0, -0.2f)).build();
        af.tick(1.24f, Vector3f.UNIT_Z.negate().mult(300), Vector3f.ZERO);
        logger.info("Simple wing setup test."); 
        logger.info("Frontal wind, 1 degree of dihedral. Expected 1 degree of AoA");
        logger.info("Actual AoA: " + af.aoa());
        Assert.assertEquals(1f, af.aoa(), 0.1);
        logger.info("Current damp: " + af.damp());
        Assert.assertEquals(new Vector3f(0,0,0), af.damp());
        logger.info("Current lift: " + af.lift());
        logger.info("Current induced drag: " + af.inducedDrag());
        logger.info("Generated linear: " + af.linear() + " , generated torque: " + af.torque());
    }
    
    @Test
    public void aileronTest(){
        SymmetricAirfoil af = new SymmetricAirfoil.Builder()
                .name("Simple right wing")
                .aspectRatio(6.37f)
                .damper(true)
                .dehidralDegree(0.0f)
                .direction(Aileron.Direction.RIGHT)
                .incidence(1f)
                .wingArea(5.5175f)
                .cog(new Vector3f(-4, 0, -0.2f)).build();
        Aileron aileron = new Aileron(af, Aileron.Direction.RIGHT);
        aileron.controlAileron(0);
        logger.info("Simple aileron test."); 
        logger.info("Aileron at rest.");
        aileron.controlAileron(0);
        aileron.tick(1.24f, Vector3f.UNIT_Z.negate().mult(30000), Vector3f.ZERO);
        logger.info("Actual AoA: " + aileron.aoa());
        Assert.assertEquals(1f, aileron.aoa(), 0.1);
        logger.info("Current damp: " + af.damp());
        logger.info("Aileron at max deflection.");
        aileron.controlAileron(1);
        aileron.tick(1.24f, Vector3f.UNIT_Z.negate().mult(300), Vector3f.ZERO);
        logger.info("Actual AoA: " + aileron.aoa());
        logger.info("Current damp: " + af.damp());
        Assert.assertEquals(3f, aileron.aoa(), 0.1);
    }
    
    @Test
    public void dampingTest(){
        SymmetricAirfoil af = new SymmetricAirfoil.Builder()
                .name("Simple right wing")
                .aspectRatio(6.37f)
                .damper(true)
                .dehidralDegree(0.0f)
                .direction(Aileron.Direction.RIGHT)
                .incidence(1f)
                .wingArea(5.5175f)
                .cog(new Vector3f(-4, 0, -0.2f)).build();
        Aileron aileron = new Aileron(af, Aileron.Direction.RIGHT);
        aileron.controlAileron(0);
        logger.info("Simple damping test."); 
        logger.info("Damping test: Aileron at rest.");
        aileron.controlAileron(0);
        aileron.tick(1.24f, Vector3f.UNIT_Z.negate().mult(300), new Vector3f(0,0,2.79253f)); //max rate of roll should be 160degree
        logger.info("Damping test: Actual AoA: " + aileron.aoa());
        Assert.assertEquals(1f, aileron.aoa(), 0.1);
        logger.info("Damping test: Current damp: " + af.damp());
        logger.info("Damping test: Aileron at max deflection.");
        aileron.controlAileron(1);
        aileron.tick(1.24f, Vector3f.UNIT_Z.negate().mult(300), Vector3f.ZERO);
        logger.info("Damping test: Actual AoA: " + aileron.aoa());
        logger.info("Damping test: Current damp: " + af.damp());
        Assert.assertEquals(3f, aileron.aoa(), 0.1);
    }
    
}