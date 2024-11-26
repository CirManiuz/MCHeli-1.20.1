/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.client.renderer.block.model.ModelResourceLocation
 *  net.minecraft.client.renderer.block.statemap.IStateMapper
 *  net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer
 *  net.minecraft.item.Item
 *  net.minecraftforge.client.event.ModelBakeEvent
 *  net.minecraftforge.client.event.ModelRegistryEvent
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.relauncher.Side
 */
package mcheli.__helper.client;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import mcheli.MCH_MOD;
import mcheli.__helper.client.model.MCH_BakedModel;
import mcheli.__helper.client.renderer.item.CustomItemStackRenderer;
import mcheli.__helper.client.renderer.item.IItemModelRenderer;
import mcheli.__helper.info.ContentRegistries;
import mcheli.__helper.info.IItemContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid="mcheli", value={Side.CLIENT})
public class MCH_ItemModelRenderers {
    private static final Map<ModelResourceLocation, IItemModelRenderer> renderers = Maps.newHashMap();

    @SubscribeEvent
    static void onModelRegistryEvent(ModelRegistryEvent event) {
        MCH_ItemModelRenderers.registerModelLocation(Item.func_150898_a((Block)MCH_MOD.blockDraftingTable));
        ModelLoader.setCustomStateMapper((Block)MCH_MOD.blockDraftingTable, (IStateMapper)new IStateMapper(){

            public Map<IBlockState, ModelResourceLocation> func_178130_a(Block blockIn) {
                return Maps.newHashMap();
            }
        });
        ModelLoader.setCustomStateMapper((Block)MCH_MOD.blockDraftingTableLit, (IStateMapper)new IStateMapper(){

            public Map<IBlockState, ModelResourceLocation> func_178130_a(Block blockIn) {
                return Maps.newHashMap();
            }
        });
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemSpawnGunnerVsMonster);
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemSpawnGunnerVsPlayer);
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemRangeFinder);
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemWrench);
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemFuel);
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemGLTD);
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemChain);
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemParachute);
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemContainer);
        for (int i = 0; i < MCH_MOD.itemUavStation.length; ++i) {
            MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemUavStation[i]);
        }
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.invisibleItem);
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemStingerBullet);
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemJavelinBullet);
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemStinger);
        MCH_ItemModelRenderers.registerModelLocation(MCH_MOD.itemJavelin);
        ContentRegistries.heli().forEachValue(MCH_ItemModelRenderers::registerLegacyModelLocation);
        ContentRegistries.plane().forEachValue(MCH_ItemModelRenderers::registerLegacyModelLocation);
        ContentRegistries.tank().forEachValue(MCH_ItemModelRenderers::registerLegacyModelLocation);
        ContentRegistries.vehicle().forEachValue(MCH_ItemModelRenderers::registerLegacyModelLocation);
        ContentRegistries.throwable().forEachValue(MCH_ItemModelRenderers::registerLegacyModelLocation);
    }

    @SubscribeEvent
    static void onBakedModelEvent(ModelBakeEvent event) {
        for (Map.Entry<ModelResourceLocation, IItemModelRenderer> entry : renderers.entrySet()) {
            IBakedModel bakedmodel = (IBakedModel)event.getModelRegistry().func_82594_a((Object)entry.getKey());
            if (bakedmodel == null) continue;
            event.getModelRegistry().func_82595_a((Object)entry.getKey(), (Object)new MCH_BakedModel(bakedmodel, entry.getValue()));
        }
    }

    public static void registerRenderer(Item item, IItemModelRenderer renderer) {
        item.setTileEntityItemStackRenderer((TileEntityItemStackRenderer)CustomItemStackRenderer.getInstance());
        renderers.put(MCH_ItemModelRenderers.getInventoryModel(item), renderer);
    }

    public static void registerModelLocation(Item item) {
        MCH_ItemModelRenderers.registerModelLocation(item, 0);
    }

    public static void registerModelLocation(Item item, int meta) {
        ModelLoader.setCustomModelResourceLocation((Item)item, (int)meta, (ModelResourceLocation)MCH_ItemModelRenderers.getInventoryModel(item));
    }

    public static void registerLegacyModelLocation(IItemContent content) {
        ModelLoader.setCustomModelResourceLocation((Item)content.getItem(), (int)0, (ModelResourceLocation)new ModelResourceLocation(content.getItem().getRegistryName(), "mcheli_legacy"));
    }

    private static ModelResourceLocation getInventoryModel(Item item) {
        return new ModelResourceLocation(item.getRegistryName(), "inventory");
    }

    @Nullable
    public static IItemModelRenderer getRenderer(Item item) {
        return renderers.get(MCH_ItemModelRenderers.getInventoryModel(item));
    }
}

