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
import javax.annotation.Nullable;
import mcheli.__helper.client._IModelCustom;
import mcheli.__helper.client._ModelFormatException;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public interface IVertexModelLoader {
    public String getExtension();

    @Nullable
    public _IModelCustom load(IResourceManager var1, ResourceLocation var2) throws IOException, _ModelFormatException;

    default public ResourceLocation withExtension(ResourceLocation location) {
        return new ResourceLocation(location.func_110624_b(), location.func_110623_a() + "." + this.getExtension());
    }
}

