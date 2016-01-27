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
package com.codebetyars.skyhussars.engine;

import com.jme3.asset.AssetManager;
import com.jme3.light.Light;
import com.jme3.math.FastMath;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.Calendar;
import jme3utilities.sky.SkyControl;

public class DayLightWeatherManager {

    public DayLightWeatherManager(AssetManager assetManager, Camera camera, Node node) {
        SkyControl sc = new SkyControl(assetManager, camera, 0.9f, true, true);
        node.addControl(sc);
        sc.getSunAndStars().setHour(5f);
        sc.getSunAndStars().setObserverLatitude(37.4046f * FastMath.DEG_TO_RAD);
        sc.getSunAndStars().setSolarLongitude(Calendar.AUGUST, 10);
        System.out.println("Sun location: " + sc.getSunAndStars().getSunDirection());
        sc.setCloudiness(0f);
        sc.setEnabled(true);

        Lighting lighting = new Lighting();
        lighting.setLightingBodies(sc.getSunAndStars().getSunDirection(), sc.getMoonDirection());

        for (Light light : lighting.getLights()) {
            node.addLight(light);
        }

    }
}
