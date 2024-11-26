/*
 * Decompiled with CFR 0.152.
 */
package mcheli;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_OStream
extends ByteArrayOutputStream {
    public int index = 0;
    public static final int SIZE = 30720;

    public void write(DataOutputStream dos) {
        try {
            int datasize = this.index + 30720 <= this.size() ? 30720 : this.size() - this.index;
            dos.writeInt(this.index);
            dos.writeInt(datasize);
            dos.writeInt(this.size());
            dos.write(this.buf, this.index, datasize);
            this.index += datasize;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isDataEnd() {
        return this.index >= this.size();
    }
}

