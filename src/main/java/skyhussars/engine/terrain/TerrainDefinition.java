/*
 * Copyright (c) 2017, ZoltanTheHun
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
package skyhussars.engine.terrain;

import java.util.Optional;

public class TerrainDefinition {

    private String name;
    private int size; //km
    private String heightmapPath;
    private String textureMap;
    private TerrainTexture tx1,tx2,tx3;    
    public TerrainDefinition(){};
    public TerrainDefinition(TerrainDefinition td){
        this.heightmapPath = td.heightmapPath;
        this.textureMap = td.textureMap;
        this.size = td.size;
        this.name = td.name;
        this.tx1 = td.tx1;
        this.tx2 = td.tx2;
        this.tx3 = td.tx3;
    }
    
    public TerrainDefinition heightMapPath(String path){
        TerrainDefinition td = new TerrainDefinition(this);
        td.heightmapPath = path;
        return td;
    }
    
    public TerrainDefinition textureMap(String path){
        TerrainDefinition td = new TerrainDefinition(this);
        td.textureMap = path;
        return td;
    }
    
    public TerrainDefinition size(int size){
        TerrainDefinition td = new TerrainDefinition(this);
        td.size = size;
        return td;
    }
    
    public TerrainDefinition name(String path){
        TerrainDefinition td = new TerrainDefinition(this);
        td.name = name;
        return td;
    }
    
    public TerrainDefinition tx1(TerrainTexture tx){
        TerrainDefinition td = new TerrainDefinition(this);
        td.tx1 = tx;
        return td;
    }
    
    public TerrainDefinition tx2(TerrainTexture tx){
        TerrainDefinition td = new TerrainDefinition(this);
        td.tx2 = tx;
        return td;
    }
    
    public TerrainDefinition tx3(TerrainTexture tx){
        TerrainDefinition td = new TerrainDefinition(this);
        td.tx3 = tx;
        return td;
    }
    
    public Optional<String> name(){return Optional.of(name);}
    public Optional<Integer> size(){return Optional.of(size);}
    public Optional<String> heightMapPath(){return Optional.of(heightmapPath);}
    public TerrainTexture tx1(){return tx1;}
    public TerrainTexture tx2(){return tx2;}   
    public TerrainTexture tx3(){return tx3;}
    public Optional<String> textureMap(){return Optional.ofNullable(textureMap);}
}
