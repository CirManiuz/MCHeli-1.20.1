/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.ProgressManager
 *  net.minecraftforge.fml.common.ProgressManager$ProgressBar
 */
package mcheli.__helper.client;

import net.minecraftforge.fml.common.ProgressManager;

public class ProgressHolder {
    private static ProgressManager.ProgressBar currentBar;

    public static void push(String title, int steps) {
        currentBar = ProgressManager.push((String)title, (int)steps);
    }

    public static void step(String message) {
        currentBar.step(message);
    }

    public static void pop() {
        ProgressManager.pop((ProgressManager.ProgressBar)currentBar);
        currentBar = null;
    }
}

