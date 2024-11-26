/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.__helper.client.model.loader;

import java.io.IOException;
import java.net.URL;
import javax.annotation.Nullable;
import mcheli.__helper.client._IModelCustom;
import mcheli.__helper.client._IModelCustomLoader;
import mcheli.__helper.client._ModelFormatException;
import mcheli.__helper.client.model.loader.IVertexModelLoader;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class TechneModelLoader
implements _IModelCustomLoader,
IVertexModelLoader {
    private static final String[] types = new String[]{"tcn"};

    @Override
    public String getType() {
        return "Techne model";
    }

    @Override
    public String[] getSuffixes() {
        return types;
    }

    @Override
    @Deprecated
    public _IModelCustom loadInstance(ResourceLocation resource) throws _ModelFormatException {
        throw new UnsupportedOperationException("Techne model is unsupported. file:" + resource);
    }

    @Override
    @Deprecated
    public _IModelCustom loadInstance(String resourceName, URL resource) throws _ModelFormatException {
        throw new UnsupportedOperationException("Techne model is unsupported. file:" + resource);
    }

    @Override
    public String getExtension() {
        return "tcn";
    }

    @Override
    @Nullable
    public _IModelCustom load(IResourceManager resourceManager, ResourceLocation location) throws IOException, _ModelFormatException {
        return null;
    }
}

