/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.item.Item
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.common.registry.ForgeRegistries
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package mcheli.wrapper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class W_Item
extends Item {
    public W_Item(int par1) {
    }

    public W_Item() {
    }

    public static int getIdFromItem(Item i) {
        return i == null ? 0 : field_150901_e.func_148757_b((Object)i);
    }

    public static Item getItemById(int i) {
        return Item.func_150899_d((int)i);
    }

    public static Item getItemByName(String nm) {
        return (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation(nm));
    }

    public static String getNameForItem(Item item) {
        return ForgeRegistries.ITEMS.getKey((IForgeRegistryEntry)item).toString();
    }

    public static Item getItemFromBlock(Block block) {
        return Item.func_150898_a((Block)block);
    }
}

