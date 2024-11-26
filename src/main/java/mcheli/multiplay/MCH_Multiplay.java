/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.command.CommandException
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.server.CommandScoreboard
 *  net.minecraft.command.server.CommandTeleport
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.management.PlayerList
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.multiplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import mcheli.MCH_Lib;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.entity.IEntitySinglePassenger;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.helicopter.MCH_EntityHeli;
import mcheli.multiplay.MCH_PacketNotifyMarkPoint;
import mcheli.multiplay.MCH_PacketNotifySpotedEntity;
import mcheli.multiplay.MCH_TargetType;
import mcheli.plane.MCP_EntityPlane;
import mcheli.tank.MCH_EntityTank;
import mcheli.vehicle.MCH_EntityVehicle;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_Multiplay {
    public static final MCH_TargetType[][] ENTITY_SPOT_TABLE = new MCH_TargetType[][]{{MCH_TargetType.NONE, MCH_TargetType.NONE}, {MCH_TargetType.OTHER_MOB, MCH_TargetType.OTHER_MOB}, {MCH_TargetType.MONSTER, MCH_TargetType.MONSTER}, {MCH_TargetType.NONE, MCH_TargetType.NO_TEAM_PLAYER}, {MCH_TargetType.NONE, MCH_TargetType.SAME_TEAM_PLAYER}, {MCH_TargetType.NONE, MCH_TargetType.OTHER_TEAM_PLAYER}, {MCH_TargetType.NONE, MCH_TargetType.NONE}, {MCH_TargetType.NONE, MCH_TargetType.NO_TEAM_PLAYER}, {MCH_TargetType.NONE, MCH_TargetType.SAME_TEAM_PLAYER}, {MCH_TargetType.NONE, MCH_TargetType.OTHER_TEAM_PLAYER}};

    public static boolean canSpotEntityWithFilter(int filter, Entity entity) {
        if (entity instanceof MCP_EntityPlane) {
            return (filter & 0x20) != 0;
        }
        if (entity instanceof MCH_EntityHeli) {
            return (filter & 0x10) != 0;
        }
        if (entity instanceof MCH_EntityVehicle || entity instanceof MCH_EntityTank) {
            return (filter & 8) != 0;
        }
        if (entity instanceof EntityPlayer) {
            return (filter & 4) != 0;
        }
        if (entity instanceof EntityLivingBase) {
            if (MCH_Multiplay.isMonster(entity)) {
                return (filter & 2) != 0;
            }
            return (filter & 1) != 0;
        }
        return false;
    }

    public static boolean isMonster(Entity entity) {
        return entity.getClass().toString().toLowerCase().indexOf("monster") >= 0;
    }

    public static MCH_TargetType canSpotEntity(Entity user, double posX, double posY, double posZ, Entity target, boolean checkSee) {
        Vec3d ve;
        Vec3d vs;
        RayTraceResult mop;
        if (!(user instanceof EntityLivingBase)) {
            return MCH_TargetType.NONE;
        }
        EntityLivingBase spotter = (EntityLivingBase)user;
        int col = spotter.func_96124_cp() == null ? 0 : 1;
        int row = 0;
        if (target instanceof EntityLivingBase) {
            row = !MCH_Multiplay.isMonster(target) ? 1 : 2;
        }
        if (spotter.func_96124_cp() != null) {
            if (target instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)target;
                row = player.func_96124_cp() == null ? 3 : (spotter.func_184191_r((Entity)player) ? 4 : 5);
            } else if (target instanceof MCH_EntityAircraft) {
                MCH_EntityAircraft ac = (MCH_EntityAircraft)target;
                EntityPlayer rideEntity = ac.getFirstMountPlayer();
                row = rideEntity == null ? 6 : (rideEntity.func_96124_cp() == null ? 7 : (spotter.func_184191_r((Entity)rideEntity) ? 8 : 9));
            }
        } else if (target instanceof EntityPlayer || target instanceof MCH_EntityAircraft) {
            row = 0;
        }
        MCH_TargetType ret = ENTITY_SPOT_TABLE[row][col];
        if (checkSee && ret != MCH_TargetType.NONE && (mop = target.field_70170_p.func_72933_a(vs = new Vec3d(posX, posY, posZ), ve = new Vec3d(target.field_70165_t, target.field_70163_u + (double)target.func_70047_e(), target.field_70161_v))) != null && mop.field_72313_a == RayTraceResult.Type.BLOCK) {
            ret = MCH_TargetType.NONE;
        }
        return ret;
    }

    public static boolean canAttackEntity(DamageSource ds, Entity target) {
        return MCH_Multiplay.canAttackEntity(ds.func_76346_g(), target);
    }

    public static boolean canAttackEntity(Entity attacker, Entity target) {
        if (attacker != null && target != null) {
            MCH_EntityAircraft ac;
            EntityPlayer attackPlayer = null;
            EntityPlayer targetPlayer = null;
            if (attacker instanceof EntityPlayer) {
                attackPlayer = (EntityPlayer)attacker;
            }
            if (target instanceof EntityPlayer) {
                targetPlayer = (EntityPlayer)target;
            } else if (target instanceof IEntitySinglePassenger && ((IEntitySinglePassenger)target).getRiddenByEntity() instanceof EntityPlayer) {
                targetPlayer = (EntityPlayer)((IEntitySinglePassenger)target).getRiddenByEntity();
            }
            if (target instanceof MCH_EntityAircraft && (ac = (MCH_EntityAircraft)target).getRiddenByEntity() instanceof EntityPlayer) {
                targetPlayer = (EntityPlayer)ac.getRiddenByEntity();
            }
            if (attackPlayer != null && targetPlayer != null && !attackPlayer.func_96122_a(targetPlayer)) {
                return false;
            }
        }
        return true;
    }

    public static void jumpSpawnPoint(EntityPlayer player) {
        MCH_Lib.DbgLog(false, "JumpSpawnPoint", new Object[0]);
        CommandTeleport cmd = new CommandTeleport();
        if (cmd.func_184882_a(MCH_Utils.getServer(), (ICommandSender)player)) {
            MinecraftServer minecraftServer = MCH_Utils.getServer();
            for (String playerName : minecraftServer.func_184103_al().func_72369_d()) {
                try {
                    EntityPlayerMP jumpPlayer = CommandTeleport.func_184888_a((MinecraftServer)minecraftServer, (ICommandSender)player, (String)playerName);
                    BlockPos cc = null;
                    if (jumpPlayer != null && jumpPlayer.field_71093_bK == player.field_71093_bK) {
                        cc = jumpPlayer.getBedLocation(jumpPlayer.field_71093_bK);
                        if (cc != null) {
                            cc = EntityPlayer.func_180467_a((World)minecraftServer.func_71218_a(jumpPlayer.field_71093_bK), (BlockPos)cc, (boolean)true);
                        }
                        if (cc == null) {
                            cc = jumpPlayer.field_70170_p.field_73011_w.getRandomizedSpawnPoint();
                        }
                    }
                    if (cc == null) continue;
                    String[] cmdStr = new String[]{playerName, String.format("%.1f", (double)cc.func_177958_n() + 0.5), String.format("%.1f", (double)cc.func_177956_o() + 0.1), String.format("%.1f", (double)cc.func_177952_p() + 0.5)};
                    cmd.func_184881_a(minecraftServer, (ICommandSender)player, cmdStr);
                }
                catch (CommandException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void shuffleTeam(EntityPlayer player) {
        CommandScoreboard cmd;
        Collection teams = player.field_70170_p.func_96441_U().func_96525_g();
        int teamNum = teams.size();
        MCH_Lib.DbgLog(false, "ShuffleTeam:%d teams ----------", teamNum);
        if (teamNum > 0 && (cmd = new CommandScoreboard()).func_184882_a(MCH_Utils.getServer(), (ICommandSender)player)) {
            List<String> list = Arrays.asList(MCH_Utils.getServer().func_184103_al().func_72369_d());
            Collections.shuffle(list);
            ArrayList<String> listTeam = new ArrayList<String>();
            for (Object o : teams) {
                ScorePlayerTeam team = (ScorePlayerTeam)o;
                listTeam.add(team.func_96661_b());
            }
            Collections.shuffle(listTeam);
            int j = 0;
            for (int i = 0; i < list.size(); ++i) {
                listTeam.set(j, (String)listTeam.get(j) + " " + list.get(i));
                if (++j < teamNum) continue;
                j = 0;
            }
            for (j = 0; j < listTeam.size(); ++j) {
                String exe_cmd = "teams join " + (String)listTeam.get(j);
                String[] process_cmd = exe_cmd.split(" ");
                if (process_cmd.length <= 3) continue;
                MCH_Lib.DbgLog(false, "ShuffleTeam:" + exe_cmd, new Object[0]);
                try {
                    cmd.func_184881_a(MCH_Utils.getServer(), (ICommandSender)player, process_cmd);
                    continue;
                }
                catch (CommandException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean spotEntity(EntityLivingBase player, @Nullable MCH_EntityAircraft ac, double posX, double posY, double posZ, int targetFilter, float spotLength, int markTime, float angle) {
        boolean ret = false;
        if (!player.field_70170_p.field_72995_K) {
            float acRoll = 0.0f;
            if (ac != null) {
                acRoll = ac.getRotRoll();
            }
            Vec3d vv = MCH_Lib.RotVec3(0.0, 0.0, 1.0, -player.field_70177_z, -player.field_70125_A, -acRoll);
            double tx = vv.field_72450_a;
            double tz = vv.field_72449_c;
            List list = player.field_70170_p.func_72839_b((Entity)player, player.func_174813_aQ().func_72314_b((double)spotLength, (double)spotLength, (double)spotLength));
            ArrayList<Integer> entityList = new ArrayList<Integer>();
            Vec3d pos = new Vec3d(posX, posY, posZ);
            for (int i = 0; i < list.size(); ++i) {
                double dist;
                MCH_TargetType stopType;
                Entity entity = (Entity)list.get(i);
                if (!MCH_Multiplay.canSpotEntityWithFilter(targetFilter, entity) || (stopType = MCH_Multiplay.canSpotEntity((Entity)player, posX, posY, posZ, entity, true)) == MCH_TargetType.NONE || stopType == MCH_TargetType.SAME_TEAM_PLAYER || !((dist = entity.func_70092_e(pos.field_72450_a, pos.field_72448_b, pos.field_72449_c)) > 1.0) || !(dist < (double)(spotLength * spotLength))) continue;
                double cx = entity.field_70165_t - pos.field_72450_a;
                double cy = entity.field_70163_u - pos.field_72448_b;
                double cz = entity.field_70161_v - pos.field_72449_c;
                double h = MCH_Lib.getPosAngle(tx, tz, cx, cz);
                double v = Math.atan2(cy, Math.sqrt(cx * cx + cz * cz)) * 180.0 / Math.PI;
                v = Math.abs(v + (double)player.field_70125_A);
                if (!(h < (double)(angle * 2.0f)) || !(v < (double)(angle * 2.0f))) continue;
                entityList.add(entity.func_145782_y());
            }
            if (entityList.size() > 0) {
                int[] entityId = new int[entityList.size()];
                for (int i = 0; i < entityId.length; ++i) {
                    entityId[i] = (Integer)entityList.get(i);
                }
                MCH_Multiplay.sendSpotedEntityListToSameTeam(player, markTime, entityId);
                ret = true;
            } else {
                ret = false;
            }
        }
        return ret;
    }

    public static void sendSpotedEntityListToSameTeam(EntityLivingBase player, int count, int[] entityId) {
        PlayerList svCnf = MCH_Utils.getServer().func_184103_al();
        for (EntityPlayer notifyPlayer : svCnf.func_181057_v()) {
            if (player != notifyPlayer && !player.func_184191_r((Entity)notifyPlayer)) continue;
            MCH_PacketNotifySpotedEntity.send(notifyPlayer, count, entityId);
        }
    }

    public static boolean markPoint(EntityPlayer player, double posX, double posY, double posZ) {
        Vec3d vs = new Vec3d(posX, posY, posZ);
        Vec3d ve = MCH_Lib.Rot2Vec3(player.field_70177_z, player.field_70125_A);
        ve = vs.func_72441_c(ve.field_72450_a * 300.0, ve.field_72448_b * 300.0, ve.field_72449_c * 300.0);
        RayTraceResult mop = player.field_70170_p.func_72901_a(vs, ve, true);
        if (mop != null && mop.field_72313_a == RayTraceResult.Type.BLOCK) {
            MCH_Multiplay.sendMarkPointToSameTeam(player, mop.func_178782_a().func_177958_n(), mop.func_178782_a().func_177956_o(), mop.func_178782_a().func_177952_p());
            return true;
        }
        MCH_Multiplay.sendMarkPointToSameTeam(player, 0, 1000, 0);
        return false;
    }

    public static void sendMarkPointToSameTeam(EntityPlayer player, int x, int y, int z) {
        PlayerList svCnf = MCH_Utils.getServer().func_184103_al();
        for (EntityPlayer notifyPlayer : svCnf.func_181057_v()) {
            if (player != notifyPlayer && !player.func_184191_r((Entity)notifyPlayer)) continue;
            MCH_PacketNotifyMarkPoint.send(notifyPlayer, x, y, z);
        }
    }
}

