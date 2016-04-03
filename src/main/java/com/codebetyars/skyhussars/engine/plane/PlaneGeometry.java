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
package com.codebetyars.skyhussars.engine.plane;

import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class PlaneGeometry {

    private final Node rootNode;
    private final Node cockpitNode;
    private final Node modelNode;
    private final Node soundNode;

    public static enum GeometryMode {

        COCKPIT_MODE, MODEL_MODE
    }

    public PlaneGeometry() {
        rootNode = new Node();
        cockpitNode = new Node();
        // rootNode.attachChild(cockpitNode);

        modelNode = new Node();
        soundNode = new Node();
        rootNode.attachChild(cockpitNode);
        rootNode.attachChild(modelNode);
        rootNode.attachChild(soundNode);
    }

    public void attachSpatialToCockpitNode(Spatial cockpit) {
        /*
         This is an ugly hack for now, the cockpit should be parameters from configuration
         */
        cockpitNode.attachChild(cockpit);
        cockpitNode.rotate(0, FastMath.PI, 0);
        cockpitNode.move(0.015f, -0.015f, 0.7f);
    }

    public void attachSpatialToRootNode(Spatial spatial) {
        soundNode.attachChild(spatial);
    }

    public void attachSpatialToModelNode(Spatial model) {
        modelNode.attachChild(model);
    }

    public synchronized void switchTo(GeometryMode geometryMode) {
        switch (geometryMode) {
            case COCKPIT_MODE:
                modelNode.setCullHint(Node.CullHint.Always);
                cockpitNode.setCullHint(Node.CullHint.Inherit);
                break;
            case MODEL_MODE:
                cockpitNode.setCullHint(Node.CullHint.Always);
                modelNode.setCullHint(Node.CullHint.Inherit);
                break;
        }
    }

    public Node rootNode() {
        return rootNode;
    }
    public Node modelNode() {
        return modelNode;
    }
}
