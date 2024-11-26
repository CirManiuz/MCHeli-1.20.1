/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.aircraft;

import java.util.List;
import javax.annotation.Nullable;
import mcheli.MCH_Lib;
import mcheli.__helper.entity.IEntitySinglePassenger;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_SeatRackInfo;
import mcheli.mob.MCH_ItemSpawnGunner;
import mcheli.tool.MCH_ItemWrench;
import mcheli.wrapper.W_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntitySeat
extends W_Entity
implements IEntitySinglePassenger {
    public String parentUniqueID;
    private MCH_EntityAircraft parent;
    public int seatID;
    public int parentSearchCount;
    protected Entity lastRiddenByEntity;
    public static final float BB_SIZE = 1.0f;

    public MCH_EntitySeat(World world) {
        super(world);
        this.func_70105_a(1.0f, 1.0f);
        this.field_70159_w = 0.0;
        this.field_70181_x = 0.0;
        this.field_70179_y = 0.0;
        this.seatID = -1;
        this.setParent(null);
        this.parentSearchCount = 0;
        this.lastRiddenByEntity = null;
        this.field_70158_ak = true;
        this.field_70178_ae = true;
    }

    public MCH_EntitySeat(World world, double x, double y, double z) {
        this(world);
        this.func_70107_b(x, y + 1.0, z);
        this.field_70169_q = x;
        this.field_70167_r = y + 1.0;
        this.field_70166_s = z;
    }

    protected boolean func_70041_e_() {
        return false;
    }

    public AxisAlignedBB func_70114_g(Entity par1Entity) {
        return par1Entity.func_174813_aQ();
    }

    public AxisAlignedBB func_70046_E() {
        return this.func_174813_aQ();
    }

    public boolean func_70104_M() {
        return false;
    }

    public double func_70042_X() {
        return -0.3;
    }

    @Override
    public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
        if (this.getParent() != null) {
            return this.getParent().func_70097_a(par1DamageSource, par2);
        }
        return false;
    }

    public boolean func_70067_L() {
        return !this.field_70128_L;
    }

    @SideOnly(value=Side.CLIENT)
    public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    }

    public void func_70106_y() {
        super.func_70106_y();
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        this.field_70143_R = 0.0f;
        Entity riddenByEntity = this.getRiddenByEntity();
        if (riddenByEntity != null) {
            riddenByEntity.field_70143_R = 0.0f;
        }
        if (this.lastRiddenByEntity == null && riddenByEntity != null) {
            if (this.getParent() != null) {
                MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntitySeat.onUpdate:SeatID=%d", this.seatID, riddenByEntity.toString());
                this.getParent().onMountPlayerSeat(this, riddenByEntity);
            }
        } else if (this.lastRiddenByEntity != null && riddenByEntity == null && this.getParent() != null) {
            MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntitySeat.onUpdate:SeatID=%d", this.seatID, this.lastRiddenByEntity.toString());
            this.getParent().onUnmountPlayerSeat(this, this.lastRiddenByEntity);
        }
        if (this.field_70170_p.field_72995_K) {
            this.onUpdate_Client();
        } else {
            this.onUpdate_Server();
        }
        this.lastRiddenByEntity = this.getRiddenByEntity();
    }

    private void onUpdate_Client() {
        this.checkDetachmentAndDelete();
    }

    private void onUpdate_Server() {
        this.checkDetachmentAndDelete();
        Entity riddenByEntity = this.getRiddenByEntity();
        if (riddenByEntity == null || riddenByEntity.field_70128_L) {
            // empty if block
        }
    }

    public void func_184232_k(Entity passenger) {
        this.updatePosition(passenger);
    }

    public void updatePosition(@Nullable Entity ridEnt) {
        if (ridEnt != null) {
            ridEnt.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            ridEnt.field_70179_y = 0.0;
            ridEnt.field_70181_x = 0.0;
            ridEnt.field_70159_w = 0.0;
        }
    }

    public void updateRotation(@Nullable Entity ridEnt, float yaw, float pitch) {
        if (ridEnt != null) {
            ridEnt.field_70177_z = yaw;
            ridEnt.field_70125_A = pitch;
        }
    }

    protected void checkDetachmentAndDelete() {
        if (!this.field_70128_L && (this.seatID < 0 || this.getParent() == null || this.getParent().field_70128_L)) {
            if (this.getParent() != null && this.getParent().field_70128_L) {
                this.parentSearchCount = 100000000;
            }
            if (this.parentSearchCount >= 1200) {
                Entity riddenByEntity;
                this.func_70106_y();
                if (!this.field_70170_p.field_72995_K && (riddenByEntity = this.getRiddenByEntity()) != null) {
                    riddenByEntity.func_184210_p();
                }
                this.setParent(null);
                MCH_Lib.DbgLog(this.field_70170_p, "[Error]\u5ea7\u5e2d\u30a8\u30f3\u30c6\u30a3\u30c6\u30a3\u306f\u672c\u4f53\u304c\u898b\u3064\u304b\u3089\u306a\u3044\u305f\u3081\u524a\u9664 seat=%d, parentUniqueID=%s", this.seatID, this.parentUniqueID);
            } else {
                ++this.parentSearchCount;
            }
        } else {
            this.parentSearchCount = 0;
        }
    }

    protected void func_70014_b(NBTTagCompound par1NBTTagCompound) {
        par1NBTTagCompound.func_74768_a("SeatID", this.seatID);
        par1NBTTagCompound.func_74778_a("ParentUniqueID", this.parentUniqueID);
    }

    protected void func_70037_a(NBTTagCompound par1NBTTagCompound) {
        this.seatID = par1NBTTagCompound.func_74762_e("SeatID");
        this.parentUniqueID = par1NBTTagCompound.func_74779_i("ParentUniqueID");
    }

    @SideOnly(value=Side.CLIENT)
    public float getShadowSize() {
        return 0.0f;
    }

    public boolean canRideMob(Entity entity) {
        if (this.getParent() == null || this.seatID < 0) {
            return false;
        }
        return !(this.getParent().getSeatInfo(this.seatID + 1) instanceof MCH_SeatRackInfo);
    }

    public boolean isGunnerMode() {
        Entity riddenByEntity = this.getRiddenByEntity();
        if (riddenByEntity != null && this.getParent() != null) {
            return this.getParent().getIsGunnerMode(riddenByEntity);
        }
        return false;
    }

    public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
        if (this.getParent() == null || this.getParent().isDestroyed()) {
            return false;
        }
        if (!this.getParent().checkTeam(player)) {
            return false;
        }
        ItemStack itemStack = player.func_184586_b(hand);
        if (!itemStack.func_190926_b() && itemStack.func_77973_b() instanceof MCH_ItemWrench) {
            return this.getParent().func_184230_a(player, hand);
        }
        if (!itemStack.func_190926_b() && itemStack.func_77973_b() instanceof MCH_ItemSpawnGunner) {
            return this.getParent().func_184230_a(player, hand);
        }
        Entity riddenByEntity = this.getRiddenByEntity();
        if (riddenByEntity != null) {
            return false;
        }
        if (player.func_184187_bx() != null) {
            return false;
        }
        if (!this.canRideMob((Entity)player)) {
            return false;
        }
        player.func_184220_m((Entity)this);
        return true;
    }

    @Nullable
    public MCH_EntityAircraft getParent() {
        return this.parent;
    }

    public void setParent(MCH_EntityAircraft parent) {
        this.parent = parent;
        Entity riddenByEntity = this.getRiddenByEntity();
        if (riddenByEntity != null) {
            MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntitySeat.setParent:SeatID=%d %s : " + this.getParent(), this.seatID, riddenByEntity.toString());
            if (this.getParent() != null) {
                this.getParent().onMountPlayerSeat(this, riddenByEntity);
            }
        }
    }

    @Override
    @Nullable
    public Entity getRiddenByEntity() {
        List passengers = this.func_184188_bt();
        return passengers.isEmpty() ? null : (Entity)passengers.get(0);
    }
}

