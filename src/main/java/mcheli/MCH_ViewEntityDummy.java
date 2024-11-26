/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.stats.RecipeBook
 *  net.minecraft.stats.StatisticsManager
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.world.World
 */
package mcheli;

import mcheli.MCH_Camera;
import mcheli.__helper.client.MCH_CameraManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class MCH_ViewEntityDummy
extends EntityPlayerSP {
    private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    private static MCH_ViewEntityDummy instance = null;
    private float zoom;

    private MCH_ViewEntityDummy(World world) {
        super(Minecraft.func_71410_x(), world, Minecraft.func_71410_x().func_147114_u(), new StatisticsManager(), new RecipeBook());
        this.field_70737_aN = 0;
        this.field_70738_aO = 1;
        this.func_70105_a(1.0f, 1.0f);
    }

    public static MCH_ViewEntityDummy getInstance(World w) {
        if ((instance == null || MCH_ViewEntityDummy.instance.field_70128_L) && w.field_72995_K) {
            instance = new MCH_ViewEntityDummy(w);
            if (Minecraft.func_71410_x().field_71439_g != null) {
                MCH_ViewEntityDummy.instance.field_71158_b = Minecraft.func_71410_x().field_71439_g.field_71158_b;
            }
            instance.func_70107_b(0.0, -4.0, 0.0);
            w.func_72838_d((Entity)instance);
        }
        return instance;
    }

    public static void onUnloadWorld() {
        if (instance != null) {
            instance.func_70106_y();
            instance = null;
        }
    }

    public AxisAlignedBB func_174813_aQ() {
        return ZERO_AABB;
    }

    public void func_70071_h_() {
    }

    public void update(MCH_Camera camera) {
        if (camera == null) {
            return;
        }
        this.zoom = camera.getCameraZoom();
        this.field_70126_B = this.field_70177_z;
        this.field_70127_C = this.field_70125_A;
        this.field_70177_z = camera.rotationYaw;
        this.field_70125_A = camera.rotationPitch;
        this.field_70169_q = camera.posX;
        this.field_70167_r = camera.posY;
        this.field_70166_s = camera.posZ;
        this.field_70165_t = camera.posX;
        this.field_70163_u = camera.posY;
        this.field_70161_v = camera.posZ;
        MCH_CameraManager.setCameraZoom(this.zoom);
    }

    public static void setCameraPosition(double x, double y, double z) {
        if (instance == null) {
            return;
        }
        MCH_ViewEntityDummy.instance.field_70169_q = x;
        MCH_ViewEntityDummy.instance.field_70167_r = y;
        MCH_ViewEntityDummy.instance.field_70166_s = z;
        MCH_ViewEntityDummy.instance.field_70142_S = x;
        MCH_ViewEntityDummy.instance.field_70137_T = y;
        MCH_ViewEntityDummy.instance.field_70136_U = z;
        MCH_ViewEntityDummy.instance.field_70165_t = x;
        MCH_ViewEntityDummy.instance.field_70163_u = y;
        MCH_ViewEntityDummy.instance.field_70161_v = z;
    }

    public float func_175156_o() {
        return super.func_175156_o() * (1.0f / this.zoom);
    }

    public float func_70047_e() {
        return 0.0f;
    }
}

