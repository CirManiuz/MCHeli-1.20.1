/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.particle.IParticleFactory
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleBreaking$Factory
 *  net.minecraft.client.particle.ParticleBreaking$SlimeFactory
 *  net.minecraft.client.particle.ParticleBreaking$SnowballFactory
 *  net.minecraft.client.particle.ParticleBubble$Factory
 *  net.minecraft.client.particle.ParticleCloud$Factory
 *  net.minecraft.client.particle.ParticleCrit$Factory
 *  net.minecraft.client.particle.ParticleCrit$MagicFactory
 *  net.minecraft.client.particle.ParticleDigging$Factory
 *  net.minecraft.client.particle.ParticleDrip$LavaFactory
 *  net.minecraft.client.particle.ParticleDrip$WaterFactory
 *  net.minecraft.client.particle.ParticleEnchantmentTable$EnchantmentTable
 *  net.minecraft.client.particle.ParticleExplosion$Factory
 *  net.minecraft.client.particle.ParticleExplosionHuge$Factory
 *  net.minecraft.client.particle.ParticleExplosionLarge$Factory
 *  net.minecraft.client.particle.ParticleFirework$Factory
 *  net.minecraft.client.particle.ParticleFlame$Factory
 *  net.minecraft.client.particle.ParticleFootStep$Factory
 *  net.minecraft.client.particle.ParticleHeart$AngryVillagerFactory
 *  net.minecraft.client.particle.ParticleHeart$Factory
 *  net.minecraft.client.particle.ParticleLava$Factory
 *  net.minecraft.client.particle.ParticleNote$Factory
 *  net.minecraft.client.particle.ParticlePortal$Factory
 *  net.minecraft.client.particle.ParticleRedstone$Factory
 *  net.minecraft.client.particle.ParticleSmokeLarge$Factory
 *  net.minecraft.client.particle.ParticleSmokeNormal$Factory
 *  net.minecraft.client.particle.ParticleSnowShovel$Factory
 *  net.minecraft.client.particle.ParticleSpell$AmbientMobFactory
 *  net.minecraft.client.particle.ParticleSpell$Factory
 *  net.minecraft.client.particle.ParticleSpell$InstantFactory
 *  net.minecraft.client.particle.ParticleSpell$MobFactory
 *  net.minecraft.client.particle.ParticleSpell$WitchFactory
 *  net.minecraft.client.particle.ParticleSplash$Factory
 *  net.minecraft.client.particle.ParticleSuspend$Factory
 *  net.minecraft.client.particle.ParticleSuspendedTown$Factory
 *  net.minecraft.client.particle.ParticleSuspendedTown$HappyVillagerFactory
 *  net.minecraft.client.particle.ParticleWaterWake$Factory
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.client.FMLClientHandler
 */
package mcheli.particles;

import java.util.function.Supplier;
import mcheli.particles.MCH_EntityBlockDustFX;
import mcheli.particles.MCH_EntityParticleBase;
import mcheli.particles.MCH_EntityParticleExplode;
import mcheli.particles.MCH_EntityParticleMarkPoint;
import mcheli.particles.MCH_EntityParticleSmoke;
import mcheli.particles.MCH_EntityParticleSplash;
import mcheli.particles.MCH_ParticleParam;
import mcheli.wrapper.W_Particle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleBreaking;
import net.minecraft.client.particle.ParticleBubble;
import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.client.particle.ParticleCrit;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.particle.ParticleDrip;
import net.minecraft.client.particle.ParticleEnchantmentTable;
import net.minecraft.client.particle.ParticleExplosion;
import net.minecraft.client.particle.ParticleExplosionHuge;
import net.minecraft.client.particle.ParticleExplosionLarge;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.particle.ParticleFootStep;
import net.minecraft.client.particle.ParticleHeart;
import net.minecraft.client.particle.ParticleLava;
import net.minecraft.client.particle.ParticleNote;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.client.particle.ParticleSmokeLarge;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.particle.ParticleSnowShovel;
import net.minecraft.client.particle.ParticleSpell;
import net.minecraft.client.particle.ParticleSplash;
import net.minecraft.client.particle.ParticleSuspend;
import net.minecraft.client.particle.ParticleSuspendedTown;
import net.minecraft.client.particle.ParticleWaterWake;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

public class MCH_ParticlesUtil {
    public static MCH_EntityParticleMarkPoint markPoint = null;

    public static void spawnParticleExplode(World w, double x, double y, double z, float size, float r, float g, float b, float a, int age) {
        MCH_EntityParticleExplode epe = new MCH_EntityParticleExplode(w, x, y, z, size, age, 0.0);
        epe.setParticleMaxAge(age);
        epe.func_70538_b(r, g, b);
        epe.func_82338_g(a);
        FMLClientHandler.instance().getClient().field_71452_i.func_78873_a((Particle)epe);
    }

    public static void spawnParticleTileCrack(World w, int blockX, int blockY, int blockZ, double x, double y, double z, double mx, double my, double mz) {
        W_Particle.BlockParticleParam name = W_Particle.getParticleTileCrackName(w, blockX, blockY, blockZ);
        if (!name.isEmpty()) {
            MCH_ParticlesUtil.DEF_spawnParticle(name.name, x, y, z, mx, my, mz, 20.0f, name.stateId);
        }
    }

    public static boolean spawnParticleTileDust(World w, int blockX, int blockY, int blockZ, double x, double y, double z, double mx, double my, double mz, float scale) {
        boolean ret = false;
        int[][] offset = new int[][]{{0, 0, 0}, {0, 0, -1}, {0, 0, 1}, {1, 0, 0}, {-1, 0, 0}};
        int len = offset.length;
        for (int i = 0; i < len; ++i) {
            Particle e;
            W_Particle.BlockParticleParam name = W_Particle.getParticleTileDustName(w, blockX + offset[i][0], blockY + offset[i][1], blockZ + offset[i][2]);
            if (name.isEmpty() || !((e = MCH_ParticlesUtil.DEF_spawnParticle(name.name, x, y, z, mx, my, mz, 20.0f, name.stateId)) instanceof MCH_EntityBlockDustFX)) continue;
            ((MCH_EntityBlockDustFX)e).setScale(scale * 2.0f);
            ret = true;
            break;
        }
        return ret;
    }

    public static Particle DEF_spawnParticle(String s, double x, double y, double z, double mx, double my, double mz, float dist, int ... params) {
        Particle e = MCH_ParticlesUtil.doSpawnParticle(s, x, y, z, mx, my, mz, params);
        if (e != null) {
            // empty if block
        }
        return e;
    }

    public static Particle doSpawnParticle(String type, double x, double y, double z, double mx, double my, double mz, int ... params) {
        Minecraft mc = Minecraft.func_71410_x();
        if (mc != null && mc.func_175606_aa() != null && mc.field_71452_i != null) {
            int i = mc.field_71474_y.field_74362_aa;
            if (i == 1 && mc.field_71441_e.field_73012_v.nextInt(3) == 0) {
                i = 2;
            }
            double d6 = mc.func_175606_aa().field_70165_t - x;
            double d7 = mc.func_175606_aa().field_70163_u - y;
            double d8 = mc.func_175606_aa().field_70161_v - z;
            Particle entityfx = null;
            if (type.equalsIgnoreCase("hugeexplosion")) {
                entityfx = MCH_ParticlesUtil.create(ParticleExplosionHuge.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
                mc.field_71452_i.func_78873_a(entityfx);
            } else if (type.equalsIgnoreCase("largeexplode")) {
                entityfx = MCH_ParticlesUtil.create(ParticleExplosionLarge.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
                mc.field_71452_i.func_78873_a(entityfx);
            } else if (type.equalsIgnoreCase("fireworksSpark")) {
                entityfx = MCH_ParticlesUtil.create(ParticleFirework.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
                mc.field_71452_i.func_78873_a(entityfx);
            }
            if (entityfx != null) {
                return entityfx;
            }
            double d9 = 300.0;
            if (d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9) {
                return null;
            }
            if (i > 1) {
                return null;
            }
            if (type.equalsIgnoreCase("bubble")) {
                entityfx = MCH_ParticlesUtil.create(ParticleBubble.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("suspended")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSuspend.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("depthsuspend")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSuspendedTown.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("townaura")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSuspendedTown.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("crit")) {
                entityfx = MCH_ParticlesUtil.create(ParticleCrit.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("magicCrit")) {
                entityfx = MCH_ParticlesUtil.create(ParticleCrit.MagicFactory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("smoke")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSmokeNormal.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("mobSpell")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSpell.MobFactory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("mobSpellAmbient")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSpell.AmbientMobFactory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("spell")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSpell.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("instantSpell")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSpell.InstantFactory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("witchMagic")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSpell.WitchFactory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("note")) {
                entityfx = MCH_ParticlesUtil.create(ParticleNote.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("portal")) {
                entityfx = MCH_ParticlesUtil.create(ParticlePortal.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("enchantmenttable")) {
                entityfx = MCH_ParticlesUtil.create(ParticleEnchantmentTable.EnchantmentTable::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("explode")) {
                entityfx = MCH_ParticlesUtil.create(ParticleExplosion.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("flame")) {
                entityfx = MCH_ParticlesUtil.create(ParticleFlame.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("lava")) {
                entityfx = MCH_ParticlesUtil.create(ParticleLava.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("footstep")) {
                entityfx = MCH_ParticlesUtil.create(ParticleFootStep.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("splash")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSplash.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("wake")) {
                entityfx = MCH_ParticlesUtil.create(ParticleWaterWake.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("largesmoke")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSmokeLarge.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("cloud")) {
                entityfx = MCH_ParticlesUtil.create(ParticleCloud.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("reddust")) {
                entityfx = MCH_ParticlesUtil.create(ParticleRedstone.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("snowballpoof")) {
                entityfx = MCH_ParticlesUtil.create(ParticleBreaking.SnowballFactory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("dripWater")) {
                entityfx = MCH_ParticlesUtil.create(ParticleDrip.WaterFactory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("dripLava")) {
                entityfx = MCH_ParticlesUtil.create(ParticleDrip.LavaFactory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("snowshovel")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSnowShovel.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("slime")) {
                entityfx = MCH_ParticlesUtil.create(ParticleBreaking.SlimeFactory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("heart")) {
                entityfx = MCH_ParticlesUtil.create(ParticleHeart.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("angryVillager")) {
                entityfx = MCH_ParticlesUtil.create(ParticleHeart.AngryVillagerFactory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.equalsIgnoreCase("happyVillager")) {
                entityfx = MCH_ParticlesUtil.create(ParticleSuspendedTown.HappyVillagerFactory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, new int[0]);
            } else if (type.startsWith("iconcrack")) {
                entityfx = MCH_ParticlesUtil.create(ParticleBreaking.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, params);
            } else if (type.startsWith("blockcrack")) {
                entityfx = MCH_ParticlesUtil.create(ParticleDigging.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, params);
            } else if (type.startsWith("blockdust")) {
                entityfx = MCH_ParticlesUtil.create(MCH_EntityBlockDustFX.Factory::new, (World)mc.field_71441_e, x, y, z, mx, my, mz, params);
            }
            if (entityfx != null) {
                mc.field_71452_i.func_78873_a(entityfx);
            }
            return entityfx;
        }
        return null;
    }

    public static void spawnParticle(MCH_ParticleParam p) {
        if (p.world.field_72995_K) {
            MCH_EntityParticleBase entityFX = null;
            entityFX = p.name.equalsIgnoreCase("Splash") ? new MCH_EntityParticleSplash(p.world, p.posX, p.posY, p.posZ, p.motionX, p.motionY, p.motionZ) : new MCH_EntityParticleSmoke(p.world, p.posX, p.posY, p.posZ, p.motionX, p.motionY, p.motionZ);
            entityFX.func_70538_b(p.r, p.g, p.b);
            entityFX.func_82338_g(p.a);
            if (p.age > 0) {
                entityFX.setParticleMaxAge(p.age);
            }
            entityFX.moutionYUpAge = p.motionYUpAge;
            entityFX.gravity = p.gravity;
            entityFX.isEffectedWind = p.isEffectWind;
            entityFX.diffusible = p.diffusible;
            entityFX.toWhite = p.toWhite;
            if (p.diffusible) {
                entityFX.setParticleScale(p.size * 0.2f);
                entityFX.particleMaxScale = p.size * 2.0f;
            } else {
                entityFX.setParticleScale(p.size);
            }
            FMLClientHandler.instance().getClient().field_71452_i.func_78873_a((Particle)entityFX);
        }
    }

    public static void spawnMarkPoint(EntityPlayer player, double x, double y, double z) {
        MCH_ParticlesUtil.clearMarkPoint();
        markPoint = new MCH_EntityParticleMarkPoint(player.field_70170_p, x, y, z, player.func_96124_cp());
        FMLClientHandler.instance().getClient().field_71452_i.func_78873_a((Particle)markPoint);
    }

    public static void clearMarkPoint() {
        if (markPoint != null) {
            markPoint.func_187112_i();
            markPoint = null;
        }
    }

    private static Particle create(Supplier<IParticleFactory> factoryFunc, World world, double x, double y, double z, double mx, double my, double mz, int ... param) {
        return factoryFunc.get().func_178902_a(-1, world, x, y, z, mx, my, mz, param);
    }
}

