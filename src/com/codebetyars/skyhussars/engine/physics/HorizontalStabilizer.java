package com.codebetyars.skyhussars.engine.physics;

import com.jme3.math.Vector3f;

public class HorizontalStabilizer extends SymmetricAirfoil{

    public HorizontalStabilizer(String name,Vector3f cog,float wingArea, float incidence,float aspectRatio) {
        super(name,cog,wingArea, incidence,aspectRatio,false,90f);
    }
    
    //public float calculateDrag();
    
}
