/*
 * Decompiled with CFR 0.152.
 */
package mcheli.hud;

import mcheli.hud.MCH_HudItem;

public class MCH_HudItemExit
extends MCH_HudItem {
    public MCH_HudItemExit(int fileLine) {
        super(fileLine);
    }

    @Override
    public void execute() {
        this.parent.exit = true;
    }
}

