package com.codebetyars.skyhussars.engine;

import com.codebetyars.skyhussars.engine.mission.MissionDescriptor;
import com.codebetyars.skyhussars.engine.plane.PlaneDescriptor;

public interface DataModel {

    PlaneDescriptor getPlaneDescriptor(String planeName);

    MissionDescriptor getMissionDescriptor(String name);

}
