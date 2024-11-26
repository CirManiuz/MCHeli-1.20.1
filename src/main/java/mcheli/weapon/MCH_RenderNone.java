/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.registry.IRenderFactory
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.weapon;

import mcheli.weapon.MCH_RenderBulletBase;
import mcheli.wrapper.W_Entity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderNone
extends MCH_RenderBulletBase<W_Entity> {
    public static final IRenderFactory<W_Entity> FACTORY = MCH_RenderNone::new;

    protected MCH_RenderNone(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void renderBullet(W_Entity entity, double posX, double posY, double posZ, float yaw, float partialTickTime) {
    }

    protected ResourceLocation getEntityTexture(W_Entity entity) {
        return TEX_DEFAULT;
    }
}

