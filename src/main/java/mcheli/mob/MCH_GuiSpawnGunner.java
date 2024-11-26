/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.mob;

import java.util.List;
import mcheli.MCH_MOD;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.gui.MCH_Gui;
import mcheli.mob.MCH_EntityGunner;
import mcheli.mob.MCH_ItemSpawnGunner;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_GuiSpawnGunner
extends MCH_Gui {
    public MCH_GuiSpawnGunner(Minecraft minecraft) {
        super(minecraft);
    }

    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
    }

    @Override
    public boolean func_73868_f() {
        return false;
    }

    @Override
    public boolean isDrawGui(EntityPlayer player) {
        return player != null && player.field_70170_p != null && !player.func_184614_ca().func_190926_b() && player.func_184614_ca().func_77973_b() instanceof MCH_ItemSpawnGunner;
    }

    @Override
    public void drawGui(EntityPlayer player, boolean isThirdPersonView) {
        if (isThirdPersonView) {
            return;
        }
        if (!this.isDrawGui(player)) {
            return;
        }
        GL11.glLineWidth((float)scaleFactor);
        GL11.glDisable((int)3042);
        this.draw(player, this.searchTarget(player));
    }

    private Entity searchTarget(EntityPlayer player) {
        float f = 1.0f;
        float pitch = player.field_70127_C + (player.field_70125_A - player.field_70127_C) * f;
        float yaw = player.field_70126_B + (player.field_70177_z - player.field_70126_B) * f;
        double dx = player.field_70169_q + (player.field_70165_t - player.field_70169_q) * (double)f;
        double dy = player.field_70167_r + (player.field_70163_u - player.field_70167_r) * (double)f + (double)player.func_70047_e();
        double dz = player.field_70166_s + (player.field_70161_v - player.field_70166_s) * (double)f;
        Vec3d vec3 = new Vec3d(dx, dy, dz);
        float f3 = MathHelper.func_76134_b((float)(-yaw * ((float)Math.PI / 180) - (float)Math.PI));
        float f4 = MathHelper.func_76126_a((float)(-yaw * ((float)Math.PI / 180) - (float)Math.PI));
        float f5 = -MathHelper.func_76134_b((float)(-pitch * ((float)Math.PI / 180)));
        float f6 = MathHelper.func_76126_a((float)(-pitch * ((float)Math.PI / 180)));
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0;
        Vec3d vec31 = vec3.func_72441_c((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
        Object target = null;
        List list = player.field_70170_p.func_72872_a(MCH_EntityGunner.class, player.func_174813_aQ().func_72314_b(5.0, 5.0, 5.0));
        for (int i = 0; i < list.size(); ++i) {
            MCH_EntityGunner gunner = (MCH_EntityGunner)((Object)list.get(i));
            if (gunner.func_174813_aQ().func_72327_a(vec3, vec31) == null || target != null && !(player.func_70068_e((Entity)gunner) < player.func_70068_e((Entity)target))) continue;
            target = gunner;
        }
        if (target != null) {
            return target;
        }
        MCH_ItemSpawnGunner item = (MCH_ItemSpawnGunner)player.func_184614_ca().func_77973_b();
        if (item.targetType == 1 && !player.field_70170_p.field_72995_K && player.func_96124_cp() == null) {
            return null;
        }
        List list1 = player.field_70170_p.func_72872_a(MCH_EntitySeat.class, player.func_174813_aQ().func_72314_b(5.0, 5.0, 5.0));
        for (int i = 0; i < list1.size(); ++i) {
            MCH_EntitySeat seat = (MCH_EntitySeat)list1.get(i);
            if (seat.getParent() == null || seat.getParent().getAcInfo() == null || seat.func_174813_aQ().func_72327_a(vec3, vec31) == null || target != null && !(player.func_70068_e((Entity)seat) < player.func_70068_e((Entity)target))) continue;
            target = seat.getRiddenByEntity() instanceof MCH_EntityGunner ? seat.getRiddenByEntity() : seat;
        }
        if (target == null) {
            List list2 = player.field_70170_p.func_72872_a(MCH_EntityAircraft.class, player.func_174813_aQ().func_72314_b(5.0, 5.0, 5.0));
            for (int i = 0; i < list2.size(); ++i) {
                MCH_EntityAircraft ac = (MCH_EntityAircraft)list2.get(i);
                if (ac.isUAV() || ac.getAcInfo() == null || ac.func_174813_aQ().func_72327_a(vec3, vec31) == null || target != null && !(player.func_70068_e((Entity)ac) < player.func_70068_e((Entity)target))) continue;
                target = ac.getRiddenByEntity() instanceof MCH_EntityGunner ? ac.getRiddenByEntity() : ac;
            }
        }
        return target;
    }

    void draw(EntityPlayer player, Entity entity) {
        double size;
        if (entity == null) {
            return;
        }
        GL11.glEnable((int)3042);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        int srcBlend = GL11.glGetInteger((int)3041);
        int dstBlend = GL11.glGetInteger((int)3040);
        GL11.glBlendFunc((int)770, (int)771);
        for (size = 512.0; size < (double)this.field_146294_l || size < (double)this.field_146295_m; size *= 2.0) {
        }
        GL11.glBlendFunc((int)srcBlend, (int)dstBlend);
        GL11.glDisable((int)3042);
        double factor = size / 512.0;
        double SCALE_FACTOR = (double)scaleFactor * factor;
        double CX = this.field_146297_k.field_71443_c / 2;
        double CY = this.field_146297_k.field_71440_d / 2;
        double px = (CX - 0.0) / SCALE_FACTOR;
        double py = (CY + 0.0) / SCALE_FACTOR;
        GL11.glPushMatrix();
        if (entity instanceof MCH_EntityGunner) {
            MCH_EntityGunner gunner = (MCH_EntityGunner)entity;
            String seatName = "";
            if (gunner.func_184187_bx() instanceof MCH_EntitySeat) {
                seatName = "(seat " + (((MCH_EntitySeat)gunner.func_184187_bx()).seatID + 2) + ")";
            } else if (gunner.func_184187_bx() instanceof MCH_EntityAircraft) {
                seatName = "(seat 1)";
            }
            String name = MCH_MOD.isTodaySep01() ? " EMB4 " : " Gunner ";
            this.drawCenteredString(gunner.getTeamName() + name + seatName, (int)px, (int)py + 20, -8355840);
            int S = 10;
            this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py - (double)S, px + (double)S, py + (double)S, px - (double)S, py + (double)S}, -8355840, 2);
        } else if (entity instanceof MCH_EntitySeat) {
            MCH_EntitySeat seat = (MCH_EntitySeat)entity;
            if (seat.getRiddenByEntity() == null) {
                this.drawCenteredString("seat " + (seat.seatID + 2), (int)px, (int)py + 20, -16711681);
                int S = 10;
                this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py - (double)S, px + (double)S, py + (double)S, px - (double)S, py + (double)S}, -16711681, 2);
            } else {
                this.drawCenteredString("seat " + (seat.seatID + 2), (int)px, (int)py + 20, -65536);
                int S = 10;
                this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py - (double)S, px + (double)S, py + (double)S, px - (double)S, py + (double)S}, -65536, 2);
                this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py + (double)S}, -65536);
                this.drawLine(new double[]{px + (double)S, py - (double)S, px - (double)S, py + (double)S}, -65536);
            }
        } else if (entity instanceof MCH_EntityAircraft) {
            MCH_EntityAircraft ac = (MCH_EntityAircraft)entity;
            if (ac.getRiddenByEntity() == null) {
                this.drawCenteredString("seat 1", (int)px, (int)py + 20, -16711681);
                int S = 10;
                this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py - (double)S, px + (double)S, py + (double)S, px - (double)S, py + (double)S}, -16711681, 2);
            } else {
                this.drawCenteredString("seat 1", (int)px, (int)py + 20, -65536);
                int S = 10;
                this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py - (double)S, px + (double)S, py + (double)S, px - (double)S, py + (double)S}, -65536, 2);
                this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py + (double)S}, -65536);
                this.drawLine(new double[]{px + (double)S, py - (double)S, px - (double)S, py + (double)S}, -65536);
            }
        }
        GL11.glPopMatrix();
    }
}

