/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.MoverType
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.datasync.DataParameter
 *  net.minecraft.network.datasync.DataSerializer
 *  net.minecraft.network.datasync.DataSerializers
 *  net.minecraft.network.datasync.EntityDataManager
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.container;

import java.util.List;
import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.MCH_MOD;
import mcheli.__helper.entity.IEntityItemStackPickable;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.aircraft.MCH_IEntityCanRideAircraft;
import mcheli.aircraft.MCH_SeatRackInfo;
import mcheli.multiplay.MCH_Multiplay;
import mcheli.wrapper.W_AxisAlignedBB;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_Blocks;
import mcheli.wrapper.W_EntityContainer;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntityContainer
extends W_EntityContainer
implements MCH_IEntityCanRideAircraft,
IEntityItemStackPickable {
    public static final float Y_OFFSET = 0.5f;
    private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.func_187226_a(MCH_EntityContainer.class, (DataSerializer)DataSerializers.field_187192_b);
    private static final DataParameter<Integer> FORWARD_DIR = EntityDataManager.func_187226_a(MCH_EntityContainer.class, (DataSerializer)DataSerializers.field_187192_b);
    private static final DataParameter<Integer> DAMAGE_TAKEN = EntityDataManager.func_187226_a(MCH_EntityContainer.class, (DataSerializer)DataSerializers.field_187192_b);
    private double speedMultiplier = 0.07;
    private int boatPosRotationIncrements;
    private double boatX;
    private double boatY;
    private double boatZ;
    private double boatYaw;
    private double boatPitch;
    @SideOnly(value=Side.CLIENT)
    private double velocityX;
    @SideOnly(value=Side.CLIENT)
    private double velocityY;
    @SideOnly(value=Side.CLIENT)
    private double velocityZ;

    public MCH_EntityContainer(World par1World) {
        super(par1World);
        this.field_70156_m = true;
        this.func_70105_a(2.0f, 1.0f);
        this.field_70138_W = 0.6f;
        this.field_70178_ae = true;
        this._renderDistanceWeight = 2.0;
    }

    public MCH_EntityContainer(World par1World, double par2, double par4, double par6) {
        this(par1World);
        this.func_70107_b(par2, par4 + 0.5, par6);
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
        this.field_70180_af.func_187214_a(TIME_SINCE_HIT, (Object)0);
        this.field_70180_af.func_187214_a(FORWARD_DIR, (Object)1);
        this.field_70180_af.func_187214_a(DAMAGE_TAKEN, (Object)0);
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

    @Override
    public int func_70302_i_() {
        return 54;
    }

    @Override
    public String getInvName() {
        return "Container " + super.getInvName();
    }

    public double func_70042_X() {
        return -0.3;
    }

    @Override
    public boolean func_70097_a(DamageSource ds, float damage) {
        boolean flag;
        if (this.func_180431_b(ds)) {
            return false;
        }
        if (this.field_70170_p.field_72995_K || this.field_70128_L) {
            return false;
        }
        damage = MCH_Config.applyDamageByExternal(this, ds, damage);
        if (!MCH_Multiplay.canAttackEntity(ds, (Entity)this)) {
            return false;
        }
        if (!(ds.func_76346_g() instanceof EntityPlayer) || !ds.func_76355_l().equalsIgnoreCase("player")) {
            return false;
        }
        MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityContainer.attackEntityFrom:damage=%.1f:%s", Float.valueOf(damage), ds.func_76355_l());
        W_WorldFunc.MOD_playSoundAtEntity(this, "hit", 1.0f, 1.3f);
        this.setDamageTaken(this.getDamageTaken() + (int)(damage * 20.0f));
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.func_70018_K();
        boolean bl = flag = ds.func_76346_g() instanceof EntityPlayer && ((EntityPlayer)ds.func_76346_g()).field_71075_bZ.field_75098_d;
        if (flag || (float)this.getDamageTaken() > 40.0f) {
            if (!flag) {
                this.func_145778_a(MCH_MOD.itemContainer, 1, 0.0f);
            }
            this.func_70106_y();
        }
        return true;
    }

    @SideOnly(value=Side.CLIENT)
    public void func_70057_ab() {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11);
    }

    public boolean func_70067_L() {
        return !this.field_70128_L;
    }

    @SideOnly(value=Side.CLIENT)
    public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.boatPosRotationIncrements = posRotationIncrements + 10;
        this.boatX = x;
        this.boatY = y;
        this.boatZ = z;
        this.boatYaw = yaw;
        this.boatPitch = pitch;
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

    public void func_70071_h_() {
        double d5;
        double d4;
        super.func_70071_h_();
        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }
        if ((float)this.getDamageTaken() > 0.0f) {
            this.setDamageTaken(this.getDamageTaken() - 1);
        }
        this.field_70169_q = this.field_70165_t;
        this.field_70167_r = this.field_70163_u;
        this.field_70166_s = this.field_70161_v;
        int b0 = 5;
        double d0 = 0.0;
        for (int i = 0; i < b0; ++i) {
            AxisAlignedBB boundingBox = this.func_174813_aQ();
            double d1 = boundingBox.field_72338_b + (boundingBox.field_72337_e - boundingBox.field_72338_b) * (double)(i + 0) / (double)b0 - 0.125;
            double d2 = boundingBox.field_72338_b + (boundingBox.field_72337_e - boundingBox.field_72338_b) * (double)(i + 1) / (double)b0 - 0.125;
            AxisAlignedBB axisalignedbb = W_AxisAlignedBB.getAABB(boundingBox.field_72340_a, d1, boundingBox.field_72339_c, boundingBox.field_72336_d, d2, boundingBox.field_72334_f);
            if (this.field_70170_p.func_72875_a(axisalignedbb, Material.field_151586_h)) {
                d0 += 1.0 / (double)b0;
                continue;
            }
            if (!this.field_70170_p.func_72875_a(axisalignedbb, Material.field_151587_i)) continue;
            d0 += 1.0 / (double)b0;
        }
        double d3 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        if (d3 > 0.2625) {
            // empty if block
        }
        if (this.field_70170_p.field_72995_K) {
            if (this.boatPosRotationIncrements > 0) {
                d4 = this.field_70165_t + (this.boatX - this.field_70165_t) / (double)this.boatPosRotationIncrements;
                d5 = this.field_70163_u + (this.boatY - this.field_70163_u) / (double)this.boatPosRotationIncrements;
                double d11 = this.field_70161_v + (this.boatZ - this.field_70161_v) / (double)this.boatPosRotationIncrements;
                double d10 = MathHelper.func_76138_g((double)(this.boatYaw - (double)this.field_70177_z));
                this.field_70177_z = (float)((double)this.field_70177_z + d10 / (double)this.boatPosRotationIncrements);
                this.field_70125_A = (float)((double)this.field_70125_A + (this.boatPitch - (double)this.field_70125_A) / (double)this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.func_70107_b(d4, d5, d11);
                this.func_70101_b(this.field_70177_z, this.field_70125_A);
            } else {
                d4 = this.field_70165_t + this.field_70159_w;
                d5 = this.field_70163_u + this.field_70181_x;
                double d11 = this.field_70161_v + this.field_70179_y;
                this.func_70107_b(d4, d5, d11);
                if (this.field_70122_E) {
                    this.field_70159_w *= (double)0.9f;
                    this.field_70179_y *= (double)0.9f;
                }
                this.field_70159_w *= 0.99;
                this.field_70181_x *= 0.95;
                this.field_70179_y *= 0.99;
            }
        } else {
            double d12;
            if (d0 < 1.0) {
                d4 = d0 * 2.0 - 1.0;
                this.field_70181_x += 0.04 * d4;
            } else {
                if (this.field_70181_x < 0.0) {
                    this.field_70181_x /= 2.0;
                }
                this.field_70181_x += 0.007;
            }
            d4 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
            if (d4 > 0.35) {
                d5 = 0.35 / d4;
                this.field_70159_w *= d5;
                this.field_70179_y *= d5;
                d4 = 0.35;
            }
            if (d4 > d3 && this.speedMultiplier < 0.35) {
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
            if (this.field_70122_E) {
                this.field_70159_w *= (double)0.9f;
                this.field_70179_y *= (double)0.9f;
            }
            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= 0.99;
            this.field_70181_x *= 0.95;
            this.field_70179_y *= 0.99;
            this.field_70125_A = 0.0f;
            d5 = this.field_70177_z;
            double d11 = this.field_70169_q - this.field_70165_t;
            double d10 = this.field_70166_s - this.field_70161_v;
            if (d11 * d11 + d10 * d10 > 0.001) {
                d5 = (float)(Math.atan2(d10, d11) * 180.0 / Math.PI);
            }
            if ((d12 = MathHelper.func_76138_g((double)(d5 - (double)this.field_70177_z))) > 5.0) {
                d12 = 5.0;
            }
            if (d12 < -5.0) {
                d12 = -5.0;
            }
            this.field_70177_z = (float)((double)this.field_70177_z + d12);
            this.func_70101_b(this.field_70177_z, this.field_70125_A);
            if (!this.field_70170_p.field_72995_K) {
                int l;
                List list = this.field_70170_p.func_72839_b((Entity)this, this.func_174813_aQ().func_72314_b(0.2, 0.0, 0.2));
                if (list != null && !list.isEmpty()) {
                    for (l = 0; l < list.size(); ++l) {
                        Entity entity = (Entity)list.get(l);
                        if (!entity.func_70104_M() || !(entity instanceof MCH_EntityContainer)) continue;
                        entity.func_70108_f((Entity)this);
                    }
                }
                if (MCH_Config.Collision_DestroyBlock.prmBool) {
                    for (l = 0; l < 4; ++l) {
                        int i1 = MathHelper.func_76128_c((double)(this.field_70165_t + ((double)(l % 2) - 0.5) * 0.8));
                        int j1 = MathHelper.func_76128_c((double)(this.field_70161_v + ((double)(l / 2) - 0.5) * 0.8));
                        for (int k1 = 0; k1 < 2; ++k1) {
                            int l1 = MathHelper.func_76128_c((double)this.field_70163_u) + k1;
                            if (W_WorldFunc.isEqualBlock(this.field_70170_p, i1, l1, j1, W_Block.getSnowLayer())) {
                                this.field_70170_p.func_175698_g(new BlockPos(i1, l1, j1));
                                continue;
                            }
                            if (!W_WorldFunc.isEqualBlock(this.field_70170_p, i1, l1, j1, W_Blocks.field_150392_bi)) continue;
                            W_WorldFunc.destroyBlock(this.field_70170_p, i1, l1, j1, true);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void func_70014_b(NBTTagCompound par1NBTTagCompound) {
        super.func_70014_b(par1NBTTagCompound);
    }

    @Override
    protected void func_70037_a(NBTTagCompound par1NBTTagCompound) {
        super.func_70037_a(par1NBTTagCompound);
    }

    @SideOnly(value=Side.CLIENT)
    public float getShadowSize() {
        return 2.0f;
    }

    public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
        if (player != null) {
            this.displayInventory(player);
        }
        return true;
    }

    public void setDamageTaken(int par1) {
        this.field_70180_af.func_187227_b(DAMAGE_TAKEN, (Object)par1);
    }

    public int getDamageTaken() {
        return (Integer)this.field_70180_af.func_187225_a(DAMAGE_TAKEN);
    }

    public void setTimeSinceHit(int par1) {
        this.field_70180_af.func_187227_b(TIME_SINCE_HIT, (Object)par1);
    }

    public int getTimeSinceHit() {
        return (Integer)this.field_70180_af.func_187225_a(TIME_SINCE_HIT);
    }

    public void setForwardDirection(int par1) {
        this.field_70180_af.func_187227_b(FORWARD_DIR, (Object)par1);
    }

    public int getForwardDirection() {
        return (Integer)this.field_70180_af.func_187225_a(FORWARD_DIR);
    }

    @Override
    public boolean canRideAircraft(MCH_EntityAircraft ac, int seatID, MCH_SeatRackInfo info) {
        for (String s : info.names) {
            if (!s.equalsIgnoreCase("container")) continue;
            return ac.func_184187_bx() == null && this.func_184187_bx() == null;
        }
        return false;
    }

    @Override
    public boolean isSkipNormalRender() {
        return this.func_184187_bx() instanceof MCH_EntitySeat;
    }

    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack((Item)MCH_MOD.itemContainer);
    }
}

