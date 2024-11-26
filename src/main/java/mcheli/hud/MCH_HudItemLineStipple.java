/*
 * Decompiled with CFR 0.152.
 */
package mcheli.hud;

import mcheli.hud.MCH_HudItem;

public class MCH_HudItemLineStipple
extends MCH_HudItem {
    private final String pat;
    private final String fac;
    private final String[] pos;

    public MCH_HudItemLineStipple(int fileLine, String[] position) {
        super(fileLine);
        this.pat = position[0];
        this.fac = position[1];
        this.pos = new String[position.length - 2];
        for (int i = 0; i < position.length - 2; ++i) {
            this.pos[i] = MCH_HudItemLineStipple.toFormula(position[2 + i]);
        }
    }

    @Override
    public void execute() {
        double[] lines = new double[this.pos.length];
        for (int i = 0; i < lines.length; i += 2) {
            lines[i + 0] = centerX + MCH_HudItemLineStipple.calc(this.pos[i + 0]);
            lines[i + 1] = centerY + MCH_HudItemLineStipple.calc(this.pos[i + 1]);
        }
        this.drawLineStipple(lines, colorSetting, (int)MCH_HudItemLineStipple.calc(this.fac), (int)MCH_HudItemLineStipple.calc(this.pat));
    }
}

