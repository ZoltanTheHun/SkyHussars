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

import com.codebetyars.skyhussars.engine.plane.PlaneGeometry;
import com.codebetyars.skyhussars.engine.plane.PlaneGeometry.GeometryMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CameraManager {

    public static enum CameraMode {

        COCKPIT_VIEW(GeometryMode.COCKPIT_MODE), OUTER_VIEW(GeometryMode.MODEL_MODE);

        CameraMode(GeometryMode geometryMode) {
            this.geometryMode = geometryMode;
        }

        public final GeometryMode geometryMode;
    }

    @Autowired
    private ComplexCamera camera;

    private CameraMode cameraMode = CameraMode.OUTER_VIEW;

    private boolean fovChangeActive;
    private boolean fovNarrowing;
    private final int minFov = 20;
    private final int maxFov = 100;
    private final float fovChangeRate = 12f;

    private PlaneGeometry focus;

    private final Quaternion rotationX = new Quaternion();
    private final Quaternion rotationY = new Quaternion();

    public void update(float tpf) {
        switch (cameraMode) {
            case OUTER_VIEW:
                follow();
                break;
            case COCKPIT_VIEW:
                showCockpit();
                break;
        }
        updateFov(tpf);
    }

    private void updateFov(float tpf) {
        if (fovChangeActive) {
            if (fovNarrowing && camera.fov() > minFov) {
                fov(camera.fov() - fovChangeRate * tpf);
            }
            if (!fovNarrowing && camera.fov() < maxFov) {
                fov(camera.fov() + fovChangeRate * tpf);
            }
        }
    }

    private void follow() {
        Vector3f cameraLocation = new Vector3f(0, 3.5f, -12);
        Node node = focus.rootNode();
        camera.moveCameraTo((node.getLocalTranslation()).add(node.getLocalRotation().mult(cameraLocation)));
        camera.lookAt(node.getLocalTranslation(), node.getLocalRotation().mult(Vector3f.UNIT_Y));
    }

    private void showCockpit() {
        camera.moveCameraTo(focus.rootNode().getLocalTranslation());
        Node node = focus.rootNode();
        camera.lookAtDirection(node.getLocalRotation().mult(rotationX).
                mult(rotationY).mult(Vector3f.UNIT_Z), node.getLocalRotation().
                mult(Vector3f.UNIT_Y));
    }

    public void init() {
        camera.init();
        if (focus != null) {
            follow();
        }
    }

    public synchronized void followWithCamera(PlaneGeometry planeGeometry) {
        focus = planeGeometry;
        switchToView(cameraMode);
    }

    public void fov(float fov) {
        camera.fov(fov);
    }

    public float fov() {
        return camera.fov();
    }

    public void setFovChangeActive(boolean fovChangeActive) {
        this.fovChangeActive = fovChangeActive;
    }

    public void setFovChangeActive(boolean active, boolean fovNarrowing) {
        this.fovChangeActive = active;
        this.fovNarrowing = fovNarrowing;
    }

    public void moveCameraTo(Vector3f location) {
        camera.moveCameraTo(location);
    }

    public void switchToView(CameraMode view) {
        focus.switchTo(view.geometryMode);
        this.cameraMode = view;
    }

    private final float DEG170 = 2.96706f;

    public void rotateCameraX(float value, float tpf) {
        if (cameraMode == CameraMode.COCKPIT_VIEW) {
            Quaternion rotation = new Quaternion();
            //naive approach!!!
            rotationX.multLocal(rotation.fromAngles(0, value, 0));
            float[] angles = rotationX.toAngles(null);
            if (angles[1] > DEG170) {
                rotationX.fromAngles(0, DEG170, 0);
            } else if (angles[1] < -DEG170) {
                rotationX.fromAngles(0, -DEG170, 0);
            }
        }
    }

    private final float DEG80 = 1.39626f;

    public void rotateCameraY(float value, float tpf) {
        if (cameraMode == CameraMode.COCKPIT_VIEW) {
            Quaternion rotation = new Quaternion();
            //naive approach!!!
            rotationY.multLocal(rotation.fromAngles(value, 0, 0));
            float[] angles = rotationY.toAngles(null);
            if (angles[0] > DEG80) {
                rotationY.fromAngles(DEG80, 0, 0);
            } else if (angles[0] < -DEG80) {
                rotationY.fromAngles(-DEG80, 0, 0);
            }
        }
    }

    public void centerCamera() {
        rotationX.fromAngles(0, 0, 0);
        rotationY.fromAngles(0, 0, 0);
    }
}
