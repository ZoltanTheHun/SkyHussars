package com.codebetyars.skyhussars.engine.gamestates;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainMenu implements ScreenController {

    private final static Logger logger = LoggerFactory.getLogger(MainMenu.class);

    @Autowired
    private MenuState menu;

    public void openSingleMissionMenu() {
        nifty.gotoScreen("singleMissionMenu");
    }

    public void exitGame() {
        menu.exitGame();
    }

    private Nifty nifty;
    private Screen screen;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }
}
