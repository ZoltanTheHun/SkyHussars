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

import com.codebetyars.skyhussars.engine.Pilot;
import com.jme3.input.controls.ActionListener;

public class FlightKeyboardControls implements ActionListener {

    private Pilot pilot;

    public FlightKeyboardControls(Pilot pilot) {
        this.pilot = pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }
    private boolean noseDown = false;
    private boolean noseUp = false;
    private boolean rotateLeft = false;
    private boolean rotateRight = false;
    private boolean fire = false;

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
            switch (name) {
                case "Throttle0%":
                    pilot.setThrottle(0);
                    break;
                case "Throttle20%":
                    pilot.setThrottle(0.2f);
                    break;
                case "Throttle40%":
                    pilot.setThrottle(0.4f);
                    break;
                case "Throttle60%":
                    pilot.setThrottle(0.6f);
                    break;
                case "Throttle80%":
                    pilot.setThrottle(0.8f);
                    break;
                case "Throttle100%":
                    pilot.setThrottle(1.0f);
                    break;
                case "NoseDown":
                    noseDown = true;
                    break;
                case "NoseUp":
                    noseUp = true;
                    break;
                case "RotateLeft":
                    rotateLeft = true;
                    break;
                case "RotateRight":
                    rotateRight = true;
                    break;
                case ControlsMapper.FIRE:
                    fire = true;
                    break;          
            }
        } else {
            switch (name) {
                case "NoseDown":
                    noseDown = false;
                    break;
                case "NoseUp":
                    noseUp = false;
                    break;
                case "RotateLeft":
                    rotateLeft = false;
                    break;
                case "RotateRight":
                    rotateRight = false;
                    break;
                case ControlsMapper.FIRE:
                    fire = false;
                    break;
            }
        }
        setNoseControl();
        setRotationControl();
        setFire();

    }

    private void setNoseControl() {
        if (noseDown == noseUp) {
            pilot.setElevator(0f);
        } else if (noseDown == true) {
            pilot.setElevator(-1f);
        } else {
            pilot.setElevator(1f);
        }
    }

    private void setRotationControl() {
        if (rotateLeft == rotateRight) {
            pilot.setAileron(0f);
        } else if (rotateLeft == true) {
            pilot.setAileron(-1f);
        } else {
            pilot.setAileron(1f);
        }
    }
    
    private void setFire(){
        pilot.firing(fire);
    }
}
