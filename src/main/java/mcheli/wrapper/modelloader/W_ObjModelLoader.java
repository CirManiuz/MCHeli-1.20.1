/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 */
package mcheli.wrapper.modelloader;

import java.net.URL;
import mcheli.__helper.client._IModelCustom;
import mcheli.__helper.client._IModelCustomLoader;
import mcheli.__helper.client._ModelFormatException;
import mcheli.wrapper.modelloader.W_WavefrontObject;
import net.minecraft.util.ResourceLocation;

public class W_ObjModelLoader
implements _IModelCustomLoader {
    private static final String[] types = new String[]{"obj"};

    @Override
    public String getType() {
        return "OBJ model";
    }

    @Override
    public String[] getSuffixes() {
        return types;
    }

    @Override
    public _IModelCustom loadInstance(ResourceLocation resource) throws _ModelFormatException {
        return new W_WavefrontObject(resource);
    }

    @Override
    public _IModelCustom loadInstance(String resourceName, URL resource) throws _ModelFormatException {
        return new W_WavefrontObject(resourceName, resource);
    }
}

