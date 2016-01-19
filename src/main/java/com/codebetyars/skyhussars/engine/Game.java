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

import com.codebetyars.skyhussars.engine.plane.Plane;
import com.jme3.scene.Node;

public class Game extends GameState {

    private Plane playerPlane;
    private DataManager dataManager;
    private CameraManager cameraManager;
    private TerrainManager terrainManager;
    private GuiManager guiManager;
    private GameControls gameControls;
    private DayLightWeatherManager dayLightWeatherManager;

    public Game(DataManager dataManager, Controls controls,
            CameraManager cameraManager, TerrainManager terrainManager,
            GuiManager guiManager, Node node, GameControls commonControls, DayLightWeatherManager dayLightWeatherManager) {
        this.dataManager = dataManager;
        this.cameraManager = cameraManager;
        this.terrainManager = terrainManager;
        this.guiManager = guiManager;
        this.gameControls = commonControls;
        this.dayLightWeatherManager = dayLightWeatherManager;
        playerPlane = new Plane(dataManager);
        node.attachChild(playerPlane.getModel());
        node.attachChild(playerPlane.getEngineSound());
        initializeScene();
        controls.setUpControls(playerPlane);
    }

    private void initializeScene() {
        initiliazePlayer();
    }

    private void initiliazePlayer() {
        playerPlane.setLocation(0, 0);
        playerPlane.setHeight(3000);
        cameraManager.moveCameraTo(playerPlane.getLocation());
        cameraManager.followWithCamera(playerPlane.getModel());
    }

    public GameState update(float tpf) {
        if (gameControls.reset()) {
            gameControls.reset(false);
            initializeScene();
            gameControls.freezed(false);
        }
        if (!gameControls.paused()) {
            playerPlane.getEngineSound().play();
            playerPlane.update(tpf);
            cameraManager.followWithCamera(playerPlane.getModel());
            if (terrainManager.checkCollisionWithGround(playerPlane)) {
                gameControls.freezed(true);
            }
            guiManager.update(playerPlane.getSpeedKmH());
        } else {
            playerPlane.getEngineSound().pause();
        }
        return this;
    }

    @Override
    public void close() {
    }

    @Override
    public void initialize() {
        initializeScene();
        guiManager.switchScreen("main");
        guiManager.cursor(false);
        gameControls.paused(false);
    }
}
