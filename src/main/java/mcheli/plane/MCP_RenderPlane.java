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
package mcheli.plane;

import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_RenderAircraft;
import mcheli.plane.MCP_EntityPlane;
import mcheli.plane.MCP_PlaneInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCP_RenderPlane
extends MCH_RenderAircraft<MCP_EntityPlane> {
    public static final IRenderFactory<MCP_EntityPlane> FACTORY = MCP_RenderPlane::new;

    public MCP_RenderPlane(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 2.0f;
    }

    @Override
    public void renderAircraft(MCH_EntityAircraft entity, double posX, double posY, double posZ, float yaw, float pitch, float roll, float tickTime) {
        MCP_EntityPlane plane;
        MCP_PlaneInfo planeInfo = null;
        if (entity != null && entity instanceof MCP_EntityPlane) {
            plane = (MCP_EntityPlane)entity;
            planeInfo = plane.getPlaneInfo();
            if (planeInfo == null) {
                return;
            }
        } else {
            return;
        }
        this.renderDebugHitBox(plane, posX, posY += (double)0.35f, posZ, yaw, pitch);
        this.renderDebugPilotSeat(plane, posX, posY, posZ, yaw, pitch, roll);
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        GL11.glRotatef((float)yaw, (float)0.0f, (float)-1.0f, (float)0.0f);
        GL11.glRotatef((float)pitch, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)roll, (float)0.0f, (float)0.0f, (float)1.0f);
        this.bindTexture("textures/planes/" + plane.getTextureName() + ".png", plane);
        if (planeInfo.haveNozzle() && plane.partNozzle != null) {
            this.renderNozzle(plane, planeInfo, tickTime);
        }
        if (planeInfo.haveWing() && plane.partWing != null) {
            this.renderWing(plane, planeInfo, tickTime);
        }
        if (planeInfo.haveRotor() && plane.partNozzle != null) {
            this.renderRotor(plane, planeInfo, tickTime);
        }
        MCP_RenderPlane.renderBody(planeInfo.model);
    }

    public void renderRotor(MCP_EntityPlane plane, MCP_PlaneInfo planeInfo, float tickTime) {
        float rot = plane.getNozzleRotation();
        float prevRot = plane.getPrevNozzleRotation();
        for (MCP_PlaneInfo.Rotor r : planeInfo.rotorList) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)r.pos.field_72450_a, (double)r.pos.field_72448_b, (double)r.pos.field_72449_c);
            GL11.glRotatef((float)((prevRot + (rot - prevRot) * tickTime) * r.maxRotFactor), (float)((float)r.rot.field_72450_a), (float)((float)r.rot.field_72448_b), (float)((float)r.rot.field_72449_c));
            GL11.glTranslated((double)(-r.pos.field_72450_a), (double)(-r.pos.field_72448_b), (double)(-r.pos.field_72449_c));
            MCP_RenderPlane.renderPart(r.model, planeInfo.model, r.modelName);
            for (MCP_PlaneInfo.Blade b : r.blades) {
                float br = plane.prevRotationRotor;
                GL11.glPushMatrix();
                GL11.glTranslated((double)b.pos.field_72450_a, (double)b.pos.field_72448_b, (double)b.pos.field_72449_c);
                GL11.glRotatef((float)(br += (plane.rotationRotor - plane.prevRotationRotor) * tickTime), (float)((float)b.rot.field_72450_a), (float)((float)b.rot.field_72448_b), (float)((float)b.rot.field_72449_c));
                GL11.glTranslated((double)(-b.pos.field_72450_a), (double)(-b.pos.field_72448_b), (double)(-b.pos.field_72449_c));
                for (int i = 0; i < b.numBlade; ++i) {
                    GL11.glTranslated((double)b.pos.field_72450_a, (double)b.pos.field_72448_b, (double)b.pos.field_72449_c);
                    GL11.glRotatef((float)b.rotBlade, (float)((float)b.rot.field_72450_a), (float)((float)b.rot.field_72448_b), (float)((float)b.rot.field_72449_c));
                    GL11.glTranslated((double)(-b.pos.field_72450_a), (double)(-b.pos.field_72448_b), (double)(-b.pos.field_72449_c));
                    MCP_RenderPlane.renderPart(b.model, planeInfo.model, b.modelName);
                }
                GL11.glPopMatrix();
            }
            GL11.glPopMatrix();
        }
    }

    public void renderWing(MCP_EntityPlane plane, MCP_PlaneInfo planeInfo, float tickTime) {
        float rot = plane.getWingRotation();
        float prevRot = plane.getPrevWingRotation();
        for (MCP_PlaneInfo.Wing w : planeInfo.wingList) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)w.pos.field_72450_a, (double)w.pos.field_72448_b, (double)w.pos.field_72449_c);
            GL11.glRotatef((float)((prevRot + (rot - prevRot) * tickTime) * w.maxRotFactor), (float)((float)w.rot.field_72450_a), (float)((float)w.rot.field_72448_b), (float)((float)w.rot.field_72449_c));
            GL11.glTranslated((double)(-w.pos.field_72450_a), (double)(-w.pos.field_72448_b), (double)(-w.pos.field_72449_c));
            MCP_RenderPlane.renderPart(w.model, planeInfo.model, w.modelName);
            if (w.pylonList != null) {
                for (MCP_PlaneInfo.Pylon p : w.pylonList) {
                    GL11.glPushMatrix();
                    GL11.glTranslated((double)p.pos.field_72450_a, (double)p.pos.field_72448_b, (double)p.pos.field_72449_c);
                    GL11.glRotatef((float)((prevRot + (rot - prevRot) * tickTime) * p.maxRotFactor), (float)((float)p.rot.field_72450_a), (float)((float)p.rot.field_72448_b), (float)((float)p.rot.field_72449_c));
                    GL11.glTranslated((double)(-p.pos.field_72450_a), (double)(-p.pos.field_72448_b), (double)(-p.pos.field_72449_c));
                    MCP_RenderPlane.renderPart(p.model, planeInfo.model, p.modelName);
                    GL11.glPopMatrix();
                }
            }
            GL11.glPopMatrix();
        }
    }

    public void renderNozzle(MCP_EntityPlane plane, MCP_PlaneInfo planeInfo, float tickTime) {
        float rot = plane.getNozzleRotation();
        float prevRot = plane.getPrevNozzleRotation();
        for (MCH_AircraftInfo.DrawnPart n : planeInfo.nozzles) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)n.pos.field_72450_a, (double)n.pos.field_72448_b, (double)n.pos.field_72449_c);
            GL11.glRotatef((float)(prevRot + (rot - prevRot) * tickTime), (float)((float)n.rot.field_72450_a), (float)((float)n.rot.field_72448_b), (float)((float)n.rot.field_72449_c));
            GL11.glTranslated((double)(-n.pos.field_72450_a), (double)(-n.pos.field_72448_b), (double)(-n.pos.field_72449_c));
            MCP_RenderPlane.renderPart(n.model, planeInfo.model, n.modelName);
            GL11.glPopMatrix();
        }
    }

    protected ResourceLocation getEntityTexture(MCP_EntityPlane entity) {
        return TEX_DEFAULT;
    }
}

