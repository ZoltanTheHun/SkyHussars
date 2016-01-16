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
