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
package skyhussars.persistence.base;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegistryLoader<T> {
    
    private String name;
    private File registryLocation;
    private Class<T> targetClass;
    private String jsonName;
    private Registry<T> registry;
    
    public RegistryLoader(String name,File registryLocation,Class<T> targetClass){
        this.name = checkNotNull(name);
        this.registryLocation = checkNotNull(registryLocation);
        this.targetClass = checkNotNull(targetClass);
        if(!registryLocation.isDirectory()) 
            throw new IllegalArgumentException( 
                    name + " registry: Unable to open planes directory. " 
                            + registryLocation.getPath()
                            + " is not a directoy");
        List<T> descriptors = loadDescriptors();
    }
    
    
    private File[] collectDirectories() {
        File[] directories = registryLocation.listFiles();
        if (directories == null) {
            throw new IllegalStateException(
                    name + " registry: Unable to open entries directory. "
                    + registryLocation.getPath() + " is not a directory");
        }
        if (directories.length == 0) {
            throw new IllegalStateException(
                    name + " registry: There are no entries under planes directory. Folder empty at "
                    + registryLocation.getPath());
        }
        return directories;
    }
    
    private List<T> loadDescriptors() {
        List<T> descriptors = new ArrayList<>();
        for (File dir : collectDirectories()) {
            T descriptor = Marshal.unmarshal(openDescriptorFile(dir),targetClass);
            descriptors.add(descriptor);
        }
        return descriptors;
    }
    
    private File openDescriptorFile(File planeDirectory) {
        File descriptorFile = new File(planeDirectory.getPath() + jsonName);
        if (!descriptorFile.exists()) {
            throw new IllegalStateException(
                    "Unable to open plane descriptor at "
                    + descriptorFile.getPath());
        }
        return descriptorFile;
    }
}
