/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.MoverType
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.datasync.DataParameter
 *  net.minecraft.network.datasync.DataSerializer
 *  net.minecraft.network.datasync.DataSerializers
 *  net.minecraft.network.datasync.EntityDataManager
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.parachute;

import java.util.List;
import javax.annotation.Nullable;
import mcheli.MCH_Lib;
import mcheli.__helper.entity.IEntitySinglePassenger;
import mcheli.particles.MCH_ParticleParam;
import mcheli.particles.MCH_ParticlesUtil;
import mcheli.wrapper.W_AxisAlignedBB;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_Lib;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntityParachute
extends W_Entity
implements IEntitySinglePassenger {
    private static final DataParameter<Byte> TYPE = EntityDataManager.func_187226_a(MCH_EntityParachute.class, (DataSerializer)DataSerializers.field_187191_a);
    private double speedMultiplier = 0.07;
    private int paraPosRotInc;
    private double paraX;
    private double paraY;
    private double paraZ;
    private double paraYaw;
    private double paraPitch;
    @SideOnly(value=Side.CLIENT)
    private double velocityX;
    @SideOnly(value=Side.CLIENT)
    private double velocityY;
    @SideOnly(value=Side.CLIENT)
    private double velocityZ;
    public Entity user;
    public int onGroundCount;

    public MCH_EntityParachute(World par1World) {
        super(par1World);
        this.field_70156_m = true;
        this.func_70105_a(1.5f, 0.6f);
        this.user = null;
        this.onGroundCount = 0;
    }

    public MCH_EntityParachute(World par1World, double par2, double par4, double par6) {
        this(par1World);
        this.func_70107_b(par2, par4, par6);
        this.field_70159_w = 0.0;
        this.field_70181_x = 0.0;
        this.field_70179_y = 0.0;
        this.field_70169_q = par2;
        this.field_70167_r = par4;
        this.field_70166_s = par6;
    }

    protected boolean func_70041_e_() {
        return false;
    }

    @Override
    protected void func_70088_a() {
        this.field_70180_af.func_187214_a(TYPE, (Object)0);
    }

    public void setType(int n) {
        this.field_70180_af.func_187227_b(TYPE, (Object)((byte)n));
    }

    public int getType() {
        return ((Byte)this.field_70180_af.func_187225_a(TYPE)).byteValue();
    }

    public AxisAlignedBB func_70114_g(Entity par1Entity) {
        return par1Entity.func_174813_aQ();
    }

    public AxisAlignedBB func_70046_E() {
        return this.func_174813_aQ();
    }

    public boolean func_70104_M() {
        return true;
    }

    public double func_70042_X() {
        return (double)this.field_70131_O * 0.0 - (double)0.3f;
    }

    @Override
    public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
        return false;
    }

    public boolean func_70067_L() {
        return !this.field_70128_L;
    }

    @SideOnly(value=Side.CLIENT)
    public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.paraPosRotInc = posRotationIncrements + 10;
        this.paraX = x;
        this.paraY = y;
        this.paraZ = z;
        this.paraYaw = yaw;
        this.paraPitch = pitch;
        this.field_70159_w = this.velocityX;
        this.field_70181_x = this.velocityY;
        this.field_70179_y = this.velocityZ;
    }

    @SideOnly(value=Side.CLIENT)
    public void func_70016_h(double par1, double par3, double par5) {
        this.velocityX = this.field_70159_w = par1;
        this.velocityY = this.field_70181_x = par3;
        this.velocityZ = this.field_70179_y = par5;
    }

    public void func_70106_y() {
        super.func_70106_y();
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (!this.field_70170_p.field_72995_K && this.field_70173_aa % 10 == 0) {
            MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityParachute.onUpdate %d, %.3f", this.field_70173_aa, this.field_70181_x);
        }
        if (this.isOpenParachute() && this.field_70181_x > -0.3 && this.field_70173_aa > 20) {
            this.field_70143_R = (float)((double)this.field_70143_R * 0.85);
        }
        if (!this.field_70170_p.field_72995_K && this.user != null && this.user.func_184187_bx() == null) {
            this.user.func_184220_m((Entity)this);
            this.field_70177_z = this.field_70126_B = this.user.field_70177_z;
            this.user = null;
        }
        this.field_70169_q = this.field_70165_t;
        this.field_70167_r = this.field_70163_u;
        this.field_70166_s = this.field_70161_v;
        double d1 = this.func_174813_aQ().field_72338_b + (this.func_174813_aQ().field_72337_e - this.func_174813_aQ().field_72338_b) * 0.0 / 5.0 - 0.125;
        double d2 = this.func_174813_aQ().field_72338_b + (this.func_174813_aQ().field_72337_e - this.func_174813_aQ().field_72338_b) * 1.0 / 5.0 - 0.125;
        AxisAlignedBB axisalignedbb = W_AxisAlignedBB.getAABB(this.func_174813_aQ().field_72340_a, d1, this.func_174813_aQ().field_72339_c, this.func_174813_aQ().field_72336_d, d2, this.func_174813_aQ().field_72334_f);
        if (this.field_70170_p.func_72875_a(axisalignedbb, Material.field_151586_h)) {
            this.onWaterSetBoat();
            this.func_70106_y();
        }
        if (this.field_70170_p.field_72995_K) {
            this.onUpdateClient();
        } else {
            this.onUpdateServer();
        }
    }

    public void onUpdateClient() {
        if (this.paraPosRotInc > 0) {
            double x = this.field_70165_t + (this.paraX - this.field_70165_t) / (double)this.paraPosRotInc;
            double y = this.field_70163_u + (this.paraY - this.field_70163_u) / (double)this.paraPosRotInc;
            double z = this.field_70161_v + (this.paraZ - this.field_70161_v) / (double)this.paraPosRotInc;
            double yaw = MathHelper.func_76138_g((double)(this.paraYaw - (double)this.field_70177_z));
            this.field_70177_z = (float)((double)this.field_70177_z + yaw / (double)this.paraPosRotInc);
            this.field_70125_A = (float)((double)this.field_70125_A + (this.paraPitch - (double)this.field_70125_A) / (double)this.paraPosRotInc);
            --this.paraPosRotInc;
            this.func_70107_b(x, y, z);
            this.func_70101_b(this.field_70177_z, this.field_70125_A);
            if (this.getRiddenByEntity() != null) {
                this.func_70101_b(this.getRiddenByEntity().field_70126_B, this.field_70125_A);
            }
        } else {
            this.func_70107_b(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
            if (this.field_70122_E) {
                // empty if block
            }
            this.field_70159_w *= 0.99;
            this.field_70181_x *= 0.95;
            this.field_70179_y *= 0.99;
        }
        if (!this.isOpenParachute() && this.field_70181_x > 0.01) {
            float color = 0.6f + this.field_70146_Z.nextFloat() * 0.2f;
            double dx = this.field_70169_q - this.field_70165_t;
            double dy = this.field_70167_r - this.field_70163_u;
            double dz = this.field_70166_s - this.field_70161_v;
            int num = 1 + (int)((double)MathHelper.func_76133_a((double)(dx * dx + dy * dy + dz * dz)) * 2.0);
            for (double i = 0.0; i < (double)num; i += 1.0) {
                MCH_ParticleParam prm = new MCH_ParticleParam(this.field_70170_p, "smoke", this.field_70169_q + (this.field_70165_t - this.field_70169_q) * (i / (double)num) * 0.8, this.field_70167_r + (this.field_70163_u - this.field_70167_r) * (i / (double)num) * 0.8, this.field_70166_s + (this.field_70161_v - this.field_70166_s) * (i / (double)num) * 0.8);
                prm.motionX = this.field_70159_w * 0.5 + (this.field_70146_Z.nextDouble() - 0.5) * 0.5;
                prm.motionX = this.field_70181_x * -0.5 + (this.field_70146_Z.nextDouble() - 0.5) * 0.5;
                prm.motionX = this.field_70179_y * 0.5 + (this.field_70146_Z.nextDouble() - 0.5) * 0.5;
                prm.size = 5.0f;
                prm.setColor(0.8f + this.field_70146_Z.nextFloat(), color, color, color);
                MCH_ParticlesUtil.spawnParticle(prm);
            }
        }
    }

    public void onUpdateServer() {
        double yawDiff;
        double gravity;
        double prevSpeed = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        double d = gravity = this.field_70122_E ? 0.01 : 0.03;
        if (this.getType() == 2 && this.field_70173_aa < 20) {
            gravity = 0.01;
        }
        this.field_70181_x -= gravity;
        if (this.isOpenParachute()) {
            double speed;
            if (W_Lib.isEntityLivingBase(this.getRiddenByEntity())) {
                double mv = W_Lib.getEntityMoveDist(this.getRiddenByEntity());
                if (!this.isOpenParachute()) {
                    mv = 0.0;
                }
                if (mv > 0.0) {
                    double mx = -Math.sin(this.getRiddenByEntity().field_70177_z * (float)Math.PI / 180.0f);
                    double mz = Math.cos(this.getRiddenByEntity().field_70177_z * (float)Math.PI / 180.0f);
                    this.field_70159_w += mx * this.speedMultiplier * 0.05;
                    this.field_70179_y += mz * this.speedMultiplier * 0.05;
                }
            }
            if ((speed = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y)) > 0.35) {
                this.field_70159_w *= 0.35 / speed;
                this.field_70179_y *= 0.35 / speed;
                speed = 0.35;
            }
            if (speed > prevSpeed && this.speedMultiplier < 0.35) {
                this.speedMultiplier += (0.35 - this.speedMultiplier) / 35.0;
                if (this.speedMultiplier > 0.35) {
                    this.speedMultiplier = 0.35;
                }
            } else {
                this.speedMultiplier -= (this.speedMultiplier - 0.07) / 35.0;
                if (this.speedMultiplier < 0.07) {
                    this.speedMultiplier = 0.07;
                }
            }
        }
        if (this.field_70122_E) {
            ++this.onGroundCount;
            if (this.onGroundCount > 5) {
                this.onGroundAndDead();
                return;
            }
        }
        this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
        this.field_70181_x = this.getType() == 2 && this.field_70173_aa < 20 ? (this.field_70181_x *= 0.95) : (this.field_70181_x *= 0.9);
        if (this.isOpenParachute()) {
            this.field_70159_w *= 0.95;
            this.field_70179_y *= 0.95;
        } else {
            this.field_70159_w *= 0.99;
            this.field_70179_y *= 0.99;
        }
        this.field_70125_A = 0.0f;
        double yaw = this.field_70177_z;
        double dx = this.field_70169_q - this.field_70165_t;
        double dz = this.field_70166_s - this.field_70161_v;
        if (dx * dx + dz * dz > 0.001) {
            yaw = (float)(Math.atan2(dx, dz) * 180.0 / Math.PI);
        }
        if ((yawDiff = MathHelper.func_76138_g((double)(yaw - (double)this.field_70177_z))) > 20.0) {
            yawDiff = 20.0;
        }
        if (yawDiff < -20.0) {
            yawDiff = -20.0;
        }
        if (this.getRiddenByEntity() != null) {
            this.func_70101_b(this.getRiddenByEntity().field_70177_z, this.field_70125_A);
        } else {
            this.field_70177_z = (float)((double)this.field_70177_z + yawDiff);
            this.func_70101_b(this.field_70177_z, this.field_70125_A);
        }
        List list = this.field_70170_p.func_72839_b((Entity)this, this.func_174813_aQ().func_72314_b(0.2, 0.0, 0.2));
        if (list != null && !list.isEmpty()) {
            for (int l = 0; l < list.size(); ++l) {
                Entity entity = (Entity)list.get(l);
                if (entity == this.getRiddenByEntity() || !entity.func_70104_M() || !(entity instanceof MCH_EntityParachute)) continue;
                entity.func_70108_f((Entity)this);
            }
        }
        if (this.getRiddenByEntity() != null && this.getRiddenByEntity().field_70128_L) {
            this.func_70106_y();
        }
    }

    public void onGroundAndDead() {
        this.field_70163_u += 1.2;
        this.func_184232_k(this.getRiddenByEntity());
        this.func_70106_y();
    }

    public void onWaterSetBoat() {
        if (this.field_70170_p.field_72995_K) {
            return;
        }
        if (this.getType() != 2) {
            return;
        }
        if (this.getRiddenByEntity() == null) {
            return;
        }
        int px = (int)(this.field_70165_t + 0.5);
        int py = (int)(this.field_70163_u + 0.5);
        int pz = (int)(this.field_70161_v + 0.5);
        boolean foundBlock = false;
        for (int y = 0; y < 5 && py + y >= 0 && py + y <= 255; ++y) {
            Block block = W_WorldFunc.getBlock(this.field_70170_p, px, py - y, pz);
            if (block != W_Block.getWater()) continue;
            py -= y;
            foundBlock = true;
            break;
        }
        if (!foundBlock) {
            return;
        }
        int countWater = 0;
        for (int y = 0; y < 3 && py + y >= 0 && py + y <= 255; ++y) {
            for (int x = -2; x <= 2; ++x) {
                Block block;
                for (int z = -2; z <= 2 && ((block = W_WorldFunc.getBlock(this.field_70170_p, px + x, py - y, pz + z)) != W_Block.getWater() || ++countWater <= 37); ++z) {
                }
            }
        }
        if (countWater > 37) {
            EntityBoat entityboat = new EntityBoat(this.field_70170_p, (double)px, (double)((float)py + 1.0f), (double)pz);
            entityboat.field_70177_z = this.field_70177_z - 90.0f;
            this.field_70170_p.func_72838_d((Entity)entityboat);
            this.getRiddenByEntity().func_184220_m((Entity)entityboat);
        }
    }

    public boolean isOpenParachute() {
        return this.getType() != 2 || this.field_70181_x < -0.1;
    }

    public void func_184232_k(Entity passenger) {
        if (this.func_184196_w(passenger)) {
            double x = -Math.sin((double)this.field_70177_z * Math.PI / 180.0) * 0.1;
            double z = Math.cos((double)this.field_70177_z * Math.PI / 180.0) * 0.1;
            passenger.func_70107_b(this.field_70165_t + x, this.field_70163_u + this.func_70042_X() + passenger.func_70033_W(), this.field_70161_v + z);
        }
    }

    protected void func_70014_b(NBTTagCompound nbt) {
        nbt.func_74774_a("ParachuteModelType", (byte)this.getType());
    }

    protected void func_70037_a(NBTTagCompound nbt) {
        this.setType(nbt.func_74771_c("ParachuteModelType"));
    }

    @SideOnly(value=Side.CLIENT)
    public float getShadowSize() {
        return 4.0f;
    }

    public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
        return false;
    }

    @Override
    @Nullable
    public Entity getRiddenByEntity() {
        List passengers = this.func_184188_bt();
        return passengers.isEmpty() ? null : (Entity)passengers.get(0);
    }
}

