/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.entity.Entity
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.datasync.DataParameter
 *  net.minecraft.network.datasync.DataSerializer
 *  net.minecraft.network.datasync.DataSerializers
 *  net.minecraft.network.datasync.EntityDataManager
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.weapon;

import javax.annotation.Nullable;
import mcheli.weapon.MCH_EntityBullet;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponInfoManager;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntityA10
extends W_Entity {
    private static final DataParameter<String> WEAPON_NAME = EntityDataManager.func_187226_a(MCH_EntityA10.class, (DataSerializer)DataSerializers.field_187194_d);
    public final int DESPAWN_COUNT = 70;
    public int despawnCount = 0;
    public Entity shootingAircraft;
    public Entity shootingEntity;
    public int shotCount = 0;
    public int direction = 0;
    public int power;
    public float acceleration;
    public int explosionPower;
    public boolean isFlaming;
    public String name;
    public MCH_WeaponInfo weaponInfo;
    static int snd_num = 0;

    public MCH_EntityA10(World world) {
        super(world);
        this.field_70158_ak = true;
        this.field_70156_m = false;
        this.func_70105_a(5.0f, 3.0f);
        this.field_70159_w = 0.0;
        this.field_70181_x = 0.0;
        this.field_70179_y = 0.0;
        this.power = 32;
        this.acceleration = 4.0f;
        this.explosionPower = 1;
        this.isFlaming = false;
        this.shootingEntity = null;
        this.shootingAircraft = null;
        this.field_70178_ae = true;
        this._renderDistanceWeight *= 10.0;
    }

    public MCH_EntityA10(World world, double x, double y, double z) {
        this(world);
        this.field_70169_q = this.field_70165_t = x;
        this.field_70142_S = this.field_70165_t;
        this.field_70167_r = this.field_70163_u = y;
        this.field_70137_T = this.field_70163_u;
        this.field_70166_s = this.field_70161_v = z;
        this.field_70136_U = this.field_70161_v;
    }

    protected boolean func_70041_e_() {
        return false;
    }

    @Override
    protected void func_70088_a() {
        this.field_70180_af.func_187214_a(WEAPON_NAME, (Object)"");
    }

    public void setWeaponName(String s) {
        if (s != null && !s.isEmpty()) {
            this.weaponInfo = MCH_WeaponInfoManager.get(s);
            if (this.weaponInfo != null && !this.field_70170_p.field_72995_K) {
                this.field_70180_af.func_187227_b(WEAPON_NAME, (Object)s);
            }
        }
    }

    public String getWeaponName() {
        return (String)this.field_70180_af.func_187225_a(WEAPON_NAME);
    }

    @Nullable
    public MCH_WeaponInfo getInfo() {
        return this.weaponInfo;
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

    @Override
    public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
        return false;
    }

    public boolean func_70067_L() {
        return false;
    }

    public void func_70106_y() {
        super.func_70106_y();
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (!this.field_70128_L) {
            ++this.despawnCount;
        }
        if (this.weaponInfo == null) {
            this.setWeaponName(this.getWeaponName());
            if (this.weaponInfo == null) {
                this.func_70106_y();
                return;
            }
        }
        if (this.field_70170_p.field_72995_K) {
            this.onUpdate_Client();
        } else {
            this.onUpdate_Server();
        }
        if (!this.field_70128_L) {
            if (this.despawnCount <= 20) {
                this.field_70181_x = -0.3;
            } else {
                this.func_70107_b(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
                this.field_70181_x += 0.02;
            }
        }
    }

    public boolean isRender() {
        return this.despawnCount > 20;
    }

    private void onUpdate_Client() {
        this.shotCount += 4;
    }

    private void onUpdate_Server() {
        if (!this.field_70128_L) {
            if (this.despawnCount > 70) {
                this.func_70106_y();
            } else if (this.despawnCount > 0 && this.shotCount < 40) {
                for (int i = 0; i < 2; ++i) {
                    this.shotGAU8(true, this.shotCount);
                    ++this.shotCount;
                }
                if (this.shotCount == 38) {
                    W_WorldFunc.MOD_playSoundEffect(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, "gau-8_snd", 150.0f, 1.0f);
                }
            }
        }
    }

    protected void shotGAU8(boolean playSound, int cnt) {
        float yaw = 90 * this.direction;
        float pitch = 30.0f;
        double x = this.field_70165_t;
        double y = this.field_70163_u;
        double z = this.field_70161_v;
        double tX = this.field_70146_Z.nextDouble() - 0.5;
        double tY = -2.6;
        double tZ = this.field_70146_Z.nextDouble() - 0.5;
        if (this.direction == 0) {
            tZ += 10.0;
            z += (double)cnt * 0.6;
        }
        if (this.direction == 1) {
            tX -= 10.0;
            x -= (double)cnt * 0.6;
        }
        if (this.direction == 2) {
            tZ -= 10.0;
            z -= (double)cnt * 0.6;
        }
        if (this.direction == 3) {
            tX += 10.0;
            x += (double)cnt * 0.6;
        }
        double dist = MathHelper.func_76133_a((double)(tX * tX + tY * tY + tZ * tZ));
        tX = tX * 4.0 / dist;
        tY = tY * 4.0 / dist;
        tZ = tZ * 4.0 / dist;
        MCH_EntityBullet e = new MCH_EntityBullet(this.field_70170_p, x, y, z, tX, tY, tZ, yaw, pitch, this.acceleration);
        e.setName(this.getWeaponName());
        e.explosionPower = this.shotCount % 4 == 0 ? this.explosionPower : 0;
        e.setPower(this.power);
        e.shootingEntity = this.shootingEntity;
        e.shootingAircraft = this.shootingAircraft;
        this.field_70170_p.func_72838_d((Entity)e);
    }

    protected void func_70014_b(NBTTagCompound par1NBTTagCompound) {
        par1NBTTagCompound.func_74778_a("WeaponName", this.getWeaponName());
    }

    protected void func_70037_a(NBTTagCompound par1NBTTagCompound) {
        this.despawnCount = 200;
        if (par1NBTTagCompound.func_74764_b("WeaponName")) {
            this.setWeaponName(par1NBTTagCompound.func_74779_i("WeaponName"));
        }
    }

    @SideOnly(value=Side.CLIENT)
    public float getShadowSize() {
        return 10.0f;
    }
}

