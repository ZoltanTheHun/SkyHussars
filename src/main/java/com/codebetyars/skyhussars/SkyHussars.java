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
package com.codebetyars.skyhussars;

import com.codebetyars.skyhussars.engine.SettingsManager;
import com.codebetyars.skyhussars.engine.loader.PlaneRegistryLoader;
import com.codebetyars.skyhussars.engine.plane.Plane;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class SkyHussars extends SimpleApplication {

    private final SettingsManager settingsManager = new SettingsManager();

    private final static Logger logger = LoggerFactory.getLogger(Plane.class);

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(false);
        settings.setTitle("SkyHussars");
        settings.setSettingsDialogImage("images/settings_image.jpg");
        
        SkyHussars application = new SkyHussars();
        application.setSettings(settings);
        application.start();
    }

    private SkyHussarsContext skyHussarsContext;

    private void setupAssetRoot() {
        assetManager.registerLocator(settingsManager.assetDirectory().getPath(), FileLocator.class);
    }

    private void registerCommonFunctionsToContext(GenericApplicationContext appcontext) {
        DefaultListableBeanFactory beanFactory = appcontext.getDefaultListableBeanFactory();
        beanFactory.registerSingleton("settingsManager", settingsManager);
        beanFactory.registerSingleton("renderManager", renderManager);
        beanFactory.registerSingleton("application", this);
        beanFactory.registerSingleton("rootNode", getRootNode());
        beanFactory.registerSingleton("assetManager", getAssetManager());
        beanFactory.registerSingleton("inputManager", getInputManager());
        beanFactory.registerSingleton("camera", getCamera());
        beanFactory.registerSingleton("flyByCamera", getFlyByCamera());
        beanFactory.registerSingleton("audioRenderer", getAudioRenderer());
        beanFactory.registerSingleton("guiViewPort", getGuiViewPort());
        beanFactory.registerSingleton("listener", listener);
        beanFactory.registerSingleton("planeRegistry", new PlaneRegistryLoader(settingsManager.assetDirectory()).planeRegistry());
    }

    @Override
    public void simpleInitApp() {
        setupAssetRoot();
        AnnotationConfigApplicationContext appcontext = new AnnotationConfigApplicationContext();
        registerCommonFunctionsToContext(appcontext);
        appcontext.register(SkyHussarsContext.class);
        appcontext.refresh();
        skyHussarsContext = appcontext.getBean(SkyHussarsContext.class);
        skyHussarsContext.simpleInitApp();
        flyCam.setEnabled(false);
        setDisplayStatView(false);
    }

    @Override
    public void simpleUpdate(float tpf) {
        skyHussarsContext.simpleUpdate(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        skyHussarsContext.simpleRender(rm);
    }

}
