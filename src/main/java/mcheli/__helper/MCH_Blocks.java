/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.minecraft.block.Block
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package mcheli.__helper;

import com.google.common.collect.Sets;
import java.util.Set;
import mcheli.__helper.MCH_Utils;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid="mcheli")
public class MCH_Blocks {
    private static Set<Block> registryWrapper = Sets.newLinkedHashSet();

    @SubscribeEvent
    static void onBlockRegisterEvent(RegistryEvent.Register<Block> event) {
        for (Block block : registryWrapper) {
            event.getRegistry().register((IForgeRegistryEntry)block);
        }
    }

    public static void register(Block block, String name) {
        registryWrapper.add((Block)block.setRegistryName(MCH_Utils.suffix(name)));
    }
}

