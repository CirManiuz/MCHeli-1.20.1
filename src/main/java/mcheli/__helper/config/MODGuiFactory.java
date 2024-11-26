/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraftforge.fml.client.DefaultGuiFactory
 */
package mcheli.__helper.config;

import mcheli.__helper.config.GuiMODConfigTop;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;

public class MODGuiFactory
extends DefaultGuiFactory {
    public MODGuiFactory() {
        super("mcheli", "MC Helicopter MOD");
    }

    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new GuiMODConfigTop(parentScreen);
    }
}

