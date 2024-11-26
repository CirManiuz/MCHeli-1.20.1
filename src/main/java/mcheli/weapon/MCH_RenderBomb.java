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

import mcheli.weapon.MCH_EntityBomb;
import mcheli.weapon.MCH_RenderBulletBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderBomb
extends MCH_RenderBulletBase<MCH_EntityBomb> {
    public static final IRenderFactory<MCH_EntityBomb> FACTORY = MCH_RenderBomb::new;

    public MCH_RenderBomb(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 0.0f;
    }

    @Override
    public void renderBullet(MCH_EntityBomb entity, double posX, double posY, double posZ, float yaw, float partialTickTime) {
        if (!(entity instanceof MCH_EntityBomb)) {
            return;
        }
        MCH_EntityBomb bomb = entity;
        if (bomb.getInfo() == null) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        GL11.glRotatef((float)(-entity.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-entity.field_70125_A), (float)-1.0f, (float)0.0f, (float)0.0f);
        if (bomb.isBomblet > 0 || bomb.getInfo().bomblet <= 0 || bomb.getInfo().bombletSTime > 0) {
            this.renderModel(bomb);
        }
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(MCH_EntityBomb entity) {
        return TEX_DEFAULT;
    }
}

