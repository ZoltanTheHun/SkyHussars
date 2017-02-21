/*
 * Copyright (c) 2016, ZoltanTheHun
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributionwis in binary form must reproduce the above copyright notice,
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
package com.codebetyars.skyhussars.engine.data;

import com.codebetyars.skyhussars.engine.plane.PlaneDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PlaneRegistry is an immutable collection of plane descriptors. Working with
 * the plane registry happens through the plane name. All names are stored only
 * once.
 */
public class PlaneRegistry {

    private final Map<String, PlaneDescriptor> planeDescriptors;

    /**
     * This constructor will create an empty plane registry.
     */
    public PlaneRegistry() {
        planeDescriptors = new HashMap<>();
    }

    /**
     * This constructor will create a plane registry from the collection of
     * planeDescriptors
     *
     * @param planeDescriptors - a collection of planeDescriptors that will be
     * stored in the PlaneRegistry
     */
    public PlaneRegistry(Collection<PlaneDescriptor> planeDescriptors) {
        this.planeDescriptors = new HashMap<>();
        for (PlaneDescriptor planeDescriptor : planeDescriptors) {
            addPlane(planeDescriptor);
        }
    }

    private PlaneRegistry(Map<String, PlaneDescriptor> planeDescriptors) {
        this.planeDescriptors = new HashMap<>(planeDescriptors);
    }

    /**
     * Lists the available planes in this registry by name
     *
     * @return the list of plane names stored in this registry
     */
    public List<String> availablePlanes() {
        return new ArrayList<>(planeDescriptors.keySet());
    }

    /**
     * Creates a new PlaneRegistry with all the planes in the current registry
     * plus with the one provided as parameter.
     *
     * @param planeDescriptor the extra plane in the new registry, if the
     * planeName is already contained in the registry, the new planeDescriptor
     * will replace the plane under this name.
     * @return the newly created registry
     */
    public PlaneRegistry registerPlane(PlaneDescriptor planeDescriptor) {
        PlaneRegistry planeRegistry = new PlaneRegistry(planeDescriptors);
        planeRegistry.addPlane(planeDescriptor);
        return planeRegistry;
    }

    private void addPlane(PlaneDescriptor planeDescriptor) {
        this.planeDescriptors.put(planeDescriptor.getName(), planeDescriptor);
    }
    /**
     * Returns the planeDescriptor by name of the descriptor
     * @param name
     * @return a planeDescriptor
     */
    public PlaneDescriptor planeDescriptor(String name){
        return planeDescriptors.get(name);
    }
}
