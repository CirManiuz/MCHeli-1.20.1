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
package mcheli.uav;

import mcheli.MCH_Lib;
import mcheli.MCH_ModelManager;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderUavStation
extends W_Render<MCH_EntityUavStation> {
    public static final IRenderFactory<MCH_EntityUavStation> FACTORY = MCH_RenderUavStation::new;
    public static final String[] MODEL_NAME = new String[]{"uav_station", "uav_portable_controller"};
    public static final String[] TEX_NAME_ON = new String[]{"uav_station_on", "uav_portable_controller_on"};
    public static final String[] TEX_NAME_OFF = new String[]{"uav_station", "uav_portable_controller"};

    public MCH_RenderUavStation(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 1.0f;
    }

    public void doRender(MCH_EntityUavStation entity, double posX, double posY, double posZ, float par8, float tickTime) {
        if (!(entity instanceof MCH_EntityUavStation)) {
            return;
        }
        MCH_EntityUavStation uavSt = entity;
        if (uavSt.getKind() <= 0) {
            return;
        }
        int kind = uavSt.getKind() - 1;
        GL11.glPushMatrix();
        GL11.glTranslated((double)posX, (double)(posY + (double)0.35f), (double)posZ);
        GL11.glEnable((int)2884);
        GL11.glRotatef((float)entity.field_70177_z, (float)0.0f, (float)-1.0f, (float)0.0f);
        GL11.glRotatef((float)entity.field_70125_A, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.75f, (float)0.75f, (float)0.75f, (float)1.0f);
        GL11.glEnable((int)3042);
        int srcBlend = GL11.glGetInteger((int)3041);
        int dstBlend = GL11.glGetInteger((int)3040);
        GL11.glBlendFunc((int)770, (int)771);
        if (kind == 0) {
            if (uavSt.getControlAircract() != null && uavSt.getRiddenByEntity() != null) {
                this.bindTexture("textures/" + TEX_NAME_ON[kind] + ".png");
            } else {
                this.bindTexture("textures/" + TEX_NAME_OFF[kind] + ".png");
            }
            MCH_ModelManager.render(MODEL_NAME[kind]);
        } else {
            if (uavSt.rotCover > 0.95f) {
                this.bindTexture("textures/" + TEX_NAME_ON[kind] + ".png");
            } else {
                this.bindTexture("textures/" + TEX_NAME_OFF[kind] + ".png");
            }
            this.renderPortableController(uavSt, MODEL_NAME[kind], tickTime);
        }
        GL11.glBlendFunc((int)srcBlend, (int)dstBlend);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public void renderPortableController(MCH_EntityUavStation uavSt, String name, float tickTime) {
        MCH_ModelManager.renderPart(name, "$body");
        float rot = MCH_Lib.smooth(uavSt.rotCover, uavSt.prevRotCover, tickTime);
        this.renderRotPart(name, "$cover", rot * 60.0f, 0.0, -0.1812, -0.3186);
        this.renderRotPart(name, "$laptop_cover", rot * 95.0f, 0.0, -0.1808, -0.0422);
        this.renderRotPart(name, "$display", rot * -85.0f, 0.0, -0.1807, 0.2294);
    }

    private void renderRotPart(String modelName, String partName, float rot, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glTranslated((double)x, (double)y, (double)z);
        GL11.glRotatef((float)rot, (float)-1.0f, (float)0.0f, (float)0.0f);
        GL11.glTranslated((double)(-x), (double)(-y), (double)(-z));
        MCH_ModelManager.renderPart(modelName, partName);
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(MCH_EntityUavStation entity) {
        return TEX_DEFAULT;
    }
}

