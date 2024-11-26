/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package mcheli.hud;

import javax.annotation.Nullable;
import mcheli.__helper.info.ContentRegistries;
import mcheli.hud.MCH_Hud;

public class MCH_HudManager {
    private MCH_HudManager() {
    }

    @Nullable
    public static MCH_Hud get(String name) {
        return ContentRegistries.hud().get(name);
    }

    public static boolean contains(String name) {
        return ContentRegistries.hud().contains(name);
    }

    public static int size() {
        return ContentRegistries.hud().size();
    }
}

