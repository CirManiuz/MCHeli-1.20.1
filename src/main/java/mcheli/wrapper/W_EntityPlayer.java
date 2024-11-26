/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.world.World
 */
package mcheli.wrapper;

import mcheli.wrapper.W_EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public abstract class W_EntityPlayer
extends EntityPlayer {
    public W_EntityPlayer(World par1World, EntityPlayer player) {
        super(par1World, player.func_146103_bH());
    }

    public static void closeScreen(Entity p) {
        if (p != null) {
            if (p.field_70170_p.field_72995_K) {
                W_EntityPlayerSP.closeScreen(p);
            } else if (p instanceof EntityPlayerMP) {
                ((EntityPlayerMP)p).func_71053_j();
            }
        }
    }

    public static boolean hasItem(EntityPlayer player, Item item) {
        return item != null && player.field_71071_by.func_70431_c(new ItemStack(item));
    }

    public static boolean consumeInventoryItem(EntityPlayer player, Item item) {
        int index = player.field_71071_by.func_194014_c(new ItemStack(item));
        return item != null && player.field_71071_by.func_70298_a(index, 1).func_190926_b();
    }

    public static void addChatMessage(EntityPlayer player, String s) {
        player.func_145747_a((ITextComponent)new TextComponentString(s));
    }

    public static EntityItem dropPlayerItemWithRandomChoice(EntityPlayer player, ItemStack item, boolean b1, boolean b2) {
        return player.func_146097_a(item, b1, b2);
    }

    public static boolean isPlayer(Entity entity) {
        return entity instanceof EntityPlayer;
    }
}

