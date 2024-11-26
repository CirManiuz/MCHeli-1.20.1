/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 */
package mcheli.wrapper;

import mcheli.MCH_Config;
import mcheli.__helper.MCH_Utils;
import mcheli.wrapper.W_Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class W_EntityRenderer {
    @Deprecated
    public static void setItemRenderer(Minecraft mc, ItemRenderer ir) {
        W_Reflection.setItemRenderer(ir);
    }

    public static boolean isShaderSupport() {
        return OpenGlHelper.field_148824_g && !MCH_Config.DisableShader.prmBool;
    }

    public static void activateShader(String n) {
        W_EntityRenderer.activateShader(MCH_Utils.suffix("shaders/post/" + n + ".json"));
    }

    public static void activateShader(ResourceLocation r) {
        Minecraft mc = Minecraft.func_71410_x();
        mc.field_71460_t.func_175069_a(r);
    }

    public static void deactivateShader() {
        Minecraft.func_71410_x().field_71460_t.func_181022_b();
    }

    public static void renderEntityWithPosYaw(RenderManager rm, Entity par1Entity, double par2, double par4, double par6, float par8, float par9, boolean b) {
        rm.func_188391_a(par1Entity, par2, par4, par6, par8, par9, b);
    }
}

