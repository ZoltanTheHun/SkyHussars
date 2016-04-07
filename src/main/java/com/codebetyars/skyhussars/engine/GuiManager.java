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

import com.codebetyars.skyhussars.engine.gamestates.MainMenuControls;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GuiManager implements ScreenController, InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(GuiManager.class);

    @Autowired
    private AssetManager assetManager;

    @Autowired
    private InputManager inputManager;

    @Autowired
    private AudioRenderer audioRenderer;

    @Autowired
    private ViewPort guiViewPort;

    @Autowired
    private CameraManager cameraManager;
    
    @Autowired
    private MainMenuControls mainMenuControls;

    private Nifty nifty;

    private NiftyJmeDisplay niftyDisplay;

    @Override
    public void afterPropertiesSet() throws Exception {
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
    }

    public void createGUI() {
        nifty.fromXml("Interface/BasicGUI.xml", "start", mainMenuControls);
        nifty.addControls();
        nifty.update();
        mainMenuControls.init(nifty);
        guiViewPort.addProcessor(niftyDisplay);
    }


    public void update(String speed) {
        nifty.getCurrentScreen().findElementByName("speedDisplay").
                getRenderer(TextRenderer.class).setText(speed + "km/h");
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

}
