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

import com.codebetyars.skyhussars.engine.controls.ControlsMapper;
import com.codebetyars.skyhussars.engine.controls.ControlsManager;
import com.codebetyars.skyhussars.engine.plane.Plane;
import com.jme3.scene.Node;

public class Mission extends GameState {

    private Pilot player;
    private Plane activePlane;
    private DataManager dataManager;
    private CameraManager cameraManager;
    private TerrainManager terrainManager;
    private GuiManager guiManager;
    private DayLightWeatherManager dayLightWeatherManager;
    private ControlsManager controlsManager;
    private boolean paused = false;
    private boolean ended = false;

    public Mission(DataManager dataManager,
            CameraManager cameraManager, TerrainManager terrainManager,
            GuiManager guiManager, Node node, DayLightWeatherManager dayLightWeatherManager, ControlsMapper controlsMapper) {
        this.dataManager = dataManager;
        this.cameraManager = cameraManager;
        this.terrainManager = terrainManager;
        this.guiManager = guiManager;
        this.dayLightWeatherManager = dayLightWeatherManager;
        activePlane = new Plane(dataManager);
        player = new Pilot(activePlane);
        /*not finished object creation?*/
        this.controlsManager = new ControlsManager(controlsMapper, player, this);
        node.attachChild(activePlane.getModel());
        node.attachChild(activePlane.getEngineSound());
        initiliazePlayer();
    }

    /**
     * This method is used to initialize a scene
     */
    public void initializeScene() {
        initiliazePlayer();
        ended = false;
    }

    private void initiliazePlayer() {
        activePlane.setLocation(0, 0);
        activePlane.setHeight(3000);
        cameraManager.moveCameraTo(activePlane.getLocation());
        cameraManager.followWithCamera(activePlane.getModel());
    }

    @Override
    public GameState update(float tpf) {
        if (!paused && !ended) {
            activePlane.getEngineSound().play();
            activePlane.update(tpf);
            cameraManager.followWithCamera(activePlane.getModel());
            if (terrainManager.checkCollisionWithGround(activePlane)) {
                ended = true;
            }
            guiManager.update(activePlane.getSpeedKmH());
        } else {
            activePlane.getEngineSound().pause();
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
        ended = false;
    }

    public void paused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }
}
