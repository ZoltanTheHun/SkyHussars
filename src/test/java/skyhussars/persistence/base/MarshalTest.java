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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import skyhussars.persistence.terrain.TerrainDescriptor;

public class MarshalTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    

    @Test
    public void testTerrainMarshal() throws IOException{
        String expected = "{  \"name\" : \"Test Terrain\",  \"size\" : 10,  \"heightMapLocation\" : \"Test\"}";
        TerrainDescriptor terrainDescriptor = new TerrainDescriptor("Test Terrain",10,"Test");
        File testFile = folder.newFile("marshal.txt");
        Marshal.marshal(terrainDescriptor, testFile);
        String json = Files.lines(testFile.toPath(), StandardCharsets.UTF_8).collect(joining(""));
        assertEquals(expected,json);
    }
    
    @Test
    public void testTerrainUnmarshal() throws IOException{
        TerrainDescriptor expected = new TerrainDescriptor("Test Terrain",10,"Test");
        String json = "{  \"name\" : \"Test Terrain\",  \"size\" : 10,  \"heightMapLocation\" : \"Test\"}";
        File testFile = folder.newFile("unmarshal.txt");
        PrintWriter pw = new PrintWriter(testFile);
        pw.print(json);
        pw.flush();
        pw.close();
        TerrainDescriptor actual = Marshal.unmarshal(testFile, TerrainDescriptor.class);
        assertEquals(expected, actual);
    }
}
