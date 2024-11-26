/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.color.ItemColors
 *  net.minecraft.item.Item
 *  net.minecraftforge.client.event.ColorHandlerEvent$Item
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.relauncher.Side
 */
package mcheli.__helper.client.renderer.color;

import mcheli.MCH_MOD;
import mcheli.mob.MCH_ItemSpawnGunner;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid="mcheli", value={Side.CLIENT})
public class ItemColorRegistration {
    @SubscribeEvent
    static void onRegisterItemColor(ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        itemColors.func_186730_a(MCH_ItemSpawnGunner::getColorFromItemStack, new Item[]{MCH_MOD.itemSpawnGunnerVsMonster, MCH_MOD.itemSpawnGunnerVsPlayer});
    }
}

