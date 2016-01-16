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
        sc.getSunAndStars().setHour(12f);
        sc.getSunAndStars().setObserverLatitude(37.4046f * FastMath.DEG_TO_RAD);
        sc.getSunAndStars().setSolarLongitude(Calendar.FEBRUARY, 10);
        sc.setCloudiness(1f);
        sc.setEnabled(true);
        setupLighting(node);
    }

    private void setupLighting(Node node) {
        Lighting lighting = new Lighting();
        for (Light light : lighting.getLights()) {
            node.addLight(light);
        }
    }
}
