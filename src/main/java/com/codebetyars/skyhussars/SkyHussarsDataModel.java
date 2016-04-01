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

import com.codebetyars.skyhussars.engine.DataModel;
import com.codebetyars.skyhussars.engine.mission.MissionDescriptor;
import com.codebetyars.skyhussars.engine.mission.PlaneMissionDescriptor;
import com.jme3.math.Vector3f;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SkyHussarsDataModel implements DataModel, InitializingBean {

    private Map<String, MissionDescriptor> missions = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {

        PlaneMissionDescriptor planeMissionDescriptor = new PlaneMissionDescriptor();
        planeMissionDescriptor.player(true);
        planeMissionDescriptor.planeType("Lockheed P-80A-1-LO Shooting Star");
        planeMissionDescriptor.startLocation(new Vector3f(0, 3000, 0));

        PlaneMissionDescriptor planeMissionDescriptor2 = new PlaneMissionDescriptor();
        planeMissionDescriptor2.player(false);
        planeMissionDescriptor2.planeType("Lockheed P-80A-1-LO Shooting Star");
        planeMissionDescriptor2.startLocation(new Vector3f(0, 3000, 100));

        PlaneMissionDescriptor planeMissionDescriptor3 = new PlaneMissionDescriptor();
        planeMissionDescriptor3.player(false);
        planeMissionDescriptor3.planeType("Lockheed P-80A-1-LO Shooting Star");
        planeMissionDescriptor3.startLocation(new Vector3f(0, 3000, 1000));

        PlaneMissionDescriptor planeMissionDescriptor4 = new PlaneMissionDescriptor();
        planeMissionDescriptor4.player(false);
        planeMissionDescriptor4.planeType("Lockheed P-80A-1-LO Shooting Star");
        planeMissionDescriptor4.startLocation(new Vector3f(50, 2500, 100));

        PlaneMissionDescriptor planeMissionDescriptor5 = new PlaneMissionDescriptor();
        planeMissionDescriptor5.player(false);
        planeMissionDescriptor5.planeType("Lockheed P-80A-1-LO Shooting Star");
        planeMissionDescriptor5.startLocation(new Vector3f(-100, 3000, 100));

        PlaneMissionDescriptor planeMissionDescriptor6 = new PlaneMissionDescriptor();
        planeMissionDescriptor6.player(false);
        planeMissionDescriptor6.planeType("Lockheed P-80A-1-LO Shooting Star");
        planeMissionDescriptor6.startLocation(new Vector3f(100, 3000, 100));

        PlaneMissionDescriptor planeMissionDescriptor7 = new PlaneMissionDescriptor();
        planeMissionDescriptor7.player(false);
        planeMissionDescriptor7.planeType("Lockheed P-80A-1-LO Shooting Star");
        planeMissionDescriptor7.startLocation(new Vector3f(0, 3000, 5000));

        List<PlaneMissionDescriptor> planeMissionDescriptors = new ArrayList<>();
        planeMissionDescriptors.add(planeMissionDescriptor);
        planeMissionDescriptors.add(planeMissionDescriptor2);
        planeMissionDescriptors.add(planeMissionDescriptor3);
        planeMissionDescriptors.add(planeMissionDescriptor4);
        planeMissionDescriptors.add(planeMissionDescriptor5);
        planeMissionDescriptors.add(planeMissionDescriptor6);
        planeMissionDescriptors.add(planeMissionDescriptor7);

        MissionDescriptor missionDescriptor = new MissionDescriptor();
        missionDescriptor.name("Test mission");
        missionDescriptor.planeMissionDescriptors(planeMissionDescriptors);

        missions.put(missionDescriptor.name(), missionDescriptor);
    }

    public MissionDescriptor getMissionDescriptor(String name) {
        return missions.get(name);
    }
}
