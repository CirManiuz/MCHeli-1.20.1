/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.flare;

import mcheli.wrapper.W_ModelBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class MCH_ModelFlare
extends W_ModelBase {
    public ModelRenderer model = new ModelRenderer((ModelBase)this, 0, 0).func_78787_b(4, 4);

    public MCH_ModelFlare() {
        this.model.func_78790_a(-2.0f, -2.0f, -2.0f, 4, 4, 4, 0.0f);
    }

    public void renderModel(double yaw, double pitch, float par7) {
        this.model.func_78785_a(par7);
    }
}

