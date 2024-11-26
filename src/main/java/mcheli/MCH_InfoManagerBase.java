/*
 * Decompiled with CFR 0.152.
 */
package mcheli;

import java.io.File;
import java.io.FileFilter;
import mcheli.MCH_BaseInfo;
import mcheli.MCH_InputFile;
import mcheli.MCH_Lib;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.addon.AddonResourceLocation;

public abstract class MCH_InfoManagerBase<T extends MCH_BaseInfo> {
    public abstract T newInfo(AddonResourceLocation var1, String var2);

    protected void put(String name, T info) {
    }

    protected abstract boolean contains(String var1);

    protected abstract int size();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean load(String path, String type) {
        File dir = new File((path = path.replace('\\', '/')) + type);
        File[] files = dir.listFiles(new FileFilter(){

            @Override
            public boolean accept(File pathname) {
                String s = pathname.getName().toLowerCase();
                return pathname.isFile() && s.length() >= 5 && s.substring(s.length() - 4).compareTo(".txt") == 0;
            }
        });
        if (files == null || files.length <= 0) {
            return false;
        }
        for (File f : files) {
            int line = 0;
            try (MCH_InputFile inFile = new MCH_InputFile();){
                String str;
                String name = f.getName().toLowerCase();
                name = name.substring(0, name.length() - 4);
                if (this.contains(name)) {
                    inFile.close();
                    continue;
                }
                if (!inFile.openUTF8(f)) continue;
                T info = this.newInfo(MCH_Utils.buildinAddon(name), f.getCanonicalPath());
                while ((str = inFile.br.readLine()) != null) {
                    ++line;
                    int eqIdx = (str = str.trim()).indexOf(61);
                    if (eqIdx < 0 || str.length() <= eqIdx + 1) continue;
                    ((MCH_BaseInfo)info).loadItemData(str.substring(0, eqIdx).trim().toLowerCase(), str.substring(eqIdx + 1).trim());
                }
                line = 0;
                if (!((MCH_BaseInfo)info).validate()) continue;
                this.put(name, info);
            }
        }
        MCH_Lib.Log("Read %d %s", this.size(), type);
        return this.size() > 0;
    }
}

