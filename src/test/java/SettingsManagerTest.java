import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import skyhussars.engine.SettingsManager;

/*
 * Copyright (c) 2018, ZoltanTheHun
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
public class SettingsManagerTest {
    @Rule
    public ExpectedException expected = ExpectedException.none();
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test 
    public void testInstatiation(){   
        expected.expect(NullPointerException.class);
        SettingsManager settingsManager = new SettingsManager(null);
    }
        
    @Test 
    public void testAssetFolderDoesNotExist(){
        try {
            expected.expect(IllegalStateException.class);
            File root = folder.newFolder("test");
            SettingsManager settingsManager = new SettingsManager(root.getCanonicalPath());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Test 
    public void testAssetFolderExistsNextToRoot(){
        try {
            File root = folder.newFolder("test");
            File assets = new File(root, "assets");
            assets.mkdir();
            File altAssets = new File(root.getParentFile(),"assets");
            altAssets.mkdir();
            SettingsManager settingsManager = new SettingsManager(root.getCanonicalPath());
            assertEquals("Asset folder should prefer sibling directory: ",settingsManager.assetDirectory().getCanonicalPath(),altAssets.getCanonicalPath());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Test 
    public void testAssetFolderExistsUnderRoot(){
        try {
            File root = folder.newFolder("test");
            File assets = new File(root, "assets");
            assets.mkdir();
            SettingsManager settingsManager = new SettingsManager(root.getCanonicalPath());
            assertEquals("Asset folder should be under root: ",settingsManager.assetDirectory().getCanonicalPath(),assets.getCanonicalPath());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
