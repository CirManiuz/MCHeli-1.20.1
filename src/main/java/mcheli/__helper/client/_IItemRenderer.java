/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.__helper.client;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Deprecated
@SideOnly(value=Side.CLIENT)
public interface _IItemRenderer {
    public boolean handleRenderType(ItemStack var1, ItemRenderType var2);

    public boolean shouldUseRenderHelper(ItemRenderType var1, ItemStack var2, ItemRendererHelper var3);

    public void renderItem(ItemRenderType var1, ItemStack var2, Object ... var3);

    @Deprecated
    public static enum ItemRendererHelper {
        ENTITY_ROTATION,
        ENTITY_BOBBING,
        EQUIPPED_BLOCK,
        BLOCK_3D,
        INVENTORY_BLOCK;

    }

    @Deprecated
    public static enum ItemRenderType {
        ENTITY,
        EQUIPPED,
        EQUIPPED_FIRST_PERSON,
        INVENTORY,
        FIRST_PERSON_MAP;

    }
}

