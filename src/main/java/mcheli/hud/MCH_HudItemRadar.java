/*
 * Decompiled with CFR 0.152.
 */
package mcheli.hud;

import java.util.ArrayList;
import mcheli.MCH_Lib;
import mcheli.MCH_Vector2;
import mcheli.hud.MCH_HudItem;

public class MCH_HudItemRadar
extends MCH_HudItem {
    private final String rot;
    private final String left;
    private final String top;
    private final String width;
    private final String height;
    private final boolean isEntityRadar;

    public MCH_HudItemRadar(int fileLine, boolean isEntityRadar, String rot, String left, String top, String width, String height) {
        super(fileLine);
        this.isEntityRadar = isEntityRadar;
        this.rot = MCH_HudItemRadar.toFormula(rot);
        this.left = MCH_HudItemRadar.toFormula(left);
        this.top = MCH_HudItemRadar.toFormula(top);
        this.width = MCH_HudItemRadar.toFormula(width);
        this.height = MCH_HudItemRadar.toFormula(height);
    }

    @Override
    public void execute() {
        if (this.isEntityRadar) {
            if (EntityList != null && EntityList.size() > 0) {
                this.drawEntityList(EntityList, (float)MCH_HudItemRadar.calc(this.rot), centerX + MCH_HudItemRadar.calc(this.left), centerY + MCH_HudItemRadar.calc(this.top), MCH_HudItemRadar.calc(this.width), MCH_HudItemRadar.calc(this.height));
            }
        } else if (EnemyList != null && EnemyList.size() > 0) {
            this.drawEntityList(EnemyList, (float)MCH_HudItemRadar.calc(this.rot), centerX + MCH_HudItemRadar.calc(this.left), centerY + MCH_HudItemRadar.calc(this.top), MCH_HudItemRadar.calc(this.width), MCH_HudItemRadar.calc(this.height));
        }
    }

    protected void drawEntityList(ArrayList<MCH_Vector2> src, float r, double left, double top, double w, double h) {
        double w1 = -w / 2.0;
        double w2 = w / 2.0;
        double h1 = -h / 2.0;
        double h2 = h / 2.0;
        double w_factor = w / 64.0;
        double h_factor = h / 64.0;
        double[] list = new double[src.size() * 2];
        int idx = 0;
        for (MCH_Vector2 v : src) {
            list[idx + 0] = v.x / 2.0 * w_factor;
            list[idx + 1] = v.y / 2.0 * h_factor;
            idx += 2;
        }
        MCH_Lib.rotatePoints(list, r);
        ArrayList<Double> drawList = new ArrayList<Double>();
        int i = 0;
        while (i + 1 < list.length) {
            if (list[i + 0] > w1 && list[i + 0] < w2 && list[i + 1] > h1 && list[i + 1] < h2) {
                drawList.add(list[i + 0] + left + w / 2.0);
                drawList.add(list[i + 1] + top + h / 2.0);
            }
            i += 2;
        }
        this.drawPoints(drawList, colorSetting, scaleFactor * 2);
    }
}

