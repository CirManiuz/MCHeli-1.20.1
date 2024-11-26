/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 */
package mcheli.wrapper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class W_Particle {
    public static BlockParticleParam getParticleTileCrackName(World w, int blockX, int blockY, int blockZ) {
        IBlockState iblockstate = w.func_180495_p(new BlockPos(blockX, blockY, blockZ));
        if (iblockstate.func_185904_a() != Material.field_151579_a) {
            return new BlockParticleParam("blockcrack", Block.func_176210_f((IBlockState)iblockstate));
        }
        return BlockParticleParam.EMPTY;
    }

    public static BlockParticleParam getParticleTileDustName(World w, int blockX, int blockY, int blockZ) {
        IBlockState iblockstate = w.func_180495_p(new BlockPos(blockX, blockY, blockZ));
        if (iblockstate.func_185904_a() != Material.field_151579_a) {
            return new BlockParticleParam("blockdust", Block.func_176210_f((IBlockState)iblockstate));
        }
        return BlockParticleParam.EMPTY;
    }

    public static class BlockParticleParam {
        public static final BlockParticleParam EMPTY = new BlockParticleParam();
        public final String name;
        public final int stateId;
        private final boolean empty;

        public BlockParticleParam(String name, int stateId) {
            this.name = name;
            this.stateId = stateId;
            this.empty = false;
        }

        private BlockParticleParam() {
            this.name = "";
            this.stateId = 0;
            this.empty = true;
        }

        public boolean isEmpty() {
            return this.empty;
        }
    }
}

