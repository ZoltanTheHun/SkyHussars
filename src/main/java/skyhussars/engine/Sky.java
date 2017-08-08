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
package skyhussars.engine;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import java.util.Calendar;
import java.util.GregorianCalendar;
import jme3utilities.sky.SkyControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sky {

    @Autowired
    private Node rootNode;
    @Autowired
    private AssetManager assetManager;
    @Autowired
    private ComplexCamera camera;
    @Autowired
    private Lighting lighting;

    private int hour;
    private SkyControl skyControl;

    public Spatial getSky(AssetManager assetManager) {

        return SkyFactory.createSky(
                assetManager, assetManager.loadTexture("Textures/skydome.png"),
                new Vector3f(0.8f, 1f, 1f).normalize()/*Vector3f.UNIT_XYZ*/, true, 200050);
    }

    public void init() {
        Calendar now = new GregorianCalendar();
        skyControl = new SkyControl(assetManager, camera.testCamera(), 0.9f, true, true);
        skyControl.getSunAndStars().setHour(now.get(Calendar.HOUR_OF_DAY));
        skyControl.getSunAndStars().setSolarLongitude(now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        skyControl.getSunAndStars().setObserverLatitude(37.4046f * FastMath.DEG_TO_RAD);
        skyControl.setCloudiness(1f);
        rootNode.addControl(skyControl);
        skyControl.setEnabled(true);
        skyControl.getSunAndStars().setHour(hour);
        lighting.setLightingBodies(skyControl.getSunAndStars().getSunDirection(), skyControl.getMoonDirection());
    }
    public void setHour(int hour){
        this.hour = hour;
    }

    public void disableSky() {
        skyControl.setEnabled(false);
        rootNode.removeControl(skyControl);
    }
}
