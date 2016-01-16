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

public class WorldPhysicsData {
    
    //for airdensity look for Density altitude tables,which also uses temperature and preassure
    // for the sake of simplicty this implementation we will use standard atmosphere data
    
    private static int[] airDensityAltitude = {-1000,0,1000,2000,3000,4000,5000,6000,
        7000,8000,9000,10000,15000,20000,25000,30000,40000,50000,60000,70000,80000};
    private static float[] airDensity = {1.347f,1.225f,1.112f,1.007f,0.9093f,0.8194f,
        0.7364f,0.6601f,0.59f,0.5258f,0.4671f,0.4135f,0.1948f,0.08891f,0.04008f,
        0.01841f,0.003996f,0.001027f,0.0003097f,0.00008283f,0.00001846f};
    
    public static float getAirDensity(int altitude){
        int index = 0;
        for(int altitudeIndicator : airDensityAltitude){
            if(altitudeIndicator > altitude) break;
            index ++;
        }
        return airDensity[index];
    }
}
