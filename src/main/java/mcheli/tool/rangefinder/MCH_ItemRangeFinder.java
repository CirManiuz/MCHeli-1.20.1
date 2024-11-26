/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.EnumAction
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ActionResult
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumHand
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.tool.rangefinder;

import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.multiplay.MCH_PacketIndSpotEntity;
import mcheli.wrapper.W_Item;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_Reflection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_ItemRangeFinder
extends W_Item {
    public static int rangeFinderUseCooldown = 0;
    public static boolean continueUsingItem = false;
    public static float zoom = 2.0f;
    public static int mode = 0;

    public MCH_ItemRangeFinder(int itemId) {
        super(itemId);
        this.field_77777_bU = 1;
        this.func_77656_e(10);
    }

    public static boolean canUse(EntityPlayer player) {
        MCH_EntityAircraft ac;
        if (player == null) {
            return false;
        }
        if (player.field_70170_p == null) {
            return false;
        }
        if (player.func_184614_ca().func_190926_b()) {
            return false;
        }
        if (!(player.func_184614_ca().func_77973_b() instanceof MCH_ItemRangeFinder)) {
            return false;
        }
        if (player.func_184187_bx() instanceof MCH_EntityAircraft) {
            return false;
        }
        return !(player.func_184187_bx() instanceof MCH_EntitySeat) || (ac = ((MCH_EntitySeat)player.func_184187_bx()).getParent()) == null || !ac.getIsGunnerMode((Entity)player) && ac.getWeaponIDBySeatID(ac.getSeatIdByEntity((Entity)player)) < 0;
    }

    public static boolean isUsingScope(EntityPlayer player) {
        return player.func_184612_cw() > 8 || continueUsingItem;
    }

    public static void onStartUseItem() {
        zoom = 2.0f;
        W_Reflection.setCameraZoom(2.0f);
        continueUsingItem = true;
    }

    public static void onStopUseItem() {
        W_Reflection.restoreCameraZoom();
        continueUsingItem = false;
    }

    @SideOnly(value=Side.CLIENT)
    public void spotEntity(EntityPlayer player, ItemStack itemStack) {
        if (player != null && player.field_70170_p.field_72995_K && rangeFinderUseCooldown == 0 && player.func_184612_cw() > 8) {
            if (mode == 2) {
                rangeFinderUseCooldown = 60;
                MCH_PacketIndSpotEntity.send((EntityLivingBase)player, 0);
            } else if (itemStack.func_77960_j() < itemStack.func_77958_k()) {
                rangeFinderUseCooldown = 60;
                MCH_PacketIndSpotEntity.send((EntityLivingBase)player, mode == 0 ? 60 : 3);
            } else {
                W_McClient.MOD_playSoundFX("ng", 1.0f, 1.0f);
            }
        }
    }

    public void func_77615_a(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (worldIn.field_72995_K) {
            MCH_ItemRangeFinder.onStopUseItem();
        }
    }

    public ItemStack func_77654_b(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        return stack;
    }

    @SideOnly(value=Side.CLIENT)
    public boolean func_77662_d() {
        return true;
    }

    public EnumAction func_77661_b(ItemStack itemStack) {
        return EnumAction.BOW;
    }

    public int func_77626_a(ItemStack itemStack) {
        return 72000;
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer player, EnumHand handIn) {
        ItemStack itemstack = player.func_184586_b(handIn);
        if (MCH_ItemRangeFinder.canUse(player)) {
            player.func_184598_c(handIn);
        }
        return ActionResult.newResult((EnumActionResult)EnumActionResult.SUCCESS, (Object)itemstack);
    }
}

