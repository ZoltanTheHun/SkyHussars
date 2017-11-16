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
package skyhussars.planeed;

import skyhussars.engine.plane.PlaneDescriptor;
import java.io.File;
import static skyhussars.planeed.UiHelpers.*;
import java.util.Optional;
import skyhussars.engine.loader.PlaneDescriptorMarshal;
public class PlaneEdState {
    
    private File openFile;
    private PlaneDescriptor planeDescriptor;
    private final PlaneDescriptorMarshal pdl = new PlaneDescriptorMarshal();
    
    public PlaneEdState(){};
    public PlaneEdState(PlaneEdState state){
        this.openFile = state.openFile;
        this.planeDescriptor = state.planeDescriptor;
    }
    
    public PlaneEdState openFile(File openFile){
        PlaneEdState s = new PlaneEdState(this);
        s.openFile = openFile;
        return s;
    }
    
    public PlaneEdState planeDescriptor(PlaneDescriptor planeDescriptor){
        PlaneEdState s = new PlaneEdState(this);
        s.planeDescriptor = planeDescriptor;
        return s;
    }
    
    public Optional<PlaneDescriptor> planeDescriptor(){return Optional.ofNullable(planeDescriptor);}
    public Optional<File> openFile(){return Optional.ofNullable(openFile);}
    
    public PlaneEdState loadPlane(File file) {
        if(file == null) {
            displayError("An unexpected error occured while trying to load the file.");
            throw new IllegalArgumentException("No file was provided to open.");
        }
        openFile = file;
        planeDescriptor = pdl.unmarshal(openFile);
        if( planeDescriptor == null) {
            displayError("Unable to open file with name: " + openFile.getPath() + openFile.getName());
            openFile = null;
        }
        return this;
    }
    
    public PlaneEdState save(PlaneProperties pp){
        planeDescriptor.setName(pp.getName().getValue());
        planeDescriptor.setMassTakeOffMax(pp.getMassTakeOffMax().getValue());
        planeDescriptor.setMassGross(pp.getMassGross().getValue());
        planeDescriptor.setMassEmpty(pp.getMassEmpty().getValue());
        if(openFile != null) pdl.marshal(planeDescriptor, openFile); //this is fine for now
        return this;
    }
}
