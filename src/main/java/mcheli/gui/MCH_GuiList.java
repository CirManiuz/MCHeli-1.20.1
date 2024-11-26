/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package mcheli.gui;

import java.util.ArrayList;
import java.util.List;
import mcheli.gui.MCH_GuiListItem;
import mcheli.gui.MCH_GuiSliderVertical;
import mcheli.wrapper.W_GuiButton;
import net.minecraft.client.Minecraft;

public class MCH_GuiList
extends W_GuiButton {
    public List<MCH_GuiListItem> listItems;
    public MCH_GuiSliderVertical scrollBar;
    public final int maxRowNum;
    public MCH_GuiListItem lastPushItem;

    public MCH_GuiList(int id, int maxRow, int posX, int posY, int w, int h, String name) {
        super(id, posX, posY, w, h, "");
        this.maxRowNum = maxRow > 0 ? maxRow : 1;
        this.listItems = new ArrayList<MCH_GuiListItem>();
        this.scrollBar = new MCH_GuiSliderVertical(0, posX + w - 20, posY, 20, h, name, 0.0f, 0.0f, 0.0f, 1.0f);
        this.lastPushItem = null;
    }

    public void func_191745_a(Minecraft mc, int x, int y, float partialTicks) {
        if (this.isVisible()) {
            MCH_GuiList.func_73734_a((int)this.field_146128_h, (int)this.field_146129_i, (int)(this.field_146128_h + this.field_146120_f), (int)(this.field_146129_i + this.field_146121_g), (int)-2143272896);
            this.scrollBar.func_191745_a(mc, x, y, partialTicks);
            for (int i = 0; i < this.maxRowNum && i + this.getStartRow() < this.listItems.size(); ++i) {
                MCH_GuiListItem item = this.listItems.get(i + this.getStartRow());
                item.draw(mc, x, y, this.field_146128_h, this.field_146129_i + 5 + 20 * i, partialTicks);
            }
        }
    }

    public void addItem(MCH_GuiListItem item) {
        this.listItems.add(item);
        int listNum = this.listItems.size();
        this.scrollBar.valueMax = listNum > this.maxRowNum ? (float)(listNum - this.maxRowNum) : 0.0f;
    }

    public MCH_GuiListItem getItem(int i) {
        return i < this.getItemNum() ? this.listItems.get(i) : null;
    }

    public int getItemNum() {
        return this.listItems.size();
    }

    public void scrollUp(float a) {
        if (this.isVisible()) {
            this.scrollBar.scrollUp(a);
        }
    }

    public void scrollDown(float a) {
        if (this.isVisible()) {
            this.scrollBar.scrollDown(a);
        }
    }

    public int getStartRow() {
        int startRow = (int)this.scrollBar.getSliderValue();
        return startRow >= 0 ? startRow : 0;
    }

    protected void func_146119_b(Minecraft mc, int x, int y) {
        if (this.isVisible()) {
            this.scrollBar.func_146119_b(mc, x, y);
        }
    }

    public boolean func_146116_c(Minecraft mc, int x, int y) {
        boolean b = false;
        if (this.isVisible()) {
            b |= this.scrollBar.func_146116_c(mc, x, y);
            for (int i = 0; i < this.maxRowNum && i + this.getStartRow() < this.listItems.size(); ++i) {
                MCH_GuiListItem item = this.listItems.get(i + this.getStartRow());
                if (!item.mousePressed(mc, x, y)) continue;
                this.lastPushItem = item;
                b = true;
            }
        }
        return b;
    }

    public void func_146118_a(int x, int y) {
        if (this.isVisible()) {
            this.scrollBar.func_146118_a(x, y);
            for (MCH_GuiListItem item : this.listItems) {
                item.mouseReleased(x, y);
            }
        }
    }
}

