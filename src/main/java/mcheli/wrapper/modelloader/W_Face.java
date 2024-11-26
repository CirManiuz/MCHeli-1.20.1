/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.wrapper.modelloader;

import mcheli.wrapper.modelloader.W_TextureCoordinate;
import mcheli.wrapper.modelloader.W_Vertex;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class W_Face {
    public int[] verticesID;
    public W_Vertex[] vertices;
    public W_Vertex[] vertexNormals;
    public W_Vertex faceNormal;
    public W_TextureCoordinate[] textureCoordinates;

    public W_Face copy() {
        W_Face f = new W_Face();
        return f;
    }

    public void addFaceForRender(Tessellator tessellator) {
        this.addFaceForRender(tessellator, 0.0f);
    }

    public void addFaceForRender(Tessellator tessellator, float textureOffset) {
        int i;
        BufferBuilder builder = tessellator.func_178180_c();
        if (this.faceNormal == null) {
            this.faceNormal = this.calculateFaceNormal();
        }
        float averageU = 0.0f;
        float averageV = 0.0f;
        if (this.textureCoordinates != null && this.textureCoordinates.length > 0) {
            for (i = 0; i < this.textureCoordinates.length; ++i) {
                averageU += this.textureCoordinates[i].u;
                averageV += this.textureCoordinates[i].v;
            }
            averageU /= (float)this.textureCoordinates.length;
            averageV /= (float)this.textureCoordinates.length;
        }
        for (i = 0; i < this.vertices.length; ++i) {
            if (this.textureCoordinates != null && this.textureCoordinates.length > 0) {
                float offsetU = textureOffset;
                float offsetV = textureOffset;
                if (this.textureCoordinates[i].u > averageU) {
                    offsetU = -offsetU;
                }
                if (this.textureCoordinates[i].v > averageV) {
                    offsetV = -offsetV;
                }
                builder.func_181662_b((double)this.vertices[i].x, (double)this.vertices[i].y, (double)this.vertices[i].z).func_187315_a((double)(this.textureCoordinates[i].u + offsetU), (double)(this.textureCoordinates[i].v + offsetV));
            } else {
                builder.func_181662_b((double)this.vertices[i].x, (double)this.vertices[i].y, (double)this.vertices[i].z).func_187315_a(0.0, 0.0);
            }
            if (this.vertexNormals != null && i < this.vertexNormals.length) {
                builder.func_181663_c(this.vertexNormals[i].x, this.vertexNormals[i].y, this.vertexNormals[i].z).func_181675_d();
                continue;
            }
            builder.func_181663_c(this.faceNormal.x, this.faceNormal.y, this.faceNormal.z).func_181675_d();
        }
    }

    public W_Vertex calculateFaceNormal() {
        Vec3d v1 = new Vec3d((double)(this.vertices[1].x - this.vertices[0].x), (double)(this.vertices[1].y - this.vertices[0].y), (double)(this.vertices[1].z - this.vertices[0].z));
        Vec3d v2 = new Vec3d((double)(this.vertices[2].x - this.vertices[0].x), (double)(this.vertices[2].y - this.vertices[0].y), (double)(this.vertices[2].z - this.vertices[0].z));
        Vec3d normalVector = v1.func_72431_c(v2).func_72432_b();
        return new W_Vertex((float)normalVector.field_72450_a, (float)normalVector.field_72448_b, (float)normalVector.field_72449_c);
    }

    public String toString() {
        return "W_Face[id:" + this.verticesID + "]";
    }
}

