/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.world.World
 */
package mcheli.aircraft;

import java.util.List;
import mcheli.MCH_PacketNotifyLock;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.weapon.MCH_EntityBaseBullet;
import mcheli.wrapper.W_Lib;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class MCH_MissileDetector {
    private MCH_EntityAircraft ac;
    private World world;
    private int alertCount;
    public static final int SEARCH_RANGE = 60;

    public MCH_MissileDetector(MCH_EntityAircraft aircraft, World w) {
        this.world = w;
        this.ac = aircraft;
        this.alertCount = 0;
    }

    public void update() {
        boolean isLocked;
        if (!this.ac.haveFlare()) {
            return;
        }
        if (this.alertCount > 0) {
            --this.alertCount;
        }
        if (isLocked = this.ac.getEntityData().func_74767_n("Tracking")) {
            this.ac.getEntityData().func_74757_a("Tracking", false);
        }
        if (this.ac.getEntityData().func_74767_n("LockOn")) {
            if (this.alertCount == 0) {
                this.alertCount = 10;
                if (this.ac != null && this.ac.haveFlare() && !this.ac.isDestroyed()) {
                    for (int i = 0; i < 2; ++i) {
                        Entity entity = this.ac.getEntityBySeatId(i);
                        if (!(entity instanceof EntityPlayerMP)) continue;
                        MCH_PacketNotifyLock.sendToPlayer((EntityPlayer)((EntityPlayerMP)entity));
                    }
                }
            }
            this.ac.getEntityData().func_74757_a("LockOn", false);
        }
        if (this.ac.isDestroyed()) {
            return;
        }
        Entity rider = this.ac.getRiddenByEntity();
        if (rider == null) {
            rider = this.ac.getEntityBySeatId(1);
        }
        if (rider != null) {
            if (this.ac.isFlareUsing()) {
                this.destroyMissile();
            } else if (!this.ac.isUAV() && !this.world.field_72995_K) {
                if (this.alertCount == 0 && (isLocked || this.isLockedByMissile())) {
                    this.alertCount = 20;
                    W_WorldFunc.MOD_playSoundAtEntity(this.ac, "alert", 1.0f, 1.0f);
                }
            } else if (this.ac.isUAV() && this.world.field_72995_K && this.alertCount == 0 && (isLocked || this.isLockedByMissile())) {
                this.alertCount = 20;
                if (W_Lib.isClientPlayer(rider)) {
                    W_McClient.MOD_playSoundFX("alert", 1.0f, 1.0f);
                }
            }
        }
    }

    public boolean destroyMissile() {
        List list = this.world.func_72872_a(MCH_EntityBaseBullet.class, this.ac.func_174813_aQ().func_72314_b(60.0, 60.0, 60.0));
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                MCH_EntityBaseBullet msl = (MCH_EntityBaseBullet)((Object)list.get(i));
                if (msl.targetEntity == null || !this.ac.isMountedEntity(msl.targetEntity) && !msl.targetEntity.equals((Object)this.ac)) continue;
                msl.targetEntity = null;
                msl.func_70106_y();
            }
        }
        return false;
    }

    public boolean isLockedByMissile() {
        List list = this.world.func_72872_a(MCH_EntityBaseBullet.class, this.ac.func_174813_aQ().func_72314_b(60.0, 60.0, 60.0));
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                MCH_EntityBaseBullet msl = (MCH_EntityBaseBullet)((Object)list.get(i));
                if (msl.targetEntity == null || !this.ac.isMountedEntity(msl.targetEntity) && !msl.targetEntity.equals((Object)this.ac)) continue;
                return true;
            }
        }
        return false;
    }
}

