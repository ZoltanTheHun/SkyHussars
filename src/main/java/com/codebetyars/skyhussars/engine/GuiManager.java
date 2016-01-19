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
