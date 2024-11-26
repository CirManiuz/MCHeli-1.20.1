/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 */
package mcheli;

import mcheli.__helper.client._IItemRenderer;
import net.minecraft.item.ItemStack;

@Deprecated
public class MCH_InvisibleItemRender
implements _IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    public boolean useCurrentWeapon() {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object ... data) {
    }
}

