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
