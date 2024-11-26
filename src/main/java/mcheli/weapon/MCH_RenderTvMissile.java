/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.registry.IRenderFactory
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.weapon;

import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.weapon.MCH_EntityBaseBullet;
import mcheli.weapon.MCH_RenderBulletBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderTvMissile
extends MCH_RenderBulletBase<MCH_EntityBaseBullet> {
    public static final IRenderFactory<MCH_EntityBaseBullet> FACTORY = MCH_RenderTvMissile::new;

    public MCH_RenderTvMissile(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 0.5f;
    }

    @Override
    public void renderBullet(MCH_EntityBaseBullet entity, double posX, double posY, double posZ, float par8, float par9) {
        MCH_EntityAircraft ac = null;
        Entity ridingEntity = Minecraft.func_71410_x().field_71439_g.func_184187_bx();
        if (ridingEntity instanceof MCH_EntityAircraft) {
            ac = (MCH_EntityAircraft)ridingEntity;
        } else if (ridingEntity instanceof MCH_EntitySeat) {
            ac = ((MCH_EntitySeat)ridingEntity).getParent();
        } else if (ridingEntity instanceof MCH_EntityUavStation) {
            ac = ((MCH_EntityUavStation)ridingEntity).getControlAircract();
        }
        if (ac != null && !ac.isRenderBullet(entity, (Entity)Minecraft.func_71410_x().field_71439_g)) {
            return;
        }
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

