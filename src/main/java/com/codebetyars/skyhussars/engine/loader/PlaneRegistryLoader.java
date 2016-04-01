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
package com.codebetyars.skyhussars.engine.loader;

import com.codebetyars.skyhussars.engine.data.PlaneRegistry;
import com.codebetyars.skyhussars.engine.plane.PlaneDescriptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaneRegistryLoader {

    private final String planesFolderName = "/Planes";
    private final String jsonDescriptorname = "/plane.json";
    private final ObjectMapper mapper = new ObjectMapper();

    private final PlaneRegistry planeRegistry;

    public PlaneRegistryLoader(File assetFolder) {
        planeRegistry = new PlaneRegistry(loadplaneDescriptors(assetFolder));
    }

    private List<PlaneDescriptor> loadplaneDescriptors(File assetFolder) {
        List<PlaneDescriptor> planeDescriptors = new ArrayList<>();
        for (File planeDirectory : collectPlaneDirectories(assetFolder)) {
            PlaneDescriptor planeDescriptor = unmarshall(openDescriptorFile(planeDirectory));
            planeDescriptors.add(planeDescriptor);
        }
        return planeDescriptors;
    }

    public PlaneRegistry planeRegistry() {
        return planeRegistry;
    }

    private File[] collectPlaneDirectories(File assetFolder) {
        File planeCollection = new File(assetFolder.getPath() + planesFolderName);
        if (!planeCollection.exists()) {
            throw new IllegalStateException(
                    "Unable to open planes directory. Directory does not exists at "
                    + planeCollection.getPath());
        }
        File[] planes = planeCollection.listFiles();
        if (planes == null) {
            throw new IllegalStateException(
                    "Unable to open planes directory. "
                    + planeCollection.getPath() + " is not a directory");
        }
        if (planes.length == 0) {
            throw new IllegalStateException(
                    "There are no planes under planes directory. Folder empty at "
                    + planeCollection.getPath());
        }
        return planes;
    }

    private File openDescriptorFile(File planeDirectory) {
        File descriptorFile = new File(planeDirectory.getPath() + jsonDescriptorname);
        if (!descriptorFile.exists()) {
            throw new IllegalStateException(
                    "Unable to open plane descriptor at "
                    + descriptorFile.getPath());
        }
        return descriptorFile;
    }

    private PlaneDescriptor unmarshall(File descriptorFile) {
        PlaneDescriptor planeDescriptor = null;
        try {
            planeDescriptor = mapper.readValue(descriptorFile, PlaneDescriptor.class);
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to unmarshall descriptor file at "
                    + descriptorFile.getPath(), ex);
        }
        return planeDescriptor;
    }
}
