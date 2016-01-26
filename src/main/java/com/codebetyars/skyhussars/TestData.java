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

import com.codebetyars.skyhussars.engine.plane.GunDescriptor;
import com.codebetyars.skyhussars.engine.plane.EngineDescriptor;
import com.codebetyars.skyhussars.engine.plane.EngineLocation;
import com.codebetyars.skyhussars.engine.plane.GunGroupDescriptor;
import com.codebetyars.skyhussars.engine.plane.GunLocation;
import com.codebetyars.skyhussars.engine.plane.PlaneDescriptor;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestData {

    private Map<String, PlaneDescriptor> planes = new HashMap<>();
    private Map<String, EngineDescriptor> engines = new HashMap<>();

    public TestData() {
        EngineDescriptor engine = new EngineDescriptor();
        engine.setName("Allison J33-A-9");
        engine.setThrustMax(17125);
        engines.put(engine.getName(), engine);
        engine = new EngineDescriptor();
        engine.setName("Allison J33-A-17");
        engine.setThrustMax(17792);
        engines.put(engine.getName(), engine);

        GunDescriptor gun = new GunDescriptor();
        gun.setName(".50 M3 Browning");
        gun.setRateOfFire(20);
        List<GunLocation> guns = new ArrayList<>();
        GunLocation gun1 = new GunLocation();
        gun1.setGunDescriptor(gun);
        gun1.setRoundsMax(300);
        gun1.setLocation(new Vector3f(0.5f, 0.0f, 2.0f));
        guns.add(gun1);
        GunLocation gun2 = new GunLocation();
        gun2.setGunDescriptor(gun);
        gun2.setRoundsMax(300);
        gun2.setLocation(new Vector3f(-0.5f, 0.0f, 2.0f));
        guns.add(gun2);
        GunGroupDescriptor gunGroup = new GunGroupDescriptor();
        gunGroup.setName("6x .50 M3 Browning");
        gunGroup.setGunLocations(guns);

        List<GunGroupDescriptor> gunGroups = new ArrayList<>();
        gunGroups.add(gunGroup);

        EngineLocation engineLocation = new EngineLocation();
        engineLocation.setEngineDescriptor(engines.get("Allison J33-A-9"));
        engineLocation.setLocation(new Vector3f(0f, 0f, 0f));
        List<EngineLocation> engineLocations = new LinkedList<>();
        engineLocations.add(engineLocation);
        PlaneDescriptor planeDescriptor = new PlaneDescriptor();
        planeDescriptor.setName("Lockheed P-80A-1-LO Shooting Star");
        planeDescriptor.setEngineLocations(engineLocations);
        planeDescriptor.setMassEmpty(3593);
        planeDescriptor.setMassGross(5307);
        planeDescriptor.setMassTakeOffMax(6350);
        planeDescriptor.setInternalTank(1609);
        planeDescriptor.setGunGroups(gunGroups);
        planes.put(planeDescriptor.getName(), planeDescriptor);


        engineLocation = new EngineLocation();
        engineLocation.setEngineDescriptor(engines.get("Allison J33-A-17"));
        engineLocation.setLocation(new Vector3f(0f, 0f, 0f));
        engineLocations = new LinkedList<>();
        engineLocations.add(engineLocation);
        planeDescriptor = new PlaneDescriptor();
        planeDescriptor.setName("Lockheed P-80A-5-LO Shooting Star");
        planeDescriptor.setEngineLocations(engineLocations);
        planeDescriptor.setMassEmpty(3593);
        planeDescriptor.setMassGross(5307);
        planeDescriptor.setMassTakeOffMax(6350);
        planeDescriptor.setInternalTank(1609);
        planeDescriptor.setGunGroups(gunGroups);
        planes.put(planeDescriptor.getName(), planeDescriptor);
    }

    public PlaneDescriptor getPlaneDescriptor(String planeName) {
        return planes.get(planeName);
    }
}
