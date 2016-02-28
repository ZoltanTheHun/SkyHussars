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

import com.jme3.input.FlyByCamera;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CameraManager {

    @Autowired
    private Camera camera;

    @Autowired
    private FlyByCamera flyByCamera;

    private boolean fovChangeActive;
    private boolean fovNarrowing;
    private final int minFov = 20;
    private final int maxFov = 100;
    private float fovChangeRate = 8f;

    public Spatial focus;

    public void update(float tpf) {
        follow();
        updateFov(tpf);
    }

    private void updateFov(float tpf) {
        if (fovChangeActive) {
            if (fovNarrowing && fov > minFov) {
                setFov(fov - fovChangeRate * tpf);
            }
            if (!fovNarrowing && fov < maxFov) {
                setFov(fov + fovChangeRate * tpf);
            }
        }
    }

    private void follow() {
        Vector3f cameraLocation = new Vector3f(0, 3.5f, -12);
        camera.setLocation((focus.getWorldTranslation()).add(focus.getLocalRotation().mult(cameraLocation)));
        camera.lookAt(focus.getWorldTranslation(), focus.getLocalRotation().mult(Vector3f.UNIT_Y));
    }

    public void init() {
        if (focus != null) {
            follow();
        }
    }

    public void followWithCamera(Spatial spatial) {
        this.focus = spatial;
    }
    private float aspect;
    private float fov;
    private float near = 0.1f;
    private float far = 200000f;

    public void initializeCamera() {
        aspect = (float) camera.getWidth() / (float) camera.getHeight();
        setFov(45);
        flyByCamera.setMoveSpeed(200);
    }

    public void setFov(float fov) {
        this.fov = fov;
        camera.setFrustumPerspective(fov, aspect, near, far);
    }

    public float getFov() {
        return fov;
    }

    public void setFovChangeActive(boolean fovChangeActive) {
        this.fovChangeActive = fovChangeActive;
    }

    public void setFovChangeActive(boolean active, boolean fovNarrowing) {
        this.fovChangeActive = active;
        this.fovNarrowing = fovNarrowing;
    }

    public void moveCameraTo(Vector3f location) {
        camera.setLocation(location);
    }

    public void flyCamActive(boolean cursor) {
        flyByCamera.setEnabled(!cursor);
    }

    public boolean flyCamActive() {
        return flyByCamera.isEnabled();
    }
}
