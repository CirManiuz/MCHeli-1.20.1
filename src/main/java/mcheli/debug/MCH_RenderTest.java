/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.registry.IRenderFactory
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.debug;

import mcheli.MCH_Config;
import mcheli.debug.MCH_ModelTest;
import mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderTest
extends W_Render<Entity> {
    protected MCH_ModelTest model;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private String textureName;

    public static final IRenderFactory<Entity> factory(float x, float y, float z, String texture_name) {
        return renderManager -> new MCH_RenderTest(renderManager, x, y, z, texture_name);
    }

    public MCH_RenderTest(RenderManager renderManager, float x, float y, float z, String texture_name) {
        super(renderManager);
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        this.textureName = texture_name;
        this.model = new MCH_ModelTest();
    }

    public void func_76986_a(Entity e, double posX, double posY, double posZ, float par8, float par9) {
        if (!MCH_Config.TestMode.prmBool) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glTranslated((double)(posX + (double)this.offsetX), (double)(posY + (double)this.offsetY), (double)(posZ + (double)this.offsetZ));
        GL11.glScalef((float)e.field_70130_N, (float)e.field_70131_O, (float)e.field_70130_N);
        GL11.glColor4f((float)0.5f, (float)0.5f, (float)0.5f, (float)1.0f);
        float prevYaw = e.field_70177_z - e.field_70126_B < -180.0f ? e.field_70126_B - 360.0f : (e.field_70126_B - e.field_70177_z < -180.0f ? e.field_70126_B + 360.0f : e.field_70126_B);
        float yaw = -(prevYaw + (e.field_70177_z - prevYaw) * par9) - 180.0f;
        float pitch = -(e.field_70127_C + (e.field_70125_A - e.field_70127_C) * par9);
        GL11.glRotatef((float)yaw, (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)pitch, (float)1.0f, (float)0.0f, (float)0.0f);
        this.bindTexture("textures/" + this.textureName + ".png");
        this.model.renderModel(0.0, 0.0, 0.1f);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation func_110775_a(Entity entity) {
        return TEX_DEFAULT;
    }
}

