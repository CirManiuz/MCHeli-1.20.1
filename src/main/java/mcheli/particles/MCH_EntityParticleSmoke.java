/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.particles;

import java.util.List;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.particles.MCH_EntityParticleBase;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntityParticleSmoke
extends MCH_EntityParticleBase {
    private static final VertexFormat VERTEX_FORMAT = new VertexFormat().func_181721_a(DefaultVertexFormats.field_181713_m).func_181721_a(DefaultVertexFormats.field_181715_o).func_181721_a(DefaultVertexFormats.field_181714_n).func_181721_a(DefaultVertexFormats.field_181716_p).func_181721_a(DefaultVertexFormats.field_181717_q).func_181721_a(DefaultVertexFormats.field_181718_r);

    public MCH_EntityParticleSmoke(World par1World, double x, double y, double z, double mx, double my, double mz) {
        super(par1World, x, y, z, mx, my, mz);
        this.field_70553_i = this.field_70551_j = this.field_187136_p.nextFloat() * 0.3f + 0.7f;
        this.field_70552_h = this.field_70551_j;
        this.setParticleScale(this.field_187136_p.nextFloat() * 0.5f + 5.0f);
        this.setParticleMaxAge((int)(16.0 / ((double)this.field_187136_p.nextFloat() * 0.8 + 0.2)) + 2);
    }

    public void func_189213_a() {
        this.field_187123_c = this.field_187126_f;
        this.field_187124_d = this.field_187127_g;
        this.field_187125_e = this.field_187128_h;
        if (this.field_70546_d < this.field_70547_e) {
            this.func_70536_a((int)(8.0 * (double)this.field_70546_d / (double)this.field_70547_e));
            ++this.field_70546_d;
        } else {
            this.func_187112_i();
            return;
        }
        if (this.diffusible && this.field_70544_f < this.particleMaxScale) {
            this.field_70544_f += 0.8f;
        }
        if (this.toWhite) {
            float mn = this.getMinColor();
            float mx = this.getMaxColor();
            float dist = mx - mn;
            if ((double)dist > 0.2) {
                this.field_70552_h += (mx - this.field_70552_h) * 0.016f;
                this.field_70553_i += (mx - this.field_70553_i) * 0.016f;
                this.field_70551_j += (mx - this.field_70551_j) * 0.016f;
            }
        }
        this.effectWind();
        this.field_187130_j = (float)(this.field_70546_d / this.field_70547_e) > this.moutionYUpAge ? (this.field_187130_j += 0.02) : (this.field_187130_j += (double)this.gravity);
        this.func_187110_a(this.field_187129_i, this.field_187130_j, this.field_187131_k);
        if (this.diffusible) {
            this.field_187129_i *= 0.96;
            this.field_187131_k *= 0.96;
            this.field_187130_j *= 0.96;
        } else {
            this.field_187129_i *= 0.9;
            this.field_187131_k *= 0.9;
        }
    }

    public float getMinColor() {
        return this.min(this.min(this.field_70551_j, this.field_70553_i), this.field_70552_h);
    }

    public float getMaxColor() {
        return this.max(this.max(this.field_70551_j, this.field_70553_i), this.field_70552_h);
    }

    public float min(float a, float b) {
        return a < b ? a : b;
    }

    public float max(float a, float b) {
        return a > b ? a : b;
    }

    public void effectWind() {
        if (this.isEffectedWind) {
            List list = this.field_187122_b.func_72872_a(MCH_EntityAircraft.class, this.getCollisionBoundingBox().func_72314_b(15.0, 15.0, 15.0));
            for (int i = 0; i < list.size(); ++i) {
                MCH_EntityAircraft ac = (MCH_EntityAircraft)list.get(i);
                if (!(ac.getThrottle() > (double)0.1f)) continue;
                float dist = this.getDistance(ac);
                double vel = (23.0 - (double)dist) * (double)0.01f * ac.getThrottle();
                double mx = ac.field_70165_t - this.field_187126_f;
                double mz = ac.field_70161_v - this.field_187128_h;
                this.field_187129_i -= mx * vel;
                this.field_187131_k -= mz * vel;
            }
        }
    }

    @Override
    public int func_70537_b() {
        return 3;
    }

    @SideOnly(value=Side.CLIENT)
    public int func_189214_a(float p_70070_1_) {
        double y = this.field_187127_g;
        this.field_187127_g += 3000.0;
        int i = super.func_189214_a(p_70070_1_);
        this.field_187127_g = y;
        return i;
    }

    public void func_180434_a(BufferBuilder buffer, Entity entityIn, float par2, float par3, float par4, float par5, float par6, float par7) {
        W_McClient.MOD_bindTexture("textures/particles/smoke.png");
        GlStateManager.func_179147_l();
        int srcBlend = GlStateManager.func_187397_v((int)3041);
        int dstBlend = GlStateManager.func_187397_v((int)3040);
        GlStateManager.func_187401_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179140_f();
        GlStateManager.func_179129_p();
        float f6 = (float)this.field_94054_b / 8.0f;
        float f7 = f6 + 0.125f;
        float f8 = 0.0f;
        float f9 = 1.0f;
        float f10 = 0.1f * this.field_70544_f;
        float f11 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)par2 - field_70556_an);
        float f12 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)par2 - field_70554_ao);
        float f13 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)par2 - field_70555_ap);
        int i = this.func_189214_a(par2);
        int j = i >> 16 & 0xFFFF;
        int k = i & 0xFFFF;
        buffer.func_181668_a(7, VERTEX_FORMAT);
        buffer.func_181662_b((double)(f11 - par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 - par5 * f10 - par7 * f10)).func_187315_a((double)f7, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(j, k).func_181663_c(0.0f, 1.0f, 0.0f).func_181675_d();
        buffer.func_181662_b((double)(f11 - par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 - par5 * f10 + par7 * f10)).func_187315_a((double)f7, (double)f8).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(j, k).func_181663_c(0.0f, 1.0f, 0.0f).func_181675_d();
        buffer.func_181662_b((double)(f11 + par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 + par5 * f10 + par7 * f10)).func_187315_a((double)f6, (double)f8).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(j, k).func_181663_c(0.0f, 1.0f, 0.0f).func_181675_d();
        buffer.func_181662_b((double)(f11 + par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 + par5 * f10 - par7 * f10)).func_187315_a((double)f6, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(j, k).func_181663_c(0.0f, 1.0f, 0.0f).func_181675_d();
        Tessellator.func_178181_a().func_78381_a();
        GlStateManager.func_179089_o();
        GlStateManager.func_179145_e();
        GlStateManager.func_179112_b((int)srcBlend, (int)dstBlend);
        GlStateManager.func_179084_k();
    }

    private float getDistance(MCH_EntityAircraft entity) {
        float f = (float)(this.field_187126_f - entity.field_70165_t);
        float f1 = (float)(this.field_187127_g - entity.field_70163_u);
        float f2 = (float)(this.field_187128_h - entity.field_70161_v);
        return MathHelper.func_76129_c((float)(f * f + f1 * f1 + f2 * f2));
    }
}

