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
package mcheli.vehicle;

import mcheli.MCH_Config;
import mcheli.MCH_Key;
import mcheli.MCH_Lib;
import mcheli.MCH_ViewEntityDummy;
import mcheli.aircraft.MCH_AircraftClientTickHandler;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.vehicle.MCH_EntityVehicle;
import mcheli.vehicle.MCH_PacketVehiclePlayerControl;
import mcheli.vehicle.MCH_VehicleInfo;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_Network;
import mcheli.wrapper.W_Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MCH_ClientVehicleTickHandler
extends MCH_AircraftClientTickHandler {
    public MCH_Key KeySwitchMode;
    public MCH_Key KeySwitchHovering;
    public MCH_Key KeyZoom;
    public MCH_Key KeyExtra;
    public MCH_Key[] Keys;

    public MCH_ClientVehicleTickHandler(Minecraft minecraft, MCH_Config config) {
        super(minecraft, config);
        this.updateKeybind(config);
    }

    @Override
    public void updateKeybind(MCH_Config config) {
        super.updateKeybind(config);
        this.KeySwitchMode = new MCH_Key(MCH_Config.KeySwitchMode.prmInt);
        this.KeySwitchHovering = new MCH_Key(MCH_Config.KeySwitchHovering.prmInt);
        this.KeyZoom = new MCH_Key(MCH_Config.KeyZoom.prmInt);
        this.KeyExtra = new MCH_Key(MCH_Config.KeyExtra.prmInt);
        this.Keys = new MCH_Key[]{this.KeyUp, this.KeyDown, this.KeyRight, this.KeyLeft, this.KeySwitchMode, this.KeySwitchHovering, this.KeyUseWeapon, this.KeySwWeaponMode, this.KeySwitchWeapon1, this.KeySwitchWeapon2, this.KeyZoom, this.KeyCameraMode, this.KeyUnmount, this.KeyUnmountForce, this.KeyFlare, this.KeyExtra, this.KeyGUI};
    }

    protected void update(EntityPlayer player, MCH_EntityVehicle vehicle, MCH_VehicleInfo info) {
        if (info != null) {
            MCH_ClientVehicleTickHandler.setRotLimitPitch(info.minRotationPitch, info.maxRotationPitch, (Entity)player);
        }
        vehicle.updateCameraRotate(player.field_70177_z, player.field_70125_A);
        vehicle.updateRadar(5);
    }

    @Override
    protected void onTick(boolean inGUI) {
        for (MCH_Key k : this.Keys) {
            k.update();
        }
        this.isBeforeRiding = this.isRiding;
        EntityPlayerSP player = this.mc.field_71439_g;
        MCH_EntityAircraft vehicle = null;
        boolean isPilot = true;
        if (player != null) {
            MCH_EntitySeat seat;
            if (player.func_184187_bx() instanceof MCH_EntityVehicle) {
                vehicle = (MCH_EntityVehicle)player.func_184187_bx();
            } else if (player.func_184187_bx() instanceof MCH_EntitySeat && (seat = (MCH_EntitySeat)player.func_184187_bx()).getParent() instanceof MCH_EntityVehicle) {
                isPilot = false;
                vehicle = (MCH_EntityVehicle)seat.getParent();
            }
        }
        if (vehicle != null && vehicle.getAcInfo() != null) {
            MCH_Lib.disableFirstPersonItemRender(player.func_184614_ca());
            this.update((EntityPlayer)player, (MCH_EntityVehicle)vehicle, ((MCH_EntityVehicle)vehicle).getVehicleInfo());
            MCH_ViewEntityDummy viewEntityDummy = MCH_ViewEntityDummy.getInstance((World)this.mc.field_71441_e);
            viewEntityDummy.update(((MCH_EntityVehicle)vehicle).camera);
            if (!inGUI) {
                if (!vehicle.isDestroyed()) {
                    this.playerControl((EntityPlayer)player, (MCH_EntityVehicle)vehicle, isPilot);
                }
            } else {
                this.playerControlInGUI((EntityPlayer)player, (MCH_EntityVehicle)vehicle, isPilot);
            }
            MCH_Lib.setRenderViewEntity((EntityLivingBase)viewEntityDummy);
            this.isRiding = true;
        } else {
            this.isRiding = false;
        }
        if (!this.isBeforeRiding && this.isRiding) {
            W_Reflection.setThirdPersonDistance(((MCH_EntityVehicle)vehicle).thirdPersonDist);
        } else if (this.isBeforeRiding && !this.isRiding) {
            W_Reflection.restoreDefaultThirdPersonDistance();
            MCH_Lib.enableFirstPersonItemRender();
            MCH_Lib.setRenderViewEntity((EntityLivingBase)player);
        }
    }

    protected void playerControlInGUI(EntityPlayer player, MCH_EntityVehicle vehicle, boolean isPilot) {
        this.commonPlayerControlInGUI(player, vehicle, isPilot, new MCH_PacketVehiclePlayerControl());
    }

    protected void playerControl(EntityPlayer player, MCH_EntityVehicle vehicle, boolean isPilot) {
        MCH_PacketVehiclePlayerControl pc = new MCH_PacketVehiclePlayerControl();
        boolean send = false;
        send = this.commonPlayerControl(player, vehicle, isPilot, pc);
        if (this.KeyExtra.isKeyDown()) {
            if (vehicle.getTowChainEntity() != null) {
                MCH_ClientVehicleTickHandler.playSoundOK();
                pc.unhitchChainId = W_Entity.getEntityId(vehicle.getTowChainEntity());
                send = true;
            } else {
                MCH_ClientVehicleTickHandler.playSoundNG();
            }
        }
        if (this.KeySwitchHovering.isKeyDown() || this.KeySwitchMode.isKeyDown()) {
            // empty if block
        }
        if (this.KeyZoom.isKeyDown()) {
            if (vehicle.canZoom()) {
                vehicle.zoomCamera();
                MCH_ClientVehicleTickHandler.playSound("zoom", 0.5f, 1.0f);
            } else if (vehicle.getAcInfo().haveHatch()) {
                if (vehicle.canFoldHatch()) {
                    pc.switchHatch = (byte)2;
                    send = true;
                } else if (vehicle.canUnfoldHatch()) {
                    pc.switchHatch = 1;
                    send = true;
                } else {
                    MCH_ClientVehicleTickHandler.playSoundNG();
                }
            }
        }
        if (send) {
            W_Network.sendToServer(pc);
        }
    }
}

