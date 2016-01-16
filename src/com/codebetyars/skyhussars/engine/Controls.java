/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
