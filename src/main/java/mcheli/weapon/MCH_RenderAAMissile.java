/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.client.registry.IRenderFactory
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.weapon;

import mcheli.MCH_Lib;
import mcheli.weapon.MCH_EntityAAMissile;
import mcheli.weapon.MCH_RenderBulletBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderAAMissile
extends MCH_RenderBulletBase<MCH_EntityAAMissile> {
    public static final IRenderFactory<MCH_EntityAAMissile> FACTORY = MCH_RenderAAMissile::new;

    public MCH_RenderAAMissile(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 0.5f;
    }

    @Override
    public void renderBullet(MCH_EntityAAMissile entity, double posX, double posY, double posZ, float par8, float par9) {
        if (!(entity instanceof MCH_EntityAAMissile)) {
            return;
        }
        MCH_EntityAAMissile aam = entity;
        double mx = aam.prevMotionX + (aam.field_70159_w - aam.prevMotionX) * (double)par9;
        double my = aam.prevMotionY + (aam.field_70181_x - aam.prevMotionY) * (double)par9;
        double mz = aam.prevMotionZ + (aam.field_70179_y - aam.prevMotionZ) * (double)par9;
        GL11.glPushMatrix();
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        Vec3d v = MCH_Lib.getYawPitchFromVec(mx, my, mz);
        GL11.glRotatef((float)((float)v.field_72448_b - 90.0f), (float)0.0f, (float)-1.0f, (float)0.0f);
        GL11.glRotatef((float)((float)v.field_72449_c), (float)-1.0f, (float)0.0f, (float)0.0f);
        this.renderModel(aam);
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(MCH_EntityAAMissile entity) {
        return TEX_DEFAULT;
    }
}

