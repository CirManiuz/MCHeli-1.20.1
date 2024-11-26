/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package mcheli.hud;

import mcheli.MCH_Lib;
import mcheli.hud.MCH_HudItem;
import org.lwjgl.opengl.GL11;

public class MCH_HudItemGraduation
extends MCH_HudItem {
    private final String drawRot;
    private final String drawRoll;
    private final String drawPosX;
    private final String drawPosY;
    private final int type;

    public MCH_HudItemGraduation(int fileLine, int type, String rot, String roll, String posx, String posy) {
        super(fileLine);
        this.drawRot = MCH_HudItemGraduation.toFormula(rot);
        this.drawRoll = MCH_HudItemGraduation.toFormula(roll);
        this.drawPosX = MCH_HudItemGraduation.toFormula(posx);
        this.drawPosY = MCH_HudItemGraduation.toFormula(posy);
        this.type = type;
    }

    @Override
    public void execute() {
        GL11.glPushMatrix();
        int x = (int)(centerX + MCH_HudItemGraduation.calc(this.drawPosX));
        int y = (int)(centerY + MCH_HudItemGraduation.calc(this.drawPosY));
        GL11.glTranslated((double)x, (double)y, (double)0.0);
        GL11.glRotatef((float)((float)MCH_HudItemGraduation.calc(this.drawRoll)), (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glTranslated((double)(-x), (double)(-y), (double)0.0);
        if (this.type == 0) {
            this.drawCommonGraduationYaw(MCH_HudItemGraduation.calc(this.drawRot), colorSetting, x, y);
        } else if (this.type == 1) {
            this.drawCommonGraduationPitch1(MCH_HudItemGraduation.calc(this.drawRot), colorSetting, x, y);
        } else if (this.type == 2 || this.type == 3) {
            this.drawCommonGraduationPitch2(MCH_HudItemGraduation.calc(this.drawRot), colorSetting, x, y);
        }
        GL11.glPopMatrix();
    }

    private void drawCommonGraduationPitch2(double playerPitch, int color, int posX, int posY) {
        playerPitch = -playerPitch;
        int pitch_n = (int)playerPitch / 5 * 5;
        double[] line = new double[8];
        int start = this.type == 2 ? 0 : 1;
        int end = this.type == 2 ? 5 : 4;
        int INT = this.type == 2 ? 1 : 2;
        for (int i = start; i < end; ++i) {
            int pitch = -(-pitch_n - 10 + i * 5);
            double p_rest = playerPitch % 5.0;
            int x = pitch != 0 ? 50 : 100;
            int y = posY + (int)((double)(-60 * INT) + p_rest * 6.0 * (double)INT + (double)(i * 30 * INT));
            line[0] = posX - x;
            line[1] = y + (pitch > 0 ? 2 : (pitch == 0 ? 0 : -2));
            line[2] = posX - 50;
            line[3] = y;
            line[4] = posX + x;
            line[5] = line[1];
            line[6] = posX + 50;
            line[7] = y;
            this.drawLine(line, color);
            line[0] = posX - 50;
            line[1] = y;
            line[2] = posX - 30;
            line[3] = y;
            line[4] = posX + 50;
            line[5] = y;
            line[6] = posX + 30;
            line[7] = y;
            if (pitch >= 0) {
                this.drawLine(line, color);
            } else {
                this.drawLineStipple(line, color, 1, 52428);
            }
            if (pitch == 0) continue;
            this.drawCenteredString("" + pitch, posX - 50 - 10, y - 4, color);
            this.drawCenteredString("" + pitch, posX + 50 + 10, y - 4, color);
        }
    }

    private void drawCommonGraduationPitch1(double playerPitch, int color, int posX, int posY) {
        int pitch = (int)playerPitch % 360;
        int y = (int)(playerPitch * 10.0 % 10.0);
        if (y < 0) {
            y += 10;
        }
        int posX_L = posX - 100;
        int posX_R = posX + 100;
        int linePosY = posY;
        posY -= 80;
        double[] line = new double[144];
        int p = playerPitch >= 0.0 || y == 0 ? pitch - 8 : pitch - 9;
        int i = 0;
        while (i < line.length / 8) {
            int olx = p % 3 == 0 ? 15 : 5;
            int ilx = 0;
            line[i * 8 + 0] = posX_L - olx;
            line[i * 8 + 1] = posY + i * 10 - y;
            line[i * 8 + 2] = posX_L + ilx;
            line[i * 8 + 3] = posY + i * 10 - y;
            line[i * 8 + 4] = posX_R + olx;
            line[i * 8 + 5] = posY + i * 10 - y;
            line[i * 8 + 6] = posX_R - ilx;
            line[i * 8 + 7] = posY + i * 10 - y;
            ++i;
            ++p;
        }
        this.drawLine(line, color);
        double[] verticalLine = new double[]{posX_L - 25, linePosY - 90, posX_L, linePosY - 90, posX_L, linePosY + 90, posX_L - 25, linePosY + 90};
        this.drawLine(verticalLine, color, 3);
        verticalLine = new double[]{posX_R + 25, linePosY - 90, posX_R, linePosY - 90, posX_R, linePosY + 90, posX_R + 25, linePosY + 90};
        this.drawLine(verticalLine, color, 3);
    }

    private void drawCommonGraduationYaw(double playerYaw, int color, int posX, int posY) {
        double yaw = MCH_Lib.getRotate360(playerYaw);
        posX -= 90;
        double[] line = new double[76];
        int x = (int)(yaw * 10.0) % 10;
        int y = (int)yaw - 9;
        int i = 0;
        while (i < line.length / 4) {
            int azPosX = posX + i * 10 - x;
            line[i * 4 + 0] = azPosX;
            line[i * 4 + 1] = posY;
            line[i * 4 + 2] = azPosX;
            line[i * 4 + 3] = posY + (y % 3 == 0 ? 10 : (y % 45 == 0 ? 15 : 5));
            if (y % 45 == 0) {
                this.drawCenteredString(MCH_Lib.getAzimuthStr8(y), azPosX, posY - 10, -65536);
            } else if (y % 3 == 0) {
                int rot = y + 180;
                if (rot < 0) {
                    rot += 360;
                }
                if (rot > 360) {
                    rot -= 360;
                }
                this.drawCenteredString(String.format("%d", rot), azPosX, posY - 10, color);
            }
            ++i;
            ++y;
        }
        this.drawLine(line, color);
    }
}

