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
package mcheli.helicopter;

import mcheli.MCH_Config;
import mcheli.MCH_Key;
import mcheli.MCH_Lib;
import mcheli.MCH_ViewEntityDummy;
import mcheli.aircraft.MCH_AircraftClientTickHandler;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.aircraft.MCH_SeatInfo;
import mcheli.helicopter.MCH_EntityHeli;
import mcheli.helicopter.MCH_HeliPacketPlayerControl;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_Network;
import mcheli.wrapper.W_Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MCH_ClientHeliTickHandler
extends MCH_AircraftClientTickHandler {
    public MCH_Key KeySwitchMode;
    public MCH_Key KeySwitchHovering;
    public MCH_Key KeyZoom;
    public MCH_Key[] Keys;

    public MCH_ClientHeliTickHandler(Minecraft minecraft, MCH_Config config) {
        super(minecraft, config);
        this.updateKeybind(config);
    }

    @Override
    public void updateKeybind(MCH_Config config) {
        super.updateKeybind(config);
        this.KeySwitchMode = new MCH_Key(MCH_Config.KeySwitchMode.prmInt);
        this.KeySwitchHovering = new MCH_Key(MCH_Config.KeySwitchHovering.prmInt);
        this.KeyZoom = new MCH_Key(MCH_Config.KeyZoom.prmInt);
        this.Keys = new MCH_Key[]{this.KeyUp, this.KeyDown, this.KeyRight, this.KeyLeft, this.KeySwitchMode, this.KeySwitchHovering, this.KeyUseWeapon, this.KeySwWeaponMode, this.KeySwitchWeapon1, this.KeySwitchWeapon2, this.KeyZoom, this.KeyCameraMode, this.KeyUnmount, this.KeyUnmountForce, this.KeyFlare, this.KeyExtra, this.KeyFreeLook, this.KeyGUI, this.KeyGearUpDown, this.KeyPutToRack, this.KeyDownFromRack};
    }

    protected void update(EntityPlayer player, MCH_EntityHeli heli, boolean isPilot) {
        MCH_SeatInfo seatInfo;
        if (heli.getIsGunnerMode((Entity)player) && (seatInfo = heli.getSeatInfo((Entity)player)) != null) {
            MCH_ClientHeliTickHandler.setRotLimitPitch(seatInfo.minPitch, seatInfo.maxPitch, (Entity)player);
        }
        heli.updateCameraRotate(player.field_70177_z, player.field_70125_A);
        heli.updateRadar(5);
    }

    @Override
    protected void onTick(boolean inGUI) {
        for (MCH_Key k : this.Keys) {
            k.update();
        }
        this.isBeforeRiding = this.isRiding;
        EntityPlayerSP player = this.mc.field_71439_g;
        MCH_EntityAircraft heli = null;
        boolean isPilot = true;
        if (player != null) {
            MCH_EntityUavStation uavStation;
            if (player.func_184187_bx() instanceof MCH_EntityHeli) {
                heli = (MCH_EntityHeli)player.func_184187_bx();
            } else if (player.func_184187_bx() instanceof MCH_EntitySeat) {
                MCH_EntitySeat seat = (MCH_EntitySeat)player.func_184187_bx();
                if (seat.getParent() instanceof MCH_EntityHeli) {
                    isPilot = false;
                    heli = (MCH_EntityHeli)seat.getParent();
                }
            } else if (player.func_184187_bx() instanceof MCH_EntityUavStation && (uavStation = (MCH_EntityUavStation)player.func_184187_bx()).getControlAircract() instanceof MCH_EntityHeli) {
                heli = (MCH_EntityHeli)uavStation.getControlAircract();
            }
        }
        if (heli != null && heli.getAcInfo() != null) {
            this.update((EntityPlayer)player, (MCH_EntityHeli)heli, isPilot);
            MCH_ViewEntityDummy viewEntityDummy = MCH_ViewEntityDummy.getInstance((World)this.mc.field_71441_e);
            viewEntityDummy.update(((MCH_EntityHeli)heli).camera);
            if (!inGUI) {
                if (!heli.isDestroyed()) {
                    this.playerControl((EntityPlayer)player, (MCH_EntityHeli)heli, isPilot);
                }
            } else {
                this.playerControlInGUI((EntityPlayer)player, (MCH_EntityHeli)heli, isPilot);
            }
            boolean hideHand = true;
            if (isPilot && heli.isAlwaysCameraView() || heli.getIsGunnerMode((Entity)player)) {
                MCH_Lib.setRenderViewEntity((EntityLivingBase)viewEntityDummy);
            } else {
                MCH_Lib.setRenderViewEntity((EntityLivingBase)player);
                if (!isPilot && heli.getCurrentWeaponID((Entity)player) < 0) {
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
        if (!this.isBeforeRiding && this.isRiding) {
            W_Reflection.setThirdPersonDistance(((MCH_EntityHeli)heli).thirdPersonDist);
        } else if (this.isBeforeRiding && !this.isRiding) {
            W_Reflection.restoreDefaultThirdPersonDistance();
            W_Reflection.setCameraRoll(0.0f);
            MCH_Lib.enableFirstPersonItemRender();
            MCH_Lib.setRenderViewEntity((EntityLivingBase)player);
        }
    }

    protected void playerControlInGUI(EntityPlayer player, MCH_EntityHeli heli, boolean isPilot) {
        this.commonPlayerControlInGUI(player, heli, isPilot, new MCH_HeliPacketPlayerControl());
    }

    protected void playerControl(EntityPlayer player, MCH_EntityHeli heli, boolean isPilot) {
        MCH_HeliPacketPlayerControl pc = new MCH_HeliPacketPlayerControl();
        boolean send = false;
        send = this.commonPlayerControl(player, heli, isPilot, pc);
        if (isPilot) {
            if (this.KeyExtra.isKeyDown()) {
                if (heli.getTowChainEntity() != null) {
                    MCH_ClientHeliTickHandler.playSoundOK();
                    pc.unhitchChainId = W_Entity.getEntityId(heli.getTowChainEntity());
                    send = true;
                } else if (heli.canSwitchFoldBlades()) {
                    if (heli.isFoldBlades()) {
                        heli.unfoldBlades();
                        pc.switchFold = 0;
                    } else {
                        heli.foldBlades();
                        pc.switchFold = 1;
                    }
                    send = true;
                    MCH_ClientHeliTickHandler.playSoundOK();
                } else {
                    MCH_ClientHeliTickHandler.playSoundNG();
                }
            }
            if (this.KeySwitchHovering.isKeyDown()) {
                if (heli.canSwitchHoveringMode()) {
                    pc.switchMode = (byte)(heli.isHoveringMode() ? 2 : 3);
                    heli.switchHoveringMode(!heli.isHoveringMode());
                    send = true;
                } else {
                    MCH_ClientHeliTickHandler.playSoundNG();
                }
            } else if (this.KeySwitchMode.isKeyDown()) {
                if (heli.canSwitchGunnerMode()) {
                    pc.switchMode = (byte)(!heli.getIsGunnerMode((Entity)player) ? 1 : 0);
                    heli.switchGunnerMode(!heli.getIsGunnerMode((Entity)player));
                    send = true;
                } else {
                    MCH_ClientHeliTickHandler.playSoundNG();
                }
            }
        } else if (this.KeySwitchMode.isKeyDown()) {
            if (heli.canSwitchGunnerModeOtherSeat(player)) {
                heli.switchGunnerModeOtherSeat(player);
                send = true;
            } else {
                MCH_ClientHeliTickHandler.playSoundNG();
            }
        }
        if (this.KeyZoom.isKeyDown()) {
            boolean isUav;
            boolean bl = isUav = heli.isUAV() && !heli.getAcInfo().haveHatch();
            if (heli.getIsGunnerMode((Entity)player) || isUav) {
                heli.zoomCamera();
                MCH_ClientHeliTickHandler.playSound("zoom", 0.5f, 1.0f);
            } else if (isPilot && heli.getAcInfo().haveHatch()) {
                if (heli.canFoldHatch()) {
                    pc.switchHatch = (byte)2;
                    send = true;
                } else if (heli.canUnfoldHatch()) {
                    pc.switchHatch = 1;
                    send = true;
                } else {
                    MCH_ClientHeliTickHandler.playSoundNG();
                }
            }
        }
        if (send) {
            W_Network.sendToServer(pc);
        }
    }
}

