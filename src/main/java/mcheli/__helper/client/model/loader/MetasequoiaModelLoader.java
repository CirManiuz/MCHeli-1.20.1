/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.__helper.client.model.loader;

import java.io.IOException;
import mcheli.__helper.client._IModelCustom;
import mcheli.__helper.client._ModelFormatException;
import mcheli.__helper.client.model.loader.IVertexModelLoader;
import mcheli.wrapper.modelloader.W_MetasequoiaObject;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class MetasequoiaModelLoader
implements IVertexModelLoader {
    @Override
    public _IModelCustom load(IResourceManager resourceManager, ResourceLocation location) throws IOException, _ModelFormatException {
        ResourceLocation modelLocation = this.withExtension(location);
        IResource resource = resourceManager.func_110536_a(modelLocation);
        return new W_MetasequoiaObject(modelLocation, resource);
    }

    @Override
    public String getExtension() {
        return "mqo";
    }
}

