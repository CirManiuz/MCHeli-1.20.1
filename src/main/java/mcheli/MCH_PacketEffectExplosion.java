/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.util.math.BlockPos
 */
package mcheli;

import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import mcheli.MCH_Packet;
import mcheli.wrapper.W_Network;
import net.minecraft.util.math.BlockPos;

public class MCH_PacketEffectExplosion
extends MCH_Packet {
    ExplosionParam prm = new ExplosionParam(this);

    @Override
    public int getMessageID() {
        return 0x10000810;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.prm.posX = data.readDouble();
            this.prm.posY = data.readDouble();
            this.prm.posZ = data.readDouble();
            this.prm.size = data.readFloat();
            this.prm.exploderID = data.readInt();
            this.prm.inWater = data.readByte() != 0;
            this.prm.readAffectedPositions(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeDouble(this.prm.posX);
            dos.writeDouble(this.prm.posY);
            dos.writeDouble(this.prm.posZ);
            dos.writeFloat(this.prm.size);
            dos.writeInt(this.prm.exploderID);
            dos.writeByte(this.prm.inWater ? 1 : 0);
            this.prm.writeAffectedPositions(dos);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ExplosionParam create() {
        return new MCH_PacketEffectExplosion().aaa();
    }

    private ExplosionParam aaa() {
        return new ExplosionParam(this);
    }

    public static void send(ExplosionParam param) {
        if (param != null) {
            MCH_PacketEffectExplosion s = new MCH_PacketEffectExplosion();
            s.prm = param;
            W_Network.sendToAllPlayers(s);
        }
    }

    public class ExplosionParam {
        public double posX;
        public double posY;
        public double posZ;
        public float size;
        public int exploderID;
        public boolean inWater;
        private List<BlockPos> affectedPositions;

        public ExplosionParam(MCH_PacketEffectExplosion paramMCH_PacketEffectExplosion) {
        }

        public void setAffectedPositions(List<BlockPos> affectedPositions) {
            this.affectedPositions = Lists.newArrayList(affectedPositions);
        }

        public List<BlockPos> getAffectedBlockPositions() {
            return this.affectedPositions;
        }

        void writeAffectedPositions(DataOutputStream dos) throws IOException {
            dos.writeInt(this.affectedPositions.size());
            for (BlockPos blockpos : this.affectedPositions) {
                dos.writeInt(blockpos.func_177958_n());
                dos.writeInt(blockpos.func_177956_o());
                dos.writeInt(blockpos.func_177952_p());
            }
        }

        void readAffectedPositions(ByteArrayDataInput data) {
            int i = data.readInt();
            this.affectedPositions = Lists.newArrayListWithCapacity((int)i);
            for (int i1 = 0; i1 < i; ++i1) {
                int j1 = data.readInt();
                int k1 = data.readInt();
                int l1 = data.readInt();
                this.affectedPositions.add(new BlockPos(j1, k1, l1));
            }
        }
    }
}

