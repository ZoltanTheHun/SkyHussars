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

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ModelManager implements InitializingBean {


    @Autowired
    private AssetManager assetManager;

    private Map<String, Spatial> spatials = new HashMap<>();
    private Map<String, Material> materials = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        loadModels();
        loadMaterials();
    }

    private void loadModels() {
        Spatial p80 = assetManager.loadModel("Models/p80/p80_16_game.j3o");
        spatials.put("p80", p80);

    }

    private void loadMaterials() {
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
        Spatial model = spatials.get(modelName).clone();
        model.setMaterial(materials.get(materialName));
        return model;
    }
}
