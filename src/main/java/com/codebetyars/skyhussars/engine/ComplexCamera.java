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

import com.jme3.audio.Listener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComplexCamera {

    @Autowired
    private RenderManager renderManager;

    @Autowired
    private Camera nearCam;

    private Camera farCam;

    @Autowired
    private Listener listener;

    @Autowired
    private Node rootNode;

    private float fov;
    private final float farCamNear = 300f;
    private final float farCamFar = 200000f;
    private final float nearCamNear = 0.3f;
    private final float nearCamFar = 310f;
    private float aspect;

    private boolean initialized = false;

    public synchronized void init() {
        if (!initialized) {
            aspect = (float) nearCam.getWidth() / (float) nearCam.getHeight();

            farCam = new Camera(nearCam.getWidth(), nearCam.getHeight());
            farCam.setFrustumPerspective(fov, aspect, farCamNear, farCamFar);
            nearCam.setFrustumPerspective(fov, aspect, nearCamNear, nearCamFar);
            ViewPort viewPort = renderManager.createMainView("farMainView", nearCam);
            viewPort.setClearFlags(false, true, true);
            viewPort.attachScene(rootNode);
            fov(45);
            initialized = true;
        }
    }

    public synchronized void fov(float fov) {
        this.fov = fov;
        nearCam.setFrustumPerspective(fov, aspect, nearCamNear, farCamFar);
        farCam.setFrustumPerspective(fov, aspect, farCamNear, farCamFar);
    }

    public synchronized float fov() {
        return fov;
    }

    public synchronized void moveCameraTo(Vector3f location) {
        farCam.setLocation(location);
        nearCam.setLocation(location);
        listener.setLocation(location);
    }

    public synchronized void lookAtDirection(Vector3f direction, Vector3f up) {
        farCam.lookAtDirection(direction, up);
        nearCam.lookAtDirection(direction, up);
        listener.setRotation(nearCam.getRotation());
    }

    public synchronized void lookAt(Vector3f direction, Vector3f up) {
        farCam.lookAt(direction, up);
        nearCam.lookAt(direction, up);
        listener.setRotation(nearCam.getRotation());
    }
}
