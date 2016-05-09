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
package com.codebetyars.skyhussars.engine;

import com.jme3.asset.AssetManager;
import com.jme3.audio.Listener;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.SceneProcessor;
import com.jme3.post.filters.ComposeFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComplexCamera implements InitializingBean {

    @Autowired
    private RenderManager renderManager;

    @Autowired
    private AssetManager assetManager;

    @Autowired
    private Camera mainCam;

    @Autowired
    private Listener listener;

    @Autowired
    private Node rootNode;

    private ViewPort mainViewPort;

    private float fov = 45;

    private List<CombinedViewport> viewPorts = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        mainViewPort = renderManager.getMainViews().get(0);
        viewPorts.add(new CombinedViewport("nearView", renderManager, mainCam, fov, 0.5f, 310f, rootNode));
        viewPorts.add(new CombinedViewport("farView", renderManager, mainCam, fov, 300f, 200000f, rootNode));
        mainViewPort.setBackgroundColor(ColorRGBA.BlackNoAlpha);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        viewPorts.forEach(viewPort -> {
            fpp.addFilter(new ComposeFilter(viewPort.colorBuffer()));
        });
        mainViewPort.addProcessor(fpp);
        fov(45);
    }

    public synchronized void fov(float fov) {
        this.fov = fov;
        viewPorts.forEach(viewPort -> {
            viewPort.fov(fov);
        });
    }

    public synchronized float fov() {
        return fov;
    }

    public synchronized void moveCameraTo(Vector3f location) {
        viewPorts.forEach(viewPort -> {
            viewPort.cam().setLocation(location);
        });

        listener.setLocation(location);
    }

    public synchronized void lookAtDirection(Vector3f direction, Vector3f up) {
        viewPorts.forEach(viewPort -> {
            viewPort.cam().lookAtDirection(direction, up);
        });
        listener.setRotation(viewPorts.get(0).cam().getRotation());
    }

    public synchronized void lookAt(Vector3f direction, Vector3f up) {
        viewPorts.forEach(viewPort -> {
            viewPort.cam().lookAt(direction, up);
        });
        listener.setRotation(viewPorts.get(0).cam().getRotation());
    }

    public Camera testCamera() {
        return viewPorts.get(0).cam();
    }

    public void addEffect(SceneProcessor processor) {
        viewPorts.forEach(viewPort -> {
            viewPort.viewPort().addProcessor(processor);
        });
        //nearViewPort.addProcessor(processor);
        //farViewPort.addProcessor(processor);
    }
}
