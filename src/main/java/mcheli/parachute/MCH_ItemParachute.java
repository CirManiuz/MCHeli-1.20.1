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
 *  net.minecraft.world.World
 */
package mcheli.parachute;

import mcheli.parachute.MCH_EntityParachute;
import mcheli.wrapper.W_Item;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class MCH_ItemParachute
extends W_Item {
    public MCH_ItemParachute(int par1) {
        super(par1);
        this.field_77777_bU = 1;
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer player, EnumHand handIn) {
        ItemStack itemstack = player.func_184586_b(handIn);
        if (!world.field_72995_K && player.func_184187_bx() == null && !player.field_70122_E) {
            double x = player.field_70165_t + 0.5;
            double y = player.field_70163_u + 3.5;
            double z = player.field_70161_v + 0.5;
            MCH_EntityParachute entity = new MCH_EntityParachute(world, x, y, z);
            entity.field_70177_z = player.field_70177_z;
            entity.field_70159_w = player.field_70159_w;
            entity.field_70181_x = player.field_70181_x;
            entity.field_70179_y = player.field_70179_y;
            entity.field_70143_R = player.field_70143_R;
            player.field_70143_R = 0.0f;
            entity.user = player;
            entity.setType(1);
            world.func_72838_d((Entity)entity);
        }
        if (!player.field_71075_bZ.field_75098_d) {
            itemstack.func_190918_g(1);
        }
        return ActionResult.newResult((EnumActionResult)EnumActionResult.SUCCESS, (Object)itemstack);
    }
}

