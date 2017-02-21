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

import com.codebetyars.skyhussars.engine.plane.AirfoilDescriptor;
import com.codebetyars.skyhussars.engine.plane.EngineDescriptor;
import com.codebetyars.skyhussars.engine.plane.EngineLocation;
import com.codebetyars.skyhussars.engine.plane.GunDescriptor;
import com.codebetyars.skyhussars.engine.plane.GunGroupDescriptor;
import com.codebetyars.skyhussars.engine.plane.GunLocationDescriptor;
import com.codebetyars.skyhussars.engine.plane.PlaneDescriptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jme3.math.Vector3f;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TempDataGenerator {

    private Map<String, PlaneDescriptor> planes = new HashMap<>();
    private Map<String, EngineDescriptor> engines = new HashMap<>();

    public void createPlaneAt(String folder) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
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
        gun.setMuzzleVelocity(890);
        gun.setSpread(0.5f);
        List<GunLocationDescriptor> guns = new ArrayList<>();
        GunLocationDescriptor gun1 = new GunLocationDescriptor();
        gun1.setGunDescriptor(gun);
        gun1.setRoundsMax(300);
        gun1.setLocation(new Vector3f(0.5f, 0.0f, 2.0f));
        guns.add(gun1);
        GunLocationDescriptor gun2 = new GunLocationDescriptor();
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

        List<AirfoilDescriptor> airfoilDescriptors = new LinkedList<>();

        /*
         private float wingArea = 22.07f; //m2
         private float aspectRatio = 6.37f;
         */
        /*SymmetricAirfoil leftWing = new SymmetricAirfoil("WingA", new Vector3f(-2.0f, 0, -0.2f), wingArea / 2, 1f, aspectRatio, true, 0f);*/
        AirfoilDescriptor airfoilDescriptor = new AirfoilDescriptor();
        airfoilDescriptor.setName("WingLeft");
        airfoilDescriptor.setCog(new Vector3f(-2.0f, 0, -0.2f));
        airfoilDescriptor.setWingArea(22.07f / 4f);
        airfoilDescriptor.setIncidence(1f);
        airfoilDescriptor.setAspectRatio(6.37f);
        airfoilDescriptor.setDamper(true);
        airfoilDescriptor.setDehidralDegree(0f);
        airfoilDescriptors.add(airfoilDescriptor);

        airfoilDescriptor = new AirfoilDescriptor();
        airfoilDescriptor.setName("WingOuterLeft");
        airfoilDescriptor.setCog(new Vector3f(-4.0f, 0, -0.2f));
        airfoilDescriptor.setWingArea(22.07f / 4f);
        airfoilDescriptor.setIncidence(1f);
        airfoilDescriptor.setAspectRatio(6.37f);
        airfoilDescriptor.setDamper(true);
        airfoilDescriptor.setDehidralDegree(0f);
        airfoilDescriptors.add(airfoilDescriptor);
        /*SymmetricAirfoil rightWing = new SymmetricAirfoil("WingB", new Vector3f(2.0f, 0, -0.2f), wingArea / 2, 1f, aspectRatio, true, 0f);*/
        airfoilDescriptor = new AirfoilDescriptor();
        airfoilDescriptor.setName("WingRight");
        airfoilDescriptor.setCog(new Vector3f(2.0f, 0, -0.2f));
        airfoilDescriptor.setWingArea(22.07f / 4f);
        airfoilDescriptor.setAspectRatio(6.37f);
        airfoilDescriptor.setIncidence(1f);
        airfoilDescriptor.setDamper(true);
        airfoilDescriptor.setDehidralDegree(0f);
        airfoilDescriptors.add(airfoilDescriptor);
        airfoilDescriptor = new AirfoilDescriptor();
        airfoilDescriptor.setName("WingOuterRight");
        airfoilDescriptor.setCog(new Vector3f(4.0f, 0, -0.2f));
        airfoilDescriptor.setWingArea(22.07f / 4f);
        airfoilDescriptor.setAspectRatio(6.37f);
        airfoilDescriptor.setIncidence(1f);
        airfoilDescriptor.setDamper(true);
        airfoilDescriptor.setDehidralDegree(0f);
        airfoilDescriptors.add(airfoilDescriptor);
        /*SymmetricAirfoil horizontalStabilizer = new SymmetricAirfoil("HorizontalStabilizer", new Vector3f(0, 0, -6.0f), 5f, -3f, aspectRatio / 1.5f, false, 0f);*/
        airfoilDescriptor = new AirfoilDescriptor();
        airfoilDescriptor.setName("HorizontalStabilizer");
        airfoilDescriptor.setCog(new Vector3f(0, 0, -6f));
        airfoilDescriptor.setWingArea(5f);
        airfoilDescriptor.setIncidence(-3f);
        airfoilDescriptor.setAspectRatio(6.37f / 1.5f);
        airfoilDescriptor.setDamper(false);
        airfoilDescriptor.setDehidralDegree(0f);
        airfoilDescriptors.add(airfoilDescriptor);
        /*      private SymmetricAirfoil verticalStabilizer = new SymmetricAirfoil("VerticalStabilizer", new Vector3f(0, 0, -6.0f), 5.0f, 0f, aspectRatio / 1.5f, false, 90f);*/
        airfoilDescriptor = new AirfoilDescriptor();
        airfoilDescriptor.setName("VerticalStabilizer");
        airfoilDescriptor.setCog(new Vector3f(0, 0, -6f));
        airfoilDescriptor.setWingArea(5f);
        airfoilDescriptor.setIncidence(0f);
        airfoilDescriptor.setAspectRatio(6.37f / 1.5f);
        airfoilDescriptor.setDamper(false);
        airfoilDescriptor.setDehidralDegree(90f);
        airfoilDescriptors.add(airfoilDescriptor);

        PlaneDescriptor planeDescriptor = new PlaneDescriptor();
        planeDescriptor.setName("Lockheed P-80A-1-LO Shooting Star");
        planeDescriptor.setEngineLocations(engineLocations);
        planeDescriptor.setMassEmpty(3593);
        planeDescriptor.setMassGross(5307);
        planeDescriptor.setMassTakeOffMax(6350);
        planeDescriptor.setInternalTank(1609);
        planeDescriptor.setGunGroupDescriptors(gunGroups);
        planeDescriptor.setAirfolDescriptors(airfoilDescriptors);
        planes.put(planeDescriptor.getName(), planeDescriptor);

        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(folder + "/p80a1lo.json"), planeDescriptor);
        System.out.println(mapper.writeValueAsString(planeDescriptor));

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
        planeDescriptor.setGunGroupDescriptors(gunGroups);
        planeDescriptor.setAirfolDescriptors(airfoilDescriptors);
        planes.put(planeDescriptor.getName(), planeDescriptor);

        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(folder + "/p80a5lo.json"), planeDescriptor);
        System.out.println(mapper.writeValueAsString(planeDescriptor));

    }
}
