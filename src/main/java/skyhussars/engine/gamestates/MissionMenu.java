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
package skyhussars.engine.gamestates;

import skyhussars.engine.camera.CameraManager;
import com.jme3.input.InputManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MissionMenu implements ScreenController {

    @Autowired
    private InputManager inputManager;

    @Autowired
    private MenuState menuState;

    @Autowired
    private CameraManager cameraManager;

    private Nifty nifty;
    private Screen screen;

    private final static Logger logger = LoggerFactory.getLogger(MissionMenu.class);
    private MissionState mission;
    private boolean popupToBeClosed;
    private boolean popupVisible;

    public void exitToDesktop() {
        mission.switchState(null);
    }

    public void exitToMenu() {
        mission.switchState(menuState);
        menuState.reset();
        nifty.gotoScreen("singleMissionMenu");
    }

    public void closePopup() {
        popupToBeClosed = true;
    }

    public boolean popupToBeClosed() {
        return popupToBeClosed;
    }

    public void resetPopupToBeClosed() {
        popupToBeClosed = false;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        mission = menuState.currentMission();
        mission.speedoMeterUI(screen.findElementById("speedDisplay").getRenderer(TextRenderer.class));
        mission.altimeterUI(screen.findElementById("altimeter").getRenderer(TextRenderer.class));

        inputManager.setCursorVisible(false);
        popupVisible = false;
        Element exitMenu = screen.findElementById("exitMenuPanel");
        exitMenu.setVisible(false);
    }

    @Override
    public void onEndScreen() {
        this.mission = null;
    }

    public void switchIngameMenu() {
        popupVisible = !popupVisible;
        inputManager.setCursorVisible(popupVisible);
        cameraManager.disableCameraRotation(popupVisible);
        Element exitMenu = screen.findElementById("exitMenuPanel");
        exitMenu.setVisible(popupVisible);
    }

}
