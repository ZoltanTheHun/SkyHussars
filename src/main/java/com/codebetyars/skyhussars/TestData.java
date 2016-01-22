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
package com.codebetyars.skyhussars;

import com.codebetyars.skyhussars.engine.data.Armament;
import com.codebetyars.skyhussars.engine.data.ArmamentGroup;
import com.codebetyars.skyhussars.engine.data.Engine;
import com.codebetyars.skyhussars.engine.data.Gun;
import com.codebetyars.skyhussars.engine.data.Plane;
import java.util.ArrayList;
import java.util.List;

public class TestData {

    private Engine engine1;
    private Engine engine2;
    private Plane plane1;
    private Plane plane2;
    private Gun gun;

    public TestData() {
        gun = new Gun();
        gun.setName(".50 M3 Browning");
        ArmamentGroup gunGroup = new ArmamentGroup();
        List<Armament> armaments = new ArrayList<>();
        armaments.add(gun);
        armaments.add(gun);
        armaments.add(gun);
        armaments.add(gun);
        armaments.add(gun);
        armaments.add(gun);
        List<ArmamentGroup> armamentGroups = new ArrayList<>();
        gunGroup.setName("6x .50 M3 Browning");
        gunGroup.setArmaments(armaments);
        engine1 = new Engine();
        engine1.setName("Allison J33-A-9");
        engine1.setThrustMax(17125);
        engine2.setName("Allison J33-A-17");
        engine2.setThrustMax(17792);
        plane1 = new Plane();
        plane2 = new Plane();
        plane1.setName("Lockheed P-80A-1-LO Shooting Star");
        plane1.setEngine(engine1);
        plane1.setArmamentGroups(armamentGroups);
        plane2.setName("Lockheed P-80A-5-LO Shooting Star");
        plane2.setEngine(engine2);
        plane2.setArmamentGroups(armamentGroups);

    }
}
