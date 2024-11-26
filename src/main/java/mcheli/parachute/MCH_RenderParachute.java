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
package mcheli.parachute;

import java.util.Random;
import mcheli.MCH_Config;
import mcheli.MCH_ModelManager;
import mcheli.parachute.MCH_EntityParachute;
import mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderParachute
extends W_Render<MCH_EntityParachute> {
    public static final IRenderFactory<MCH_EntityParachute> FACTORY = MCH_RenderParachute::new;
    public static final Random rand = new Random();

    public MCH_RenderParachute(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 0.5f;
    }

    public void doRender(MCH_EntityParachute entity, double posX, double posY, double posZ, float par8, float tickTime) {
        if (!(entity instanceof MCH_EntityParachute)) {
            return;
        }
        MCH_EntityParachute parachute = entity;
        int type = parachute.getType();
        if (type <= 0) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glEnable((int)2884);
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        float prevYaw = entity.field_70126_B;
        if (entity.field_70177_z - prevYaw < -180.0f) {
            prevYaw -= 360.0f;
        } else if (prevYaw - entity.field_70177_z < -180.0f) {
            prevYaw += 360.0f;
        }
        float yaw = prevYaw + (entity.field_70177_z - prevYaw) * tickTime;
        GL11.glRotatef((float)yaw, (float)0.0f, (float)-1.0f, (float)0.0f);
        GL11.glColor4f((float)0.75f, (float)0.75f, (float)0.75f, (float)1.0f);
        GL11.glEnable((int)3042);
        int srcBlend = GL11.glGetInteger((int)3041);
        int dstBlend = GL11.glGetInteger((int)3040);
        GL11.glBlendFunc((int)770, (int)771);
        if (MCH_Config.SmoothShading.prmBool) {
            GL11.glShadeModel((int)7425);
        }
        switch (type) {
            case 1: {
                this.bindTexture("textures/parachute1.png");
                MCH_ModelManager.render("parachute1");
                break;
            }
            case 2: {
                this.bindTexture("textures/parachute2.png");
                if (parachute.isOpenParachute()) {
                    MCH_ModelManager.renderPart("parachute2", "$parachute");
                    break;
                }
                MCH_ModelManager.renderPart("parachute2", "$seat");
                break;
            }
            case 3: {
                this.bindTexture("textures/parachute2.png");
                MCH_ModelManager.renderPart("parachute2", "$parachute");
            }
        }
        GL11.glBlendFunc((int)srcBlend, (int)dstBlend);
        GL11.glDisable((int)3042);
        GL11.glShadeModel((int)7424);
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(MCH_EntityParachute entity) {
        return TEX_DEFAULT;
    }
}

