/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.World
 */
package mcheli.particles;

import mcheli.particles.MCH_EntityParticleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MCH_EntityParticleExplode
extends MCH_EntityParticleBase {
    private static final VertexFormat VERTEX_FORMAT = new VertexFormat().func_181721_a(DefaultVertexFormats.field_181713_m).func_181721_a(DefaultVertexFormats.field_181715_o).func_181721_a(DefaultVertexFormats.field_181714_n).func_181721_a(DefaultVertexFormats.field_181716_p).func_181721_a(DefaultVertexFormats.field_181717_q).func_181721_a(DefaultVertexFormats.field_181718_r);
    private static final ResourceLocation texture = new ResourceLocation("textures/entity/explosion.png");
    private int nowCount;
    private int endCount;
    private TextureManager theRenderEngine;
    private float size;

    public MCH_EntityParticleExplode(World w, double x, double y, double z, double size, double age, double mz) {
        super(w, x, y, z, 0.0, 0.0, 0.0);
        this.theRenderEngine = Minecraft.func_71410_x().field_71446_o;
        this.endCount = 1 + (int)age;
        this.size = (float)size;
    }

    public void func_180434_a(BufferBuilder buffer, Entity entityIn, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
        int i = (int)(((float)this.nowCount + p_70539_2_) * 15.0f / (float)this.endCount);
        if (i <= 15) {
            GlStateManager.func_179147_l();
            int srcBlend = GlStateManager.func_187397_v((int)3041);
            int dstBlend = GlStateManager.func_187397_v((int)3040);
            GlStateManager.func_187401_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.func_179129_p();
            this.theRenderEngine.func_110577_a(texture);
            float f6 = (float)(i % 4) / 4.0f;
            float f7 = f6 + 0.24975f;
            float f8 = (float)(i / 4) / 4.0f;
            float f9 = f8 + 0.24975f;
            float f10 = 2.0f * this.size;
            float f11 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)p_70539_2_ - field_70556_an);
            float f12 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)p_70539_2_ - field_70554_ao);
            float f13 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)p_70539_2_ - field_70555_ap);
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            RenderHelper.func_74518_a();
            int j = 0xF000F0;
            int k = j >> 16 & 0xFFFF;
            int l = j & 0xFFFF;
            buffer.func_181668_a(7, VERTEX_FORMAT);
            buffer.func_181662_b((double)(f11 - p_70539_3_ * f10 - p_70539_6_ * f10), (double)(f12 - p_70539_4_ * f10), (double)(f13 - p_70539_5_ * f10 - p_70539_7_ * f10)).func_187315_a((double)f7, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(k, l).func_181663_c(0.0f, 1.0f, 0.0f).func_181675_d();
            buffer.func_181662_b((double)(f11 - p_70539_3_ * f10 + p_70539_6_ * f10), (double)(f12 + p_70539_4_ * f10), (double)(f13 - p_70539_5_ * f10 + p_70539_7_ * f10)).func_187315_a((double)f7, (double)f8).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(k, l).func_181663_c(0.0f, 1.0f, 0.0f).func_181675_d();
            buffer.func_181662_b((double)(f11 + p_70539_3_ * f10 + p_70539_6_ * f10), (double)(f12 + p_70539_4_ * f10), (double)(f13 + p_70539_5_ * f10 + p_70539_7_ * f10)).func_187315_a((double)f6, (double)f8).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(k, l).func_181663_c(0.0f, 1.0f, 0.0f).func_181675_d();
            buffer.func_181662_b((double)(f11 + p_70539_3_ * f10 - p_70539_6_ * f10), (double)(f12 - p_70539_4_ * f10), (double)(f13 + p_70539_5_ * f10 - p_70539_7_ * f10)).func_187315_a((double)f6, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(k, l).func_181663_c(0.0f, 1.0f, 0.0f).func_181675_d();
            Tessellator.func_178181_a().func_78381_a();
            GlStateManager.func_179136_a((float)0.0f, (float)0.0f);
            GlStateManager.func_179145_e();
            GlStateManager.func_179089_o();
            GlStateManager.func_179112_b((int)srcBlend, (int)dstBlend);
            GlStateManager.func_179084_k();
        }
    }

    public int func_189214_a(float p_70070_1_) {
        return 0xF000F0;
    }

    public void func_189213_a() {
        this.field_187123_c = this.field_187126_f;
        this.field_187124_d = this.field_187127_g;
        this.field_187125_e = this.field_187128_h;
        ++this.nowCount;
        if (this.nowCount == this.endCount) {
            this.func_187112_i();
        }
    }

    @Override
    public int func_70537_b() {
        return 3;
    }
}

