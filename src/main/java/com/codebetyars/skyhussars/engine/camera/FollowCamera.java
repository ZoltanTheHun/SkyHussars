/*
 * Copyright (c) 2017, ZoltanTheHun
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
package com.codebetyars.skyhussars.engine.camera;

import com.codebetyars.skyhussars.engine.ComplexCamera;
import com.codebetyars.skyhussars.engine.plane.PlaneGeometry;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class FollowCamera implements CameraBehaviour {

    private final Quaternion rotationX = new Quaternion();
    private final Quaternion rotationY = new Quaternion();

    @Override
    public CameraBehaviour updateCam(ComplexCamera cam, PlaneGeometry focus) {
        Vector3f cameraLocation = new Vector3f(0, 3.5f, -12);
        Node node = focus.root();
        cam.moveCameraTo((node.getLocalTranslation()).add(node.getLocalRotation().mult(rotationX).mult(rotationY).mult(cameraLocation)));
        cam.lookAt(node.getLocalTranslation(), node.getLocalRotation().mult(Vector3f.UNIT_Y));
        return this;
    }

    @Override
    public CameraBehaviour center() {
        rotationX.fromAngles(0, 0, 0);
        rotationY.fromAngles(0, 0, 0);
        return this;
    }

    @Override
    public CameraBehaviour rotateX(float value) {
        Quaternion rotation = new Quaternion();
        //naive approach!!!
        rotationX.multLocal(rotation.fromAngles(0, value, 0));
        return this;
    } 

    @Override
    public CameraBehaviour rotateY(float value) {
        Quaternion rotation = new Quaternion();
        //naive approach!!!
        rotationY.multLocal(rotation.fromAngles(value, 0, 0));
        return this;
    }

}
