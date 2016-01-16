package com.codebetyars.skyhussars.engine;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.renderer.Camera;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;

public class TerrainManager {

    private TerrainQuad terrain;

    public TerrainManager(AssetManager assetManager, Camera camera) {
        AbstractHeightMap heightmap;
        Texture heightMapImage = assetManager.loadTexture(
                "Textures/Adria.bmp");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 1f);
        heightmap.load();
        int patchSize = 17;
        terrain = new TerrainQuad("my terrain", patchSize, 2049, heightmap.getHeightMap());
        Material mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        // Load alpha map (for splat textures)
        mat_terrain.setTexture("AlphaMap", assetManager.loadTexture("Textures/Adria_alpha.png"));
        Texture grass = assetManager.loadTexture(
                "Textures/ground.png");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("DiffuseMap", grass);
        mat_terrain.setFloat("DiffuseMap_0_scale", 4096f);
        terrain.setMaterial(mat_terrain);

        Texture water = assetManager.loadTexture(
                "Textures/water.png");
        water.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("DiffuseMap_2", water);
        mat_terrain.setFloat("DiffuseMap_2_scale", 32f);

        Texture land = assetManager.loadTexture(
                "Textures/forest.png");
        land.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("DiffuseMap_1", land);
        mat_terrain.setFloat("DiffuseMap_1_scale", 128f);

        terrain.setMaterial(mat_terrain);
        terrain.setLocalScale(1000f, 1f, 1000f);

        TerrainLodControl control = new TerrainLodControl(terrain, camera);
        terrain.addControl(control);
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
            //plane.setHeight((int) height + 1);
        }
        return collide;
    }
}
