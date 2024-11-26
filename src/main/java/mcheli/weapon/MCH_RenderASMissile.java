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
package mcheli.weapon;

import mcheli.weapon.MCH_EntityBaseBullet;
import mcheli.weapon.MCH_RenderBulletBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderASMissile
extends MCH_RenderBulletBase<MCH_EntityBaseBullet> {
    public static final IRenderFactory<MCH_EntityBaseBullet> FACTORY = MCH_RenderASMissile::new;

    public MCH_RenderASMissile(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 0.5f;
    }

    @Override
    public void renderBullet(MCH_EntityBaseBullet entity, double posX, double posY, double posZ, float yaw, float partialTickTime) {
        if (entity instanceof MCH_EntityBaseBullet) {
            MCH_EntityBaseBullet bullet = entity;
            GL11.glPushMatrix();
            GL11.glTranslated((double)posX, (double)posY, (double)posZ);
            GL11.glRotatef((float)(-entity.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)(-entity.field_70125_A), (float)-1.0f, (float)0.0f, (float)0.0f);
            this.renderModel(bullet);
            GL11.glPopMatrix();
        }
    }

    protected ResourceLocation getEntityTexture(MCH_EntityBaseBullet entity) {
        return TEX_DEFAULT;
    }
}
