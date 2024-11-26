/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package mcheli.multiplay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import mcheli.multiplay.MCH_ContainerScoreboard;
import mcheli.multiplay.MCH_GuiScoreboard_Base;
import mcheli.multiplay.MCH_GuiScoreboard_CreateTeam;
import mcheli.multiplay.MCH_GuiScoreboard_Main;
import mcheli.multiplay.MCH_IGuiScoreboard;
import mcheli.wrapper.W_GuiButton;
import mcheli.wrapper.W_GuiContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class MCH_GuiScoreboard
extends W_GuiContainer
implements MCH_IGuiScoreboard {
    public final EntityPlayer thePlayer;
    private MCH_GuiScoreboard_Base.SCREEN_ID screenID;
    private Map<MCH_GuiScoreboard_Base.SCREEN_ID, MCH_GuiScoreboard_Base> listScreen;
    private int lastTeamNum = 0;

    public MCH_GuiScoreboard(EntityPlayer player) {
        super(new MCH_ContainerScoreboard(player));
        this.thePlayer = player;
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        super.func_73866_w_();
        this.field_146292_n.clear();
        this.field_146293_o.clear();
        this.field_147003_i = 0;
        this.field_147009_r = 0;
        this.listScreen = new HashMap<MCH_GuiScoreboard_Base.SCREEN_ID, MCH_GuiScoreboard_Base>();
        this.listScreen.put(MCH_GuiScoreboard_Base.SCREEN_ID.MAIN, new MCH_GuiScoreboard_Main(this, this.thePlayer));
        this.listScreen.put(MCH_GuiScoreboard_Base.SCREEN_ID.CREATE_TEAM, new MCH_GuiScoreboard_CreateTeam(this, this.thePlayer));
        for (MCH_GuiScoreboard_Base s : this.listScreen.values()) {
            s.initGui(this.field_146292_n, (GuiScreen)this);
        }
        this.lastTeamNum = this.field_146297_k.field_71441_e.func_96441_U().func_96525_g().size();
        this.switchScreen(MCH_GuiScoreboard_Base.SCREEN_ID.MAIN);
    }

    public void func_73876_c() {
        super.func_73876_c();
        int nowTeamNum = this.field_146297_k.field_71441_e.func_96441_U().func_96525_g().size();
        if (this.lastTeamNum != nowTeamNum) {
            this.lastTeamNum = nowTeamNum;
            this.func_73866_w_();
        }
        for (MCH_GuiScoreboard_Base s : this.listScreen.values()) {
            try {
                s.updateScreenButtons(this.field_146292_n);
                s.func_73876_c();
            }
            catch (Exception exception) {}
        }
    }

    @Override
    public void switchScreen(MCH_GuiScoreboard_Base.SCREEN_ID id) {
        for (MCH_GuiScoreboard_Base b : this.listScreen.values()) {
            b.leaveScreen();
        }
        this.screenID = id;
        this.getCurrentScreen().onSwitchScreen();
    }

    private MCH_GuiScoreboard_Base getCurrentScreen() {
        return this.listScreen.get((Object)this.screenID);
    }

    public static void setVisible(Object g, boolean v) {
        if (g instanceof GuiButton) {
            ((GuiButton)g).field_146125_m = v;
        }
        if (g instanceof GuiTextField) {
            ((GuiTextField)g).func_146189_e(v);
        }
    }

    protected void func_73869_a(char c, int code) throws IOException {
        this.getCurrentScreen().keyTypedScreen(c, code);
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        try {
            for (MCH_GuiScoreboard_Base s : this.listScreen.values()) {
                s.mouseClickedScreen(mouseX, mouseY, mouseButton);
            }
            super.func_73864_a(mouseX, mouseY, mouseButton);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    protected void func_146284_a(GuiButton btn) throws IOException {
        if (btn != null && btn.field_146124_l) {
            this.getCurrentScreen().actionPerformedScreen(btn);
        }
    }

    public void func_146276_q_() {
    }

    public void func_146278_c(int tint) {
        GL11.glDisable((int)2896);
        GL11.glDisable((int)2912);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    protected void func_146979_b(int x, int y) {
        this.getCurrentScreen().drawGuiContainerForegroundLayerScreen(x, y);
        for (Object o : this.field_146292_n) {
            W_GuiButton btn;
            if (!(o instanceof W_GuiButton) || !(btn = (W_GuiButton)((Object)o)).isOnMouseOver() || btn.hoverStringList == null) continue;
            this.drawHoveringText(btn.hoverStringList, x, y, this.field_146289_q);
            break;
        }
    }

    public static void drawList(Minecraft mc, FontRenderer fontRendererObj, boolean mng) {
        MCH_GuiScoreboard_Base.drawList(mc, fontRendererObj, mng);
    }

    protected void func_146976_a(float par1, int par2, int par3) {
        this.getCurrentScreen().func_146976_a(par1, par2, par3);
    }

    public void func_146280_a(Minecraft mc, int width, int height) {
        super.func_146280_a(mc, width, height);
        for (MCH_GuiScoreboard_Base s : this.listScreen.values()) {
            s.func_146280_a(mc, width, height);
        }
    }
}

