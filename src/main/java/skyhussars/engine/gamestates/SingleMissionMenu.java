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

import skyhussars.engine.DayLightWeatherManager;
import skyhussars.engine.data.PlaneRegistry;
import skyhussars.engine.mission.MissionFactory;
import com.jme3.input.InputManager;
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

    private final static Logger logger = LoggerFactory.getLogger(SingleMissionMenu.class);

    @Autowired
    private InputManager inputManager;

    @Autowired
    private MenuState menu;

    @Autowired
    private PlaneRegistry planeRegistry;

    @Autowired
    private MissionFactory missionFactory;

    private Nifty nifty;
    private Screen screen;

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
        planeRegistry.availablePlanes().forEach(planeName -> planeSelect.addItem(planeName));
        planeSelect.selectItemByIndex(0);
    }

    private void populateEnemyCount() {
        DropDown<String> enemyCountBtn = screen.findNiftyControl("enemyCount", DropDown.class);
        enemyCountBtn.clear();
        for (int i = 0; i < 251; i++) {
            enemyCountBtn.addItem("" + i);
        }
        enemyCountBtn.selectItemByIndex(0);
    }

    @Override
    public void onStartScreen() {
        populateTimeControl();
        populatePlaneSelect();
        populateEnemyCount();
        inputManager.setCursorVisible(true);
    }

    @Override
    public void onEndScreen() {
    }

    public void goBackToMainMenu() {
        nifty.gotoScreen("start");
    }

    public void startGame() {
        menu.startMission(missionFactory.mission(planeType, enemyCount));
        nifty.gotoScreen("main");
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
        planeType = (String) event.getSelection();
    }

    private int enemyCount = 0;
    private String planeType;

    @NiftyEventSubscriber(id = "enemyCount")
    public void setEnemyCount(final String id, final DropDownSelectionChangedEvent event) {
        enemyCount = Integer.parseInt((String) event.getSelection());
    }
    
    @NiftyEventSubscriber(id = "theatre")
    public void setTheatre(final String id, final DropDownSelectionChangedEvent event) {
    }
}
