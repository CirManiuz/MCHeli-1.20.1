/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  javax.annotation.Nullable
 */
package mcheli.__helper.info;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.Nullable;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.addon.AddonResourceLocation;
import mcheli.__helper.info.ContentType;
import mcheli.__helper.info.IContentData;
import mcheli.__helper.info.IContentFactory;
import mcheli.helicopter.MCH_HeliInfo;
import mcheli.hud.MCH_Hud;
import mcheli.plane.MCP_PlaneInfo;
import mcheli.tank.MCH_TankInfo;
import mcheli.throwable.MCH_ThrowableInfo;
import mcheli.vehicle.MCH_VehicleInfo;
import mcheli.weapon.MCH_WeaponInfo;

public class ContentFactories {
    private static final Map<String, IContentFactory> TABLE = Maps.newHashMap();

    @Nullable
    public static IContentFactory getFactory(@Nullable String dirName) {
        return dirName == null ? null : TABLE.get(dirName);
    }

    private static IContentFactory createFactory(final ContentType type, final BiFunction<AddonResourceLocation, String, IContentData> function) {
        return new IContentFactory(){

            @Override
            public IContentData create(AddonResourceLocation location, String filepath) {
                return (IContentData)function.apply(location, filepath);
            }

            @Override
            public ContentType getType() {
                return type;
            }
        };
    }

    static {
        TABLE.put("helicopters", ContentFactories.createFactory(ContentType.HELICOPTER, MCH_HeliInfo::new));
        TABLE.put("planes", ContentFactories.createFactory(ContentType.PLANE, MCP_PlaneInfo::new));
        TABLE.put("tanks", ContentFactories.createFactory(ContentType.TANK, MCH_TankInfo::new));
        TABLE.put("vehicles", ContentFactories.createFactory(ContentType.VEHICLE, MCH_VehicleInfo::new));
        TABLE.put("throwable", ContentFactories.createFactory(ContentType.THROWABLE, MCH_ThrowableInfo::new));
        TABLE.put("weapons", ContentFactories.createFactory(ContentType.WEAPON, MCH_WeaponInfo::new));
        if (MCH_Utils.isClient()) {
            TABLE.put("hud", ContentFactories.createFactory(ContentType.HUD, MCH_Hud::new));
        }
    }
}

