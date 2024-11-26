/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package mcheli.aircraft;

import java.nio.FloatBuffer;
import javax.annotation.Nullable;
import mcheli.MCH_ClientCommonTickHandler;
import mcheli.MCH_ClientEventHook;
import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.__helper.MCH_ColorInt;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.client._IModelCustom;
import mcheli.__helper.client.renderer.MCH_Verts;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_BoundingBox;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.aircraft.MCH_IEntityCanRideAircraft;
import mcheli.aircraft.MCH_SeatInfo;
import mcheli.debug._v3.WeaponPointRenderer;
import mcheli.gui.MCH_Gui;
import mcheli.lweapon.MCH_ClientLightWeaponTickHandler;
import mcheli.multiplay.MCH_GuiTargetMarker;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.weapon.MCH_WeaponGuidanceSystem;
import mcheli.weapon.MCH_WeaponSet;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_EntityRenderer;
import mcheli.wrapper.W_Lib;
import mcheli.wrapper.W_Render;
import mcheli.wrapper.modelloader.W_ModelCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public abstract class MCH_RenderAircraft<T extends MCH_EntityAircraft>
extends W_Render<T> {
    public static boolean renderingEntity = false;
    public static _IModelCustom debugModel = null;

    protected MCH_RenderAircraft(RenderManager renderManager) {
        super(renderManager);
    }

    public void doRender(T entity, double posX, double posY, double posZ, float par8, float tickTime) {
        T ac = entity;
        MCH_AircraftInfo info = ((MCH_EntityAircraft)ac).getAcInfo();
        if (info != null) {
            GL11.glPushMatrix();
            float yaw = this.calcRot(((MCH_EntityAircraft)ac).getRotYaw(), ((MCH_EntityAircraft)ac).field_70126_B, tickTime);
            float pitch = ((MCH_EntityAircraft)ac).calcRotPitch(tickTime);
            float roll = this.calcRot(((MCH_EntityAircraft)ac).getRotRoll(), ((MCH_EntityAircraft)ac).prevRotationRoll, tickTime);
            if (MCH_Config.EnableModEntityRender.prmBool) {
                this.renderRiddenEntity((MCH_EntityAircraft)ac, tickTime, yaw, pitch + info.entityPitch, roll + info.entityRoll, info.entityWidth, info.entityHeight);
            }
            if (!MCH_RenderAircraft.shouldSkipRender(entity)) {
                this.setCommonRenderParam(info.smoothShading, ((MCH_EntityAircraft)ac).func_70070_b());
                if (((MCH_EntityAircraft)ac).isDestroyed()) {
                    GL11.glColor4f((float)0.15f, (float)0.15f, (float)0.15f, (float)1.0f);
                } else {
                    GL11.glColor4f((float)0.75f, (float)0.75f, (float)0.75f, (float)((float)MCH_Config.__TextureAlpha.prmDouble));
                }
                this.renderAircraft((MCH_EntityAircraft)ac, posX, posY, posZ, yaw, pitch, roll, tickTime);
                this.renderCommonPart((MCH_EntityAircraft)ac, info, posX, posY, posZ, tickTime);
                MCH_RenderAircraft.renderLight(posX, posY, posZ, tickTime, ac, info);
                this.restoreCommonRenderParam();
            }
            GL11.glPopMatrix();
            MCH_GuiTargetMarker.addMarkEntityPos(1, entity, posX, posY + (double)info.markerHeight, posZ);
            MCH_ClientLightWeaponTickHandler.markEntity(entity, posX, posY, posZ);
            MCH_RenderAircraft.renderEntityMarker(ac);
            if (MCH_Config.TestMode.prmBool) {
                WeaponPointRenderer.renderWeaponPoints(ac, info, posX, posY, posZ);
            }
        }
    }

    public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return true;
    }

    public static boolean shouldSkipRender(Entity entity) {
        MCH_IEntityCanRideAircraft e;
        if (entity instanceof MCH_IEntityCanRideAircraft ? (e = (MCH_IEntityCanRideAircraft)entity).isSkipNormalRender() : (entity.getClass().toString().indexOf("flansmod.common.driveables.EntityPlane") > 0 || entity.getClass().toString().indexOf("flansmod.common.driveables.EntityVehicle") > 0) && entity.func_184187_bx() instanceof MCH_EntitySeat) {
            return !renderingEntity;
        }
        return false;
    }

    public void func_76979_b(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        if (entity.func_90999_ad()) {
            this.renderEntityOnFire(entity, x, y, z, partialTicks);
        }
    }

    private void renderEntityOnFire(Entity entity, double x, double y, double z, float tick) {
        GL11.glDisable((int)2896);
        TextureMap texturemap = Minecraft.func_71410_x().func_147117_R();
        TextureAtlasSprite textureatlassprite = texturemap.func_110572_b("minecraft:blocks/fire_layer_0");
        TextureAtlasSprite textureatlassprite1 = texturemap.func_110572_b("minecraft:blocks/fire_layer_1");
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)x), (float)((float)y), (float)((float)z));
        float f1 = entity.field_70130_N * 1.4f;
        GL11.glScalef((float)(f1 * 2.0f), (float)(f1 * 2.0f), (float)(f1 * 2.0f));
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        float f2 = 1.5f;
        float f3 = 0.0f;
        float f4 = entity.field_70131_O / f1;
        float f5 = (float)(entity.field_70163_u + entity.func_174813_aQ().field_72338_b);
        GL11.glRotatef((float)(-this.field_76990_c.field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glTranslatef((float)0.0f, (float)0.0f, (float)(-0.3f + (float)((int)f4) * 0.02f));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f6 = 0.0f;
        int i = 0;
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        while (f4 > 0.0f) {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
            this.func_110776_a(TextureMap.field_110575_b);
            float f7 = textureatlassprite2.func_94209_e();
            float f8 = textureatlassprite2.func_94206_g();
            float f9 = textureatlassprite2.func_94212_f();
            float f10 = textureatlassprite2.func_94210_h();
            if (i / 2 % 2 == 0) {
                float f11 = f9;
                f9 = f7;
                f7 = f11;
            }
            bufferbuilder.func_181662_b((double)(f2 - f3), (double)(0.0f - f5), (double)f6).func_187315_a((double)f9, (double)f10).func_181675_d();
            bufferbuilder.func_181662_b((double)(-f2 - f3), (double)(0.0f - f5), (double)f6).func_187315_a((double)f7, (double)f10).func_181675_d();
            bufferbuilder.func_181662_b((double)(-f2 - f3), (double)(1.4f - f5), (double)f6).func_187315_a((double)f7, (double)f8).func_181675_d();
            bufferbuilder.func_181662_b((double)(f2 - f3), (double)(1.4f - f5), (double)f6).func_187315_a((double)f9, (double)f8).func_181675_d();
            f4 -= 0.45f;
            f5 -= 0.45f;
            f2 *= 0.9f;
            f6 += 0.03f;
            ++i;
        }
        tessellator.func_78381_a();
        GL11.glPopMatrix();
        GL11.glEnable((int)2896);
    }

    public static void renderLight(double x, double y, double z, float tickTime, MCH_EntityAircraft ac, MCH_AircraftInfo info) {
        if (!ac.haveSearchLight()) {
            return;
        }
        if (!ac.isSearchLightON()) {
            return;
        }
        Entity entity = ac.getEntityBySeatId(1);
        if (entity != null) {
            ac.lastSearchLightYaw = entity.field_70177_z;
            ac.lastSearchLightPitch = entity.field_70125_A;
        } else {
            entity = ac.getEntityBySeatId(0);
            if (entity != null) {
                ac.lastSearchLightYaw = entity.field_70177_z;
                ac.lastSearchLightPitch = entity.field_70125_A;
            }
        }
        float yaw = ac.lastSearchLightYaw;
        float pitch = ac.lastSearchLightPitch;
        RenderHelper.func_74518_a();
        GL11.glDisable((int)3553);
        GL11.glShadeModel((int)7425);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)1);
        GL11.glDisable((int)3008);
        GL11.glDisable((int)2884);
        GL11.glDepthMask((boolean)false);
        float rot = ac.prevRotYawWheel + (ac.rotYawWheel - ac.prevRotYawWheel) * tickTime;
        for (MCH_AircraftInfo.SearchLight sl : info.searchLights) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)sl.pos.field_72450_a, (double)sl.pos.field_72448_b, (double)sl.pos.field_72449_c);
            if (!sl.fixDir) {
                GL11.glRotatef((float)(yaw - ac.getRotYaw() + sl.yaw), (float)0.0f, (float)-1.0f, (float)0.0f);
                GL11.glRotatef((float)(pitch + 90.0f - ac.getRotPitch() + sl.pitch), (float)1.0f, (float)0.0f, (float)0.0f);
            } else {
                float stRot = 0.0f;
                if (sl.steering) {
                    stRot = -rot * sl.stRot;
                }
                GL11.glRotatef((float)(0.0f + sl.yaw + stRot), (float)0.0f, (float)-1.0f, (float)0.0f);
                GL11.glRotatef((float)(90.0f + sl.pitch), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            float height = sl.height;
            float width = sl.width / 2.0f;
            Tessellator tessellator = Tessellator.func_178181_a();
            BufferBuilder builder = tessellator.func_178180_c();
            builder.func_181668_a(6, DefaultVertexFormats.field_181706_f);
            MCH_ColorInt cs = new MCH_ColorInt(sl.colorStart);
            MCH_ColorInt ce = new MCH_ColorInt(sl.colorEnd);
            builder.func_181662_b(0.0, 0.0, 0.0).func_181669_b(cs.r, cs.g, cs.b, cs.a).func_181675_d();
            for (int i = 0; i < 25; ++i) {
                float angle = (float)(15.0 * (double)i / 180.0 * Math.PI);
                builder.func_181662_b((double)(MathHelper.func_76126_a((float)angle) * width), (double)height, (double)(MathHelper.func_76134_b((float)angle) * width)).func_181669_b(ce.r, ce.g, ce.b, ce.a).func_181675_d();
            }
            tessellator.func_78381_a();
            GL11.glPopMatrix();
        }
        GL11.glDepthMask((boolean)true);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)3008);
        GL11.glBlendFunc((int)770, (int)771);
        RenderHelper.func_74519_b();
    }

    protected void bindTexture(String path, MCH_EntityAircraft ac) {
        if (ac == MCH_ClientCommonTickHandler.ridingAircraft) {
            int bk = MCH_ClientCommonTickHandler.cameraMode;
            MCH_ClientCommonTickHandler.cameraMode = 0;
            super.func_110776_a(MCH_Utils.suffix(path));
            MCH_ClientCommonTickHandler.cameraMode = bk;
        } else {
            super.func_110776_a(MCH_Utils.suffix(path));
        }
    }

    public void renderRiddenEntity(MCH_EntityAircraft ac, float tickTime, float yaw, float pitch, float roll, float width, float height) {
        MCH_ClientEventHook.setCancelRender(false);
        GL11.glPushMatrix();
        this.renderEntitySimple(ac, ac.getRiddenByEntity(), tickTime, yaw, pitch, roll, width, height);
        for (MCH_EntitySeat s : ac.getSeats()) {
            if (s == null) continue;
            this.renderEntitySimple(ac, s.getRiddenByEntity(), tickTime, yaw, pitch, roll, width, height);
        }
        GL11.glPopMatrix();
        MCH_ClientEventHook.setCancelRender(true);
    }

    public void renderEntitySimple(MCH_EntityAircraft ac, Entity entity, float tickTime, float yaw, float pitch, float roll, float width, float height) {
        if (entity != null) {
            boolean isPilot = ac.isPilot(entity);
            boolean isClientPlayer = W_Lib.isClientPlayer(entity);
            if (!isClientPlayer || !W_Lib.isFirstPerson() || isClientPlayer && isPilot && ac.getCameraId() > 0) {
                GL11.glPushMatrix();
                if (entity.field_70173_aa == 0) {
                    entity.field_70142_S = entity.field_70165_t;
                    entity.field_70137_T = entity.field_70163_u;
                    entity.field_70136_U = entity.field_70161_v;
                }
                double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)tickTime;
                double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)tickTime;
                double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)tickTime;
                float f1 = entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * tickTime;
                int i = entity.func_70070_b();
                if (entity.func_70027_ad()) {
                    i = 0xF000F0;
                }
                int j = i % 65536;
                int k = i / 65536;
                OpenGlHelper.func_77475_a((int)OpenGlHelper.field_77476_b, (float)((float)j / 1.0f), (float)((float)k / 1.0f));
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                double dx = x - TileEntityRendererDispatcher.field_147554_b;
                double dy = y - TileEntityRendererDispatcher.field_147555_c;
                double dz = z - TileEntityRendererDispatcher.field_147552_d;
                GL11.glTranslated((double)dx, (double)dy, (double)dz);
                GL11.glRotatef((float)yaw, (float)0.0f, (float)-1.0f, (float)0.0f);
                GL11.glRotatef((float)pitch, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)roll, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glScaled((double)width, (double)height, (double)width);
                GL11.glRotatef((float)(-yaw), (float)0.0f, (float)-1.0f, (float)0.0f);
                GL11.glTranslated((double)(-dx), (double)(-dy), (double)(-dz));
                boolean bk = renderingEntity;
                renderingEntity = true;
                Entity ridingEntity = entity.func_184187_bx();
                if (!W_Lib.isEntityLivingBase(entity) && !(entity instanceof MCH_IEntityCanRideAircraft)) {
                    entity.func_184210_p();
                }
                EntityLivingBase entityLiving = entity instanceof EntityLivingBase ? (EntityLivingBase)entity : null;
                float bkPitch = 0.0f;
                float bkPrevPitch = 0.0f;
                if (isPilot && entityLiving != null) {
                    entityLiving.field_70761_aq = ac.getRotYaw();
                    entityLiving.field_70760_ar = ac.getRotYaw();
                    if (ac.getCameraId() > 0) {
                        entityLiving.field_70759_as = ac.getRotYaw();
                        entityLiving.field_70758_at = ac.getRotYaw();
                        bkPitch = entityLiving.field_70125_A;
                        bkPrevPitch = entityLiving.field_70127_C;
                        entityLiving.field_70125_A = ac.getRotPitch();
                        entityLiving.field_70127_C = ac.getRotPitch();
                    }
                }
                if (isClientPlayer) {
                    Entity viewEntity = this.field_76990_c.field_78734_h;
                    this.field_76990_c.field_78734_h = entity;
                    W_EntityRenderer.renderEntityWithPosYaw(this.field_76990_c, entity, dx, dy, dz, f1, tickTime, false);
                    this.field_76990_c.field_78734_h = viewEntity;
                } else {
                    W_EntityRenderer.renderEntityWithPosYaw(this.field_76990_c, entity, dx, dy, dz, f1, tickTime, false);
                }
                if (isPilot && entityLiving != null && ac.getCameraId() > 0) {
                    entityLiving.field_70125_A = bkPitch;
                    entityLiving.field_70127_C = bkPrevPitch;
                }
                entity.func_184220_m(ridingEntity);
                renderingEntity = bk;
                GL11.glPopMatrix();
            }
        }
    }

    public static void Test_Material(int light, float a, float b, float c) {
        GL11.glMaterial((int)1032, (int)light, (FloatBuffer)MCH_RenderAircraft.setColorBuffer(a, b, c, 1.0f));
    }

    public static void Test_Light(int light, float a, float b, float c) {
        GL11.glLight((int)16384, (int)light, (FloatBuffer)MCH_RenderAircraft.setColorBuffer(a, b, c, 1.0f));
        GL11.glLight((int)16385, (int)light, (FloatBuffer)MCH_RenderAircraft.setColorBuffer(a, b, c, 1.0f));
    }

    public abstract void renderAircraft(MCH_EntityAircraft var1, double var2, double var4, double var6, float var8, float var9, float var10, float var11);

    public float calcRot(float rot, float prevRot, float tickTime) {
        if ((rot = MathHelper.func_76142_g((float)rot)) - (prevRot = MathHelper.func_76142_g((float)prevRot)) < -180.0f) {
            prevRot -= 360.0f;
        } else if (prevRot - rot < -180.0f) {
            prevRot += 360.0f;
        }
        return prevRot + (rot - prevRot) * tickTime;
    }

    public void renderDebugHitBox(MCH_EntityAircraft e, double x, double y, double z, float yaw, float pitch) {
        if (MCH_Config.TestMode.prmBool && debugModel != null) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)x, (double)y, (double)z);
            GL11.glScalef((float)e.field_70130_N, (float)e.field_70131_O, (float)e.field_70130_N);
            this.bindTexture("textures/hit_box.png");
            debugModel.renderAll();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)x, (double)y, (double)z);
            for (MCH_BoundingBox bb : e.extraBoundingBox) {
                GL11.glPushMatrix();
                GL11.glTranslated((double)bb.rotatedOffset.field_72450_a, (double)bb.rotatedOffset.field_72448_b, (double)bb.rotatedOffset.field_72449_c);
                GL11.glPushMatrix();
                GL11.glScalef((float)bb.width, (float)bb.height, (float)bb.width);
                this.bindTexture("textures/bounding_box.png");
                debugModel.renderAll();
                GL11.glPopMatrix();
                this.drawHitBoxDetail(bb);
                GL11.glPopMatrix();
            }
            GL11.glPopMatrix();
        }
    }

    public void drawHitBoxDetail(MCH_BoundingBox bb) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f1 = 0.080000006f;
        String s = String.format("%.2f", Float.valueOf(bb.damegeFactor));
        GL11.glPushMatrix();
        GL11.glTranslatef((float)0.0f, (float)(0.5f + (float)(bb.offsetY * 0.0 + (double)bb.height)), (float)0.0f);
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-this.field_76990_c.field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)this.field_76990_c.field_78732_j, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glScalef((float)(-f1), (float)(-f1), (float)f1);
        GL11.glDisable((int)2896);
        GL11.glEnable((int)3042);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glDisable((int)3553);
        FontRenderer fontrenderer = this.func_76983_a();
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        int i = fontrenderer.func_78256_a(s) / 2;
        builder.func_181662_b((double)(-i - 1), -1.0, 0.1).func_181666_a(0.0f, 0.0f, 0.0f, 0.4f).func_181675_d();
        builder.func_181662_b((double)(-i - 1), 8.0, 0.1).func_181666_a(0.0f, 0.0f, 0.0f, 0.4f).func_181675_d();
        builder.func_181662_b((double)(i + 1), 8.0, 0.1).func_181666_a(0.0f, 0.0f, 0.0f, 0.4f).func_181675_d();
        builder.func_181662_b((double)(i + 1), -1.0, 0.1).func_181666_a(0.0f, 0.0f, 0.0f, 0.4f).func_181675_d();
        tessellator.func_78381_a();
        GL11.glEnable((int)3553);
        GL11.glDepthMask((boolean)false);
        int color = bb.damegeFactor > 1.0f ? 0xFF0000 : (bb.damegeFactor < 1.0f ? 65535 : 0xFFFFFF);
        fontrenderer.func_78276_b(s, -fontrenderer.func_78256_a(s) / 2, 0, 0xC0000000 | color);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2896);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public void renderDebugPilotSeat(MCH_EntityAircraft e, double x, double y, double z, float yaw, float pitch, float roll) {
        if (MCH_Config.TestMode.prmBool && debugModel != null) {
            GL11.glPushMatrix();
            MCH_SeatInfo seat = e.getSeatInfo(0);
            GL11.glTranslated((double)x, (double)y, (double)z);
            GL11.glRotatef((float)yaw, (float)0.0f, (float)-1.0f, (float)0.0f);
            GL11.glRotatef((float)pitch, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glRotatef((float)roll, (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glTranslated((double)seat.pos.field_72450_a, (double)seat.pos.field_72448_b, (double)seat.pos.field_72449_c);
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            this.bindTexture("textures/seat_pilot.png");
            debugModel.renderAll();
            GL11.glPopMatrix();
        }
    }

    public static void renderBody(@Nullable _IModelCustom model) {
        if (model != null) {
            if (model instanceof W_ModelCustom) {
                if (((W_ModelCustom)model).containsPart("$body")) {
                    model.renderPart("$body");
                } else {
                    model.renderAll();
                }
            } else {
                model.renderAll();
            }
        }
    }

    public static void renderPart(@Nullable _IModelCustom model, @Nullable _IModelCustom modelBody, String partName) {
        if (model != null) {
            model.renderAll();
        } else if (modelBody instanceof W_ModelCustom && ((W_ModelCustom)modelBody).containsPart("$" + partName)) {
            modelBody.renderPart("$" + partName);
        }
    }

    public void renderCommonPart(MCH_EntityAircraft ac, MCH_AircraftInfo info, double x, double y, double z, float tickTime) {
        MCH_RenderAircraft.renderRope(ac, info, x, y, z, tickTime);
        MCH_RenderAircraft.renderWeapon(ac, info, tickTime);
        MCH_RenderAircraft.renderRotPart(ac, info, tickTime);
        MCH_RenderAircraft.renderHatch(ac, info, tickTime);
        MCH_RenderAircraft.renderTrackRoller(ac, info, tickTime);
        MCH_RenderAircraft.renderCrawlerTrack(ac, info, tickTime);
        MCH_RenderAircraft.renderSteeringWheel(ac, info, tickTime);
        MCH_RenderAircraft.renderLightHatch(ac, info, tickTime);
        MCH_RenderAircraft.renderWheel(ac, info, tickTime);
        MCH_RenderAircraft.renderThrottle(ac, info, tickTime);
        MCH_RenderAircraft.renderCamera(ac, info, tickTime);
        MCH_RenderAircraft.renderLandingGear(ac, info, tickTime);
        MCH_RenderAircraft.renderWeaponBay(ac, info, tickTime);
        MCH_RenderAircraft.renderCanopy(ac, info, tickTime);
    }

    public static void renderLightHatch(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        if (info.lightHatchList.size() <= 0) {
            return;
        }
        float rot = ac.prevRotLightHatch + (ac.rotLightHatch - ac.prevRotLightHatch) * tickTime;
        for (MCH_AircraftInfo.Hatch t : info.lightHatchList) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)t.pos.field_72450_a, (double)t.pos.field_72448_b, (double)t.pos.field_72449_c);
            GL11.glRotated((double)(rot * t.maxRot), (double)t.rot.field_72450_a, (double)t.rot.field_72448_b, (double)t.rot.field_72449_c);
            GL11.glTranslated((double)(-t.pos.field_72450_a), (double)(-t.pos.field_72448_b), (double)(-t.pos.field_72449_c));
            MCH_RenderAircraft.renderPart(t.model, info.model, t.modelName);
            GL11.glPopMatrix();
        }
    }

    public static void renderSteeringWheel(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        if (info.partSteeringWheel.size() <= 0) {
            return;
        }
        float rot = ac.prevRotYawWheel + (ac.rotYawWheel - ac.prevRotYawWheel) * tickTime;
        for (MCH_AircraftInfo.PartWheel t : info.partSteeringWheel) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)t.pos.field_72450_a, (double)t.pos.field_72448_b, (double)t.pos.field_72449_c);
            GL11.glRotated((double)(rot * t.rotDir), (double)t.rot.field_72450_a, (double)t.rot.field_72448_b, (double)t.rot.field_72449_c);
            GL11.glTranslated((double)(-t.pos.field_72450_a), (double)(-t.pos.field_72448_b), (double)(-t.pos.field_72449_c));
            MCH_RenderAircraft.renderPart(t.model, info.model, t.modelName);
            GL11.glPopMatrix();
        }
    }

    public static void renderWheel(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        if (info.partWheel.size() <= 0) {
            return;
        }
        float yaw = ac.prevRotYawWheel + (ac.rotYawWheel - ac.prevRotYawWheel) * tickTime;
        for (MCH_AircraftInfo.PartWheel t : info.partWheel) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)t.pos2.field_72450_a, (double)t.pos2.field_72448_b, (double)t.pos2.field_72449_c);
            GL11.glRotated((double)(yaw * t.rotDir), (double)t.rot.field_72450_a, (double)t.rot.field_72448_b, (double)t.rot.field_72449_c);
            GL11.glTranslated((double)(-t.pos2.field_72450_a), (double)(-t.pos2.field_72448_b), (double)(-t.pos2.field_72449_c));
            GL11.glTranslated((double)t.pos.field_72450_a, (double)t.pos.field_72448_b, (double)t.pos.field_72449_c);
            GL11.glRotatef((float)(ac.prevRotWheel + (ac.rotWheel - ac.prevRotWheel) * tickTime), (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glTranslated((double)(-t.pos.field_72450_a), (double)(-t.pos.field_72448_b), (double)(-t.pos.field_72449_c));
            MCH_RenderAircraft.renderPart(t.model, info.model, t.modelName);
            GL11.glPopMatrix();
        }
    }

    public static void renderRotPart(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        if (!ac.haveRotPart()) {
            return;
        }
        for (int i = 0; i < ac.rotPartRotation.length; ++i) {
            float prevRot = ac.prevRotPartRotation[i];
            float rot = ac.rotPartRotation[i];
            if (prevRot > rot) {
                rot += 360.0f;
            }
            rot = MCH_Lib.smooth(rot, prevRot, tickTime);
            MCH_AircraftInfo.RotPart h = info.partRotPart.get(i);
            GL11.glPushMatrix();
            GL11.glTranslated((double)h.pos.field_72450_a, (double)h.pos.field_72448_b, (double)h.pos.field_72449_c);
            GL11.glRotatef((float)rot, (float)((float)h.rot.field_72450_a), (float)((float)h.rot.field_72448_b), (float)((float)h.rot.field_72449_c));
            GL11.glTranslated((double)(-h.pos.field_72450_a), (double)(-h.pos.field_72448_b), (double)(-h.pos.field_72449_c));
            MCH_RenderAircraft.renderPart(h.model, info.model, h.modelName);
            GL11.glPopMatrix();
        }
    }

    public static void renderWeapon(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        MCH_WeaponSet beforeWs = null;
        Entity e = ac.getRiddenByEntity();
        int weaponIndex = 0;
        for (MCH_AircraftInfo.PartWeapon w : info.partWeapon) {
            MCH_WeaponSet ws = ac.getWeaponByName(w.name[0]);
            if (ws != beforeWs) {
                weaponIndex = 0;
                beforeWs = ws;
            }
            float rotYaw = 0.0f;
            float prevYaw = 0.0f;
            float rotPitch = 0.0f;
            float prevPitch = 0.0f;
            if (w.hideGM && W_Lib.isFirstPerson()) {
                if (ws != null) {
                    boolean hide = false;
                    for (String s : w.name) {
                        if (!W_Lib.isClientPlayer(ac.getWeaponUserByWeaponName(s))) continue;
                        hide = true;
                        break;
                    }
                    if (hide) {
                        continue;
                    }
                } else if (ac.isMountedEntity(MCH_Lib.getClientPlayer())) continue;
            }
            GL11.glPushMatrix();
            if (w.turret) {
                GL11.glTranslated((double)info.turretPosition.field_72450_a, (double)info.turretPosition.field_72448_b, (double)info.turretPosition.field_72449_c);
                float ty = MCH_Lib.smooth(ac.getLastRiderYaw() - ac.getRotYaw(), ac.prevLastRiderYaw - ac.field_70126_B, tickTime);
                GL11.glRotatef((float)ty, (float)0.0f, (float)-1.0f, (float)0.0f);
                GL11.glTranslated((double)(-info.turretPosition.field_72450_a), (double)(-info.turretPosition.field_72448_b), (double)(-info.turretPosition.field_72449_c));
            }
            GL11.glTranslated((double)w.pos.field_72450_a, (double)w.pos.field_72448_b, (double)w.pos.field_72449_c);
            if (w.yaw) {
                if (ws != null) {
                    rotYaw = ws.rotationYaw - ws.defaultRotationYaw;
                    prevYaw = ws.prevRotationYaw - ws.defaultRotationYaw;
                } else if (e != null) {
                    rotYaw = e.field_70177_z - ac.getRotYaw();
                    prevYaw = e.field_70126_B - ac.field_70126_B;
                } else {
                    rotYaw = ac.getLastRiderYaw() - ac.field_70177_z;
                    prevYaw = ac.prevLastRiderYaw - ac.field_70126_B;
                }
                if (rotYaw - prevYaw > 180.0f) {
                    prevYaw += 360.0f;
                } else if (rotYaw - prevYaw < -180.0f) {
                    prevYaw -= 360.0f;
                }
                GL11.glRotatef((float)(prevYaw + (rotYaw - prevYaw) * tickTime), (float)0.0f, (float)-1.0f, (float)0.0f);
            }
            if (w.turret) {
                float ty = MCH_Lib.smooth(ac.getLastRiderYaw() - ac.getRotYaw(), ac.prevLastRiderYaw - ac.field_70126_B, tickTime);
                GL11.glRotatef((float)(-(ty -= ws.rotationTurretYaw)), (float)0.0f, (float)-1.0f, (float)0.0f);
            }
            boolean rev_sign = false;
            if (ws != null && (int)ws.defaultRotationYaw != 0) {
                float t = MathHelper.func_76142_g((float)ws.defaultRotationYaw);
                rev_sign = t >= 45.0f && t <= 135.0f || t <= -45.0f && t >= -135.0f;
                GL11.glRotatef((float)(-ws.defaultRotationYaw), (float)0.0f, (float)-1.0f, (float)0.0f);
            }
            if (w.pitch) {
                if (ws != null) {
                    rotPitch = ws.rotationPitch;
                    prevPitch = ws.prevRotationPitch;
                } else if (e != null) {
                    rotPitch = e.field_70125_A;
                    prevPitch = e.field_70127_C;
                } else {
                    rotPitch = ac.getLastRiderPitch();
                    prevPitch = ac.prevLastRiderPitch;
                }
                if (rev_sign) {
                    rotPitch = -rotPitch;
                    prevPitch = -prevPitch;
                }
                GL11.glRotatef((float)(prevPitch + (rotPitch - prevPitch) * tickTime), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (ws != null && w.recoilBuf != 0.0f) {
                MCH_WeaponSet.Recoil r = ws.recoilBuf[0];
                if (w.name.length > 1) {
                    for (String wnm : w.name) {
                        MCH_WeaponSet tws = ac.getWeaponByName(wnm);
                        if (tws == null || !(tws.recoilBuf[0].recoilBuf > r.recoilBuf)) continue;
                        r = tws.recoilBuf[0];
                    }
                }
                float recoilBuf = r.prevRecoilBuf + (r.recoilBuf - r.prevRecoilBuf) * tickTime;
                GL11.glTranslated((double)0.0, (double)0.0, (double)(w.recoilBuf * recoilBuf));
            }
            if (ws != null) {
                GL11.glRotatef((float)ws.defaultRotationYaw, (float)0.0f, (float)-1.0f, (float)0.0f);
                if (w.rotBarrel) {
                    float rotBrl = ws.prevRotBarrel + (ws.rotBarrel - ws.prevRotBarrel) * tickTime;
                    GL11.glRotatef((float)rotBrl, (float)((float)w.rot.field_72450_a), (float)((float)w.rot.field_72448_b), (float)((float)w.rot.field_72449_c));
                }
            }
            GL11.glTranslated((double)(-w.pos.field_72450_a), (double)(-w.pos.field_72448_b), (double)(-w.pos.field_72449_c));
            if (!w.isMissile || !ac.isWeaponNotCooldown(ws, weaponIndex)) {
                MCH_RenderAircraft.renderPart(w.model, info.model, w.modelName);
                for (MCH_AircraftInfo.PartWeaponChild wc : w.child) {
                    GL11.glPushMatrix();
                    MCH_RenderAircraft.renderWeaponChild(ac, info, wc, ws, e, tickTime);
                    GL11.glPopMatrix();
                }
            }
            GL11.glPopMatrix();
            ++weaponIndex;
        }
    }

    public static void renderWeaponChild(MCH_EntityAircraft ac, MCH_AircraftInfo info, MCH_AircraftInfo.PartWeaponChild w, MCH_WeaponSet ws, Entity e, float tickTime) {
        float rotYaw = 0.0f;
        float prevYaw = 0.0f;
        float rotPitch = 0.0f;
        float prevPitch = 0.0f;
        GL11.glTranslated((double)w.pos.field_72450_a, (double)w.pos.field_72448_b, (double)w.pos.field_72449_c);
        if (w.yaw) {
            if (ws != null) {
                rotYaw = ws.rotationYaw - ws.defaultRotationYaw;
                prevYaw = ws.prevRotationYaw - ws.defaultRotationYaw;
            } else if (e != null) {
                rotYaw = e.field_70177_z - ac.getRotYaw();
                prevYaw = e.field_70126_B - ac.field_70126_B;
            } else {
                rotYaw = ac.getLastRiderYaw() - ac.field_70177_z;
                prevYaw = ac.prevLastRiderYaw - ac.field_70126_B;
            }
            if (rotYaw - prevYaw > 180.0f) {
                prevYaw += 360.0f;
            } else if (rotYaw - prevYaw < -180.0f) {
                prevYaw -= 360.0f;
            }
            GL11.glRotatef((float)(prevYaw + (rotYaw - prevYaw) * tickTime), (float)0.0f, (float)-1.0f, (float)0.0f);
        }
        boolean rev_sign = false;
        if (ws != null && (int)ws.defaultRotationYaw != 0) {
            float t = MathHelper.func_76142_g((float)ws.defaultRotationYaw);
            rev_sign = t >= 45.0f && t <= 135.0f || t <= -45.0f && t >= -135.0f;
            GL11.glRotatef((float)(-ws.defaultRotationYaw), (float)0.0f, (float)-1.0f, (float)0.0f);
        }
        if (w.pitch) {
            if (ws != null) {
                rotPitch = ws.rotationPitch;
                prevPitch = ws.prevRotationPitch;
            } else if (e != null) {
                rotPitch = e.field_70125_A;
                prevPitch = e.field_70127_C;
            } else {
                rotPitch = ac.getLastRiderPitch();
                prevPitch = ac.prevLastRiderPitch;
            }
            if (rev_sign) {
                rotPitch = -rotPitch;
                prevPitch = -prevPitch;
            }
            GL11.glRotatef((float)(prevPitch + (rotPitch - prevPitch) * tickTime), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        if (ws != null && w.recoilBuf != 0.0f) {
            MCH_WeaponSet.Recoil r = ws.recoilBuf[0];
            if (w.name.length > 1) {
                for (String wnm : w.name) {
                    MCH_WeaponSet tws = ac.getWeaponByName(wnm);
                    if (tws == null || !(tws.recoilBuf[0].recoilBuf > r.recoilBuf)) continue;
                    r = tws.recoilBuf[0];
                }
            }
            float recoilBuf = r.prevRecoilBuf + (r.recoilBuf - r.prevRecoilBuf) * tickTime;
            GL11.glTranslated((double)0.0, (double)0.0, (double)(-w.recoilBuf * recoilBuf));
        }
        if (ws != null) {
            GL11.glRotatef((float)ws.defaultRotationYaw, (float)0.0f, (float)-1.0f, (float)0.0f);
        }
        GL11.glTranslated((double)(-w.pos.field_72450_a), (double)(-w.pos.field_72448_b), (double)(-w.pos.field_72449_c));
        MCH_RenderAircraft.renderPart(w.model, info.model, w.modelName);
    }

    public static void renderTrackRoller(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        if (info.partTrackRoller.size() <= 0) {
            return;
        }
        float[] rot = ac.rotTrackRoller;
        float[] prevRot = ac.prevRotTrackRoller;
        for (MCH_AircraftInfo.TrackRoller t : info.partTrackRoller) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)t.pos.field_72450_a, (double)t.pos.field_72448_b, (double)t.pos.field_72449_c);
            GL11.glRotatef((float)(prevRot[t.side] + (rot[t.side] - prevRot[t.side]) * tickTime), (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glTranslated((double)(-t.pos.field_72450_a), (double)(-t.pos.field_72448_b), (double)(-t.pos.field_72449_c));
            MCH_RenderAircraft.renderPart(t.model, info.model, t.modelName);
            GL11.glPopMatrix();
        }
    }

    public static void renderCrawlerTrack(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        if (info.partCrawlerTrack.size() <= 0) {
            return;
        }
        int prevWidth = GL11.glGetInteger((int)2833);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        for (MCH_AircraftInfo.CrawlerTrack c : info.partCrawlerTrack) {
            GL11.glPointSize((float)(c.len * 20.0f));
            if (MCH_Config.TestMode.prmBool) {
                GL11.glDisable((int)3553);
                GL11.glDisable((int)3042);
                builder.func_181668_a(0, DefaultVertexFormats.field_181706_f);
                for (int i = 0; i < c.cx.length; ++i) {
                    builder.func_181662_b((double)c.z, c.cx[i], c.cy[i]).func_181669_b((int)(255.0f / (float)c.cx.length * (float)i), 80, 255 - (int)(255.0f / (float)c.cx.length * (float)i), 255).func_181675_d();
                }
                tessellator.func_78381_a();
            }
            GL11.glEnable((int)3553);
            GL11.glEnable((int)3042);
            int L = c.lp.size() - 1;
            double rc = ac != null ? (double)ac.rotCrawlerTrack[c.side] : 0.0;
            double pc = ac != null ? (double)ac.prevRotCrawlerTrack[c.side] : 0.0;
            for (int i = 0; i < L; ++i) {
                MCH_AircraftInfo.CrawlerTrackPrm cp = c.lp.get(i);
                MCH_AircraftInfo.CrawlerTrackPrm np = c.lp.get((i + 1) % L);
                double x1 = cp.x;
                double x2 = np.x;
                double r1 = cp.r;
                double y1 = cp.y;
                double y2 = np.y;
                double r2 = np.r;
                if (r2 - r1 < -180.0) {
                    r2 += 360.0;
                }
                if (r2 - r1 > 180.0) {
                    r2 -= 360.0;
                }
                double sx = x1 + (x2 - x1) * rc;
                double sy = y1 + (y2 - y1) * rc;
                double sr = r1 + (r2 - r1) * rc;
                double ex = x1 + (x2 - x1) * pc;
                double ey = y1 + (y2 - y1) * pc;
                double er = r1 + (r2 - r1) * pc;
                double x = sx + (ex - sx) * pc;
                double y = sy + (ey - sy) * pc;
                double r = sr + (er - sr) * pc;
                GL11.glPushMatrix();
                GL11.glTranslated((double)0.0, (double)x, (double)y);
                GL11.glRotatef((float)((float)r), (float)-1.0f, (float)0.0f, (float)0.0f);
                MCH_RenderAircraft.renderPart(c.model, info.model, c.modelName);
                GL11.glPopMatrix();
            }
        }
        GL11.glEnable((int)3042);
        GL11.glPointSize((float)prevWidth);
    }

    public static void renderHatch(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        if (!info.haveHatch() || ac.partHatch == null) {
            return;
        }
        float rot = ac.getHatchRotation();
        float prevRot = ac.getPrevHatchRotation();
        for (MCH_AircraftInfo.Hatch h : info.hatchList) {
            GL11.glPushMatrix();
            if (h.isSlide) {
                float r = ac.partHatch.rotation / ac.partHatch.rotationMax;
                float pr = ac.partHatch.prevRotation / ac.partHatch.rotationMax;
                float f = pr + (r - pr) * tickTime;
                GL11.glTranslated((double)(h.pos.field_72450_a * (double)f), (double)(h.pos.field_72448_b * (double)f), (double)(h.pos.field_72449_c * (double)f));
            } else {
                GL11.glTranslated((double)h.pos.field_72450_a, (double)h.pos.field_72448_b, (double)h.pos.field_72449_c);
                GL11.glRotatef((float)((prevRot + (rot - prevRot) * tickTime) * h.maxRotFactor), (float)((float)h.rot.field_72450_a), (float)((float)h.rot.field_72448_b), (float)((float)h.rot.field_72449_c));
                GL11.glTranslated((double)(-h.pos.field_72450_a), (double)(-h.pos.field_72448_b), (double)(-h.pos.field_72449_c));
            }
            MCH_RenderAircraft.renderPart(h.model, info.model, h.modelName);
            GL11.glPopMatrix();
        }
    }

    public static void renderThrottle(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        if (!info.havePartThrottle()) {
            return;
        }
        float throttle = MCH_Lib.smooth((float)ac.getCurrentThrottle(), (float)ac.getPrevCurrentThrottle(), tickTime);
        for (MCH_AircraftInfo.Throttle h : info.partThrottle) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)h.pos.field_72450_a, (double)h.pos.field_72448_b, (double)h.pos.field_72449_c);
            GL11.glRotatef((float)(throttle * h.rot2), (float)((float)h.rot.field_72450_a), (float)((float)h.rot.field_72448_b), (float)((float)h.rot.field_72449_c));
            GL11.glTranslated((double)(-h.pos.field_72450_a), (double)(-h.pos.field_72448_b), (double)(-h.pos.field_72449_c));
            GL11.glTranslated((double)(h.slide.field_72450_a * (double)throttle), (double)(h.slide.field_72448_b * (double)throttle), (double)(h.slide.field_72449_c * (double)throttle));
            MCH_RenderAircraft.renderPart(h.model, info.model, h.modelName);
            GL11.glPopMatrix();
        }
    }

    public static void renderWeaponBay(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        for (int i = 0; i < info.partWeaponBay.size(); ++i) {
            MCH_AircraftInfo.WeaponBay w = info.partWeaponBay.get(i);
            MCH_EntityAircraft.WeaponBay ws = ac.weaponBays[i];
            GL11.glPushMatrix();
            if (w.isSlide) {
                float r = ws.rot / 90.0f;
                float pr = ws.prevRot / 90.0f;
                float f = pr + (r - pr) * tickTime;
                GL11.glTranslated((double)(w.pos.field_72450_a * (double)f), (double)(w.pos.field_72448_b * (double)f), (double)(w.pos.field_72449_c * (double)f));
            } else {
                GL11.glTranslated((double)w.pos.field_72450_a, (double)w.pos.field_72448_b, (double)w.pos.field_72449_c);
                GL11.glRotatef((float)((ws.prevRot + (ws.rot - ws.prevRot) * tickTime) * w.maxRotFactor), (float)((float)w.rot.field_72450_a), (float)((float)w.rot.field_72448_b), (float)((float)w.rot.field_72449_c));
                GL11.glTranslated((double)(-w.pos.field_72450_a), (double)(-w.pos.field_72448_b), (double)(-w.pos.field_72449_c));
            }
            MCH_RenderAircraft.renderPart(w.model, info.model, w.modelName);
            GL11.glPopMatrix();
        }
    }

    public static void renderCamera(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        if (!info.havePartCamera()) {
            return;
        }
        float rotYaw = ac.camera.partRotationYaw;
        float prevRotYaw = ac.camera.prevPartRotationYaw;
        float rotPitch = ac.camera.partRotationPitch;
        float prevRotPitch = ac.camera.prevPartRotationPitch;
        float yaw = prevRotYaw + (rotYaw - prevRotYaw) * tickTime - ac.getRotYaw();
        float pitch = prevRotPitch + (rotPitch - prevRotPitch) * tickTime - ac.getRotPitch();
        for (MCH_AircraftInfo.Camera c : info.cameraList) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)c.pos.field_72450_a, (double)c.pos.field_72448_b, (double)c.pos.field_72449_c);
            if (c.yawSync) {
                GL11.glRotatef((float)yaw, (float)0.0f, (float)-1.0f, (float)0.0f);
            }
            if (c.pitchSync) {
                GL11.glRotatef((float)pitch, (float)1.0f, (float)0.0f, (float)0.0f);
            }
            GL11.glTranslated((double)(-c.pos.field_72450_a), (double)(-c.pos.field_72448_b), (double)(-c.pos.field_72449_c));
            MCH_RenderAircraft.renderPart(c.model, info.model, c.modelName);
            GL11.glPopMatrix();
        }
    }

    public static void renderCanopy(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        if (info.haveCanopy() && ac.partCanopy != null) {
            float rot = ac.getCanopyRotation();
            float prevRot = ac.getPrevCanopyRotation();
            for (MCH_AircraftInfo.Canopy c : info.canopyList) {
                GL11.glPushMatrix();
                if (c.isSlide) {
                    float r = ac.partCanopy.rotation / ac.partCanopy.rotationMax;
                    float pr = ac.partCanopy.prevRotation / ac.partCanopy.rotationMax;
                    float f = pr + (r - pr) * tickTime;
                    GL11.glTranslated((double)(c.pos.field_72450_a * (double)f), (double)(c.pos.field_72448_b * (double)f), (double)(c.pos.field_72449_c * (double)f));
                } else {
                    GL11.glTranslated((double)c.pos.field_72450_a, (double)c.pos.field_72448_b, (double)c.pos.field_72449_c);
                    GL11.glRotatef((float)((prevRot + (rot - prevRot) * tickTime) * c.maxRotFactor), (float)((float)c.rot.field_72450_a), (float)((float)c.rot.field_72448_b), (float)((float)c.rot.field_72449_c));
                    GL11.glTranslated((double)(-c.pos.field_72450_a), (double)(-c.pos.field_72448_b), (double)(-c.pos.field_72449_c));
                }
                MCH_RenderAircraft.renderPart(c.model, info.model, c.modelName);
                GL11.glPopMatrix();
            }
        }
    }

    public static void renderLandingGear(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
        if (info.haveLandingGear() && ac.partLandingGear != null) {
            float rot = ac.getLandingGearRotation();
            float prevRot = ac.getPrevLandingGearRotation();
            float revR = 90.0f - rot;
            float revPr = 90.0f - prevRot;
            float rot1 = prevRot + (rot - prevRot) * tickTime;
            float rot1Rev = revPr + (revR - revPr) * tickTime;
            float rotHatch = 90.0f * MathHelper.func_76126_a((float)(rot1 * 2.0f * (float)Math.PI / 180.0f)) * 3.0f;
            if (rotHatch > 90.0f) {
                rotHatch = 90.0f;
            }
            for (MCH_AircraftInfo.LandingGear n : info.landingGear) {
                GL11.glPushMatrix();
                GL11.glTranslated((double)n.pos.field_72450_a, (double)n.pos.field_72448_b, (double)n.pos.field_72449_c);
                if (!n.reverse) {
                    if (!n.hatch) {
                        GL11.glRotatef((float)(rot1 * n.maxRotFactor), (float)((float)n.rot.field_72450_a), (float)((float)n.rot.field_72448_b), (float)((float)n.rot.field_72449_c));
                    } else {
                        GL11.glRotatef((float)(rotHatch * n.maxRotFactor), (float)((float)n.rot.field_72450_a), (float)((float)n.rot.field_72448_b), (float)((float)n.rot.field_72449_c));
                    }
                } else {
                    GL11.glRotatef((float)(rot1Rev * n.maxRotFactor), (float)((float)n.rot.field_72450_a), (float)((float)n.rot.field_72448_b), (float)((float)n.rot.field_72449_c));
                }
                if (n.enableRot2) {
                    if (!n.reverse) {
                        GL11.glRotatef((float)(rot1 * n.maxRotFactor2), (float)((float)n.rot2.field_72450_a), (float)((float)n.rot2.field_72448_b), (float)((float)n.rot2.field_72449_c));
                    } else {
                        GL11.glRotatef((float)(rot1Rev * n.maxRotFactor2), (float)((float)n.rot2.field_72450_a), (float)((float)n.rot2.field_72448_b), (float)((float)n.rot2.field_72449_c));
                    }
                }
                GL11.glTranslated((double)(-n.pos.field_72450_a), (double)(-n.pos.field_72448_b), (double)(-n.pos.field_72449_c));
                if (n.slide != null) {
                    float f = rot / 90.0f;
                    if (n.reverse) {
                        f = 1.0f - f;
                    }
                    GL11.glTranslated((double)((double)f * n.slide.field_72450_a), (double)((double)f * n.slide.field_72448_b), (double)((double)f * n.slide.field_72449_c));
                }
                MCH_RenderAircraft.renderPart(n.model, info.model, n.modelName);
                GL11.glPopMatrix();
            }
        }
    }

    public static void renderEntityMarker(Entity entity) {
        EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
        if (player == null) {
            return;
        }
        if (W_Entity.isEqual((Entity)player, entity)) {
            return;
        }
        MCH_EntityAircraft ac = null;
        if (player.func_184187_bx() instanceof MCH_EntityAircraft) {
            ac = (MCH_EntityAircraft)player.func_184187_bx();
        } else if (player.func_184187_bx() instanceof MCH_EntitySeat) {
            ac = ((MCH_EntitySeat)player.func_184187_bx()).getParent();
        } else if (player.func_184187_bx() instanceof MCH_EntityUavStation) {
            ac = ((MCH_EntityUavStation)player.func_184187_bx()).getControlAircract();
        }
        if (ac == null) {
            return;
        }
        if (W_Entity.isEqual(ac, entity)) {
            return;
        }
        MCH_WeaponGuidanceSystem gs = ac.getCurrentWeapon((Entity)player).getCurrentWeapon().getGuidanceSystem();
        if (gs == null || !gs.canLockEntity(entity)) {
            return;
        }
        RenderManager rm = Minecraft.func_71410_x().func_175598_ae();
        double dist = entity.func_70068_e(rm.field_78734_h);
        double x = entity.field_70165_t - TileEntityRendererDispatcher.field_147554_b;
        double y = entity.field_70163_u - TileEntityRendererDispatcher.field_147555_c;
        double z = entity.field_70161_v - TileEntityRendererDispatcher.field_147552_d;
        if (dist < 10000.0) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)((float)x), (float)((float)y + entity.field_70131_O + 0.5f), (float)((float)z));
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)(-rm.field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)rm.field_78732_j, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glScalef((float)-0.02666667f, (float)-0.02666667f, (float)0.02666667f);
            GL11.glDisable((int)2896);
            GL11.glTranslatef((float)0.0f, (float)9.374999f, (float)0.0f);
            GL11.glDepthMask((boolean)false);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            int prevWidth = GL11.glGetInteger((int)2849);
            float size = Math.max(entity.field_70130_N, entity.field_70131_O) * 20.0f;
            if (entity instanceof MCH_EntityAircraft) {
                size *= 2.0f;
            }
            Tessellator tessellator = Tessellator.func_178181_a();
            BufferBuilder builder = tessellator.func_178180_c();
            builder.func_181668_a(2, MCH_Verts.POS_COLOR_LMAP);
            boolean isLockEntity = gs.isLockingEntity(entity);
            if (isLockEntity) {
                GL11.glLineWidth((float)((float)MCH_Gui.scaleFactor * 1.5f));
                builder.func_181662_b((double)(-size - 1.0f), 0.0, 0.0).func_181666_a(1.0f, 0.0f, 0.0f, 1.0f).func_187314_a(0, 240).func_181675_d();
                builder.func_181662_b((double)(-size - 1.0f), (double)(size * 2.0f), 0.0).func_181666_a(1.0f, 0.0f, 0.0f, 1.0f).func_187314_a(0, 240).func_181675_d();
                builder.func_181662_b((double)(size + 1.0f), (double)(size * 2.0f), 0.0).func_181666_a(1.0f, 0.0f, 0.0f, 1.0f).func_187314_a(0, 240).func_181675_d();
                builder.func_181662_b((double)(size + 1.0f), 0.0, 0.0).func_181666_a(1.0f, 0.0f, 0.0f, 1.0f).func_187314_a(0, 240).func_181675_d();
            } else {
                GL11.glLineWidth((float)MCH_Gui.scaleFactor);
                builder.func_181662_b((double)(-size - 1.0f), 0.0, 0.0).func_181666_a(1.0f, 0.3f, 0.0f, 8.0f).func_187314_a(0, 240).func_181675_d();
                builder.func_181662_b((double)(-size - 1.0f), (double)(size * 2.0f), 0.0).func_181666_a(1.0f, 0.3f, 0.0f, 8.0f).func_187314_a(0, 240).func_181675_d();
                builder.func_181662_b((double)(size + 1.0f), (double)(size * 2.0f), 0.0).func_181666_a(1.0f, 0.3f, 0.0f, 8.0f).func_187314_a(0, 240).func_181675_d();
                builder.func_181662_b((double)(size + 1.0f), 0.0, 0.0).func_181666_a(1.0f, 0.3f, 0.0f, 8.0f).func_187314_a(0, 240).func_181675_d();
            }
            tessellator.func_78381_a();
            GL11.glPopMatrix();
            if (!ac.isUAV() && isLockEntity && Minecraft.func_71410_x().field_71474_y.field_74320_O == 0) {
                GL11.glPushMatrix();
                builder.func_181668_a(1, MCH_Verts.POS_COLOR_LMAP);
                GL11.glLineWidth((float)1.0f);
                builder.func_181662_b(x, y + (double)(entity.field_70131_O / 2.0f), z).func_181666_a(1.0f, 0.0f, 0.0f, 1.0f).func_187314_a(0, 240).func_181675_d();
                builder.func_181662_b(ac.field_70142_S - TileEntityRendererDispatcher.field_147554_b, ac.field_70137_T - TileEntityRendererDispatcher.field_147555_c - 1.0, ac.field_70136_U - TileEntityRendererDispatcher.field_147552_d).func_181666_a(1.0f, 0.0f, 0.0f, 1.0f).func_187314_a(0, 240).func_181675_d();
                tessellator.func_78381_a();
                GL11.glPopMatrix();
            }
            GL11.glLineWidth((float)prevWidth);
            GL11.glEnable((int)3553);
            GL11.glDepthMask((boolean)true);
            GL11.glEnable((int)2896);
            GL11.glDisable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    public static void renderRope(MCH_EntityAircraft ac, MCH_AircraftInfo info, double x, double y, double z, float tickTime) {
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        if (ac.isRepelling()) {
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            for (int i = 0; i < info.repellingHooks.size(); ++i) {
                builder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
                builder.func_181662_b(info.repellingHooks.get((int)i).pos.field_72450_a, info.repellingHooks.get((int)i).pos.field_72448_b, info.repellingHooks.get((int)i).pos.field_72449_c).func_181669_b(0, 0, 0, 255).func_181675_d();
                builder.func_181662_b(info.repellingHooks.get((int)i).pos.field_72450_a, info.repellingHooks.get((int)i).pos.field_72448_b + (double)ac.ropesLength, info.repellingHooks.get((int)i).pos.field_72449_c).func_181669_b(0, 0, 0, 255).func_181675_d();
                tessellator.func_78381_a();
            }
            GL11.glEnable((int)2896);
            GL11.glEnable((int)3553);
        }
        GL11.glPopMatrix();
    }
}

