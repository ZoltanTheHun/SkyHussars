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
import java.util.function.BiFunction;

/**
 * RegistryLoader can create a registry of type T.
 * In a given registryLocation it goes through all the subfolders and it looks 
 * for json descriptors with a given jsonName. All the descriptors are marshaled to 
 * the type specified with the targetClass
 * 
 * The registry is expected to be populated when the constructor runs.
 * 
 * @param <T> The type of the registry
 */
public class RegistryLoader<T> {
    
    private final String name;
    private final File registryLocation;
    private final Class<T> targetClass;
    private final String jsonName; 
    private final Registry<T> registry;
    
    /**
     * @param name A name that can identify this registry.
     * @param registryLocation The location which contains the folders with the registry elements
     * @param jsonName The name of the descriptors to be loaded
     * @param targetClass The type of registry that should be created
     * @param nameOf A function that can provide a name for a given T
     */
    public RegistryLoader(String name,File registryLocation,String jsonName,Class<T> targetClass,BiFunction<T,File,String> nameOf){
        checkNotNull(nameOf);
        this.name = checkNotNull(name);
        this.jsonName = checkNotNull(jsonName);
        if(jsonName.length() == 0) throw new IllegalArgumentException();
        this.registryLocation = checkNotNull(registryLocation);
        this.targetClass = checkNotNull(targetClass);
        if(!registryLocation.isDirectory()) 
            throw new IllegalArgumentException( 
                    name + " registry: Unable to open planes directory. " 
                            + registryLocation.getPath()
                            + " is not a directoy");
        
        registry = new Registry<>(registryLocation);
        populateRegistry(registry,nameOf);
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
    
    private List<T> populateRegistry(Registry<T> registry,BiFunction<T,File,String> nameOf) {
        List<T> descriptors = new ArrayList<>();
        for (File dir : collectDirectories()) {
            T descriptor = Marshal.unmarshal(openDescriptorFile(dir),targetClass);
            registry.register(nameOf.apply(descriptor,dir),descriptor);
        }
        return descriptors;
    }
    
    private File openDescriptorFile(File planeDirectory) {
        File descriptorFile = new File(planeDirectory.getPath() + File.separator + jsonName);
        if (!descriptorFile.exists()) {
            throw new IllegalStateException(
                    "Unable to open plane descriptor at "
                    + descriptorFile.getPath());
        }
        return descriptorFile;
    }
    
    /**
     * To retrieve the loaded registry from the loader
     * @return a registry of type T
     */
    public Registry<T> registry(){ return registry; }
}
