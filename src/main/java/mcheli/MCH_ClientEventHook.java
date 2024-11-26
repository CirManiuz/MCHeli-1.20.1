/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.client.event.MouseEvent
 *  net.minecraftforge.client.event.RenderLivingEvent$Post
 *  net.minecraftforge.client.event.RenderLivingEvent$Pre
 *  net.minecraftforge.client.event.RenderLivingEvent$Specials$Post
 *  net.minecraftforge.client.event.RenderLivingEvent$Specials$Pre
 *  net.minecraftforge.client.event.RenderPlayerEvent$Post
 *  net.minecraftforge.client.event.RenderPlayerEvent$Pre
 *  net.minecraftforge.event.entity.EntityJoinWorldEvent
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  org.lwjgl.opengl.GL11
 */
package mcheli;

import java.util.ArrayList;
import java.util.List;
import mcheli.MCH_ClientCommonTickHandler;
import mcheli.MCH_ClientTickHandlerBase;
import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.MCH_TextureManagerDummy;
import mcheli.__helper.entity.ITargetMarkerObject;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.aircraft.MCH_RenderAircraft;
import mcheli.lweapon.MCH_ClientLightWeaponTickHandler;
import mcheli.multiplay.MCH_GuiTargetMarker;
import mcheli.particles.MCH_ParticlesUtil;
import mcheli.tool.rangefinder.MCH_ItemRangeFinder;
import mcheli.wrapper.W_ClientEventHook;
import mcheli.wrapper.W_Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.lwjgl.opengl.GL11;

public class MCH_ClientEventHook
extends W_ClientEventHook {
    MCH_TextureManagerDummy dummyTextureManager = null;
    public static List<MCH_EntityAircraft> haveSearchLightAircraft = new ArrayList<MCH_EntityAircraft>();
    private static final ResourceLocation ir_strobe = new ResourceLocation("mcheli", "textures/ir_strobe.png");
    private static boolean cancelRender = true;

    @Override
    public void renderLivingEventSpecialsPre(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        MCH_EntityAircraft ac;
        if (MCH_Config.DisableRenderLivingSpecials.prmBool && (ac = MCH_EntityAircraft.getAircraft_RiddenOrControl((Entity)Minecraft.func_71410_x().field_71439_g)) != null && ac.isMountedEntity((Entity)event.getEntity())) {
            event.setCanceled(true);
            return;
        }
    }

    @Override
    public void renderLivingEventSpecialsPost(RenderLivingEvent.Specials.Post<EntityLivingBase> event) {
    }

    private void renderIRStrobe(EntityLivingBase entity, RenderLivingEvent.Specials.Post<EntityLivingBase> event) {
        int cm = MCH_ClientCommonTickHandler.cameraMode;
        if (cm == 0) {
            return;
        }
        int ticks = entity.field_70173_aa % 20;
        if (ticks >= 4) {
            return;
        }
        float alpha = ticks == 2 || ticks == 1 ? 1.0f : 0.5f;
        EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
        if (player == null) {
            return;
        }
        if (!player.func_184191_r((Entity)entity)) {
            return;
        }
        int j = 240;
        int k = 240;
        OpenGlHelper.func_77475_a((int)OpenGlHelper.field_77476_b, (float)((float)j / 1.0f), (float)((float)k / 1.0f));
        RenderManager rm = event.getRenderer().func_177068_d();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f1 = 0.080000006f;
        GL11.glPushMatrix();
        GL11.glTranslated((double)event.getX(), (double)(event.getY() + (double)((float)((double)entity.field_70131_O * 0.75))), (double)event.getZ());
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-rm.field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)rm.field_78732_j, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glScalef((float)(-f1), (float)(-f1), (float)f1);
        GL11.glEnable((int)3042);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glEnable((int)3553);
        rm.field_78724_e.func_110577_a(ir_strobe);
        GL11.glAlphaFunc((int)516, (float)0.003921569f);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        int i = (int)Math.max(entity.field_70130_N, entity.field_70131_O) * 20;
        builder.func_181662_b((double)(-i), (double)(-i), 0.1).func_187315_a(0.0, 0.0).func_181666_a(1.0f, 1.0f, 1.0f, alpha * (cm == 1 ? 0.9f : 0.5f)).func_181675_d();
        builder.func_181662_b((double)(-i), (double)i, 0.1).func_187315_a(0.0, 1.0).func_181666_a(1.0f, 1.0f, 1.0f, alpha * (cm == 1 ? 0.9f : 0.5f)).func_181675_d();
        builder.func_181662_b((double)i, (double)i, 0.1).func_187315_a(1.0, 1.0).func_181666_a(1.0f, 1.0f, 1.0f, alpha * (cm == 1 ? 0.9f : 0.5f)).func_181675_d();
        builder.func_181662_b((double)i, (double)(-i), 0.1).func_187315_a(1.0, 0.0).func_181666_a(1.0f, 1.0f, 1.0f, alpha * (cm == 1 ? 0.9f : 0.5f)).func_181675_d();
        tessellator.func_78381_a();
        GL11.glEnable((int)2896);
        GL11.glPopMatrix();
    }

    @Override
    public void mouseEvent(MouseEvent event) {
        if (MCH_ClientTickHandlerBase.updateMouseWheel(event.getDwheel())) {
            event.setCanceled(true);
        }
    }

    public static void setCancelRender(boolean cancel) {
        cancelRender = cancel;
    }

    @Override
    public void renderLivingEventPre(RenderLivingEvent.Pre<EntityLivingBase> event) {
        RenderManager rm;
        for (MCH_EntityAircraft ac : haveSearchLightAircraft) {
            OpenGlHelper.func_77475_a((int)OpenGlHelper.field_77476_b, (float)ac.getSearchLightValue((Entity)event.getEntity()), (float)240.0f);
        }
        if (MCH_Config.EnableModEntityRender.prmBool && cancelRender && (event.getEntity().func_184187_bx() instanceof MCH_EntityAircraft || event.getEntity().func_184187_bx() instanceof MCH_EntitySeat)) {
            event.setCanceled(true);
            return;
        }
        if (MCH_Config.EnableReplaceTextureManager.prmBool && (rm = W_Reflection.getRenderManager(event.getRenderer())) != null && !(rm.field_78724_e instanceof MCH_TextureManagerDummy)) {
            if (this.dummyTextureManager == null) {
                this.dummyTextureManager = new MCH_TextureManagerDummy(rm.field_78724_e);
            }
            rm.field_78724_e = this.dummyTextureManager;
        }
    }

    @Override
    public void renderLivingEventPost(RenderLivingEvent.Post<EntityLivingBase> event) {
        MCH_RenderAircraft.renderEntityMarker((Entity)event.getEntity());
        if (event.getEntity() instanceof ITargetMarkerObject) {
            MCH_GuiTargetMarker.addMarkEntityPos(2, (ITargetMarkerObject)event.getEntity(), event.getX(), event.getY() + (double)event.getEntity().field_70131_O + 0.5, event.getZ());
        } else {
            MCH_GuiTargetMarker.addMarkEntityPos(2, ITargetMarkerObject.fromEntity((Entity)event.getEntity()), event.getX(), event.getY() + (double)event.getEntity().field_70131_O + 0.5, event.getZ());
        }
        MCH_ClientLightWeaponTickHandler.markEntity((Entity)event.getEntity(), event.getX(), event.getY() + (double)(event.getEntity().field_70131_O / 2.0f), event.getZ());
    }

    @Override
    public void renderPlayerPre(RenderPlayerEvent.Pre event) {
        MCH_EntityAircraft v;
        if (event.getEntity() == null) {
            return;
        }
        if (event.getEntity().func_184187_bx() instanceof MCH_EntityAircraft && (v = (MCH_EntityAircraft)event.getEntity().func_184187_bx()).getAcInfo() != null && v.getAcInfo().hideEntity) {
            event.setCanceled(true);
            return;
        }
    }

    @Override
    public void renderPlayerPost(RenderPlayerEvent.Post event) {
    }

    @Override
    public void worldEventUnload(WorldEvent.Unload event) {
    }

    @Override
    public void entityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity().func_70028_i(MCH_Lib.getClientPlayer())) {
            MCH_Lib.DbgLog(true, "MCH_ClientEventHook.entityJoinWorldEvent : " + event.getEntity(), new Object[0]);
            MCH_ItemRangeFinder.mode = Minecraft.func_71410_x().func_71356_B() ? 1 : 0;
            MCH_ParticlesUtil.clearMarkPoint();
        }
    }
}

