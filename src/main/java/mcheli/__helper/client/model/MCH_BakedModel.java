/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.vecmath.Matrix4f
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.renderer.block.model.BakedQuad
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.client.renderer.block.model.ItemOverrideList
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.util.EnumFacing
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.commons.lang3.tuple.Pair
 */
package mcheli.__helper.client.model;

import java.util.List;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import mcheli.__helper.client.model.MCH_ItemOverrideList;
import mcheli.__helper.client.model.PooledModelParameters;
import mcheli.__helper.client.renderer.item.IItemModelRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

@SideOnly(value=Side.CLIENT)
public class MCH_BakedModel
implements IBakedModel {
    private final IBakedModel bakedModel;
    private final IItemModelRenderer renderer;
    private final ItemOverrideList overrides;

    public MCH_BakedModel(IBakedModel bakedModel, IItemModelRenderer renderer) {
        this.bakedModel = bakedModel;
        this.renderer = renderer;
        this.overrides = new MCH_ItemOverrideList(bakedModel);
    }

    public List<BakedQuad> func_188616_a(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return this.bakedModel.func_188616_a(state, side, rand);
    }

    public boolean func_177555_b() {
        return this.bakedModel.func_177555_b();
    }

    public boolean func_177556_c() {
        return this.bakedModel.func_177556_c();
    }

    public boolean func_188618_c() {
        if (this.renderer.shouldRenderer(PooledModelParameters.getTargetRendererStack(), PooledModelParameters.getTransformType())) {
            return true;
        }
        return this.bakedModel.func_188618_c();
    }

    public TextureAtlasSprite func_177554_e() {
        return this.bakedModel.func_177554_e();
    }

    @Deprecated
    public ItemCameraTransforms func_177552_f() {
        return this.bakedModel.func_177552_f();
    }

    public ItemOverrideList func_188617_f() {
        return this.overrides;
    }

    public boolean isAmbientOcclusion(IBlockState state) {
        return this.bakedModel.isAmbientOcclusion(state);
    }

    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        PooledModelParameters.setTransformType(cameraTransformType);
        Pair pair = this.bakedModel.handlePerspective(cameraTransformType);
        return Pair.of((Object)new MCH_BakedModel((IBakedModel)pair.getLeft(), this.renderer), (Object)pair.getRight());
    }
}

