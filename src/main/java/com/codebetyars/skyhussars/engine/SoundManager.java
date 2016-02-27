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
import com.jme3.audio.AudioNode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class SoundManager implements InitializingBean{

    @Autowired
    private AssetManager assetManager;

    private Map<String, AudioNode> sounds = new HashMap<>();
    private List<AudioNode> requestedNodes = new LinkedList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        prepareEngineSound();
    }

    private void prepareEngineSound() {
        AudioNode engineSound = new AudioNode(assetManager, "Sounds/jet.wav", false);
        AudioNode gunSound = new AudioNode(assetManager, "Sounds/shoot.ogg", false);
        engineSound.setLooping(true);  // activate continuous playing
        engineSound.setPositional(true);
        engineSound.setVolume(3);
        engineSound.setPitch(1f);
        gunSound.setLooping(true);
        gunSound.setPositional(true);
        sounds.put("engine", engineSound);
        sounds.put("gun", gunSound);
    }

    public AudioNode sound(String key) {
        AudioNode sound = sounds.get(key).clone();
        requestedNodes.add(sound);
        return sound;
    }
    
    public void muteAllSounds(){
        for(AudioNode sound : requestedNodes){
            sound.stop();
        }
    }
}
