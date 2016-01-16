package com.codebetyars.skyhussars.engine;

import com.jme3.asset.AssetManager;

public class DataManager {

    private SoundManager soundManager;
    private ModelManager modelManager;

    public DataManager(AssetManager assetManager) {
        this.modelManager = new ModelManager(assetManager);
        this.soundManager = new SoundManager(assetManager);
    }

    
    public ModelManager modelManager(){
        return modelManager;
    }
    public SoundManager soundManager(){
        return soundManager;
    }
}
