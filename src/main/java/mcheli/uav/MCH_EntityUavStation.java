/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.state.IBlockState
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
package mcheli.uav;

import java.util.List;
import javax.annotation.Nullable;
import mcheli.MCH_Config;
import mcheli.MCH_Explosion;
import mcheli.MCH_Lib;
import mcheli.MCH_MOD;
import mcheli.__helper.entity.IEntityItemStackPickable;
import mcheli.__helper.entity.IEntitySinglePassenger;
import mcheli.__helper.network.PooledGuiParameter;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.helicopter.MCH_HeliInfoManager;
import mcheli.helicopter.MCH_ItemHeli;
import mcheli.multiplay.MCH_Multiplay;
import mcheli.plane.MCP_ItemPlane;
import mcheli.plane.MCP_PlaneInfo;
import mcheli.plane.MCP_PlaneInfoManager;
import mcheli.tank.MCH_ItemTank;
import mcheli.tank.MCH_TankInfo;
import mcheli.tank.MCH_TankInfoManager;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_EntityContainer;
import mcheli.wrapper.W_EntityPlayer;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.state.IBlockState;
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

public class MCH_EntityUavStation
extends W_EntityContainer
implements IEntitySinglePassenger,
IEntityItemStackPickable {
    public static final float Y_OFFSET = 0.35f;
    private static final DataParameter<Byte> STATUS = EntityDataManager.func_187226_a(MCH_EntityUavStation.class, (DataSerializer)DataSerializers.field_187191_a);
    private static final DataParameter<Integer> LAST_AC_ID = EntityDataManager.func_187226_a(MCH_EntityUavStation.class, (DataSerializer)DataSerializers.field_187192_b);
    private static final DataParameter<BlockPos> UAV_POS = EntityDataManager.func_187226_a(MCH_EntityUavStation.class, (DataSerializer)DataSerializers.field_187200_j);
    protected Entity lastRiddenByEntity;
    public boolean isRequestedSyncStatus;
    @SideOnly(value=Side.CLIENT)
    protected double velocityX;
    @SideOnly(value=Side.CLIENT)
    protected double velocityY;
    @SideOnly(value=Side.CLIENT)
    protected double velocityZ;
    protected int aircraftPosRotInc;
    protected double aircraftX;
    protected double aircraftY;
    protected double aircraftZ;
    protected double aircraftYaw;
    protected double aircraftPitch;
    private MCH_EntityAircraft controlAircraft;
    private MCH_EntityAircraft lastControlAircraft;
    private String loadedLastControlAircraftGuid;
    public int posUavX;
    public int posUavY;
    public int posUavZ;
    public float rotCover;
    public float prevRotCover;

    public MCH_EntityUavStation(World world) {
        super(world);
        this.dropContentsWhenDead = false;
        this.field_70156_m = true;
        this.func_70105_a(2.0f, 0.7f);
        this.field_70159_w = 0.0;
        this.field_70181_x = 0.0;
        this.field_70179_y = 0.0;
        this.field_70158_ak = true;
        this.lastRiddenByEntity = null;
        this.aircraftPosRotInc = 0;
        this.aircraftX = 0.0;
        this.aircraftY = 0.0;
        this.aircraftZ = 0.0;
        this.aircraftYaw = 0.0;
        this.aircraftPitch = 0.0;
        this.posUavX = 0;
        this.posUavY = 0;
        this.posUavZ = 0;
        this.rotCover = 0.0f;
        this.prevRotCover = 0.0f;
        this.setControlAircract(null);
        this.setLastControlAircraft(null);
        this.loadedLastControlAircraftGuid = "";
    }

    @Override
    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(STATUS, (Object)0);
        this.field_70180_af.func_187214_a(LAST_AC_ID, (Object)0);
        this.field_70180_af.func_187214_a(UAV_POS, (Object)BlockPos.field_177992_a);
        this.setOpen(true);
    }

    public int getStatus() {
        return ((Byte)this.field_70180_af.func_187225_a(STATUS)).byteValue();
    }

    public void setStatus(int n) {
        if (!this.field_70170_p.field_72995_K) {
            MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityUavStation.setStatus(%d)", n);
            this.field_70180_af.func_187227_b(STATUS, (Object)((byte)n));
        }
    }

    public int getKind() {
        return 0x7F & this.getStatus();
    }

    public void setKind(int n) {
        this.setStatus(this.getStatus() & 0x80 | n);
    }

    public boolean isOpen() {
        return (this.getStatus() & 0x80) != 0;
    }

    public void setOpen(boolean b) {
        this.setStatus((b ? 128 : 0) | this.getStatus() & 0x7F);
    }

    @Nullable
    public MCH_EntityAircraft getControlAircract() {
        return this.controlAircraft;
    }

    public void setControlAircract(@Nullable MCH_EntityAircraft ac) {
        this.controlAircraft = ac;
        if (ac != null && !ac.field_70128_L) {
            this.setLastControlAircraft(ac);
        }
    }

    public void setUavPosition(int x, int y, int z) {
        if (!this.field_70170_p.field_72995_K) {
            this.posUavX = x;
            this.posUavY = y;
            this.posUavZ = z;
            this.field_70180_af.func_187227_b(UAV_POS, (Object)new BlockPos(x, y, z));
        }
    }

    public void updateUavPosition() {
        BlockPos uavPos = (BlockPos)this.field_70180_af.func_187225_a(UAV_POS);
        this.posUavX = uavPos.func_177958_n();
        this.posUavY = uavPos.func_177956_o();
        this.posUavZ = uavPos.func_177952_p();
    }

    @Override
    protected void func_70014_b(NBTTagCompound nbt) {
        super.func_70014_b(nbt);
        nbt.func_74768_a("UavStatus", this.getStatus());
        nbt.func_74768_a("PosUavX", this.posUavX);
        nbt.func_74768_a("PosUavY", this.posUavY);
        nbt.func_74768_a("PosUavZ", this.posUavZ);
        String s = "";
        if (this.getLastControlAircraft() != null && !this.getLastControlAircraft().field_70128_L) {
            s = this.getLastControlAircraft().getCommonUniqueId();
        }
        if (s.isEmpty()) {
            s = this.loadedLastControlAircraftGuid;
        }
        nbt.func_74778_a("LastCtrlAc", s);
    }

    @Override
    protected void func_70037_a(NBTTagCompound nbt) {
        super.func_70037_a(nbt);
        this.setUavPosition(nbt.func_74762_e("PosUavX"), nbt.func_74762_e("PosUavY"), nbt.func_74762_e("PosUavZ"));
        if (nbt.func_74764_b("UavStatus")) {
            this.setStatus(nbt.func_74762_e("UavStatus"));
        } else {
            this.setKind(1);
        }
        this.loadedLastControlAircraftGuid = nbt.func_74779_i("LastCtrlAc");
    }

    public void initUavPostion() {
        int rt = (int)(MCH_Lib.getRotate360(this.field_70177_z + 45.0f) / 90.0);
        this.posUavX = rt == 0 || rt == 3 ? 12 : -12;
        this.posUavZ = rt == 0 || rt == 1 ? 12 : -12;
        this.posUavY = 2;
        this.setUavPosition(this.posUavX, this.posUavY, this.posUavZ);
    }

    @Override
    public void func_70106_y() {
        super.func_70106_y();
    }

    @Override
    public boolean func_70097_a(DamageSource damageSource, float damage) {
        if (this.func_180431_b(damageSource)) {
            return false;
        }
        if (this.field_70128_L) {
            return true;
        }
        if (this.field_70170_p.field_72995_K) {
            return true;
        }
        String dmt = damageSource.func_76355_l();
        damage = MCH_Config.applyDamageByExternal(this, damageSource, damage);
        if (!MCH_Multiplay.canAttackEntity(damageSource, (Entity)this)) {
            return false;
        }
        boolean isCreative = false;
        Entity entity = damageSource.func_76346_g();
        boolean isDamegeSourcePlayer = false;
        if (entity instanceof EntityPlayer) {
            isCreative = ((EntityPlayer)entity).field_71075_bZ.field_75098_d;
            if (dmt.compareTo("player") == 0) {
                isDamegeSourcePlayer = true;
            }
            W_WorldFunc.MOD_playSoundAtEntity(this, "hit", 1.0f, 1.0f);
        } else {
            W_WorldFunc.MOD_playSoundAtEntity(this, "helidmg", 1.0f, 0.9f + this.field_70146_Z.nextFloat() * 0.1f);
        }
        this.func_70018_K();
        if (damage > 0.0f) {
            int kind;
            Entity riddenByEntity = this.getRiddenByEntity();
            if (riddenByEntity != null) {
                riddenByEntity.func_184220_m((Entity)this);
            }
            this.dropContentsWhenDead = true;
            this.func_70106_y();
            if (!isDamegeSourcePlayer) {
                MCH_Explosion.newExplosion(this.field_70170_p, null, riddenByEntity, this.field_70165_t, this.field_70163_u, this.field_70161_v, 1.0f, 0.0f, true, true, false, false, 0);
            }
            if (!isCreative && (kind = this.getKind()) > 0) {
                this.func_145778_a(MCH_MOD.itemUavStation[kind - 1], 1, 0.0f);
            }
        }
        return true;
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
        Entity riddenByEntity = this.getRiddenByEntity();
        if (this.getKind() == 2 && riddenByEntity != null) {
            double pz;
            int z;
            int y;
            double px = -Math.sin((double)this.field_70177_z * Math.PI / 180.0) * 0.9;
            int x = (int)(this.field_70165_t + px);
            BlockPos blockpos = new BlockPos(x, y = (int)(this.field_70163_u - 0.5), z = (int)(this.field_70161_v + (pz = Math.cos((double)this.field_70177_z * Math.PI / 180.0) * 0.9)));
            IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos);
            return iblockstate.func_185914_p() ? -0.4 : -0.9;
        }
        return 0.35;
    }

    @SideOnly(value=Side.CLIENT)
    public float getShadowSize() {
        return 2.0f;
    }

    public boolean func_70067_L() {
        return !this.field_70128_L;
    }

    public void func_70108_f(Entity par1Entity) {
    }

    public void func_70024_g(double par1, double par3, double par5) {
    }

    @SideOnly(value=Side.CLIENT)
    public void func_70016_h(double par1, double par3, double par5) {
        this.velocityX = this.field_70159_w = par1;
        this.velocityY = this.field_70181_x = par3;
        this.velocityZ = this.field_70179_y = par5;
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        this.prevRotCover = this.rotCover;
        this.rotCover = this.isOpen() ? (this.rotCover < 1.0f ? (this.rotCover += 0.1f) : 1.0f) : (this.rotCover > 0.0f ? (this.rotCover -= 0.1f) : 0.0f);
        Entity riddenByEntity = this.getRiddenByEntity();
        if (riddenByEntity == null) {
            if (this.lastRiddenByEntity != null) {
                this.unmountEntity(true);
            }
            this.setControlAircract(null);
        }
        int uavStationKind = this.getKind();
        if (this.field_70173_aa >= 30 || uavStationKind <= 0 || uavStationKind == 1 || uavStationKind != 2 || this.field_70170_p.field_72995_K && !this.isRequestedSyncStatus) {
            this.isRequestedSyncStatus = true;
        }
        this.field_70169_q = this.field_70165_t;
        this.field_70167_r = this.field_70163_u;
        this.field_70166_s = this.field_70161_v;
        if (this.getControlAircract() != null && this.getControlAircract().field_70128_L) {
            this.setControlAircract(null);
        }
        if (this.getLastControlAircraft() != null && this.getLastControlAircraft().field_70128_L) {
            this.setLastControlAircraft(null);
        }
        if (this.field_70170_p.field_72995_K) {
            this.onUpdate_Client();
        } else {
            this.onUpdate_Server();
        }
        this.lastRiddenByEntity = this.getRiddenByEntity();
    }

    @Nullable
    public MCH_EntityAircraft getLastControlAircraft() {
        return this.lastControlAircraft;
    }

    public MCH_EntityAircraft getAndSearchLastControlAircraft() {
        MCH_EntityAircraft ac;
        Entity entity;
        int id;
        if (this.getLastControlAircraft() == null && (id = this.getLastControlAircraftEntityId().intValue()) > 0 && (entity = this.field_70170_p.func_73045_a(id)) instanceof MCH_EntityAircraft && (ac = (MCH_EntityAircraft)entity).isUAV()) {
            this.setLastControlAircraft(ac);
        }
        return this.getLastControlAircraft();
    }

    public void setLastControlAircraft(MCH_EntityAircraft ac) {
        MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityUavStation.setLastControlAircraft:" + ac, new Object[0]);
        this.lastControlAircraft = ac;
    }

    public Integer getLastControlAircraftEntityId() {
        return (Integer)this.field_70180_af.func_187225_a(LAST_AC_ID);
    }

    public void setLastControlAircraftEntityId(int s) {
        if (!this.field_70170_p.field_72995_K) {
            this.field_70180_af.func_187227_b(LAST_AC_ID, (Object)s);
        }
    }

    public void searchLastControlAircraft() {
        if (this.loadedLastControlAircraftGuid.isEmpty()) {
            return;
        }
        List list = this.field_70170_p.func_72872_a(MCH_EntityAircraft.class, this.func_70046_E().func_72314_b(120.0, 120.0, 120.0));
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); ++i) {
            MCH_EntityAircraft ac = (MCH_EntityAircraft)list.get(i);
            if (!ac.getCommonUniqueId().equals(this.loadedLastControlAircraftGuid)) continue;
            String n = "no info : " + ac;
            MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityUavStation.searchLastControlAircraft:found" + n, new Object[0]);
            this.setLastControlAircraft(ac);
            this.setLastControlAircraftEntityId(W_Entity.getEntityId(ac));
            this.loadedLastControlAircraftGuid = "";
            return;
        }
    }

    protected void onUpdate_Client() {
        if (this.aircraftPosRotInc > 0) {
            double rpinc = this.aircraftPosRotInc;
            double yaw = MathHelper.func_76138_g((double)(this.aircraftYaw - (double)this.field_70177_z));
            this.field_70177_z = (float)((double)this.field_70177_z + yaw / rpinc);
            this.field_70125_A = (float)((double)this.field_70125_A + (this.aircraftPitch - (double)this.field_70125_A) / rpinc);
            this.func_70107_b(this.field_70165_t + (this.aircraftX - this.field_70165_t) / rpinc, this.field_70163_u + (this.aircraftY - this.field_70163_u) / rpinc, this.field_70161_v + (this.aircraftZ - this.field_70161_v) / rpinc);
            this.func_70101_b(this.field_70177_z, this.field_70125_A);
            --this.aircraftPosRotInc;
        } else {
            this.func_70107_b(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
            this.field_70181_x *= 0.96;
            this.field_70159_w = 0.0;
            this.field_70179_y = 0.0;
        }
        this.updateUavPosition();
    }

    private void onUpdate_Server() {
        this.field_70181_x -= 0.03;
        this.func_70091_d(MoverType.SELF, 0.0, this.field_70181_x, 0.0);
        this.field_70181_x *= 0.96;
        this.field_70159_w = 0.0;
        this.field_70179_y = 0.0;
        this.func_70101_b(this.field_70177_z, this.field_70125_A);
        Entity riddenByEntity = this.getRiddenByEntity();
        if (riddenByEntity != null) {
            if (riddenByEntity.field_70128_L) {
                this.unmountEntity(true);
            } else {
                ItemStack item = this.func_70301_a(0);
                if (!item.func_190926_b()) {
                    this.handleItem(riddenByEntity, item);
                    if (item.func_190916_E() == 0) {
                        this.func_70299_a(0, ItemStack.field_190927_a);
                    }
                }
            }
        }
        if (this.getLastControlAircraft() == null && this.field_70173_aa % 40 == 0) {
            this.searchLastControlAircraft();
        }
    }

    public void func_180426_a(double par1, double par3, double par5, float par7, float par8, int par9, boolean teleport) {
        this.aircraftPosRotInc = par9 + 8;
        this.aircraftX = par1;
        this.aircraftY = par3;
        this.aircraftZ = par5;
        this.aircraftYaw = par7;
        this.aircraftPitch = par8;
        this.field_70159_w = this.velocityX;
        this.field_70181_x = this.velocityY;
        this.field_70179_y = this.velocityZ;
    }

    public void func_184232_k(Entity passenger) {
        if (this.func_184196_w(passenger)) {
            double x = -Math.sin((double)this.field_70177_z * Math.PI / 180.0) * 0.9;
            double z = Math.cos((double)this.field_70177_z * Math.PI / 180.0) * 0.9;
            passenger.func_70107_b(this.field_70165_t + x, this.field_70163_u + this.func_70042_X() + passenger.func_70033_W() + (double)0.35f, this.field_70161_v + z);
        }
    }

    public void controlLastAircraft(Entity user) {
        if (this.getLastControlAircraft() != null && !this.getLastControlAircraft().field_70128_L) {
            this.getLastControlAircraft().setUavStation(this);
            this.setControlAircract(this.getLastControlAircraft());
            W_EntityPlayer.closeScreen(user);
        }
    }

    public void handleItem(@Nullable Entity user, ItemStack itemStack) {
        MCH_AircraftInfo hi;
        MCP_PlaneInfo pi;
        Item item;
        if (user == null || user.field_70128_L || itemStack.func_190926_b() || itemStack.func_190916_E() != 1) {
            return;
        }
        if (this.field_70170_p.field_72995_K) {
            return;
        }
        MCH_EntityAircraft ac = null;
        double x = this.field_70165_t + (double)this.posUavX;
        double y = this.field_70163_u + (double)this.posUavY;
        double z = this.field_70161_v + (double)this.posUavZ;
        if (y <= 1.0) {
            y = 2.0;
        }
        if ((item = itemStack.func_77973_b()) instanceof MCP_ItemPlane && (pi = MCP_PlaneInfoManager.getFromItem(item)) != null && pi.isUAV) {
            ac = !pi.isSmallUAV && this.getKind() == 2 ? null : ((MCP_ItemPlane)item).createAircraft(this.field_70170_p, x, y, z, itemStack);
        }
        if (item instanceof MCH_ItemHeli && (hi = MCH_HeliInfoManager.getFromItem(item)) != null && hi.isUAV) {
            ac = !hi.isSmallUAV && this.getKind() == 2 ? null : ((MCH_ItemHeli)item).createAircraft(this.field_70170_p, x, y, z, itemStack);
        }
        if (item instanceof MCH_ItemTank && (hi = MCH_TankInfoManager.getFromItem(item)) != null && ((MCH_TankInfo)hi).isUAV) {
            ac = !((MCH_TankInfo)hi).isSmallUAV && this.getKind() == 2 ? null : ((MCH_ItemTank)item).createAircraft(this.field_70170_p, x, y, z, itemStack);
        }
        if (ac == null) {
            return;
        }
        ac.field_70126_B = ac.field_70177_z = this.field_70177_z - 180.0f;
        user.field_70177_z = this.field_70177_z - 180.0f;
        if (this.field_70170_p.func_184144_a((Entity)ac, ac.func_174813_aQ().func_72314_b(-0.1, -0.1, -0.1)).isEmpty()) {
            itemStack.func_190918_g(1);
            MCH_Lib.DbgLog(this.field_70170_p, "Create UAV: %s : %s", item.func_77658_a(), item);
            user.field_70177_z = this.field_70177_z - 180.0f;
            if (!ac.isTargetDrone()) {
                ac.setUavStation(this);
                this.setControlAircract(ac);
            }
            this.field_70170_p.func_72838_d((Entity)ac);
            if (!ac.isTargetDrone()) {
                ac.setFuel((int)((float)ac.getMaxFuel() * 0.05f));
                W_EntityPlayer.closeScreen(user);
            } else {
                ac.setFuel(ac.getMaxFuel());
            }
        } else {
            ((MCH_EntityAircraft)ac).func_70106_y();
        }
    }

    public void _setInventorySlotContents(int par1, ItemStack itemStack) {
        super.func_70299_a(par1, itemStack);
    }

    public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
        if (hand != EnumHand.MAIN_HAND) {
            return false;
        }
        int kind = this.getKind();
        if (kind <= 0) {
            return false;
        }
        if (this.getRiddenByEntity() != null) {
            return false;
        }
        if (kind == 2) {
            if (player.func_70093_af()) {
                this.setOpen(!this.isOpen());
                return false;
            }
            if (!this.isOpen()) {
                return false;
            }
        }
        this.lastRiddenByEntity = null;
        PooledGuiParameter.setEntity(player, this);
        if (!this.field_70170_p.field_72995_K) {
            player.func_184220_m((Entity)this);
            player.openGui((Object)MCH_MOD.instance, 0, player.field_70170_p, (int)this.field_70165_t, (int)this.field_70163_u, (int)this.field_70161_v);
        }
        return true;
    }

    @Override
    public int func_70302_i_() {
        return 1;
    }

    @Override
    public int func_70297_j_() {
        return 1;
    }

    public void unmountEntity(boolean unmountAllEntity) {
        Entity rByEntity = null;
        Entity riddenByEntity = this.getRiddenByEntity();
        if (riddenByEntity != null) {
            if (!this.field_70170_p.field_72995_K) {
                rByEntity = riddenByEntity;
                riddenByEntity.func_184210_p();
            }
        } else if (this.lastRiddenByEntity != null) {
            rByEntity = this.lastRiddenByEntity;
        }
        if (this.getControlAircract() != null) {
            this.getControlAircract().setUavStation(null);
        }
        this.setControlAircract(null);
        if (this.field_70170_p.field_72995_K) {
            W_EntityPlayer.closeScreen(rByEntity);
        }
        this.lastRiddenByEntity = null;
    }

    @Override
    @Nullable
    public Entity getRiddenByEntity() {
        List passengers = this.func_184188_bt();
        return passengers.isEmpty() ? null : (Entity)passengers.get(0);
    }

    public ItemStack getPickedResult(RayTraceResult target) {
        int kind = this.getKind();
        return kind > 0 ? new ItemStack((Item)MCH_MOD.itemUavStation[kind - 1]) : ItemStack.field_190927_a;
    }
}

