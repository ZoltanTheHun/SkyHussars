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
package com.codebetyars.skyhussars.engine.terrain;

import com.codebetyars.skyhussars.engine.ComplexCamera;
import com.codebetyars.skyhussars.engine.plane.Plane;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TerrainManager {

    @Autowired
    private AssetManager assetManager;

    @Autowired
    private ComplexCamera camera;

    @Autowired
    private Node rootNode;

    private Optional<TerrainQuad> terrain;

    public TerrainQuad getTerrain() {
        return terrain.get(); //yeah, this is unsafe for now
    }

    private TerrainDefinition terrainDefinition = new TerrainDefinition().heightMapPath("Textures/AdriaSmall.bmp")
            .name("Adria")
            .size(500)
            .tx1(new TerrainTexture().description("Ground texture")
                                    .path("Textures/grass.png").scale(16384f))
            .tx2(new TerrainTexture().description("Water texture")
                                    .path("Textures/water.png").scale(1024f))
            .tx3(new TerrainTexture().description("Grass texture")
                                    .path("Textures/grass.png").scale(1024f));

    public float getHeightAt(Vector2f at) {
        return terrain.get().getHeight(at); // unsafe for now
    }

    public boolean checkCollisionWithGround(Plane plane) {
        return terrain.map(t -> t.getHeight(plane.getLocation2D()) > plane.getHeight()).orElse(false);
    }

    private Texture texture(String path){
        Texture texture = assetManager.loadTexture(path);
        texture.setWrap(Texture.WrapMode.Repeat);
        return texture;
    }
    public void loadTerrain() {
        Texture grass = assetManager.loadTexture(terrainDefinition.tx1().path());
        grass.setWrap(Texture.WrapMode.Repeat);

        Texture water = assetManager.loadTexture(terrainDefinition.tx2().path());
        water.setWrap(Texture.WrapMode.Repeat);

        Texture land = assetManager.loadTexture(terrainDefinition.tx3().path());
        land.setWrap(Texture.WrapMode.Repeat);

        Material mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        mat_terrain.setTexture("AlphaMap", assetManager.loadTexture("Textures/AdriaSmall_alpha.png"));
        mat_terrain.setTexture("DiffuseMap", texture(terrainDefinition.tx1().path()));
        mat_terrain.setFloat("DiffuseMap_0_scale", terrainDefinition.tx1().scale());
        mat_terrain.setTexture("DiffuseMap_2", texture(terrainDefinition.tx2().path()));
        mat_terrain.setFloat("DiffuseMap_2_scale", terrainDefinition.tx2().scale());
        mat_terrain.setTexture("DiffuseMap_1", texture(terrainDefinition.tx3().path()));
        mat_terrain.setFloat("DiffuseMap_1_scale", terrainDefinition.tx3().scale());

        int patchSize = 9;
        terrain = terrainDefinition.heightMapPath().map(m -> assetManager.loadTexture(m)
                .getImage()).map(i -> new ImageBasedHeightMap(i, 12f))
                .map(hm -> {
                    hm.load();
                    return new TerrainQuad("Terrain", patchSize, hm.getSize()+1, hm.getHeightMap());
                }).map(tq -> {
                    tq.setMaterial(mat_terrain);
                    tq.setLocalScale(1000f, 1f, 1000f); //heightmap size: we used a 512px heightmap to represent a 512km area? 1pixel 1m*1000 
                    tq.addControl(new TerrainLodControl(tq, camera.testCamera()));
                    tq.setShadowMode(RenderQueue.ShadowMode.Receive);
                    rootNode.attachChild(tq);
                    return tq;
                });
    }
}
