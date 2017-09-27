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
import skyhussars.engine.physics.PlanePhysicsImpl;
import skyhussars.engine.physics.Airfoil;
import skyhussars.engine.physics.Engine;
import skyhussars.engine.physics.environment.Environment;
import skyhussars.engine.plane.instruments.Instruments;
import skyhussars.engine.sound.AudioHandler;
import skyhussars.engine.weapons.ProjectileManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import skyhussars.engine.physics.PlaneResponse;
import skyhussars.engine.weapons.Bullet;
import static skyhussars.utility.Streams.pp;

public class Plane {

    private final static Logger logger = LoggerFactory.getLogger(Plane.class);

    private PlaneMissionDescriptor planeMissionDescriptor;
    private String name;
    private final PlanePhysicsImpl physics;
    private final AudioHandler engineSound;
    private final AudioHandler gunSound;
    private List<GunGroup> gunGroups;
    private final List<Engine> engines = new ArrayList<>();
    private boolean firing = false;
    private final ProjectileManager projectileManager;
    private boolean crashed = false;
    private boolean shotdown = false;
    private ParticleEmitter fireEffect;
    private final PlaneGeometry geom;
    private PlaneResponse planeResponse = new PlaneResponse(new Quaternion(), new Vector3f(),0);

    public synchronized void tick(float tpf, Environment environment) {
        planeResponse = physics.update(tpf, environment,planeResponse);
        logger.debug(getInfo());
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
    private final List<Aileron> ailerons = new ArrayList<>();;

    public Plane(List<Airfoil> airfoils,
            AudioHandler engineSound, AudioHandler gunSound,
            ProjectileManager projectileManager, PlaneGeometry planeGeometry,
            Instruments instruments, List<Engine> engines, List<GunGroup> gunGroups, float grossMass) {
        this.engineSound = engineSound;
        engineSound.audioNode().setLocalTranslation(0, 0, - 5);
        this.gunSound = gunSound;
        geom = planeGeometry;
        geom.attachSpatialToRootNode(engineSound.audioNode());
        geom.attachSpatialToRootNode(gunSound.audioNode());
        this.projectileManager = projectileManager;
        this.gunGroups = gunGroups;
        sortoutAirfoils(airfoils);
        
        Quaternion rotation = Quaternion.IDENTITY.clone();//geom.root() .getLocalRotation(); 

        Vector3f translation = geom.root().getLocalTranslation();
        this.physics = new PlanePhysicsImpl(rotation, translation,grossMass, engines, airfoils);
        this.physics.speedForward(geom.root().getLocalRotation(), 300f);
        
    }
    
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
    
    public synchronized float aoa(){ return planeResponse.aoa; }
    public BoundingVolume getHitBox() { return geom.root().getWorldBound(); }

    public void hit() {
        if (!shotdown) {
            geom.attachSpatialToRootNode(fireEffect);
            fireEffect.emitAllParticles();
            for (Engine engine : engines) {
                engine.damage(1.0f);
            }
        }
        shotdown = true;
    }

    public void update(float tpf) {
        synchronized(this){
            geom.root().setLocalRotation(planeResponse.rotation);
            geom.root().setLocalTranslation(planeResponse.translation);
        }
        
        float ratio = FastMath.PI * 2 * (physics.speedKmH() / 900);
        geom.airspeedInd().setLocalRotation(new Quaternion().fromAngles(0, 0, ratio));
        if (!crashed) {
            Vector3f startLocation = geom.root().getLocalTranslation();
            Vector3f startVelocity = physics.getVVelovity();
            Quaternion startRotation = geom.root().getWorldRotation();          
            pp(gunGroups,gunGroup -> {
                List<Bullet> bulletsFired = gunGroup.firing(firing, startLocation, startVelocity, startRotation);
                pp(bulletsFired, bullet -> projectileManager.addProjectile(bullet));
            });
        }
    }

    public void updateSound() {
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
        if (throttle < 0.0f || throttle > 1.0f) {
            throw new IllegalArgumentException();
        }
        for (Engine engine : engines) {
            engine.setThrottle(throttle);
        }
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

    public synchronized void setHeight(int height) {
        planeResponse = new PlaneResponse(planeResponse.rotation, planeResponse.translation.setY(height), planeResponse.aoa);
        /*Vector3f translation = geom.root().getLocalTranslation();
        setLocation((int) translation.getX(), height, (int) translation.getZ());*/
    }

    public void setLocation(int x, int z) { 
        setLocation(x, (int) planeResponse.translation.getY(), z); 
    }

    public void setLocation(int x, int y, int z) { setLocation(new Vector3f(x, y, z)); }

    public synchronized void setLocation(Vector3f location) {
        planeResponse = new PlaneResponse(planeResponse.rotation, location, planeResponse.aoa);
/*
        geom.root().setLocalTranslation(location);
        physics.translation(location);*/
    }

    public synchronized float getHeight() {return planeResponse.height();}
    public Vector3f getLocation() { return geom.root().getLocalTranslation();}
    public Vector3f forward() {return geom.root().getLocalRotation().mult(Vector3f.UNIT_Z).normalize();}
    public Vector3f up() {return geom.root().getLocalRotation().mult(Vector3f.UNIT_Y).normalize();}

    public float roll() {
        int i = forward().cross(Vector3f.UNIT_Y).dot(up()) > 0 ? 1 : -1;
        return i * geom.root().getLocalRotation().mult(Vector3f.UNIT_Y).angleBetween(Vector3f.UNIT_Y) * FastMath.RAD_TO_DEG;
    }

    public Vector2f getLocation2D() {  return new Vector2f(geom.root().getLocalTranslation().x, geom.root().getLocalTranslation().z); }

    public String velocityKmh() { return physics.getSpeedKmH(); }
    public void firing(boolean trigger) { firing = trigger; }
    public void crashed(boolean crashed) { this.crashed = crashed; }
    public boolean crashed() { return crashed; }
    public String getInfo() { return physics.getInfo(); }
    public PlaneGeometry planeGeometry() { return geom; }
    public void fireEffect(ParticleEmitter fireEffect) { this.fireEffect = fireEffect; }
}
