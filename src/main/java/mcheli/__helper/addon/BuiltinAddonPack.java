/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.gson.JsonElement
 */
package mcheli.__helper.addon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import java.io.File;
import java.util.List;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.addon.AddonPack;

public class BuiltinAddonPack
extends AddonPack {
    private static BuiltinAddonPack instance = null;

    public static BuiltinAddonPack instance() {
        if (instance == null) {
            instance = new BuiltinAddonPack();
        }
        return instance;
    }

    private BuiltinAddonPack() {
        super("@builtin", "MCHeli-Builtin", "1.0", null, "EMB4-MCHeli", (List<String>)ImmutableList.of((Object)"EMB4", (Object)"Murachiki27"), "Builtin addon", "1", (ImmutableMap<String, JsonElement>)ImmutableMap.of());
    }

    @Override
    public File getFile() {
        return MCH_Utils.getSource();
    }
}

