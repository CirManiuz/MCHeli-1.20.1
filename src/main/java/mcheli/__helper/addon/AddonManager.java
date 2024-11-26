/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.client.FMLClientHandler
 *  net.minecraftforge.fml.common.FMLModContainer
 *  net.minecraftforge.fml.common.MetadataCollection
 *  net.minecraftforge.fml.common.ModContainer
 *  net.minecraftforge.fml.common.discovery.ContainerType
 *  net.minecraftforge.fml.common.discovery.ModCandidate
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.__helper.addon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import mcheli.MCH_MOD;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.addon.AddonPack;
import mcheli.__helper.addon.BuiltinAddonPack;
import mcheli.__helper.addon.GeneratedAddonPack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.discovery.ContainerType;
import net.minecraftforge.fml.common.discovery.ModCandidate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AddonManager {
    public static final Pattern ZIP_PATTERN = Pattern.compile("(.+).(zip|jar)$");
    private static final Map<String, AddonPack> ADDON_LIST = Maps.newLinkedHashMap();
    public static final String BUILTIN_ADDON_DOMAIN = "@builtin";

    @Nullable
    public static AddonPack get(String addonDomain) {
        if (BUILTIN_ADDON_DOMAIN.equals(addonDomain)) {
            return BuiltinAddonPack.instance();
        }
        return ADDON_LIST.get(addonDomain);
    }

    public static List<AddonPack> getLoadedAddons() {
        return ImmutableList.builder().addAll(ADDON_LIST.values()).build();
    }

    public static List<AddonPack> loadAddons(File addonDir) {
        File[] addonFiles;
        AddonManager.checkExistAddonDir(addonDir);
        ArrayList addons = Lists.newArrayList();
        for (File addonFile : addonFiles = addonDir.listFiles()) {
            AddonPack data;
            if (!AddonManager.validAddonPath(addonFile) || (data = AddonManager.loadAddon(addonFile)) == null) continue;
            addons.add(data);
        }
        MCH_Utils.logger().info("Load complete addons. count:" + addons.size());
        return addons;
    }

    @SideOnly(value=Side.CLIENT)
    public static List<AddonPack> loadAddonsAndAddResources(File addonDir) {
        File[] addonFiles;
        AddonManager.checkExistAddonDir(addonDir);
        ArrayList addons = Lists.newArrayList();
        for (File addonFile : addonFiles = addonDir.listFiles()) {
            AddonPack data;
            if (!AddonManager.validAddonPath(addonFile) || (data = AddonManager.loadAddonAndAddResource(addonFile, MCH_MOD.class)) == null) continue;
            addons.add(data);
        }
        AddonManager.checkExistAddonDir(GeneratedAddonPack.instance().getFile());
        AddonManager.addReloadableResource(GeneratedAddonPack.instance(), MCH_MOD.class);
        FMLClientHandler.instance().refreshResources((Predicate)null);
        MCH_Utils.logger().info("Load complete addons and add resources. count:" + addons.size());
        return addons;
    }

    @Nullable
    private static AddonPack loadAddon(File addonFile) {
        try {
            AddonPack addonPack = AddonPack.create(addonFile);
            ADDON_LIST.put(addonPack.getDomain(), addonPack);
            return addonPack;
        }
        catch (Exception e) {
            MCH_Utils.logger().error("Failed to load for pack:{} :", (Object)addonFile.getName(), (Object)e);
            return null;
        }
    }

    @Nullable
    @SideOnly(value=Side.CLIENT)
    private static AddonPack loadAddonAndAddResource(File addonFile, Class<?> clazz) {
        AddonPack addonPack = AddonManager.loadAddon(addonFile);
        if (addonPack == null) {
            return null;
        }
        AddonManager.addReloadableResource(addonPack, clazz);
        return addonPack;
    }

    @SideOnly(value=Side.CLIENT)
    private static void addReloadableResource(AddonPack addonPack, Class<?> clazz) {
        HashMap descriptor = Maps.newHashMap();
        descriptor.put("modid", "mcheli");
        descriptor.put("name", "MCHeli#" + addonPack.getName());
        descriptor.put("version", addonPack.getVersion());
        File file = addonPack.getFile();
        FMLModContainer container = new FMLModContainer(clazz.getName(), new ModCandidate(file, file, file.isDirectory() ? ContainerType.DIR : ContainerType.JAR), (Map)descriptor);
        container.bindMetadata(MetadataCollection.from(null, (String)""));
        FMLClientHandler.instance().addModAsResource((ModContainer)container);
    }

    private static boolean validAddonPath(File addonFile) {
        return !GeneratedAddonPack.isGeneratedAddonName(addonFile) && (addonFile.isDirectory() || ZIP_PATTERN.matcher(addonFile.getName()).matches());
    }

    private static void checkExistAddonDir(File addonDir) {
        if (!addonDir.exists()) {
            addonDir.mkdirs();
        }
    }
}

