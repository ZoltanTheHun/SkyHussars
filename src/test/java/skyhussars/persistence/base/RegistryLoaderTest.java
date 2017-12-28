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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import skyhussars.persistence.terrain.TerrainDescriptor;

public class RegistryLoaderTest {
     
    @Rule
    public ExpectedException expected = ExpectedException.none();
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private Function<TerrainDescriptor,String> nameOf = t -> t.name;
    
    @Test
    public void testNameNullCheck(){
        expected.expect(NullPointerException.class);
        RegistryLoader<TerrainDescriptor> rl = new RegistryLoader<>(null,new File(""),
                "test.json",TerrainDescriptor.class,nameOf);
    }
    
    @Test
    public void testFileNullCheck(){
        expected.expect(NullPointerException.class);
        RegistryLoader<TerrainDescriptor> rl = new RegistryLoader<>("Test",null,
                "test.json",TerrainDescriptor.class,nameOf);
    }
    
    @Test
    public void testDescriptorClassNullCheck(){
        expected.expect(NullPointerException.class);
        RegistryLoader rl = new RegistryLoader("Test",new File(""),"test.json",
                null,nameOf);
    }
    @Test
    public void testDirectoryCheck(){
        expected.expect(IllegalArgumentException.class);
        RegistryLoader<TerrainDescriptor>  rl = new RegistryLoader("Test",new File(""),
                "test.json",TerrainDescriptor.class,nameOf);
    }
    @Test
    public void testJsonNameCheck(){
        expected.expect(NullPointerException.class);
        RegistryLoader<TerrainDescriptor>  rl = new RegistryLoader("Test",new File(""),
                null,TerrainDescriptor.class,nameOf);
    }
    
    @Test 
    public void testJsonNameLengthCheck(){
        expected.expect(IllegalArgumentException.class);
        RegistryLoader<TerrainDescriptor>  rl = new RegistryLoader("Test",new File(""),
                "",TerrainDescriptor.class,nameOf);
    }
    
    @Test 
    public void testNamingFunctionNullCheck(){
        expected.expect(NullPointerException.class);
        RegistryLoader<TerrainDescriptor>  rl = new RegistryLoader("Test",new File(""),
                "",TerrainDescriptor.class,null);
    }
    
    @Test
    public void testTerrainRegistryIsAvailable(){
        try {
            File root = folder.newFolder("root");
            File child1 = new File(root, "child1");
            child1.mkdir();
            File child2 = new File(root, "child2");
            child2.mkdir();
            File target1 = new File(child1,"test.json");
            File target2 = new File(child2,"test.json");
            target1.createNewFile();
            target2.createNewFile();                    

            try (PrintWriter test1 = new PrintWriter(target1)) {
                
                test1.print("{  \"name\" : \"Test Terrain 1\",  \"size\" : 10,  \"heightMapLocation\" : \"Test\"}");
                test1.flush();
            }
            try (PrintWriter test2 = new PrintWriter(target2)) {
                test2.print("{  \"name\" : \"Test Terrain 2\",  \"size\" : 10,  \"heightMapLocation\" : \"Test\"}");
                test2.flush();
            }
            RegistryLoader<TerrainDescriptor>  rl = new RegistryLoader<>("Terrain Registry",root,
                    "test.json",TerrainDescriptor.class,nameOf);
            Registry<TerrainDescriptor> tr = rl.registry();
            assert(tr.item("Test Terrain 1").isPresent());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
    
}
