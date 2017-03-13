/*
 * Copyright (c) 2017, ZoltanTheHun
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

import com.codebetyars.skyhussars.engine.Pilot;
import com.jme3.input.controls.AnalogListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FlightJoystickControls implements AnalogListener {

    private final static Logger logger = LoggerFactory.getLogger(FlightJoystickControls.class);

    private final Pilot pilot;

    public FlightJoystickControls(Pilot pilot) {
        this.pilot = pilot;
    }

    @Override
    public void onAnalog(String string, float axis, float tpf) {

        logger.info("Joy is at: {}, tpf: {}", axis, tpf);
        if (ControlsMapper.ROTATE_LEFT.equals(string)) {
            pilot.setAileron(-1 * axis / tpf);
        }
        if (ControlsMapper.ROTATE_RIGHT.equals(string)) {
            pilot.setAileron(axis / tpf);
        }
        if (ControlsMapper.PITCH_DOWN.equals(string)) {
            pilot.setElevator(-1 * axis / tpf);
        }
        if (ControlsMapper.PITCH_UP.equals(string)) {
            pilot.setElevator(axis / tpf);
        }

    }

}
