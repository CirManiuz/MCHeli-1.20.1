/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.passive.EntityVillager
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.datasync.DataParameter
 *  net.minecraft.network.datasync.DataSerializer
 *  net.minecraft.network.datasync.DataSerializers
 *  net.minecraft.network.datasync.EntityDataManager
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.weapon;

import java.util.List;
import javax.annotation.Nullable;
import mcheli.MCH_Config;
import mcheli.MCH_Explosion;
import mcheli.MCH_Lib;
import mcheli.__helper.MCH_CriteriaTriggers;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntityHitBox;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.aircraft.MCH_PacketNotifyHitBullet;
import mcheli.chain.MCH_EntityChain;
import mcheli.particles.MCH_ParticleParam;
import mcheli.particles.MCH_ParticlesUtil;
import mcheli.weapon.MCH_BulletModel;
import mcheli.weapon.MCH_EntityBullet;
import mcheli.weapon.MCH_EntityRocket;
import mcheli.weapon.MCH_WeaponBase;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponInfoManager;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_EntityPlayer;
import mcheli.wrapper.W_MovingObjectPosition;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MCH_EntityBaseBullet
extends W_Entity {
    private static final DataParameter<Integer> TARGET_ID = EntityDataManager.func_187226_a(MCH_EntityBaseBullet.class, (DataSerializer)DataSerializers.field_187192_b);
    private static final DataParameter<String> INFO_NAME = EntityDataManager.func_187226_a(MCH_EntityBaseBullet.class, (DataSerializer)DataSerializers.field_187194_d);
    private static final DataParameter<String> BULLET_MODEL = EntityDataManager.func_187226_a(MCH_EntityBaseBullet.class, (DataSerializer)DataSerializers.field_187194_d);
    private static final DataParameter<Byte> BOMBLET_FLAG = EntityDataManager.func_187226_a(MCH_EntityBaseBullet.class, (DataSerializer)DataSerializers.field_187191_a);
    public Entity shootingEntity;
    public Entity shootingAircraft;
    private int countOnUpdate = 0;
    public int explosionPower;
    public int explosionPowerInWater;
    private int power;
    public double acceleration;
    public double accelerationFactor;
    public Entity targetEntity;
    public int piercing;
    public int delayFuse;
    public int sprinkleTime;
    public byte isBomblet;
    private MCH_WeaponInfo weaponInfo;
    private MCH_BulletModel model;
    public double prevPosX2;
    public double prevPosY2;
    public double prevPosZ2;
    public double prevMotionX;
    public double prevMotionY;
    public double prevMotionZ;

    public MCH_EntityBaseBullet(World par1World) {
        super(par1World);
        this.func_70105_a(1.0f, 1.0f);
        this.field_70126_B = this.field_70177_z;
        this.field_70127_C = this.field_70125_A;
        this.targetEntity = null;
        this.setPower(1);
        this.acceleration = 1.0;
        this.accelerationFactor = 1.0;
        this.piercing = 0;
        this.explosionPower = 0;
        this.explosionPowerInWater = 0;
        this.delayFuse = 0;
        this.sprinkleTime = 0;
        this.isBomblet = (byte)-1;
        this.weaponInfo = null;
        this.field_70158_ak = true;
        if (par1World.field_72995_K) {
            this.model = null;
        }
    }

    public MCH_EntityBaseBullet(World par1World, double px, double py, double pz, double mx, double my, double mz, float yaw, float pitch, double acceleration) {
        this(par1World);
        this.func_70105_a(1.0f, 1.0f);
        this.func_70012_b(px, py, pz, yaw, pitch);
        this.func_70107_b(px, py, pz);
        this.field_70126_B = yaw;
        this.field_70127_C = pitch;
        if (acceleration > 3.9) {
            acceleration = 3.9;
        }
        double d = MathHelper.func_76133_a((double)(mx * mx + my * my + mz * mz));
        this.field_70159_w = mx * acceleration / d;
        this.field_70181_x = my * acceleration / d;
        this.field_70179_y = mz * acceleration / d;
        this.prevMotionX = this.field_70159_w;
        this.prevMotionY = this.field_70181_x;
        this.prevMotionZ = this.field_70179_y;
        this.acceleration = acceleration;
    }

    public void func_70012_b(double par1, double par3, double par5, float par7, float par8) {
        super.func_70012_b(par1, par3, par5, par7, par8);
        this.prevPosX2 = par1;
        this.prevPosY2 = par3;
        this.prevPosZ2 = par5;
    }

    public void func_70080_a(double x, double y, double z, float yaw, float pitch) {
        super.func_70080_a(x, y, z, yaw, pitch);
    }

    protected void func_70101_b(float yaw, float pitch) {
        super.func_70101_b(yaw, this.field_70125_A);
    }

    @SideOnly(value=Side.CLIENT)
    public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.func_70107_b(x, (y + this.field_70163_u * 2.0) / 3.0, z);
        this.func_70101_b(yaw, pitch);
    }

    @Override
    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(TARGET_ID, (Object)0);
        this.field_70180_af.func_187214_a(INFO_NAME, (Object)"");
        this.field_70180_af.func_187214_a(BULLET_MODEL, (Object)"");
        this.field_70180_af.func_187214_a(BOMBLET_FLAG, (Object)0);
    }

    public void setName(String s) {
        if (s != null && !s.isEmpty()) {
            this.weaponInfo = MCH_WeaponInfoManager.get(s);
            if (this.weaponInfo != null) {
                if (!this.field_70170_p.field_72995_K) {
                    this.field_70180_af.func_187227_b(INFO_NAME, (Object)s);
                }
                this.onSetWeasponInfo();
            }
        }
    }

    public String func_70005_c_() {
        return (String)this.field_70180_af.func_187225_a(INFO_NAME);
    }

    @Nullable
    public MCH_WeaponInfo getInfo() {
        return this.weaponInfo;
    }

    public void onSetWeasponInfo() {
        if (!this.field_70170_p.field_72995_K) {
            this.isBomblet = 0;
        }
        if (this.getInfo().bomblet > 0) {
            this.sprinkleTime = this.getInfo().bombletSTime;
        }
        this.piercing = this.getInfo().piercing;
        if (this instanceof MCH_EntityBullet) {
            if (this.getInfo().acceleration > 4.0f) {
                this.accelerationFactor = this.getInfo().acceleration / 4.0f;
            }
        } else if (this instanceof MCH_EntityRocket && this.isBomblet == 0 && this.getInfo().acceleration > 4.0f) {
            this.accelerationFactor = this.getInfo().acceleration / 4.0f;
        }
    }

    public void func_70106_y() {
        super.func_70106_y();
    }

    public void setBomblet() {
        this.isBomblet = 1;
        this.sprinkleTime = 0;
        this.field_70180_af.func_187227_b(BOMBLET_FLAG, (Object)1);
    }

    public byte getBomblet() {
        return (Byte)this.field_70180_af.func_187225_a(BOMBLET_FLAG);
    }

    public void setTargetEntity(@Nullable Entity entity) {
        this.targetEntity = entity;
        if (!this.field_70170_p.field_72995_K) {
            if (this.targetEntity instanceof EntityPlayerMP) {
                MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityBaseBullet.setTargetEntity alert" + this.targetEntity + " / " + this.targetEntity.func_184187_bx(), new Object[0]);
                if (this.targetEntity.func_184187_bx() != null && !(this.targetEntity.func_184187_bx() instanceof MCH_EntityAircraft) && !(this.targetEntity.func_184187_bx() instanceof MCH_EntitySeat)) {
                    W_WorldFunc.MOD_playSoundAtEntity(this.targetEntity, "alert", 2.0f, 1.0f);
                }
            }
            if (entity != null) {
                this.field_70180_af.func_187227_b(TARGET_ID, (Object)W_Entity.getEntityId(entity));
            } else {
                this.field_70180_af.func_187227_b(TARGET_ID, (Object)0);
            }
        }
    }

    public int getTargetEntityID() {
        if (this.targetEntity != null) {
            return W_Entity.getEntityId(this.targetEntity);
        }
        return (Integer)this.field_70180_af.func_187225_a(TARGET_ID);
    }

    public MCH_BulletModel getBulletModel() {
        if (this.getInfo() == null) {
            return null;
        }
        if (this.isBomblet < 0) {
            return null;
        }
        if (this.model == null) {
            this.model = this.isBomblet == 1 ? this.getInfo().bombletModel : this.getInfo().bulletModel;
            if (this.model == null) {
                this.model = this.getDefaultBulletModel();
            }
        }
        return this.model;
    }

    public abstract MCH_BulletModel getDefaultBulletModel();

    public void sprinkleBomblet() {
    }

    public void spawnParticle(String name, int num, float size) {
        block5: {
            if (!this.field_70170_p.field_72995_K) break block5;
            if (name.isEmpty() || num < 1 || num > 50) {
                return;
            }
            double x = (this.field_70165_t - this.field_70169_q) / (double)num;
            double y = (this.field_70163_u - this.field_70167_r) / (double)num;
            double z = (this.field_70161_v - this.field_70166_s) / (double)num;
            double x2 = (this.field_70169_q - this.prevPosX2) / (double)num;
            double y2 = (this.field_70167_r - this.prevPosY2) / (double)num;
            double z2 = (this.field_70166_s - this.prevPosZ2) / (double)num;
            if (name.equals("explode")) {
                for (int i = 0; i < num; ++i) {
                    MCH_ParticleParam prm = new MCH_ParticleParam(this.field_70170_p, "smoke", (this.field_70169_q + x * (double)i + (this.prevPosX2 + x2 * (double)i)) / 2.0, (this.field_70167_r + y * (double)i + (this.prevPosY2 + y2 * (double)i)) / 2.0, (this.field_70166_s + z * (double)i + (this.prevPosZ2 + z2 * (double)i)) / 2.0);
                    prm.size = size + this.field_70146_Z.nextFloat();
                    MCH_ParticlesUtil.spawnParticle(prm);
                }
            } else {
                for (int i = 0; i < num; ++i) {
                    MCH_ParticlesUtil.DEF_spawnParticle(name, (this.field_70169_q + x * (double)i + (this.prevPosX2 + x2 * (double)i)) / 2.0, (this.field_70167_r + y * (double)i + (this.prevPosY2 + y2 * (double)i)) / 2.0, (this.field_70166_s + z * (double)i + (this.prevPosZ2 + z2 * (double)i)) / 2.0, 0.0, 0.0, 0.0, 50.0f, new int[0]);
                }
            }
        }
    }

    public void DEF_spawnParticle(String name, int num, float size) {
        if (this.field_70170_p.field_72995_K) {
            if (name.isEmpty() || num < 1 || num > 50) {
                return;
            }
            double x = (this.field_70165_t - this.field_70169_q) / (double)num;
            double y = (this.field_70163_u - this.field_70167_r) / (double)num;
            double z = (this.field_70161_v - this.field_70166_s) / (double)num;
            double x2 = (this.field_70169_q - this.prevPosX2) / (double)num;
            double y2 = (this.field_70167_r - this.prevPosY2) / (double)num;
            double z2 = (this.field_70166_s - this.prevPosZ2) / (double)num;
            for (int i = 0; i < num; ++i) {
                MCH_ParticlesUtil.DEF_spawnParticle(name, (this.field_70169_q + x * (double)i + (this.prevPosX2 + x2 * (double)i)) / 2.0, (this.field_70167_r + y * (double)i + (this.prevPosY2 + y2 * (double)i)) / 2.0, (this.field_70166_s + z * (double)i + (this.prevPosZ2 + z2 * (double)i)) / 2.0, 0.0, 0.0, 0.0, 150.0f, new int[0]);
            }
        }
    }

    public int getCountOnUpdate() {
        return this.countOnUpdate;
    }

    public void clearCountOnUpdate() {
        this.countOnUpdate = 0;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean func_70112_a(double par1) {
        double d1 = this.func_174813_aQ().func_72320_b() * 4.0;
        return par1 < (d1 *= 64.0) * d1;
    }

    public void setParameterFromWeapon(MCH_WeaponBase w, Entity entity, Entity user) {
        this.explosionPower = w.explosionPower;
        this.explosionPowerInWater = w.explosionPowerInWater;
        this.setPower(w.power);
        this.piercing = w.piercing;
        this.shootingAircraft = entity;
        this.shootingEntity = user;
    }

    public void setParameterFromWeapon(MCH_EntityBaseBullet b, Entity entity, Entity user) {
        this.explosionPower = b.explosionPower;
        this.explosionPowerInWater = b.explosionPowerInWater;
        this.setPower(b.getPower());
        this.piercing = b.piercing;
        this.shootingAircraft = entity;
        this.shootingEntity = user;
    }

    public void setMotion(double targetX, double targetY, double targetZ) {
        double d6 = MathHelper.func_76133_a((double)(targetX * targetX + targetY * targetY + targetZ * targetZ));
        this.field_70159_w = targetX * this.acceleration / d6;
        this.field_70181_x = targetY * this.acceleration / d6;
        this.field_70179_y = targetZ * this.acceleration / d6;
    }

    public boolean usingFlareOfTarget(Entity entity) {
        if (this.getCountOnUpdate() % 3 == 0) {
            List list = this.field_70170_p.func_72839_b((Entity)this, entity.func_174813_aQ().func_72314_b(15.0, 15.0, 15.0));
            for (int i = 0; i < list.size(); ++i) {
                if (!((Entity)list.get(i)).getEntityData().func_74767_n("FlareUsing")) continue;
                return true;
            }
        }
        return false;
    }

    public void guidanceToTarget(double targetPosX, double targetPosY, double targetPosZ) {
        this.guidanceToTarget(targetPosX, targetPosY, targetPosZ, 1.0f);
    }

    public void guidanceToTarget(double targetPosX, double targetPosY, double targetPosZ, float accelerationFactor) {
        double tx = targetPosX - this.field_70165_t;
        double ty = targetPosY - this.field_70163_u;
        double tz = targetPosZ - this.field_70161_v;
        double d = MathHelper.func_76133_a((double)(tx * tx + ty * ty + tz * tz));
        double mx = tx * this.acceleration / d;
        double my = ty * this.acceleration / d;
        double mz = tz * this.acceleration / d;
        this.field_70159_w = (this.field_70159_w * 6.0 + mx) / 7.0;
        this.field_70181_x = (this.field_70181_x * 6.0 + my) / 7.0;
        this.field_70179_y = (this.field_70179_y * 6.0 + mz) / 7.0;
        double a = (float)Math.atan2(this.field_70179_y, this.field_70159_w);
        this.field_70177_z = (float)(a * 180.0 / Math.PI) - 90.0f;
        double r = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        this.field_70125_A = -((float)(Math.atan2(this.field_70181_x, r) * 180.0 / Math.PI));
    }

    public boolean checkValid() {
        if (this.shootingEntity == null && this.shootingAircraft == null) {
            return false;
        }
        Entity shooter = this.shootingAircraft == null || !this.shootingAircraft.field_70128_L || this.shootingEntity != null ? this.shootingEntity : this.shootingAircraft;
        double x = this.field_70165_t - shooter.field_70165_t;
        double z = this.field_70161_v - shooter.field_70161_v;
        return x * x + z * z < 3.38724E7 && this.field_70163_u > -10.0;
    }

    public float getGravity() {
        return this.getInfo() != null ? this.getInfo().gravity : 0.0f;
    }

    public float getGravityInWater() {
        return this.getInfo() != null ? this.getInfo().gravityInWater : 0.0f;
    }

    public void func_70071_h_() {
        int tgtEttId;
        if (this.field_70170_p.field_72995_K && this.countOnUpdate == 0 && (tgtEttId = this.getTargetEntityID()) > 0) {
            this.setTargetEntity(this.field_70170_p.func_73045_a(tgtEttId));
        }
        if (!this.field_70170_p.field_72995_K && this.getCountOnUpdate() % 20 == 19 && this.targetEntity instanceof EntityPlayerMP) {
            MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityBaseBullet.onUpdate alert" + this.targetEntity + " / " + this.targetEntity.func_184187_bx(), new Object[0]);
            if (this.targetEntity.func_184187_bx() != null && !(this.targetEntity.func_184187_bx() instanceof MCH_EntityAircraft) && !(this.targetEntity.func_184187_bx() instanceof MCH_EntitySeat)) {
                W_WorldFunc.MOD_playSoundAtEntity(this.targetEntity, "alert", 2.0f, 1.0f);
            }
        }
        this.prevMotionX = this.field_70159_w;
        this.prevMotionY = this.field_70181_x;
        this.prevMotionZ = this.field_70179_y;
        ++this.countOnUpdate;
        if (this.countOnUpdate > 10000000) {
            this.clearCountOnUpdate();
        }
        this.prevPosX2 = this.field_70169_q;
        this.prevPosY2 = this.field_70167_r;
        this.prevPosZ2 = this.field_70166_s;
        super.func_70071_h_();
        if ((this.prevMotionX != this.field_70159_w || this.prevMotionY != this.field_70181_x || this.prevMotionZ != this.field_70179_y) && this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y > 0.1) {
            double a = (float)Math.atan2(this.field_70179_y, this.field_70159_w);
            this.field_70177_z = (float)(a * 180.0 / Math.PI) - 90.0f;
            double r = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
            this.field_70125_A = -((float)(Math.atan2(this.field_70181_x, r) * 180.0 / Math.PI));
        }
        if (this.getInfo() == null) {
            if (this.countOnUpdate >= 2) {
                MCH_Lib.Log(this, "##### MCH_EntityBaseBullet onUpdate() Weapon info null %d, %s, Name=%s", W_Entity.getEntityId(this), this.getEntityName(), this.func_70005_c_());
                this.func_70106_y();
                return;
            }
            this.setName(this.func_70005_c_());
            if (this.getInfo() == null) {
                return;
            }
        }
        if (this.getInfo().bound <= 0.0f && this.field_70122_E) {
            this.field_70159_w *= 0.9;
            this.field_70179_y *= 0.9;
        }
        if (this.field_70170_p.field_72995_K && this.isBomblet < 0) {
            this.isBomblet = this.getBomblet();
        }
        if (!this.field_70170_p.field_72995_K) {
            BlockPos blockpos = new BlockPos(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            if ((int)this.field_70163_u <= 255 && !this.field_70170_p.func_175667_e(blockpos)) {
                if (this.getInfo().delayFuse > 0) {
                    if (this.delayFuse == 0) {
                        this.delayFuse = this.getInfo().delayFuse;
                    }
                } else {
                    this.func_70106_y();
                    return;
                }
            }
            if (this.delayFuse > 0) {
                --this.delayFuse;
                if (this.delayFuse == 0) {
                    this.onUpdateTimeout();
                    this.func_70106_y();
                    return;
                }
            }
            if (!this.checkValid()) {
                this.func_70106_y();
                return;
            }
            if (this.getInfo().timeFuse > 0 && this.getCountOnUpdate() > this.getInfo().timeFuse) {
                this.onUpdateTimeout();
                this.func_70106_y();
                return;
            }
            if (this.getInfo().explosionAltitude > 0 && MCH_Lib.getBlockIdY(this, 3, -this.getInfo().explosionAltitude) != 0) {
                RayTraceResult mop = new RayTraceResult(new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v), EnumFacing.DOWN, new BlockPos(this.field_70165_t, this.field_70163_u, this.field_70161_v));
                this.onImpact(mop, 1.0f);
            }
        }
        this.field_70181_x = !this.func_70090_H() ? (this.field_70181_x += (double)this.getGravity()) : (this.field_70181_x += (double)this.getGravityInWater());
        if (!this.field_70128_L) {
            this.onUpdateCollided();
        }
        this.field_70165_t += this.field_70159_w * this.accelerationFactor;
        this.field_70163_u += this.field_70181_x * this.accelerationFactor;
        this.field_70161_v += this.field_70179_y * this.accelerationFactor;
        if (this.field_70170_p.field_72995_K) {
            this.updateSplash();
        }
        if (this.func_70090_H()) {
            float f3 = 0.25f;
            this.field_70170_p.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.field_70165_t - this.field_70159_w * (double)f3, this.field_70163_u - this.field_70181_x * (double)f3, this.field_70161_v - this.field_70179_y * (double)f3, this.field_70159_w, this.field_70181_x, this.field_70179_y, new int[0]);
        }
        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
    }

    public void updateSplash() {
        if (this.getInfo() == null) {
            return;
        }
        if (this.getInfo().power <= 0) {
            return;
        }
        if (!W_WorldFunc.isBlockWater(this.field_70170_p, (int)(this.field_70169_q + 0.5), (int)(this.field_70167_r + 0.5), (int)(this.field_70166_s + 0.5)) && W_WorldFunc.isBlockWater(this.field_70170_p, (int)(this.field_70165_t + 0.5), (int)(this.field_70163_u + 0.5), (int)(this.field_70161_v + 0.5))) {
            double x = this.field_70165_t - this.field_70169_q;
            double y = this.field_70163_u - this.field_70167_r;
            double z = this.field_70161_v - this.field_70166_s;
            double d = Math.sqrt(x * x + y * y + z * z);
            if (d <= 0.15) {
                return;
            }
            x /= d;
            y /= d;
            z /= d;
            double px = this.field_70169_q;
            double py = this.field_70167_r;
            double pz = this.field_70166_s;
            int i = 0;
            while ((double)i <= d) {
                if (W_WorldFunc.isBlockWater(this.field_70170_p, (int)((px += x) + 0.5), (int)((py += y) + 0.5), (int)((pz += z) + 0.5))) {
                    float pwr = this.getInfo().power < 20 ? (float)this.getInfo().power : 20.0f;
                    int n = this.field_70146_Z.nextInt(1 + (int)pwr / 3) + (int)pwr / 2 + 1;
                    pwr *= 0.03f;
                    for (int j = 0; j < n; ++j) {
                        MCH_ParticleParam prm = new MCH_ParticleParam(this.field_70170_p, "splash", px, py + 0.5, pz, (double)pwr * (this.field_70146_Z.nextDouble() - 0.5) * 0.3, (double)pwr * (this.field_70146_Z.nextDouble() * 0.5 + 0.5) * 1.8, (double)pwr * (this.field_70146_Z.nextDouble() - 0.5) * 0.3, pwr * 5.0f);
                        MCH_ParticlesUtil.spawnParticle(prm);
                    }
                    break;
                }
                ++i;
            }
        }
    }

    public void onUpdateTimeout() {
        if (this.func_70090_H()) {
            if (this.explosionPowerInWater > 0) {
                this.newExplosion(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.explosionPowerInWater, this.explosionPowerInWater, true);
            }
        } else if (this.explosionPower > 0) {
            this.newExplosion(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.explosionPower, this.getInfo().explosionBlock, false);
        } else if (this.explosionPower < 0) {
            this.playExplosionSound();
        }
    }

    public void onUpdateBomblet() {
        if (!this.field_70170_p.field_72995_K && this.sprinkleTime > 0 && !this.field_70128_L) {
            --this.sprinkleTime;
            if (this.sprinkleTime == 0) {
                for (int i = 0; i < this.getInfo().bomblet; ++i) {
                    this.sprinkleBomblet();
                }
                this.func_70106_y();
            }
        }
    }

    public void boundBullet(EnumFacing sideHit) {
        switch (sideHit) {
            case DOWN: {
                if (!(this.field_70181_x > 0.0)) break;
                this.field_70181_x = -this.field_70181_x * (double)this.getInfo().bound;
                break;
            }
            case UP: {
                if (!(this.field_70181_x < 0.0)) break;
                this.field_70181_x = -this.field_70181_x * (double)this.getInfo().bound;
                break;
            }
            case NORTH: {
                if (this.field_70179_y > 0.0) {
                    this.field_70179_y = -this.field_70179_y * (double)this.getInfo().bound;
                    break;
                }
                this.field_70161_v += this.field_70179_y;
                break;
            }
            case SOUTH: {
                if (this.field_70179_y < 0.0) {
                    this.field_70179_y = -this.field_70179_y * (double)this.getInfo().bound;
                    break;
                }
                this.field_70161_v += this.field_70179_y;
                break;
            }
            case WEST: {
                if (this.field_70159_w > 0.0) {
                    this.field_70159_w = -this.field_70159_w * (double)this.getInfo().bound;
                    break;
                }
                this.field_70165_t += this.field_70159_w;
                break;
            }
            case EAST: {
                if (this.field_70159_w < 0.0) {
                    this.field_70159_w = -this.field_70159_w * (double)this.getInfo().bound;
                    break;
                }
                this.field_70165_t += this.field_70159_w;
            }
        }
        if (this.getInfo().bound <= 0.0f) {
            this.field_70159_w *= 0.25;
            this.field_70181_x *= 0.25;
            this.field_70179_y *= 0.25;
        }
    }

    protected void onUpdateCollided() {
        float damageFator = 1.0f;
        double mx = this.field_70159_w * this.accelerationFactor;
        double my = this.field_70181_x * this.accelerationFactor;
        double mz = this.field_70179_y * this.accelerationFactor;
        RayTraceResult m = null;
        for (int i = 0; i < 5; ++i) {
            Block block;
            Vec3d vec3 = W_WorldFunc.getWorldVec3(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v);
            Vec3d vec31 = W_WorldFunc.getWorldVec3(this.field_70170_p, this.field_70165_t + mx, this.field_70163_u + my, this.field_70161_v + mz);
            m = W_WorldFunc.clip(this.field_70170_p, vec3, vec31);
            boolean continueClip = false;
            if (this.shootingEntity != null && W_MovingObjectPosition.isHitTypeTile(m) && MCH_Config.bulletBreakableBlocks.contains(block = W_WorldFunc.getBlock(this.field_70170_p, m.func_178782_a()))) {
                W_WorldFunc.destroyBlock(this.field_70170_p, m.func_178782_a(), true);
                continueClip = true;
            }
            if (!continueClip) break;
        }
        Vec3d vec3 = W_WorldFunc.getWorldVec3(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v);
        Vec3d vec31 = W_WorldFunc.getWorldVec3(this.field_70170_p, this.field_70165_t + mx, this.field_70163_u + my, this.field_70161_v + mz);
        if (this.getInfo().delayFuse > 0) {
            if (m != null) {
                this.boundBullet(m.field_178784_b);
                if (this.delayFuse == 0) {
                    this.delayFuse = this.getInfo().delayFuse;
                }
            }
            return;
        }
        if (m != null) {
            vec31 = W_WorldFunc.getWorldVec3(this.field_70170_p, m.field_72307_f.field_72450_a, m.field_72307_f.field_72448_b, m.field_72307_f.field_72449_c);
        }
        Entity entity = null;
        List list = this.field_70170_p.func_72839_b((Entity)this, this.func_174813_aQ().func_72321_a(mx, my, mz).func_72314_b(21.0, 21.0, 21.0));
        double d0 = 0.0;
        for (int j = 0; j < list.size(); ++j) {
            double d1;
            Entity entity1 = (Entity)list.get(j);
            if (!this.canBeCollidedEntity(entity1)) continue;
            float f = 0.3f;
            AxisAlignedBB axisalignedbb = entity1.func_174813_aQ().func_72314_b((double)f, (double)f, (double)f);
            RayTraceResult m1 = axisalignedbb.func_72327_a(vec3, vec31);
            if (m1 == null || !((d1 = vec3.func_72438_d(m1.field_72307_f)) < d0) && d0 != 0.0) continue;
            entity = entity1;
            d0 = d1;
        }
        if (entity != null) {
            m = new RayTraceResult(entity);
        }
        if (m != null) {
            this.onImpact(m, damageFator);
        }
    }

    public boolean canBeCollidedEntity(Entity entity) {
        if (entity instanceof MCH_EntityChain) {
            return false;
        }
        if (!entity.func_70067_L()) {
            return false;
        }
        if (entity instanceof MCH_EntityBaseBullet) {
            if (this.field_70170_p.field_72995_K) {
                return false;
            }
            MCH_EntityBaseBullet blt = (MCH_EntityBaseBullet)entity;
            if (W_Entity.isEqual(blt.shootingAircraft, this.shootingAircraft)) {
                return false;
            }
            if (W_Entity.isEqual(blt.shootingEntity, this.shootingEntity)) {
                return false;
            }
        }
        if (entity instanceof MCH_EntitySeat) {
            return false;
        }
        if (entity instanceof MCH_EntityHitBox) {
            return false;
        }
        if (W_Entity.isEqual(entity, this.shootingEntity)) {
            return false;
        }
        if (this.shootingAircraft instanceof MCH_EntityAircraft) {
            if (W_Entity.isEqual(entity, this.shootingAircraft)) {
                return false;
            }
            if (((MCH_EntityAircraft)this.shootingAircraft).isMountedEntity(entity)) {
                return false;
            }
        }
        for (String s : MCH_Config.IgnoreBulletHitList) {
            if (entity.getClass().getName().toLowerCase().indexOf(s.toLowerCase()) < 0) continue;
            return false;
        }
        return true;
    }

    public void notifyHitBullet() {
        if (this.shootingAircraft instanceof MCH_EntityAircraft && W_EntityPlayer.isPlayer(this.shootingEntity)) {
            MCH_PacketNotifyHitBullet.send((MCH_EntityAircraft)this.shootingAircraft, (EntityPlayer)this.shootingEntity);
        }
        if (W_EntityPlayer.isPlayer(this.shootingEntity)) {
            MCH_PacketNotifyHitBullet.send(null, (EntityPlayer)this.shootingEntity);
        }
    }

    protected void onImpact(RayTraceResult m, float damageFactor) {
        if (!this.field_70170_p.field_72995_K) {
            if (m.field_72308_g != null) {
                this.onImpactEntity(m.field_72308_g, damageFactor);
                this.piercing = 0;
            }
            float expPower = (float)this.explosionPower * damageFactor;
            float expPowerInWater = (float)this.explosionPowerInWater * damageFactor;
            double dx = 0.0;
            double dy = 0.0;
            double dz = 0.0;
            if (this.piercing > 0) {
                --this.piercing;
                if (expPower > 0.0f) {
                    this.newExplosion(m.field_72307_f.field_72450_a + dx, m.field_72307_f.field_72448_b + dy, m.field_72307_f.field_72449_c + dz, 1.0f, 1.0f, false);
                }
            } else {
                if (expPowerInWater == 0.0f) {
                    if (this.getInfo().isFAE) {
                        this.newFAExplosion(this.field_70165_t, this.field_70163_u, this.field_70161_v, expPower, this.getInfo().explosionBlock);
                    } else if (expPower > 0.0f) {
                        this.newExplosion(m.field_72307_f.field_72450_a + dx, m.field_72307_f.field_72448_b + dy, m.field_72307_f.field_72449_c + dz, expPower, this.getInfo().explosionBlock, false);
                    } else if (expPower < 0.0f) {
                        this.playExplosionSound();
                    }
                } else if (m.field_72308_g != null) {
                    if (this.func_70090_H()) {
                        this.newExplosion(m.field_72307_f.field_72450_a + dx, m.field_72307_f.field_72448_b + dy, m.field_72307_f.field_72449_c + dz, expPowerInWater, expPowerInWater, true);
                    } else {
                        this.newExplosion(m.field_72307_f.field_72450_a + dx, m.field_72307_f.field_72448_b + dy, m.field_72307_f.field_72449_c + dz, expPower, this.getInfo().explosionBlock, false);
                    }
                } else if (this.func_70090_H() || MCH_Lib.isBlockInWater(this.field_70170_p, m.func_178782_a().func_177958_n(), m.func_178782_a().func_177956_o(), m.func_178782_a().func_177952_p())) {
                    this.newExplosion(m.func_178782_a().func_177958_n(), m.func_178782_a().func_177956_o(), m.func_178782_a().func_177952_p(), expPowerInWater, expPowerInWater, true);
                } else if (expPower > 0.0f) {
                    this.newExplosion(m.field_72307_f.field_72450_a + dx, m.field_72307_f.field_72448_b + dy, m.field_72307_f.field_72449_c + dz, expPower, this.getInfo().explosionBlock, false);
                } else if (expPower < 0.0f) {
                    this.playExplosionSound();
                }
                this.func_70106_y();
            }
        } else if (this.getInfo() != null && (this.getInfo().explosion == 0 || this.getInfo().modeNum >= 2) && W_MovingObjectPosition.isHitTypeTile(m)) {
            float p = this.getInfo().power;
            int i = 0;
            while ((float)i < p / 3.0f) {
                MCH_ParticlesUtil.spawnParticleTileCrack(this.field_70170_p, m.func_178782_a().func_177958_n(), m.func_178782_a().func_177956_o(), m.func_178782_a().func_177952_p(), m.field_72307_f.field_72450_a + ((double)this.field_70146_Z.nextFloat() - 0.5) * (double)p / 10.0, m.field_72307_f.field_72448_b + 0.1, m.field_72307_f.field_72449_c + ((double)this.field_70146_Z.nextFloat() - 0.5) * (double)p / 10.0, -this.field_70159_w * (double)p / 2.0, p / 2.0f, -this.field_70179_y * (double)p / 2.0);
                ++i;
            }
        }
    }

    public void onImpactEntity(Entity entity, float damageFactor) {
        if (!entity.field_70128_L) {
            MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityBaseBullet.onImpactEntity:Damage=%d:" + entity.getClass(), this.getPower());
            MCH_Lib.applyEntityHurtResistantTimeConfig(entity);
            DamageSource ds = DamageSource.func_76356_a((Entity)this, (Entity)this.shootingEntity);
            float damage = MCH_Config.applyDamageVsEntity(entity, ds, (float)this.getPower() * damageFactor);
            entity.func_70097_a(ds, damage *= this.getInfo() != null ? this.getInfo().getDamageFactor(entity) : 1.0f);
            if (this instanceof MCH_EntityBullet && entity instanceof EntityVillager && this.shootingEntity != null && this.shootingEntity instanceof EntityPlayerMP && this.shootingEntity.func_184187_bx() instanceof MCH_EntitySeat) {
                MCH_CriteriaTriggers.VILLAGER_HURT_BULLET.trigger((EntityPlayerMP)this.shootingEntity);
            }
            if (!entity.field_70128_L) {
                // empty if block
            }
        }
        this.notifyHitBullet();
    }

    public void newFAExplosion(double x, double y, double z, float exp, float expBlock) {
        MCH_Explosion.ExplosionResult result = MCH_Explosion.newExplosion(this.field_70170_p, this, this.shootingEntity, x, y, z, exp, expBlock, true, true, this.getInfo().flaming, false, 15);
        if (result != null && result.hitEntity) {
            this.notifyHitBullet();
        }
    }

    public void newExplosion(double x, double y, double z, float exp, float expBlock, boolean inWater) {
        MCH_Explosion.ExplosionResult result = !inWater ? MCH_Explosion.newExplosion(this.field_70170_p, this, this.shootingEntity, x, y, z, exp, expBlock, this.field_70146_Z.nextInt(3) == 0, true, this.getInfo().flaming, true, 0, this.getInfo() != null ? this.getInfo().damageFactor : null) : MCH_Explosion.newExplosionInWater(this.field_70170_p, this, this.shootingEntity, x, y, z, exp, expBlock, this.field_70146_Z.nextInt(3) == 0, true, this.getInfo().flaming, true, 0, this.getInfo() != null ? this.getInfo().damageFactor : null);
        if (result != null && result.hitEntity) {
            this.notifyHitBullet();
        }
    }

    public void playExplosionSound() {
        MCH_Explosion.playExplosionSound(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v);
    }

    public void func_70014_b(NBTTagCompound par1NBTTagCompound) {
        par1NBTTagCompound.func_74782_a("direction", (NBTBase)this.func_70087_a(new double[]{this.field_70159_w, this.field_70181_x, this.field_70179_y}));
        par1NBTTagCompound.func_74778_a("WeaponName", this.func_70005_c_());
    }

    public void func_70037_a(NBTTagCompound par1NBTTagCompound) {
        this.func_70106_y();
    }

    public boolean func_70067_L() {
        return true;
    }

    public float func_70111_Y() {
        return 1.0f;
    }

    @Override
    public boolean func_70097_a(DamageSource ds, float par2) {
        if (this.func_180431_b(ds)) {
            return false;
        }
        if (!this.field_70170_p.field_72995_K && par2 > 0.0f && ds.func_76355_l().equalsIgnoreCase("thrown")) {
            this.func_70018_K();
            Vec3d pos = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            RayTraceResult m = new RayTraceResult(pos, EnumFacing.DOWN, new BlockPos(pos));
            this.onImpact(m, 1.0f);
            return true;
        }
        return false;
    }

    @SideOnly(value=Side.CLIENT)
    public float getShadowSize() {
        return 0.0f;
    }

    public float getBrightness(float par1) {
        return 1.0f;
    }

    @SideOnly(value=Side.CLIENT)
    public int getBrightnessForRender(float par1) {
        return 0xF000F0;
    }

    public int getPower() {
        return this.power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}

