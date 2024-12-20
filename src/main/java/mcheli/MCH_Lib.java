/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemMapBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import mcheli.MCH_Config;
import mcheli.MCH_ItemRendererDummy;
import mcheli.MCH_MOD;
import mcheli.MCH_Vector2;
import mcheli.MCH_ViewEntityDummy;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_Blocks;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_Reflection;
import mcheli.wrapper.W_Vec3;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_Lib {
    private static HashMap<String, Material> mapMaterial = new HashMap();
    public static final String[] AZIMUTH_8 = new String[]{"S", "SW", "W", "NW", "N", "NE", "E", "SE"};
    public static final int AZIMUTH_8_ANG = 360 / AZIMUTH_8.length;

    public static void init() {
        mapMaterial.clear();
        mapMaterial.put("air", Material.field_151579_a);
        mapMaterial.put("grass", Material.field_151577_b);
        mapMaterial.put("ground", Material.field_151578_c);
        mapMaterial.put("wood", Material.field_151575_d);
        mapMaterial.put("rock", Material.field_151576_e);
        mapMaterial.put("iron", Material.field_151573_f);
        mapMaterial.put("anvil", Material.field_151574_g);
        mapMaterial.put("water", Material.field_151586_h);
        mapMaterial.put("lava", Material.field_151587_i);
        mapMaterial.put("leaves", Material.field_151584_j);
        mapMaterial.put("plants", Material.field_151585_k);
        mapMaterial.put("vine", Material.field_151582_l);
        mapMaterial.put("sponge", Material.field_151583_m);
        mapMaterial.put("cloth", Material.field_151580_n);
        mapMaterial.put("fire", Material.field_151581_o);
        mapMaterial.put("sand", Material.field_151595_p);
        mapMaterial.put("circuits", Material.field_151594_q);
        mapMaterial.put("carpet", Material.field_151593_r);
        mapMaterial.put("glass", Material.field_151592_s);
        mapMaterial.put("redstoneLight", Material.field_151591_t);
        mapMaterial.put("tnt", Material.field_151590_u);
        mapMaterial.put("coral", Material.field_151589_v);
        mapMaterial.put("ice", Material.field_151588_w);
        mapMaterial.put("packedIce", Material.field_151598_x);
        mapMaterial.put("snow", Material.field_151597_y);
        mapMaterial.put("craftedSnow", Material.field_151596_z);
        mapMaterial.put("cactus", Material.field_151570_A);
        mapMaterial.put("clay", Material.field_151571_B);
        mapMaterial.put("gourd", Material.field_151572_C);
        mapMaterial.put("dragonEgg", Material.field_151566_D);
        mapMaterial.put("portal", Material.field_151567_E);
        mapMaterial.put("cake", Material.field_151568_F);
        mapMaterial.put("web", Material.field_151569_G);
        mapMaterial.put("piston", Material.field_76233_E);
    }

    public static Material getMaterialFromName(String name) {
        if (mapMaterial.containsKey(name)) {
            return mapMaterial.get(name);
        }
        return null;
    }

    public static Vec3d calculateFaceNormal(Vec3d[] vertices) {
        Vec3d v1 = new Vec3d(vertices[1].field_72450_a - vertices[0].field_72450_a, vertices[1].field_72448_b - vertices[0].field_72448_b, vertices[1].field_72449_c - vertices[0].field_72449_c);
        Vec3d v2 = new Vec3d(vertices[2].field_72450_a - vertices[0].field_72450_a, vertices[2].field_72448_b - vertices[0].field_72448_b, vertices[2].field_72449_c - vertices[0].field_72449_c);
        return v1.func_72431_c(v2).func_72432_b();
    }

    public static double parseDouble(String s) {
        return s == null ? 0.0 : Double.parseDouble(s.replace(',', '.'));
    }

    public static float RNG(float a, float min, float max) {
        return a > max ? max : (a < min ? min : a);
    }

    public static double RNG(double a, double min, double max) {
        return a > max ? max : (a < min ? min : a);
    }

    public static float smooth(float rot, float prevRot, float tick) {
        return prevRot + (rot - prevRot) * tick;
    }

    public static float smoothRot(float rot, float prevRot, float tick) {
        if (rot - prevRot < -180.0f) {
            prevRot -= 360.0f;
        } else if (prevRot - rot < -180.0f) {
            prevRot += 360.0f;
        }
        return prevRot + (rot - prevRot) * tick;
    }

    public static double getRotateDiff(double base, double target) {
        base = MCH_Lib.getRotate360(base);
        if ((target = MCH_Lib.getRotate360(target)) - base < -180.0) {
            target += 360.0;
        } else if (target - base > 180.0) {
            base += 360.0;
        }
        return target - base;
    }

    public static float getPosAngle(double tx, double tz, double cx, double cz) {
        double length_A = Math.sqrt(tx * tx + tz * tz);
        double length_B = Math.sqrt(cx * cx + cz * cz);
        double cos_sita = (tx * cx + tz * cz) / (length_A * length_B);
        double sita = Math.acos(cos_sita);
        return (float)(sita * 180.0 / Math.PI);
    }

    public static void applyEntityHurtResistantTimeConfig(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase)entity;
            double h_time = MCH_Config.HurtResistantTime.prmDouble * (double)elb.field_70172_ad;
            elb.field_70172_ad = (int)h_time;
        }
    }

    public static int round(double d) {
        return (int)(d + 0.5);
    }

    public static Vec3d Rot2Vec3(float yaw, float pitch) {
        return new Vec3d((double)(-MathHelper.func_76126_a((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI))), (double)(-MathHelper.func_76126_a((float)(pitch / 180.0f * (float)Math.PI))), (double)(MathHelper.func_76134_b((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI))));
    }

    public static Vec3d RotVec3(double x, double y, double z, float yaw, float pitch) {
        Vec3d v = new Vec3d(x, y, z);
        v = v.func_178789_a(pitch / 180.0f * (float)Math.PI);
        v = v.func_178785_b(yaw / 180.0f * (float)Math.PI);
        return v;
    }

    public static Vec3d RotVec3(double x, double y, double z, float yaw, float pitch, float roll) {
        Vec3d v = new Vec3d(x, y, z);
        v = W_Vec3.rotateRoll(roll / 180.0f * (float)Math.PI, v);
        v = v.func_178789_a(pitch / 180.0f * (float)Math.PI);
        v = v.func_178785_b(yaw / 180.0f * (float)Math.PI);
        return v;
    }

    public static Vec3d RotVec3(Vec3d vin, float yaw, float pitch) {
        Vec3d v = new Vec3d(vin.field_72450_a, vin.field_72448_b, vin.field_72449_c);
        v = v.func_178789_a(pitch / 180.0f * (float)Math.PI);
        v = v.func_178785_b(yaw / 180.0f * (float)Math.PI);
        return v;
    }

    public static Vec3d RotVec3(Vec3d vin, float yaw, float pitch, float roll) {
        Vec3d v = new Vec3d(vin.field_72450_a, vin.field_72448_b, vin.field_72449_c);
        v = W_Vec3.rotateRoll(roll / 180.0f * (float)Math.PI, v);
        v = v.func_178789_a(pitch / 180.0f * (float)Math.PI);
        v = v.func_178785_b(yaw / 180.0f * (float)Math.PI);
        return v;
    }

    public static Vec3d _Rot2Vec3(float yaw, float pitch, float roll) {
        return new Vec3d((double)(-MathHelper.func_76126_a((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI))), (double)(-MathHelper.func_76126_a((float)(pitch / 180.0f * (float)Math.PI))), (double)(MathHelper.func_76134_b((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI))));
    }

    public static double getRotate360(double r) {
        return (r %= 360.0) >= 0.0 ? r : r + 360.0;
    }

    public static void Log(String format, Object ... data) {
        String side = MCH_MOD.proxy.isRemote() ? "[Client]" : "[Server]";
        System.out.printf("[" + MCH_Lib.getTime() + "][mcheli]" + side + " " + format + "\n", data);
    }

    public static void Log(World world, String format, Object ... data) {
        if (world != null) {
            MCH_Lib.Log((world.field_72995_K ? "[ClientWorld]" : "[ServerWorld]") + " " + format, data);
        } else {
            MCH_Lib.Log("[UnknownWorld]" + format, data);
        }
    }

    public static void Log(Entity entity, String format, Object ... data) {
        if (entity != null) {
            MCH_Lib.Log(entity.field_70170_p, format, data);
        } else {
            MCH_Lib.Log((World)null, format, data);
        }
    }

    public static void DbgLog(boolean isRemote, String format, Object ... data) {
        if (MCH_Config.DebugLog) {
            if (isRemote) {
                if (MCH_Lib.getClientPlayer() instanceof EntityPlayer) {
                    // empty if block
                }
                System.out.println(String.format(format, data));
            } else {
                System.out.println(String.format(format, data));
            }
        }
    }

    public static void DbgLog(World w, String format, Object ... data) {
        MCH_Lib.DbgLog(w.field_72995_K, format, data);
    }

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        return sdf.format(new Date());
    }

    public static String getAzimuthStr8(int dir) {
        if ((dir %= 360) < 0) {
            dir += 360;
        }
        return AZIMUTH_8[dir /= AZIMUTH_8_ANG];
    }

    public static void rotatePoints(double[] points, float r) {
        r = r / 180.0f * (float)Math.PI;
        int i = 0;
        while (i + 1 < points.length) {
            double x = points[i + 0];
            double y = points[i + 1];
            points[i + 0] = x * (double)MathHelper.func_76134_b((float)r) - y * (double)MathHelper.func_76126_a((float)r);
            points[i + 1] = x * (double)MathHelper.func_76126_a((float)r) + y * (double)MathHelper.func_76134_b((float)r);
            i += 2;
        }
    }

    public static void rotatePoints(ArrayList<MCH_Vector2> points, float r) {
        r = r / 180.0f * (float)Math.PI;
        int i = 0;
        while (i + 1 < points.size()) {
            double x = points.get((int)(i + 0)).x;
            double y = points.get((int)(i + 0)).y;
            points.get((int)(i + 0)).x = x * (double)MathHelper.func_76134_b((float)r) - y * (double)MathHelper.func_76126_a((float)r);
            points.get((int)(i + 0)).y = x * (double)MathHelper.func_76126_a((float)r) + y * (double)MathHelper.func_76134_b((float)r);
            i += 2;
        }
    }

    public static String[] listupFileNames(String path) {
        File dir = new File(path);
        return dir.list();
    }

    public static boolean isBlockInWater(World w, int x, int y, int z) {
        int[][] offset = new int[][]{{0, -1, 0}, {0, 0, 0}, {0, 0, -1}, {0, 0, 1}, {-1, 0, 0}, {1, 0, 0}, {0, 1, 0}};
        if (y <= 0) {
            return false;
        }
        for (int[] o : offset) {
            if (!W_WorldFunc.isBlockWater(w, x + o[0], y + o[1], z + o[2])) continue;
            return true;
        }
        return false;
    }

    public static int getBlockIdY(World w, double posX, double posY, double posZ, int size, int lenY, boolean canColliableOnly) {
        Block block = MCH_Lib.getBlockY(w, posX, posY, posZ, size, lenY, canColliableOnly);
        if (block == null) {
            return 0;
        }
        return W_Block.func_149682_b((Block)block);
    }

    public static int getBlockIdY(Entity entity, int size, int lenY) {
        return MCH_Lib.getBlockIdY(entity, size, lenY, true);
    }

    public static int getBlockIdY(Entity entity, int size, int lenY, boolean canColliableOnly) {
        Block block = MCH_Lib.getBlockY(entity, size, lenY, canColliableOnly);
        if (block == null) {
            return 0;
        }
        return W_Block.func_149682_b((Block)block);
    }

    public static Block getBlockY(Entity entity, int size, int lenY, boolean canColliableOnly) {
        return MCH_Lib.getBlockY(entity.field_70170_p, entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, size, lenY, canColliableOnly);
    }

    public static Block getBlockY(World world, Vec3d pos, int size, int lenY, boolean canColliableOnly) {
        return MCH_Lib.getBlockY(world, pos.field_72450_a, pos.field_72448_b, pos.field_72449_c, size, lenY, canColliableOnly);
    }

    public static Block getBlockY(World world, double posX, double posY, double posZ, int size, int lenY, boolean canColliableOnly) {
        if (lenY == 0) {
            return W_Blocks.field_150350_a;
        }
        int px = (int)(posX + 0.5);
        int py = (int)(posY + 0.5);
        int pz = (int)(posZ + 0.5);
        int cntY = lenY > 0 ? lenY : -lenY;
        for (int y = 0; y < cntY; ++y) {
            if (py + y < 0 || py + y > 255) {
                return W_Blocks.field_150350_a;
            }
            for (int x = -size / 2; x <= size / 2; ++x) {
                for (int z = -size / 2; z <= size / 2; ++z) {
                    IBlockState iblockstate = world.func_180495_p(new BlockPos(px + x, py + (lenY > 0 ? y : -y), pz + z));
                    Block block = W_WorldFunc.getBlock(world, px + x, py + (lenY > 0 ? y : -y), pz + z);
                    if (block == null || block == W_Blocks.field_150350_a) continue;
                    if (canColliableOnly) {
                        if (!block.func_176209_a(iblockstate, true)) continue;
                        return block;
                    }
                    return block;
                }
            }
        }
        return W_Blocks.field_150350_a;
    }

    public static Vec3d getYawPitchFromVec(Vec3d v) {
        return MCH_Lib.getYawPitchFromVec(v.field_72450_a, v.field_72448_b, v.field_72449_c);
    }

    public static Vec3d getYawPitchFromVec(double x, double y, double z) {
        double p = MathHelper.func_76133_a((double)(x * x + z * z));
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI);
        float roll = (float)(Math.atan2(y, p) * 180.0 / Math.PI);
        return new Vec3d(0.0, (double)yaw, (double)roll);
    }

    public static float getAlpha(int argb) {
        return (float)(argb >> 24) / 255.0f;
    }

    public static float getRed(int argb) {
        return (float)(argb >> 16 & 0xFF) / 255.0f;
    }

    public static float getGreen(int argb) {
        return (float)(argb >> 8 & 0xFF) / 255.0f;
    }

    public static float getBlue(int argb) {
        return (float)(argb & 0xFF) / 255.0f;
    }

    public static void enableFirstPersonItemRender() {
        switch (MCH_Config.DisableItemRender.prmInt) {
            case 1: {
                break;
            }
            case 2: {
                MCH_ItemRendererDummy.disableDummyItemRenderer();
                break;
            }
            case 3: {
                W_Reflection.restoreCameraZoom();
            }
        }
    }

    public static void disableFirstPersonItemRender(ItemStack itemStack) {
        if (itemStack.func_190926_b() && itemStack.func_77973_b() instanceof ItemMapBase && !(W_McClient.getRenderEntity() instanceof MCH_ViewEntityDummy)) {
            return;
        }
        MCH_Lib.disableFirstPersonItemRender();
    }

    public static void disableFirstPersonItemRender() {
        switch (MCH_Config.DisableItemRender.prmInt) {
            case 1: {
                W_Reflection.setItemRendererMainHand(new ItemStack((Item)MCH_MOD.invisibleItem));
                break;
            }
            case 2: {
                MCH_ItemRendererDummy.enableDummyItemRenderer();
                break;
            }
            case 3: {
                W_Reflection.setCameraZoom(1.01f);
            }
        }
    }

    public static Entity getClientPlayer() {
        return MCH_MOD.proxy.getClientPlayer();
    }

    public static void setRenderViewEntity(EntityLivingBase entity) {
        if (MCH_Config.ReplaceRenderViewEntity.prmBool) {
            W_McClient.setRenderEntity(entity);
        }
    }

    public static Entity getRenderViewEntity() {
        return W_McClient.getRenderEntity();
    }

    public static boolean isFlansModEntity(Entity entity) {
        if (entity == null) {
            return false;
        }
        String className = entity.getClass().getName();
        return entity != null && (className.indexOf("EntityVehicle") >= 0 || className.indexOf("EntityPlane") >= 0 || className.indexOf("EntityMecha") >= 0 || className.indexOf("EntityAAGun") >= 0);
    }
}

