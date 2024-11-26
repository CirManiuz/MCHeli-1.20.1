/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 */
package mcheli.__helper.client;

import java.net.URL;
import mcheli.__helper.client._IModelCustom;
import mcheli.__helper.client._ModelFormatException;
import net.minecraft.util.ResourceLocation;

@Deprecated
public interface _IModelCustomLoader {
    public String getType();

    public String[] getSuffixes();

    @Deprecated
    public _IModelCustom loadInstance(ResourceLocation var1) throws _ModelFormatException;

    @Deprecated
    public _IModelCustom loadInstance(String var1, URL var2) throws _ModelFormatException;
}

