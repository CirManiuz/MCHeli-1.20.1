/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.wrapper.modelloader;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import mcheli.wrapper.modelloader.W_Face;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class W_GroupObject {
    public String name;
    public ArrayList<W_Face> faces = new ArrayList();
    public int glDrawingMode;

    public W_GroupObject() {
        this("");
    }

    public W_GroupObject(String name) {
        this(name, -1);
    }

    public W_GroupObject(String name, int glDrawingMode) {
        this.name = name;
        this.glDrawingMode = glDrawingMode;
    }

    public void render() {
        if (this.faces.size() > 0) {
            Tessellator tessellator = Tessellator.func_178181_a();
            BufferBuilder builder = tessellator.func_178180_c();
            builder.func_181668_a(this.glDrawingMode, DefaultVertexFormats.field_181710_j);
            this.render(tessellator);
            tessellator.func_78381_a();
        }
    }

    public void render(Tessellator tessellator) {
        if (this.faces.size() > 0) {
            for (W_Face face : this.faces) {
                face.addFaceForRender(tessellator);
            }
        }
    }

    public String toString() {
        return "W_GroupObject[size=" + this.faces.size() + ",values=[" + Joiner.on((char)'\n').join(this.faces) + "]]";
    }
}

