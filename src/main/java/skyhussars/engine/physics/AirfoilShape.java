/*
 * Copyright (c) 2017, ZoltanTheHun
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

import static com.jme3.math.FastMath.abs;

public class AirfoilShape {

    /**
     * 
     * @param aoas List of angle of attacks
     * @param machs List of machs speeds
     * @param clfs  List of lift coefficients for angle of attacks and mach speeds
     */
    public AirfoilShape(float[] aoas, float[] machs,float[][] clfs){               
    }
    private final float[] constAoa = {0, 2, 4, 6, 8, 10, 15, 30};
    private final float[] machs = {0.5f};
    private final float[] clm05 = {0f, 0.246f, 0.475f, 0.68f, 0.775f, 0.795f, 0.82f, 0.8f};

    public float liftCoefficient(float aoa,float machSpeed) {
        //abs is used for symmetric wings? not perfect
        float absAoa = abs(aoa);
        float liftCoefficient = 0f;
        for (int i = 1; i < constAoa.length; i++) {
            if (absAoa < constAoa[i]) {
                float diff = constAoa[i] - constAoa[i - 1];
                float real = absAoa - constAoa[i - 1];
                float a = real / diff;
                float b = 1f - a;
                liftCoefficient = clm05[i] * a + clm05[i - 1] * b;
                break;
            }
        }
        return liftCoefficient;
    }
}
