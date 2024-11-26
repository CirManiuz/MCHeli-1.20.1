/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.text.ITextComponent
 */
package mcheli;

import java.io.File;
import java.util.List;
import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.__helper.MCH_SoundEvents;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.addon.AddonManager;
import mcheli.__helper.addon.AddonPack;
import mcheli.__helper.addon.AddonResourceLocation;
import mcheli.__helper.info.ContentRegistries;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_SoundUpdater;
import mcheli.helicopter.MCH_HeliInfo;
import mcheli.plane.MCP_PlaneInfo;
import mcheli.tank.MCH_TankInfo;
import mcheli.vehicle.MCH_VehicleInfo;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.wrapper.W_LanguageRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;

public class MCH_CommonProxy {
    protected static String[] CONTENT_DIRS = new String[]{"helicopters", "planes", "tanks", "vehicles", "weapons", "throwable"};
    public MCH_Config config = null;
    public String lastConfigFileName;

    public String getDataDir() {
        return MCH_Utils.getServer().func_71270_I();
    }

    public void registerRenderer() {
    }

    public void registerBlockRenderer() {
    }

    public void registerModels() {
    }

    public void registerModelsHeli(MCH_HeliInfo info, boolean reload) {
    }

    public void registerModelsPlane(MCP_PlaneInfo info, boolean reload) {
    }

    public void registerModelsVehicle(MCH_VehicleInfo info, boolean reload) {
    }

    public void registerModelsTank(MCH_TankInfo info, boolean reload) {
    }

    public void registerClientTick() {
    }

    public void registerServerTick() {
    }

    public boolean isRemote() {
        return false;
    }

    public String side() {
        return "Server";
    }

    public MCH_SoundUpdater CreateSoundUpdater(MCH_EntityAircraft aircraft) {
        return null;
    }

    public void registerSounds() {
        MCH_SoundEvents.registerSoundEventName("alert");
        MCH_SoundEvents.registerSoundEventName("locked");
        MCH_SoundEvents.registerSoundEventName("gltd");
        MCH_SoundEvents.registerSoundEventName("zoom");
        MCH_SoundEvents.registerSoundEventName("ng");
        MCH_SoundEvents.registerSoundEventName("a-10_snd");
        MCH_SoundEvents.registerSoundEventName("gau-8_snd");
        MCH_SoundEvents.registerSoundEventName("hit");
        MCH_SoundEvents.registerSoundEventName("helidmg");
        MCH_SoundEvents.registerSoundEventName("heli");
        MCH_SoundEvents.registerSoundEventName("plane");
        MCH_SoundEvents.registerSoundEventName("plane_cc");
        MCH_SoundEvents.registerSoundEventName("plane_cv");
        MCH_SoundEvents.registerSoundEventName("chain");
        MCH_SoundEvents.registerSoundEventName("chain_ct");
        MCH_SoundEvents.registerSoundEventName("eject_seat");
        MCH_SoundEvents.registerSoundEventName("fim92_snd");
        MCH_SoundEvents.registerSoundEventName("fim92_reload");
        MCH_SoundEvents.registerSoundEventName("lockon");
        MCH_SoundEvents.registerSoundEventName("wrench");
        for (MCH_WeaponInfo mCH_WeaponInfo : ContentRegistries.weapon().values()) {
            MCH_SoundEvents.registerSoundEventName(mCH_WeaponInfo.soundFileName);
        }
        for (MCH_AircraftInfo mCH_AircraftInfo : ContentRegistries.plane().values()) {
            if (mCH_AircraftInfo.soundMove.isEmpty()) continue;
            MCH_SoundEvents.registerSoundEventName(mCH_AircraftInfo.soundMove);
        }
        for (MCH_AircraftInfo mCH_AircraftInfo : ContentRegistries.heli().values()) {
            if (mCH_AircraftInfo.soundMove.isEmpty()) continue;
            MCH_SoundEvents.registerSoundEventName(mCH_AircraftInfo.soundMove);
        }
        for (MCH_AircraftInfo mCH_AircraftInfo : ContentRegistries.tank().values()) {
            if (mCH_AircraftInfo.soundMove.isEmpty()) continue;
            MCH_SoundEvents.registerSoundEventName(mCH_AircraftInfo.soundMove);
        }
        for (MCH_AircraftInfo mCH_AircraftInfo : ContentRegistries.vehicle().values()) {
            if (mCH_AircraftInfo.soundMove.isEmpty()) continue;
            MCH_SoundEvents.registerSoundEventName(mCH_AircraftInfo.soundMove);
        }
    }

    public void loadConfig(String fileName) {
        this.lastConfigFileName = fileName;
        this.config = new MCH_Config("./", fileName);
        this.config.load();
        this.config.write();
    }

    public void reconfig() {
        MCH_Lib.DbgLog(false, "MCH_CommonProxy.reconfig()", new Object[0]);
        this.loadConfig(this.lastConfigFileName);
    }

    public void save() {
        MCH_Lib.DbgLog(false, "MCH_CommonProxy.save()", new Object[0]);
        this.config.write();
    }

    public void reloadHUD() {
    }

    public Entity getClientPlayer() {
        return null;
    }

    public void setCreativeDigDelay(int n) {
    }

    public void init() {
    }

    public boolean isFirstPerson() {
        return false;
    }

    public boolean isSinglePlayer() {
        return MCH_Utils.getServer().func_71264_H();
    }

    public void readClientModList() {
    }

    public void printChatMessage(ITextComponent chat, int showTime, int pos) {
    }

    public void hitBullet() {
    }

    public void clientLocked() {
    }

    public void setRenderEntityDistanceWeight(double renderDistWeight) {
    }

    public List<AddonPack> loadAddonPacks(File addonDir) {
        return AddonManager.loadAddons(addonDir);
    }

    public void onLoadStartAddons(int addonSize) {
    }

    public void onLoadStepAddon(String addonDomain) {
        MCH_Utils.logger().debug("addon(" + addonDomain + ") loading start.");
    }

    public void onLoadFinishAddons() {
    }

    public void onLoadStartContents(String typeName, int fileSize) {
        MCH_Utils.logger().debug("content type(" + typeName + ") loading start. steps:" + fileSize);
    }

    public void onLoadFinishContents(String typeName) {
    }

    public void onParseStartFile(AddonResourceLocation location) {
        MCH_Utils.logger().debug("content file(" + (Object)((Object)location) + ") loading start.");
    }

    public void onParseFinishFile(AddonResourceLocation location) {
    }

    public boolean canLoadContentDirName(String dir) {
        return MCH_Utils.inArray(CONTENT_DIRS, dir);
    }

    public void updateGeneratedLanguage() {
        W_LanguageRegistry.clear();
    }

    public void registerRecipeDescriptions() {
    }
}

