/*
 * Decompiled with CFR 0.152.
 */
package mcheli.debug._v1.model;

import mcheli.__helper.debug.DebugInfoObject;
import mcheli.debug._v1.PrintStreamWrapper;

class _TextureCoord
implements DebugInfoObject {
    public final float u;
    public final float v;
    public final float w;

    public _TextureCoord(float u, float v) {
        this(u, v, 0.0f);
    }

    public _TextureCoord(float u, float v, float w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    @Override
    public void printInfo(PrintStreamWrapper stream) {
        stream.println(String.format("T: [%.6f, %.6f, %.6f]", Float.valueOf(this.u), Float.valueOf(this.v), Float.valueOf(this.w)));
    }
}

