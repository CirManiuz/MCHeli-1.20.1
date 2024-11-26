/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.IThreadListener
 *  net.minecraftforge.fml.relauncher.Side
 */
package mcheli.tank;

import com.google.common.io.ByteArrayDataInput;
import mcheli.__helper.network.HandleSide;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.tank.MCH_EntityTank;
import mcheli.tank.MCH_TankPacketPlayerControl;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.weapon.MCH_WeaponParam;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_TankPacketHandler {
    @HandleSide(value={Side.SERVER})
    public static void onPacket_PlayerControl(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.field_70170_p.field_72995_K) {
            return;
        }
        MCH_TankPacketPlayerControl pc = new MCH_TankPacketPlayerControl();
        pc.readData(data);
        scheduler.func_152344_a(() -> {
            MCH_EntityUavStation uavStation;
            MCH_EntityTank tank = null;
            if (player.func_184187_bx() instanceof MCH_EntityTank) {
                tank = (MCH_EntityTank)player.func_184187_bx();
            } else if (player.func_184187_bx() instanceof MCH_EntitySeat) {
                if (((MCH_EntitySeat)player.func_184187_bx()).getParent() instanceof MCH_EntityTank) {
                    tank = (MCH_EntityTank)((MCH_EntitySeat)player.func_184187_bx()).getParent();
                }
            } else if (player.func_184187_bx() instanceof MCH_EntityUavStation && (uavStation = (MCH_EntityUavStation)player.func_184187_bx()).getControlAircract() instanceof MCH_EntityTank) {
                tank = (MCH_EntityTank)uavStation.getControlAircract();
            }
            if (tank == null) {
                return;
            }
            MCH_EntityTank ac = tank;
            if (pc.isUnmount == 1) {
                ac.unmountEntity();
            } else if (pc.isUnmount == 2) {
                ac.unmountCrew();
            } else if (pc.ejectSeat) {
                ac.ejectSeat((Entity)player);
            } else {
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
                    prm.entity = tank;
                    prm.user = player;
                    prm.setPosAndRot(pc.useWeaponPosX, pc.useWeaponPosY, pc.useWeaponPosZ, 0.0f, 0.0f);
                    prm.option1 = pc.useWeaponOption1;
                    prm.option2 = pc.useWeaponOption2;
                    ac.useCurrentWeapon(prm);
                }
                if (ac.isPilot((Entity)player)) {
                    ac.throttleUp = pc.throttleUp;
                    ac.throttleDown = pc.throttleDown;
                    double dx = ac.field_70165_t - ac.field_70169_q;
                    double dz = ac.field_70161_v - ac.field_70166_s;
                    double dist = dx * dx + dz * dz;
                    if (pc.useBrake && ac.getCurrentThrottle() <= 0.03 && dist < 0.01) {
                        ac.moveLeft = false;
                        ac.moveRight = false;
                    }
                    ac.setBrake(pc.useBrake);
                }
                if (pc.useFlareType > 0) {
                    ac.useFlare(pc.useFlareType);
                }
                if (pc.openGui) {
                    ac.openGui(player);
                }
                if (pc.switchHatch > 0 && ac.getAcInfo().haveHatch()) {
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

