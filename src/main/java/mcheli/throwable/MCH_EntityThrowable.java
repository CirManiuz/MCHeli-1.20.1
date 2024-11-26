/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.projectile.EntityThrowable
 *  net.minecraft.network.datasync.DataParameter
 *  net.minecraft.network.datasync.DataSerializer
 *  net.minecraft.network.datasync.DataSerializers
 *  net.minecraft.network.datasync.EntityDataManager
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.registry.IThrowableEntity
 */
package mcheli.throwable;

import javax.annotation.Nullable;
import mcheli.MCH_Explosion;
import mcheli.MCH_Lib;
import mcheli.particles.MCH_ParticleParam;
import mcheli.particles.MCH_ParticlesUtil;
import mcheli.throwable.MCH_ThrowableInfo;
import mcheli.throwable.MCH_ThrowableInfoManager;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IThrowableEntity;

public class MCH_EntityThrowable
extends EntityThrowable
implements IThrowableEntity {
    private static final DataParameter<String> INFO_NAME = EntityDataManager.func_187226_a(MCH_EntityThrowable.class, (DataSerializer)DataSerializers.field_187194_d);
    private int countOnUpdate;
    private MCH_ThrowableInfo throwableInfo;
    public double boundPosX;
    public double boundPosY;
    public double boundPosZ;
    public RayTraceResult lastOnImpact;
    public int noInfoCount;

    public MCH_EntityThrowable(World par1World) {
        super(par1World);
        this.init();
    }

    public MCH_EntityThrowable(World par1World, EntityLivingBase par2EntityLivingBase, float acceleration) {
        super(par1World, par2EntityLivingBase);
        this.field_70159_w *= (double)acceleration;
        this.field_70181_x *= (double)acceleration;
        this.field_70179_y *= (double)acceleration;
        this.init();
    }

    public MCH_EntityThrowable(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
        this.init();
    }

    public MCH_EntityThrowable(World worldIn, double x, double y, double z, float yaw, float pitch) {
        this(worldIn);
        this.func_70105_a(0.25f, 0.25f);
        this.func_70012_b(x, y, z, yaw, pitch);
        this.field_70165_t -= (double)(MathHelper.func_76134_b((float)(this.field_70177_z / 180.0f * (float)Math.PI)) * 0.16f);
        this.field_70163_u -= (double)0.1f;
        this.field_70161_v -= (double)(MathHelper.func_76126_a((float)(this.field_70177_z / 180.0f * (float)Math.PI)) * 0.16f);
        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        this.func_184538_a(null, pitch, yaw, 0.0f, 1.5f, 1.0f);
    }

    public void init() {
        this.lastOnImpact = null;
        this.countOnUpdate = 0;
        this.setInfo(null);
        this.noInfoCount = 0;
        this.field_70180_af.func_187214_a(INFO_NAME, (Object)"");
    }

    public void func_184538_a(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = 0.4f;
        this.field_70159_w = -MathHelper.func_76126_a((float)(rotationYawIn / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(rotationPitchIn / 180.0f * (float)Math.PI)) * f;
        this.field_70179_y = MathHelper.func_76134_b((float)(rotationYawIn / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(rotationPitchIn / 180.0f * (float)Math.PI)) * f;
        this.field_70181_x = -MathHelper.func_76126_a((float)((rotationPitchIn + pitchOffset) / 180.0f * (float)Math.PI)) * f;
        this.func_70186_c(this.field_70159_w, this.field_70181_x, this.field_70179_y, velocity, 1.0f);
    }

    public void func_70106_y() {
        String s = this.getInfo() != null ? this.getInfo().name : "null";
        MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityThrowable.setDead(%s)", s);
        super.func_70106_y();
    }

    public void func_70071_h_() {
        this.boundPosX = this.field_70165_t;
        this.boundPosY = this.field_70163_u;
        this.boundPosZ = this.field_70161_v;
        if (this.getInfo() != null) {
            Block block = W_WorldFunc.getBlock(this.field_70170_p, (int)(this.field_70165_t + 0.5), (int)this.field_70163_u, (int)(this.field_70161_v + 0.5));
            Material mat = W_WorldFunc.getBlockMaterial(this.field_70170_p, (int)(this.field_70165_t + 0.5), (int)this.field_70163_u, (int)(this.field_70161_v + 0.5));
            this.field_70181_x = block != null && mat == Material.field_151586_h ? (this.field_70181_x += (double)this.getInfo().gravityInWater) : (this.field_70181_x += (double)this.getInfo().gravity);
        }
        super.func_70071_h_();
        if (this.lastOnImpact != null) {
            this.boundBullet(this.lastOnImpact);
            this.func_70107_b(this.boundPosX + this.field_70159_w, this.boundPosY + this.field_70181_x, this.boundPosZ + this.field_70179_y);
            this.lastOnImpact = null;
        }
        ++this.countOnUpdate;
        if (this.countOnUpdate >= 0x7FFFFFF0) {
            this.func_70106_y();
            return;
        }
        if (this.getInfo() == null) {
            String s = (String)this.field_70180_af.func_187225_a(INFO_NAME);
            if (!s.isEmpty()) {
                this.setInfo(MCH_ThrowableInfoManager.get(s));
            }
            if (this.getInfo() == null) {
                ++this.noInfoCount;
                if (this.noInfoCount > 10) {
                    this.func_70106_y();
                }
                return;
            }
        }
        if (this.field_70128_L) {
            return;
        }
        if (!this.field_70170_p.field_72995_K) {
            if (this.countOnUpdate == this.getInfo().timeFuse && this.getInfo().explosion > 0) {
                MCH_Explosion.newExplosion(this.field_70170_p, null, null, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.getInfo().explosion, this.getInfo().explosion, true, true, false, true, 0);
                this.func_70106_y();
                return;
            }
            if (this.countOnUpdate >= this.getInfo().aliveTime) {
                this.func_70106_y();
            }
        } else if (this.countOnUpdate >= this.getInfo().timeFuse && this.getInfo().explosion <= 0) {
            for (int i = 0; i < this.getInfo().smokeNum; ++i) {
                float r = this.getInfo().smokeColor.r * 0.9f + this.field_70146_Z.nextFloat() * 0.1f;
                float g = this.getInfo().smokeColor.g * 0.9f + this.field_70146_Z.nextFloat() * 0.1f;
                float b = this.getInfo().smokeColor.b * 0.9f + this.field_70146_Z.nextFloat() * 0.1f;
                if (this.getInfo().smokeColor.r == this.getInfo().smokeColor.g) {
                    g = r;
                }
                if (this.getInfo().smokeColor.r == this.getInfo().smokeColor.b) {
                    b = r;
                }
                if (this.getInfo().smokeColor.g == this.getInfo().smokeColor.b) {
                    b = g;
                }
                this.spawnParticle("explode", 4, this.getInfo().smokeSize + this.field_70146_Z.nextFloat() * this.getInfo().smokeSize / 3.0f, r, g, b, this.getInfo().smokeVelocityHorizontal * (this.field_70146_Z.nextFloat() - 0.5f), this.getInfo().smokeVelocityVertical * this.field_70146_Z.nextFloat(), this.getInfo().smokeVelocityHorizontal * (this.field_70146_Z.nextFloat() - 0.5f));
            }
        }
    }

    public void spawnParticle(String name, int num, float size, float r, float g, float b, float mx, float my, float mz) {
        if (this.field_70170_p.field_72995_K) {
            if (name.isEmpty() || num < 1) {
                return;
            }
            double x = (this.field_70165_t - this.field_70169_q) / (double)num;
            double y = (this.field_70163_u - this.field_70167_r) / (double)num;
            double z = (this.field_70161_v - this.field_70166_s) / (double)num;
            for (int i = 0; i < num; ++i) {
                MCH_ParticleParam prm = new MCH_ParticleParam(this.field_70170_p, "smoke", this.field_70169_q + x * (double)i, 1.0 + this.field_70167_r + y * (double)i, this.field_70166_s + z * (double)i);
                prm.setMotion(mx, my, mz);
                prm.size = size;
                prm.setColor(1.0f, r, g, b);
                prm.isEffectWind = true;
                prm.toWhite = true;
                MCH_ParticlesUtil.spawnParticle(prm);
            }
        }
    }

    protected float func_70185_h() {
        return 0.0f;
    }

    public void boundBullet(RayTraceResult m) {
        if (m.field_178784_b == null) {
            return;
        }
        float bound = this.getInfo().bound;
        switch (m.field_178784_b) {
            case DOWN: 
            case UP: {
                this.field_70159_w *= (double)0.9f;
                this.field_70179_y *= (double)0.9f;
                this.boundPosY = m.field_72307_f.field_72448_b;
                if (m.field_178784_b == EnumFacing.DOWN && this.field_70181_x > 0.0 || m.field_178784_b == EnumFacing.UP && this.field_70181_x < 0.0) {
                    this.field_70181_x = -this.field_70181_x * (double)bound;
                    break;
                }
                this.field_70181_x = 0.0;
                break;
            }
            case NORTH: {
                if (!(this.field_70179_y > 0.0)) break;
                this.field_70179_y = -this.field_70179_y * (double)bound;
                break;
            }
            case SOUTH: {
                if (!(this.field_70179_y < 0.0)) break;
                this.field_70179_y = -this.field_70179_y * (double)bound;
                break;
            }
            case WEST: {
                if (!(this.field_70159_w > 0.0)) break;
                this.field_70159_w = -this.field_70159_w * (double)bound;
                break;
            }
            case EAST: {
                if (!(this.field_70159_w < 0.0)) break;
                this.field_70159_w = -this.field_70159_w * (double)bound;
            }
        }
    }

    protected void func_70184_a(RayTraceResult m) {
        if (this.getInfo() != null) {
            this.lastOnImpact = m;
        }
    }

    @Nullable
    public MCH_ThrowableInfo getInfo() {
        return this.throwableInfo;
    }

    public void setInfo(MCH_ThrowableInfo info) {
        this.throwableInfo = info;
        if (info != null && !this.field_70170_p.field_72995_K) {
            this.field_70180_af.func_187227_b(INFO_NAME, (Object)info.name);
        }
    }

    public void setThrower(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            this.field_70192_c = (EntityLivingBase)entity;
        }
    }
}

