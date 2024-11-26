/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.item.crafting.IRecipe
 */
package mcheli.block;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.MCH_Lib;
import mcheli.MCH_Packet;
import mcheli.__helper.network.PacketHelper;
import mcheli.wrapper.W_Network;
import net.minecraft.item.crafting.IRecipe;

public class MCH_DraftingTableCreatePacket
extends MCH_Packet {
    public IRecipe recipe;

    @Override
    public int getMessageID() {
        return 537395216;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.recipe = PacketHelper.readRecipe(data);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            PacketHelper.writeRecipe(dos, this.recipe);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(IRecipe recipe) {
        if (recipe != null) {
            MCH_DraftingTableCreatePacket s = new MCH_DraftingTableCreatePacket();
            s.recipe = recipe;
            W_Network.sendToServer(s);
            MCH_Lib.DbgLog(true, "MCH_DraftingTableCreatePacket.send recipe = " + recipe.getRegistryName(), new Object[0]);
        }
    }
}

