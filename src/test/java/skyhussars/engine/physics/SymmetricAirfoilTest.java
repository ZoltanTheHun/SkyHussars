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
                .cog(new Vector3f(4, 0, -0.2f)).build();
        af.tick(1.24f, Vector3f.UNIT_Z.negate().mult(300), Vector3f.ZERO);
        logger.info("Simple wing setup test."); 
        logger.info("Frontal wind, 1 degree of dihedral. Expected 1 degree of AoA");
        logger.info("Actual AoA: " + af.aoa());
        Assert.assertEquals(1f, af.aoa(), 0.1);
        logger.info("Generated lift: " + af.linear() + " , generated torque: " + af.torque());
    }

}