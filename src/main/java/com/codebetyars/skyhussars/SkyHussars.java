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
import com.codebetyars.skyhussars.engine.controls.FlowControls;
import com.codebetyars.skyhussars.engine.controls.ControlsMapper;
import com.codebetyars.skyhussars.engine.DataManager;
import com.codebetyars.skyhussars.engine.DayLightWeatherManager;
import com.codebetyars.skyhussars.engine.Game;
import com.codebetyars.skyhussars.engine.GameState;
import com.codebetyars.skyhussars.engine.GuiManager;
import com.codebetyars.skyhussars.engine.MainMenu;
import com.codebetyars.skyhussars.engine.TerrainManager;
import com.codebetyars.skyhussars.engine.data.Engine;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import java.io.File;

public class SkyHussars extends SimpleApplication {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        Engine obj = new Engine();
        obj.setName("TestEngine");

        //Object to JSON in file
        try {
            mapper.writeValue(new File("./file.json"), obj);

            //Object to JSON in String
            String jsonInString = mapper.writeValueAsString(obj);
            System.out.println("results: " + jsonInString);
        } catch (Exception ex) {
            System.out.println("scheise");
        }
        SkyHussars app = new SkyHussars();
        AppSettings settings = new AppSettings(false);
        settings.setTitle("SkyHussars");
        settings.setSettingsDialogImage("Textures/settings_image.jpg");
        app.setSettings(settings);
        app.start();
    }
    private TerrainManager terrainManager;
    private DayLightWeatherManager dayLightWeatherManager;
    private ControlsMapper controlsMapper;
    private CameraManager cameraManager;
    private GuiManager guiManager;
    private GameState currentState;
    private Game game;
    private MainMenu mainMenu;

    @Override
    public void simpleInitApp() {
        instantiateResources();
        initGame();
    }

    private void instantiateResources() {
        DataManager dataManager = new DataManager(assetManager);
        controlsMapper = new ControlsMapper(inputManager);
        cameraManager = new CameraManager(this.cam, flyCam);
        guiManager = new GuiManager(assetManager, inputManager,
                audioRenderer, guiViewPort, "Interface/BasicGUI.xml", cameraManager);
        terrainManager = new TerrainManager(assetManager, getCamera());
        rootNode.attachChild(terrainManager.getTerrain());
        cameraManager.initializeCamera();
        dayLightWeatherManager = new DayLightWeatherManager(assetManager, cam, rootNode);

        game = new Game(dataManager, cameraManager, terrainManager,
                guiManager, rootNode, dayLightWeatherManager, controlsMapper);
        mainMenu = new MainMenu(guiManager, game);
    }

    private void initGame() {
        this.setDisplayStatView(false);
        //actually this should go to a builder method together with constructor
        guiManager.createGUI();
        currentState = mainMenu;
        currentState.initialize();
    }

    @Override
    public void simpleUpdate(float tpf) {
        GameState nextState = currentState.update(tpf);
        if (nextState != currentState) {
            currentState.close();
            currentState = nextState;
            currentState.initialize();
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
