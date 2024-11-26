/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.world.World
 */
package mcheli.tank;

import mcheli.MCH_Config;
import mcheli.MCH_Key;
import mcheli.MCH_Lib;
import mcheli.MCH_ViewEntityDummy;
import mcheli.aircraft.MCH_AircraftClientTickHandler;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.aircraft.MCH_SeatInfo;
import mcheli.tank.MCH_EntityTank;
import mcheli.tank.MCH_TankPacketPlayerControl;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.wrapper.W_Network;
import mcheli.wrapper.W_Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MCH_ClientTankTickHandler
extends MCH_AircraftClientTickHandler {
    public MCH_Key KeySwitchMode;
    public MCH_Key KeyZoom;
    public MCH_Key[] Keys;

    public MCH_ClientTankTickHandler(Minecraft minecraft, MCH_Config config) {
        super(minecraft, config);
        this.updateKeybind(config);
    }

    @Override
    public void updateKeybind(MCH_Config config) {
        super.updateKeybind(config);
        this.KeySwitchMode = new MCH_Key(MCH_Config.KeySwitchMode.prmInt);
        this.KeyZoom = new MCH_Key(MCH_Config.KeyZoom.prmInt);
        this.Keys = new MCH_Key[]{this.KeyUp, this.KeyDown, this.KeyRight, this.KeyLeft, this.KeySwitchMode, this.KeyUseWeapon, this.KeySwWeaponMode, this.KeySwitchWeapon1, this.KeySwitchWeapon2, this.KeyZoom, this.KeyCameraMode, this.KeyUnmount, this.KeyUnmountForce, this.KeyFlare, this.KeyExtra, this.KeyFreeLook, this.KeyGUI, this.KeyGearUpDown, this.KeyBrake, this.KeyPutToRack, this.KeyDownFromRack};
    }

    protected void update(EntityPlayer player, MCH_EntityTank tank) {
        MCH_SeatInfo seatInfo;
        if (tank.getIsGunnerMode((Entity)player) && (seatInfo = tank.getSeatInfo((Entity)player)) != null) {
            MCH_ClientTankTickHandler.setRotLimitPitch(seatInfo.minPitch, seatInfo.maxPitch, (Entity)player);
        }
        tank.updateRadar(10);
        tank.updateCameraRotate(player.field_70177_z, player.field_70125_A);
    }

    @Override
    protected void onTick(boolean inGUI) {
        for (MCH_Key k : this.Keys) {
            k.update();
        }
        this.isBeforeRiding = this.isRiding;
        EntityPlayerSP player = this.mc.field_71439_g;
        MCH_EntityAircraft tank = null;
        boolean isPilot = true;
        if (player != null) {
            MCH_EntityUavStation uavStation;
            if (player.func_184187_bx() instanceof MCH_EntityTank) {
                tank = (MCH_EntityTank)player.func_184187_bx();
            } else if (player.func_184187_bx() instanceof MCH_EntitySeat) {
                MCH_EntitySeat seat = (MCH_EntitySeat)player.func_184187_bx();
                if (seat.getParent() instanceof MCH_EntityTank) {
                    isPilot = false;
                    tank = (MCH_EntityTank)seat.getParent();
                }
            } else if (player.func_184187_bx() instanceof MCH_EntityUavStation && (uavStation = (MCH_EntityUavStation)player.func_184187_bx()).getControlAircract() instanceof MCH_EntityTank) {
                tank = (MCH_EntityTank)uavStation.getControlAircract();
            }
        }
        if (tank != null && tank.getAcInfo() != null) {
            this.update((EntityPlayer)player, (MCH_EntityTank)tank);
            MCH_ViewEntityDummy viewEntityDummy = MCH_ViewEntityDummy.getInstance((World)this.mc.field_71441_e);
            viewEntityDummy.update(((MCH_EntityTank)tank).camera);
            if (!inGUI) {
                if (!tank.isDestroyed()) {
                    this.playerControl((EntityPlayer)player, (MCH_EntityTank)tank, isPilot);
                }
            } else {
                this.playerControlInGUI((EntityPlayer)player, (MCH_EntityTank)tank, isPilot);
            }
            boolean hideHand = true;
            if (isPilot && tank.isAlwaysCameraView() || tank.getIsGunnerMode((Entity)player) || tank.getCameraId() > 0) {
                MCH_Lib.setRenderViewEntity((EntityLivingBase)viewEntityDummy);
            } else {
                MCH_Lib.setRenderViewEntity((EntityLivingBase)player);
                if (!isPilot && tank.getCurrentWeaponID((Entity)player) < 0) {
                    hideHand = false;
                }
            }
            if (hideHand) {
                MCH_Lib.disableFirstPersonItemRender(player.func_184614_ca());
            }
            this.isRiding = true;
        } else {
            this.isRiding = false;
        }
        if (!this.isBeforeRiding && this.isRiding && tank != null) {
            W_Reflection.setThirdPersonDistance(((MCH_EntityTank)tank).thirdPersonDist);
            MCH_ViewEntityDummy.getInstance((World)this.mc.field_71441_e).func_70107_b(((MCH_EntityTank)tank).field_70165_t, ((MCH_EntityTank)tank).field_70163_u + 0.5, ((MCH_EntityTank)tank).field_70161_v);
        } else if (this.isBeforeRiding && !this.isRiding) {
            W_Reflection.restoreDefaultThirdPersonDistance();
            MCH_Lib.enableFirstPersonItemRender();
            MCH_Lib.setRenderViewEntity((EntityLivingBase)player);
            W_Reflection.setCameraRoll(0.0f);
        }
    }

    protected void playerControlInGUI(EntityPlayer player, MCH_EntityTank tank, boolean isPilot) {
        this.commonPlayerControlInGUI(player, tank, isPilot, new MCH_TankPacketPlayerControl());
    }

    protected void playerControl(EntityPlayer player, MCH_EntityTank tank, boolean isPilot) {
        MCH_TankPacketPlayerControl pc = new MCH_TankPacketPlayerControl();
        boolean send = false;
        MCH_EntityTank ac = tank;
        send = this.commonPlayerControl(player, tank, isPilot, pc);
        if (ac.getAcInfo().defaultFreelook && pc.switchFreeLook > 0) {
            pc.switchFreeLook = 0;
        }
        if (isPilot) {
            if (this.KeySwitchMode.isKeyDown()) {
                if (ac.getIsGunnerMode((Entity)player) && ac.canSwitchCameraPos()) {
                    pc.switchMode = 0;
                    ac.switchGunnerMode(false);
                    send = true;
                    ac.setCameraId(1);
                } else if (ac.getCameraId() > 0) {
                    ac.setCameraId(ac.getCameraId() + 1);
                    if (ac.getCameraId() >= ac.getCameraPosNum()) {
                        ac.setCameraId(0);
                    }
                } else if (((MCH_EntityAircraft)ac).canSwitchGunnerMode()) {
                    pc.switchMode = (byte)(!ac.getIsGunnerMode((Entity)player) ? 1 : 0);
                    ac.switchGunnerMode(!ac.getIsGunnerMode((Entity)player));
                    send = true;
                    ac.setCameraId(0);
                } else if (ac.canSwitchCameraPos()) {
                    ac.setCameraId(1);
                } else {
                    MCH_ClientTankTickHandler.playSoundNG();
                }
            }
        } else if (this.KeySwitchMode.isKeyDown()) {
            if (tank.canSwitchGunnerModeOtherSeat(player)) {
                tank.switchGunnerModeOtherSeat(player);
                send = true;
            } else {
                MCH_ClientTankTickHandler.playSoundNG();
            }
        }
        if (this.KeyZoom.isKeyDown()) {
            boolean isUav;
            boolean bl = isUav = tank.isUAV() && !tank.getAcInfo().haveHatch();
            if (tank.getIsGunnerMode((Entity)player) || isUav) {
                tank.zoomCamera();
                MCH_ClientTankTickHandler.playSound("zoom", 0.5f, 1.0f);
            } else if (isPilot && tank.getAcInfo().haveHatch()) {
                if (tank.canFoldHatch()) {
                    pc.switchHatch = (byte)2;
                    send = true;
                } else if (tank.canUnfoldHatch()) {
                    pc.switchHatch = 1;
                    send = true;
                }
            }
        }
        if (send) {
            W_Network.sendToServer(pc);
        }
    }
}

