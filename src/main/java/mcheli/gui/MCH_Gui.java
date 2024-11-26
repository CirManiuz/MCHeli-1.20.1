/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.gui;

import java.util.ArrayList;
import java.util.Random;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public abstract class MCH_Gui
extends GuiScreen {
    protected int centerX = 0;
    protected int centerY = 0;
    protected Random rand = new Random();
    protected float smoothCamPartialTicks;
    public static int scaleFactor;

    public MCH_Gui(Minecraft minecraft) {
        this.field_146297_k = minecraft;
        this.smoothCamPartialTicks = 0.0f;
        this.field_73735_i = -110.0f;
    }

    public void func_73866_w_() {
        super.func_73866_w_();
    }

    public boolean func_73868_f() {
        return false;
    }

    public void onTick() {
    }

    public abstract boolean isDrawGui(EntityPlayer var1);

    public abstract void drawGui(EntityPlayer var1, boolean var2);

    public void func_73863_a(int par1, int par2, float partialTicks) {
        this.smoothCamPartialTicks = partialTicks;
        W_ScaledResolution scaledresolution = new W_ScaledResolution(this.field_146297_k, this.field_146297_k.field_71443_c, this.field_146297_k.field_71440_d);
        scaleFactor = scaledresolution.func_78325_e();
        if (!this.field_146297_k.field_71474_y.field_74319_N) {
            this.field_146294_l = this.field_146297_k.field_71443_c / scaleFactor;
            this.field_146295_m = this.field_146297_k.field_71440_d / scaleFactor;
            this.centerX = this.field_146294_l / 2;
            this.centerY = this.field_146295_m / 2;
            GL11.glPushMatrix();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            if (this.field_146297_k.field_71439_g != null) {
                this.drawGui((EntityPlayer)this.field_146297_k.field_71439_g, this.field_146297_k.field_71474_y.field_74320_O != 0);
            }
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPopMatrix();
        }
    }

    public void drawTexturedModalRectRotate(double left, double top, double width, double height, double uLeft, double vTop, double uWidth, double vHeight, float rot) {
        GL11.glPushMatrix();
        GL11.glTranslated((double)(left + width / 2.0), (double)(top + height / 2.0), (double)0.0);
        GL11.glRotatef((float)rot, (float)0.0f, (float)0.0f, (float)1.0f);
        float f = 0.00390625f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        builder.func_181662_b(-width / 2.0, height / 2.0, (double)this.field_73735_i).func_187315_a(uLeft * (double)f, (vTop + vHeight) * (double)f).func_181675_d();
        builder.func_181662_b(width / 2.0, height / 2.0, (double)this.field_73735_i).func_187315_a((uLeft + uWidth) * (double)f, (vTop + vHeight) * (double)f).func_181675_d();
        builder.func_181662_b(width / 2.0, -height / 2.0, (double)this.field_73735_i).func_187315_a((uLeft + uWidth) * (double)f, vTop * (double)f).func_181675_d();
        builder.func_181662_b(-width / 2.0, -height / 2.0, (double)this.field_73735_i).func_187315_a(uLeft * (double)f, vTop * (double)f).func_181675_d();
        tessellator.func_78381_a();
        GL11.glPopMatrix();
    }

    public void drawTexturedRect(double left, double top, double width, double height, double uLeft, double vTop, double uWidth, double vHeight, double textureWidth, double textureHeight) {
        float fx = (float)(1.0 / textureWidth);
        float fy = (float)(1.0 / textureHeight);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        builder.func_181662_b(left, top + height, (double)this.field_73735_i).func_187315_a(uLeft * (double)fx, (vTop + vHeight) * (double)fy).func_181675_d();
        builder.func_181662_b(left + width, top + height, (double)this.field_73735_i).func_187315_a((uLeft + uWidth) * (double)fx, (vTop + vHeight) * (double)fy).func_181675_d();
        builder.func_181662_b(left + width, top, (double)this.field_73735_i).func_187315_a((uLeft + uWidth) * (double)fx, vTop * (double)fy).func_181675_d();
        builder.func_181662_b(left, top, (double)this.field_73735_i).func_187315_a(uLeft * (double)fx, vTop * (double)fy).func_181675_d();
        tessellator.func_78381_a();
    }

    public void drawLineStipple(double[] line, int color, int factor, int pattern) {
        GL11.glEnable((int)2852);
        GL11.glLineStipple((int)factor, (short)((short)pattern));
        this.drawLine(line, color);
        GL11.glDisable((int)2852);
    }

    public void drawLine(double[] line, int color) {
        this.drawLine(line, color, 1);
    }

    public void drawString(String s, int x, int y, int color) {
        this.func_73731_b(this.field_146297_k.field_71466_p, s, x, y, color);
    }

    public void drawDigit(String s, int x, int y, int interval, int color) {
        GL11.glEnable((int)3042);
        GL11.glColor4ub((byte)((byte)(color >> 16 & 0xFF)), (byte)((byte)(color >> 8 & 0xFF)), (byte)((byte)(color & 0xFF)), (byte)((byte)(color >> 24 & 0xFF)));
        int srcBlend = GL11.glGetInteger((int)3041);
        int dstBlend = GL11.glGetInteger((int)3040);
        GL11.glBlendFunc((int)770, (int)771);
        W_McClient.MOD_bindTexture("textures/gui/digit.png");
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                this.func_73729_b(x + interval * i, y, 16 * (c - 48), 0, 16, 16);
            }
            if (c != '-') continue;
            this.func_73729_b(x + interval * i, y, 160, 0, 16, 16);
        }
        GL11.glBlendFunc((int)srcBlend, (int)dstBlend);
        GL11.glDisable((int)3042);
    }

    public void drawCenteredString(String s, int x, int y, int color) {
        this.func_73732_a(this.field_146297_k.field_71466_p, s, x, y, color);
    }

    public void drawLine(double[] line, int color, int mode) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4ub((byte)((byte)(color >> 16 & 0xFF)), (byte)((byte)(color >> 8 & 0xFF)), (byte)((byte)(color >> 0 & 0xFF)), (byte)((byte)(color >> 24 & 0xFF)));
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(mode, DefaultVertexFormats.field_181705_e);
        for (int i = 0; i < line.length; i += 2) {
            builder.func_181662_b(line[i + 0], line[i + 1], (double)this.field_73735_i).func_181675_d();
        }
        tessellator.func_78381_a();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
        GL11.glPopMatrix();
    }

    public void drawPoints(double[] points, int color, int pointWidth) {
        int prevWidth = GL11.glGetInteger((int)2833);
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4ub((byte)((byte)(color >> 16 & 0xFF)), (byte)((byte)(color >> 8 & 0xFF)), (byte)((byte)(color >> 0 & 0xFF)), (byte)((byte)(color >> 24 & 0xFF)));
        GL11.glPointSize((float)pointWidth);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(0, DefaultVertexFormats.field_181705_e);
        for (int i = 0; i < points.length; i += 2) {
            builder.func_181662_b(points[i], points[i + 1], 0.0).func_181675_d();
        }
        tessellator.func_78381_a();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
        GL11.glPointSize((float)prevWidth);
    }

    public void drawPoints(ArrayList<Double> points, int color, int pointWidth) {
        int prevWidth = GL11.glGetInteger((int)2833);
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4ub((byte)((byte)(color >> 16 & 0xFF)), (byte)((byte)(color >> 8 & 0xFF)), (byte)((byte)(color >> 0 & 0xFF)), (byte)((byte)(color >> 24 & 0xFF)));
        GL11.glPointSize((float)pointWidth);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(0, DefaultVertexFormats.field_181705_e);
        for (int i = 0; i < points.size(); i += 2) {
            builder.func_181662_b(points.get(i).doubleValue(), points.get(i + 1).doubleValue(), 0.0).func_181675_d();
        }
        tessellator.func_78381_a();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
        GL11.glPointSize((float)prevWidth);
    }
}

