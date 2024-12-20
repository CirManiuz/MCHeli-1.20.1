/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.opengl.GL11
 */
package mcheli.uav;

import java.io.IOException;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.helicopter.MCH_HeliInfoManager;
import mcheli.helicopter.MCH_ItemHeli;
import mcheli.plane.MCP_ItemPlane;
import mcheli.plane.MCP_PlaneInfoManager;
import mcheli.tank.MCH_ItemTank;
import mcheli.tank.MCH_TankInfoManager;
import mcheli.uav.MCH_ContainerUavStation;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.uav.MCH_UavPacketStatus;
import mcheli.wrapper.W_GuiContainer;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_Network;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class MCH_GuiUavStation
extends W_GuiContainer {
    final MCH_EntityUavStation uavStation;
    static final int BX = 20;
    static final int BY = 22;
    private GuiButton buttonContinue;

    public MCH_GuiUavStation(InventoryPlayer inventoryPlayer, MCH_EntityUavStation uavStation) {
        super(new MCH_ContainerUavStation(inventoryPlayer, uavStation));
        this.uavStation = uavStation;
    }

    protected void func_146979_b(int param1, int param2) {
        if (this.uavStation == null) {
            return;
        }
        ItemStack item = this.uavStation.func_70301_a(0);
        MCH_AircraftInfo info = null;
        if (!item.func_190926_b() && item.func_77973_b() instanceof MCP_ItemPlane) {
            info = MCP_PlaneInfoManager.getFromItem(item.func_77973_b());
        }
        if (!item.func_190926_b() && item.func_77973_b() instanceof MCH_ItemHeli) {
            info = MCH_HeliInfoManager.getFromItem(item.func_77973_b());
        }
        if (!item.func_190926_b() && item.func_77973_b() instanceof MCH_ItemTank) {
            info = MCH_TankInfoManager.getFromItem(item.func_77973_b());
        }
        if (item.func_190926_b() || info != null && info.isUAV) {
            if (this.uavStation.getKind() <= 1) {
                this.drawString("UAV Station", 8, 6, 0xFFFFFF);
            } else if (item.func_190926_b() || info.isSmallUAV) {
                this.drawString("UAV Controller", 8, 6, 0xFFFFFF);
            } else {
                this.drawString("Small UAV only", 8, 6, 0xFF0000);
            }
        } else if (!item.func_190926_b()) {
            this.drawString("Not UAV", 8, 6, 0xFF0000);
        }
        this.drawString(I18n.func_135052_a((String)"container.inventory", (Object[])new Object[0]), 8, this.field_147000_g - 96 + 2, 0xFFFFFF);
        this.drawString(String.format("X.%+2d", this.uavStation.posUavX), 58, 15, 0xFFFFFF);
        this.drawString(String.format("Y.%+2d", this.uavStation.posUavY), 58, 37, 0xFFFFFF);
        this.drawString(String.format("Z.%+2d", this.uavStation.posUavZ), 58, 59, 0xFFFFFF);
    }

    protected void func_146976_a(float par1, int par2, int par3) {
        W_McClient.MOD_bindTexture("textures/gui/uav_station.png");
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int x = (this.field_146294_l - this.field_146999_f) / 2;
        int y = (this.field_146295_m - this.field_147000_g) / 2;
        this.func_73729_b(x, y, 0, 0, this.field_146999_f, this.field_147000_g);
    }

    protected void func_146284_a(GuiButton btn) throws IOException {
        if (btn != null && btn.field_146124_l) {
            if (btn.field_146127_k == 256) {
                if (this.uavStation != null && !this.uavStation.field_70128_L && this.uavStation.getLastControlAircraft() != null && !this.uavStation.getLastControlAircraft().field_70128_L) {
                    MCH_UavPacketStatus data = new MCH_UavPacketStatus();
                    data.posUavX = (byte)this.uavStation.posUavX;
                    data.posUavY = (byte)this.uavStation.posUavY;
                    data.posUavZ = (byte)this.uavStation.posUavZ;
                    data.continueControl = true;
                    W_Network.sendToServer(data);
                }
                this.buttonContinue.field_146124_l = false;
            } else {
                int[] pos = new int[]{this.uavStation.posUavX, this.uavStation.posUavY, this.uavStation.posUavZ};
                int i = btn.field_146127_k >> 4 & 0xF;
                int j = (btn.field_146127_k & 0xF) - 1;
                int[] BTN = new int[]{-10, -1, 1, 10};
                int n = i;
                pos[n] = pos[n] + BTN[j];
                if (pos[i] < -50) {
                    pos[i] = -50;
                }
                if (pos[i] > 50) {
                    pos[i] = 50;
                }
                if (this.uavStation.posUavX != pos[0] || this.uavStation.posUavY != pos[1] || this.uavStation.posUavZ != pos[2]) {
                    MCH_UavPacketStatus data = new MCH_UavPacketStatus();
                    data.posUavX = (byte)pos[0];
                    data.posUavY = (byte)pos[1];
                    data.posUavZ = (byte)pos[2];
                    W_Network.sendToServer(data);
                }
            }
        }
    }

    public void func_73866_w_() {
        super.func_73866_w_();
        this.field_146292_n.clear();
        int x = this.field_146294_l / 2 - 5;
        int y = this.field_146295_m / 2 - 76;
        String[] BTN = new String[]{"-10", "-1", "+1", "+10"};
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 4; ++col) {
                int id = row << 4 | col + 1;
                this.field_146292_n.add(new GuiButton(id, x + col * 20, y + row * 22, 20, 20, BTN[col]));
            }
        }
        this.buttonContinue = new GuiButton(256, x - 80 + 3, y + 44, 50, 20, "Continue");
        this.buttonContinue.field_146124_l = false;
        if (this.uavStation != null && !this.uavStation.field_70128_L && this.uavStation.getAndSearchLastControlAircraft() != null) {
            this.buttonContinue.field_146124_l = true;
        }
        this.field_146292_n.add(this.buttonContinue);
    }
}

