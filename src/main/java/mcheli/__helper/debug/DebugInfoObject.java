/*
 * Decompiled with CFR 0.152.
 */
package mcheli.__helper.debug;

import mcheli.debug._v1.PrintStreamWrapper;

public interface DebugInfoObject {
    public void printInfo(PrintStreamWrapper var1);

    default public void printInfo() {
        this.printInfo(PrintStreamWrapper.create(System.out));
    }
}

