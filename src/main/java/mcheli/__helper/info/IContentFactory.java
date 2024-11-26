/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package mcheli.__helper.info;

import javax.annotation.Nullable;
import mcheli.__helper.addon.AddonResourceLocation;
import mcheli.__helper.info.ContentType;
import mcheli.__helper.info.IContentData;

public interface IContentFactory {
    @Nullable
    public IContentData create(AddonResourceLocation var1, String var2);

    public ContentType getType();
}

