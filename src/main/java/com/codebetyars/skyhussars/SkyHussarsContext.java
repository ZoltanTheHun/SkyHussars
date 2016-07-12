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
import com.codebetyars.skyhussars.engine.ComplexCamera;
import com.codebetyars.skyhussars.engine.gamestates.GameState;
import com.codebetyars.skyhussars.engine.GuiManager;
import com.codebetyars.skyhussars.engine.gamestates.MainMenu;
import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import jme3utilities.sky.SkyControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Configuration
@ComponentScan
public class SkyHussarsContext {

    @Autowired
    private AssetManager assetManager;

    @Autowired
    private Node rootNode;

    @Autowired
    private ComplexCamera camera;
    
    @Autowired
    private CameraManager cameraManager;
    
    @Autowired
    private GuiManager guiManager;

    @Autowired
    private MainMenu mainMenu;

    private GameState gameState;

    @Bean
    public SkyControl skyControl() {
        Calendar now = new GregorianCalendar();
        SkyControl skyControl = new SkyControl(assetManager, camera.testCamera(), 0.9f, true, true);
        skyControl.getSunAndStars().setHour(now.get(Calendar.HOUR_OF_DAY));
        skyControl.getSunAndStars().setSolarLongitude(now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        skyControl.getSunAndStars().setObserverLatitude(37.4046f * FastMath.DEG_TO_RAD);
        skyControl.setCloudiness(0f);
        rootNode/*camera.skyNode()*/.addControl(skyControl);
        skyControl.setEnabled(true);
        return skyControl;
    }

    public void simpleInitApp() {
        cameraManager.init();
        guiManager.createGUI();
        mainMenu.initialize();
        gameState = mainMenu;
    }

    public void simpleUpdate(float tpf) {
        GameState nextState = gameState.update(tpf);
        if (nextState != gameState) {
            gameState.close();
            gameState = nextState;
            gameState.initialize();
        }
        /* This is needed to make sure the node is updated by rendering*/
        rootNode.updateLogicalState(tpf);
        rootNode.updateGeometricState();
    }

    public void simpleRender(RenderManager rm) {
    }

}
