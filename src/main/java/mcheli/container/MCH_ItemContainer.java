/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ActionResult
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.container;

import java.util.List;
import mcheli.container.MCH_EntityContainer;
import mcheli.wrapper.W_Item;
import mcheli.wrapper.W_MovingObjectPosition;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_ItemContainer
extends W_Item {
    public MCH_ItemContainer(int par1) {
        super(par1);
        this.func_77625_d(1);
    }

    public ActionResult<ItemStack> func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        int i;
        float f8;
        float f6;
        double d3;
        float f5;
        ItemStack itemstack = playerIn.func_184586_b(handIn);
        float f = 1.0f;
        float f1 = playerIn.field_70127_C + (playerIn.field_70125_A - playerIn.field_70127_C) * f;
        float f2 = playerIn.field_70126_B + (playerIn.field_70177_z - playerIn.field_70126_B) * f;
        double d0 = playerIn.field_70169_q + (playerIn.field_70165_t - playerIn.field_70169_q) * (double)f;
        double d1 = playerIn.field_70167_r + (playerIn.field_70163_u - playerIn.field_70167_r) * (double)f + (double)playerIn.func_70047_e();
        double d2 = playerIn.field_70166_s + (playerIn.field_70161_v - playerIn.field_70166_s) * (double)f;
        Vec3d vec3 = W_WorldFunc.getWorldVec3(worldIn, d0, d1, d2);
        float f3 = MathHelper.func_76134_b((float)(-f2 * ((float)Math.PI / 180) - (float)Math.PI));
        float f4 = MathHelper.func_76126_a((float)(-f2 * ((float)Math.PI / 180) - (float)Math.PI));
        float f7 = f4 * (f5 = -MathHelper.func_76134_b((float)(-f1 * ((float)Math.PI / 180))));
        Vec3d vec31 = vec3.func_72441_c((double)f7 * (d3 = 5.0), (double)(f6 = MathHelper.func_76126_a((float)(-f1 * ((float)Math.PI / 180)))) * d3, (double)(f8 = f3 * f5) * d3);
        RayTraceResult movingobjectposition = W_WorldFunc.clip(worldIn, vec3, vec31, true);
        if (movingobjectposition == null) {
            return ActionResult.newResult((EnumActionResult)EnumActionResult.PASS, (Object)itemstack);
        }
        Vec3d vec32 = playerIn.func_70676_i(f);
        boolean flag = false;
        float f9 = 1.0f;
        List list = worldIn.func_72839_b((Entity)playerIn, playerIn.func_174813_aQ().func_72321_a(vec32.field_72450_a * d3, vec32.field_72448_b * d3, vec32.field_72449_c * d3).func_72314_b((double)f9, (double)f9, (double)f9));
        for (i = 0; i < list.size(); ++i) {
            Entity entity = (Entity)list.get(i);
            if (!entity.func_70067_L()) continue;
            float f10 = entity.func_70111_Y();
            AxisAlignedBB axisalignedbb = entity.func_174813_aQ().func_72314_b((double)f10, (double)f10, (double)f10);
            if (!axisalignedbb.func_72318_a(vec3)) continue;
            flag = true;
        }
        if (flag) {
            return ActionResult.newResult((EnumActionResult)EnumActionResult.FAIL, (Object)itemstack);
        }
        if (W_MovingObjectPosition.isHitTypeTile(movingobjectposition)) {
            i = movingobjectposition.func_178782_a().func_177958_n();
            int j = movingobjectposition.func_178782_a().func_177956_o();
            int k = movingobjectposition.func_178782_a().func_177952_p();
            MCH_EntityContainer entityboat = new MCH_EntityContainer(worldIn, (float)i + 0.5f, (float)j + 1.0f, (float)k + 0.5f);
            entityboat.field_70177_z = ((MathHelper.func_76128_c((double)((double)(playerIn.field_70177_z * 4.0f / 360.0f) + 0.5)) & 3) - 1) * 90;
            if (!worldIn.func_184144_a((Entity)entityboat, entityboat.func_174813_aQ().func_72314_b(-0.1, -0.1, -0.1)).isEmpty()) {
                return ActionResult.newResult((EnumActionResult)EnumActionResult.FAIL, (Object)itemstack);
            }
            if (!worldIn.field_72995_K) {
                worldIn.func_72838_d((Entity)entityboat);
            }
            if (!playerIn.field_71075_bZ.field_75098_d) {
                itemstack.func_190918_g(1);
            }
        }
        return ActionResult.newResult((EnumActionResult)EnumActionResult.SUCCESS, (Object)itemstack);
    }
}

