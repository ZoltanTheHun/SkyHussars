package com.codebetyars.skyhussars.engine;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class GuiManager implements ScreenController {

    private Nifty nifty;
    private InputManager inputManager;
    private String guiLocation;
    private ViewPort guiViewPort;
    private NiftyJmeDisplay niftyDisplay;
    private CameraManager cameraManager;

    public GuiManager(AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer,
            ViewPort guiViewPort, String guiLocation,CameraManager cameraManager) {
        niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        this.inputManager = inputManager;
        this.guiViewPort = guiViewPort;
        nifty = niftyDisplay.getNifty();
        this.startGame = false;
        this.guiLocation = guiLocation;
        this.cameraManager = cameraManager;
    }

    public void createGUI() {
        nifty.fromXml(guiLocation, "start", this);
        nifty.addControls();
        nifty.update();
        guiViewPort.addProcessor(niftyDisplay);
    }

    public void update(String speed) {
        nifty.getCurrentScreen().findElementByName("speedDisplay").getRenderer(TextRenderer.class).setText(speed + "km/h");
    }

    public void switchScreen(String screenId) {
        nifty.gotoScreen(screenId);
    }

    public void cursor(boolean cursor) {
        inputManager.setCursorVisible(cursor);
        cameraManager.flyCamActive(cursor);
    }

    public boolean cursor() {
        return inputManager.isCursorVisible();
    }

    public void bind(Nifty nifty, Screen screen) {
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }
    /*
     temp solution, needs restructuring dependencies
     */
    public boolean startGame = false;

    public void startGame() {
        startGame = true;
    }
}
