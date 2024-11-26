/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package mcheli.helicopter;

import mcheli.MCH_Config;
import mcheli.MCH_KeyName;
import mcheli.MCH_Lib;
import mcheli.aircraft.MCH_AircraftCommonGui;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.helicopter.MCH_EntityHeli;
import mcheli.helicopter.MCH_HeliInfo;
import mcheli.weapon.MCH_EntityTvMissile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_GuiHeli
extends MCH_AircraftCommonGui {
    public MCH_GuiHeli(Minecraft minecraft) {
        super(minecraft);
    }

    @Override
    public boolean isDrawGui(EntityPlayer player) {
        return MCH_EntityAircraft.getAircraft_RiddenOrControl((Entity)player) instanceof MCH_EntityHeli;
    }

    @Override
    public void drawGui(EntityPlayer player, boolean isThirdPersonView) {
        MCH_EntityAircraft ac = MCH_EntityAircraft.getAircraft_RiddenOrControl((Entity)player);
        if (!(ac instanceof MCH_EntityHeli) || ac.isDestroyed()) {
            return;
        }
        MCH_EntityHeli heli = (MCH_EntityHeli)ac;
        int seatID = ac.getSeatIdByEntity((Entity)player);
        GL11.glLineWidth((float)scaleFactor);
        if (heli.getCameraMode(player) == 1) {
            this.drawNightVisionNoise();
        }
        if (!isThirdPersonView || MCH_Config.DisplayHUDThirdPerson.prmBool) {
            if (seatID == 0 && heli.getIsGunnerMode((Entity)player)) {
                this.drawHud(ac, player, 1);
            } else {
                this.drawHud(ac, player, seatID);
            }
        }
        this.drawDebugtInfo(heli);
        if (!heli.getIsGunnerMode((Entity)player)) {
            if (!isThirdPersonView || MCH_Config.DisplayHUDThirdPerson.prmBool) {
                this.drawKeyBind(heli, player, seatID);
            }
            this.drawHitBullet(heli, -14101432, seatID);
        } else {
            if (!isThirdPersonView || MCH_Config.DisplayHUDThirdPerson.prmBool) {
                MCH_EntityTvMissile tvmissile = heli.getTVMissile();
                if (!heli.isMissileCameraMode((Entity)player)) {
                    this.drawKeyBind(heli, player, seatID);
                } else if (tvmissile != null) {
                    this.drawTvMissileNoise(heli, tvmissile);
                }
            }
            this.drawHitBullet(heli, -805306369, seatID);
        }
    }

    public void drawKeyBind(MCH_EntityHeli heli, EntityPlayer player, int seatID) {
        String msg;
        int c;
        if (MCH_Config.HideKeybind.prmBool) {
            return;
        }
        MCH_HeliInfo info = heli.getHeliInfo();
        if (info == null) {
            return;
        }
        int colorActive = -1342177281;
        int colorInactive = -1349546097;
        int RX = this.centerX + 120;
        int LX = this.centerX - 200;
        this.drawKeyBind(heli, info, player, seatID, RX, LX, colorActive, colorInactive);
        if (seatID == 0 && info.isEnableGunnerMode && !Keyboard.isKeyDown((int)MCH_Config.KeyFreeLook.prmInt)) {
            c = heli.isHoveringMode() ? colorInactive : colorActive;
            msg = (heli.getIsGunnerMode((Entity)player) ? "Normal" : "Gunner") + " : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwitchMode.prmInt);
            this.drawString(msg, RX, this.centerY - 70, c);
        }
        if (seatID > 0 && heli.canSwitchGunnerModeOtherSeat(player)) {
            String msg2 = (heli.getIsGunnerMode((Entity)player) ? "Normal" : "Camera") + " : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwitchMode.prmInt);
            this.drawString(msg2, RX, this.centerY - 40, colorActive);
        }
        if (seatID == 0 && !Keyboard.isKeyDown((int)MCH_Config.KeyFreeLook.prmInt)) {
            c = heli.getIsGunnerMode((Entity)player) ? colorInactive : colorActive;
            msg = (heli.getIsGunnerMode((Entity)player) ? "Normal" : "Hovering") + " : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwitchHovering.prmInt);
            this.drawString(msg, RX, this.centerY - 60, c);
        }
        if (seatID == 0) {
            if (heli.getTowChainEntity() != null && !heli.getTowChainEntity().field_70128_L) {
                String msg3 = "Drop  : " + MCH_KeyName.getDescOrName(MCH_Config.KeyExtra.prmInt);
                this.drawString(msg3, RX, this.centerY - 30, colorActive);
            } else if (info.isEnableFoldBlade && MCH_Lib.getBlockIdY(heli.field_70170_p, heli.field_70165_t, heli.field_70163_u, heli.field_70161_v, 1, -2, true) > 0 && heli.getCurrentThrottle() <= 0.01) {
                String msg4 = "FoldBlade  : " + MCH_KeyName.getDescOrName(MCH_Config.KeyExtra.prmInt);
                this.drawString(msg4, RX, this.centerY - 30, colorActive);
            }
        }
        if ((heli.getIsGunnerMode((Entity)player) || heli.isUAV()) && info.cameraZoom > 1) {
            String msg5 = "Zoom : " + MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt);
            this.drawString(msg5, LX, this.centerY - 80, colorActive);
        } else if (seatID == 0 && (heli.canFoldHatch() || heli.canUnfoldHatch())) {
            String msg6 = "OpenHatch : " + MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt);
            this.drawString(msg6, LX, this.centerY - 80, colorActive);
        }
    }
}

