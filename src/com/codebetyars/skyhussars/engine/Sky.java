package com.codebetyars.skyhussars.engine;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

public class Sky {

    public Spatial getSky(AssetManager assetManager) {
        
        return SkyFactory.createSky(
                assetManager, assetManager.loadTexture("Textures/skydome.png"),
                new Vector3f(0.8f,1f,1f).normalize()/*Vector3f.UNIT_XYZ*/, true,200050);
    }
}
