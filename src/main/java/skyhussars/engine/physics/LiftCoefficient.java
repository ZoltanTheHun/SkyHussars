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

public class LiftCoefficient {

    /**
     * 
     * @param aoas List of angle of attacks
     * @param machs List of machs speeds
     * @param clfs  List of lift coefficients for angle of attacks and mach speeds
     */
    public LiftCoefficient(String name,float[] aoas, float[] machs,float[][] clfs){
        this.name = name;
        this.aoas = aoas;
        this.machs = machs;
        this.clfs = clfs;      
    }
    
    private final String name;
    private final float[] aoas;
    private final float[] machs;
    private final float[][] clfs;

    public float calc(float aoa,float machSpeed) {
        //abs is used for symmetric wings? not perfect
        float absAoa = abs(aoa);
        float liftCoefficient = 0f;
        for (int i = 1; i < aoas.length; i++) {
            if (absAoa < aoas[i]) {
                float diff = aoas[i] - aoas[i - 1];
                float real = absAoa - aoas[i - 1];
                float a = real / diff;
                float b = 1f - a;
                liftCoefficient = clfs[0][i] * a + clfs[0][i - 1] * b;
                break;
            }
        }
        return liftCoefficient;
    }
}
