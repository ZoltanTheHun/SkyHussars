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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A registry of T items. The specialty of the registry that all items are 
 * identified by a unique name and no new item can be registered twice with the 
 * same name
 * 
 * @param <T> 
 */
public class Registry<T> {
    
    private final Map<String, T> descriptors;
    
    /**
     * This constructor creates an empty registry that does not contain any item
     */
    public Registry(){
        this.descriptors = new HashMap<>();
    }
    
    /**
     * Register a new item to this registry. If the name is already associated 
     * with this registry, return with false value
     * @param name the name of the descriptor
     * @param descriptor the new descriptor item of type T
     * @return if the name already exists, return false, if the registration is
     * successful, return true
     */
    public boolean register(String name,T descriptor){
        if (descriptors.containsKey(name)) return false;
        descriptors.put(name, descriptor);
        return true;
    }
    
    /**
     * Return an item by name, if no item exists the returned value is empty
     * @param name name of the item, null is not permitted
     * @return an optional item identified by the given name
     */
    public Optional<T> item(String name){
        return Optional.ofNullable(descriptors.get(checkNotNull(name)));
    }
    
    /**
     * Lists the available items in this registry by name
     *
     * @return the list of item names stored in this registry
     */
    public List<String> availableItems() {
        return new ArrayList<>(descriptors.keySet());
    }
}
