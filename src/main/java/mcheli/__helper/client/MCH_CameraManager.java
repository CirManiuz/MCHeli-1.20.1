/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.EntityViewRenderEvent$CameraSetup
 *  net.minecraftforge.client.event.EntityViewRenderEvent$FOVModifier
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.relauncher.Side
 */
package mcheli.__helper.client;

import javax.annotation.Nullable;
import mcheli.MCH_ViewEntityDummy;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.tool.rangefinder.MCH_ItemRangeFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid="mcheli", value={Side.CLIENT})
public class MCH_CameraManager {
    private static final float DEF_THIRD_CAMERA_DIST = 4.0f;
    private static final Minecraft mc = Minecraft.func_71410_x();
    private static float cameraRoll = 0.0f;
    private static float cameraDistance = 4.0f;
    private static float cameraZoom = 1.0f;
    private static MCH_EntityAircraft ridingAircraft = null;

    @SubscribeEvent
    static void onCameraSetupEvent(EntityViewRenderEvent.CameraSetup event) {
        MCH_EntityAircraft ridingEntity;
        Entity entity = event.getEntity();
        float f = event.getEntity().func_70047_e();
        if (MCH_CameraManager.mc.field_71474_y.field_74320_O > 0) {
            if (MCH_CameraManager.mc.field_71474_y.field_74320_O == 2) {
                GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            }
            GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)(-(cameraDistance - 4.0f)));
            if (MCH_CameraManager.mc.field_71474_y.field_74320_O == 2) {
                GlStateManager.func_179114_b((float)-180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            }
        }
        if ((ridingEntity = ridingAircraft) != null && ridingEntity.canSwitchFreeLook() && ridingEntity.isPilot((Entity)MCH_CameraManager.mc.field_71439_g)) {
            boolean flag = !(entity instanceof MCH_ViewEntityDummy);
            GlStateManager.func_179109_b((float)0.0f, (float)(-f), (float)0.0f);
            if (flag) {
                GlStateManager.func_179114_b((float)cameraRoll, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            if (ridingEntity.isOverridePlayerPitch() && flag) {
                GlStateManager.func_179114_b((float)ridingEntity.field_70125_A, (float)1.0f, (float)0.0f, (float)0.0f);
                event.setPitch(event.getPitch() - ridingEntity.field_70125_A);
            }
            if (ridingEntity.isOverridePlayerYaw() && !ridingEntity.isHovering() && flag) {
                GlStateManager.func_179114_b((float)ridingEntity.field_70177_z, (float)0.0f, (float)1.0f, (float)0.0f);
                event.setYaw(event.getYaw() - ridingEntity.field_70177_z);
            }
            GlStateManager.func_179109_b((float)0.0f, (float)f, (float)0.0f);
        }
    }

    @SubscribeEvent
    static void onFOVModifierEvent(EntityViewRenderEvent.FOVModifier event) {
        MCH_ViewEntityDummy viewer = MCH_ViewEntityDummy.getInstance((World)MCH_CameraManager.mc.field_71441_e);
        if (viewer == event.getEntity() || MCH_ItemRangeFinder.isUsingScope((EntityPlayer)MCH_CameraManager.mc.field_71439_g)) {
            event.setFOV(event.getFOV() * (1.0f / cameraZoom));
        }
    }

    public static void setCameraRoll(float roll) {
        cameraRoll = roll = MathHelper.func_76142_g((float)roll);
    }

    public static void setThirdPeasonCameraDistance(float distance) {
        cameraDistance = distance = MathHelper.func_76131_a((float)distance, (float)4.0f, (float)60.0f);
    }

    public static void setCameraZoom(float zoom) {
        cameraZoom = zoom;
    }

    public static float getThirdPeasonCameraDistance() {
        return cameraDistance;
    }

    public static void setRidingAircraft(@Nullable MCH_EntityAircraft aircraft) {
        ridingAircraft = aircraft;
    }
}

