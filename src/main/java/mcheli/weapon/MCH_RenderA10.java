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

import mcheli.MCH_ModelManager;
import mcheli.weapon.MCH_EntityA10;
import mcheli.weapon.MCH_RenderBulletBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderA10
extends MCH_RenderBulletBase<MCH_EntityA10> {
    public static final IRenderFactory<MCH_EntityA10> FACTORY = MCH_RenderA10::new;

    public MCH_RenderA10(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 10.5f;
    }

    @Override
    public void renderBullet(MCH_EntityA10 e, double posX, double posY, double posZ, float par8, float tickTime) {
        if (!(e instanceof MCH_EntityA10)) {
            return;
        }
        if (!e.isRender()) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        float yaw = -(e.field_70126_B + (e.field_70177_z - e.field_70126_B) * tickTime);
        float pitch = -(e.field_70127_C + (e.field_70125_A - e.field_70127_C) * tickTime);
        GL11.glRotatef((float)yaw, (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)pitch, (float)1.0f, (float)0.0f, (float)0.0f);
        this.bindTexture("textures/bullets/a10.png");
        MCH_ModelManager.render("a-10");
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(MCH_EntityA10 entity) {
        return TEX_DEFAULT;
    }
}

