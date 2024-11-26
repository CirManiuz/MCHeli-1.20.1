package mcheli.wrapper;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour; // Для свойств блока
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


public abstract class W_BlockContainer extends Block implements EntityBlock {

    protected W_BlockContainer(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /**
     * Абстрактный метод для создания нового BlockEntity.
     */
    @Override
    public abstract @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state);

    /**
     * Метод для определения логики тика BlockEntity (если требуется).
     */
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(BlockEntityType<T> type, BlockEntityType<?> expectedType) {
        return null; // Здесь можно реализовать логику, если блок имеет тикер.
    }

    /**
     * Метод для установки уровня освещённости.
     */
    public W_BlockContainer setLightLevel(int lightLevel) {
        this.defaultBlockState().setValue(BlockStateProperties.LIGHT_LEVEL, lightLevel);
        return this;
    }
}
