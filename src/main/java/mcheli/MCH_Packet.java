/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 */
package mcheli;

import com.google.common.io.ByteArrayDataInput;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.wrapper.W_PacketBase;

public abstract class MCH_Packet
extends W_PacketBase {
    public static final int MSGID_INVALID = 0;
    public static final int MSGID_SERVER = 0x10000000;
    public static final int MSGID_CLIENT = 0x20000000;
    public static final int BLKID_COMMON = 2048;
    public static final int BLKID_AIRCRAFT = 4096;
    public static final int BLKID_HELI = 8192;
    public static final int BLKID_GLTD = 16384;
    public static final int BLKID_PLANE = 32768;
    public static final int BLKID_LW = 65536;
    public static final int BLKID_VEHICLE = 131072;
    public static final int BLKID_UAV = 262144;
    public static final int BLKID_DTABLE = 524288;
    public static final int BLKID_TANK = 0x100000;
    public static final int MSGID_EFFECT_EXPLOSION = 0x10000810;
    public static final int MSGID_IND_OPEN_SCREEN = 0x20000820;
    public static final int MSGID_NOTIFY_SERVER_SETTINGS = 268437568;
    public static final int MSGID_IND_MULTIPLAY_COMMAND = 0x20000880;
    public static final int MSGID_IND_SPOT_ENTITY = 0x20000900;
    public static final int MSGID_NOTIFY_SPOTED_ENTITY = 0x10000901;
    public static final int MSGID_NOTIFY_MARK_POINT = 268437762;
    public static final int MSGID_LARGE_DATA = 0x20000A00;
    public static final int MSGID_MOD_LIST = 536873473;
    public static final int MSGID_IND_CLIENT = 0x10000A10;
    public static final int MSGID_COMMAND_TITLE = 0x10000B00;
    public static final int MSGID_COMMAND_SAVE = 536873729;
    public static final int MSGID_NOTIFY_LOCK = 0x20000C00;
    public static final int MSGID_SEAT_LIST_REQUEST = 0x20001010;
    public static final int MSGID_SEAT_LIST_RESPONSE = 0x10001011;
    public static final int MSGID_SEAT_PLAYER_CONTROL = 0x20001020;
    public static final int MSGID_NOTIFY_TVMISSILE = 0x10001030;
    public static final int MSGID_NOTIFY_WEAPON_ID = 0x10001031;
    public static final int MSGID_NOTIFY_HIT_BULLET = 268439602;
    public static final int MSGID_IND_RELOAD = 536875059;
    public static final int MSGID_NOTIFY_AMMO_NUM = 268439604;
    public static final int MSGID_IND_NOTIFY_AMMO_NUM = 536875061;
    public static final int MSGID_IND_ROTATION = 536875062;
    public static final int MSGID_NOTIFY_INFO_RELOADED = 536875063;
    public static final int MSGID_NOTIFY_CLIENT_SETTING = 536875072;
    public static final int MSGID_NOTIFY_MOUNT = 0x10001050;
    public static final int MSGID_STATUS_REQUEST = 536875104;
    public static final int MSGID_STATUS_RESPONSE = 0x10001061;
    public static final int MSGID_HELI_PLAYER_CONTROL = 0x20002010;
    public static final int MSGID_HELI_REQUEST_STATUS = 0x20002020;
    public static final int MSGID_HELI_NOTIFY_SYNC_STATUS = 268443696;
    public static final int MSGID_GLTD_PLAYER_CONTROL = 536887312;
    public static final int MSGID_PLANE_PLAYER_CONTROL = 536903696;
    public static final int MSGID_TANK_PLAYER_CONTROL = 0x20100010;
    public static final int MSGID_LW_PLAYER_CONTROL = 0x20010010;
    public static final int MSGID_VEHICLE_PLAYER_CONTROL = 0x20020010;
    public static final int MSGID_UAV_STATUS = 537133072;
    public static final int MSGID_DTABLE_CREATE = 537395216;

    public MCH_Packet() {
    }

    public MCH_Packet(ByteArrayDataInput data) {
        this.readData(data);
    }

    public byte setBit(byte data, int bit, boolean b) {
        return (byte)(data | (b ? 1 : 0) << bit);
    }

    public short setBit(short data, int bit, boolean b) {
        return (short)(data | (b ? 1 : 0) << bit);
    }

    public boolean getBit(byte data, int bit) {
        return (data >> bit & 1) != 0;
    }

    public boolean getBit(short data, int bit) {
        return (data >> bit & 1) != 0;
    }

    public abstract void readData(ByteArrayDataInput var1);

    public abstract void writeData(DataOutputStream var1);

    public abstract int getMessageID();

    @Override
    public byte[] createData() {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(data);
        try {
            dos.writeInt(this.getMessageID());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.writeData(dos);
        return data.toByteArray();
    }
}

