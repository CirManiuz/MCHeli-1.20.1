/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  org.lwjgl.opengl.GL11
 */
package mcheli;

import mcheli.MCH_ModelManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

public class MCH_TEST_ModelRenderer
extends ModelRenderer {
    public MCH_TEST_ModelRenderer(ModelBase par1ModelBase) {
        super(par1ModelBase);
    }

    public void func_78785_a(float par1) {
        GL11.glPushMatrix();
        GL11.glScaled((double)0.2, (double)-0.2, (double)0.2);
        MCH_ModelManager.render("helicopters", "ah-64");
        GL11.glPopMatrix();
    }
}

