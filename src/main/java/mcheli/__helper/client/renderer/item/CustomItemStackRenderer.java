/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.client.model.animation.Animation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.__helper.client.renderer.item;

import mcheli.__helper.client.MCH_ItemModelRenderers;
import mcheli.__helper.client.model.PooledModelParameters;
import mcheli.__helper.client.renderer.item.IItemModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class CustomItemStackRenderer
extends TileEntityItemStackRenderer {
    private static CustomItemStackRenderer instance;

    public void func_192838_a(ItemStack p_192838_1_, float partialTicks) {
        IItemModelRenderer renderer = MCH_ItemModelRenderers.getRenderer(p_192838_1_.func_77973_b());
        if (renderer != null) {
            renderer.renderItem(p_192838_1_, PooledModelParameters.getEntity(), PooledModelParameters.getTransformType(), Animation.getPartialTickTime());
        }
    }

    public static CustomItemStackRenderer getInstance() {
        if (instance == null) {
            instance = new CustomItemStackRenderer();
        }
        return instance;
    }
}

