/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.lweapon;

import mcheli.MCH_Config;
import mcheli.MCH_KeyName;
import mcheli.MCH_Lib;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.gltd.MCH_EntityGLTD;
import mcheli.gui.MCH_Gui;
import mcheli.lweapon.MCH_ClientLightWeaponTickHandler;
import mcheli.lweapon.MCH_ItemLightWeaponBase;
import mcheli.weapon.MCH_WeaponGuidanceSystem;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_GuiLightWeapon
extends MCH_Gui {
    public MCH_GuiLightWeapon(Minecraft minecraft) {
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
        Entity re;
        return MCH_ItemLightWeaponBase.isHeld(player) && !((re = player.func_184187_bx()) instanceof MCH_EntityAircraft) && !(re instanceof MCH_EntityGLTD);
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
        MCH_WeaponGuidanceSystem gs = MCH_ClientLightWeaponTickHandler.gs;
        if (gs != null && MCH_ClientLightWeaponTickHandler.weapon != null && MCH_ClientLightWeaponTickHandler.weapon.getInfo() != null) {
            boolean canFire;
            PotionEffect pe = player.func_70660_b(MobEffects.field_76439_r);
            if (pe != null) {
                this.drawNightVisionNoise();
            }
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            int srcBlend = GL11.glGetInteger((int)3041);
            int dstBlend = GL11.glGetInteger((int)3040);
            GL11.glBlendFunc((int)770, (int)771);
            double dist = 0.0;
            if (gs.getTargetEntity() != null) {
                double dx = gs.getTargetEntity().field_70165_t - player.field_70165_t;
                double dz = gs.getTargetEntity().field_70161_v - player.field_70161_v;
                dist = Math.sqrt(dx * dx + dz * dz);
            }
            boolean bl = canFire = MCH_ClientLightWeaponTickHandler.weaponMode == 0 || dist >= 40.0 || gs.getLockCount() <= 0;
            if ("fgm148".equalsIgnoreCase(MCH_ItemLightWeaponBase.getName(player.func_184614_ca()))) {
                this.drawGuiFGM148(player, gs, canFire, player.func_184614_ca());
                this.drawKeyBind(-805306369, true);
            } else {
                double size;
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                W_McClient.MOD_bindTexture("textures/gui/stinger.png");
                for (size = 512.0; size < (double)this.field_146294_l || size < (double)this.field_146295_m; size *= 2.0) {
                }
                this.drawTexturedModalRectRotate(-(size - (double)this.field_146294_l) / 2.0, -(size - (double)this.field_146295_m) / 2.0 - 20.0, size, size, 0.0, 0.0, 256.0, 256.0, 0.0f);
                this.drawKeyBind(-805306369, false);
            }
            GL11.glBlendFunc((int)srcBlend, (int)dstBlend);
            GL11.glDisable((int)3042);
            this.drawLock(-14101432, -2161656, gs.getLockCount(), gs.getLockCountMax());
            this.drawRange(player, gs, canFire, -14101432, -2161656);
        }
    }

    public void drawNightVisionNoise() {
        GL11.glEnable((int)3042);
        GL11.glColor4f((float)0.0f, (float)1.0f, (float)0.0f, (float)0.3f);
        int srcBlend = GL11.glGetInteger((int)3041);
        int dstBlend = GL11.glGetInteger((int)3040);
        GL11.glBlendFunc((int)1, (int)1);
        W_McClient.MOD_bindTexture("textures/gui/alpha.png");
        this.drawTexturedModalRectRotate(0.0, 0.0, this.field_146294_l, this.field_146295_m, this.rand.nextInt(256), this.rand.nextInt(256), 256.0, 256.0, 0.0f);
        GL11.glBlendFunc((int)srcBlend, (int)dstBlend);
        GL11.glDisable((int)3042);
    }

    void drawLock(int color, int colorLock, int cntLock, int cntMax) {
        int posX = this.centerX;
        int posY = this.centerY + 20;
        MCH_GuiLightWeapon.func_73734_a((int)(posX - 20), (int)(posY + 20 + 1), (int)(posX - 20 + 40), (int)(posY + 20 + 1 + 1 + 3 + 1), (int)color);
        float lock = (float)cntLock / (float)cntMax;
        MCH_GuiLightWeapon.func_73734_a((int)(posX - 20 + 1), (int)(posY + 20 + 1 + 1), (int)(posX - 20 + 1 + (int)(38.0 * (double)lock)), (int)(posY + 20 + 1 + 1 + 3), (int)-2161656);
    }

    void drawRange(EntityPlayer player, MCH_WeaponGuidanceSystem gs, boolean canFire, int color1, int color2) {
        Entity target;
        String msgLockDist = "[--.--]";
        int color = color2;
        if (gs.getLockCount() > 0 && (target = gs.getLockingEntity()) != null) {
            double dx = target.field_70165_t - player.field_70165_t;
            double dz = target.field_70161_v - player.field_70161_v;
            msgLockDist = String.format("[%.2f]", Math.sqrt(dx * dx + dz * dz));
            int n = color = canFire ? color1 : color2;
            if (!MCH_Config.HideKeybind.prmBool && gs.isLockComplete()) {
                String k = MCH_KeyName.getDescOrName(MCH_Config.KeyAttack.prmInt);
                this.drawCenteredString("Shot : " + k, this.centerX, this.centerY + 65, -805306369);
            }
        }
        this.drawCenteredString(msgLockDist, this.centerX, this.centerY + 50, color);
    }

    void drawGuiFGM148(EntityPlayer player, MCH_WeaponGuidanceSystem gs, boolean canFire, ItemStack itemStack) {
        double h;
        double w;
        double y;
        double x;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        double fac = (double)this.field_146294_l / 800.0 < (double)this.field_146295_m / 700.0 ? (double)this.field_146294_l / 800.0 : (double)this.field_146295_m / 700.0;
        int size = (int)(1024.0 * fac);
        size = size / 64 * 64;
        fac = (double)size / 1024.0;
        double left = -(size - this.field_146294_l) / 2;
        double top = -(size - this.field_146295_m) / 2 - 20;
        double right = left + (double)size;
        double bottom = top + (double)size;
        Vec3d pos = MCH_ClientLightWeaponTickHandler.getMartEntityPos();
        if (gs.getLockCount() > 0) {
            int scale;
            int n = scale = scaleFactor > 0 ? scaleFactor : 2;
            if (pos == null) {
                pos = new Vec3d((double)(this.field_146294_l / 2 * scale), (double)(this.field_146295_m / 2 * scale), 0.0);
            }
            double IX = 280.0 * fac;
            double IY = 370.0 * fac;
            double cx = pos.field_72450_a / (double)scale;
            double cy = (double)this.field_146295_m - pos.field_72448_b / (double)scale;
            double sx = MCH_Lib.RNG(cx, left + IX, right - IX);
            double sy = MCH_Lib.RNG(cy, top + IY, bottom - IY);
            if (gs.getLockCount() >= gs.getLockCountMax() / 2) {
                this.drawLine(new double[]{-1.0, sy, this.field_146294_l + 1, sy, sx, -1.0, sx, this.field_146295_m + 1}, -1593835521);
            }
            if (player.field_70173_aa % 6 >= 3) {
                pos = MCH_ClientLightWeaponTickHandler.getMartEntityBBPos();
                if (pos == null) {
                    pos = new Vec3d((double)((this.field_146294_l / 2 - 65) * scale), (double)((this.field_146295_m / 2 + 50) * scale), 0.0);
                }
                double bx = pos.field_72450_a / (double)scale;
                double by = (double)this.field_146295_m - pos.field_72448_b / (double)scale;
                double dx = Math.abs(cx - bx);
                double dy = Math.abs(cy - by);
                double p = 1.0 - (double)gs.getLockCount() / (double)gs.getLockCountMax();
                dx = MCH_Lib.RNG(dx, 25.0, 70.0);
                dy = MCH_Lib.RNG(dy, 15.0, 70.0);
                dx += (70.0 - dx) * p;
                dy += (70.0 - dy) * p;
                int lx = 10;
                int ly = 6;
                this.drawLine(new double[]{sx - dx, sy - dy + (double)ly, sx - dx, sy - dy, sx - dx + (double)lx, sy - dy}, -1593835521, 3);
                this.drawLine(new double[]{sx + dx, sy - dy + (double)ly, sx + dx, sy - dy, sx + dx - (double)lx, sy - dy}, -1593835521, 3);
                this.drawLine(new double[]{sx - dx, sy + (dy /= 6.0) - (double)ly, sx - dx, sy + dy, sx - dx + (double)lx, sy + dy}, -1593835521, 3);
                this.drawLine(new double[]{sx + dx, sy + dy - (double)ly, sx + dx, sy + dy, sx + dx - (double)lx, sy + dy}, -1593835521, 3);
            }
        }
        MCH_GuiLightWeapon.func_73734_a((int)-1, (int)-1, (int)((int)left + 1), (int)(this.field_146295_m + 1), (int)-16777216);
        MCH_GuiLightWeapon.func_73734_a((int)((int)right - 1), (int)-1, (int)(this.field_146294_l + 1), (int)(this.field_146295_m + 1), (int)-16777216);
        MCH_GuiLightWeapon.func_73734_a((int)-1, (int)-1, (int)(this.field_146294_l + 1), (int)((int)top + 1), (int)-16777216);
        MCH_GuiLightWeapon.func_73734_a((int)-1, (int)((int)bottom - 1), (int)(this.field_146294_l + 1), (int)(this.field_146295_m + 1), (int)-16777216);
        GL11.glEnable((int)3042);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        W_McClient.MOD_bindTexture("textures/gui/javelin.png");
        this.drawTexturedModalRectRotate(left, top, size, size, 0.0, 0.0, 256.0, 256.0, 0.0f);
        W_McClient.MOD_bindTexture("textures/gui/javelin2.png");
        PotionEffect pe = player.func_70660_b(MobEffects.field_76439_r);
        if (pe == null) {
            x = 247.0;
            y = 211.0;
            w = 380.0;
            h = 350.0;
            this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0, 1024.0);
        }
        if (player.func_184612_cw() <= 60) {
            x = 130.0;
            y = 334.0;
            w = 257.0;
            h = 455.0;
            this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0, 1024.0);
        }
        if (MCH_ClientLightWeaponTickHandler.selectedZoom == 0) {
            x = 387.0;
            y = 211.0;
            w = 510.0;
            h = 350.0;
            this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0, 1024.0);
        }
        if (MCH_ClientLightWeaponTickHandler.selectedZoom == MCH_ClientLightWeaponTickHandler.weapon.getInfo().zoom.length - 1) {
            x = 511.0;
            y = 211.0;
            w = 645.0;
            h = 350.0;
            this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0, 1024.0);
        }
        if (gs.getLockCount() > 0) {
            x = 643.0;
            y = 211.0;
            w = 775.0;
            h = 350.0;
            this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0, 1024.0);
        }
        if (MCH_ClientLightWeaponTickHandler.weaponMode == 1) {
            x = 768.0;
            y = 340.0;
            w = 890.0;
            h = 455.0;
            this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0, 1024.0);
        } else {
            x = 768.0;
            y = 456.0;
            w = 890.0;
            h = 565.0;
            this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0, 1024.0);
        }
        if (!canFire) {
            x = 379.0;
            y = 670.0;
            w = 511.0;
            h = 810.0;
            this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0, 1024.0);
        }
        if (itemStack.func_77960_j() >= itemStack.func_77958_k()) {
            x = 512.0;
            y = 670.0;
            w = 645.0;
            h = 810.0;
            this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0, 1024.0);
        }
        if (gs.getLockCount() < gs.getLockCountMax()) {
            x = 646.0;
            y = 670.0;
            w = 776.0;
            h = 810.0;
            this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0, 1024.0);
        }
        if (pe != null) {
            x = 768.0;
            y = 562.0;
            w = 890.0;
            h = 694.0;
            this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0, 1024.0);
        }
    }

    public void drawKeyBind(int color, boolean canSwitchMode) {
        int OffX = this.centerX + 55;
        int OffY = this.centerY + 40;
        this.drawString("CAM MODE :", OffX, OffY + 10, color);
        this.drawString("ZOOM      :", OffX, OffY + 20, color);
        if (canSwitchMode) {
            this.drawString("MODE      :", OffX, OffY + 30, color);
        }
        this.drawString(MCH_KeyName.getDescOrName(MCH_Config.KeyCameraMode.prmInt), OffX += 60, OffY + 10, color);
        this.drawString(MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt), OffX, OffY + 20, color);
        if (canSwitchMode) {
            this.drawString(MCH_KeyName.getDescOrName(MCH_Config.KeySwWeaponMode.prmInt), OffX, OffY + 30, color);
        }
    }
}

