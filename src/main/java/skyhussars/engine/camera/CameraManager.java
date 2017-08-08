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
package skyhussars.engine.camera;

import skyhussars.engine.ComplexCamera;
import skyhussars.engine.plane.PlaneGeometry;
import skyhussars.engine.plane.PlaneGeometry.GeometryMode;
import com.jme3.math.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CameraManager {

    private final static Logger logger = LoggerFactory.getLogger(CameraManager.class);

    public static enum CameraMode {

        COCKPIT_VIEW(GeometryMode.COCKPIT_MODE, new CockpitCamera()), OUTER_VIEW(GeometryMode.MODEL_MODE, new FollowCamera());

        CameraMode(GeometryMode geometryMode, CameraBehaviour behaviour) {
            this.geometryMode = geometryMode;
            this.behaviour = behaviour;
        }

        public final GeometryMode geometryMode;
        public final CameraBehaviour behaviour;
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

    public void update(float tpf) {
        cameraMode.behaviour.updateCam(camera, focus);
        updateFov(tpf);
    }

    private void updateFov(float tpf) {
        if(FovMode.STABLE != fovMode) fov(camera.fov() + fovMode.sign * fovChangeRate * tpf);
    }

    public synchronized void followWithCamera(PlaneGeometry planeGeometry) {
        focus = planeGeometry;
        switchToView(cameraMode);
    }

    public void fov(float fov) {
        if(fov < minFov) fov = minFov;
        if(fov > maxFov) fov = maxFov;
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

    public enum CameraPlane {

        X, Y
    };

    public enum FovMode {

        INCREASE(1), DECREASE(-1), STABLE(0);
        
        FovMode(int dir){
            this.sign = dir;
        }
        
        public final int sign;
    }

    public void rotateCamera(CameraPlane p, float value, float tpf) {
        if (!disableCameraRotation) {
            switch (p) {
                case X:
                    cameraMode.behaviour.rotateX(value);
                    break;
                case Y:
                    cameraMode.behaviour.rotateY(value);
                    break;
            }
        }
    }

    public void centerCamera() {
        cameraMode.behaviour.center();
    }

    public void disableCameraRotation(boolean state) {
        this.disableCameraRotation = state;
    }
}
