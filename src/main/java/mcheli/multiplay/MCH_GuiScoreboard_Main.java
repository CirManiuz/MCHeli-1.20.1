/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.entity.player.EntityPlayer
 */
package mcheli.multiplay;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import mcheli.MCH_ServerSettings;
import mcheli.multiplay.MCH_GuiScoreboard_Base;
import mcheli.multiplay.MCH_IGuiScoreboard;
import mcheli.multiplay.MCH_PacketIndMultiplayCommand;
import mcheli.wrapper.W_GuiButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

public class MCH_GuiScoreboard_Main
extends MCH_GuiScoreboard_Base {
    private W_GuiButton buttonSwitchPVP;

    public MCH_GuiScoreboard_Main(MCH_IGuiScoreboard switcher, EntityPlayer player) {
        super(switcher, player);
    }

    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        if (this.buttonSwitchPVP != null) {
            return;
        }
        this.field_147003_i = 0;
        this.field_147009_r = 0;
        int WIDTH = MCH_GuiScoreboard_Main.getScoreboradWidth(this.field_146297_k) * 3 / 4;
        if (WIDTH < 80) {
            WIDTH = 80;
        }
        int LEFT = MCH_GuiScoreboard_Main.getScoreBoardLeft(this.field_146297_k, this.getTeamNum() + 1, 0) / 4;
        this.buttonSwitchPVP = new W_GuiButton(1024, LEFT, 80, WIDTH, 20, "");
        this.listGui.add(this.buttonSwitchPVP);
        W_GuiButton btn = new W_GuiButton(256, LEFT, 100, WIDTH, 20, "Team shuffle");
        btn.addHoverString("Shuffle all players.");
        this.listGui.add(btn);
        this.listGui.add(new W_GuiButton(512, LEFT, 120, WIDTH, 20, "New team"));
        btn = new W_GuiButton(768, LEFT, 140, WIDTH, 20, "Jump spawn pos");
        btn.addHoverString("Teleport all players -> spawn point.");
        this.listGui.add(btn);
        btn = new W_GuiButton(1280, LEFT, 160, WIDTH, 20, "Destroy All");
        btn.addHoverString("Destroy all aircraft and vehicle.");
        this.listGui.add(btn);
    }

    protected void func_73869_a(char c, int code) throws IOException {
        if (code == 1) {
            this.field_146297_k.field_71439_g.func_71053_j();
        }
    }

    @Override
    public void updateScreenButtons(List<GuiButton> list) {
        Iterator<GuiButton> iterator = list.iterator();
        while (iterator.hasNext()) {
            GuiButton o;
            GuiButton button = o = iterator.next();
            if (button.field_146127_k != 1024) continue;
            button.field_146126_j = "PVP : " + (MCH_ServerSettings.enablePVP ? "ON" : "OFF");
        }
    }

    protected void func_146284_a(GuiButton btn) throws IOException {
        if (btn != null && btn.field_146124_l) {
            switch (btn.field_146127_k) {
                case 256: {
                    MCH_PacketIndMultiplayCommand.send(256, "");
                    break;
                }
                case 768: {
                    MCH_PacketIndMultiplayCommand.send(512, "");
                    break;
                }
                case 512: {
                    this.switchScreen(SCREEN_ID.CREATE_TEAM);
                    break;
                }
                case 1024: {
                    MCH_PacketIndMultiplayCommand.send(1024, "");
                    break;
                }
                case 1280: {
                    MCH_PacketIndMultiplayCommand.send(1280, "");
                }
            }
        }
    }

    @Override
    public void drawGuiContainerForegroundLayerScreen(int x, int y) {
        super.drawGuiContainerForegroundLayerScreen(x, y);
    }

    @Override
    protected void func_146976_a(float par1, int par2, int par3) {
        MCH_GuiScoreboard_Main.drawList(this.field_146297_k, this.field_146289_q, true);
    }
}

