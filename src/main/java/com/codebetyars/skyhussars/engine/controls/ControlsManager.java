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
package com.codebetyars.skyhussars.engine.controls;

import com.codebetyars.skyhussars.engine.CameraManager;
import com.codebetyars.skyhussars.engine.mission.Mission;

public class ControlsManager {

    private FlightKeyboardControls inFlightKeyboardControls;
    private FlowControls gameFlowControls;
    private CameraControls cameraControls;

    public ControlsManager(ControlsMapper controlsMapper, Mission mission,CameraManager cameraManager) {
        inFlightKeyboardControls = new FlightKeyboardControls(mission.player());
        gameFlowControls = new FlowControls(mission);
        cameraControls = new CameraControls(cameraManager);
        controlsMapper.setupFlightKeyboardControls(inFlightKeyboardControls);
        controlsMapper.setupFlowControls(gameFlowControls);
        controlsMapper.setupCameraControls(cameraControls);
    }
    
}
