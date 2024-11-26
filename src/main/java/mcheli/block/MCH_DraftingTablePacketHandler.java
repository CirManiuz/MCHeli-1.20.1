/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.IThreadListener
 *  net.minecraftforge.fml.relauncher.Side
 */
package mcheli.block;

import com.google.common.io.ByteArrayDataInput;
import mcheli.MCH_Lib;
import mcheli.__helper.network.HandleSide;
import mcheli.block.MCH_DraftingTableCreatePacket;
import mcheli.block.MCH_DraftingTableGuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_DraftingTablePacketHandler {
    @HandleSide(value={Side.SERVER})
    public static void onPacketCreate(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.field_70170_p.field_72995_K) {
            MCH_DraftingTableCreatePacket packet = new MCH_DraftingTableCreatePacket();
            packet.readData(data);
            scheduler.func_152344_a(() -> {
                boolean openScreen = player.field_71070_bA instanceof MCH_DraftingTableGuiContainer;
                MCH_Lib.DbgLog(false, "MCH_DraftingTablePacketHandler.onPacketCreate : " + openScreen, new Object[0]);
                if (openScreen) {
                    ((MCH_DraftingTableGuiContainer)player.field_71070_bA).createRecipeItem(packet.recipe);
                }
            });
        }
    }
}

