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

import static com.jme3.math.FastMath.*;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import java.util.Arrays;
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
                .direction(Aileron.ControlDir.RIGHT)
                .incidence(1f)
                .wingArea(5.5175f)
                .aoaConst( new float[]{0, 2, 4, 6, 8, 10, 15, 30})
                .machs(new float[]{0.5f})
                .cls(new float[][]{{0f, 0.246f, 0.475f, 0.68f, 0.775f, 0.795f, 0.82f, 0.8f}})
                .cog(new Vector3f(4, 0, -0.2f)).build();
        AirfoilResponse afp = af.tick(1.24f, Vector3f.UNIT_Z.negate().mult(300), Vector3f.ZERO);
        logger.info("Simple wing setup test."); 
        logger.info("Frontal wind, 1 degree of dihedral. Expected 1 degree of AoA");
        logger.info("Actual AoA: " + afp.aoa);
        Assert.assertEquals(1f, afp.aoa, 0.1f);
        float[] dampAngles = new float[3];
        afp.damp.toAngles(dampAngles);
        logger.info("Damp angles: " + Arrays.toString(dampAngles));
        Assert.assertEquals("Z Damp angle : ",0f, RAD_TO_DEG * dampAngles[2], 0.1f);
        logger.info("Current lift: " + afp.lift);
        logger.info("Current induced drag: " + afp.inducedDrag);
        logger.info("Generated linear: " + afp.linear + " , generated torque: " + afp.torque);
    }
    
    @Test
    public void aileronTest(){
        SymmetricAirfoil af = new SymmetricAirfoil.Builder()
                .name("Simple right wing")
                .aspectRatio(6.37f)
                .damper(true)
                .dehidralDegree(0.0f)
                .direction(Aileron.ControlDir.RIGHT)
                .incidence(1f)
                .wingArea(5.5175f)
                .cog(new Vector3f(-4, 0, -0.2f))
                .aoaConst( new float[]{0, 2, 4, 6, 8, 10, 15, 30})
                .machs(new float[]{0.5f})
                .cls(new float[][]{{0f, 0.246f, 0.475f, 0.68f, 0.775f, 0.795f, 0.82f, 0.8f}}).build();
        Aileron aileron = new Aileron(af, Aileron.ControlDir.RIGHT);
        aileron.controlAileron(0);
        logger.info("Simple aileron test."); 
        logger.info("Aileron at rest.");
        aileron.controlAileron(0);
        AirfoilResponse afp = aileron.tick(1.24f, Vector3f.UNIT_Z.negate().mult(30000), Vector3f.ZERO);
        logger.info("Actual AoA: " + afp.aoa);
        Assert.assertEquals(1f, afp.aoa, 0.1);
        float[] dampAngles = new float[3];
        afp.damp.toAngles(dampAngles);
        logger.info("Damp angles: " + Arrays.toString(dampAngles));
        logger.info("Aileron at max deflection.");
        aileron.controlAileron(1);
        aileron.tick(1.24f, Vector3f.UNIT_Z.negate().mult(300), Vector3f.ZERO);
        logger.info("Actual AoA: " + afp.aoa);
        dampAngles = new float[3];
        afp.damp.toAngles(dampAngles);
        logger.info("Damp angles: " + Arrays.toString(dampAngles));
        Assert.assertEquals(3f, afp.aoa, 0.1);
    }
    
    
    private final float mass = 5307.0f;
    private final float rPlane = 1.3f;
    private float length = length = 10.49f;
    private Matrix3f  momentOfInertiaTensor = new Matrix3f((mass / 12) * (3 * rPlane * rPlane + length * length), 0f, 0f,
                0f, (mass / 12) * (3 * rPlane * rPlane + length * length), 0f,
                0f, 0f, (mass / 2) * (rPlane * rPlane));
    @Test
    public void dampingTest(){
        SymmetricAirfoil af = new SymmetricAirfoil.Builder()
                .name("Simple right wing")
                .aspectRatio(6.37f)
                .damper(true)
                .dehidralDegree(0.0f)
                .direction(Aileron.ControlDir.RIGHT)
                .incidence(1f)
                .wingArea(5.5175f)
                .cog(new Vector3f(-4, 0, -0.2f))
                .dampingFactor(2)
                .aoaConst( new float[]{0, 2, 4, 6, 8, 10, 15, 30})
                .machs(new float[]{0.5f})
                .cls(new float[][]{{0f, 0.246f, 0.475f, 0.68f, 0.775f, 0.795f, 0.82f, 0.8f}})
                .build();
        Aileron aileron = new Aileron(af, Aileron.ControlDir.RIGHT);
        aileron.controlAileron(0);
        logger.info("Simple damping test."); 
        logger.info("Damping test: Aileron at rest.");
        aileron.controlAileron(0);
        //aileron.tick(1.24f, Vector3f.UNIT_Z.negate().mult(300), new Vector3f(0,0,2.79253f)); //max rate of roll should be 160degree
        logger.info("Rolling: 1 degree.");
        AirfoilResponse afp = aileron.tick(1.24f, Vector3f.UNIT_Z.negate().mult(300), new Vector3f(0,0,0.01745f));  // 1 degree of roll
        logger.info("Damping test: Actual AoA: " + afp.aoa);
        Assert.assertEquals(1f, afp.aoa, 0.1);
        Vector3f angAcc = momentOfInertiaTensor.invert().mult(afp.torque);
        logger.info("Damping test: Angular Acceleration: " + angAcc);
        float[] dampAngles = new float[3];
        afp.damp.toAngles(dampAngles);
        logger.info("Damping test: Damp angles: " + Arrays.toString(dampAngles));
        logger.info("Damping test: Aileron at max deflection.");
        aileron.controlAileron(1);
        aileron.tick(1.24f, Vector3f.UNIT_Z.negate().mult(300), Vector3f.ZERO);
        logger.info("Damping test: Actual AoA: " + afp.aoa);
        dampAngles = new float[3];
        afp.damp.toAngles(dampAngles);
        logger.info("Damping test: Damp angles: " + Arrays.toString(dampAngles));
        Assert.assertEquals(3f, afp.aoa, 0.1);
    }
    
}