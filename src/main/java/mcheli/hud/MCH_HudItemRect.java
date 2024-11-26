/*
 * Decompiled with CFR 0.152.
 */
package mcheli.hud;

import mcheli.hud.MCH_HudItem;

public class MCH_HudItemRect
extends MCH_HudItem {
    private final String left;
    private final String top;
    private final String width;
    private final String height;

    public MCH_HudItemRect(int fileLine, String left, String top, String width, String height) {
        super(fileLine);
        this.left = MCH_HudItemRect.toFormula(left);
        this.top = MCH_HudItemRect.toFormula(top);
        this.width = MCH_HudItemRect.toFormula(width);
        this.height = MCH_HudItemRect.toFormula(height);
    }

    @Override
    public void execute() {
        double x2 = centerX + MCH_HudItemRect.calc(this.left);
        double y2 = centerY + MCH_HudItemRect.calc(this.top);
        double x1 = x2 + (double)((int)MCH_HudItemRect.calc(this.width));
        double y1 = y2 + (double)((int)MCH_HudItemRect.calc(this.height));
        MCH_HudItemRect.drawRect(x1, y1, x2, y2, colorSetting);
    }
}

