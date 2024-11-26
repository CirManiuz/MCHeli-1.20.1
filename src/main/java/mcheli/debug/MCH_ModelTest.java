/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.debug;

import mcheli.wrapper.W_ModelBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class MCH_ModelTest
extends W_ModelBase {
    public ModelRenderer test = new ModelRenderer((ModelBase)this, 0, 0);

    public MCH_ModelTest() {
        this.test.func_78790_a(-5.0f, -5.0f, -5.0f, 10, 10, 10, 0.0f);
    }

    public void renderModel(double yaw, double pitch, float par7) {
        this.test.func_78785_a(par7);
    }
}

