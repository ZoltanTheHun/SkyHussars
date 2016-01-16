package com.codebetyars.skyhussars.engine;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private Map<String, AudioNode> sounds = new HashMap<String, AudioNode>();

    public SoundManager(AssetManager assetManager) {
        prepareEngineSound(assetManager);
    }

    private void prepareEngineSound(AssetManager assetManager) {
        AudioNode engineSound = new AudioNode(assetManager, "Sounds/jet.wav", false);
        engineSound.setLooping(true);  // activate continuous playing
        engineSound.setPositional(true);
        engineSound.setVolume(3);
        engineSound.setPitch(1f);
        sounds.put("engine", engineSound);
    }

    public AudioNode sound(String key) {
        return sounds.get(key);
    }
}
