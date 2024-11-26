/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.entity.Entity
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  org.lwjgl.opengl.GL11
 */
package mcheli.particles;

import mcheli.MCH_Lib;
import mcheli.__helper.entity.ITargetMarkerObject;
import mcheli.multiplay.MCH_GuiTargetMarker;
import mcheli.particles.MCH_EntityParticleBase;
import mcheli.wrapper.W_Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class MCH_EntityParticleMarkPoint
extends MCH_EntityParticleBase
implements ITargetMarkerObject {
    final Team taem;

    public MCH_EntityParticleMarkPoint(World par1World, double x, double y, double z, Team team) {
        super(par1World, x, y, z, 0.0, 0.0, 0.0);
        this.setParticleMaxAge(30);
        this.taem = team;
    }

    public void func_189213_a() {
        this.field_187123_c = this.field_187126_f;
        this.field_187124_d = this.field_187127_g;
        this.field_187125_e = this.field_187128_h;
        EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
        if (player == null) {
            this.func_187112_i();
        } else if (player.func_96124_cp() == null && this.taem != null) {
            this.func_187112_i();
        } else if (player.func_96124_cp() != null && !player.func_184194_a(this.taem)) {
            this.func_187112_i();
        }
    }

    public void func_187112_i() {
        super.func_187112_i();
        MCH_Lib.DbgLog(true, "MCH_EntityParticleMarkPoint.setExpired : " + this, new Object[0]);
    }

    @Override
    public int func_70537_b() {
        return 3;
    }

    public void func_180434_a(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        double pz;
        double py;
        double px;
        double scale;
        GL11.glPushMatrix();
        Minecraft mc = Minecraft.func_71410_x();
        EntityPlayerSP player = mc.field_71439_g;
        if (player == null) {
            return;
        }
        double ix = field_70556_an;
        double iy = field_70554_ao;
        double iz = field_70555_ap;
        if (mc.field_71474_y.field_74320_O > 0 && entityIn != null) {
            Entity viewer = entityIn;
            double dist = W_Reflection.getThirdPersonDistance();
            float yaw = mc.field_71474_y.field_74320_O != 2 ? -viewer.field_70177_z : -viewer.field_70177_z;
            float pitch = mc.field_71474_y.field_74320_O != 2 ? -viewer.field_70125_A : -viewer.field_70125_A;
            Vec3d v = MCH_Lib.RotVec3(0.0, 0.0, -dist, yaw, pitch);
            if (mc.field_71474_y.field_74320_O == 2) {
                v = new Vec3d(-v.field_72450_a, -v.field_72448_b, -v.field_72449_c);
            }
            Vec3d vs = new Vec3d(viewer.field_70165_t, viewer.field_70163_u + (double)viewer.func_70047_e(), viewer.field_70161_v);
            RayTraceResult mop = entityIn.field_70170_p.func_72933_a(vs.func_72441_c(0.0, 0.0, 0.0), vs.func_72441_c(v.field_72450_a, v.field_72448_b, v.field_72449_c));
            double block_dist = dist;
            if (mop != null && mop.field_72313_a == RayTraceResult.Type.BLOCK && (block_dist = vs.func_72438_d(mop.field_72307_f) - 0.4) < 0.0) {
                block_dist = 0.0;
            }
            GL11.glTranslated((double)(v.field_72450_a * (block_dist / dist)), (double)(v.field_72448_b * (block_dist / dist)), (double)(v.field_72449_c * (block_dist / dist)));
            ix += v.field_72450_a * (block_dist / dist);
            iy += v.field_72448_b * (block_dist / dist);
            iz += v.field_72449_c * (block_dist / dist);
        }
        if ((scale = Math.sqrt((px = (double)((float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)partialTicks - ix))) * px + (py = (double)((float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)partialTicks - iy))) * py + (pz = (double)((float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)partialTicks - iz))) * pz) / 100.0) < 1.0) {
            scale = 1.0;
        }
        MCH_GuiTargetMarker.addMarkEntityPos(100, this, px / scale, py / scale, pz / scale, false);
        GL11.glPopMatrix();
    }

    @Override
    public double getX() {
        return this.field_187126_f;
    }

    @Override
    public double getY() {
        return this.field_187127_g;
    }

    @Override
    public double getZ() {
        return this.field_187128_h;
    }
}

