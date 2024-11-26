/*
 * Decompiled with CFR 0.152.
 */
package mcheli.debug._v1.model;

import java.util.List;
import mcheli.__helper.debug.DebugInfoObject;
import mcheli.debug._v1.PrintStreamWrapper;
import mcheli.debug._v1.model._GroupObject;

public class ObjModel
implements DebugInfoObject {
    private List<_GroupObject> groupObjects;
    private int vertexNum;
    private int faceNum;

    public ObjModel(List<_GroupObject> groupObjects, int verts, int faces) {
        this.groupObjects = groupObjects;
        this.vertexNum = verts;
        this.faceNum = faces;
    }

    public String toString() {
        return "ObjModel[verts:" + this.vertexNum + ", face:" + this.faceNum + ", obj:" + this.groupObjects + "]";
    }

    @Override
    public void printInfo(PrintStreamWrapper stream) {
        stream.push("Obj Model Info:");
        this.groupObjects.forEach(g -> g.printInfo(stream));
        stream.pop();
        stream.println();
    }
}

