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
package com.codebetyars.skyhussars.planeed;

import com.codebetyars.skyhussars.engine.plane.PlaneDescriptor;
import java.io.File;
import java.util.Optional;

public class PlaneEdState {
    
    private File openFile;
    private PlaneDescriptor planeDescriptor;
    
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
}
