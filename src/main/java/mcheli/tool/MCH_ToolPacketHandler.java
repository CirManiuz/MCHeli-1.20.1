/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.IThreadListener
 *  net.minecraftforge.fml.relauncher.Side
 */
package mcheli.tool;

import com.google.common.io.ByteArrayDataInput;
import mcheli.MCH_Config;
import mcheli.__helper.network.HandleSide;
import mcheli.multiplay.MCH_Multiplay;
import mcheli.multiplay.MCH_PacketIndSpotEntity;
import mcheli.tool.rangefinder.MCH_ItemRangeFinder;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_ToolPacketHandler {
    @HandleSide(value={Side.SERVER})
    public static void onPacket_IndSpotEntity(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.field_70170_p.field_72995_K) {
            return;
        }
        MCH_PacketIndSpotEntity pc = new MCH_PacketIndSpotEntity();
        pc.readData(data);
        scheduler.func_152344_a(() -> {
            ItemStack itemStack = player.func_184614_ca();
            if (!itemStack.func_190926_b() && itemStack.func_77973_b() instanceof MCH_ItemRangeFinder) {
                if (pc.targetFilter == 0) {
                    if (MCH_Multiplay.markPoint(player, player.field_70165_t, player.field_70163_u + (double)player.func_70047_e(), player.field_70161_v)) {
                        W_WorldFunc.MOD_playSoundAtEntity((Entity)player, "pi", 1.0f, 1.0f);
                    } else {
                        W_WorldFunc.MOD_playSoundAtEntity((Entity)player, "ng", 1.0f, 1.0f);
                    }
                } else if (itemStack.func_77960_j() < itemStack.func_77958_k()) {
                    int time;
                    if (MCH_Config.RangeFinderConsume.prmBool) {
                        itemStack.func_77972_a(1, (EntityLivingBase)player);
                    }
                    int n = time = (pc.targetFilter & 0xFC) == 0 ? 60 : MCH_Config.RangeFinderSpotTime.prmInt;
                    if (MCH_Multiplay.spotEntity((EntityLivingBase)player, null, player.field_70165_t, player.field_70163_u + (double)player.func_70047_e(), player.field_70161_v, pc.targetFilter, MCH_Config.RangeFinderSpotDist.prmInt, time, 20.0f)) {
                        W_WorldFunc.MOD_playSoundAtEntity((Entity)player, "pi", 1.0f, 1.0f);
                    } else {
                        W_WorldFunc.MOD_playSoundAtEntity((Entity)player, "ng", 1.0f, 1.0f);
                    }
                }
            }
        });
    }
}

