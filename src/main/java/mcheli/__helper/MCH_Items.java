/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.registry.ForgeRegistries
 *  net.minecraftforge.fml.common.registry.GameRegistry$ObjectHolder
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package mcheli.__helper;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import mcheli.__helper.MCH_Utils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@GameRegistry.ObjectHolder(value="mcheli")
@Mod.EventBusSubscriber(modid="mcheli")
public class MCH_Items {
    private static Set<Item> registryWrapper = Sets.newLinkedHashSet();

    @SubscribeEvent
    static void onItemRegistryEvent(RegistryEvent.Register<Item> event) {
        for (Item item : registryWrapper) {
            event.getRegistry().register((IForgeRegistryEntry)item);
        }
    }

    public static Item register(Item item, String name) {
        registryWrapper.add((Item)item.setRegistryName(MCH_Utils.suffix(name)));
        return item;
    }

    public static ItemBlock registerBlock(Block block) {
        ItemBlock itemBlock = new ItemBlock(block);
        registryWrapper.add((Item)itemBlock.setRegistryName(block.getRegistryName()));
        return itemBlock;
    }

    @Nullable
    public static Item get(String name) {
        return (Item)ForgeRegistries.ITEMS.getValue(MCH_Utils.suffix(name));
    }

    public static String getName(Item item) {
        return ForgeRegistries.ITEMS.getKey((IForgeRegistryEntry)item).toString();
    }
}

