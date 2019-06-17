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
package skyhussars.terrained;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import skyhussars.engine.terrain.TheatreLoader;
import skyhussars.persistence.terrain.TerrainDescriptor;

/**
 * State manager for the Theatre editor. 
 * 
 * This class is the main interface between the UI and the underlying functionalities.
 * 
 */
@Component
public class TerrainEdService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TerrainEdController.class);
    
    private final TerrainProperties terrainProperties;
    private final TheatreLoader loader;
    
    @Autowired
    public TerrainEdService(TerrainProperties terrainProperties,TheatreLoader loader){
        this.terrainProperties = terrainProperties;
        this.loader = loader;
    }
    
    public boolean saveToFile(File file){
        boolean success = false;
        if (file != null && canSave()) {
            LOGGER.info("Saving " + terrainProperties.name.get() + " to " + file.getAbsolutePath());
            new Persistence().persist(terrainProperties, file);
            success = true;
        }
        return success;
    }
    
    private boolean canSave() {
        return terrainProperties.name.get() != null
                && terrainProperties.size.get() >= 1
                && terrainProperties.location.get() != null;
    }

    public TerrainProperties getTerrainProperties() {
        return terrainProperties;
    }
    
    public TerrainEdService newTheatre(String name){
        TerrainDescriptor terrain = loader.createTheatre(name);
        terrainProperties.from(terrain);
        return this;
    }
        
}
