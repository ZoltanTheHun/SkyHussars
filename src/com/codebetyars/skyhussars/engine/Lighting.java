package com.codebetyars.skyhussars.engine;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.util.LinkedList;
import java.util.List;

public class Lighting {

    private List<Light> lights;

    public Lighting() {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(1f));
        sun.setDirection(new Vector3f(0.0f, -1.0f, 0.0f));
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.White.mult(1f));
        lights = new LinkedList<Light>();
        lights.add(sun);
        lights.add(ambientLight);
    }
    
    public List<Light> getLights(){
        return lights;
    }
}
