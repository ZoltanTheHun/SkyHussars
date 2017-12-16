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
package skyhussars.persistence.terrain;

import static com.google.common.base.Preconditions.*;
import java.util.Objects;


/**
 * This class is the representation of the Terrain datastructure in the filesystem
 */
public class TerrainDescriptor {
    
    public String name;
    public int size;
    public String heightMapLocation;
    
    /* For Jackson deserialization */
    private TerrainDescriptor(){};
    
    /**
     * 
     * @param name name of the Terrain
     * @param size size of the Terrain in kms
     * @param heightMapLocation location of the heightmap data for the terrain
     */
    public TerrainDescriptor(String name,int size,String heightMapLocation){
        if(size < 1) throw new IllegalArgumentException();
        this.name = checkNotNull(name);
        this.size = size;
        this.heightMapLocation = checkNotNull(heightMapLocation);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + this.size;
        hash = 97 * hash + Objects.hashCode(this.heightMapLocation);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TerrainDescriptor other = (TerrainDescriptor) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        if (!Objects.equals(this.heightMapLocation, other.heightMapLocation)) {
            return false;
        }
        return true;
    }
    
    
    
}
