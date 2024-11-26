/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 *  org.lwjgl.input.Keyboard
 */
package mcheli.debug._v3;

import java.util.Arrays;
import mcheli.__helper.MCH_Utils;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class V3Debugger {
    public static final Stater TOGGLE_I = new Stater(24);
    public static final Numeric NUM_X = new Numeric("TX", 203, 205, 0.0625);
    public static final Numeric NUM_Y = new Numeric("TY", 200, 208, 0.0625);
    public static final Numeric ROT_X = new Numeric("RX", 36, 37, 5.0);
    public static final Numeric ROT_Y = new Numeric("RY", 49, 50, 5.0);
    public static final Numeric ROT_Z = new Numeric("RZ", 22, 23, 5.0);
    private static boolean tickOnce;

    static void onClient(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Arrays.fill(KeyStater.tickChunk, false);
            tickOnce = false;
        }
    }

    public static boolean checkTick() {
        if (!tickOnce) {
            tickOnce = true;
            return true;
        }
        return false;
    }

    static void info(Object o) {
        MCH_Utils.logger().info(o);
    }

    public static class Numeric {
        final KeyStater incKey;
        final KeyStater decKey;
        String name;
        double num;
        final double dif;

        public Numeric(String name, int decKey, int incKey, double dif) {
            this.name = name;
            this.decKey = new KeyStater(decKey);
            this.incKey = new KeyStater(incKey);
            this.dif = dif;
        }

        public double value() {
            if (this.incKey.press()) {
                this.num += this.dif;
                V3Debugger.info("Num " + this.name + ".value : " + this.num);
            }
            if (this.decKey.press()) {
                this.num -= this.dif;
                V3Debugger.info("Num " + this.name + ".value : " + this.num);
            }
            return this.num;
        }

        public float valueFloat() {
            return (float)this.value();
        }

        public int valueInt() {
            return (int)this.value();
        }
    }

    public static class Stater {
        final KeyStater key;
        boolean state;

        public Stater(int key) {
            this.key = new KeyStater(key);
        }

        public boolean state() {
            if (this.key.press()) {
                this.state = !this.state;
                V3Debugger.info("Key " + this.key.keydown() + ".state : " + this.state);
            }
            return this.state;
        }
    }

    static class KeyStater {
        static boolean[] tickChunk = new boolean[256];
        final int key;
        private boolean down;
        private boolean chunk;

        public KeyStater(int key) {
            this.key = key;
        }

        public boolean press() {
            if (!tickChunk[this.key]) {
                boolean flag = this.keydown();
                boolean flag1 = this.chunk;
                this.chunk = flag;
                this.down = flag && !flag1;
                KeyStater.tickChunk[this.key] = true;
                return this.down;
            }
            return false;
        }

        public boolean keydown() {
            return Keyboard.isKeyDown((int)this.key);
        }

        public String keyname() {
            return Keyboard.getKeyName((int)this.key);
        }
    }
}

