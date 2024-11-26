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
package mcheli.helicopter;

import mcheli.aircraft.MCH_Blade;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_RenderAircraft;
import mcheli.aircraft.MCH_Rotor;
import mcheli.helicopter.MCH_EntityHeli;
import mcheli.helicopter.MCH_HeliInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderHeli
extends MCH_RenderAircraft<MCH_EntityHeli> {
    public static final IRenderFactory<MCH_EntityHeli> FACTORY = MCH_RenderHeli::new;

    public MCH_RenderHeli(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 2.0f;
    }

    @Override
    public void renderAircraft(MCH_EntityAircraft entity, double posX, double posY, double posZ, float yaw, float pitch, float roll, float tickTime) {
        MCH_EntityHeli heli;
        MCH_HeliInfo heliInfo = null;
        if (entity != null && entity instanceof MCH_EntityHeli) {
            heli = (MCH_EntityHeli)entity;
            heliInfo = heli.getHeliInfo();
            if (heliInfo == null) {
                return;
            }
        } else {
            return;
        }
        this.renderDebugHitBox(heli, posX, posY += (double)0.35f, posZ, yaw, pitch);
        this.renderDebugPilotSeat(heli, posX, posY, posZ, yaw, pitch, roll);
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        GL11.glRotatef((float)yaw, (float)0.0f, (float)-1.0f, (float)0.0f);
        GL11.glRotatef((float)pitch, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)roll, (float)0.0f, (float)0.0f, (float)1.0f);
        this.bindTexture("textures/helicopters/" + heli.getTextureName() + ".png", heli);
        MCH_RenderHeli.renderBody(heliInfo.model);
        this.drawModelBlade(heli, heliInfo, tickTime);
    }

    public void drawModelBlade(MCH_EntityHeli heli, MCH_HeliInfo info, float tickTime) {
        for (int i = 0; i < heli.rotors.length && i < info.rotorList.size(); ++i) {
            MCH_HeliInfo.Rotor rotorInfo = info.rotorList.get(i);
            MCH_Rotor rotor = heli.rotors[i];
            GL11.glPushMatrix();
            if (rotorInfo.oldRenderMethod) {
                GL11.glTranslated((double)rotorInfo.pos.field_72450_a, (double)rotorInfo.pos.field_72448_b, (double)rotorInfo.pos.field_72449_c);
            }
            for (MCH_Blade b : rotor.blades) {
                GL11.glPushMatrix();
                float rot = b.getRotation();
                float prevRot = b.getPrevRotation();
                if (rot - prevRot < -180.0f) {
                    prevRot -= 360.0f;
                } else if (prevRot - rot < -180.0f) {
                    prevRot += 360.0f;
                }
                if (!rotorInfo.oldRenderMethod) {
                    GL11.glTranslated((double)rotorInfo.pos.field_72450_a, (double)rotorInfo.pos.field_72448_b, (double)rotorInfo.pos.field_72449_c);
                }
                GL11.glRotatef((float)(prevRot + (rot - prevRot) * tickTime), (float)((float)rotorInfo.rot.field_72450_a), (float)((float)rotorInfo.rot.field_72448_b), (float)((float)rotorInfo.rot.field_72449_c));
                if (!rotorInfo.oldRenderMethod) {
                    GL11.glTranslated((double)(-rotorInfo.pos.field_72450_a), (double)(-rotorInfo.pos.field_72448_b), (double)(-rotorInfo.pos.field_72449_c));
                }
                MCH_RenderHeli.renderPart(rotorInfo.model, info.model, rotorInfo.modelName);
                GL11.glPopMatrix();
            }
            GL11.glPopMatrix();
        }
    }

    protected ResourceLocation getEntityTexture(MCH_EntityHeli entity) {
        return TEX_DEFAULT;
    }
}

