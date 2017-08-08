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
package skyhussars.engine.camera;

import skyhussars.engine.ComplexCamera;
import skyhussars.engine.plane.PlaneGeometry;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class CockpitCamera implements CameraBehaviour{

    private final Quaternion rotationX = new Quaternion();
    private final Quaternion rotationY = new Quaternion();

    @Override
    public CameraBehaviour center() {
        rotationX.fromAngles(0, 0, 0);
        rotationY.fromAngles(0, 0, 0);
        return this;
    }

    private final float DEG80 = 1.39626f;
    private final float DEG170 = 2.96706f;

    @Override
    public CockpitCamera rotateY(float value) {
        Quaternion rotation = new Quaternion();
        //naive approach!!!
        rotationY.multLocal(rotation.fromAngles(value, 0, 0));
        float[] angles = rotationY.toAngles(null);
        if (angles[0] > DEG80) {
            rotationY.fromAngles(DEG80, 0, 0);
        } else if (angles[0] < -DEG80) {
            rotationY.fromAngles(-DEG80, 0, 0);
        }
        return this;
    }

    @Override
    public CameraBehaviour rotateX(float value) {
        Quaternion rotation = new Quaternion();
        //naive approach!!!
        rotationX.multLocal(rotation.fromAngles(0, value, 0));
        float[] angles = rotationX.toAngles(null);
        if (angles[1] > DEG170) {
            rotationX.fromAngles(0, DEG170, 0);
        } else if (angles[1] < -DEG170) {
            rotationX.fromAngles(0, -DEG170, 0);
        }
        return this;
    }
    
    @Override
    public CockpitCamera updateCam(ComplexCamera cam, PlaneGeometry focus){
        Vector3f cameraLocation = new Vector3f(0, 0.8f, 1.6f);
        Node node = focus.root();
        cam.moveCameraTo(focus.root().getLocalTranslation().add(node.getLocalRotation().mult(cameraLocation)));
        cam.lookAtDirection(node.getLocalRotation().mult(rotationX).
                mult(rotationY).mult(Vector3f.UNIT_Z), node.getLocalRotation().
                mult(Vector3f.UNIT_Y));
        return this;
    }

}
