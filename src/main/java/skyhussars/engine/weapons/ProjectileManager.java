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
package skyhussars.engine.weapons;

import skyhussars.engine.DataManager;
import skyhussars.engine.plane.Plane;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import static java.lang.Math.abs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static skyhussars.utility.Streams.create;
import static skyhussars.utility.Streams.pp;
import static skyhussars.utility.Streams.sp;

@Component
public class ProjectileManager {

    private final static Logger logger = LoggerFactory.getLogger(ProjectileManager.class);

    @Autowired
    private Node rootNode;

    @Autowired
    private DataManager dataManager;

    private List<Projectile> projectiles = new ArrayList<>();

    private final List<Geometry> projectileGeometries = new LinkedList<>();
    private final List<Geometry> newProjectiles = new LinkedList<>();

    public synchronized void addProjectiles(List<Bullet> bullets){
        sp(bullets,bullet -> this.addProjectile(bullet));
    }
    public synchronized void addProjectile(Bullet projectile) {
        projectiles.add(projectile);
    }
    
    public synchronized void updateGeoms(){
        int geometrySurplus = projectileGeometries.size() - projectiles.size();

        if(geometrySurplus < 0) { 
            //if we have more projectiles than geometries
            //add as much geometry as necessary
            List<Geometry> newGeoms = create(abs(geometrySurplus),() -> dataManager.getBullet());
            projectileGeometries.addAll(newGeoms);
            newGeoms.forEach(g -> rootNode.attachChild(g));
            
        } else if (geometrySurplus > 0){ 
            // if we have less projectiles than geometries
            // hide/remove the ones that are not needed
            int lastActiveInd = projectileGeometries.size() - geometrySurplus;
            List<Geometry> extraGeoms = new LinkedList<>(projectileGeometries.subList(lastActiveInd,  projectileGeometries.size()));
            extraGeoms.forEach(g ->{rootNode.detachChild(g);});
            projectileGeometries.removeAll(extraGeoms);
        }
        /*by this point both list should have same size*/
        Iterator<Geometry > geomIterator = projectileGeometries.iterator();
        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile projectile = it.next();
            Geometry geom = geomIterator.next();
            geom.setLocalTranslation(projectile.getLocation());
            Vector3f direction = projectile.getVelocity().normalize();
            geom.lookAt(direction, Vector3f.UNIT_Y);
        }
    }
    
    public synchronized void update(float tpf) {
        pp(projectiles,projectile -> projectile.update(tpf));
        projectiles.removeIf(p -> !p.isLive());
    }

    public synchronized void checkCollision(Plane plane) {
        Node planeNode = plane.planeGeometry().outside();
        projectileGeometries.forEach(projectile -> {
            CollisionResults collisionResults = new CollisionResults();
            if (projectile.collideWith(planeNode.getWorldBound(), collisionResults) > 0) {
                if (collisionResults.size() > 0) {
                    plane.hit();
                }
            }
        });
    }
}
