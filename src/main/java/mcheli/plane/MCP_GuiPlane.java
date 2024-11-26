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
package mcheli.plane;

import mcheli.MCH_Config;
import mcheli.MCH_KeyName;
import mcheli.aircraft.MCH_AircraftCommonGui;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.plane.MCP_EntityPlane;
import mcheli.plane.MCP_PlaneInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCP_GuiPlane
extends MCH_AircraftCommonGui {
    public MCP_GuiPlane(Minecraft minecraft) {
        super(minecraft);
    }

    @Override
    public boolean isDrawGui(EntityPlayer player) {
        return MCH_EntityAircraft.getAircraft_RiddenOrControl((Entity)player) instanceof MCP_EntityPlane;
    }

    @Override
    public void drawGui(EntityPlayer player, boolean isThirdPersonView) {
        MCH_EntityAircraft ac = MCH_EntityAircraft.getAircraft_RiddenOrControl((Entity)player);
        if (!(ac instanceof MCP_EntityPlane) || ac.isDestroyed()) {
            return;
        }
        MCP_EntityPlane plane = (MCP_EntityPlane)ac;
        int seatID = ac.getSeatIdByEntity((Entity)player);
        GL11.glLineWidth((float)scaleFactor);
        if (plane.getCameraMode(player) == 1) {
            this.drawNightVisionNoise();
        }
        if (!isThirdPersonView || MCH_Config.DisplayHUDThirdPerson.prmBool) {
            if (seatID == 0 && plane.getIsGunnerMode((Entity)player)) {
                this.drawHud(ac, player, 1);
            } else {
                this.drawHud(ac, player, seatID);
            }
        }
        this.drawDebugtInfo(plane);
        if (!isThirdPersonView || MCH_Config.DisplayHUDThirdPerson.prmBool) {
            if (plane.getTVMissile() != null && (plane.getIsGunnerMode((Entity)player) || plane.isUAV())) {
                this.drawTvMissileNoise(plane, plane.getTVMissile());
            } else {
                this.drawKeybind(plane, player, seatID);
            }
        }
        this.drawHitBullet(plane, -14101432, seatID);
    }

    public void drawKeybind(MCP_EntityPlane plane, EntityPlayer player, int seatID) {
        int stat;
        String msg;
        if (MCH_Config.HideKeybind.prmBool) {
            return;
        }
        MCP_PlaneInfo info = plane.getPlaneInfo();
        if (info == null) {
            return;
        }
        int colorActive = -1342177281;
        int colorInactive = -1349546097;
        int RX = this.centerX + 120;
        int LX = this.centerX - 200;
        this.drawKeyBind(plane, info, player, seatID, RX, LX, colorActive, colorInactive);
        if (seatID == 0 && info.isEnableGunnerMode && !Keyboard.isKeyDown((int)MCH_Config.KeyFreeLook.prmInt)) {
            int c = plane.isHoveringMode() ? colorInactive : colorActive;
            msg = (plane.getIsGunnerMode((Entity)player) ? "Normal" : "Gunner") + " : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwitchMode.prmInt);
            this.drawString(msg, RX, this.centerY - 70, c);
        }
        if (seatID > 0 && plane.canSwitchGunnerModeOtherSeat(player)) {
            String msg2 = (plane.getIsGunnerMode((Entity)player) ? "Normal" : "Camera") + " : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwitchMode.prmInt);
            this.drawString(msg2, RX, this.centerY - 40, colorActive);
        }
        if (seatID == 0 && info.isEnableVtol && !Keyboard.isKeyDown((int)MCH_Config.KeyFreeLook.prmInt) && (stat = plane.getVtolMode()) != 1) {
            msg = (stat == 0 ? "VTOL : " : "Normal : ") + MCH_KeyName.getDescOrName(MCH_Config.KeyExtra.prmInt);
            this.drawString(msg, RX, this.centerY - 60, colorActive);
        }
        if (plane.canEjectSeat((Entity)player)) {
            String msg3 = "Eject seat: " + MCH_KeyName.getDescOrName(MCH_Config.KeySwitchHovering.prmInt);
            this.drawString(msg3, RX, this.centerY - 30, colorActive);
        }
        if (plane.getIsGunnerMode((Entity)player) && info.cameraZoom > 1) {
            String msg4 = "Zoom : " + MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt);
            this.drawString(msg4, LX, this.centerY - 80, colorActive);
        } else if (seatID == 0) {
            if (plane.canFoldWing() || plane.canUnfoldWing()) {
                String msg5 = "FoldWing : " + MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt);
                this.drawString(msg5, LX, this.centerY - 80, colorActive);
            } else if (plane.canFoldHatch() || plane.canUnfoldHatch()) {
                String msg6 = "OpenHatch : " + MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt);
                this.drawString(msg6, LX, this.centerY - 80, colorActive);
            }
        }
    }
}

