/*
 * Decompiled with CFR 0.152.
 */
package mcheli.__helper.info;

import java.util.List;
import mcheli.__helper.addon.AddonResourceLocation;

public interface IContentData {
    public void parse(List<String> var1, String var2, boolean var3) throws Exception;

    public boolean validate() throws Exception;

    public void onPostReload();

    public AddonResourceLocation getLoation();

    public String getContentPath();
}

