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
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

public class CombinedViewport {

    private final Camera cam;
    private final ViewPort viewPort;
    private Texture2D colorBuffer;
    private final float fov;
    private final float near;
    private final float far;
    private final float aspect;
    private final String name;
    private final Node node;
    private FilterPostProcessor fpp;

    public CombinedViewport(String name,
            RenderManager renderManager,
            Camera mainCam,
            float fov,
            float near,
            float far,
            Node node,
            FilterPostProcessor fpp
    ) {
        this.name = name;
        this.cam = mainCam.clone();
        this.viewPort = renderManager.createPreView(name, cam);
        this.fov = fov;
        this.near = near;
        this.far = far;
        this.aspect = (float) mainCam.getWidth() / (float) mainCam.getHeight();
        this.node = node;
        this.fpp = fpp;
        setupView();
    }

    private void setupView() {
        cam.setFrustumPerspective(fov, aspect, near, far);

        FrameBuffer offBuffer = new FrameBuffer(cam.getWidth(), cam.getHeight(), 1);

        colorBuffer = new Texture2D(cam.getWidth(), cam.getHeight(), Image.Format.RGBA8);
        colorBuffer.setMinFilter(Texture.MinFilter.Trilinear);
        colorBuffer.setMagFilter(Texture.MagFilter.Bilinear);

        offBuffer.setDepthBuffer(Image.Format.Depth);
        offBuffer.setColorTexture(colorBuffer);

        viewPort.setBackgroundColor(ColorRGBA.BlackNoAlpha);
        viewPort.setOutputFrameBuffer(offBuffer);
        viewPort.setClearFlags(true, true, true);

        viewPort.attachScene(node);
        
        viewPort.addProcessor(fpp);

    }

    public Texture2D colorBuffer() {
        return colorBuffer;
    }

    public Camera cam() {
        return cam;
    }

    public ViewPort viewPort() {
        return viewPort;
    }

    public String name() {
        return name;
    }

    public void fov(float f) {
        cam.setFrustumPerspective(fov, aspect, near, far);
    }
    
    public void addFilter(Filter filter) {
        fpp.addFilter(filter);
    }
}
