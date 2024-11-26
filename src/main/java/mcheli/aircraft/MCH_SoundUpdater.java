/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.aircraft;

import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.wrapper.W_SoundUpdater;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class MCH_SoundUpdater
extends W_SoundUpdater {
    private final MCH_EntityAircraft theAircraft;
    private final EntityPlayerSP thePlayer;
    private boolean isMoving;
    private boolean silent;
    private float aircraftPitch;
    private float aircraftVolume;
    private float addPitch;
    private boolean isFirstUpdate;
    private double prevDist;
    private int soundDelay = 0;

    public MCH_SoundUpdater(Minecraft mc, MCH_EntityAircraft aircraft, EntityPlayerSP entityPlayerSP) {
        super(mc, aircraft);
        this.theAircraft = aircraft;
        this.thePlayer = entityPlayerSP;
        this.isFirstUpdate = true;
    }

    public void update() {
        if (this.theAircraft.getSoundName().isEmpty() || this.theAircraft.getAcInfo() == null) {
            return;
        }
        if (this.isFirstUpdate) {
            this.isFirstUpdate = false;
            this.initEntitySound(this.theAircraft.getSoundName());
        }
        MCH_AircraftInfo info = this.theAircraft.getAcInfo();
        boolean isBeforeMoving = this.isMoving;
        boolean isDead = this.theAircraft.field_70128_L;
        if (isDead || !this.silent && this.aircraftVolume == 0.0f) {
            if (isDead) {
                this.stopEntitySound(this.theAircraft);
            }
            this.silent = true;
            if (isDead) {
                return;
            }
        }
        boolean isRide = this.theAircraft.getSeatIdByEntity((Entity)this.thePlayer) >= 0;
        boolean isPlaying = this.isEntitySoundPlaying(this.theAircraft);
        if (!isPlaying && this.aircraftVolume > 0.0f) {
            if (this.soundDelay > 0) {
                --this.soundDelay;
            } else {
                this.soundDelay = 20;
                this.playEntitySound(this.theAircraft.getSoundName(), this.theAircraft, this.aircraftVolume, this.aircraftPitch, true);
            }
            this.silent = false;
        }
        float prevVolume = this.aircraftVolume;
        float prevPitch = this.aircraftPitch;
        boolean bl = this.isMoving = (double)(info.soundVolume * this.theAircraft.getSoundVolume()) >= 0.01;
        if (this.isMoving) {
            this.aircraftVolume = info.soundVolume * this.theAircraft.getSoundVolume();
            this.aircraftPitch = info.soundPitch * this.theAircraft.getSoundPitch();
            if (!isRide) {
                double dist = this.thePlayer.func_70011_f(this.theAircraft.field_70165_t, this.thePlayer.field_70163_u, this.theAircraft.field_70161_v);
                double pitch = this.prevDist - dist;
                if (Math.abs(pitch) > 0.3) {
                    this.addPitch = (float)((double)this.addPitch + pitch / 40.0);
                    float maxAddPitch = 0.2f;
                    if (this.addPitch < -maxAddPitch) {
                        this.addPitch = -maxAddPitch;
                    }
                    if (this.addPitch > maxAddPitch) {
                        this.addPitch = maxAddPitch;
                    }
                }
                this.addPitch = (float)((double)this.addPitch * 0.9);
                this.aircraftPitch += this.addPitch;
                this.prevDist = dist;
            }
            if (this.aircraftPitch < 0.0f) {
                this.aircraftPitch = 0.0f;
            }
        } else if (isBeforeMoving) {
            this.aircraftVolume = 0.0f;
            this.aircraftPitch = 0.0f;
        }
        if (!this.silent) {
            if (this.aircraftPitch != prevPitch) {
                this.setEntitySoundPitch(this.theAircraft, this.aircraftPitch);
            }
            if (this.aircraftVolume != prevVolume) {
                this.setEntitySoundVolume(this.theAircraft, this.aircraftVolume);
            }
        }
        boolean updateLocation = false;
        updateLocation = true;
        if (updateLocation && this.aircraftVolume > 0.0f) {
            if (isRide) {
                this.updateSoundLocation(this.theAircraft);
            } else {
                double px = this.thePlayer.field_70165_t;
                double py = this.thePlayer.field_70163_u;
                double pz = this.thePlayer.field_70161_v;
                double dx = this.theAircraft.field_70165_t - px;
                double dy = this.theAircraft.field_70163_u - py;
                double dz = this.theAircraft.field_70161_v - pz;
                double dist = (double)info.soundRange / 16.0;
                this.updateSoundLocation(px + (dx /= dist), py + (dy /= dist), pz + (dz /= dist));
            }
        } else if (this.isEntitySoundPlaying(this.theAircraft)) {
            this.stopEntitySound(this.theAircraft);
        }
    }
}

