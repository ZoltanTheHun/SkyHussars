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
package com.codebetyars.skyhussars;

import com.codebetyars.skyhussars.engine.CameraManager;
import com.codebetyars.skyhussars.engine.GameState;
import com.codebetyars.skyhussars.engine.GuiManager;
import com.codebetyars.skyhussars.engine.MainMenu;
import com.codebetyars.skyhussars.engine.mission.MissionFactory;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
public class SkyHussars extends SimpleApplication {

    // Start Spring application context and start the game
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SkyHussarsContext.class);
        context.getBean(SkyHussars.class).start();
    }

    @Autowired
    private MainMenu mainMenu;

    @Autowired
    private CameraManager cameraManager;

    @Autowired
    private GuiManager guiManager;

    @Autowired
    private MissionFactory missionFactory;

    @Override
    @Autowired
    public void setSettings(AppSettings settings) {
        super.setSettings(settings);
    }

    private GameState gameState = mainMenu;

    @Override
    public void simpleInitApp() {
        setDisplayStatView(false);
        cameraManager.initializeCamera();
        guiManager.createGUI();
        mainMenu.setPendingMission(missionFactory.mission("Test mission"));
        mainMenu.initialize();
    }

    @Override
    public void simpleUpdate(float tpf) {
        GameState nextState = gameState.update(tpf);
        if (nextState != gameState) {
            gameState.close();
            gameState = nextState;
            gameState.initialize();
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

}
