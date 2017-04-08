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
import java.util.function.UnaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CameraManager {

    private final static Logger logger = LoggerFactory.getLogger(CameraManager.class);

    public static enum CameraMode {

        COCKPIT_VIEW(GeometryMode.COCKPIT_MODE, CameraManager::showCockpit), OUTER_VIEW(GeometryMode.MODEL_MODE, CameraManager::follow);

        CameraMode(GeometryMode geometryMode, UnaryOperator<CameraManager> updateCam) {
            this.geometryMode = geometryMode;
            this.updateCam = updateCam;
        }

        public final GeometryMode geometryMode;
        public final UnaryOperator<CameraManager> updateCam;
    }

    @Autowired
    private ComplexCamera camera;

    private CameraMode cameraMode = CameraMode.OUTER_VIEW;

    private final int minFov = 20;
    private final int maxFov = 100;
    private final float fovChangeRate = 12f;
    private boolean disableCameraRotation = false;
    private FovMode fovMode = FovMode.STABLE;

    private PlaneGeometry focus;

    private final Quaternion rotationX = new Quaternion();
    private final Quaternion rotationY = new Quaternion();

    public void update(float tpf) {
        cameraMode.updateCam.apply(this);
        updateFov(tpf);
    }

    private void updateFov(float tpf) {
        switch (fovMode) {
            case DECREASE:
                if (camera.fov() > minFov) {
                    fov(camera.fov() - fovChangeRate * tpf);
                }
                break;
            case INCREASE:
                if (camera.fov() < maxFov) {
                    fov(camera.fov() + fovChangeRate * tpf);
                }
                break;
        }
    }

    public static CameraManager follow(CameraManager cm) {
        Vector3f cameraLocation = new Vector3f(0, 3.5f, -12);
        Node node = cm.focus.root();
        cm.camera.moveCameraTo((node.getLocalTranslation()).add(node.getLocalRotation().mult(cameraLocation)));
        cm.camera.lookAt(node.getLocalTranslation(), node.getLocalRotation().mult(Vector3f.UNIT_Y));
        return cm;
    }

    public static CameraManager showCockpit(CameraManager cm) {
        cm.camera.moveCameraTo(cm.focus.root().getLocalTranslation());
        Node node = cm.focus.root();
        cm.camera.lookAtDirection(node.getLocalRotation().mult(cm.rotationX).
                mult(cm.rotationY).mult(Vector3f.UNIT_Z), node.getLocalRotation().
                mult(Vector3f.UNIT_Y));
        return cm;
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

    public void setFovMode(FovMode fovMode) {
        this.fovMode = fovMode;
    }

    public void moveCameraTo(Vector3f location) {
        camera.moveCameraTo(location);
    }

    public void switchToView(CameraMode view) {
        focus.switchTo(view.geometryMode);
        this.cameraMode = view;
    }

    private final float DEG170 = 2.96706f;

    public enum CameraPlane {

        X, Y
    };

    public enum FovMode {
        INCREASE, DECREASE, STABLE
    }

    public void rotateCamera(CameraPlane p, float value, float tpf) {
        if (!disableCameraRotation) {
            switch (p) {
                case X:
                    rotateCameraX(value, tpf);
                    break;
                case Y:
                    rotateCameraY(value, tpf);
                    break;
            }
        }
    }

    private void rotateCameraX(float value, float tpf) {
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

    private void rotateCameraY(float value, float tpf) {
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

    public void disableCameraRotation(boolean state) {
        this.disableCameraRotation = state;
    }
}
