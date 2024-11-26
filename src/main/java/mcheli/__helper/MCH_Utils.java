/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  org.apache.logging.log4j.Logger
 */
package mcheli.__helper;

import java.io.File;
import javax.annotation.Nullable;
import mcheli.MCH_MOD;
import mcheli.__helper.MCH_Logger;
import mcheli.__helper.addon.AddonResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.Logger;

public class MCH_Utils {
    public static ResourceLocation suffix(String name) {
        return new ResourceLocation("mcheli", name);
    }

    public static AddonResourceLocation addon(String domain, String path) {
        return new AddonResourceLocation(MCH_Utils.suffix(path), domain);
    }

    public static AddonResourceLocation buildinAddon(String path) {
        return new AddonResourceLocation(MCH_Utils.suffix(path), "@builtin");
    }

    public static File getSource() {
        return MCH_MOD.getSource();
    }

    public static boolean isClient() {
        return MCH_MOD.proxy.isRemote();
    }

    public static MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    public static Logger logger() {
        return MCH_Logger.get();
    }

    public static <T> boolean inArray(T[] objArray, @Nullable T target) {
        for (T obj : objArray) {
            if (!(target == null ? obj == null : target.equals(obj))) continue;
            return true;
        }
        return false;
    }
}

