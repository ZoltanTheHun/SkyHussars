package com.codebetyars.skyhussars.engine.gamestates;

import com.codebetyars.skyhussars.engine.DayLightWeatherManager;
import com.codebetyars.skyhussars.engine.Lighting;
import com.codebetyars.skyhussars.engine.data.PlaneRegistry;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainMenuControls implements ScreenController {

    @Autowired
    private DayLightWeatherManager dayLightWeatherManager;

    @Autowired
    private PlaneRegistry planeRegistry;

    @Autowired
    private MainMenu mainMenu;

    @Autowired
    private Lighting lighting;

    private final static Logger logger = LoggerFactory.getLogger(MainMenuControls.class);

    @NiftyEventSubscriber(id = "timeControl")
    public void setTime(final String id, final DropDownSelectionChangedEvent event) {
        String time = (String) event.getSelection();
        SimpleDateFormat dateformat = new SimpleDateFormat("HH");
        int hour;
        if ("Now".equals(time)) {
            hour = Integer.parseInt(dateformat.format(new Date()));

        } else {
            hour = Integer.parseInt(time.split(":")[0]);
        }
        logger.debug("Set time of flight:" + hour);
        dayLightWeatherManager.setHour(hour);
    }

    @NiftyEventSubscriber(id = "planeSelect")
    public void setPlane(final String id, final DropDownSelectionChangedEvent event) {
        String planeName = (String) event.getSelection();
        mainMenu.setPlaneType(planeName);
    }

    @NiftyEventSubscriber(id = "enemyCount")
    public void setEnemyCount(final String id, final DropDownSelectionChangedEvent event) {
        String enemyCount = (String) event.getSelection();
        mainMenu.setEnemyCount(Integer.parseInt(enemyCount));
    }

    @NiftyEventSubscriber(id = "waterEnabled")
    public void setWaterEnabled(final String id, final DropDownSelectionChangedEvent event) {
        String disableWater = (String) event.getSelection();
        lighting.waterEnabled(!"YES".equals(disableWater));
    }

    public void init(Nifty nifty) {
        populateTimeControl(nifty);
        populatePlaneSelect(nifty);
        populateEnemyCount(nifty);
        populateWaterEnabled(nifty);
    }

    private void populateWaterEnabled(Nifty nifty) {
        DropDown<String> waterDisabled = nifty.getScreen("start").findNiftyControl("waterEnabled", DropDown.class);
        waterDisabled.addItem("NO");
        waterDisabled.addItem("YES");
        waterDisabled.selectItemByIndex(0);
    }

    private void populateTimeControl(Nifty nifty) {
        DropDown<String> timeControl = nifty.getScreen("start").findNiftyControl("timeControl", DropDown.class);
        timeControl.addItem("Now");
        for (int i = 0; i < 24; i++) {
            timeControl.addItem((i < 10 ? "0" + i : i) + ":00");
        }
        timeControl.selectItemByIndex(0);
    }

    private void populatePlaneSelect(Nifty nifty) {
        DropDown<String> planeSelect = nifty.getScreen("start").findNiftyControl("planeSelect", DropDown.class);
        planeRegistry.availablePlanes().forEach(planeName
                -> planeSelect.addItem(planeName));
        planeSelect.selectItemByIndex(0);
    }

    private void populateEnemyCount(Nifty nifty) {
        DropDown<String> enemyCount = nifty.getScreen("start").findNiftyControl("enemyCount", DropDown.class);
        for (int i = 0; i < 65; i++) {
            enemyCount.addItem("" + i);
        }
        enemyCount.selectItemByIndex(0);
    }

    public void startGame() {
        mainMenu.startGame();
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }
}
