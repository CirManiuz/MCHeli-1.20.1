/*
 * Decompiled with CFR 0.152.
 */
package mcheli.__helper.debug;

public class DebugTrace {
    public static void printOutTraceback() {
        new RuntimeException().printStackTrace(System.out);
    }
}

