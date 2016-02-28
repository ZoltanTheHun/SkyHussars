package com.codebetyars.skyhussars;

import com.codebetyars.skyhussars.engine.CameraManager;
import com.codebetyars.skyhussars.engine.GameState;
import com.codebetyars.skyhussars.engine.GuiManager;
import com.codebetyars.skyhussars.engine.MainMenu;
import com.codebetyars.skyhussars.engine.mission.MissionFactory;
import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.renderer.Camera;
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
    private Camera camera;

    @Autowired
    private Node rootNode;

    @Autowired
    private CameraManager cameraManager;

    @Autowired
    private GuiManager guiManager;

    @Autowired
    private MainMenu mainMenu;

    @Autowired
    private MissionFactory missionFactory;

    private GameState gameState;

    @Bean
    public SkyControl skyControl() {
        Calendar now = new GregorianCalendar();
        SkyControl skyControl = new SkyControl(assetManager, camera, 0.9f, true, true);
        skyControl.getSunAndStars().setHour(now.get(Calendar.HOUR_OF_DAY));
        skyControl.getSunAndStars().setSolarLongitude(now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        skyControl.getSunAndStars().setObserverLatitude(37.4046f * FastMath.DEG_TO_RAD);
        skyControl.setCloudiness(0f);
        rootNode.addControl(skyControl);
        skyControl.setEnabled(true);
        return skyControl;
    }


    public void simpleInitApp() {
        cameraManager.initializeCamera();
        guiManager.createGUI();
        mainMenu.setPendingMission(missionFactory.mission("Test mission"));
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
    }

    public void simpleRender(RenderManager rm) {
    }
}
