/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.opengl.GL11
 */
package mcheli.aircraft;

import java.io.IOException;
import mcheli.MCH_PacketIndOpenScreen;
import mcheli.aircraft.MCH_AircraftGuiContainer;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_PacketIndReload;
import mcheli.command.MCH_PacketCommandSave;
import mcheli.multiplay.MCH_PacketIndMultiplayCommand;
import mcheli.weapon.MCH_WeaponDummy;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponSet;
import mcheli.wrapper.W_GuiContainer;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class MCH_AircraftGui
extends W_GuiContainer {
    private final EntityPlayer thePlayer;
    private final MCH_EntityAircraft aircraft;
    private GuiButton buttonReload;
    private GuiButton buttonNext;
    private GuiButton buttonPrev;
    private GuiButton buttonInventory;
    private int currentWeaponId;
    private int reloadWait;
    private GuiTextField editCommand;
    public static final int BUTTON_RELOAD = 1;
    public static final int BUTTON_NEXT = 2;
    public static final int BUTTON_PREV = 3;
    public static final int BUTTON_CLOSE = 4;
    public static final int BUTTON_CONFIG = 5;
    public static final int BUTTON_INVENTORY = 6;

    public MCH_AircraftGui(EntityPlayer player, MCH_EntityAircraft ac) {
        super(new MCH_AircraftGuiContainer(player, ac));
        this.aircraft = ac;
        this.thePlayer = player;
        this.field_146999_f = 210;
        this.field_147000_g = 236;
        this.buttonReload = null;
        this.currentWeaponId = 0;
    }

    public void func_73866_w_() {
        super.func_73866_w_();
        this.field_146292_n.clear();
        this.buttonReload = new GuiButton(1, this.field_147003_i + 85, this.field_147009_r + 40, 50, 20, "Reload");
        this.buttonNext = new GuiButton(3, this.field_147003_i + 140, this.field_147009_r + 40, 20, 20, "<<");
        this.buttonPrev = new GuiButton(2, this.field_147003_i + 160, this.field_147009_r + 40, 20, 20, ">>");
        this.buttonReload.field_146124_l = this.canReload(this.thePlayer);
        this.buttonNext.field_146124_l = this.aircraft.getWeaponNum() >= 2;
        this.buttonPrev.field_146124_l = this.aircraft.getWeaponNum() >= 2;
        this.buttonInventory = new GuiButton(6, this.field_147003_i + 210 - 30 - 60, this.field_147009_r + 90, 80, 20, "Inventory");
        this.field_146292_n.add(new GuiButton(5, this.field_147003_i + 210 - 30 - 60, this.field_147009_r + 110, 80, 20, "MOD Options"));
        this.field_146292_n.add(new GuiButton(4, this.field_147003_i + 210 - 30 - 20, this.field_147009_r + 10, 40, 20, "Close"));
        this.field_146292_n.add(this.buttonReload);
        this.field_146292_n.add(this.buttonNext);
        this.field_146292_n.add(this.buttonPrev);
        if (this.aircraft != null && this.aircraft.func_70302_i_() > 0) {
            this.field_146292_n.add(this.buttonInventory);
        }
        this.editCommand = new GuiTextField(0, this.field_146289_q, this.field_147003_i + 25, this.field_147009_r + 215, 160, 15);
        this.editCommand.func_146180_a(this.aircraft.getCommand());
        this.editCommand.func_146203_f(512);
        this.currentWeaponId = 0;
        this.reloadWait = 10;
    }

    public void closeScreen() {
        MCH_PacketCommandSave.send(this.editCommand.func_146179_b());
        this.field_146297_k.field_71439_g.func_71053_j();
    }

    public boolean canReload(EntityPlayer player) {
        return this.aircraft.canPlayerSupplyAmmo(player, this.currentWeaponId);
    }

    public void func_73876_c() {
        super.func_73876_c();
        if (this.reloadWait > 0) {
            --this.reloadWait;
            if (this.reloadWait == 0) {
                this.buttonReload.field_146124_l = this.canReload(this.thePlayer);
                this.reloadWait = 20;
            }
        }
        this.editCommand.func_146178_a();
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.editCommand.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    public void func_146281_b() {
        super.func_146281_b();
    }

    protected void func_146284_a(GuiButton button) throws IOException {
        super.func_146284_a(button);
        if (!button.field_146124_l) {
            return;
        }
        switch (button.field_146127_k) {
            case 4: {
                this.closeScreen();
                break;
            }
            case 1: {
                this.buttonReload.field_146124_l = this.canReload(this.thePlayer);
                if (!this.buttonReload.field_146124_l) break;
                MCH_PacketIndReload.send(this.aircraft, this.currentWeaponId);
                this.aircraft.supplyAmmo(this.currentWeaponId);
                this.reloadWait = 3;
                this.buttonReload.field_146124_l = false;
                break;
            }
            case 2: {
                ++this.currentWeaponId;
                if (this.currentWeaponId >= this.aircraft.getWeaponNum()) {
                    this.currentWeaponId = 0;
                }
                this.buttonReload.field_146124_l = this.canReload(this.thePlayer);
                break;
            }
            case 3: {
                --this.currentWeaponId;
                if (this.currentWeaponId < 0) {
                    this.currentWeaponId = this.aircraft.getWeaponNum() - 1;
                }
                this.buttonReload.field_146124_l = this.canReload(this.thePlayer);
                break;
            }
            case 5: {
                MCH_PacketIndOpenScreen.send(2);
                break;
            }
            case 6: {
                MCH_PacketIndOpenScreen.send(3);
            }
        }
    }

    protected void func_146979_b(int par1, int par2) {
        super.func_146979_b(par1, par2);
        MCH_EntityAircraft ac = this.aircraft;
        this.drawString(ac.getGuiInventory().getInventoryName(), 10, 10, 0xFFFFFF);
        if (this.aircraft.getNumEjectionSeat() > 0) {
            this.drawString("Parachute", 9, 95, 0xFFFFFF);
        }
        if (this.aircraft.getWeaponNum() > 0) {
            MCH_WeaponSet ws = this.aircraft.getWeapon(this.currentWeaponId);
            if (ws != null && !(ws.getFirstWeapon() instanceof MCH_WeaponDummy)) {
                this.drawString(ws.getName(), 79, 30, 0xFFFFFF);
                int rest = ws.getRestAllAmmoNum() + ws.getAmmoNum();
                int color = rest == ws.getAllAmmoNum() ? 2675784 : (rest == 0 ? 0xFF0000 : 0xFFFFFF);
                String s = String.format("%4d/%4d", rest, ws.getAllAmmoNum());
                this.drawString(s, 145, 70, color);
                int itemPosX = 90;
                for (MCH_WeaponInfo.RoundItem r : ws.getInfo().roundItems) {
                    this.drawString("" + r.num, itemPosX, 80, 0xFFFFFF);
                    itemPosX += 20;
                }
                itemPosX = 85;
                for (MCH_WeaponInfo.RoundItem r : ws.getInfo().roundItems) {
                    this.drawItemStack(r.itemStack, itemPosX, 62);
                    itemPosX += 20;
                }
            }
        } else {
            this.drawString("None", 79, 45, 0xFFFFFF);
        }
    }

    protected void func_73869_a(char c, int code) {
        if (code == 1) {
            this.closeScreen();
        } else if (code == 28) {
            String s = this.editCommand.func_146179_b().trim();
            if (s.startsWith("/")) {
                s = s.substring(1);
            }
            if (!s.isEmpty()) {
                MCH_PacketIndMultiplayCommand.send(768, s);
            }
        } else {
            this.editCommand.func_146201_a(c, code);
        }
    }

    protected void func_146976_a(float var1, int var2, int var3) {
        W_McClient.MOD_bindTexture("textures/gui/gui.png");
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int x = (this.field_146294_l - this.field_146999_f) / 2;
        int y = (this.field_146295_m - this.field_147000_g) / 2;
        this.func_73729_b(x, y, 0, 0, this.field_146999_f, this.field_147000_g);
        for (int i = 0; i < this.aircraft.getNumEjectionSeat(); ++i) {
            this.func_73729_b(x + 10 + 18 * i - 1, y + 105 - 1, 215, 55, 18, 18);
        }
        int ff = (int)(this.aircraft.getFuelP() * 50.0f);
        if (ff >= 99) {
            ff = 100;
        }
        this.func_73729_b(x + 57, y + 30 + 50 - ff, 215, 0, 12, ff);
        ff = (int)((double)(this.aircraft.getFuelP() * 100.0f) + 0.5);
        int color = ff > 20 ? -14101432 : 0xFF0000;
        this.drawString(String.format("%3d", ff) + "%", x + 30, y + 65, color);
        this.editCommand.func_146194_f();
    }
}

