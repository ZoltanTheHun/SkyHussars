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
package com.codebetyars.skyhussars.engine.gamestates;

import com.codebetyars.skyhussars.engine.*;
import com.codebetyars.skyhussars.engine.plane.Plane;
import com.codebetyars.skyhussars.engine.weapons.ProjectileManager;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mission extends GameState {

    private Pilot player;
    private final CameraManager cameraManager;
    private final TerrainManager terrainManager;
    private final GuiManager guiManager;
    private final DayLightWeatherManager dayLightWeatherManager;
    private final ProjectileManager projectileManager;
    private boolean paused = false;
    private boolean ended = false;
    private final List<Plane> planes;
    private List<Pilot> pilots;
    private final SoundManager soundManager;
    private final static Logger logger = LoggerFactory.getLogger(Mission.class);

    public Mission(List<Plane> planes, ProjectileManager projectileManager, SoundManager soundManager,
            CameraManager cameraManager, TerrainManager terrainManager,
            GuiManager guiManager, DayLightWeatherManager dayLightWeatherManager) {

        this.planes = planes;
        this.projectileManager = projectileManager;
        this.cameraManager = cameraManager;
        this.terrainManager = terrainManager;
        this.guiManager = guiManager;
        this.dayLightWeatherManager = dayLightWeatherManager;
        this.soundManager = soundManager;
        for (Plane plane : planes) {
            if (plane.planeMissionDescriptor().player()) {
                player = new Pilot(plane);
            }
        }
        initiliazePlayer();
    }

    public Pilot player() {
        return player;
    }

    /**
     * This method is used to initialize a scene
     */
    public void initializeScene() {
        initiliazePlayer();
        ended = false;
    }

    private void initiliazePlayer() {
        Plane plane = player.plane();
        cameraManager.moveCameraTo(plane.getLocation());
        cameraManager.followWithCamera(plane.planeGeometry());
        cameraManager.init();
    }

    @Override
    public GameState update(float tpf) {
        long millis = System.currentTimeMillis();
        if (!paused && !ended) {
            planes.parallelStream().forEach(plane -> {
                plane.update(tpf);
                if (terrainManager.checkCollisionWithGround(plane)) {
                    plane.crashed(true);
                }

            });
            projectileManager.update(tpf);

            planes.forEach(plane -> {
                plane.updateSound();
                projectileManager.checkCollision(plane);
            });
            if (player.plane().crashed()) {
                ended = true;
            }
            guiManager.update(player.plane().getSpeedKmH());
        } else {
            soundManager.muteAllSounds();
        }
        cameraManager.update(tpf);
        millis = System.currentTimeMillis() - millis;
        logger.info("Gamestate update complete in " + millis);
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

    public boolean paused() {
        return paused;
    }

    public void reinitPlayer() {
        player.plane().setLocation(0, 0);
        player.plane().setHeight(3000);
        player.plane().crashed(false);
        initiliazePlayer();
        ended = false;
    }
}