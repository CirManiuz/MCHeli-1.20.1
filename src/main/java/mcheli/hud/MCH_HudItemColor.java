/*
 * Decompiled with CFR 0.152.
 */
package mcheli.hud;

import mcheli.hud.MCH_HudItem;

public class MCH_HudItemColor
extends MCH_HudItem {
    private final String updateColor;

    public MCH_HudItemColor(int fileLine, String newColor) {
        super(fileLine);
        this.updateColor = newColor;
    }

    public static MCH_HudItemColor createByParams(int fileLine, String[] prm) {
        if (prm.length == 1) {
            return new MCH_HudItemColor(fileLine, MCH_HudItemColor.toFormula(prm[0]));
        }
        if (prm.length == 4) {
            return new MCH_HudItemColor(fileLine, "((" + MCH_HudItemColor.toFormula(prm[0]) + ")<<24)|((" + MCH_HudItemColor.toFormula(prm[1]) + ")<<16)|((" + MCH_HudItemColor.toFormula(prm[2]) + ")<<8 )|((" + MCH_HudItemColor.toFormula(prm[3]) + ")<<0 )");
        }
        return null;
    }

    @Override
    public void execute() {
        double d = MCH_HudItemColor.calc(this.updateColor);
        long l = (long)d;
        MCH_HudItem.colorSetting = (int)l;
        MCH_HudItemColor.updateVarMapItem("color", MCH_HudItemColor.getColor());
    }
}

