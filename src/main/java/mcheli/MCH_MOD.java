/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.command.CommandHandler
 *  net.minecraft.command.ICommand
 *  net.minecraft.item.Item
 *  net.minecraft.item.Item$ToolMaterial
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.Loader
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.Mod$Instance
 *  net.minecraftforge.fml.common.SidedProxy
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLServerStartedEvent
 *  net.minecraftforge.fml.common.event.FMLServerStartingEvent
 *  net.minecraftforge.fml.common.registry.GameRegistry
 *  org.apache.logging.log4j.Logger
 */
package mcheli;

import java.io.File;
import java.util.Calendar;
import java.util.Map;
import mcheli.MCH_Achievement;
import mcheli.MCH_CommonProxy;
import mcheli.MCH_Config;
import mcheli.MCH_CreativeTabs;
import mcheli.MCH_EventHook;
import mcheli.MCH_InvisibleItem;
import mcheli.MCH_Lib;
import mcheli.MCH_PacketHandler;
import mcheli.MCH_SoundsJson;
import mcheli.__helper.MCH_Blocks;
import mcheli.__helper.MCH_Entities;
import mcheli.__helper.MCH_Items;
import mcheli.__helper.MCH_Logger;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.info.ContentRegistries;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_EntityHide;
import mcheli.aircraft.MCH_EntityHitBox;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.aircraft.MCH_ItemAircraft;
import mcheli.aircraft.MCH_ItemFuel;
import mcheli.block.MCH_DraftingTableBlock;
import mcheli.block.MCH_DraftingTableTileEntity;
import mcheli.chain.MCH_EntityChain;
import mcheli.chain.MCH_ItemChain;
import mcheli.command.MCH_Command;
import mcheli.container.MCH_EntityContainer;
import mcheli.container.MCH_ItemContainer;
import mcheli.flare.MCH_EntityFlare;
import mcheli.gltd.MCH_EntityGLTD;
import mcheli.gltd.MCH_ItemGLTD;
import mcheli.gui.MCH_GuiCommonHandler;
import mcheli.helicopter.MCH_EntityHeli;
import mcheli.helicopter.MCH_HeliInfo;
import mcheli.helicopter.MCH_ItemHeli;
import mcheli.lweapon.MCH_ItemLightWeaponBase;
import mcheli.lweapon.MCH_ItemLightWeaponBullet;
import mcheli.mob.MCH_EntityGunner;
import mcheli.mob.MCH_ItemSpawnGunner;
import mcheli.parachute.MCH_EntityParachute;
import mcheli.parachute.MCH_ItemParachute;
import mcheli.plane.MCP_EntityPlane;
import mcheli.plane.MCP_ItemPlane;
import mcheli.plane.MCP_PlaneInfo;
import mcheli.tank.MCH_EntityTank;
import mcheli.tank.MCH_ItemTank;
import mcheli.tank.MCH_TankInfo;
import mcheli.throwable.MCH_EntityThrowable;
import mcheli.throwable.MCH_ItemThrowable;
import mcheli.throwable.MCH_ThrowableInfo;
import mcheli.tool.MCH_ItemWrench;
import mcheli.tool.rangefinder.MCH_ItemRangeFinder;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.uav.MCH_ItemUavStation;
import mcheli.vehicle.MCH_EntityVehicle;
import mcheli.vehicle.MCH_ItemVehicle;
import mcheli.vehicle.MCH_VehicleInfo;
import mcheli.weapon.MCH_EntityA10;
import mcheli.weapon.MCH_EntityAAMissile;
import mcheli.weapon.MCH_EntityASMissile;
import mcheli.weapon.MCH_EntityATMissile;
import mcheli.weapon.MCH_EntityBomb;
import mcheli.weapon.MCH_EntityBullet;
import mcheli.weapon.MCH_EntityDispensedItem;
import mcheli.weapon.MCH_EntityMarkerRocket;
import mcheli.weapon.MCH_EntityRocket;
import mcheli.weapon.MCH_EntityTorpedo;
import mcheli.weapon.MCH_EntityTvMissile;
import mcheli.weapon.MCH_WeaponInfoManager;
import mcheli.wrapper.NetworkMod;
import mcheli.wrapper.W_Item;
import mcheli.wrapper.W_ItemList;
import mcheli.wrapper.W_LanguageRegistry;
import mcheli.wrapper.W_NetworkRegistry;
import net.minecraft.block.Block;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid="mcheli", name="MC Helicopter MOD", dependencies="required-after:forge@[14.23.5.2854,)", guiFactory="mcheli.__helper.config.MODGuiFactory")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class MCH_MOD {
    public static final String MOD_ID = "mcheli";
    @Deprecated
    public static final String DOMAIN = "mcheli";
    public static final String MOD_NAME = "MC Helicopter MOD";
    public static final String MCVER = "1.12.2";
    public static String VER = "";
    public static final String MOD_CH = "MCHeli_CH";
    public static final String ADDON_FOLDER_NAME = "mcheli_addons";
    @Mod.Instance(value="mcheli")
    public static MCH_MOD instance;
    @SidedProxy(clientSide="mcheli.MCH_ClientProxy", serverSide="mcheli.MCH_CommonProxy")
    public static MCH_CommonProxy proxy;
    public static MCH_PacketHandler packetHandler;
    public static MCH_Config config;
    public static String sourcePath;
    private static File sourceFile;
    private static File addonDir;
    public static MCH_InvisibleItem invisibleItem;
    public static MCH_ItemGLTD itemGLTD;
    public static MCH_ItemLightWeaponBullet itemStingerBullet;
    public static MCH_ItemLightWeaponBase itemStinger;
    public static MCH_ItemLightWeaponBullet itemJavelinBullet;
    public static MCH_ItemLightWeaponBase itemJavelin;
    public static MCH_ItemUavStation[] itemUavStation;
    public static MCH_ItemParachute itemParachute;
    public static MCH_ItemContainer itemContainer;
    public static MCH_ItemChain itemChain;
    public static MCH_ItemFuel itemFuel;
    public static MCH_ItemWrench itemWrench;
    public static MCH_ItemRangeFinder itemRangeFinder;
    public static MCH_ItemSpawnGunner itemSpawnGunnerVsPlayer;
    public static MCH_ItemSpawnGunner itemSpawnGunnerVsMonster;
    public static MCH_CreativeTabs creativeTabs;
    public static MCH_CreativeTabs creativeTabsHeli;
    public static MCH_CreativeTabs creativeTabsPlane;
    public static MCH_CreativeTabs creativeTabsTank;
    public static MCH_CreativeTabs creativeTabsVehicle;
    public static MCH_DraftingTableBlock blockDraftingTable;
    public static MCH_DraftingTableBlock blockDraftingTableLit;
    public static ItemBlock itemDraftingTable;
    public static Item sampleHelmet;
    private static Boolean isSep01;

    @Mod.EventHandler
    public void PreInit(FMLPreInitializationEvent evt) {
        MCH_Logger.setLogger(evt.getModLog());
        VER = Loader.instance().activeModContainer().getVersion();
        MCH_Lib.init();
        MCH_Lib.Log("MC Ver:1.12.2 MOD Ver:" + VER + "", new Object[0]);
        MCH_Lib.Log("Start load...", new Object[0]);
        sourcePath = evt.getSourceFile().getPath();
        sourceFile = evt.getSourceFile();
        addonDir = new File(evt.getModConfigurationDirectory().getParentFile(), "/mcheli_addons/");
        MCH_Lib.Log("SourcePath: " + sourcePath, new Object[0]);
        MCH_Lib.Log("CurrentDirectory:" + new File(".").getAbsolutePath(), new Object[0]);
        proxy.init();
        creativeTabs = new MCH_CreativeTabs("MC Heli Item");
        creativeTabsHeli = new MCH_CreativeTabs("MC Heli Helicopters");
        creativeTabsPlane = new MCH_CreativeTabs("MC Heli Planes");
        creativeTabsTank = new MCH_CreativeTabs("MC Heli Tanks");
        creativeTabsVehicle = new MCH_CreativeTabs("MC Heli Vehicles");
        W_ItemList.init();
        proxy.loadConfig("config/mcheli.cfg");
        config = MCH_MOD.proxy.config;
        ContentRegistries.loadContents(addonDir);
        MCH_SoundsJson.updateGenerated();
        MCH_Lib.Log("Register item", new Object[0]);
        this.registerItemSpawnGunner();
        this.registerItemRangeFinder();
        this.registerItemWrench();
        this.registerItemFuel();
        this.registerItemGLTD();
        this.registerItemChain();
        this.registerItemParachute();
        this.registerItemContainer();
        this.registerItemUavStation();
        this.registerItemInvisible();
        MCH_MOD.registerItemThrowable();
        this.registerItemLightWeaponBullet();
        this.registerItemLightWeapon();
        MCH_MOD.registerItemAircraft();
        blockDraftingTable = new MCH_DraftingTableBlock(MCH_Config.BlockID_DraftingTableOFF.prmInt, false);
        blockDraftingTable.func_149663_c("drafting_table");
        blockDraftingTable.func_149647_a(creativeTabs);
        blockDraftingTableLit = new MCH_DraftingTableBlock(MCH_Config.BlockID_DraftingTableON.prmInt, true);
        blockDraftingTableLit.func_149663_c("lit_drafting_table");
        MCH_Blocks.register((Block)blockDraftingTable, "drafting_table");
        MCH_Blocks.register((Block)blockDraftingTableLit, "lit_drafting_table");
        itemDraftingTable = MCH_Items.registerBlock((Block)blockDraftingTable);
        W_LanguageRegistry.addName((Object)blockDraftingTable, "Drafting Table");
        W_LanguageRegistry.addNameForObject((Object)blockDraftingTable, "ja_jp", "\u88fd\u56f3\u53f0");
        MCH_Achievement.PreInit();
        MCH_Lib.Log("Register system", new Object[0]);
        W_NetworkRegistry.registerChannel(packetHandler, MOD_CH);
        MinecraftForge.EVENT_BUS.register((Object)new MCH_EventHook());
        proxy.registerClientTick();
        W_NetworkRegistry.registerGuiHandler(this, new MCH_GuiCommonHandler());
        MCH_Lib.Log("Register entity", new Object[0]);
        this.registerEntity();
        MCH_Lib.Log("Register renderer", new Object[0]);
        proxy.registerRenderer();
        MCH_Lib.Log("Register models", new Object[0]);
        proxy.registerModels();
        MCH_Lib.Log("Register Sounds", new Object[0]);
        proxy.registerSounds();
        proxy.updateGeneratedLanguage();
        MCH_Lib.Log("End load", new Object[0]);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt) {
        GameRegistry.registerTileEntity(MCH_DraftingTableTileEntity.class, (ResourceLocation)MCH_Utils.suffix("drafting_table"));
        proxy.registerBlockRenderer();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        creativeTabs.setFixedIconItem(MCH_Config.CreativeTabIcon.prmString);
        creativeTabsHeli.setFixedIconItem(MCH_Config.CreativeTabIconHeli.prmString);
        creativeTabsPlane.setFixedIconItem(MCH_Config.CreativeTabIconPlane.prmString);
        creativeTabsTank.setFixedIconItem(MCH_Config.CreativeTabIconTank.prmString);
        creativeTabsVehicle.setFixedIconItem(MCH_Config.CreativeTabIconVehicle.prmString);
        proxy.registerRecipeDescriptions();
        MCH_WeaponInfoManager.setRoundItems();
        proxy.readClientModList();
    }

    @Mod.EventHandler
    public void onStartServer(FMLServerStartingEvent event) {
        proxy.registerServerTick();
    }

    public void registerEntity() {
        MCH_Entities.register(MCH_EntitySeat.class, "MCH.E.Seat", 100, this, 400, 10, true);
        MCH_Entities.register(MCH_EntityHeli.class, "MCH.E.Heli", 101, this, 400, 10, true);
        MCH_Entities.register(MCH_EntityGLTD.class, "MCH.E.GLTD", 102, this, 200, 10, true);
        MCH_Entities.register(MCP_EntityPlane.class, "MCH.E.Plane", 103, this, 400, 10, true);
        MCH_Entities.register(MCH_EntityChain.class, "MCH.E.Chain", 104, this, 200, 10, true);
        MCH_Entities.register(MCH_EntityHitBox.class, "MCH.E.PSeat", 105, this, 200, 10, true);
        MCH_Entities.register(MCH_EntityParachute.class, "MCH.E.Parachute", 106, this, 200, 10, true);
        MCH_Entities.register(MCH_EntityContainer.class, "MCH.E.Container", 107, this, 200, 10, true);
        MCH_Entities.register(MCH_EntityVehicle.class, "MCH.E.Vehicle", 108, this, 400, 10, true);
        MCH_Entities.register(MCH_EntityUavStation.class, "MCH.E.UavStation", 109, this, 400, 10, true);
        MCH_Entities.register(MCH_EntityHitBox.class, "MCH.E.HitBox", 110, this, 200, 10, true);
        MCH_Entities.register(MCH_EntityHide.class, "MCH.E.Hide", 111, this, 200, 10, true);
        MCH_Entities.register(MCH_EntityTank.class, "MCH.E.Tank", 112, this, 400, 10, true);
        MCH_Entities.register(MCH_EntityRocket.class, "MCH.E.Rocket", 200, this, 530, 5, true);
        MCH_Entities.register(MCH_EntityTvMissile.class, "MCH.E.TvMissle", 201, this, 530, 5, true);
        MCH_Entities.register(MCH_EntityBullet.class, "MCH.E.Bullet", 202, this, 530, 5, true);
        MCH_Entities.register(MCH_EntityA10.class, "MCH.E.A10", 203, this, 530, 5, true);
        MCH_Entities.register(MCH_EntityAAMissile.class, "MCH.E.AAM", 204, this, 530, 5, true);
        MCH_Entities.register(MCH_EntityASMissile.class, "MCH.E.ASM", 205, this, 530, 5, true);
        MCH_Entities.register(MCH_EntityTorpedo.class, "MCH.E.Torpedo", 206, this, 530, 5, true);
        MCH_Entities.register(MCH_EntityATMissile.class, "MCH.E.ATMissle", 207, this, 530, 5, true);
        MCH_Entities.register(MCH_EntityBomb.class, "MCH.E.Bomb", 208, this, 530, 5, true);
        MCH_Entities.register(MCH_EntityMarkerRocket.class, "MCH.E.MkRocket", 209, this, 530, 5, true);
        MCH_Entities.register(MCH_EntityDispensedItem.class, "MCH.E.DispItem", 210, this, 530, 5, true);
        MCH_Entities.register(MCH_EntityFlare.class, "MCH.E.Flare", 300, this, 330, 10, true);
        MCH_Entities.register(MCH_EntityThrowable.class, "MCH.E.Throwable", 400, this, 330, 10, true);
        MCH_Entities.register(MCH_EntityGunner.class, "MCH.E.Gunner", 500, this, 530, 5, true);
    }

    @Mod.EventHandler
    public void registerCommand(FMLServerStartedEvent e) {
        CommandHandler handler = (CommandHandler)FMLCommonHandler.instance().getSidedDelegate().getServer().func_71187_D();
        handler.func_71560_a((ICommand)new MCH_Command());
    }

    private void registerItemSpawnGunner() {
        String name = "spawn_gunner_vs_monster";
        MCH_ItemSpawnGunner item = new MCH_ItemSpawnGunner();
        item.targetType = 0;
        item.primaryColor = 0xC0C0A0;
        item.secondaryColor = 0xC00000;
        itemSpawnGunnerVsMonster = item;
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "Gunner (vs Monster)");
        W_LanguageRegistry.addNameForObject((Object)item, "ja_jp", "\u5bfe\u30e2\u30f3\u30b9\u30bf\u30fc \u5c04\u6483\u624b");
        name = "spawn_gunner_vs_player";
        item = new MCH_ItemSpawnGunner();
        item.targetType = 1;
        item.primaryColor = 0xC0C0A0;
        item.secondaryColor = 49152;
        itemSpawnGunnerVsPlayer = item;
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "Gunner (vs Player of other team)");
        W_LanguageRegistry.addNameForObject((Object)item, "ja_jp", "\u5bfe\u4ed6\u30c1\u30fc\u30e0\u30d7\u30ec\u30a4\u30e4\u30fc \u5c04\u6483\u624b");
    }

    private void registerItemRangeFinder() {
        MCH_ItemRangeFinder item;
        String name = "rangefinder";
        itemRangeFinder = item = new MCH_ItemRangeFinder(MCH_Config.ItemID_RangeFinder.prmInt);
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "Laser Rangefinder");
        W_LanguageRegistry.addNameForObject((Object)item, "ja_jp", "\u30ec\u30fc\u30b6\u30fc \u30ec\u30f3\u30b8 \u30d5\u30a1\u30a4\u30f3\u30c0\u30fc");
    }

    private void registerItemWrench() {
        MCH_ItemWrench item;
        String name = "wrench";
        itemWrench = item = new MCH_ItemWrench(MCH_Config.ItemID_Wrench.prmInt, Item.ToolMaterial.IRON);
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "Wrench");
        W_LanguageRegistry.addNameForObject((Object)item, "ja_jp", "\u30ec\u30f3\u30c1");
    }

    public void registerItemInvisible() {
        MCH_InvisibleItem item;
        String name = "internal";
        invisibleItem = item = new MCH_InvisibleItem(MCH_Config.ItemID_InvisibleItem.prmInt);
        MCH_MOD.registerItem(item, name, null);
    }

    public void registerItemUavStation() {
        String[] dispName = new String[]{"UAV Station", "Portable UAV Controller"};
        String[] localName = new String[]{"UAV\u30b9\u30c6\u30fc\u30b7\u30e7\u30f3", "\u643a\u5e2fUAV\u5236\u5fa1\u7aef\u672b"};
        itemUavStation = new MCH_ItemUavStation[MCH_ItemUavStation.UAV_STATION_KIND_NUM];
        String name = "uav_station";
        for (int i = 0; i < itemUavStation.length; ++i) {
            MCH_ItemUavStation item;
            String nn = i > 0 ? "" + (i + 1) : "";
            MCH_MOD.itemUavStation[i] = item = new MCH_ItemUavStation(MCH_Config.ItemID_UavStation[i].prmInt, 1 + i);
            MCH_MOD.registerItem(item, name + nn, creativeTabs);
            W_LanguageRegistry.addName((Object)item, dispName[i]);
            W_LanguageRegistry.addNameForObject((Object)item, "ja_jp", localName[i]);
        }
    }

    public void registerItemParachute() {
        MCH_ItemParachute item;
        String name = "parachute";
        itemParachute = item = new MCH_ItemParachute(MCH_Config.ItemID_Parachute.prmInt);
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "Parachute");
        W_LanguageRegistry.addNameForObject((Object)item, "ja_jp", "\u30d1\u30e9\u30b7\u30e5\u30fc\u30c8");
    }

    public void registerItemContainer() {
        MCH_ItemContainer item;
        String name = "container";
        itemContainer = item = new MCH_ItemContainer(MCH_Config.ItemID_Container.prmInt);
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "Container");
        W_LanguageRegistry.addNameForObject((Object)item, "ja_jp", "\u30b3\u30f3\u30c6\u30ca");
    }

    public void registerItemLightWeapon() {
        MCH_ItemLightWeaponBase item;
        String name = "fim92";
        itemStinger = item = new MCH_ItemLightWeaponBase(MCH_Config.ItemID_Stinger.prmInt, itemStingerBullet);
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "FIM-92 Stinger");
        name = "fgm148";
        itemJavelin = item = new MCH_ItemLightWeaponBase(MCH_Config.ItemID_Stinger.prmInt, itemJavelinBullet);
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "FGM-148 Javelin");
    }

    public void registerItemLightWeaponBullet() {
        MCH_ItemLightWeaponBullet item;
        String name = "fim92_bullet";
        itemStingerBullet = item = new MCH_ItemLightWeaponBullet(MCH_Config.ItemID_StingerMissile.prmInt);
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "FIM-92 Stinger missile");
        name = "fgm148_bullet";
        itemJavelinBullet = item = new MCH_ItemLightWeaponBullet(MCH_Config.ItemID_StingerMissile.prmInt);
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "FGM-148 Javelin missile");
    }

    public void registerItemChain() {
        MCH_ItemChain item;
        String name = "chain";
        itemChain = item = new MCH_ItemChain(MCH_Config.ItemID_Chain.prmInt);
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "Chain");
        W_LanguageRegistry.addNameForObject((Object)item, "ja_jp", "\u9396");
    }

    public void registerItemFuel() {
        MCH_ItemFuel item;
        String name = "fuel";
        itemFuel = item = new MCH_ItemFuel(MCH_Config.ItemID_Fuel.prmInt);
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "Fuel");
        W_LanguageRegistry.addNameForObject((Object)item, "ja_jp", "\u71c3\u6599");
    }

    public void registerItemGLTD() {
        MCH_ItemGLTD item;
        String name = "gltd";
        itemGLTD = item = new MCH_ItemGLTD(MCH_Config.ItemID_GLTD.prmInt);
        MCH_MOD.registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName((Object)item, "GLTD:Target Designator");
        W_LanguageRegistry.addNameForObject((Object)item, "ja_jp", "GLTD:\u30ec\u30fc\u30b6\u30fc\u76ee\u6a19\u6307\u793a\u88c5\u7f6e");
    }

    public static void registerItem(W_Item item, String name, MCH_CreativeTabs ct) {
        item.func_77655_b("mcheli:" + name);
        if (ct != null) {
            item.func_77637_a(ct);
            ct.addIconItem(item);
        }
        MCH_Items.register(item, name);
    }

    public static void registerItemThrowable() {
        for (Map.Entry<String, MCH_ThrowableInfo> entry : ContentRegistries.throwable().entries()) {
            MCH_ThrowableInfo info = entry.getValue();
            info.item = new MCH_ItemThrowable(info.itemID);
            info.item.func_77625_d(info.stackSize);
            MCH_MOD.registerItem(info.item, entry.getKey(), creativeTabs);
            MCH_ItemThrowable.registerDispenseBehavior(info.item);
            info.itemID = W_Item.getIdFromItem(info.item) - 256;
            W_LanguageRegistry.addName((Object)info.item, info.displayName);
            for (String lang : info.displayNameLang.keySet()) {
                W_LanguageRegistry.addNameForObject((Object)info.item, lang, info.displayNameLang.get(lang));
            }
        }
    }

    public static void registerItemAircraft() {
        MCH_AircraftInfo info;
        for (Map.Entry<String, MCH_HeliInfo> entry : ContentRegistries.heli().entries()) {
            info = entry.getValue();
            info.item = new MCH_ItemHeli(info.itemID);
            info.item.func_77656_e(info.maxHp);
            if (!info.canRide && (info.ammoSupplyRange > 0.0f || info.fuelSupplyRange > 0.0f)) {
                MCH_MOD.registerItem(info.item, entry.getKey(), creativeTabs);
            } else {
                MCH_MOD.registerItem(info.item, entry.getKey(), creativeTabsHeli);
            }
            MCH_ItemAircraft.registerDispenseBehavior(info.item);
            info.itemID = W_Item.getIdFromItem(info.item) - 256;
            W_LanguageRegistry.addName((Object)info.item, info.displayName);
            for (String lang : info.displayNameLang.keySet()) {
                W_LanguageRegistry.addNameForObject((Object)info.item, lang, (String)info.displayNameLang.get(lang));
            }
        }
        for (Map.Entry<String, MCH_AircraftInfo> entry : ContentRegistries.plane().entries()) {
            info = (MCP_PlaneInfo)entry.getValue();
            ((MCP_PlaneInfo)info).item = new MCP_ItemPlane(((MCP_PlaneInfo)info).itemID);
            ((MCP_PlaneInfo)info).item.func_77656_e(((MCP_PlaneInfo)info).maxHp);
            if (!((MCP_PlaneInfo)info).canRide && (((MCP_PlaneInfo)info).ammoSupplyRange > 0.0f || ((MCP_PlaneInfo)info).fuelSupplyRange > 0.0f)) {
                MCH_MOD.registerItem(((MCP_PlaneInfo)info).item, entry.getKey(), creativeTabs);
            } else {
                MCH_MOD.registerItem(((MCP_PlaneInfo)info).item, entry.getKey(), creativeTabsPlane);
            }
            MCH_ItemAircraft.registerDispenseBehavior(((MCP_PlaneInfo)info).item);
            ((MCP_PlaneInfo)info).itemID = W_Item.getIdFromItem(((MCP_PlaneInfo)info).item) - 256;
            W_LanguageRegistry.addName((Object)((MCP_PlaneInfo)info).item, ((MCP_PlaneInfo)info).displayName);
            for (String lang : ((MCP_PlaneInfo)info).displayNameLang.keySet()) {
                W_LanguageRegistry.addNameForObject((Object)((MCP_PlaneInfo)info).item, lang, (String)((MCP_PlaneInfo)info).displayNameLang.get(lang));
            }
        }
        for (Map.Entry<String, MCH_AircraftInfo> entry : ContentRegistries.tank().entries()) {
            info = (MCH_TankInfo)entry.getValue();
            ((MCH_TankInfo)info).item = new MCH_ItemTank(((MCH_TankInfo)info).itemID);
            ((MCH_TankInfo)info).item.func_77656_e(((MCH_TankInfo)info).maxHp);
            if (!((MCH_TankInfo)info).canRide && (((MCH_TankInfo)info).ammoSupplyRange > 0.0f || ((MCH_TankInfo)info).fuelSupplyRange > 0.0f)) {
                MCH_MOD.registerItem(((MCH_TankInfo)info).item, entry.getKey(), creativeTabs);
            } else {
                MCH_MOD.registerItem(((MCH_TankInfo)info).item, entry.getKey(), creativeTabsTank);
            }
            MCH_ItemAircraft.registerDispenseBehavior(((MCH_TankInfo)info).item);
            ((MCH_TankInfo)info).itemID = W_Item.getIdFromItem(((MCH_TankInfo)info).item) - 256;
            W_LanguageRegistry.addName((Object)((MCH_TankInfo)info).item, ((MCH_TankInfo)info).displayName);
            for (String lang : ((MCH_TankInfo)info).displayNameLang.keySet()) {
                W_LanguageRegistry.addNameForObject((Object)((MCH_TankInfo)info).item, lang, (String)((MCH_TankInfo)info).displayNameLang.get(lang));
            }
        }
        for (Map.Entry<String, MCH_AircraftInfo> entry : ContentRegistries.vehicle().entries()) {
            info = (MCH_VehicleInfo)entry.getValue();
            ((MCH_VehicleInfo)info).item = new MCH_ItemVehicle(((MCH_VehicleInfo)info).itemID);
            ((MCH_VehicleInfo)info).item.func_77656_e(((MCH_VehicleInfo)info).maxHp);
            if (!((MCH_VehicleInfo)info).canRide && (((MCH_VehicleInfo)info).ammoSupplyRange > 0.0f || ((MCH_VehicleInfo)info).fuelSupplyRange > 0.0f)) {
                MCH_MOD.registerItem(((MCH_VehicleInfo)info).item, entry.getKey(), creativeTabs);
            } else {
                MCH_MOD.registerItem(((MCH_VehicleInfo)info).item, entry.getKey(), creativeTabsVehicle);
            }
            MCH_ItemAircraft.registerDispenseBehavior(((MCH_VehicleInfo)info).item);
            ((MCH_VehicleInfo)info).itemID = W_Item.getIdFromItem(((MCH_VehicleInfo)info).item) - 256;
            W_LanguageRegistry.addName((Object)((MCH_VehicleInfo)info).item, ((MCH_VehicleInfo)info).displayName);
            for (String lang : ((MCH_VehicleInfo)info).displayNameLang.keySet()) {
                W_LanguageRegistry.addNameForObject((Object)((MCH_VehicleInfo)info).item, lang, (String)((MCH_VehicleInfo)info).displayNameLang.get(lang));
            }
        }
    }

    @Deprecated
    public static Logger getLogger() {
        return MCH_Logger.get();
    }

    public static File getSource() {
        return sourceFile;
    }

    public static File getAddonDir() {
        return addonDir;
    }

    public static boolean isTodaySep01() {
        if (isSep01 == null) {
            Calendar c = Calendar.getInstance();
            isSep01 = c.get(2) + 1 == 9 && c.get(5) == 1;
        }
        return isSep01;
    }

    static {
        packetHandler = new MCH_PacketHandler();
        isSep01 = null;
    }
}

