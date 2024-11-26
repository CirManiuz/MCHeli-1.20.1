/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package mcheli.hud;

import mcheli.hud.MCH_HudItem;
import mcheli.wrapper.W_TextureUtil;
import org.lwjgl.opengl.GL11;

public class MCH_HudItemTexture
extends MCH_HudItem {
    private final String name;
    private final String left;
    private final String top;
    private final String width;
    private final String height;
    private final String uLeft;
    private final String vTop;
    private final String uWidth;
    private final String vHeight;
    private final String rot;
    private int textureWidth;
    private int textureHeight;

    public MCH_HudItemTexture(int fileLine, String name, String left, String top, String width, String height, String uLeft, String vTop, String uWidth, String vHeight, String rot) {
        super(fileLine);
        this.name = name;
        this.left = MCH_HudItemTexture.toFormula(left);
        this.top = MCH_HudItemTexture.toFormula(top);
        this.width = MCH_HudItemTexture.toFormula(width);
        this.height = MCH_HudItemTexture.toFormula(height);
        this.uLeft = MCH_HudItemTexture.toFormula(uLeft);
        this.vTop = MCH_HudItemTexture.toFormula(vTop);
        this.uWidth = MCH_HudItemTexture.toFormula(uWidth);
        this.vHeight = MCH_HudItemTexture.toFormula(vHeight);
        this.rot = MCH_HudItemTexture.toFormula(rot);
        this.textureHeight = 0;
        this.textureWidth = 0;
    }

    @Override
    public void execute() {
        GL11.glEnable((int)3042);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if (this.textureWidth == 0 || this.textureHeight == 0) {
            int w = 0;
            int h = 0;
            W_TextureUtil.TextureParam prm = W_TextureUtil.getTextureInfo("mcheli", "textures/gui/" + this.name + ".png");
            if (prm != null) {
                w = prm.width;
                h = prm.height;
            }
            this.textureWidth = w > 0 ? w : 256;
            this.textureHeight = h > 0 ? h : 256;
        }
        this.drawTexture(this.name, centerX + MCH_HudItemTexture.calc(this.left), centerY + MCH_HudItemTexture.calc(this.top), MCH_HudItemTexture.calc(this.width), MCH_HudItemTexture.calc(this.height), MCH_HudItemTexture.calc(this.uLeft), MCH_HudItemTexture.calc(this.vTop), MCH_HudItemTexture.calc(this.uWidth), MCH_HudItemTexture.calc(this.vHeight), (float)MCH_HudItemTexture.calc(this.rot), this.textureWidth, this.textureHeight);
    }
}

