package skyhussars.engine;

import skyhussars.engine.mission.MissionDescriptor;

public interface DataModel {

    MissionDescriptor getMissionDescriptor(String name);
    MissionDescriptor getNewMission(String playerPlane, int enemyCount);
}
