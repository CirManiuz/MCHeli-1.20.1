/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 */
package mcheli.aircraft;

import java.util.ArrayList;
import java.util.List;
import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class MCH_BoundingBox {
    private AxisAlignedBB boundingBox;
    public final double offsetX;
    public final double offsetY;
    public final double offsetZ;
    public final float width;
    public final float height;
    public Vec3d rotatedOffset;
    public List<Vec3d> pos = new ArrayList<Vec3d>();
    public final float damegeFactor;

    public MCH_BoundingBox(double x, double y, double z, float w, float h, float df) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        this.width = w;
        this.height = h;
        this.damegeFactor = df;
        this.boundingBox = new AxisAlignedBB(x - (double)(w / 2.0f), y - (double)(h / 2.0f), z - (double)(w / 2.0f), x + (double)(w / 2.0f), y + (double)(h / 2.0f), z + (double)(w / 2.0f));
        this.updatePosition(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f);
    }

    public void add(double x, double y, double z) {
        this.pos.add(0, new Vec3d(x, y, z));
        while (this.pos.size() > MCH_Config.HitBoxDelayTick.prmInt + 2) {
            this.pos.remove(MCH_Config.HitBoxDelayTick.prmInt + 2);
        }
    }

    public MCH_BoundingBox copy() {
        return new MCH_BoundingBox(this.offsetX, this.offsetY, this.offsetZ, this.width, this.height, this.damegeFactor);
    }

    public void updatePosition(double posX, double posY, double posZ, float yaw, float pitch, float roll) {
        Vec3d v = new Vec3d(this.offsetX, this.offsetY, this.offsetZ);
        this.rotatedOffset = MCH_Lib.RotVec3(v, -yaw, -pitch, -roll);
        this.add(posX + this.rotatedOffset.field_72450_a, posY + this.rotatedOffset.field_72448_b, posZ + this.rotatedOffset.field_72449_c);
        int index = MCH_Config.HitBoxDelayTick.prmInt;
        Vec3d cp = index + 0 < this.pos.size() ? this.pos.get(index + 0) : this.pos.get(this.pos.size() - 1);
        Vec3d pp = index + 1 < this.pos.size() ? this.pos.get(index + 1) : this.pos.get(this.pos.size() - 1);
        double sx = ((double)this.width + Math.abs(cp.field_72450_a - pp.field_72450_a)) / 2.0;
        double sy = ((double)this.height + Math.abs(cp.field_72448_b - pp.field_72448_b)) / 2.0;
        double sz = ((double)this.width + Math.abs(cp.field_72449_c - pp.field_72449_c)) / 2.0;
        double x = (cp.field_72450_a + pp.field_72450_a) / 2.0;
        double y = (cp.field_72448_b + pp.field_72448_b) / 2.0;
        double z = (cp.field_72449_c + pp.field_72449_c) / 2.0;
        this.boundingBox = new AxisAlignedBB(x - sx, y - sy, z - sz, x + sx, y + sy, z + sz);
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }
}

