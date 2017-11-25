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
package skyhussars.engine.ai;

import skyhussars.engine.World;
import skyhussars.engine.plane.Plane;
import com.jme3.math.Vector3f;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIPilot {

    private final static Logger logger = LoggerFactory.getLogger(AIPilot.class);

    private final Plane plane;
    private final float initialHeight;
    private Plane target;

    public AIPilot(Plane plane) {
        this.plane = plane;
        this.initialHeight = plane.getHeight();

    }

    public void update(World world) {
        plane.getLocation();
        plane.setThrottle(1);

        // first simple decision tree like AI.
        // 1. don't get too close to earth
        // 2. if enemy behind turn left
        // if enemy to the left-ahead turn left
        // if enemy to the right-ahead turn right
        // if above/below make corrections
        // how to combine moves?
        Optional<Plane> noticedPlane = world.lookAround();
        // should have some clever method chaining here?
        if (isCloseToGround(world)) {
            increaseHeight();
        } else if (noticedPlane.isPresent()) {
            navigateToDirection(findDirection(noticedPlane.get()));
        } else {
            levelFlight();
        }
    }

    public boolean isCloseToGround(World world) {
        return world.getStandardHeightFor(plane) < 300;
    }

    private Vector3f findDirection(Plane target) {
        return target.getLocation().subtract(plane.getLocation()).normalize();
    }

    private void navigateToDirection(Vector3f direction) {
        /**
         * TODO: revise direction code, this should not be negated
         */
        Vector3f flyingDir = plane.forward();
        Vector3f targetDir = direction;
        //the all too naive AI
        //if behind, try to turn back
        //let's determine forward plane, then dot product tells us if direction
        //is backwards
        logger.debug("flyingto:  {}, targeting: {}, dot: {}", flyingDir, targetDir, flyingDir.dot(targetDir));
        if (isBehind(flyingDir, targetDir)) {
            turnLeft();
        } else {
            levelFlight();
        }
        //if left ahead, turn that direction, else right
        //and change hight to accomodate
        //if(flyingDir.cross(Vector3f.UNIT_Y).dot(targetDir)<);

    }

    private boolean isBehind(Vector3f flyingDir, Vector3f targetDir) {
        return flyingDir.dot(targetDir) < 0;
    }

    private void turnLeft() {
        logger.debug("turn left when {}", plane.roll());
        if (plane.roll() > -45) {
            plane.setAileron(-1);
        } else if (plane.roll() < -60) {
            plane.setAileron(1);
        } else {
            plane.setAileron(0);
        }
        // pullInTurn();
    }

    public void pullInTurn() {
        if (plane.roll() > -45 && plane.roll() < - 60) {
            plane.setElevator(1f);
        }
    }

    private void increaseHeight() {
        rollUp();
        plane.setElevator(1f);
    }

    private void rollUp() {
        logger.debug("plane roll: {}", plane.roll());
        if (plane.roll() > 2f) {
            plane.setAileron(-1);
        } else if (plane.roll() < -2f) {
            plane.setAileron(1);
        } else {
            plane.setAileron(0);
        }
    }

    private void levelFlight() {
        rollUp();
        if (plane.getHeight() > initialHeight) {
            plane.setElevator(-1f);
            plane.setThrottle(0.5f);
        }
        if (plane.getHeight() < initialHeight) {
            plane.setElevator(1f);
            plane.setThrottle(1f);
        }
    }
}
