/*
 * Decompiled with CFR 0.152.
 */
package mcheli;

public class MCH_Color {
    public float a;
    public float r;
    public float g;
    public float b;

    public MCH_Color(float aa, float rr, float gg, float bb) {
        this.a = this.round(aa);
        this.r = this.round(rr);
        this.g = this.round(gg);
        this.b = this.round(bb);
    }

    public MCH_Color(float rr, float gg, float bb) {
        this(1.0f, rr, gg, bb);
    }

    public MCH_Color() {
        this(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public float round(float f) {
        return f > 1.0f ? 1.0f : (f < 0.0f ? 0.0f : f);
    }
}

