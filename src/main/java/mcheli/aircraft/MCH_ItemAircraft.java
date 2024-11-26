/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockDispenser
 *  net.minecraft.block.BlockSponge
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityMinecartEmpty
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ActionResult
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.aircraft;

import java.util.List;
import javax.annotation.Nullable;
import mcheli.MCH_Config;
import mcheli.__helper.MCH_CriteriaTriggers;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_ItemAircraftDispenseBehavior;
import mcheli.wrapper.W_EntityPlayer;
import mcheli.wrapper.W_Item;
import mcheli.wrapper.W_MovingObjectPosition;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSponge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class MCH_ItemAircraft
extends W_Item {
    private static boolean isRegistedDispenseBehavior = false;

    public MCH_ItemAircraft(int i) {
        super(i);
    }

    public static void registerDispenseBehavior(Item item) {
        if (isRegistedDispenseBehavior) {
            return;
        }
        BlockDispenser.field_149943_a.func_82595_a((Object)item, (Object)new MCH_ItemAircraftDispenseBehavior());
    }

    @Nullable
    public abstract MCH_AircraftInfo getAircraftInfo();

    @Nullable
    public abstract MCH_EntityAircraft createAircraft(World var1, double var2, double var4, double var6, ItemStack var8);

    public MCH_EntityAircraft onTileClick(ItemStack itemStack, World world, float rotationYaw, int x, int y, int z) {
        MCH_EntityAircraft ac = this.createAircraft(world, (float)x + 0.5f, (float)y + 1.0f, (float)z + 0.5f, itemStack);
        if (ac == null) {
            return null;
        }
        ac.initRotationYaw(((MathHelper.func_76128_c((double)((double)(rotationYaw * 4.0f / 360.0f) + 0.5)) & 3) - 1) * 90);
        if (!world.func_184144_a((Entity)ac, ac.func_174813_aQ().func_72314_b(-0.1, -0.1, -0.1)).isEmpty()) {
            return null;
        }
        return ac;
    }

    public String toString() {
        MCH_AircraftInfo info = this.getAircraftInfo();
        if (info != null) {
            return super.toString() + "(" + info.getDirectoryName() + ":" + info.name + ")";
        }
        return super.toString() + "(null)";
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer player, EnumHand handIn) {
        float f8;
        float f6;
        double d3;
        float f5;
        ItemStack itemstack = player.func_184586_b(handIn);
        float f = 1.0f;
        float f1 = player.field_70127_C + (player.field_70125_A - player.field_70127_C) * f;
        float f2 = player.field_70126_B + (player.field_70177_z - player.field_70126_B) * f;
        double d0 = player.field_70169_q + (player.field_70165_t - player.field_70169_q) * (double)f;
        double d1 = player.field_70167_r + (player.field_70163_u - player.field_70167_r) * (double)f + 1.62;
        double d2 = player.field_70166_s + (player.field_70161_v - player.field_70166_s) * (double)f;
        Vec3d vec3 = W_WorldFunc.getWorldVec3(world, d0, d1, d2);
        float f3 = MathHelper.func_76134_b((float)(-f2 * ((float)Math.PI / 180) - (float)Math.PI));
        float f4 = MathHelper.func_76126_a((float)(-f2 * ((float)Math.PI / 180) - (float)Math.PI));
        float f7 = f4 * (f5 = -MathHelper.func_76134_b((float)(-f1 * ((float)Math.PI / 180))));
        Vec3d vec31 = vec3.func_72441_c((double)f7 * (d3 = 5.0), (double)(f6 = MathHelper.func_76126_a((float)(-f1 * ((float)Math.PI / 180)))) * d3, (double)(f8 = f3 * f5) * d3);
        RayTraceResult mop = W_WorldFunc.clip(world, vec3, vec31, true);
        if (mop == null) {
            return ActionResult.newResult((EnumActionResult)EnumActionResult.PASS, (Object)itemstack);
        }
        Vec3d vec32 = player.func_70676_i(f);
        boolean flag = false;
        float f9 = 1.0f;
        List list = world.func_72839_b((Entity)player, player.func_174813_aQ().func_72321_a(vec32.field_72450_a * d3, vec32.field_72448_b * d3, vec32.field_72449_c * d3).func_72314_b((double)f9, (double)f9, (double)f9));
        for (int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity)list.get(i);
            if (!entity.func_70067_L()) continue;
            float f10 = entity.func_70111_Y();
            AxisAlignedBB axisalignedbb = entity.func_174813_aQ().func_72314_b((double)f10, (double)f10, (double)f10);
            if (!axisalignedbb.func_72318_a(vec3)) continue;
            flag = true;
        }
        if (flag) {
            return ActionResult.newResult((EnumActionResult)EnumActionResult.PASS, (Object)itemstack);
        }
        if (W_MovingObjectPosition.isHitTypeTile(mop)) {
            if (MCH_Config.PlaceableOnSpongeOnly.prmBool) {
                MCH_AircraftInfo acInfo = this.getAircraftInfo();
                if (acInfo != null && acInfo.isFloat && !acInfo.canMoveOnGround) {
                    if (world.func_180495_p(mop.func_178782_a().func_177977_b()).func_177230_c() != Blocks.field_150360_v) {
                        return ActionResult.newResult((EnumActionResult)EnumActionResult.FAIL, (Object)itemstack);
                    }
                    for (int x = -1; x <= 1; ++x) {
                        for (int z = -1; z <= 1; ++z) {
                            if (world.func_180495_p(mop.func_178782_a().func_177982_a(x, 0, z)).func_177230_c() == Blocks.field_150355_j) continue;
                            return ActionResult.newResult((EnumActionResult)EnumActionResult.FAIL, (Object)itemstack);
                        }
                    }
                } else {
                    Block block = world.func_180495_p(mop.func_178782_a()).func_177230_c();
                    if (!(block instanceof BlockSponge)) {
                        return ActionResult.newResult((EnumActionResult)EnumActionResult.FAIL, (Object)itemstack);
                    }
                }
            }
            this.spawnAircraft(itemstack, world, player, mop.func_178782_a());
        }
        return ActionResult.newResult((EnumActionResult)EnumActionResult.SUCCESS, (Object)itemstack);
    }

    public MCH_EntityAircraft spawnAircraft(ItemStack itemStack, World world, EntityPlayer player, BlockPos blockpos) {
        MCH_EntityAircraft ac = this.onTileClick(itemStack, world, player.field_70177_z, blockpos.func_177958_n(), blockpos.func_177956_o(), blockpos.func_177952_p());
        if (ac != null) {
            if (ac.getAcInfo() != null && ac.getAcInfo().creativeOnly && !player.field_71075_bZ.field_75098_d) {
                return null;
            }
            if (ac.isUAV()) {
                if (world.field_72995_K) {
                    if (ac.isSmallUAV()) {
                        W_EntityPlayer.addChatMessage(player, "Please use the UAV station OR Portable Controller");
                    } else {
                        W_EntityPlayer.addChatMessage(player, "Please use the UAV station");
                    }
                }
                ac = null;
            } else {
                if (!world.field_72995_K) {
                    ac.getAcDataFromItem(itemStack);
                    world.func_72838_d((Entity)ac);
                    MCH_CriteriaTriggers.PUT_AIRCRAFT.trigger((EntityPlayerMP)player);
                }
                if (!player.field_71075_bZ.field_75098_d) {
                    itemStack.func_190918_g(1);
                }
            }
        }
        return ac;
    }

    public void rideEntity(ItemStack item, Entity target, EntityPlayer player) {
        if (!MCH_Config.PlaceableOnSpongeOnly.prmBool && target instanceof EntityMinecartEmpty && target.func_184188_bt().isEmpty()) {
            BlockPos blockpos = new BlockPos((int)target.field_70165_t, (int)target.field_70163_u + 2, (int)target.field_70161_v);
            MCH_EntityAircraft ac = this.spawnAircraft(item, player.field_70170_p, player, blockpos);
            if (!player.field_70170_p.field_72995_K && ac != null) {
                ac.func_184220_m(target);
            }
        }
    }
}

