/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skyhussars.persistence.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class Marshal {
        private static final ObjectMapper mapper = new ObjectMapper();
    
    public static <T> void marshal(T descriptor,File file){
        try { mapper.writerWithDefaultPrettyPrinter().writeValue(file, descriptor);
        } catch (IOException ex) { throw new RuntimeException(ex); }
    }

    public static <T> T unmarshal(File descriptorFile,Class<T> targetClass) {
        T descriptor = null;
        try { descriptor = mapper.readValue(descriptorFile,targetClass); } 
        catch (IOException ex) {throw new RuntimeException(ex);}
        return descriptor;
    }
}
