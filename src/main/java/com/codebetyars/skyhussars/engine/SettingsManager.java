/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codebetyars.skyhussars.engine;

import com.jme3.asset.plugins.FileLocator;
import java.io.File;

public class SettingsManager {

    private File assetDirectory;

    public File assetDirectory() {
            return assetDirectory;
    }

    public SettingsManager() {
        setupAssetRoot();
    }
            

    private void setupAssetRoot() {
        File dirs[] = new File[]{new File(System.getProperty("user.dir") + "/assets"),
            new File(System.getProperty("user.dir") + "/../assets")};
        for (File dir : dirs) {
            if (dir.exists()) {
                assetDirectory = dir;
            }
        }
        if (assetDirectory == null) {
            throw new IllegalStateException("Cannot find asset directory");
        }

    }


}
