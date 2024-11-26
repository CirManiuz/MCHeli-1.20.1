/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.IThreadListener
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package mcheli;

import com.google.common.io.ByteArrayDataInput;
import mcheli.MCH_CommonPacketHandler;
import mcheli.MCH_Lib;
import mcheli.aircraft.MCH_AircraftPacketHandler;
import mcheli.block.MCH_DraftingTablePacketHandler;
import mcheli.command.MCH_CommandPacketHandler;
import mcheli.gltd.MCH_GLTDPacketHandler;
import mcheli.helicopter.MCH_HeliPacketHandler;
import mcheli.lweapon.MCH_LightWeaponPacketHandler;
import mcheli.multiplay.MCH_MultiplayPacketHandler;
import mcheli.plane.MCP_PlanePacketHandler;
import mcheli.tank.MCH_TankPacketHandler;
import mcheli.tool.MCH_ToolPacketHandler;
import mcheli.uav.MCH_UavPacketHandler;
import mcheli.vehicle.MCH_VehiclePacketHandler;
import mcheli.wrapper.W_PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MCH_PacketHandler
extends W_PacketHandler {
    @Override
    public void onPacket(ByteArrayDataInput data, EntityPlayer entityPlayer, MessageContext ctx) {
        int msgid = this.getMessageId(data);
        IThreadListener handler = FMLCommonHandler.instance().getWorldThread(ctx.netHandler);
        switch (msgid) {
            default: {
                MCH_Lib.DbgLog(entityPlayer.field_70170_p, "MCH_PacketHandler.onPacket invalid MSGID=0x%X(%d)", msgid, msgid);
                break;
            }
            case 0x10000810: {
                MCH_CommonPacketHandler.onPacketEffectExplosion(entityPlayer, data, handler);
                break;
            }
            case 0x20000820: {
                MCH_CommonPacketHandler.onPacketIndOpenScreen(entityPlayer, data, handler);
                break;
            }
            case 268437568: {
                MCH_CommonPacketHandler.onPacketNotifyServerSettings(entityPlayer, data, handler);
                break;
            }
            case 0x20000C00: {
                MCH_CommonPacketHandler.onPacketNotifyLock(entityPlayer, data, handler);
                break;
            }
            case 0x20000880: {
                MCH_MultiplayPacketHandler.onPacket_Command(entityPlayer, data, handler);
                break;
            }
            case 0x10000901: {
                MCH_MultiplayPacketHandler.onPacket_NotifySpotedEntity(entityPlayer, data, handler);
                break;
            }
            case 268437762: {
                MCH_MultiplayPacketHandler.onPacket_NotifyMarkPoint(entityPlayer, data, handler);
                break;
            }
            case 0x20000A00: {
                MCH_MultiplayPacketHandler.onPacket_LargeData(entityPlayer, data, handler);
                break;
            }
            case 536873473: {
                MCH_MultiplayPacketHandler.onPacket_ModList(entityPlayer, data, handler);
                break;
            }
            case 0x10000A10: {
                MCH_MultiplayPacketHandler.onPacket_IndClient(entityPlayer, data, handler);
                break;
            }
            case 0x10000B00: {
                MCH_CommandPacketHandler.onPacketTitle(entityPlayer, data, handler);
                break;
            }
            case 536873729: {
                MCH_CommandPacketHandler.onPacketSave(entityPlayer, data, handler);
                break;
            }
            case 0x20000900: {
                MCH_ToolPacketHandler.onPacket_IndSpotEntity(entityPlayer, data, handler);
                break;
            }
            case 0x20002010: {
                MCH_HeliPacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
                break;
            }
            case 536875104: {
                MCH_AircraftPacketHandler.onPacketStatusRequest(entityPlayer, data, handler);
                break;
            }
            case 0x10001061: {
                MCH_AircraftPacketHandler.onPacketStatusResponse(entityPlayer, data, handler);
                break;
            }
            case 0x20001010: {
                MCH_AircraftPacketHandler.onPacketSeatListRequest(entityPlayer, data, handler);
                break;
            }
            case 0x10001011: {
                MCH_AircraftPacketHandler.onPacketSeatListResponse(entityPlayer, data, handler);
                break;
            }
            case 0x20001020: {
                MCH_AircraftPacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
                break;
            }
            case 0x10001030: {
                MCH_AircraftPacketHandler.onPacketNotifyTVMissileEntity(entityPlayer, data, handler);
                break;
            }
            case 536875072: {
                MCH_AircraftPacketHandler.onPacket_ClientSetting(entityPlayer, data, handler);
                break;
            }
            case 0x10001050: {
                MCH_AircraftPacketHandler.onPacketOnMountEntity(entityPlayer, data, handler);
                break;
            }
            case 0x10001031: {
                MCH_AircraftPacketHandler.onPacketNotifyWeaponID(entityPlayer, data, handler);
                break;
            }
            case 268439602: {
                MCH_AircraftPacketHandler.onPacketNotifyHitBullet(entityPlayer, data, handler);
                break;
            }
            case 536875059: {
                MCH_AircraftPacketHandler.onPacketIndReload(entityPlayer, data, handler);
                break;
            }
            case 536875062: {
                MCH_AircraftPacketHandler.onPacketIndRotation(entityPlayer, data, handler);
                break;
            }
            case 536875063: {
                MCH_AircraftPacketHandler.onPacketNotifyInfoReloaded(entityPlayer, data, handler);
                break;
            }
            case 268439604: {
                MCH_AircraftPacketHandler.onPacketNotifyAmmoNum(entityPlayer, data, handler);
                break;
            }
            case 536875061: {
                MCH_AircraftPacketHandler.onPacketIndNotifyAmmoNum(entityPlayer, data, handler);
                break;
            }
            case 536887312: {
                MCH_GLTDPacketHandler.onPacket_GLTDPlayerControl(entityPlayer, data, handler);
                break;
            }
            case 536903696: {
                MCP_PlanePacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
                break;
            }
            case 0x20100010: {
                MCH_TankPacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
                break;
            }
            case 0x20010010: {
                MCH_LightWeaponPacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
                break;
            }
            case 0x20020010: {
                MCH_VehiclePacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
                break;
            }
            case 537133072: {
                MCH_UavPacketHandler.onPacketUavStatus(entityPlayer, data, handler);
                break;
            }
            case 537395216: {
                MCH_DraftingTablePacketHandler.onPacketCreate(entityPlayer, data, handler);
            }
        }
    }

    protected int getMessageId(ByteArrayDataInput data) {
        try {
            return data.readInt();
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}

