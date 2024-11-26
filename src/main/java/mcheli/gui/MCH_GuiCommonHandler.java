/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.network.IGuiHandler
 */
package mcheli.gui;

import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.network.PooledGuiParameter;
import mcheli.aircraft.MCH_AircraftGui;
import mcheli.aircraft.MCH_AircraftGuiContainer;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.block.MCH_DraftingTableGui;
import mcheli.block.MCH_DraftingTableGuiContainer;
import mcheli.gui.MCH_ConfigGui;
import mcheli.gui.MCH_ConfigGuiContainer;
import mcheli.multiplay.MCH_ContainerScoreboard;
import mcheli.multiplay.MCH_GuiScoreboard;
import mcheli.uav.MCH_ContainerUavStation;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.uav.MCH_GuiUavStation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class MCH_GuiCommonHandler
implements IGuiHandler {
    public static final int GUIID_UAV_STATION = 0;
    public static final int GUIID_AIRCRAFT = 1;
    public static final int GUIID_CONFG = 2;
    public static final int GUIID_INVENTORY = 3;
    public static final int GUIID_DRAFTING = 4;
    public static final int GUIID_MULTI_MNG = 5;

    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        MCH_Lib.DbgLog(world, "MCH_GuiCommonHandler.getServerGuiElement ID=%d (%d, %d, %d)", id, x, y, z);
        switch (id) {
            case 0: {
                Entity uavStation = PooledGuiParameter.getEntity(player);
                PooledGuiParameter.resetEntity(player);
                if (!(uavStation instanceof MCH_EntityUavStation)) break;
                return new MCH_ContainerUavStation(player.field_71071_by, (MCH_EntityUavStation)uavStation);
            }
            case 1: {
                MCH_EntityAircraft ac = null;
                if (player.func_184187_bx() instanceof MCH_EntityAircraft) {
                    ac = (MCH_EntityAircraft)player.func_184187_bx();
                } else if (player.func_184187_bx() instanceof MCH_EntityUavStation) {
                    ac = ((MCH_EntityUavStation)player.func_184187_bx()).getControlAircract();
                }
                if (ac == null) break;
                return new MCH_AircraftGuiContainer(player, ac);
            }
            case 2: {
                return new MCH_ConfigGuiContainer(player);
            }
            case 4: {
                return new MCH_DraftingTableGuiContainer(player, x, y, z);
            }
            case 5: {
                if (MCH_Utils.getServer().func_71264_H() && !MCH_Config.DebugLog) break;
                return new MCH_ContainerScoreboard(player);
            }
        }
        return null;
    }

    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        MCH_Lib.DbgLog(world, "MCH_GuiCommonHandler.getClientGuiElement ID=%d (%d, %d, %d)", id, x, y, z);
        switch (id) {
            case 0: {
                Entity uavStation = PooledGuiParameter.getEntity(player);
                PooledGuiParameter.resetEntity(player);
                if (!(uavStation instanceof MCH_EntityUavStation)) break;
                return new MCH_GuiUavStation(player.field_71071_by, (MCH_EntityUavStation)uavStation);
            }
            case 1: {
                MCH_EntityAircraft ac = null;
                if (player.func_184187_bx() instanceof MCH_EntityAircraft) {
                    ac = (MCH_EntityAircraft)player.func_184187_bx();
                } else if (player.func_184187_bx() instanceof MCH_EntityUavStation) {
                    ac = ((MCH_EntityUavStation)player.func_184187_bx()).getControlAircract();
                }
                if (ac == null) break;
                return new MCH_AircraftGui(player, ac);
            }
            case 2: {
                return new MCH_ConfigGui(player);
            }
            case 4: {
                return new MCH_DraftingTableGui(player, x, y, z);
            }
            case 5: {
                return new MCH_GuiScoreboard(player);
            }
        }
        return null;
    }
}

