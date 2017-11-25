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

import com.jme3.math.Vector3f;
import skyhussars.engine.Sky;
import skyhussars.engine.DayLightWeatherManager;
import skyhussars.engine.Pilot;
import skyhussars.engine.terrain.TerrainManager;
import skyhussars.engine.camera.CameraManager;
import skyhussars.engine.sound.SoundManager;
import skyhussars.engine.plane.Plane;
import skyhussars.engine.weapons.ProjectileManager;
import com.jme3.scene.Node;
import de.lessvoid.nifty.elements.render.TextRenderer;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import skyhussars.engine.physics.PlaneResponse;
import static skyhussars.utility.Streams.list;
import static skyhussars.utility.Streams.pf;
import static skyhussars.utility.Streams.pp;

public class MissionState implements GameState {

    private final Pilot player;
    private final CameraManager cameraManager;
    private final TerrainManager terrainManager;
    private final DayLightWeatherManager dayLightWeatherManager;
    private final ProjectileManager projectileManager;
    private boolean paused = false;
    private boolean ended = false;
    private final List<Plane> planes;
    private List<Pilot> pilots;
    private final SoundManager soundManager;
    private final WorldThread worldThread;
    private Timer timer;
    private GameState nextState = this;
    private final Node rootNode;
    private final Sky sky;

    public MissionState(List<Plane> planes, ProjectileManager projectileManager, SoundManager soundManager,
            CameraManager cameraManager, TerrainManager terrainManager,
            DayLightWeatherManager dayLightWeatherManager, Node rootNode, Sky sky) {
        this.rootNode = rootNode;
        this.sky = sky;
        this.planes = planes;
        this.projectileManager = projectileManager;
        this.cameraManager = cameraManager;
        this.terrainManager = terrainManager;
        this.dayLightWeatherManager = dayLightWeatherManager;
        this.soundManager = soundManager;
        Plane pilotedPlane = pf(planes,plane -> plane.planeMissionDescriptor().player()).findFirst().get();
        player = new Pilot(pilotedPlane);
        
        initializeScene();
        worldThread = new WorldThread(planes, ticks, terrainManager,projectileManager);
    }
    private final int ticks = 30;

    public Pilot player() { return player; }

    /**
     * This method is used to initialize a scene
     */
    private void initializeScene() { initPlayer();}

    private void keepWorldThreadRunning() {
        if (timer == null) {
            timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override public void run() { worldThread.run(); }
            }, 0, 1000 / ticks);  // 16 = 60 tick, 50 = 20 tick
        }
    }

    private void stopWorldThread() {
        if (timer != null) { timer.cancel(); timer = null; }
    }

    private TextRenderer speedoMeterUI;
    private TextRenderer altimeterUI;
    private TextRenderer aoaUI;

    public synchronized void speedoMeterUI(TextRenderer speedoMeterUI) { this.speedoMeterUI = speedoMeterUI;}
    public synchronized void altimeterUI(TextRenderer altimeterUI) { this.altimeterUI = altimeterUI;}
    public synchronized void aoaUI(TextRenderer aoaUI){ this.aoaUI = aoaUI;}
    
    @Override
    public synchronized GameState update(float tpf) {
        if(nextState == null) initialize();
        if (!paused && !ended) run(); else stop();
        auxiliaryUpdates(tpf);
        if(nextState != this) close();
        return nextState;
    }
    
    private void auxiliaryUpdates(float tpf){
        soundManager.update();
        cameraManager.update(tpf);
    }
    
    private void run(){
        keepWorldThreadRunning();
        worldThread.updateView();
        updatePlanes();
        if (player.plane().crashed()) {
            ended = true;
        }
        /* take another look at it later to get rid of a chance of a null reference */
        if (speedoMeterUI != null && altimeterUI != null) {
            speedoMeterUI.setText(player.plane().velocityKmh() + "km/h");
            altimeterUI.setText((player.plane().getHeight() + "m"));
            aoaUI.setText("Aoa: "+(player.plane().aoa()));
        }
    }
    
    private void stop(){
        stopWorldThread();
        soundManager.muteAllSounds();
    }

    private void updatePlanes() {
        planes.forEach(plane -> {
            //plane.update(tpf);
            if (terrainManager.checkCollisionWithGround(plane)) plane.crashed(true);
            plane.updateSound();
            projectileManager.checkCollision(plane);
        });
    }

    private void close() {
        if (timer != null) {
            timer.cancel();
        }
        sky.disableSky();
        soundManager.muteAllSounds();
        soundManager.update();
        rootNode.detachAllChildren();
        rootNode.getLocalLightList().clear();
        rootNode.getWorldLightList().clear();
        /*rootNode.forceRefresh(true, true, true);*/
    }

    private void initialize() {
        initializeScene();
        ended = false;
    }
    public synchronized void switchState(GameState state) {nextState = state;}
    public void paused(boolean paused) {this.paused = paused;}
    public boolean paused() {return paused;}

    public void initPlayer() {
        float kmh = 300f;
        PlaneResponse pr  =  new PlaneResponse();
        Vector3f velocity = pr.forwardNorm().mult(kmh / 3.6f);
        pr = pr.velocity(velocity);
        Plane plane = player.plane().planeResponse(pr)
                                    .setLocation(0, 0)
                                    .setHeight(3000)
                                    .crashed(false);
        ended = false;
        cameraManager.disableCameraRotation(false);
        cameraManager.moveCameraTo(plane.getLocation());
        cameraManager.followWithCamera(plane.planeGeometry());
    }

}
