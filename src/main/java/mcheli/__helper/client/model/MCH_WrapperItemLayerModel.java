/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Maps
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.client.renderer.block.model.ModelBlock
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.client.model.IModel
 *  net.minecraftforge.client.model.ItemLayerModel
 *  net.minecraftforge.client.model.PerspectiveMapWrapper
 *  net.minecraftforge.client.model.SimpleModelState
 *  net.minecraftforge.common.model.IModelState
 */
package mcheli.__helper.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;

public class MCH_WrapperItemLayerModel
implements IModel {
    private ItemLayerModel model;
    private ModelBlock raw;

    public MCH_WrapperItemLayerModel(ModelBlock modelBlock) {
        this.raw = modelBlock;
        this.model = new ItemLayerModel(modelBlock);
    }

    public Collection<ResourceLocation> getTextures() {
        return this.model.getTextures();
    }

    public IModel retexture(ImmutableMap<String, String> textures) {
        return this.model.retexture(textures);
    }

    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ItemCameraTransforms transforms = this.raw.func_181682_g();
        EnumMap tMap = Maps.newEnumMap(ItemCameraTransforms.TransformType.class);
        tMap.putAll(PerspectiveMapWrapper.getTransforms((ItemCameraTransforms)transforms));
        tMap.putAll(PerspectiveMapWrapper.getTransforms((IModelState)state));
        SimpleModelState perState = new SimpleModelState(ImmutableMap.copyOf((Map)tMap), state.apply(Optional.empty()));
        return this.model.bake((IModelState)perState, format, bakedTextureGetter);
    }
}

