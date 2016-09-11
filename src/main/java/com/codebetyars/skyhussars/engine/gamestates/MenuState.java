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

import com.codebetyars.skyhussars.engine.GuiManager;
import com.codebetyars.skyhussars.engine.mission.MissionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuState implements GameState {

    private enum Action {

        IDLE, START_MISSION, EXIT_GAME
    };

    private Action action = Action.IDLE;

    @Autowired
    private MissionFactory missionFactory;

    @Autowired
    private GuiManager guiManager;

    private int enemyCount = 0;

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public String getPlaneType() {
        return planeType;
    }

    public void setPlaneType(String planeType) {
        this.planeType = planeType;
    }

    private String planeType;

    @Override
    public GameState update(float tpf) {
        GameState nextState = null;
        switch (action) {
            case IDLE: 
                nextState = this;
                break;
            case START_MISSION:
                nextState = missionFactory.mission(planeType, enemyCount);
                action = Action.IDLE;
                break;
            case EXIT_GAME:
                nextState = null;
                action = Action.IDLE;    
                break;
        }
        return nextState;
    }

    @Override
    public void close() {
    }

    @Override
    public void initialize() {
        guiManager.cursor(true);
    }

    public void startGame() {
        action = Action.START_MISSION;
    }
    
    public void exitGame() {
        action = Action.EXIT_GAME;
    }

}
