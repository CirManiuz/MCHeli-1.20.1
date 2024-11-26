package mcheli.wrapper;

import net.minecraft.core.registries.BuiltInRegistries; // Новый способ работы с реестрами
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public abstract class W_Block extends Block {
    protected W_Block(Properties properties) {
        super(properties);
    }

    // Получить блок по имени
    public static Block getBlockFromName(String name) {
        return BuiltInRegistries.BLOCK.get(new ResourceLocation(name));
    }

    // Получить блок снега
    public static Block getSnowLayer() {
        return Blocks.SNOW;
    }

    // Проверка на null или блок воздуха
    public static boolean isNull(Block block) {
        return block == null || block == Blocks.AIR;
    }

    // Сравнение ID блока с блоком
    public static boolean isEqual(int blockId, Block block) {
        return BuiltInRegistries.BLOCK.byId(blockId) == block;
    }

    // Сравнение двух блоков
    public static boolean isEqual(Block block1, Block block2) {
        return block1 == block2;
    }

    // Получить блок воды
    public static Block getWater() {
        return Blocks.WATER;
    }

    // Получить блок по ID
    public static Block getBlockById(int i) {
        return BuiltInRegistries.BLOCK.byId(i);
    }
}
