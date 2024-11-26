/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.tool.rangefinder;

import mcheli.MCH_Config;
import mcheli.MCH_KeyName;
import mcheli.MCH_Lib;
import mcheli.gui.MCH_Gui;
import mcheli.tool.rangefinder.MCH_ItemRangeFinder;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_GuiRangeFinder
extends MCH_Gui {
    public MCH_GuiRangeFinder(Minecraft minecraft) {
        super(minecraft);
    }

    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
    }

    @Override
    public boolean func_73868_f() {
        return false;
    }

    @Override
    public boolean isDrawGui(EntityPlayer player) {
        return MCH_ItemRangeFinder.canUse(player);
    }

    @Override
    public void drawGui(EntityPlayer player, boolean isThirdPersonView) {
        if (isThirdPersonView) {
            return;
        }
        GL11.glLineWidth((float)scaleFactor);
        if (!this.isDrawGui(player)) {
            return;
        }
        GL11.glDisable((int)3042);
        if (MCH_ItemRangeFinder.isUsingScope(player)) {
            this.drawRF(player);
        }
    }

    void drawRF(EntityPlayer player) {
        double size;
        GL11.glEnable((int)3042);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        int srcBlend = GL11.glGetInteger((int)3041);
        int dstBlend = GL11.glGetInteger((int)3040);
        GL11.glBlendFunc((int)770, (int)771);
        W_McClient.MOD_bindTexture("textures/gui/rangefinder.png");
        for (size = 512.0; size < (double)this.field_146294_l || size < (double)this.field_146295_m; size *= 2.0) {
        }
        this.drawTexturedModalRectRotate(-(size - (double)this.field_146294_l) / 2.0, -(size - (double)this.field_146295_m) / 2.0, size, size, 0.0, 0.0, 256.0, 256.0, 0.0f);
        GL11.glBlendFunc((int)srcBlend, (int)dstBlend);
        GL11.glDisable((int)3042);
        double factor = size / 512.0;
        double SCALE_FACTOR = (double)scaleFactor * factor;
        double CX = this.field_146297_k.field_71443_c / 2;
        double CY = this.field_146297_k.field_71440_d / 2;
        double px = (CX - 80.0 * SCALE_FACTOR) / SCALE_FACTOR;
        double py = (CY + 55.0 * SCALE_FACTOR) / SCALE_FACTOR;
        GL11.glPushMatrix();
        GL11.glScaled((double)factor, (double)factor, (double)factor);
        ItemStack item = player.func_184614_ca();
        int damage = (int)((double)(item.func_77958_k() - item.func_77960_j()) / (double)item.func_77958_k() * 100.0);
        this.drawDigit(String.format("%3d", damage), (int)px, (int)py, 13, damage > 0 ? -15663328 : -61424);
        if (damage <= 0) {
            this.drawString("Please craft", (int)px + 40, (int)py + 0, -65536);
            this.drawString("redstone", (int)px + 40, (int)py + 10, -65536);
        }
        px = (CX - 20.0 * SCALE_FACTOR) / SCALE_FACTOR;
        if (damage > 0) {
            Vec3d vs = new Vec3d(player.field_70165_t, player.field_70163_u + (double)player.func_70047_e(), player.field_70161_v);
            Vec3d ve = MCH_Lib.Rot2Vec3(player.field_70177_z, player.field_70125_A);
            ve = vs.func_72441_c(ve.field_72450_a * 300.0, ve.field_72448_b * 300.0, ve.field_72449_c * 300.0);
            RayTraceResult mop = player.field_70170_p.func_72901_a(vs, ve, true);
            if (mop != null && mop.field_72313_a != RayTraceResult.Type.MISS) {
                int range = (int)player.func_70011_f(mop.field_72307_f.field_72450_a, mop.field_72307_f.field_72448_b, mop.field_72307_f.field_72449_c);
                this.drawDigit(String.format("%4d", range), (int)px, (int)py, 13, -15663328);
            } else {
                this.drawDigit(String.format("----", new Object[0]), (int)px, (int)py, 13, -61424);
            }
        }
        MCH_GuiRangeFinder.func_73734_a((int)((int)(px -= 80.0)), (int)((int)(py -= 4.0)), (int)((int)px + 30), (int)((int)py + 2), (int)-15663328);
        MCH_GuiRangeFinder.func_73734_a((int)((int)px), (int)((int)py), (int)((int)px + MCH_ItemRangeFinder.rangeFinderUseCooldown / 2), (int)((int)py + 2), (int)-61424);
        this.drawString(String.format("x%.1f", Float.valueOf(MCH_ItemRangeFinder.zoom)), (int)px, (int)py - 20, -1);
        int mode = MCH_ItemRangeFinder.mode;
        this.drawString(">", (int)(px += 130.0), (int)py - 30 + mode * 10, -1);
        this.drawString("Players/Vehicles", (int)(px += 10.0), (int)py - 30, mode == 0 ? -1 : -12566464);
        this.drawString("Monsters/Mobs", (int)px, (int)py - 20, mode == 1 ? -1 : -12566464);
        this.drawString("Mark Point", (int)px, (int)py - 10, mode == 2 ? -1 : -12566464);
        GL11.glPopMatrix();
        px = (CX - 160.0 * SCALE_FACTOR) / (double)scaleFactor;
        py = (CY - 100.0 * SCALE_FACTOR) / (double)scaleFactor;
        if (px < 10.0) {
            px = 10.0;
        }
        if (py < 10.0) {
            py = 10.0;
        }
        String s = "Spot      : " + MCH_KeyName.getDescOrName(MCH_Config.KeyAttack.prmInt);
        this.drawString(s, (int)px, (int)py + 0, -1);
        s = "Zoom in   : " + MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt);
        this.drawString(s, (int)px, (int)py + 10, MCH_ItemRangeFinder.zoom < 10.0f ? -1 : -12566464);
        s = "Zoom out : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwWeaponMode.prmInt);
        this.drawString(s, (int)px, (int)py + 20, MCH_ItemRangeFinder.zoom > 1.2f ? -1 : -12566464);
        s = "Mode      : " + MCH_KeyName.getDescOrName(MCH_Config.KeyFlare.prmInt);
        this.drawString(s, (int)px, (int)py + 30, -1);
    }
}

