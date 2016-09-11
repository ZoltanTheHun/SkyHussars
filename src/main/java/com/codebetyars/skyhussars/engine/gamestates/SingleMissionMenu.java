package com.codebetyars.skyhussars.engine.gamestates;

import com.codebetyars.skyhussars.engine.DayLightWeatherManager;
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
public class SingleMissionMenu implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private final static Logger logger = LoggerFactory.getLogger(SingleMissionMenu.class);

    @Autowired
    private MenuState menu;

    @Autowired
    private PlaneRegistry planeRegistry;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    private void populateTimeControl() {
        DropDown<String> timeControl = screen.findNiftyControl("timeControl", DropDown.class);
        timeControl.clear();
        timeControl.addItem("Now");
        for (int i = 0; i < 24; i++) {
            timeControl.addItem((i < 10 ? "0" + i : i) + ":00");
        }
        timeControl.selectItemByIndex(0);
    }

    private void populatePlaneSelect() {
        DropDown<String> planeSelect = screen.findNiftyControl("planeSelect", DropDown.class);
        planeSelect.clear();
        planeRegistry.availablePlanes().forEach(planeName
                -> planeSelect.addItem(planeName));
        planeSelect.selectItemByIndex(0);
    }

    private void populateEnemyCount() {
        DropDown<String> enemyCount = screen.findNiftyControl("enemyCount", DropDown.class);
        enemyCount.clear();
        for (int i = 0; i < 251; i++) {
            enemyCount.addItem("" + i);
        }
        enemyCount.selectItemByIndex(0);
    }

    @Override
    public void onStartScreen() {
        populateTimeControl();
        populatePlaneSelect();
        populateEnemyCount();
    }

    @Override
    public void onEndScreen() {
    }

    public void goBackToMainMenu() {
        nifty.gotoScreen("start");
    }
    
    public void startGame(){
        menu.startGame();
    }

    @Autowired
    private DayLightWeatherManager dayLightWeatherManager;

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
        menu.setPlaneType(planeName);
    }

    @NiftyEventSubscriber(id = "enemyCount")
    public void setEnemyCount(final String id, final DropDownSelectionChangedEvent event) {
        String enemyCount = (String) event.getSelection();
        menu.setEnemyCount(Integer.parseInt(enemyCount));
    }

}
