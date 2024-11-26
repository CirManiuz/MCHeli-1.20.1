/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.util.IThreadListener
 *  net.minecraftforge.fml.relauncher.Side
 */
package mcheli.helicopter;

import com.google.common.io.ByteArrayDataInput;
import mcheli.MCH_Lib;
import mcheli.__helper.MCH_CriteriaTriggers;
import mcheli.__helper.network.HandleSide;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.chain.MCH_EntityChain;
import mcheli.container.MCH_EntityContainer;
import mcheli.helicopter.MCH_EntityHeli;
import mcheli.helicopter.MCH_HeliPacketPlayerControl;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.weapon.MCH_WeaponParam;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_HeliPacketHandler {
    @HandleSide(value={Side.SERVER})
    public static void onPacket_PlayerControl(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.field_70170_p.field_72995_K) {
            return;
        }
        MCH_HeliPacketPlayerControl pc = new MCH_HeliPacketPlayerControl();
        pc.readData(data);
        scheduler.func_152344_a(() -> {
            MCH_EntityUavStation uavStation;
            MCH_EntityHeli heli = null;
            if (player.func_184187_bx() instanceof MCH_EntityHeli) {
                heli = (MCH_EntityHeli)player.func_184187_bx();
            } else if (player.func_184187_bx() instanceof MCH_EntitySeat) {
                if (((MCH_EntitySeat)player.func_184187_bx()).getParent() instanceof MCH_EntityHeli) {
                    heli = (MCH_EntityHeli)((MCH_EntitySeat)player.func_184187_bx()).getParent();
                }
            } else if (player.func_184187_bx() instanceof MCH_EntityUavStation && (uavStation = (MCH_EntityUavStation)player.func_184187_bx()).getControlAircract() instanceof MCH_EntityHeli) {
                heli = (MCH_EntityHeli)uavStation.getControlAircract();
            }
            if (heli == null) {
                return;
            }
            MCH_EntityHeli ac = heli;
            if (pc.isUnmount == 1) {
                ac.unmountEntity();
            } else if (pc.isUnmount == 2) {
                ac.unmountCrew();
            } else {
                Entity e;
                if (pc.switchFold == 0) {
                    heli.setFoldBladeStat((byte)3);
                }
                if (pc.switchFold == 1) {
                    heli.setFoldBladeStat((byte)1);
                }
                if (pc.switchMode == 0) {
                    ac.switchGunnerMode(false);
                }
                if (pc.switchMode == 1) {
                    ac.switchGunnerMode(true);
                }
                if (pc.switchMode == 2) {
                    ac.switchHoveringMode(false);
                }
                if (pc.switchMode == 3) {
                    ac.switchHoveringMode(true);
                }
                if (pc.switchSearchLight) {
                    ac.setSearchLight(!ac.isSearchLightON());
                }
                if (pc.switchCameraMode > 0) {
                    ac.switchCameraMode(player, pc.switchCameraMode - 1);
                }
                if (pc.switchWeapon >= 0) {
                    ac.switchWeapon((Entity)player, pc.switchWeapon);
                }
                if (pc.useWeapon) {
                    MCH_WeaponParam prm = new MCH_WeaponParam();
                    prm.entity = ac;
                    prm.user = player;
                    prm.setPosAndRot(pc.useWeaponPosX, pc.useWeaponPosY, pc.useWeaponPosZ, 0.0f, 0.0f);
                    prm.option1 = pc.useWeaponOption1;
                    prm.option2 = pc.useWeaponOption2;
                    ac.useCurrentWeapon(prm);
                }
                if (ac.isPilot((Entity)player)) {
                    ac.throttleUp = pc.throttleUp;
                    ac.throttleDown = pc.throttleDown;
                    ac.moveLeft = pc.moveLeft;
                    ac.moveRight = pc.moveRight;
                }
                if (pc.useFlareType > 0) {
                    ac.useFlare(pc.useFlareType);
                }
                if (pc.unhitchChainId >= 0 && (e = player.field_70170_p.func_73045_a(pc.unhitchChainId)) instanceof MCH_EntityChain) {
                    if (((MCH_EntityChain)e).towedEntity instanceof MCH_EntityContainer && MCH_Lib.getBlockIdY(heli, 3, -20) == 0 && player instanceof EntityPlayerMP) {
                        MCH_CriteriaTriggers.RELIEF_SUPPLIES.trigger((EntityPlayerMP)player);
                    }
                    e.func_70106_y();
                }
                if (pc.openGui) {
                    ac.openGui(player);
                }
                if (pc.switchHatch > 0) {
                    ac.foldHatch(pc.switchHatch == 2);
                }
                if (pc.switchFreeLook > 0) {
                    ac.switchFreeLookMode(pc.switchFreeLook == 1);
                }
                if (pc.switchGear == 1) {
                    ac.foldLandingGear();
                }
                if (pc.switchGear == 2) {
                    ac.unfoldLandingGear();
                }
                if (pc.putDownRack == 1) {
                    ac.mountEntityToRack();
                }
                if (pc.putDownRack == 2) {
                    ac.unmountEntityFromRack();
                }
                if (pc.putDownRack == 3) {
                    ac.rideRack();
                }
                if (pc.isUnmount == 3) {
                    ac.unmountAircraft();
                }
                if (pc.switchGunnerStatus) {
                    ac.setGunnerStatus(!ac.getGunnerStatus());
                }
            }
        });
    }
}

