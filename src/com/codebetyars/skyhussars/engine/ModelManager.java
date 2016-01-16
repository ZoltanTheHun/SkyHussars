package com.codebetyars.skyhussars.engine;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import java.util.HashMap;
import java.util.Map;

public class ModelManager {

    private Map<String, Spatial> spatials = new HashMap<String, Spatial>();
    private Map<String, Material> materials = new HashMap<String,Material>();

    public ModelManager(AssetManager assetManager) {
        loadModels(assetManager);
        loadMaterials(assetManager);
    }

    private void loadModels(AssetManager assetManager) {
        Spatial p80 = assetManager.loadModel("Models/p80/p80_16_game.j3o");
        spatials.put("p80", p80);

    }

    private void loadMaterials(AssetManager assetManager) {
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        Texture texture = assetManager.loadTexture("Textures/p80.png");
        material.setFloat("Shininess", 100f);
        material.setBoolean("UseMaterialColors", true);
        material.setColor("Ambient", ColorRGBA.Gray);
        material.setColor("Diffuse", ColorRGBA.Gray);
        material.setColor("Specular", ColorRGBA.Gray);
        material.setTexture("DiffuseMap", texture);
        materials.put("p80_material", material);
    }
    
    public Spatial model(String modelName, String materialName){
        Spatial model = spatials.get(modelName);
        model.setMaterial(materials.get(materialName));
        return model;
    }
}
