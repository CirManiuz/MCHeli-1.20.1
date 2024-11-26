/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.collect.ListMultimap
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Multimaps
 */
package mcheli;

import com.google.common.base.Joiner;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import mcheli.MCH_MOD;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.addon.GeneratedAddonPack;

public class MCH_SoundsJson {
    public static void updateGenerated() {
        File soundsDir = new File(MCH_MOD.getSource().getPath() + "/assets/mcheli/sounds");
        File[] soundFiles = soundsDir.listFiles(f -> {
            String s = f.getName().toLowerCase();
            return f.isFile() && s.length() >= 5 && s.substring(s.length() - 4).compareTo(".ogg") == 0;
        });
        ListMultimap multimap = Multimaps.newListMultimap((Map)Maps.newLinkedHashMap(), Lists::newLinkedList);
        LinkedList lines = Lists.newLinkedList();
        int cnt = 0;
        if (soundFiles != null) {
            for (File f2 : soundFiles) {
                String name2 = f2.getName().toLowerCase();
                int ei = name2.lastIndexOf(".");
                String key = name2 = name2.substring(0, ei);
                char c = key.charAt(key.length() - 1);
                if (c >= '0' && c <= '9') {
                    key = key.substring(0, key.length() - 1);
                }
                multimap.put((Object)key, (Object)name2);
            }
            lines.add("{");
            for (String key : multimap.keySet()) {
                String sounds = Joiner.on((String)",").join((Iterable)multimap.get((Object)key).stream().map(name -> '\"' + MCH_Utils.suffix(name).toString() + '\"').collect(Collectors.toList()));
                String line = "\"" + key + "\": {\"category\": \"master\",\"sounds\": [" + sounds + "]}";
                if (++cnt < multimap.keySet().size()) {
                    line = line + ",";
                }
                lines.add(line);
            }
            lines.add("}");
            lines.add("");
        }
        GeneratedAddonPack.instance().updateAssetFile("sounds.json", lines);
        MCH_Utils.logger().info("Update sounds.json, %d sounds.", (Object)cnt);
    }
}

