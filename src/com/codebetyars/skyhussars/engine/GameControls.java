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

import com.jme3.input.controls.ActionListener;

public class GameControls implements ActionListener {

    public static boolean turnOffAngular = false;
    private boolean paused = true;
    private boolean followCamera = true;
    private boolean freezed = false;
    private boolean reset = false;

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Pause") && isPressed && !freezed) {
            paused = !paused;
        } else if (name.equals("Camera") && isPressed) {
            followCamera = !followCamera;
        } else if (name.equals("TurnOffAngular") && isPressed) {
            turnOffAngular = !turnOffAngular;
        }  else if (name.equals("Reset") && isPressed) {
            reset = true;
        }
    }

    public boolean paused() {
        return paused;
    }

    public void freezed(boolean freezed) {
        this.freezed = freezed;
        this.paused = freezed;
    }
    public boolean freezed(){
        return freezed;
    }

    public void paused(boolean isPaused) {
        this.paused = isPaused;
    }
    
    public void reset(boolean reset){
        this.reset = reset;
    }
    
    public boolean reset(){
        return reset;
    }
            

    public void setFollowCamera(boolean follow) {
        followCamera = true;
    }

    public boolean isFollowCamera() {
        return followCamera;
    }
}
