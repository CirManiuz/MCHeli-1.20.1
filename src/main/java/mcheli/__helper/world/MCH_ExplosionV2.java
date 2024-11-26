/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.EnchantmentProtection
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.__helper.world;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import mcheli.MCH_Config;
import mcheli.MCH_DamageFactor;
import mcheli.MCH_Explosion;
import mcheli.MCH_Lib;
import mcheli.flare.MCH_EntityFlare;
import mcheli.particles.MCH_ParticleParam;
import mcheli.particles.MCH_ParticlesUtil;
import mcheli.weapon.MCH_EntityBaseBullet;
import mcheli.wrapper.W_AxisAlignedBB;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_Blocks;
import mcheli.wrapper.W_ChunkPosition;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_ExplosionV2
extends Explosion {
    private static Random explosionRNG = new Random();
    public final int field_77289_h = 16;
    public World field_77287_j;
    public final Entity field_77283_e;
    public final double field_77284_b;
    public final double field_77285_c;
    public final double field_77282_d;
    public final float field_77280_f;
    public final boolean field_77286_a;
    public final boolean field_82755_b;
    public boolean isDestroyBlock;
    public int countSetFireEntity;
    public boolean isPlaySound;
    public boolean isInWater;
    private MCH_Explosion.ExplosionResult result;
    public EntityPlayer explodedPlayer;
    public float explosionSizeBlock;
    public MCH_DamageFactor damageFactor = null;

    @SideOnly(value=Side.CLIENT)
    public MCH_ExplosionV2(World worldIn, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        this(worldIn, null, null, x, y, z, size, false, true);
        this.func_180343_e().addAll(affectedPositions);
        this.isPlaySound = false;
    }

    public MCH_ExplosionV2(World worldIn, @Nullable Entity exploderIn, @Nullable Entity player, double x, double y, double z, float size, boolean flaming, boolean damagesTerrain) {
        super(worldIn, exploderIn, x, y, z, size, flaming, damagesTerrain);
        this.field_77287_j = worldIn;
        this.field_77283_e = exploderIn;
        this.explodedPlayer = player instanceof EntityPlayer ? (EntityPlayer)player : null;
        this.field_77284_b = x;
        this.field_77285_c = y;
        this.field_77282_d = z;
        this.field_77280_f = size;
        this.field_77286_a = flaming;
        this.field_82755_b = damagesTerrain;
        this.isDestroyBlock = false;
        this.explosionSizeBlock = size;
        this.countSetFireEntity = 0;
        this.isPlaySound = true;
        this.isInWater = false;
        this.result = new MCH_Explosion.ExplosionResult();
    }

    public void func_77278_a() {
        HashSet<BlockPos> hashset = new HashSet<BlockPos>();
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                for (int k = 0; k < 16; ++k) {
                    if (i != 0 && i != 15 && j != 0 && j != 15 && k != 0 && k != 15) continue;
                    double d3 = (float)i / 15.0f * 2.0f - 1.0f;
                    double d4 = (float)j / 15.0f * 2.0f - 1.0f;
                    double d5 = (float)k / 15.0f * 2.0f - 1.0f;
                    double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d0 = this.field_77284_b;
                    double d1 = this.field_77285_c;
                    double d2 = this.field_77282_d;
                    for (float f1 = this.explosionSizeBlock * (0.7f + this.field_77287_j.field_73012_v.nextFloat() * 0.6f); f1 > 0.0f; f1 -= 0.22500001f) {
                        int l = MathHelper.func_76128_c((double)d0);
                        int i1 = MathHelper.func_76128_c((double)d1);
                        int j1 = MathHelper.func_76128_c((double)d2);
                        int k1 = W_WorldFunc.getBlockId(this.field_77287_j, l, i1, j1);
                        BlockPos blockpos = new BlockPos(l, i1, j1);
                        IBlockState iblockstate = this.field_77287_j.func_180495_p(blockpos);
                        Block block = iblockstate.func_177230_c();
                        if (k1 > 0) {
                            float f3 = this.field_77283_e != null ? W_Entity.getBlockExplosionResistance(this.field_77283_e, this, this.field_77287_j, l, i1, j1, block) : block.getExplosionResistance(this.field_77287_j, blockpos, this.field_77283_e, (Explosion)this);
                            if (this.isInWater) {
                                f3 *= this.field_77287_j.field_73012_v.nextFloat() * 0.2f + 0.2f;
                            }
                            f1 -= (f3 + 0.3f) * 0.3f;
                        }
                        if (f1 > 0.0f && (this.field_77283_e == null || W_Entity.shouldExplodeBlock(this.field_77283_e, this, this.field_77287_j, l, i1, j1, k1, f1))) {
                            hashset.add(blockpos);
                        }
                        d0 += d3 * (double)0.3f;
                        d1 += d4 * (double)0.3f;
                        d2 += d5 * (double)0.3f;
                    }
                }
            }
        }
        float f = this.field_77280_f * 2.0f;
        this.func_180343_e().addAll(hashset);
        int i = MathHelper.func_76128_c((double)(this.field_77284_b - (double)f - 1.0));
        int j = MathHelper.func_76128_c((double)(this.field_77284_b + (double)f + 1.0));
        int k = MathHelper.func_76128_c((double)(this.field_77285_c - (double)f - 1.0));
        int l1 = MathHelper.func_76128_c((double)(this.field_77285_c + (double)f + 1.0));
        int i2 = MathHelper.func_76128_c((double)(this.field_77282_d - (double)f - 1.0));
        int j2 = MathHelper.func_76128_c((double)(this.field_77282_d + (double)f + 1.0));
        List list = this.field_77287_j.func_72839_b(this.field_77283_e, W_AxisAlignedBB.getAABB(i, k, i2, j, l1, j2));
        Vec3d vec3 = W_WorldFunc.getWorldVec3(this.field_77287_j, this.field_77284_b, this.field_77285_c, this.field_77282_d);
        for (int k2 = 0; k2 < list.size(); ++k2) {
            double fireFactor;
            double d2;
            double d1;
            double d0;
            double d8;
            Entity entity = (Entity)list.get(k2);
            double d7 = entity.func_70011_f(this.field_77284_b, this.field_77285_c, this.field_77282_d) / (double)f;
            if (!(d7 <= 1.0) || (d8 = (double)MathHelper.func_76133_a((double)((d0 = entity.field_70165_t - this.field_77284_b) * d0 + (d1 = entity.field_70163_u + (double)entity.func_70047_e() - this.field_77285_c) * d1 + (d2 = entity.field_70161_v - this.field_77282_d) * d2))) == 0.0) continue;
            d0 /= d8;
            d1 /= d8;
            d2 /= d8;
            double d9 = this.getBlockDensity(vec3, entity.func_174813_aQ());
            double d10 = (1.0 - d7) * d9;
            float damage = (int)((d10 * d10 + d10) / 2.0 * 8.0 * (double)f + 1.0);
            if (!(!(damage > 0.0f) || entity instanceof EntityItem || entity instanceof EntityExpBottle || entity instanceof EntityXPOrb || W_Entity.isEntityFallingBlock(entity))) {
                if (entity instanceof MCH_EntityBaseBullet && this.explodedPlayer instanceof EntityPlayer) {
                    if (!W_Entity.isEqual(((MCH_EntityBaseBullet)entity).shootingEntity, (Entity)this.explodedPlayer)) {
                        this.result.hitEntity = true;
                        MCH_Lib.DbgLog(this.field_77287_j, "MCH_Explosion.doExplosionA:Damage=%.1f:HitEntityBullet=" + entity.getClass(), Float.valueOf(damage));
                    }
                } else {
                    MCH_Lib.DbgLog(this.field_77287_j, "MCH_Explosion.doExplosionA:Damage=%.1f:HitEntity=" + entity.getClass(), Float.valueOf(damage));
                    this.result.hitEntity = true;
                }
            }
            MCH_Lib.applyEntityHurtResistantTimeConfig(entity);
            DamageSource ds = DamageSource.func_94539_a((Explosion)this);
            damage = MCH_Config.applyDamageVsEntity(entity, ds, damage);
            W_Entity.attackEntityFrom(entity, ds, damage *= this.damageFactor != null ? this.damageFactor.getDamageFactor(entity) : 1.0f);
            double d11 = d10;
            if (entity instanceof EntityLivingBase) {
                d11 = EnchantmentProtection.func_92092_a((EntityLivingBase)((EntityLivingBase)entity), (double)d10);
            }
            if (!(entity instanceof MCH_EntityBaseBullet)) {
                entity.field_70159_w += d0 * d11 * 0.4;
                entity.field_70181_x += d1 * d11 * 0.1;
                entity.field_70179_y += d2 * d11 * 0.4;
            }
            if (entity instanceof EntityPlayer) {
                this.func_77277_b().put((EntityPlayer)entity, W_WorldFunc.getWorldVec3(this.field_77287_j, d0 * d10, d1 * d10, d2 * d10));
            }
            if (!(damage > 0.0f) || this.countSetFireEntity <= 0 || !((fireFactor = 1.0 - d8 / (double)f) > 0.0)) continue;
            entity.func_70015_d((int)(fireFactor * (double)this.countSetFireEntity));
        }
    }

    private double getBlockDensity(Vec3d vec3, AxisAlignedBB bb) {
        double d0 = 1.0 / ((bb.field_72336_d - bb.field_72340_a) * 2.0 + 1.0);
        double d1 = 1.0 / ((bb.field_72337_e - bb.field_72338_b) * 2.0 + 1.0);
        double d2 = 1.0 / ((bb.field_72334_f - bb.field_72339_c) * 2.0 + 1.0);
        if (d0 >= 0.0 && d1 >= 0.0 && d2 >= 0.0) {
            int i = 0;
            int j = 0;
            float f = 0.0f;
            while (f <= 1.0f) {
                float f1 = 0.0f;
                while (f1 <= 1.0f) {
                    float f2 = 0.0f;
                    while (f2 <= 1.0f) {
                        double d3 = bb.field_72340_a + (bb.field_72336_d - bb.field_72340_a) * (double)f;
                        double d4 = bb.field_72338_b + (bb.field_72337_e - bb.field_72338_b) * (double)f1;
                        double d5 = bb.field_72339_c + (bb.field_72334_f - bb.field_72339_c) * (double)f2;
                        if (this.field_77287_j.func_147447_a(new Vec3d(d3, d4, d5), vec3, false, true, false) == null) {
                            ++i;
                        }
                        ++j;
                        f2 = (float)((double)f2 + d2);
                    }
                    f1 = (float)((double)f1 + d1);
                }
                f = (float)((double)f + d0);
            }
            return i / j;
        }
        return 0.0;
    }

    public void func_77279_a(boolean spawnParticles) {
        this.doExplosionB(spawnParticles, false);
    }

    private void doExplosionB(boolean spawnParticles, boolean vanillaMode) {
        if (this.isPlaySound) {
            this.field_77287_j.func_184148_a(null, this.field_77284_b, this.field_77285_c, this.field_77282_d, SoundEvents.field_187539_bB, SoundCategory.BLOCKS, 4.0f, (1.0f + (this.field_77287_j.field_73012_v.nextFloat() - this.field_77287_j.field_73012_v.nextFloat()) * 0.2f) * 0.7f);
        }
        if (this.field_82755_b) {
            Iterator iterator = this.func_180343_e().iterator();
            int cnt = 0;
            int flareCnt = (int)this.field_77280_f;
            while (iterator.hasNext()) {
                BlockPos chunkposition = (BlockPos)iterator.next();
                int i = W_ChunkPosition.getChunkPosX(chunkposition);
                int j = W_ChunkPosition.getChunkPosY(chunkposition);
                int k = W_ChunkPosition.getChunkPosZ(chunkposition);
                int l = W_WorldFunc.getBlockId(this.field_77287_j, i, j, k);
                ++cnt;
                if (spawnParticles) {
                    if (vanillaMode) {
                        this.spawnVanillaExlosionEffect(i, j, k);
                    } else if (this.spawnExlosionEffect(cnt, i, j, k, flareCnt > 0)) {
                        --flareCnt;
                    }
                }
                if (l <= 0 || !this.isDestroyBlock || !(this.explosionSizeBlock > 0.0f) || !MCH_Config.Explosion_DestroyBlock.prmBool) continue;
                Block block = W_Block.getBlockById(l);
                if (block.func_149659_a((Explosion)this)) {
                    block.func_180653_a(this.field_77287_j, chunkposition, this.field_77287_j.func_180495_p(chunkposition), 1.0f / this.explosionSizeBlock, 0);
                }
                block.onBlockExploded(this.field_77287_j, chunkposition, (Explosion)this);
            }
        }
        if (this.field_77286_a && MCH_Config.Explosion_FlamingBlock.prmBool) {
            for (BlockPos chunkposition : this.func_180343_e()) {
                int i = W_ChunkPosition.getChunkPosX(chunkposition);
                int j = W_ChunkPosition.getChunkPosY(chunkposition);
                int k = W_ChunkPosition.getChunkPosZ(chunkposition);
                int l = W_WorldFunc.getBlockId(this.field_77287_j, i, j, k);
                IBlockState iblockstate = this.field_77287_j.func_180495_p(chunkposition.func_177977_b());
                Block b = iblockstate.func_177230_c();
                if (l != 0 || b == null || !iblockstate.func_185914_p() || explosionRNG.nextInt(3) != 0) continue;
                W_WorldFunc.setBlock(this.field_77287_j, i, j, k, (Block)W_Blocks.field_150480_ab);
            }
        }
    }

    private boolean spawnExlosionEffect(int cnt, int i, int j, int k, boolean spawnFlare) {
        boolean spawnedFlare = false;
        double d0 = (float)i + this.field_77287_j.field_73012_v.nextFloat();
        double d1 = (float)j + this.field_77287_j.field_73012_v.nextFloat();
        double d2 = (float)k + this.field_77287_j.field_73012_v.nextFloat();
        double mx = d0 - this.field_77284_b;
        double my = d1 - this.field_77285_c;
        double mz = d2 - this.field_77282_d;
        double d6 = MathHelper.func_76133_a((double)(mx * mx + my * my + mz * mz));
        mx /= d6;
        my /= d6;
        mz /= d6;
        double d7 = 0.5 / (d6 / (double)this.field_77280_f + 0.1);
        mx *= (d7 *= (double)(this.field_77287_j.field_73012_v.nextFloat() * this.field_77287_j.field_73012_v.nextFloat() + 0.3f)) * 0.5;
        my *= d7 * 0.5;
        mz *= d7 * 0.5;
        double px = (d0 + this.field_77284_b * 1.0) / 2.0;
        double py = (d1 + this.field_77285_c * 1.0) / 2.0;
        double pz = (d2 + this.field_77282_d * 1.0) / 2.0;
        double r = Math.PI * (double)this.field_77287_j.field_73012_v.nextInt(360) / 180.0;
        if (this.field_77280_f >= 4.0f && spawnFlare) {
            double a = Math.min((double)(this.field_77280_f / 12.0f), 0.6) * (double)(0.5f + this.field_77287_j.field_73012_v.nextFloat() * 0.5f);
            this.field_77287_j.func_72838_d((Entity)new MCH_EntityFlare(this.field_77287_j, px, py + 2.0, pz, Math.sin(r) * a, (1.0 + my / 5.0) * a, Math.cos(r) * a, 2.0f, 0));
            spawnedFlare = true;
        }
        if (cnt % 4 == 0) {
            float bdf = Math.min(this.field_77280_f / 3.0f, 2.0f) * (0.5f + this.field_77287_j.field_73012_v.nextFloat() * 0.5f);
            MCH_ParticlesUtil.spawnParticleTileDust(this.field_77287_j, (int)(px + 0.5), (int)(py - 0.5), (int)(pz + 0.5), px, py + 1.0, pz, Math.sin(r) * (double)bdf, 0.5 + my / 5.0 * (double)bdf, Math.cos(r) * (double)bdf, Math.min(this.field_77280_f / 2.0f, 3.0f) * (0.5f + this.field_77287_j.field_73012_v.nextFloat() * 0.5f));
        }
        int es = (int)(this.field_77280_f >= 4.0f ? this.field_77280_f : 4.0f);
        if (this.field_77280_f <= 1.0f || cnt % es == 0) {
            if (this.field_77287_j.field_73012_v.nextBoolean()) {
                my *= 3.0;
                mx *= 0.1;
                mz *= 0.1;
            } else {
                my *= 0.2;
                mx *= 3.0;
                mz *= 3.0;
            }
            MCH_ParticleParam prm = new MCH_ParticleParam(this.field_77287_j, "explode", px, py, pz, mx, my, mz, this.field_77280_f < 8.0f ? this.field_77280_f * 2.0f : (this.field_77280_f < 2.0f ? 2.0f : 16.0f));
            prm.g = prm.b = 0.3f + this.field_77287_j.field_73012_v.nextFloat() * 0.4f;
            prm.r = prm.b;
            prm.r += 0.1f;
            prm.g += 0.05f;
            prm.b += 0.0f;
            prm.age = 10 + this.field_77287_j.field_73012_v.nextInt(30);
            MCH_ParticleParam tmp1231_1229 = prm;
            tmp1231_1229.age = (int)((float)tmp1231_1229.age * (this.field_77280_f < 6.0f ? this.field_77280_f : 6.0f));
            prm.age = prm.age * 2 / 3;
            prm.diffusible = true;
            MCH_ParticlesUtil.spawnParticle(prm);
        }
        return spawnedFlare;
    }

    private void spawnVanillaExlosionEffect(int i, int j, int k) {
        double d0 = (float)i + this.field_77287_j.field_73012_v.nextFloat();
        double d1 = (float)j + this.field_77287_j.field_73012_v.nextFloat();
        double d2 = (float)k + this.field_77287_j.field_73012_v.nextFloat();
        double d3 = d0 - this.field_77284_b;
        double d4 = d1 - this.field_77285_c;
        double d5 = d2 - this.field_77282_d;
        double d6 = MathHelper.func_76133_a((double)(d3 * d3 + d4 * d4 + d5 * d5));
        d3 /= d6;
        d4 /= d6;
        d5 /= d6;
        double d7 = 0.5 / (d6 / (double)this.field_77280_f + 0.1);
        MCH_ParticlesUtil.DEF_spawnParticle("explode", (d0 + this.field_77284_b) / 2.0, (d1 + this.field_77285_c) / 2.0, (d2 + this.field_77282_d) / 2.0, d3 *= (d7 *= (double)(this.field_77287_j.field_73012_v.nextFloat() * this.field_77287_j.field_73012_v.nextFloat() + 0.3f)), d4 *= d7, d5 *= d7, 10.0f, new int[0]);
        MCH_ParticlesUtil.DEF_spawnParticle("smoke", d0, d1, d2, d3, d4, d5, 10.0f, new int[0]);
    }

    public EntityLivingBase func_94613_c() {
        if (this.explodedPlayer != null && this.explodedPlayer instanceof EntityPlayer) {
            return this.explodedPlayer;
        }
        return super.func_94613_c();
    }

    public MCH_Explosion.ExplosionResult getResult() {
        return this.result;
    }

    public static void playExplosionSound(World world, double x, double y, double z) {
        world.func_184134_a(x, y, z, SoundEvents.field_187539_bB, SoundCategory.BLOCKS, 4.0f, (1.0f + (world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.2f) * 0.7f, false);
    }

    public static void effectMODExplosion(World world, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        MCH_ExplosionV2 explosion = new MCH_ExplosionV2(world, x, y, z, size, affectedPositions);
        explosion.func_77278_a();
        explosion.doExplosionB(true, false);
    }

    public static void effectVanillaExplosion(World world, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        MCH_ExplosionV2 explosion = new MCH_ExplosionV2(world, x, y, z, size, affectedPositions);
        explosion.func_77278_a();
        explosion.doExplosionB(true, true);
    }

    public static void effectExplosionInWater(World world, double x, double y, double z, float size) {
        if (size <= 0.0f) {
            return;
        }
        int range = (int)((double)size + 0.5) / 1;
        int ex = (int)(x + 0.5);
        int ey = (int)(y + 0.5);
        int ez = (int)(z + 0.5);
        for (int i1 = -range; i1 <= range; ++i1) {
            if (ey + i1 < 1) continue;
            for (int j1 = -range; j1 <= range; ++j1) {
                for (int k1 = -range; k1 <= range; ++k1) {
                    int d = j1 * j1 + i1 * i1 + k1 * k1;
                    if (d >= range * range || !W_Block.func_149680_a((Block)W_WorldFunc.getBlock(world, ex + j1, ey + i1, ez + k1), (Block)W_Block.getWater())) continue;
                    int n = explosionRNG.nextInt(2);
                    for (int i = 0; i < n; ++i) {
                        MCH_ParticleParam prm = new MCH_ParticleParam(world, "splash", ex + j1, ey + i1, ez + k1, (double)(j1 / range) * ((double)explosionRNG.nextFloat() - 0.2), 1.0 - Math.sqrt(j1 * j1 + k1 * k1) / (double)range + (double)explosionRNG.nextFloat() * 0.4 * (double)range * 0.4, (double)(k1 / range) * ((double)explosionRNG.nextFloat() - 0.2), explosionRNG.nextInt(range) * 3 + range);
                        MCH_ParticlesUtil.spawnParticle(prm);
                    }
                }
            }
        }
    }
}

