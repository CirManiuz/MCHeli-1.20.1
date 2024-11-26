/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.client.registry.IRenderFactory
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.tank;

import mcheli.MCH_Config;
import mcheli.__helper.MCH_ColorInt;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_RenderAircraft;
import mcheli.tank.MCH_EntityTank;
import mcheli.tank.MCH_EntityWheel;
import mcheli.tank.MCH_TankInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderTank
extends MCH_RenderAircraft<MCH_EntityTank> {
    public static final IRenderFactory<MCH_EntityTank> FACTORY = MCH_RenderTank::new;

    public MCH_RenderTank(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 2.0f;
    }

    @Override
    public void renderAircraft(MCH_EntityAircraft entity, double posX, double posY, double posZ, float yaw, float pitch, float roll, float tickTime) {
        MCH_EntityTank tank;
        MCH_TankInfo tankInfo = null;
        if (entity != null && entity instanceof MCH_EntityTank) {
            tank = (MCH_EntityTank)entity;
            tankInfo = tank.getTankInfo();
            if (tankInfo == null) {
                return;
            }
        } else {
            return;
        }
        this.renderWheel(tank, posX, posY += (double)0.35f, posZ);
        this.renderDebugHitBox(tank, posX, posY, posZ, yaw, pitch);
        this.renderDebugPilotSeat(tank, posX, posY, posZ, yaw, pitch, roll);
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        GL11.glRotatef((float)yaw, (float)0.0f, (float)-1.0f, (float)0.0f);
        GL11.glRotatef((float)pitch, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)roll, (float)0.0f, (float)0.0f, (float)1.0f);
        this.bindTexture("textures/tanks/" + tank.getTextureName() + ".png", tank);
        MCH_RenderTank.renderBody(tankInfo.model);
    }

    public void renderWheel(MCH_EntityTank tank, double posX, double posY, double posZ) {
        if (!MCH_Config.TestMode.prmBool) {
            return;
        }
        if (debugModel == null) {
            return;
        }
        GL11.glColor4f((float)0.75f, (float)0.75f, (float)0.75f, (float)0.5f);
        for (MCH_EntityWheel w : tank.WheelMng.wheels) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)(w.field_70165_t - tank.field_70165_t + posX), (double)(w.field_70163_u - tank.field_70163_u + posY + 0.25), (double)(w.field_70161_v - tank.field_70161_v + posZ));
            GL11.glScalef((float)w.field_70130_N, (float)(w.field_70131_O / 2.0f), (float)w.field_70130_N);
            this.bindTexture("textures/seat_pilot.png");
            debugModel.renderAll();
            GL11.glPopMatrix();
        }
        GL11.glColor4f((float)0.75f, (float)0.75f, (float)0.75f, (float)1.0f);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(1, DefaultVertexFormats.field_181706_f);
        Vec3d wp = tank.getTransformedPosition(tank.WheelMng.weightedCenter);
        wp = wp.func_178786_a(tank.field_70165_t, tank.field_70163_u, tank.field_70161_v);
        for (int i = 0; i < tank.WheelMng.wheels.length / 2; ++i) {
            MCH_ColorInt cint = new MCH_ColorInt((i & 4) > 0 ? 0xFF0000 : 0, (i & 2) > 0 ? 65280 : 0, (i & 1) > 0 ? 255 : 0, 192);
            MCH_EntityWheel w1 = tank.WheelMng.wheels[i * 2 + 0];
            MCH_EntityWheel w2 = tank.WheelMng.wheels[i * 2 + 1];
            if (w1.isPlus) {
                builder.func_181662_b(w2.field_70165_t - tank.field_70165_t + posX, w2.field_70163_u - tank.field_70163_u + posY, w2.field_70161_v - tank.field_70161_v + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                builder.func_181662_b(w1.field_70165_t - tank.field_70165_t + posX, w1.field_70163_u - tank.field_70163_u + posY, w1.field_70161_v - tank.field_70161_v + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                builder.func_181662_b(w1.field_70165_t - tank.field_70165_t + posX, w1.field_70163_u - tank.field_70163_u + posY, w1.field_70161_v - tank.field_70161_v + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                builder.func_181662_b(posX + wp.field_72450_a, posY + wp.field_72448_b, posZ + wp.field_72449_c).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                builder.func_181662_b(posX + wp.field_72450_a, posY + wp.field_72448_b, posZ + wp.field_72449_c).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                builder.func_181662_b(w2.field_70165_t - tank.field_70165_t + posX, w2.field_70163_u - tank.field_70163_u + posY, w2.field_70161_v - tank.field_70161_v + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                continue;
            }
            builder.func_181662_b(w1.field_70165_t - tank.field_70165_t + posX, w1.field_70163_u - tank.field_70163_u + posY, w1.field_70161_v - tank.field_70161_v + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
            builder.func_181662_b(w2.field_70165_t - tank.field_70165_t + posX, w2.field_70163_u - tank.field_70163_u + posY, w2.field_70161_v - tank.field_70161_v + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
            builder.func_181662_b(w2.field_70165_t - tank.field_70165_t + posX, w2.field_70163_u - tank.field_70163_u + posY, w2.field_70161_v - tank.field_70161_v + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
            builder.func_181662_b(posX + wp.field_72450_a, posY + wp.field_72448_b, posZ + wp.field_72449_c).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
            builder.func_181662_b(posX + wp.field_72450_a, posY + wp.field_72448_b, posZ + wp.field_72449_c).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
            builder.func_181662_b(w1.field_70165_t - tank.field_70165_t + posX, w1.field_70163_u - tank.field_70163_u + posY, w1.field_70161_v - tank.field_70161_v + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
        }
        tessellator.func_78381_a();
    }

    protected ResourceLocation getEntityTexture(MCH_EntityTank entity) {
        return TEX_DEFAULT;
    }
}

