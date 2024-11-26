/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.world.World
 */
package mcheli.particles;

import mcheli.particles.MCH_EntityParticleBase;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class MCH_EntityParticleSplash
extends MCH_EntityParticleBase {
    public MCH_EntityParticleSplash(World par1World, double x, double y, double z, double mx, double my, double mz) {
        super(par1World, x, y, z, mx, my, mz);
        this.field_70553_i = this.field_70551_j = this.field_187136_p.nextFloat() * 0.3f + 0.7f;
        this.field_70552_h = this.field_70551_j;
        this.setParticleScale(this.field_187136_p.nextFloat() * 0.5f + 5.0f);
        this.setParticleMaxAge((int)(80.0 / ((double)this.field_187136_p.nextFloat() * 0.8 + 0.2)) + 2);
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
        }
        this.field_187130_j -= (double)0.06f;
        Block block = W_WorldFunc.getBlock(this.field_187122_b, (int)(this.field_187126_f + 0.5), (int)(this.field_187127_g + 0.5), (int)(this.field_187128_h + 0.5));
        boolean beforeInWater = W_Block.func_149680_a((Block)block, (Block)W_Block.getWater());
        this.func_187110_a(this.field_187129_i, this.field_187130_j, this.field_187131_k);
        block = W_WorldFunc.getBlock(this.field_187122_b, (int)(this.field_187126_f + 0.5), (int)(this.field_187127_g + 0.5), (int)(this.field_187128_h + 0.5));
        boolean nowInWater = W_Block.func_149680_a((Block)block, (Block)W_Block.getWater());
        if (this.field_187130_j < -0.6 && !beforeInWater && nowInWater) {
            double p = -this.field_187130_j * 10.0;
            int i = 0;
            while ((double)i < p) {
                this.field_187122_b.func_175688_a(EnumParticleTypes.WATER_SPLASH, this.field_187126_f + 0.5 + (this.field_187136_p.nextDouble() - 0.5) * 2.0, this.field_187127_g + this.field_187136_p.nextDouble(), this.field_187128_h + 0.5 + (this.field_187136_p.nextDouble() - 0.5) * 2.0, (this.field_187136_p.nextDouble() - 0.5) * 2.0, 4.0, (this.field_187136_p.nextDouble() - 0.5) * 2.0, new int[0]);
                this.field_187122_b.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.field_187126_f + 0.5 + (this.field_187136_p.nextDouble() - 0.5) * 2.0, this.field_187127_g - this.field_187136_p.nextDouble(), this.field_187128_h + 0.5 + (this.field_187136_p.nextDouble() - 0.5) * 2.0, (this.field_187136_p.nextDouble() - 0.5) * 2.0, -0.5, (this.field_187136_p.nextDouble() - 0.5) * 2.0, new int[0]);
                ++i;
            }
        } else if (this.field_187132_l) {
            this.func_187112_i();
        }
        this.field_187129_i *= 0.9;
        this.field_187131_k *= 0.9;
    }

    public void func_180434_a(BufferBuilder buffer, Entity entityIn, float par2, float par3, float par4, float par5, float par6, float par7) {
        W_McClient.MOD_bindTexture("textures/particles/smoke.png");
        float f6 = (float)this.field_94054_b / 8.0f;
        float f7 = f6 + 0.125f;
        float f8 = 0.0f;
        float f9 = 1.0f;
        float f10 = 0.1f * this.field_70544_f;
        float f11 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)par2 - field_70556_an);
        float f12 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)par2 - field_70554_ao);
        float f13 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)par2 - field_70555_ap);
        float f14 = 1.0f;
        int i = this.func_189214_a(par2);
        int j = i >> 16 & 0xFFFF;
        int k = i & 0xFFFF;
        buffer.func_181662_b((double)(f11 - par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 - par5 * f10 - par7 * f10)).func_187315_a((double)f7, (double)f9).func_181666_a(this.field_70552_h * f14, this.field_70553_i * f14, this.field_70551_j * f14, this.field_82339_as).func_187314_a(j, k).func_181675_d();
        buffer.func_181662_b((double)(f11 - par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 - par5 * f10 + par7 * f10)).func_187315_a((double)f7, (double)f8).func_181666_a(this.field_70552_h * f14, this.field_70553_i * f14, this.field_70551_j * f14, this.field_82339_as).func_187314_a(j, k).func_181675_d();
        buffer.func_181662_b((double)(f11 + par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 + par5 * f10 + par7 * f10)).func_187315_a((double)f6, (double)f8).func_181666_a(this.field_70552_h * f14, this.field_70553_i * f14, this.field_70551_j * f14, this.field_82339_as).func_187314_a(j, k).func_181675_d();
        buffer.func_181662_b((double)(f11 + par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 + par5 * f10 - par7 * f10)).func_187315_a((double)f6, (double)f9).func_181666_a(this.field_70552_h * f14, this.field_70553_i * f14, this.field_70551_j * f14, this.field_82339_as).func_187314_a(j, k).func_181675_d();
    }
}

