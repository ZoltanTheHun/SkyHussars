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
package skyhussars.engine.gamestates;

import skyhussars.engine.terrain.TerrainManager;
import skyhussars.engine.World;
import skyhussars.engine.ai.AIPilot;
import skyhussars.engine.physics.environment.AtmosphereImpl;
import skyhussars.engine.physics.environment.Environment;
import skyhussars.engine.plane.Plane;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyhussars.engine.weapons.Bullet;
import skyhussars.engine.weapons.ProjectileManager;
import static skyhussars.utility.Streams.*;

public class WorldThread extends TimerTask {

    private final static Logger logger = LoggerFactory.getLogger(WorldThread.class);

    private final List<Plane> planes;
    private final float tick;

    private final Environment environment = new Environment(10, new AtmosphereImpl());
    private final List<AIPilot> aiPilots = new LinkedList<>();
    private final World world;
    private final ProjectileManager projectileManager;

    public WorldThread(List<Plane> planes, int tickrate, TerrainManager terrainManager, ProjectileManager projectileManager) {
        this.planes = planes;

        aiPilots.addAll(list(pf(planes,plane -> !plane.planeMissionDescriptor().player())
            .map( plane -> new AIPilot(plane))));
        
        world = new World(planes, terrainManager);
        tick = (float) 1 / (float) tickrate;
        this.projectileManager = projectileManager;
    }

    private final AtomicLong cycle = new AtomicLong(0);
    
    @Override
    public void run() {     
        pp(aiPilots,aiPilot -> aiPilot.update(world));
        List<Bullet> bullets = flatList(pm(planes,plane -> plane.tick(tick, environment)));
        projectileManager.addProjectiles(bullets);
        projectileManager.update(tick);
        cycle.incrementAndGet();
    }

    public long cycle() {return cycle.get();}
    public void updatePlaneLocations() {sp(planes,p -> p.update());}
}
