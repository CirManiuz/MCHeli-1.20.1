/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.command.ICommandManager
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.server.CommandScoreboard
 *  net.minecraft.command.server.CommandSummon
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.util.IThreadListener
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraftforge.fml.relauncher.Side
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import mcheli.MCH_Lib;
import mcheli.MCH_PacketNotifyServerSettings;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.network.HandleSide;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.multiplay.MCH_GuiTargetMarker;
import mcheli.multiplay.MCH_Multiplay;
import mcheli.multiplay.MCH_MultiplayClient;
import mcheli.multiplay.MCH_PacketIndClient;
import mcheli.multiplay.MCH_PacketIndMultiplayCommand;
import mcheli.multiplay.MCH_PacketLargeData;
import mcheli.multiplay.MCH_PacketModList;
import mcheli.multiplay.MCH_PacketNotifyMarkPoint;
import mcheli.multiplay.MCH_PacketNotifySpotedEntity;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MCH_MultiplayPacketHandler {
    private static final Logger logger = LogManager.getLogger();
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static byte[] imageData = null;
    private static String lastPlayerName = "";
    private static double lastDataPercent = 0.0;
    public static EntityPlayer modListRequestPlayer = null;
    private static int playerInfoId = 0;

    @HandleSide(value={Side.SERVER})
    public static void onPacket_Command(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.field_70170_p.field_72995_K) {
            return;
        }
        MCH_PacketIndMultiplayCommand pc = new MCH_PacketIndMultiplayCommand();
        pc.readData(data);
        scheduler.func_152344_a(() -> {
            MinecraftServer minecraftServer = MCH_Utils.getServer();
            MCH_Lib.DbgLog(false, "MCH_MultiplayPacketHandler.onPacket_Command cmd:%d:%s", pc.CmdID, pc.CmdStr);
            switch (pc.CmdID) {
                case 256: {
                    MCH_Multiplay.shuffleTeam(player);
                    break;
                }
                case 512: {
                    MCH_Multiplay.jumpSpawnPoint(player);
                    break;
                }
                case 768: {
                    ICommandManager icommandmanager = minecraftServer.func_71187_D();
                    icommandmanager.func_71556_a((ICommandSender)player, pc.CmdStr);
                    break;
                }
                case 1024: {
                    if (!new CommandScoreboard().func_184882_a(minecraftServer, (ICommandSender)player)) break;
                    minecraftServer.func_71188_g(!minecraftServer.func_71219_W());
                    MCH_PacketNotifyServerSettings.send(null);
                    break;
                }
                case 1280: {
                    MCH_MultiplayPacketHandler.destoryAllAircraft(player);
                    break;
                }
                default: {
                    MCH_Lib.DbgLog(false, "MCH_MultiplayPacketHandler.onPacket_Command unknown cmd:%d:%s", pc.CmdID, pc.CmdStr);
                }
            }
        });
    }

    private static void destoryAllAircraft(EntityPlayer player) {
        CommandSummon cmd = new CommandSummon();
        if (cmd.func_184882_a(MCH_Utils.getServer(), (ICommandSender)player)) {
            for (Entity e : player.field_70170_p.field_72996_f) {
                if (!(e instanceof MCH_EntityAircraft)) continue;
                ((MCH_EntityAircraft)e).func_70106_y();
            }
        }
    }

    @HandleSide(value={Side.CLIENT})
    public static void onPacket_NotifySpotedEntity(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.field_70170_p.field_72995_K) {
            return;
        }
        MCH_PacketNotifySpotedEntity pc = new MCH_PacketNotifySpotedEntity();
        pc.readData(data);
        scheduler.func_152344_a(() -> {
            if (pc.count > 0) {
                for (int i = 0; i < pc.num; ++i) {
                    MCH_GuiTargetMarker.addSpotedEntity(pc.entityId[i], pc.count);
                }
            }
        });
    }

    @HandleSide(value={Side.CLIENT})
    public static void onPacket_NotifyMarkPoint(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.field_70170_p.field_72995_K) {
            return;
        }
        MCH_PacketNotifyMarkPoint pc = new MCH_PacketNotifyMarkPoint();
        pc.readData(data);
        scheduler.func_152344_a(() -> MCH_GuiTargetMarker.markPoint(pc.px, pc.py, pc.pz));
    }

    @HandleSide(value={Side.SERVER})
    public static void onPacket_LargeData(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.field_70170_p.field_72995_K) {
            return;
        }
        MCH_PacketLargeData pc = new MCH_PacketLargeData();
        pc.readData(data);
        scheduler.func_152344_a(() -> {
            try {
                double dataPercent;
                if (pc.imageDataIndex < 0 || pc.imageDataTotalSize <= 0) {
                    return;
                }
                if (pc.imageDataIndex == 0) {
                    if (imageData != null && !lastPlayerName.isEmpty()) {
                        MCH_MultiplayPacketHandler.LogError("[mcheli]Err1:Saving the %s screen shot to server FAILED!!!", lastPlayerName);
                    }
                    imageData = new byte[pc.imageDataTotalSize];
                    lastPlayerName = player.func_145748_c_().func_150254_d();
                    lastDataPercent = 0.0;
                }
                if ((dataPercent = (double)((pc.imageDataIndex + pc.imageDataSize) / pc.imageDataTotalSize) * 100.0) - lastDataPercent >= 10.0 || lastDataPercent == 0.0) {
                    MCH_MultiplayPacketHandler.LogInfo("[mcheli]Saving the %s screen shot to server. %.0f%% : %dbyte / %dbyte", player.func_145748_c_(), dataPercent, pc.imageDataIndex, pc.imageDataTotalSize);
                    lastDataPercent = dataPercent;
                }
                if (imageData == null) {
                    if (imageData != null && lastPlayerName.isEmpty()) {
                        MCH_MultiplayPacketHandler.LogError("[mcheli]Err2:Saving the %s screen shot to server FAILED!!!", player.func_145748_c_());
                    }
                    imageData = null;
                    lastPlayerName = "";
                    lastDataPercent = 0.0;
                    return;
                }
                for (int i = 0; i < pc.imageDataSize; ++i) {
                    MCH_MultiplayPacketHandler.imageData[pc.imageDataIndex + i] = pc.buf[i];
                }
                if (pc.imageDataIndex + pc.imageDataSize >= pc.imageDataTotalSize) {
                    DataOutputStream dos = null;
                    String dt = dateFormat.format(new Date()).toString();
                    File file = new File("screenshots_op");
                    file.mkdir();
                    file = new File(file, player.func_145748_c_() + "_" + dt + ".png");
                    String s = file.getAbsolutePath();
                    MCH_MultiplayPacketHandler.LogInfo("[mcheli]Save Screenshot has been completed: %s", s);
                    FileOutputStream fos = new FileOutputStream(s);
                    dos = new DataOutputStream(fos);
                    dos.write(imageData);
                    dos.flush();
                    dos.close();
                    imageData = null;
                    lastPlayerName = "";
                    lastDataPercent = 0.0;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void LogInfo(String format, Object ... args) {
        logger.info(String.format(format, args));
    }

    public static void LogError(String format, Object ... args) {
        logger.error(String.format(format, args));
    }

    @HandleSide(value={Side.CLIENT})
    public static void onPacket_IndClient(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.field_70170_p.field_72995_K) {
            return;
        }
        MCH_PacketIndClient pc = new MCH_PacketIndClient();
        pc.readData(data);
        scheduler.func_152344_a(() -> {
            if (pc.CmdID == 1) {
                MCH_MultiplayClient.startSendImageData();
            } else if (pc.CmdID == 2) {
                MCH_MultiplayClient.sendModsInfo(player.func_145748_c_().func_150254_d(), player.func_145748_c_().func_150260_c(), Integer.parseInt(pc.CmdStr));
            }
        });
    }

    public static int getPlayerInfoId(EntityPlayer player) {
        modListRequestPlayer = player;
        if (++playerInfoId > 1000000) {
            playerInfoId = 1;
        }
        return playerInfoId;
    }

    @HandleSide(value={Side.CLIENT, Side.SERVER})
    public static void onPacket_ModList(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        MCH_PacketModList pc = new MCH_PacketModList();
        pc.readData(data);
        if (player.field_70170_p.field_72995_K) {
            scheduler.func_152344_a(() -> {
                MCH_Lib.DbgLog(player.field_70170_p, "MCH_MultiplayPacketHandler.onPacket_ModList : ID=%d, Num=%d", pc.id, pc.num);
                if (pc.firstData) {
                    MCH_Lib.Log(TextFormatting.RED + "###### " + player.func_145748_c_() + " ######", new Object[0]);
                }
                for (String s : pc.list) {
                    MCH_Lib.Log(s, new Object[0]);
                    player.func_145747_a((ITextComponent)new TextComponentString(s));
                }
            });
        } else if (pc.id == playerInfoId) {
            scheduler.func_152344_a(() -> {
                if (modListRequestPlayer != null) {
                    MCH_PacketModList.send(modListRequestPlayer, pc);
                } else {
                    if (pc.firstData) {
                        MCH_MultiplayPacketHandler.LogInfo("###### " + player.func_145748_c_() + " ######", new Object[0]);
                    }
                    for (String s : pc.list) {
                        MCH_MultiplayPacketHandler.LogInfo(s, new Object[0]);
                    }
                }
            });
        }
    }
}

