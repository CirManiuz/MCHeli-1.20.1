/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 */
package mcheli.debug._v1.model;

import java.util.Arrays;
import java.util.List;
import mcheli.__helper.debug.DebugInfoObject;
import mcheli.debug._v1.PrintStreamWrapper;
import mcheli.debug._v1.model._TextureCoord;
import mcheli.debug._v1.model._Vertex;
import net.minecraft.util.math.Vec3d;

class _Face
implements DebugInfoObject {
    private int[] verticesID;
    private _Vertex[] vertices;
    private _Vertex[] vertexNormals;
    private _TextureCoord[] textureCoordinates;
    private _Vertex faceNormal;

    _Face(int[] ids, _Vertex[] verts, _TextureCoord[] texCoords) {
        this(ids, verts, verts, texCoords);
    }

    _Face(int[] ids, _Vertex[] verts, _Vertex[] normals, _TextureCoord[] texCoords) {
        this.verticesID = ids;
        this.vertices = verts;
        this.vertexNormals = normals;
        this.textureCoordinates = texCoords;
        this.faceNormal = _Face.calculateFaceNormal(verts);
    }

    private static _Vertex calculateFaceNormal(_Vertex[] verts) {
        Vec3d v1 = new Vec3d((double)(verts[1].x - verts[0].x), (double)(verts[1].y - verts[0].y), (double)(verts[1].z - verts[0].z));
        Vec3d v2 = new Vec3d((double)(verts[2].x - verts[0].x), (double)(verts[2].y - verts[0].y), (double)(verts[2].z - verts[0].z));
        Vec3d normalVector = v1.func_72431_c(v2).func_72432_b();
        return new _Vertex((float)normalVector.field_72450_a, (float)normalVector.field_72448_b, (float)normalVector.field_72449_c);
    }

    _Face calcVerticesNormal(List<_Face> faces, boolean shading, double facet) {
        _Vertex[] vnormals = new _Vertex[this.verticesID.length];
        for (int i = 0; i < this.verticesID.length; ++i) {
            _Vertex vn = _Face.getVerticesNormalFromFace(this.faceNormal, this.verticesID[i], faces, (float)facet);
            vn = vn.normalize();
            if (shading) {
                if ((double)(this.faceNormal.x * vn.x + this.faceNormal.y * vn.y + this.faceNormal.z * vn.z) >= facet) {
                    vnormals[i] = vn;
                    continue;
                }
                vnormals[i] = this.faceNormal;
                continue;
            }
            vnormals[i] = this.faceNormal;
        }
        return new _Face(this.verticesID, this.vertices, vnormals, this.textureCoordinates);
    }

    private static _Vertex getVerticesNormalFromFace(_Vertex fnormal, int verticesID, List<_Face> faces, float facet) {
        _Vertex v = new _Vertex(0.0f, 0.0f, 0.0f);
        block0: for (_Face f : faces) {
            for (int id : f.verticesID) {
                if (id != verticesID) continue;
                if (f.faceNormal.x * fnormal.x + f.faceNormal.y * fnormal.y + f.faceNormal.z * fnormal.z < facet) continue block0;
                v = v.add(f.faceNormal);
                continue block0;
            }
        }
        v = v.normalize();
        return v;
    }

    @Override
    public void printInfo(PrintStreamWrapper stream) {
        stream.println("F: [");
        stream.push();
        stream.println("ids: " + Arrays.toString(this.verticesID));
        stream.println("--- verts");
        Arrays.stream(this.vertices).forEach(v -> v.printInfo(stream));
        stream.println("--- normals");
        Arrays.stream(this.vertexNormals).forEach(n -> n.printInfo(stream));
        stream.println("--- tex coords");
        Arrays.stream(this.textureCoordinates).forEach(t -> t.printInfo(stream));
        stream.println("--- face normal");
        this.faceNormal.printInfo(stream);
        stream.pop();
        stream.println("]");
    }
}

