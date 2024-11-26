/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.Item
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraftforge.client.model.ICustomModelLoader
 *  net.minecraftforge.client.model.ModelLoaderRegistry
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.client.registry.ClientRegistry
 *  net.minecraftforge.fml.client.registry.RenderingRegistry
 *  net.minecraftforge.fml.relauncher.Side
 */
package mcheli;

import java.io.File;
import java.util.List;
import mcheli.MCH_ClientCommonTickHandler;
import mcheli.MCH_ClientEventHook;
import mcheli.MCH_CommonProxy;
import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.MCH_MOD;
import mcheli.MCH_ModelManager;
import mcheli.MCH_RenderNull;
import mcheli.MCH_ViewEntityDummy;
import mcheli.__helper.addon.AddonManager;
import mcheli.__helper.addon.AddonPack;
import mcheli.__helper.client.MCH_ItemModelRenderers;
import mcheli.__helper.client.RecipeDescriptionManager;
import mcheli.__helper.client._IModelCustom;
import mcheli.__helper.client.model.LegacyModelLoader;
import mcheli.__helper.client.renderer.item.BuiltInDraftingTableItemRenderer;
import mcheli.__helper.client.renderer.item.BuiltInGLTDItemRenderer;
import mcheli.__helper.client.renderer.item.BuiltInInvisibleItemRenderer;
import mcheli.__helper.client.renderer.item.BuiltInLightWeaponItemRenderer;
import mcheli.__helper.client.renderer.item.BuiltInRangeFinderItemRenderer;
import mcheli.__helper.client.renderer.item.BuiltInWrenchItemRenderer;
import mcheli.__helper.info.ContentRegistries;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntityHide;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.aircraft.MCH_RenderAircraft;
import mcheli.aircraft.MCH_SoundUpdater;
import mcheli.block.MCH_DraftingTableRenderer;
import mcheli.block.MCH_DraftingTableTileEntity;
import mcheli.chain.MCH_EntityChain;
import mcheli.chain.MCH_RenderChain;
import mcheli.command.MCH_GuiTitle;
import mcheli.container.MCH_EntityContainer;
import mcheli.container.MCH_RenderContainer;
import mcheli.debug.MCH_RenderTest;
import mcheli.flare.MCH_EntityFlare;
import mcheli.flare.MCH_RenderFlare;
import mcheli.gltd.MCH_EntityGLTD;
import mcheli.gltd.MCH_RenderGLTD;
import mcheli.helicopter.MCH_EntityHeli;
import mcheli.helicopter.MCH_HeliInfo;
import mcheli.helicopter.MCH_RenderHeli;
import mcheli.mob.MCH_EntityGunner;
import mcheli.mob.MCH_RenderGunner;
import mcheli.multiplay.MCH_MultiplayClient;
import mcheli.parachute.MCH_EntityParachute;
import mcheli.parachute.MCH_RenderParachute;
import mcheli.particles.MCH_ParticlesUtil;
import mcheli.plane.MCP_EntityPlane;
import mcheli.plane.MCP_PlaneInfo;
import mcheli.plane.MCP_RenderPlane;
import mcheli.tank.MCH_EntityTank;
import mcheli.tank.MCH_RenderTank;
import mcheli.tank.MCH_TankInfo;
import mcheli.throwable.MCH_EntityThrowable;
import mcheli.throwable.MCH_RenderThrowable;
import mcheli.throwable.MCH_ThrowableInfo;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.uav.MCH_RenderUavStation;
import mcheli.vehicle.MCH_EntityVehicle;
import mcheli.vehicle.MCH_RenderVehicle;
import mcheli.vehicle.MCH_VehicleInfo;
import mcheli.weapon.MCH_BulletModel;
import mcheli.weapon.MCH_DefaultBulletModels;
import mcheli.weapon.MCH_EntityA10;
import mcheli.weapon.MCH_EntityAAMissile;
import mcheli.weapon.MCH_EntityASMissile;
import mcheli.weapon.MCH_EntityATMissile;
import mcheli.weapon.MCH_EntityBomb;
import mcheli.weapon.MCH_EntityBullet;
import mcheli.weapon.MCH_EntityCartridge;
import mcheli.weapon.MCH_EntityDispensedItem;
import mcheli.weapon.MCH_EntityMarkerRocket;
import mcheli.weapon.MCH_EntityRocket;
import mcheli.weapon.MCH_EntityTorpedo;
import mcheli.weapon.MCH_EntityTvMissile;
import mcheli.weapon.MCH_RenderA10;
import mcheli.weapon.MCH_RenderAAMissile;
import mcheli.weapon.MCH_RenderASMissile;
import mcheli.weapon.MCH_RenderBomb;
import mcheli.weapon.MCH_RenderBullet;
import mcheli.weapon.MCH_RenderCartridge;
import mcheli.weapon.MCH_RenderNone;
import mcheli.weapon.MCH_RenderTvMissile;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.wrapper.W_LanguageRegistry;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_Reflection;
import mcheli.wrapper.W_TickRegistry;
import mcheli.wrapper.modelloader.W_ModelCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_ClientProxy
extends MCH_CommonProxy {
    public String lastLoadHUDPath = "";

    @Override
    public String getDataDir() {
        return Minecraft.func_71410_x().field_71412_D.getPath();
    }

    @Override
    public void registerRenderer() {
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntitySeat.class, MCH_RenderTest.factory(0.0f, 0.3125f, 0.0f, "seat"));
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityHeli.class, MCH_RenderHeli.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCP_EntityPlane.class, MCP_RenderPlane.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityTank.class, MCH_RenderTank.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityGLTD.class, MCH_RenderGLTD.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityChain.class, MCH_RenderChain.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityParachute.class, MCH_RenderParachute.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityContainer.class, MCH_RenderContainer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityVehicle.class, MCH_RenderVehicle.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityUavStation.class, MCH_RenderUavStation.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityCartridge.class, MCH_RenderCartridge.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityHide.class, MCH_RenderNull.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_ViewEntityDummy.class, MCH_RenderNull.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityRocket.class, MCH_RenderBullet.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityTvMissile.class, MCH_RenderTvMissile.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityBullet.class, MCH_RenderBullet.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityA10.class, MCH_RenderA10.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityAAMissile.class, MCH_RenderAAMissile.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityASMissile.class, MCH_RenderASMissile.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityATMissile.class, MCH_RenderTvMissile.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityTorpedo.class, MCH_RenderBullet.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityBomb.class, MCH_RenderBomb.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityMarkerRocket.class, MCH_RenderBullet.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityDispensedItem.class, MCH_RenderNone.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityFlare.class, MCH_RenderFlare.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityThrowable.class, MCH_RenderThrowable.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityGunner.class, MCH_RenderGunner.FACTORY);
        MCH_ItemModelRenderers.registerRenderer(MCH_MOD.itemJavelin, new BuiltInLightWeaponItemRenderer());
        MCH_ItemModelRenderers.registerRenderer(MCH_MOD.itemStinger, new BuiltInLightWeaponItemRenderer());
        MCH_ItemModelRenderers.registerRenderer(MCH_MOD.invisibleItem, new BuiltInInvisibleItemRenderer());
        MCH_ItemModelRenderers.registerRenderer(MCH_MOD.itemGLTD, new BuiltInGLTDItemRenderer());
        MCH_ItemModelRenderers.registerRenderer(MCH_MOD.itemWrench, new BuiltInWrenchItemRenderer());
        MCH_ItemModelRenderers.registerRenderer(MCH_MOD.itemRangeFinder, new BuiltInRangeFinderItemRenderer());
        MCH_ItemModelRenderers.registerRenderer((Item)MCH_MOD.itemDraftingTable, new BuiltInDraftingTableItemRenderer());
        ModelLoaderRegistry.registerLoader((ICustomModelLoader)LegacyModelLoader.INSTANCE);
    }

    @Override
    public void registerBlockRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(MCH_DraftingTableTileEntity.class, (TileEntitySpecialRenderer)new MCH_DraftingTableRenderer());
    }

    @Override
    public void registerModels() {
        MCH_ModelManager.setForceReloadMode(true);
        MCH_RenderAircraft.debugModel = MCH_ModelManager.load("box");
        MCH_ModelManager.load("a-10");
        MCH_RenderGLTD.model = MCH_ModelManager.load("gltd");
        MCH_ModelManager.load("chain");
        MCH_ModelManager.load("container");
        MCH_ModelManager.load("parachute1");
        MCH_ModelManager.load("parachute2");
        MCH_ModelManager.load("lweapons", "fim92");
        MCH_ModelManager.load("lweapons", "fgm148");
        for (String s : MCH_RenderUavStation.MODEL_NAME) {
            MCH_ModelManager.load(s);
        }
        MCH_ModelManager.load("wrench");
        MCH_ModelManager.load("rangefinder");
        ContentRegistries.heli().forEachValue(info -> this.registerModelsHeli((MCH_HeliInfo)info, false));
        ContentRegistries.plane().forEachValue(info -> this.registerModelsPlane((MCP_PlaneInfo)info, false));
        ContentRegistries.tank().forEachValue(info -> this.registerModelsTank((MCH_TankInfo)info, false));
        ContentRegistries.vehicle().forEachValue(info -> this.registerModelsVehicle((MCH_VehicleInfo)info, false));
        MCH_ClientProxy.registerModels_Bullet();
        MCH_DefaultBulletModels.Bullet = this.loadBulletModel("bullet");
        MCH_DefaultBulletModels.AAMissile = this.loadBulletModel("aamissile");
        MCH_DefaultBulletModels.ATMissile = this.loadBulletModel("asmissile");
        MCH_DefaultBulletModels.ASMissile = this.loadBulletModel("asmissile");
        MCH_DefaultBulletModels.Bomb = this.loadBulletModel("bomb");
        MCH_DefaultBulletModels.Rocket = this.loadBulletModel("rocket");
        MCH_DefaultBulletModels.Torpedo = this.loadBulletModel("torpedo");
        for (MCH_ThrowableInfo wi : ContentRegistries.throwable().values()) {
            wi.model = MCH_ModelManager.load("throwable", wi.name);
        }
        MCH_ModelManager.load("blocks", "drafting_table");
    }

    public static void registerModels_Bullet() {
        for (MCH_WeaponInfo wi : ContentRegistries.weapon().values()) {
            _IModelCustom m = null;
            if (!wi.bulletModelName.isEmpty() && (m = MCH_ModelManager.load("bullets", wi.bulletModelName)) != null) {
                wi.bulletModel = new MCH_BulletModel(wi.bulletModelName, m);
            }
            if (!wi.bombletModelName.isEmpty() && (m = MCH_ModelManager.load("bullets", wi.bombletModelName)) != null) {
                wi.bombletModel = new MCH_BulletModel(wi.bombletModelName, m);
            }
            if (wi.cartridge == null || wi.cartridge.name.isEmpty()) continue;
            wi.cartridge.model = MCH_ModelManager.load("bullets", wi.cartridge.name);
            if (wi.cartridge.model != null) continue;
            wi.cartridge = null;
        }
    }

    @Override
    public void registerModelsHeli(MCH_HeliInfo info, boolean reload) {
        MCH_ModelManager.setForceReloadMode(reload);
        info.model = MCH_ModelManager.load("helicopters", info.name);
        for (MCH_HeliInfo.Rotor rotor : info.rotorList) {
            rotor.model = this.loadPartModel("helicopters", info.name, info.model, rotor.modelName);
        }
        this.registerCommonPart("helicopters", info);
        MCH_ModelManager.setForceReloadMode(false);
    }

    @Override
    public void registerModelsPlane(MCP_PlaneInfo info, boolean reload) {
        MCH_ModelManager.setForceReloadMode(reload);
        info.model = MCH_ModelManager.load("planes", info.name);
        for (MCH_AircraftInfo.DrawnPart n : info.nozzles) {
            n.model = this.loadPartModel("planes", info.name, info.model, n.modelName);
        }
        for (MCP_PlaneInfo.Rotor r : info.rotorList) {
            r.model = this.loadPartModel("planes", info.name, info.model, r.modelName);
            for (MCP_PlaneInfo.Blade b : r.blades) {
                b.model = this.loadPartModel("planes", info.name, info.model, b.modelName);
            }
        }
        for (MCP_PlaneInfo.Wing w : info.wingList) {
            w.model = this.loadPartModel("planes", info.name, info.model, w.modelName);
            if (w.pylonList == null) continue;
            for (MCP_PlaneInfo.Pylon p : w.pylonList) {
                p.model = this.loadPartModel("planes", info.name, info.model, p.modelName);
            }
        }
        this.registerCommonPart("planes", info);
        MCH_ModelManager.setForceReloadMode(false);
    }

    @Override
    public void registerModelsVehicle(MCH_VehicleInfo info, boolean reload) {
        MCH_ModelManager.setForceReloadMode(reload);
        info.model = MCH_ModelManager.load("vehicles", info.name);
        for (MCH_VehicleInfo.VPart vp : info.partList) {
            vp.model = this.loadPartModel("vehicles", info.name, info.model, vp.modelName);
            if (vp.child == null) continue;
            this.registerVCPModels(info, vp);
        }
        this.registerCommonPart("vehicles", info);
        MCH_ModelManager.setForceReloadMode(false);
    }

    @Override
    public void registerModelsTank(MCH_TankInfo info, boolean reload) {
        MCH_ModelManager.setForceReloadMode(reload);
        info.model = MCH_ModelManager.load("tanks", info.name);
        this.registerCommonPart("tanks", info);
        MCH_ModelManager.setForceReloadMode(false);
    }

    private MCH_BulletModel loadBulletModel(String name) {
        _IModelCustom m = MCH_ModelManager.load("bullets", name);
        return m != null ? new MCH_BulletModel(name, m) : null;
    }

    private _IModelCustom loadPartModel(String path, String name, _IModelCustom body, String part) {
        if (body instanceof W_ModelCustom && ((W_ModelCustom)body).containsPart("$" + part)) {
            return null;
        }
        return MCH_ModelManager.load(path, name + "_" + part);
    }

    private void registerCommonPart(String path, MCH_AircraftInfo info) {
        for (MCH_AircraftInfo.Hatch hatch : info.hatchList) {
            hatch.model = this.loadPartModel(path, info.name, info.model, hatch.modelName);
        }
        for (MCH_AircraftInfo.Camera camera : info.cameraList) {
            camera.model = this.loadPartModel(path, info.name, info.model, camera.modelName);
        }
        for (MCH_AircraftInfo.Throttle throttle : info.partThrottle) {
            throttle.model = this.loadPartModel(path, info.name, info.model, throttle.modelName);
        }
        for (MCH_AircraftInfo.RotPart rotPart : info.partRotPart) {
            rotPart.model = this.loadPartModel(path, info.name, info.model, rotPart.modelName);
        }
        for (MCH_AircraftInfo.PartWeapon partWeapon : info.partWeapon) {
            partWeapon.model = this.loadPartModel(path, info.name, info.model, partWeapon.modelName);
            for (MCH_AircraftInfo.PartWeaponChild wc : partWeapon.child) {
                wc.model = this.loadPartModel(path, info.name, info.model, wc.modelName);
            }
        }
        for (MCH_AircraftInfo.Canopy canopy : info.canopyList) {
            canopy.model = this.loadPartModel(path, info.name, info.model, canopy.modelName);
        }
        for (MCH_AircraftInfo.DrawnPart drawnPart : info.landingGear) {
            drawnPart.model = this.loadPartModel(path, info.name, info.model, drawnPart.modelName);
        }
        for (MCH_AircraftInfo.WeaponBay weaponBay : info.partWeaponBay) {
            weaponBay.model = this.loadPartModel(path, info.name, info.model, weaponBay.modelName);
        }
        for (MCH_AircraftInfo.CrawlerTrack crawlerTrack : info.partCrawlerTrack) {
            crawlerTrack.model = this.loadPartModel(path, info.name, info.model, crawlerTrack.modelName);
        }
        for (MCH_AircraftInfo.TrackRoller trackRoller : info.partTrackRoller) {
            trackRoller.model = this.loadPartModel(path, info.name, info.model, trackRoller.modelName);
        }
        for (MCH_AircraftInfo.PartWheel partWheel : info.partWheel) {
            partWheel.model = this.loadPartModel(path, info.name, info.model, partWheel.modelName);
        }
        for (MCH_AircraftInfo.PartWheel partWheel : info.partSteeringWheel) {
            partWheel.model = this.loadPartModel(path, info.name, info.model, partWheel.modelName);
        }
    }

    private void registerVCPModels(MCH_VehicleInfo info, MCH_VehicleInfo.VPart vp) {
        for (MCH_VehicleInfo.VPart vcp : vp.child) {
            vcp.model = this.loadPartModel("vehicles", info.name, info.model, vcp.modelName);
            if (vcp.child == null) continue;
            this.registerVCPModels(info, vcp);
        }
    }

    @Override
    public void registerClientTick() {
        Minecraft mc = Minecraft.func_71410_x();
        MCH_ClientCommonTickHandler.instance = new MCH_ClientCommonTickHandler(mc, MCH_MOD.config);
        W_TickRegistry.registerTickHandler(MCH_ClientCommonTickHandler.instance, Side.CLIENT);
    }

    @Override
    public boolean isRemote() {
        return true;
    }

    @Override
    public String side() {
        return "Client";
    }

    @Override
    public MCH_SoundUpdater CreateSoundUpdater(MCH_EntityAircraft aircraft) {
        if (aircraft == null || !aircraft.field_70170_p.field_72995_K) {
            return null;
        }
        return new MCH_SoundUpdater(Minecraft.func_71410_x(), aircraft, Minecraft.func_71410_x().field_71439_g);
    }

    @Override
    public void registerSounds() {
        super.registerSounds();
        W_McClient.addSound("alert.ogg");
        W_McClient.addSound("locked.ogg");
        W_McClient.addSound("gltd.ogg");
        W_McClient.addSound("zoom.ogg");
        W_McClient.addSound("ng.ogg");
        W_McClient.addSound("a-10_snd.ogg");
        W_McClient.addSound("gau-8_snd.ogg");
        W_McClient.addSound("hit.ogg");
        W_McClient.addSound("helidmg.ogg");
        W_McClient.addSound("heli.ogg");
        W_McClient.addSound("plane.ogg");
        W_McClient.addSound("plane_cc.ogg");
        W_McClient.addSound("plane_cv.ogg");
        W_McClient.addSound("chain.ogg");
        W_McClient.addSound("chain_ct.ogg");
        W_McClient.addSound("eject_seat.ogg");
        W_McClient.addSound("fim92_snd.ogg");
        W_McClient.addSound("fim92_reload.ogg");
        W_McClient.addSound("lockon.ogg");
        for (MCH_WeaponInfo mCH_WeaponInfo : ContentRegistries.weapon().values()) {
            W_McClient.addSound(mCH_WeaponInfo.soundFileName + ".ogg");
        }
        for (MCH_AircraftInfo mCH_AircraftInfo : ContentRegistries.plane().values()) {
            if (mCH_AircraftInfo.soundMove.isEmpty()) continue;
            W_McClient.addSound(mCH_AircraftInfo.soundMove + ".ogg");
        }
        for (MCH_AircraftInfo mCH_AircraftInfo : ContentRegistries.heli().values()) {
            if (mCH_AircraftInfo.soundMove.isEmpty()) continue;
            W_McClient.addSound(mCH_AircraftInfo.soundMove + ".ogg");
        }
        for (MCH_AircraftInfo mCH_AircraftInfo : ContentRegistries.tank().values()) {
            if (mCH_AircraftInfo.soundMove.isEmpty()) continue;
            W_McClient.addSound(mCH_AircraftInfo.soundMove + ".ogg");
        }
        for (MCH_AircraftInfo mCH_AircraftInfo : ContentRegistries.vehicle().values()) {
            if (mCH_AircraftInfo.soundMove.isEmpty()) continue;
            W_McClient.addSound(mCH_AircraftInfo.soundMove + ".ogg");
        }
    }

    @Override
    public void loadConfig(String fileName) {
        this.lastConfigFileName = fileName;
        this.config = new MCH_Config(Minecraft.func_71410_x().field_71412_D.getPath(), "/" + fileName);
        this.config.load();
        this.config.write();
    }

    @Override
    public void reconfig() {
        MCH_Lib.DbgLog(false, "MCH_ClientProxy.reconfig()", new Object[0]);
        this.loadConfig(this.lastConfigFileName);
        MCH_ClientCommonTickHandler.instance.updatekeybind(this.config);
    }

    @Override
    public void reloadHUD() {
        ContentRegistries.hud().reloadAll();
    }

    @Override
    public Entity getClientPlayer() {
        return Minecraft.func_71410_x().field_71439_g;
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register((Object)new MCH_ParticlesUtil());
        MinecraftForge.EVENT_BUS.register((Object)new MCH_ClientEventHook());
    }

    @Override
    public void setCreativeDigDelay(int n) {
        W_Reflection.setCreativeDigSpeed(n);
    }

    @Override
    public boolean isFirstPerson() {
        return Minecraft.func_71410_x().field_71474_y.field_74320_O == 0;
    }

    @Override
    public boolean isSinglePlayer() {
        return Minecraft.func_71410_x().func_71356_B();
    }

    @Override
    public void readClientModList() {
        try {
            Minecraft mc = Minecraft.func_71410_x();
            MCH_MultiplayClient.readModList(mc.func_110432_I().func_148255_b(), mc.func_110432_I().func_111285_a());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printChatMessage(ITextComponent chat, int showTime, int pos) {
        ((MCH_GuiTitle)MCH_ClientCommonTickHandler.instance.gui_Title).setupTitle(chat, showTime, pos);
    }

    @Override
    public void hitBullet() {
        MCH_ClientCommonTickHandler.instance.gui_Common.hitBullet();
    }

    @Override
    public void clientLocked() {
        MCH_ClientCommonTickHandler.isLocked = true;
    }

    @Override
    public void setRenderEntityDistanceWeight(double renderDistWeight) {
        Entity.func_184227_b((double)renderDistWeight);
    }

    @Override
    public List<AddonPack> loadAddonPacks(File addonDir) {
        return AddonManager.loadAddonsAndAddResources(addonDir);
    }

    @Override
    public boolean canLoadContentDirName(String dir) {
        return "hud".equals(dir) || super.canLoadContentDirName(dir);
    }

    @Override
    public void updateGeneratedLanguage() {
        W_LanguageRegistry.updateGeneratedLang();
    }

    @Override
    public void registerRecipeDescriptions() {
        RecipeDescriptionManager.registerDescriptionInfos(Minecraft.func_71410_x().func_110442_L());
    }
}

