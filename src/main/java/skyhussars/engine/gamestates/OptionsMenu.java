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
package skyhussars.engine.gamestates;

import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OptionsMenu implements ScreenController {

    private Nifty nifty;
    private Screen screen;

    @Autowired
    private Options options;

    @Autowired
    private OptionsManager optionsManager;

    @Autowired
    private InputManager inputManager;

    private  Joystick[] joysticks;
    private  boolean hasJoysticks;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        joysticks = inputManager.getJoysticks();
        hasJoysticks = joysticks != null && joysticks.length > 0;
        if (hasJoysticks) {
            DropDown<String> joystickElement = screen.findNiftyControl("joystickControl", DropDown.class);
            int activeElement = 0; //we will default to first element if it is not set in options
            for (int i = 0; i < joysticks.length; i++) {
                joystickElement.addItem(joysticks[i].getName());
                if (options.getJoyId().orElse(-1) == joysticks[i].getJoyId()) {
                    activeElement = i;
                }
            }
            joystickElement.selectItemByIndex(activeElement);
        }
    }

    @Override
    public void onEndScreen() {
    }

    public void cancel() {
        nifty.gotoScreen("start");
    }

    public void accept() {
        setJoystick();
        nifty.gotoScreen("start");
    }

    private void setJoystick() {
        if (hasJoysticks) {
            DropDown<String> joystickElement = screen.findNiftyControl("joystickControl", DropDown.class);
            options.setJoyId(Optional.of(joysticks[joystickElement.getSelectedIndex()].getJoyId()));
            optionsManager.persistOptions(options);
        }
    }
}