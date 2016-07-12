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
package com.codebetyars.skyhussars.engine.ai;

import com.codebetyars.skyhussars.engine.World;
import com.codebetyars.skyhussars.engine.plane.Plane;
import com.jme3.math.Vector3f;
import java.util.Optional;

public class AIPilot {

    private final Plane plane;
    private final float initialHeight;
    private Plane target;

    public AIPilot(Plane plane) {
        this.plane = plane;
        this.initialHeight = plane.getHeight();

    }

    public void update(World world) {
        plane.getLocation();
        /* Optional<Plane> noticedPlane = world.lookAround();
         Optional<Vector3f> plannedDirection = Optional.empty();

         if (noticedPlane.isPresent()) {
         navigateToDirection(Optional.of(findDirection(noticedPlane.get())));
         }*/
        turnLeft();

        if (plane.getHeight() > initialHeight) {
            plane.setElevator(-1f);
            plane.setThrottle(0.5f);
        }
        if (plane.getHeight() < initialHeight) {
            plane.setElevator(1f);
            plane.setThrottle(1f);
        }
    }

    private Vector3f findDirection(Plane target) {
        return plane.getLocation().subtract(target.getLocation());
    }

    private void navigateToDirection(Optional<Vector3f> direction) {
        if (direction.isPresent()) {
            Vector3f flyingDir = plane.getDirection().normalize();
            Vector3f targetDir = direction.get().normalize();
            //the all too naive AI
            //if behind, try to turn back
            //let's determine forward plane, then dot product tells us if direction
            //is backwards
            if (flyingDir.cross(Vector3f.UNIT_X).dot(targetDir) < 0) {
                turnLeft();
            } else {
                levelFlight();
            }
            //if left ahead, turn that direction, else right
            //and change hight to accomodate
            //if(flyingDir.cross(Vector3f.UNIT_Y).dot(targetDir)<);

        }
    }

    private void turnLeft() {
        if (plane.roll() < 0.610865) {
            plane.setAileron(-1);
        } else {
            plane.setAileron(0);
        }
    }

    private void levelFlight() {

    }
}
