/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.tool;

import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.gui.MCH_Gui;
import mcheli.tool.MCH_ItemWrench;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_GuiWrench
extends MCH_Gui {
    public MCH_GuiWrench(Minecraft minecraft) {
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
        return player != null && player.field_70170_p != null && !player.func_184614_ca().func_190926_b() && player.func_184614_ca().func_77973_b() instanceof MCH_ItemWrench;
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
        MCH_EntityAircraft ac = ((MCH_ItemWrench)player.func_184614_ca().func_77973_b()).getMouseOverAircraft((EntityLivingBase)player);
        if (ac != null && ac.getMaxHP() > 0) {
            int color = (double)((float)ac.getHP() / (float)ac.getMaxHP()) > 0.3 ? -14101432 : -2161656;
            this.drawHP(color, -15433180, ac.getHP(), ac.getMaxHP());
        }
    }

    void drawHP(int color, int colorBG, int hp, int hpmax) {
        int posX = this.centerX;
        int posY = this.centerY + 20;
        MCH_GuiWrench.func_73734_a((int)(posX - 20), (int)(posY + 20 + 1), (int)(posX - 20 + 40), (int)(posY + 20 + 1 + 1 + 3 + 1), (int)colorBG);
        if (hp > hpmax) {
            hp = hpmax;
        }
        float hpp = (float)hp / (float)hpmax;
        MCH_GuiWrench.func_73734_a((int)(posX - 20 + 1), (int)(posY + 20 + 1 + 1), (int)(posX - 20 + 1 + (int)(38.0 * (double)hpp)), (int)(posY + 20 + 1 + 1 + 3), (int)color);
        int hppn = (int)(hpp * 100.0f);
        if (hp < hpmax && hppn >= 100) {
            hppn = 99;
        }
        this.drawCenteredString(String.format("%d %%", hppn), posX, posY + 30, color);
    }
}

