/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.lwjgl.BufferUtils
 */
package mcheli;

import java.nio.FloatBuffer;
import javax.annotation.Nullable;
import mcheli.__helper.entity.ITargetMarkerObject;
import org.lwjgl.BufferUtils;

public class MCH_MarkEntityPos {
    public FloatBuffer pos;
    public int type;
    private ITargetMarkerObject target;

    public MCH_MarkEntityPos(int type, ITargetMarkerObject target) {
        this.type = type;
        this.pos = BufferUtils.createFloatBuffer((int)3);
        this.target = target;
    }

    public MCH_MarkEntityPos(int type) {
        this(type, null);
    }

    @Nullable
    public ITargetMarkerObject getTarget() {
        return this.target;
    }
}

