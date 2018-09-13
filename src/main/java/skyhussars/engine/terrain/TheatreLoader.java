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

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import skyhussars.persistence.terrain.TerrainDescriptor;
import static skyhussars.utility.Streams.*;
/**
 * TheatreLoader collects and manages the load of theatres from a dedicated Threatres 
 * folder under an asset root. It is expected that all theatres have a descriptor file
 * called theatre.json in their directory. The folder name is used to identify the unique theatres.
 */
public class TheatreLoader {
    
    private final String theatresFolderName = "/Theatres";
    private final String jsonDescriptorname = "/theatre.json";
    private final Map<String,TerrainDescriptor> theatres; 
    private final File theatresFolder;
   
    public TheatreLoader(File assetFolder){
        theatresFolder = new File(assetFolder.getPath() + theatresFolderName);
        if(!theatresFolder.exists()) throw new IllegalStateException("TheatreLoader could not found " + theatresFolder.getPath());
        if(theatresFolder.isFile()) throw new IllegalStateException("TheatreLoader expected a folder, but found a file: " + theatresFolder.getPath());
        theatres = loadTheatres(theatresFolder.listFiles());
    }
    
    private Map<String,TerrainDescriptor> loadTheatres(File[] folders){
        Map<String, TerrainDescriptor> terrainDescriptors = new LinkedHashMap<>();
        for(File folder : folders){
            if(folder.isDirectory()) terrainDescriptors.put(folder.getName(), null);
        }
        return terrainDescriptors;
    }
    
    public List<String> theatres(){ return list(theatres.keySet().stream());}        
}