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
package com.codebetyars.skyhussars.engine.controls;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ControlsMapper {

    public static final String FIRE = "Fire";
    public static final String INCREASE_FOV = "IncreaseFov";
    public static final String DECREASE_FOV = "DecreaseFov";

    @Value("#{application.inputManager}")
    private InputManager inputManager;

    public void setupFlowControls(ActionListener actionListener) {
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Camera", new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addListener(actionListener, "Pause", "Camera", "Reset");
    }

    public void setupFlightKeyboardControls(FlightKeyboardControls flightKeyboardControls) {
        inputManager.addMapping("NoseDown", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("NoseUp", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("RotateLeft", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("RotateRight", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Throttle0%", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Throttle20%", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Throttle40%", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("Throttle60%", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("Throttle80%", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("Throttle100%", new KeyTrigger(KeyInput.KEY_6));
        inputManager.addMapping(FIRE, new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(flightKeyboardControls, "Throttle0%",
                "Throttle20%", "Throttle40%", "Throttle60%", "Throttle80%", "Throttle100%",
                "NoseDown", "NoseUp", "RotateLeft", "RotateRight", FIRE);
    }

    public void setupCameraControls(CameraControls cameraControls) {
        inputManager.addMapping(INCREASE_FOV, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(DECREASE_FOV, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(cameraControls, INCREASE_FOV,DECREASE_FOV);
    }
}
