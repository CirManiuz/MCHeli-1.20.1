/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.scoreboard.Score
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.util.text.TextFormatting
 *  org.lwjgl.opengl.GL11
 */
package mcheli.multiplay;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import mcheli.multiplay.MCH_ContainerScoreboard;
import mcheli.multiplay.MCH_IGuiScoreboard;
import mcheli.wrapper.W_GuiContainer;
import mcheli.wrapper.W_ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

public abstract class MCH_GuiScoreboard_Base
extends W_GuiContainer {
    public List<Gui> listGui;
    public static final int BUTTON_ID_SHUFFLE = 256;
    public static final int BUTTON_ID_CREATE_TEAM = 512;
    public static final int BUTTON_ID_CREATE_TEAM_OK = 528;
    public static final int BUTTON_ID_CREATE_TEAM_CANCEL = 544;
    public static final int BUTTON_ID_CREATE_TEAM_FF = 560;
    public static final int BUTTON_ID_CREATE_TEAM_NEXT_C = 576;
    public static final int BUTTON_ID_CREATE_TEAM_PREV_C = 577;
    public static final int BUTTON_ID_JUMP_SPAWN_POINT = 768;
    public static final int BUTTON_ID_SWITCH_PVP = 1024;
    public static final int BUTTON_ID_DESTORY_ALL = 1280;
    private MCH_IGuiScoreboard screen_switcher;

    public MCH_GuiScoreboard_Base(MCH_IGuiScoreboard switcher, EntityPlayer player) {
        super(new MCH_ContainerScoreboard(player));
        this.screen_switcher = switcher;
        this.field_146297_k = Minecraft.func_71410_x();
    }

    public void func_73866_w_() {
    }

    public void initGui(List<GuiButton> buttonList, GuiScreen parents) {
        this.listGui = new ArrayList<Gui>();
        this.field_146297_k = Minecraft.func_71410_x();
        this.field_146289_q = this.field_146297_k.field_71466_p;
        this.field_146294_l = parents.field_146294_l;
        this.field_146295_m = parents.field_146295_m;
        this.func_73866_w_();
        for (Gui b : this.listGui) {
            if (!(b instanceof GuiButton)) continue;
            buttonList.add((GuiButton)b);
        }
        this.field_146292_n.clear();
    }

    public static void setVisible(Object g, boolean v) {
        if (g instanceof GuiButton) {
            ((GuiButton)g).field_146125_m = v;
        }
        if (g instanceof GuiTextField) {
            ((GuiTextField)g).func_146189_e(v);
        }
    }

    public void updateScreenButtons(List<GuiButton> list) {
    }

    protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {
    }

    public int getTeamNum() {
        return this.field_146297_k.field_71441_e.func_96441_U().func_96525_g().size();
    }

    protected void acviveScreen() {
    }

    public void onSwitchScreen() {
        for (Gui b : this.listGui) {
            MCH_GuiScoreboard_Base.setVisible(b, true);
        }
        this.acviveScreen();
    }

    public void leaveScreen() {
        for (Gui b : this.listGui) {
            MCH_GuiScoreboard_Base.setVisible(b, false);
        }
    }

    public void keyTypedScreen(char c, int code) throws IOException {
        this.func_73869_a(c, code);
    }

    public void mouseClickedScreen(int mouseX, int mouseY, int mouseButton) throws IOException {
        block3: {
            try {
                this.func_73864_a(mouseX, mouseY, mouseButton);
            }
            catch (Exception e) {
                if (mouseButton != 0) break block3;
                for (int l = 0; l < this.field_146292_n.size(); ++l) {
                    GuiButton guibutton = (GuiButton)this.field_146292_n.get(l);
                    if (!guibutton.func_146116_c(this.field_146297_k, mouseX, mouseY)) continue;
                    guibutton.func_146113_a(this.field_146297_k.func_147118_V());
                    this.func_146284_a(guibutton);
                }
            }
        }
    }

    public void drawGuiContainerForegroundLayerScreen(int param1, int param2) {
        this.func_146979_b(param1, param2);
    }

    protected void actionPerformedScreen(GuiButton btn) throws IOException {
        this.func_146284_a(btn);
    }

    public void switchScreen(SCREEN_ID id) {
        this.screen_switcher.switchScreen(id);
    }

    public static int getScoreboradWidth(Minecraft mc) {
        W_ScaledResolution scaledresolution = new W_ScaledResolution(mc, mc.field_71443_c, mc.field_71440_d);
        int ScaledWidth = scaledresolution.func_78326_a() - 40;
        int width = ScaledWidth * 3 / 4 / (mc.field_71441_e.func_96441_U().func_96525_g().size() + 1);
        if (width > 150) {
            width = 150;
        }
        return width;
    }

    public static int getScoreBoardLeft(Minecraft mc, int teamNum, int teamIndex) {
        W_ScaledResolution scaledresolution = new W_ScaledResolution(mc, mc.field_71443_c, mc.field_71440_d);
        int ScaledWidth = scaledresolution.func_78326_a();
        return (int)((double)(ScaledWidth / 2) + (double)(MCH_GuiScoreboard_Base.getScoreboradWidth(mc) + 10) * ((double)(-teamNum) / 2.0 + (double)teamIndex));
    }

    public static void drawList(Minecraft mc, FontRenderer fontRendererObj, boolean mng) {
        ArrayList<ScorePlayerTeam> teamList = new ArrayList<ScorePlayerTeam>();
        teamList.add(null);
        for (Object team : mc.field_71441_e.func_96441_U().func_96525_g()) {
            teamList.add((ScorePlayerTeam)team);
        }
        Collections.sort(teamList, new Comparator<ScorePlayerTeam>(){

            @Override
            public int compare(ScorePlayerTeam o1, ScorePlayerTeam o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                return o1.func_96661_b().compareTo(o2.func_96661_b());
            }
        });
        for (int i = 0; i < teamList.size(); ++i) {
            if (mng) {
                MCH_GuiScoreboard_Base.drawPlayersList(mc, fontRendererObj, (ScorePlayerTeam)teamList.get(i), 1 + i, 1 + teamList.size());
                continue;
            }
            MCH_GuiScoreboard_Base.drawPlayersList(mc, fontRendererObj, (ScorePlayerTeam)teamList.get(i), i, teamList.size());
        }
    }

    public static void drawPlayersList(Minecraft mc, FontRenderer fontRendererObj, ScorePlayerTeam team, int teamIndex, int teamNum) {
        W_ScaledResolution scaledresolution = new W_ScaledResolution(mc, mc.field_71443_c, mc.field_71440_d);
        int ScaledHeight = scaledresolution.func_78328_b();
        ScoreObjective scoreobjective = mc.field_71441_e.func_96441_U().func_96539_a(0);
        NetHandlerPlayClient nethandlerplayclient = mc.field_71439_g.field_71174_a;
        ArrayList list = Lists.newArrayList((Iterable)nethandlerplayclient.func_175106_d());
        int MaxPlayers = (list.size() / 5 + 1) * 5;
        int n = MaxPlayers = MaxPlayers < 10 ? 10 : MaxPlayers;
        if (MaxPlayers > nethandlerplayclient.field_147304_c) {
            MaxPlayers = nethandlerplayclient.field_147304_c;
        }
        int width = MCH_GuiScoreboard_Base.getScoreboradWidth(mc);
        int listLeft = MCH_GuiScoreboard_Base.getScoreBoardLeft(mc, teamNum, teamIndex);
        int listTop = ScaledHeight / 2 - (MaxPlayers * 9 + 10) / 2;
        MCH_GuiScoreboard_Base.func_73734_a((int)(listLeft - 1), (int)(listTop - 1 - 18), (int)(listLeft + width), (int)(listTop + 9 * MaxPlayers), (int)Integer.MIN_VALUE);
        String teamName = ScorePlayerTeam.func_96667_a((Team)team, (String)(team == null ? "No team" : team.func_96661_b()));
        int teamNameX = listLeft + width / 2 - fontRendererObj.func_78256_a(teamName) / 2;
        fontRendererObj.func_175063_a(teamName, (float)teamNameX, (float)(listTop - 18), -1);
        String ff_onoff = "FriendlyFire : " + (team == null ? "ON" : (team.func_96665_g() ? "ON" : "OFF"));
        int ff_onoffX = listLeft + width / 2 - fontRendererObj.func_78256_a(ff_onoff) / 2;
        fontRendererObj.func_175063_a(ff_onoff, (float)ff_onoffX, (float)(listTop - 9), -1);
        int drawY = 0;
        for (int i = 0; i < MaxPlayers; ++i) {
            int j4;
            int k4;
            int x = listLeft;
            int y = listTop + drawY * 9;
            int rectY = listTop + i * 9;
            MCH_GuiScoreboard_Base.func_73734_a((int)x, (int)rectY, (int)(x + width - 1), (int)(rectY + 8), (int)0x20FFFFFF);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glEnable((int)3008);
            if (i >= list.size()) continue;
            NetworkPlayerInfo guiplayerinfo = (NetworkPlayerInfo)list.get(i);
            String playerName = guiplayerinfo.func_178845_a().getName();
            ScorePlayerTeam steam = mc.field_71441_e.func_96441_U().func_96509_i(playerName);
            if ((steam != null || team != null) && (steam == null || team == null || !steam.func_142054_a((Team)team))) continue;
            ++drawY;
            fontRendererObj.func_175063_a(playerName, (float)x, (float)y, -1);
            if (scoreobjective != null && (k4 = x + width - 12 - 5) - (j4 = x + fontRendererObj.func_78256_a(playerName) + 5) > 5) {
                Score score = scoreobjective.func_96682_a().func_96529_a(guiplayerinfo.func_178845_a().getName(), scoreobjective);
                String s1 = TextFormatting.YELLOW + "" + score.func_96652_c();
                fontRendererObj.func_175063_a(s1, (float)(k4 - fontRendererObj.func_78256_a(s1)), (float)y, 0xFFFFFF);
            }
            MCH_GuiScoreboard_Base.drawResponseTime(x + width - 12, y, guiplayerinfo.func_178853_c());
        }
    }

    public static void drawResponseTime(int x, int y, int responseTime) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(field_110324_m);
        int b2 = responseTime < 0 ? 5 : (responseTime < 150 ? 0 : (responseTime < 300 ? 1 : (responseTime < 600 ? 2 : (responseTime < 1000 ? 3 : 4))));
        MCH_GuiScoreboard_Base.static_drawTexturedModalRect(x, y, 0, 176 + b2 * 8, 10, 8, 0.0);
    }

    public static void static_drawTexturedModalRect(int x, int y, int x2, int y2, int x3, int y3, double zLevel) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        builder.func_181662_b((double)(x + 0), (double)(y + y3), zLevel).func_187315_a((double)((float)(x2 + 0) * 0.00390625f), (double)((float)(y2 + y3) * 0.00390625f)).func_181675_d();
        builder.func_181662_b((double)(x + x3), (double)(y + y3), zLevel).func_187315_a((double)((float)(x2 + x3) * 0.00390625f), (double)((float)(y2 + y3) * 0.00390625f)).func_181675_d();
        builder.func_181662_b((double)(x + x3), (double)(y + 0), zLevel).func_187315_a((double)((float)(x2 + x3) * 0.00390625f), (double)((float)(y2 + 0) * 0.00390625f)).func_181675_d();
        builder.func_181662_b((double)(x + 0), (double)(y + 0), zLevel).func_187315_a((double)((float)(x2 + 0) * 0.00390625f), (double)((float)(y2 + 0) * 0.00390625f)).func_181675_d();
        tessellator.func_78381_a();
    }

    public static enum SCREEN_ID {
        MAIN,
        CREATE_TEAM;

    }
}

