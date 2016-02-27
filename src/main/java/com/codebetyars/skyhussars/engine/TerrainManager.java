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

import com.codebetyars.skyhussars.engine.plane.Plane;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TerrainManager implements InitializingBean {

    @Autowired
    private AssetManager assetManager;

    @Autowired
    private Camera camera;

    @Autowired
    private Node rootNode;

    private TerrainQuad terrain;

    @Override
    public void afterPropertiesSet() {
        AbstractHeightMap heightmap = new ImageBasedHeightMap(assetManager.loadTexture("Textures/Adria.bmp").getImage(), 1f);
        heightmap.load();

        Texture grass = assetManager.loadTexture("Textures/ground.png");
        grass.setWrap(Texture.WrapMode.Repeat);

        Texture water = assetManager.loadTexture("Textures/water.png");
        water.setWrap(Texture.WrapMode.Repeat);

        Texture land = assetManager.loadTexture("Textures/forest.png");
        land.setWrap(Texture.WrapMode.Repeat);

        Material mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        mat_terrain.setTexture("AlphaMap", assetManager.loadTexture("Textures/Adria_alpha.png"));
        mat_terrain.setTexture("DiffuseMap", grass);
        mat_terrain.setFloat("DiffuseMap_0_scale", 4096f);
        mat_terrain.setTexture("DiffuseMap_2", water);
        mat_terrain.setFloat("DiffuseMap_2_scale", 32f);
        mat_terrain.setTexture("DiffuseMap_1", land);
        mat_terrain.setFloat("DiffuseMap_1_scale", 128f);

        terrain = new TerrainQuad("my terrain", 17, 2049, heightmap.getHeightMap());
        terrain.setMaterial(mat_terrain);
        terrain.setLocalScale(1000f, 1f, 1000f);
        terrain.addControl(new TerrainLodControl(terrain, camera));

        rootNode.attachChild(terrain);
    }

    public TerrainQuad getTerrain() {
        return terrain;
    }

    public float getHeightAt(Vector2f at) {
        return terrain.getHeight(at);
    }

    public boolean checkCollisionWithGround(Plane plane) {
        boolean collide = false;
        float height = terrain.getHeight(plane.getLocation2D());
        if (height > plane.getHeight()) {
            collide = true;
        }
        return collide;
    }
}
