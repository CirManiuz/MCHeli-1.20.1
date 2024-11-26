/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.client.registry.IRenderFactory
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.gltd;

import java.util.Random;
import mcheli.MCH_RenderLib;
import mcheli.__helper.client._IModelCustom;
import mcheli.gltd.MCH_EntityGLTD;
import mcheli.wrapper.W_Lib;
import mcheli.wrapper.W_Render;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderGLTD
extends W_Render<MCH_EntityGLTD> {
    public static final IRenderFactory<MCH_EntityGLTD> FACTORY = MCH_RenderGLTD::new;
    public static final Random rand = new Random();
    public static _IModelCustom model;

    public MCH_RenderGLTD(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 0.5f;
    }

    public void doRender(MCH_EntityGLTD entity, double posX, double posY, double posZ, float par8, float tickTime) {
        MCH_EntityGLTD gltd = entity;
        GL11.glPushMatrix();
        GL11.glTranslated((double)posX, (double)(posY + 0.25), (double)posZ);
        this.setCommonRenderParam(true, entity.func_70070_b());
        this.bindTexture("textures/gltd.png");
        Minecraft mc = Minecraft.func_71410_x();
        boolean isNotRenderHead = false;
        if (gltd.getRiddenByEntity() != null) {
            gltd.isUsedPlayer = true;
            gltd.renderRotaionYaw = gltd.getRiddenByEntity().field_70177_z;
            gltd.renderRotaionPitch = gltd.getRiddenByEntity().field_70125_A;
            boolean bl = isNotRenderHead = mc.field_71474_y.field_74320_O == 0 && W_Lib.isClientPlayer(gltd.getRiddenByEntity());
        }
        if (gltd.isUsedPlayer) {
            GL11.glPushMatrix();
            GL11.glRotatef((float)(-gltd.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
            model.renderPart("$body");
            GL11.glPopMatrix();
        } else {
            GL11.glRotatef((float)(-gltd.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
            model.renderPart("$body");
        }
        GL11.glTranslatef((float)0.0f, (float)0.45f, (float)0.0f);
        if (gltd.isUsedPlayer) {
            GL11.glRotatef((float)gltd.renderRotaionYaw, (float)0.0f, (float)-1.0f, (float)0.0f);
            GL11.glRotatef((float)gltd.renderRotaionPitch, (float)1.0f, (float)0.0f, (float)0.0f);
        }
        GL11.glTranslatef((float)0.0f, (float)-0.45f, (float)0.0f);
        if (!isNotRenderHead) {
            model.renderPart("$head");
        }
        GL11.glTranslatef((float)0.0f, (float)0.45f, (float)0.0f);
        this.restoreCommonRenderParam();
        GL11.glDisable((int)2896);
        Vec3d[] v = new Vec3d[]{new Vec3d(0.0, 0.2, 0.0), new Vec3d(0.0, 0.2, 100.0)};
        int a = rand.nextInt(64);
        MCH_RenderLib.drawLine(v, 0x6080FF80 | a << 24);
        GL11.glEnable((int)2896);
        GL11.glPopMatrix();
    }

    public boolean shouldRender(MCH_EntityGLTD livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return true;
    }

    protected ResourceLocation getEntityTexture(MCH_EntityGLTD entity) {
        return TEX_DEFAULT;
    }
}

