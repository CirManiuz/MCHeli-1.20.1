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
package mcheli.vehicle;

import com.google.common.io.ByteArrayDataInput;
import mcheli.__helper.network.HandleSide;
import mcheli.chain.MCH_EntityChain;
import mcheli.vehicle.MCH_EntityVehicle;
import mcheli.vehicle.MCH_PacketVehiclePlayerControl;
import mcheli.weapon.MCH_WeaponParam;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_VehiclePacketHandler {
    @HandleSide(value={Side.SERVER})
    public static void onPacket_PlayerControl(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!(player.func_184187_bx() instanceof MCH_EntityVehicle)) {
            return;
        }
        if (player.field_70170_p.field_72995_K) {
            return;
        }
        MCH_PacketVehiclePlayerControl pc = new MCH_PacketVehiclePlayerControl();
        pc.readData(data);
        scheduler.func_152344_a(() -> {
            MCH_EntityVehicle vehicle = (MCH_EntityVehicle)player.func_184187_bx();
            if (pc.isUnmount == 1) {
                vehicle.unmountEntity();
            } else if (pc.isUnmount == 2) {
                vehicle.unmountCrew();
            } else {
                Entity e;
                if (pc.switchSearchLight) {
                    vehicle.setSearchLight(!vehicle.isSearchLightON());
                }
                if (pc.switchCameraMode > 0) {
                    vehicle.switchCameraMode(player, pc.switchCameraMode - 1);
                }
                if (pc.switchWeapon >= 0) {
                    vehicle.switchWeapon((Entity)player, pc.switchWeapon);
                }
                if (pc.useWeapon) {
                    MCH_WeaponParam prm = new MCH_WeaponParam();
                    prm.entity = vehicle;
                    prm.user = player;
                    prm.setPosAndRot(pc.useWeaponPosX, pc.useWeaponPosY, pc.useWeaponPosZ, 0.0f, 0.0f);
                    prm.option1 = pc.useWeaponOption1;
                    prm.option2 = pc.useWeaponOption2;
                    vehicle.useCurrentWeapon(prm);
                }
                if (vehicle.isPilot((Entity)player)) {
                    vehicle.throttleUp = pc.throttleUp;
                    vehicle.throttleDown = pc.throttleDown;
                    vehicle.moveLeft = pc.moveLeft;
                    vehicle.moveRight = pc.moveRight;
                }
                if (pc.useFlareType > 0) {
                    vehicle.useFlare(pc.useFlareType);
                }
                if (pc.unhitchChainId >= 0 && (e = player.field_70170_p.func_73045_a(pc.unhitchChainId)) instanceof MCH_EntityChain) {
                    e.func_70106_y();
                }
                if (pc.openGui) {
                    vehicle.openGui(player);
                }
                if (pc.switchHatch > 0) {
                    vehicle.foldHatch(pc.switchHatch == 2);
                }
                if (pc.isUnmount == 3) {
                    vehicle.unmountAircraft();
                }
                if (pc.switchGunnerStatus) {
                    vehicle.setGunnerStatus(!vehicle.getGunnerStatus());
                }
            }
        });
    }
}

