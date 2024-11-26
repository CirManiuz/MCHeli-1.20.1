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
package mcheli.gltd;

import com.google.common.io.ByteArrayDataInput;
import mcheli.__helper.network.HandleSide;
import mcheli.gltd.MCH_EntityGLTD;
import mcheli.gltd.MCH_PacketGLTDPlayerControl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_GLTDPacketHandler {
    @HandleSide(value={Side.SERVER})
    public static void onPacket_GLTDPlayerControl(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!(player.func_184187_bx() instanceof MCH_EntityGLTD)) {
            return;
        }
        if (player.field_70170_p.field_72995_K) {
            return;
        }
        MCH_PacketGLTDPlayerControl pc = new MCH_PacketGLTDPlayerControl();
        pc.readData(data);
        scheduler.func_152344_a(() -> {
            MCH_EntityGLTD gltd = (MCH_EntityGLTD)player.func_184187_bx();
            if (pc.unmount) {
                Entity riddenByEntity = gltd.getRiddenByEntity();
                if (riddenByEntity != null) {
                    riddenByEntity.func_184210_p();
                }
            } else {
                if (pc.switchCameraMode >= 0) {
                    gltd.camera.setMode(0, pc.switchCameraMode);
                }
                if (pc.switchWeapon >= 0) {
                    gltd.switchWeapon(pc.switchWeapon);
                }
                if (pc.useWeapon) {
                    gltd.useCurrentWeapon(pc.useWeaponOption1, pc.useWeaponOption2);
                }
            }
        });
    }
}

