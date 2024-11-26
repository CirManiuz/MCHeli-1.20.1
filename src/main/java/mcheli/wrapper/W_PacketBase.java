/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  com.google.common.io.ByteStreams
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package mcheli.wrapper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class W_PacketBase
implements IMessage {
    ByteArrayDataInput data;

    public byte[] createData() {
        return null;
    }

    public void fromBytes(ByteBuf buf) {
        byte[] dst = new byte[buf.array().length - 1];
        buf.getBytes(0, dst);
        this.data = ByteStreams.newDataInput((byte[])dst);
    }

    public void toBytes(ByteBuf buf) {
        buf.writeBytes(this.createData());
    }
}

