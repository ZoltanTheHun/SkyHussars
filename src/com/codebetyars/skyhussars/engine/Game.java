package com.codebetyars.skyhussars.engine;

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
