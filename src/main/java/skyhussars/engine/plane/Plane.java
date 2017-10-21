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
package skyhussars.engine.plane;

import skyhussars.engine.mission.PlaneMissionDescriptor;
import skyhussars.engine.physics.Aileron;
import skyhussars.engine.physics.Aileron.ControlDir;
import skyhussars.engine.physics.Airfoil;
import skyhussars.engine.physics.Engine;
import skyhussars.engine.physics.environment.Environment;
import skyhussars.engine.plane.instruments.Instruments;
import skyhussars.engine.sound.AudioHandler;
import com.jme3.bounding.BoundingVolume;
import com.jme3.effect.ParticleEmitter;
import static com.jme3.math.FastMath.RAD_TO_DEG;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import static java.util.Collections.*;
import java.util.List;
import java.util.stream.Collectors;
import skyhussars.engine.physics.PlanePhysics;
import skyhussars.engine.physics.PlaneResponse;
import skyhussars.engine.plane.instruments.AnalogueAirspeedIndicator;
import skyhussars.engine.weapons.Bullet;
import static skyhussars.utility.NumberFormats.*;
import static skyhussars.utility.Streams.*;

public class Plane {

    private final static Logger logger = LoggerFactory.getLogger(Plane.class);

    private PlaneMissionDescriptor planeMissionDescriptor;
    private String name;
    private final PlanePhysics physics;
    private final AudioHandler engineSound;
    private final AudioHandler gunSound;
    private final List<GunGroup> gunGroups;
    private final List<Engine> engines = new ArrayList<>();
    private boolean firing = false;
    private boolean crashed = false;
    private boolean shotdown = false;
    private ParticleEmitter fireEffect;
    private final PlaneGeometry geom;
    private PlaneResponse planeResponse = new PlaneResponse();
    private final AnalogueAirspeedIndicator airspeedIndicator;

    public List<Bullet> tick(float tick, Environment environment) {
        PlaneResponse localResponse = physics.update(tick, environment,planeResponse);
        synchronized(this){ planeResponse = localResponse;}
        return !crashed ? flatList(pm(gunGroups,gunGroup -> gunGroup.firing(firing, localResponse))) : emptyList();
    }

    public void planeMissinDescriptor(PlaneMissionDescriptor planeMissionDescriptor) {
        this.planeMissionDescriptor = planeMissionDescriptor;
    }

    public PlaneMissionDescriptor planeMissionDescriptor() {
        return planeMissionDescriptor;
    }

    private final List<Aileron> horizontalStabilizers = new ArrayList<>();
    private final List<Aileron> verticalStabilizers = new ArrayList<>();

    private final List<Airfoil> airfoils = new ArrayList<>();
    private final List<Aileron> ailerons = new ArrayList<>();
    
    private float velocityMs; // velocity in ms

    public Plane(List<Airfoil> airfoils,
            AudioHandler engineSound, AudioHandler gunSound,PlaneGeometry planeGeometry,
            Instruments instruments, List<Engine> engines, List<GunGroup> gunGroups
            , AnalogueAirspeedIndicator airspeedIndicator, PlanePhysics physics) {
        this.engineSound = engineSound;
        engineSound.audioNode().setLocalTranslation(0, 0, - 5);
        this.gunSound = gunSound;
        geom = planeGeometry;
        geom.attachSpatialToRootNode(engineSound.audioNode());
        geom.attachSpatialToRootNode(gunSound.audioNode());
        this.gunGroups = gunGroups;
        sortoutAirfoils(airfoils);
        this.physics = physics;
        float kmh = 300f;
        
        Vector3f velocity =  planeResponse.forwardNorm().mult(kmh / 3.6f);
        this.planeResponse = planeResponse.velocity(velocity);
        this.airspeedIndicator = airspeedIndicator;
    }
    
    private synchronized PlaneResponse planeResponse(){return planeResponse;}
    private synchronized Plane planeResponse(PlaneResponse pr){planeResponse = pr; return this;}
    
    private Plane sortoutAirfoils(List<Airfoil> airfoils){
        ailerons.addAll(airfoils.stream() 
                .filter(af -> af.direction().equals(ControlDir.LEFT) || af.direction().equals(ControlDir.RIGHT))
                .map(af -> (Aileron) af).collect(Collectors.toList()));
        horizontalStabilizers.addAll(airfoils.stream()
                .filter(af -> af.direction().equals(ControlDir.HORIZONTAL_STABILIZER))
                .map(af -> (Aileron) af)
                .collect(Collectors.toList()));
        verticalStabilizers.addAll(airfoils.stream()
                .filter(af -> af.direction().equals(ControlDir.VERTICAL_STABILIZER))
                .map(af -> (Aileron) af)
                .collect(Collectors.toList()));
        this.airfoils.addAll(airfoils);
        return this;
    }
    
    public float aoa(){ return planeResponse().aoa; }
    public BoundingVolume getHitBox() { return geom.root().getWorldBound(); }

    public void hit() {
        if (!shotdown) {
            geom.attachSpatialToRootNode(fireEffect);
            fireEffect.emitAllParticles();
            for (Engine engine : engines) {engine.damage(1.0f);}
        }
        shotdown = true;
    }

    public void update(float tpf) {
        PlaneResponse localResponse;
        synchronized(this){ localResponse = planeResponse; }
        velocityMs = localResponse.velocityMs();
        geom.translation(localResponse.translation)
            .rotation(localResponse.rotation);
        airspeedIndicator.update(localResponse);
        geom.update();
    }

    public synchronized void updateSound() {
        if (!crashed) {
            engineSound.play();
            if (firing)gunSound.play(); else  gunSound.stop();
        } else {
            engineSound.pause();
            gunSound.stop();
        }
    }

    /**
     * This method provides throttle controls for the airplane
     *
     * @param throttle - Amount of throttle applied, should be between 0.0f and
     * 1.0f
     */
    public void setThrottle(float throttle) {
        /* maybe it would be better to normalize instead of throwing an exception*/
        if (throttle < 0.0f || throttle > 1.0f) throw new IllegalArgumentException();
        pp(engines,e -> e.setThrottle(throttle));
        engineSound.setPitch(0.5f + throttle);
    }

    public void setAileron(float aileron) {
        ailerons.forEach(w -> w.controlAileron(maxAileron * aileron));
    }

    float maxElevator = 10f;
    float maxAileron = 2f;

    /**
     * Sets the status of the elevator. If the elevator is negative, it pushes
     * the nose down. If the elevator is positive, it pulls the nose up.
     *
     * @param elevator must be between -1.0 and 1.0
     */
    public void setElevator(float elevator) {
        horizontalStabilizers.forEach(s -> s.controlAileron(maxElevator * elevator));
    }

    public void setRudder(float rudder) {
        verticalStabilizers.forEach(s -> s.controlAileron(rudder));
    }

    public void setHeight(int height) {planeResponse(planeResponse().height(height));}
    public void setLocation(int x, int z) {setLocation(x, (int) planeResponse().height(), z);}
    public void setLocation(int x, int y, int z) { setLocation(new Vector3f(x, y, z)); }

    public synchronized void setLocation(Vector3f translation) {planeResponse(planeResponse().translation(translation));}
    
    public float roll() {
        int i = forward().cross(Vector3f.UNIT_Y).dot(up()) > 0 ? 1 : -1;
        return i * geom.rotation().mult(Vector3f.UNIT_Y).angleBetween(Vector3f.UNIT_Y) * RAD_TO_DEG;
    }
    
    public float getHeight() {return planeResponse().height();}
    public Vector3f getLocation() { return geom.translation();}
    public Vector3f forward() {return geom.forwardNormal();}
    public Vector3f up() {return geom.upNormal();}
    public Vector2f getLocation2D() {  return new Vector2f(geom.translation().x, geom.translation().z); }
    public String velocityKmh() { return toMin3Integer0Fraction(velocityMs * 3.6f);}
    public void firing(boolean trigger) { firing = trigger; }
    public synchronized void crashed(boolean crashed) { this.crashed = crashed; }
    public synchronized boolean crashed() { return crashed; }
    public String getInfo() { return planeResponse.toString();}
    public PlaneGeometry planeGeometry() { return geom; }
    public void fireEffect(ParticleEmitter fireEffect) { this.fireEffect = fireEffect; }
}
