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
package com.codebetyars.skyhussars.engine;

import com.codebetyars.skyhussars.TestData;
import com.codebetyars.skyhussars.engine.mission.MissionDescriptor;
import com.codebetyars.skyhussars.engine.plane.PlaneDescriptor;
import com.codebetyars.skyhussars.engine.plane.PlaneFactory;
import com.codebetyars.skyhussars.engine.weapons.ProjectileManager;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class DataManager {

    private SoundManager soundManager;
    private ModelManager modelManager;
    private TestData testData = new TestData();
    private PlaneFactory planeFactory;
    private AssetManager assetManager;
    private Geometry geom;
    private ProjectileManager projectileManager;

    public DataManager(AssetManager assetManager, Node node) {
        this.modelManager = new ModelManager(assetManager);
        this.soundManager = new SoundManager(assetManager);
        projectileManager = new ProjectileManager(this, node);
        this.planeFactory = new PlaneFactory(this, projectileManager);
        this.assetManager = assetManager;
        Box box = new Box(0.2f, 0.2f, 0.2f);
        geom = new Geometry("bullet", box); // wrap shape into geometry
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md"); // create material
        mat.setColor("Color", ColorRGBA.Green);
        geom.setMaterial(mat);
    }

    public ModelManager modelManager() {
        return modelManager;
    }

    public SoundManager soundManager() {
        return soundManager;
    }

    public PlaneFactory planeFactory() {
        return planeFactory;
    }

    public ProjectileManager projectileManager() {
        return projectileManager;
    }

    public PlaneDescriptor getPlaneDescriptor(String planeDescriptorId) {
        return testData.getPlaneDescriptor(planeDescriptorId);
    }

    public MissionDescriptor missionDescriptor(String name) {
        return testData.getMissionDescriptor(name);
    }

    public Geometry getBox() {
        return geom.clone();
    }

    public Geometry hitBox(Box box) {
        Geometry geom = new Geometry("hitbox", box); // wrap shape into geometry
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md"); // create material
        mat.setColor("Color", ColorRGBA.Red);
        geom.setMaterial(mat);
        return geom;
    }

    public ParticleEmitter fireEffect() {
        ParticleEmitter fire = new ParticleEmitter("Emitter", Type.Triangle, 30);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Textures/explosion0.png"));
        fire.setMaterial(mat_red);
        fire.setImagesX(8);
        fire.setImagesY(8); // 2x2 texture animation
        fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(1.5f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(0.5f);
        fire.setHighLife(3f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        return fire;
    }
}
