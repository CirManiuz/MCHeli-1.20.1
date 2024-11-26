/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.model.ModelBiped$ArmPose
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.resources.DefaultPlayerSkin
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.registry.IRenderFactory
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.mob;

import mcheli.MCH_MOD;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.mob.MCH_EntityGunner;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderGunner
extends RenderLivingBase<MCH_EntityGunner> {
    public static final IRenderFactory<MCH_EntityGunner> FACTORY = MCH_RenderGunner::new;
    private static final ResourceLocation steveTextures = new ResourceLocation("mcheli", "textures/mob/heligunner.png");
    private static final ResourceLocation EMB4_TEXTURE = DefaultPlayerSkin.func_177335_a();
    public ModelBiped modelBipedMain;
    public ModelBiped modelArmorChestplate;
    public ModelBiped modelArmor;

    public MCH_RenderGunner(RenderManager renderManager) {
        super(renderManager, (ModelBase)new ModelBiped(0.0f, 0.0f, 64, MCH_MOD.isTodaySep01() ? 64 : 32), 0.5f);
        this.modelBipedMain = (ModelBiped)this.field_77045_g;
        this.modelArmorChestplate = new ModelBiped(1.0f, 0.0f, 64, MCH_MOD.isTodaySep01() ? 64 : 32);
        this.modelArmor = new ModelBiped(0.5f, 0.0f, 64, MCH_MOD.isTodaySep01() ? 64 : 32);
    }

    protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_) {
        return -1;
    }

    protected boolean canRenderName(MCH_EntityGunner targetEntity) {
        return targetEntity.func_96124_cp() != null;
    }

    public void doRender(MCH_EntityGunner entity, double x, double y, double z, float entityYaw, float partialTicks) {
        MCH_EntityAircraft ac;
        GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
        this.modelArmor.field_78117_n = this.modelBipedMain.field_78117_n = entity.func_70093_af();
        this.modelArmorChestplate.field_78117_n = this.modelBipedMain.field_78117_n;
        double d3 = y;
        if (entity.func_70093_af()) {
            d3 -= 0.125;
        }
        if (!((ac = entity.getAc()) == null || ac.getAcInfo() == null || ac.getAcInfo().hideEntity && ac.isPilot((Entity)entity))) {
            super.func_76986_a((EntityLivingBase)entity, x, d3, z, entityYaw, partialTicks);
        }
        this.modelBipedMain.field_78117_n = false;
        this.modelArmor.field_78117_n = false;
        this.modelArmorChestplate.field_78117_n = false;
        this.modelArmor.field_187076_m = this.modelBipedMain.field_187076_m = ModelBiped.ArmPose.EMPTY;
        this.modelArmorChestplate.field_187076_m = this.modelBipedMain.field_187076_m;
    }

    protected void preRenderCallback(MCH_EntityGunner entitylivingbaseIn, float partialTickTime) {
        float f1 = 0.9375f;
        GL11.glScalef((float)f1, (float)f1, (float)f1);
    }

    public void renderFirstPersonArm(EntityPlayer p_82441_1_) {
        float f = 1.0f;
        GL11.glColor3f((float)f, (float)f, (float)f);
        this.modelBipedMain.field_78095_p = 0.0f;
        this.modelBipedMain.func_78087_a(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, (Entity)p_82441_1_);
        this.modelBipedMain.field_178723_h.func_78785_a(0.0625f);
    }

    protected void _renderOffsetLivingLabel(MCH_EntityGunner p_96449_1_, double p_96449_2_, double p_96449_4_, double p_96449_6_, String p_96449_8_, float p_96449_9_, double p_96449_10_) {
    }

    protected void _preRenderCallback(MCH_EntityGunner entitylivingbaseIn, float partialTickTime) {
        this.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }

    protected int _shouldRenderPass(MCH_EntityGunner p_77032_1_, int p_77032_2_, float p_77032_3_) {
        return this.shouldRenderPass(p_77032_1_, p_77032_2_, p_77032_3_);
    }

    protected void _renderEquippedItems(MCH_EntityGunner p_77029_1_, float p_77029_2_) {
    }

    protected void _rotateCorpse(MCH_EntityGunner entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        this.func_77043_a(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }

    protected void _renderLivingAt(MCH_EntityGunner entityLivingBaseIn, double x, double y, double z) {
        this.func_77039_a(entityLivingBaseIn, x, y, z);
    }

    public void _doRender(MCH_EntityGunner entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(MCH_EntityGunner entity) {
        return MCH_MOD.isTodaySep01() ? EMB4_TEXTURE : steveTextures;
    }

    public void _doRender2(MCH_EntityGunner entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
