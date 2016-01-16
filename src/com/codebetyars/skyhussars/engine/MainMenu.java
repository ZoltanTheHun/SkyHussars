package com.codebetyars.skyhussars.engine;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class MainMenu extends GameState implements ScreenController {

    public MainMenu(GuiManager guiManager, GameState game) {
        this.game = game;
        this.guiManager = guiManager;
    }
    private GameState game;
    private GuiManager guiManager;
    private float time = 0;
    //private boolean startGame = false;

    @Override
    public GameState update(float tpf) {
        GameState nextState = this;
        System.out.println("Start game" + guiManager.startGame);
        if (guiManager.startGame) {
            nextState = game;
            guiManager.startGame = false;
        }
        return nextState;
    }

    @Override
    public void close() {
    }

    @Override
    public void initialize() {
        guiManager.startGame = false;
        guiManager.cursor(true);
    }

    public void startGame() {
        guiManager.startGame = true;
    }

    public void bind(Nifty nifty, Screen screen) {
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }
}
