package com.codebetyars.skyhussars.engine;

import com.codebetyars.skyhussars.engine.mission.MissionDescriptor;

public interface DataModel {

    MissionDescriptor getMissionDescriptor(String name);

}
