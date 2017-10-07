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
package skyhussars.engine.plane;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaneGeometry {

    private final static Logger logger = LoggerFactory.getLogger(PlaneGeometry.class);
    private final Node root;
    private final Node cockpitNode;
    private final Node outside;
    private final Node soundNode;
    private Node airspeedInd;

    public static enum GeometryMode {

        COCKPIT_MODE, MODEL_MODE
    }

    public PlaneGeometry() {
        root = new Node();
        cockpitNode = new Node();
        // rootNode.attachChild(cockpitNode);

        outside = new Node();
        soundNode = new Node();
        root.attachChild(cockpitNode);
        root.attachChild(outside);
        root.attachChild(soundNode);
    }

    public PlaneGeometry attachSpatialToCockpitNode(Spatial cockpit) {
        if (cockpit instanceof Geometry) {
            Geometry geom = (Geometry) cockpit;
            logger.info("Geom is:" + geom.getName());
        } 
        if (cockpit instanceof Node) {
            Node geom = (Node) cockpit;
            logger.info("Node is:" + geom.getName());
            if(geom.getChildren() != null) {
                Optional<Spatial> spd = geom.getChildren().stream().filter(s -> "spd".equals(s.getName())).findFirst();
                Optional<Spatial> ind = spd.map(s -> {
                   List<Spatial> a = ((Node) s).getChildren();
                   Optional<Spatial> b = a.stream().filter(t -> "indicator".equals(t.getName())).findFirst();
                   return b;
                }).orElse(Optional.empty());
                ind.map(i -> {
                    airspeedInd = (Node)i;
                    return i;
                });
                
            }
        } 
        if (cockpit instanceof Geometry) {
            Geometry geom = (Geometry) cockpit;
            System.out.println(geom.getName());
        } 
        cockpitNode.attachChild(cockpit);
        return this;
    }
    
    public Node airspeedInd(){
        return airspeedInd;
    }

    public PlaneGeometry attachSpatialToRootNode(Spatial spatial) {
        soundNode.attachChild(spatial);
        return this;
    }

    public PlaneGeometry attachSpatialToModelNode(Spatial model) {
        outside.attachChild(model);
        return this;
    }

    public synchronized void switchTo(GeometryMode geometryMode) {
        switch (geometryMode) {
            case COCKPIT_MODE:
                outside.setCullHint(Node.CullHint.Always);
                cockpitNode.setCullHint(Node.CullHint.Inherit);
                break;
            case MODEL_MODE:
                cockpitNode.setCullHint(Node.CullHint.Always);
                outside.setCullHint(Node.CullHint.Inherit);
                break;
        }
    }

    public Node root() {return root;}
    public Node outside() {return outside;}  
    public Vector3f translation(){return root.getLocalTranslation();}
    public Vector3f forwardNormal(){return root.getLocalRotation().mult(Vector3f.UNIT_Z).normalize();}
    public Vector3f upNormal(){return root.getLocalRotation().mult(Vector3f.UNIT_Y).normalize();}
}
