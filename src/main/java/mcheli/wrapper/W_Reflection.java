/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.common.ObfuscationReflectionHelper
 */
package mcheli.wrapper;

import javax.annotation.Nonnull;
import mcheli.__helper.client.MCH_CameraManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class W_Reflection {
    public static RenderManager getRenderManager(Render<?> render) {
        return render.func_177068_d();
    }

    public static void restoreDefaultThirdPersonDistance() {
        W_Reflection.setThirdPersonDistance(4.0f);
    }

    public static void setThirdPersonDistance(float dist) {
        if ((double)dist < 0.1) {
            return;
        }
        MCH_CameraManager.setThirdPeasonCameraDistance(dist);
    }

    public static float getThirdPersonDistance() {
        return MCH_CameraManager.getThirdPeasonCameraDistance();
    }

    public static void setCameraRoll(float roll) {
        MCH_CameraManager.setCameraRoll(roll);
    }

    public static void restoreCameraZoom() {
        W_Reflection.setCameraZoom(1.0f);
    }

    public static void setCameraZoom(float zoom) {
        try {
            Minecraft mc = Minecraft.func_71410_x();
            ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, (Object)mc.field_71460_t, (Object)Float.valueOf(zoom), (String)"field_78503_V");
            MCH_CameraManager.setCameraZoom(zoom);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static void setItemRenderer(ItemRenderer r) {
    }

    public static void setCreativeDigSpeed(int n) {
        try {
            Minecraft mc = Minecraft.func_71410_x();
            ObfuscationReflectionHelper.setPrivateValue(PlayerControllerMP.class, (Object)mc.field_71442_b, (Object)n, (String)"field_78781_i");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ItemRenderer getItemRenderer() {
        return Minecraft.func_71410_x().field_71460_t.field_78516_c;
    }

    public static void setItemRendererMainHand(ItemStack itemToRender) {
        try {
            ObfuscationReflectionHelper.setPrivateValue(ItemRenderer.class, (Object)W_Reflection.getItemRenderer(), (Object)itemToRender, (String)"field_187467_d");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nonnull
    public static ItemStack getItemRendererMainHand() {
        try {
            return (ItemStack)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, (Object)W_Reflection.getItemRenderer(), (String)"field_187467_d");
        }
        catch (Exception e) {
            e.printStackTrace();
            return ItemStack.field_190927_a;
        }
    }

    public static void setItemRendererMainProgress(float equippedProgress) {
        try {
            ObfuscationReflectionHelper.setPrivateValue(ItemRenderer.class, (Object)W_Reflection.getItemRenderer(), (Object)Float.valueOf(equippedProgress), (String)"field_187469_f");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

