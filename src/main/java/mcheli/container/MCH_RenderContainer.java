/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.registry.IRenderFactory
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.container;

import java.util.Random;
import mcheli.MCH_Lib;
import mcheli.MCH_ModelManager;
import mcheli.aircraft.MCH_RenderAircraft;
import mcheli.container.MCH_EntityContainer;
import mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderContainer
extends W_Render<MCH_EntityContainer> {
    public static final IRenderFactory<MCH_EntityContainer> FACTORY = MCH_RenderContainer::new;
    public static final Random rand = new Random();

    public MCH_RenderContainer(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 0.5f;
    }

    public void doRender(MCH_EntityContainer entity, double posX, double posY, double posZ, float par8, float tickTime) {
        if (MCH_RenderAircraft.shouldSkipRender(entity)) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glEnable((int)2884);
        GL11.glTranslated((double)posX, (double)(posY - 0.2 + 0.5), (double)posZ);
        float yaw = MCH_Lib.smoothRot(entity.field_70177_z, entity.field_70126_B, tickTime);
        float pitch = MCH_Lib.smoothRot(entity.field_70125_A, entity.field_70127_C, tickTime);
        GL11.glRotatef((float)yaw, (float)0.0f, (float)-1.0f, (float)0.0f);
        GL11.glRotatef((float)pitch, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.75f, (float)0.75f, (float)0.75f, (float)1.0f);
        this.bindTexture("textures/container.png");
        MCH_ModelManager.render("container");
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(MCH_EntityContainer entity) {
        return TEX_DEFAULT;
    }
}

