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
package com.codebetyars.skyhussars.engine.sound;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.Listener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class SoundManager implements InitializingBean {

    @Autowired
    private AssetManager assetManager;

    private Map<String, AudioNode> sounds = new HashMap<>();
    private List<AudioHandler> requestedHandlers = new LinkedList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        prepareEngineSound();
    }

    @Autowired
    private Listener listener;

    private void prepareEngineSound() {
        AudioNode engineSound = new AudioNode(assetManager, "Sounds/jet.wav", false);
        AudioNode gunSound = new AudioNode(assetManager, "Sounds/shoot.ogg", false);

        /*
         Probably the sound of the engines will be combined with the engine implementation.
         However probably the doppler effect the setVelocity call is also necessary
         */
        engineSound.setLooping(true);
        engineSound.setPositional(true);
        engineSound.setReverbEnabled(true);
        engineSound.setMaxDistance(10000f);
        engineSound.setRefDistance(5f);
        engineSound.setVolume(3f);
        engineSound.setPitch(1f);

        gunSound.setLooping(true);
        gunSound.setPositional(true);
        sounds.put("engine", engineSound);
        sounds.put("gun", gunSound);
    }

    public AudioHandler sound(String key) {
        AudioHandler handler = new AudioHandler(sounds.get(key).clone());
        requestedHandlers.add(handler);
        return handler;
    }

    public void muteAllSounds() {
        requestedHandlers.stream().forEach(handler -> {
            handler.stop();
        });
    }

    public void update() {
        if (requestedHandlers.size() > 20) {
            requestedHandlers.sort((handler1, handler2) -> {
                float dist1 = handler1.audioNode().getWorldTranslation().distance(listener.getLocation());
                float dist2 = handler2.audioNode().getWorldTranslation().distance(listener.getLocation());
                if (dist1 > dist2) {
                    return 1;
                } else {
                    return -1;
                }
            });
        }
        requestedHandlers.stream().limit(20).forEach(handler -> handler.update());
        requestedHandlers.stream().skip(20).forEach(handler -> {
            handler.stop();
            handler.update();
        });
    }
}
