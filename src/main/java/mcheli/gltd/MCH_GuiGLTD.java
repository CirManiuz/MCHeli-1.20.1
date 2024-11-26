/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.gltd;

import mcheli.MCH_Camera;
import mcheli.MCH_Config;
import mcheli.MCH_KeyName;
import mcheli.gltd.MCH_EntityGLTD;
import mcheli.gui.MCH_Gui;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_GuiGLTD
extends MCH_Gui {
    public MCH_GuiGLTD(Minecraft minecraft) {
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
        return player.func_184187_bx() != null && player.func_184187_bx() instanceof MCH_EntityGLTD;
    }

    @Override
    public void drawGui(EntityPlayer player, boolean isThirdPersonView) {
        if (isThirdPersonView && !MCH_Config.DisplayHUDThirdPerson.prmBool) {
            return;
        }
        GL11.glLineWidth((float)scaleFactor);
        if (!this.isDrawGui(player)) {
            return;
        }
        MCH_EntityGLTD gltd = (MCH_EntityGLTD)player.func_184187_bx();
        if (gltd.camera.getMode(0) == 1) {
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
        this.drawString(String.format("x%.1f", Float.valueOf(gltd.camera.getCameraZoom())), this.centerX - 70, this.centerY + 10, -805306369);
        this.drawString(gltd.weaponCAS.getName(), this.centerX - 200, this.centerY + 65, gltd.countWait == 0 ? -819986657 : -807468024);
        this.drawCommonPosition(gltd, -819986657);
        this.drawString(gltd.camera.getModeName(0), this.centerX + 30, this.centerY - 50, -819986657);
        this.drawSight(gltd.camera, -819986657);
        this.drawTargetPosition(gltd, -819986657, -807468024);
        this.drawKeyBind(gltd.camera, -805306369, -813727873);
    }

    public void drawKeyBind(MCH_Camera camera, int color, int colorCannotUse) {
        int OffX = this.centerX + 55;
        int OffY = this.centerY + 40;
        this.drawString("DISMOUNT :", OffX, OffY + 0, color);
        this.drawString("CAM MODE :", OffX, OffY + 10, color);
        this.drawString("ZOOM IN   :", OffX, OffY + 20, camera.getCameraZoom() < 10.0f ? color : colorCannotUse);
        this.drawString("ZOOM OUT :", OffX, OffY + 30, camera.getCameraZoom() > 1.0f ? color : colorCannotUse);
        this.drawString(MCH_KeyName.getDescOrName(42) + " or " + MCH_KeyName.getDescOrName(MCH_Config.KeyUnmount.prmInt), OffX += 60, OffY + 0, color);
        this.drawString(MCH_KeyName.getDescOrName(MCH_Config.KeyCameraMode.prmInt), OffX, OffY + 10, color);
        this.drawString(MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt), OffX, OffY + 20, camera.getCameraZoom() < 10.0f ? color : colorCannotUse);
        this.drawString(MCH_KeyName.getDescOrName(MCH_Config.KeySwWeaponMode.prmInt), OffX, OffY + 30, camera.getCameraZoom() > 1.0f ? color : colorCannotUse);
    }

    public void drawCommonPosition(MCH_EntityGLTD gltd, int color) {
        Entity riddenByEntity = gltd.getRiddenByEntity();
        this.drawString(String.format("X: %+.1f", gltd.field_70165_t), this.centerX - 145, this.centerY + 0, color);
        this.drawString(String.format("Y: %+.1f", gltd.field_70163_u), this.centerX - 145, this.centerY + 10, color);
        this.drawString(String.format("Z: %+.1f", gltd.field_70161_v), this.centerX - 145, this.centerY + 20, color);
        this.drawString(String.format("AX: %+.1f", Float.valueOf(riddenByEntity.field_70177_z)), this.centerX - 145, this.centerY + 40, color);
        this.drawString(String.format("AY: %+.1f", Float.valueOf(riddenByEntity.field_70125_A)), this.centerX - 145, this.centerY + 50, color);
    }

    public void drawTargetPosition(MCH_EntityGLTD gltd, int color, int colorDanger) {
        Vec3d dst;
        Entity riddenByEntity = gltd.getRiddenByEntity();
        if (riddenByEntity == null) {
            return;
        }
        World w = riddenByEntity.field_70170_p;
        float yaw = riddenByEntity.field_70177_z;
        float pitch = riddenByEntity.field_70125_A;
        double tX = -MathHelper.func_76126_a((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI));
        double tZ = MathHelper.func_76134_b((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI));
        double tY = -MathHelper.func_76126_a((float)(pitch / 180.0f * (float)Math.PI));
        double dist = MathHelper.func_76133_a((double)(tX * tX + tY * tY + tZ * tZ));
        tX = tX * 80.0 / dist;
        tY = tY * 80.0 / dist;
        tZ = tZ * 80.0 / dist;
        MCH_Camera c = gltd.camera;
        Vec3d src = W_WorldFunc.getWorldVec3(w, c.posX, c.posY, c.posZ);
        RayTraceResult m = W_WorldFunc.clip(w, src, dst = W_WorldFunc.getWorldVec3(w, c.posX + tX, c.posY + tY, c.posZ + tZ));
        if (m != null) {
            this.drawString(String.format("X: %+.2fm", m.field_72307_f.field_72450_a), this.centerX + 50, this.centerY - 5 - 15, color);
            this.drawString(String.format("Y: %+.2fm", m.field_72307_f.field_72448_b), this.centerX + 50, this.centerY - 5, color);
            this.drawString(String.format("Z: %+.2fm", m.field_72307_f.field_72449_c), this.centerX + 50, this.centerY - 5 + 15, color);
            double x = m.field_72307_f.field_72450_a - c.posX;
            double y = m.field_72307_f.field_72448_b - c.posY;
            double z = m.field_72307_f.field_72449_c - c.posZ;
            double len = Math.sqrt(x * x + y * y + z * z);
            this.drawCenteredString(String.format("[%.2fm]", len), this.centerX, this.centerY + 30, len > 20.0 ? color : colorDanger);
        } else {
            this.drawString("X: --.--m", this.centerX + 50, this.centerY - 5 - 15, color);
            this.drawString("Y: --.--m", this.centerX + 50, this.centerY - 5, color);
            this.drawString("Z: --.--m", this.centerX + 50, this.centerY - 5 + 15, color);
            this.drawCenteredString("[--.--m]", this.centerX, this.centerY + 30, colorDanger);
        }
    }

    private void drawSight(MCH_Camera camera, int color) {
        double posX = this.centerX;
        double posY = this.centerY;
        double[] line2 = new double[]{posX - 30.0, posY - 10.0, posX - 30.0, posY - 20.0, posX - 30.0, posY - 20.0, posX - 10.0, posY - 20.0, posX - 30.0, posY + 10.0, posX - 30.0, posY + 20.0, posX - 30.0, posY + 20.0, posX - 10.0, posY + 20.0, posX + 30.0, posY - 10.0, posX + 30.0, posY - 20.0, posX + 30.0, posY - 20.0, posX + 10.0, posY - 20.0, posX + 30.0, posY + 10.0, posX + 30.0, posY + 20.0, posX + 30.0, posY + 20.0, posX + 10.0, posY + 20.0};
        this.drawLine(line2, color);
    }
}

