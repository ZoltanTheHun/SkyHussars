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

public class CameraManager {

    private Camera camera;
    private FlyByCamera flyCam;

    public CameraManager(Camera camera, FlyByCamera flyCam) {
        this.camera = camera;
        this.flyCam = flyCam;
    }

    public void followWithCamera(Spatial spatial) {
        Vector3f cameraLocation = new Vector3f(0, 3f, -12);
        camera.setLocation((spatial.getWorldTranslation()).add(spatial.getLocalRotation().mult(cameraLocation)));
        camera.lookAt(spatial.getWorldTranslation(),
                spatial.getLocalRotation().mult(Vector3f.UNIT_Y));
    }

    public void initializeCamera() {
        float aspect = (float) camera.getWidth() / (float) camera.getHeight();
        //jmonkey has 45 degrees default FOV
        float fov = 90;
        camera.setFrustumPerspective(fov, aspect, 0.1f, 200000);
        flyCam.setMoveSpeed(200);
    }

    public void moveCameraTo(Vector3f location) {
        camera.setLocation(location);
    }

    public void flyCamActive(boolean cursor) {
        flyCam.setEnabled(!cursor);
    }

    public boolean flyCamActive() {
        return flyCam.isEnabled();
    }
}
