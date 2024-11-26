/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.wrapper.modelloader;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class W_TextureCoordinate {
    public float u;
    public float v;
    public float w;

    public W_TextureCoordinate(float u, float v) {
        this(u, v, 0.0f);
    }

    public W_TextureCoordinate(float u, float v, float w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }
}

