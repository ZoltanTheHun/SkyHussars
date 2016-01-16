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

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

public class Controls {
    
    private InputManager inputManager;
    
    public Controls(InputManager inputManager){
        this.inputManager = inputManager;         
    }
    
    public void setUpControls(Plane playerPlane){
        inputManager.addMapping("NoseDown", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("NoseUp", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("RotateLeft", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("RotateRight", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Speed0", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Speed20", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Speed40", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("Speed60", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("Speed80", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("Speed100", new KeyTrigger(KeyInput.KEY_6));
        inputManager.addMapping("IncreaseDistLittle", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("IncreaseDistMuch", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("DecreaseDistLittle", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addMapping("DecreaseDistMuch", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addListener(playerPlane.getAnalogListener(), "Speed0", "Speed20", "Speed40", "Speed60", "Speed80", "Speed100");
       inputManager.addListener(playerPlane.getActionListener(),
                "NoseDown", "NoseUp","RotateLeft","RotateRight");
    }
    
    public void setUpCommonControls(ActionListener actionListener){
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Camera", new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping("TurnOffAngular", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("Reset",new KeyTrigger(KeyInput.KEY_R));
        inputManager.addListener(actionListener, "Pause","Camera","TurnOffAngular","Reset");           
    }
    
    
}
