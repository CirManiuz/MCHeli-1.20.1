/*
 * Decompiled with CFR 0.152.
 */
package mcheli.wrapper.modelloader;

import mcheli.__helper.client._IModelCustom;
import mcheli.wrapper.modelloader.W_Vertex;

public abstract class W_ModelCustom
implements _IModelCustom {
    public float min = 100000.0f;
    public float minX = 100000.0f;
    public float minY = 100000.0f;
    public float minZ = 100000.0f;
    public float max = -100000.0f;
    public float maxX = -100000.0f;
    public float maxY = -100000.0f;
    public float maxZ = -100000.0f;
    public float size = 0.0f;
    public float sizeX = 0.0f;
    public float sizeY = 0.0f;
    public float sizeZ = 0.0f;

    public void checkMinMax(W_Vertex v) {
        if (v.x < this.minX) {
            this.minX = v.x;
        }
        if (v.y < this.minY) {
            this.minY = v.y;
        }
        if (v.z < this.minZ) {
            this.minZ = v.z;
        }
        if (v.x > this.maxX) {
            this.maxX = v.x;
        }
        if (v.y > this.maxY) {
            this.maxY = v.y;
        }
        if (v.z > this.maxZ) {
            this.maxZ = v.z;
        }
    }

    public void checkMinMaxFinal() {
        if (this.minX < this.min) {
            this.min = this.minX;
        }
        if (this.minY < this.min) {
            this.min = this.minY;
        }
        if (this.minZ < this.min) {
            this.min = this.minZ;
        }
        if (this.maxX > this.max) {
            this.max = this.maxX;
        }
        if (this.maxY > this.max) {
            this.max = this.maxY;
        }
        if (this.maxZ > this.max) {
            this.max = this.maxZ;
        }
        this.sizeX = this.maxX - this.minX;
        this.sizeY = this.maxY - this.minY;
        this.sizeZ = this.maxZ - this.minZ;
        this.size = this.max - this.min;
    }

    public abstract boolean containsPart(String var1);

    public abstract void renderAll(int var1, int var2);

    public abstract void renderAllLine(int var1, int var2);

    public abstract int getVertexNum();

    public abstract int getFaceNum();
}

