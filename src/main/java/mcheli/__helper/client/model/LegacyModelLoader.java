/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.block.model.ModelBlock
 *  net.minecraft.client.renderer.block.model.ModelResourceLocation
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.client.model.ICustomModelLoader
 *  net.minecraftforge.client.model.IModel
 *  net.minecraftforge.client.model.ModelLoaderRegistry
 */
package mcheli.__helper.client.model;

import mcheli.__helper.client.model.MCH_WrapperItemLayerModel;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public enum LegacyModelLoader implements ICustomModelLoader
{
    INSTANCE;

    public static final String VARIANT = "mcheli_legacy";
    static final String TEMPLATE;

    public void func_110549_a(IResourceManager resourceManager) {
    }

    public boolean accepts(ResourceLocation modelLocation) {
        if (modelLocation instanceof ModelResourceLocation) {
            ModelResourceLocation location = (ModelResourceLocation)modelLocation;
            return location.func_110624_b().equals("mcheli") && location.func_177518_c().equals(VARIANT);
        }
        return false;
    }

    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        ModelBlock parent;
        String path = modelLocation.func_110624_b() + ":items/" + modelLocation.func_110623_a();
        ModelBlock modelblock = ModelBlock.func_178294_a((String)TEMPLATE.replaceAll("__item__", path));
        modelblock.field_178315_d = parent = (ModelBlock)ModelLoaderRegistry.getModel((ResourceLocation)modelblock.func_178305_e()).asVanillaModel().get();
        return new MCH_WrapperItemLayerModel(modelblock);
    }

    static {
        TEMPLATE = "{'parent':'item/generated','textures':{'layer0':'__item__'}}".replaceAll("'", "\"");
    }
}

