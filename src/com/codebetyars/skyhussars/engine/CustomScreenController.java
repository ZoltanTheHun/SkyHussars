package com.codebetyars.skyhussars.engine;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class CustomScreenController implements ScreenController {
    
    private String speed;
    
    public void setSpeed(String speed){
        this.speed = speed;
    }

    public void bind(Nifty nifty, Screen screen) {
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }

    public String getSpeed() {
        String speedStr = "";
        return speedStr + speed;
    }
}
