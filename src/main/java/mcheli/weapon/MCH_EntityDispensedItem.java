/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.world.World
 *  net.minecraftforge.event.ForgeEventFactory
 */
package mcheli.weapon;

import mcheli.MCH_Config;
import mcheli.weapon.MCH_BulletModel;
import mcheli.weapon.MCH_DefaultBulletModels;
import mcheli.weapon.MCH_DummyEntityPlayer;
import mcheli.weapon.MCH_EntityBaseBullet;
import mcheli.wrapper.W_Blocks;
import mcheli.wrapper.W_Item;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class MCH_EntityDispensedItem
extends MCH_EntityBaseBullet {
    public MCH_EntityDispensedItem(World par1World) {
        super(par1World);
    }

    public MCH_EntityDispensedItem(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
        super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.getInfo() != null && !this.getInfo().disableSmoke) {
            this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 7.0f * this.getInfo().smokeSize);
        }
        if (!this.field_70170_p.field_72995_K && this.getInfo() != null) {
            if (this.acceleration < 1.0E-4) {
                this.field_70159_w *= 0.999;
                this.field_70179_y *= 0.999;
            }
            if (this.func_70090_H()) {
                this.field_70159_w *= (double)this.getInfo().velocityInWater;
                this.field_70181_x *= (double)this.getInfo().velocityInWater;
                this.field_70179_y *= (double)this.getInfo().velocityInWater;
            }
        }
        this.onUpdateBomblet();
    }

    @Override
    protected void onImpact(RayTraceResult m, float damageFactor) {
        if (!this.field_70170_p.field_72995_K) {
            this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, 2000.0, 0.0));
            EntityPlayer player = null;
            Item item = null;
            int itemDamage = 0;
            if (m != null && this.getInfo() != null) {
                if (this.shootingAircraft instanceof EntityPlayer) {
                    player = (EntityPlayer)this.shootingAircraft;
                }
                if (this.shootingEntity instanceof EntityPlayer) {
                    player = (EntityPlayer)this.shootingEntity;
                }
                item = this.getInfo().dispenseItem;
                itemDamage = this.getInfo().dispenseDamege;
            }
            if (player != null && !player.field_70128_L && item != null) {
                MCH_DummyEntityPlayer dummyPlayer = new MCH_DummyEntityPlayer(this.field_70170_p, player);
                dummyPlayer.field_70125_A = 90.0f;
                int RNG = this.getInfo().dispenseRange - 1;
                for (int x = -RNG; x <= RNG; ++x) {
                    for (int y = -RNG; y <= RNG; ++y) {
                        if (y < 0 || y >= 256) continue;
                        for (int z = -RNG; z <= RNG; ++z) {
                            int dist = x * x + y * y + z * z;
                            if (dist > RNG * RNG) continue;
                            if ((double)dist <= 0.5 * (double)RNG * (double)RNG) {
                                this.useItemToBlock(m.func_178782_a().func_177982_a(x, y, z), item, itemDamage, dummyPlayer);
                                continue;
                            }
                            if (this.field_70146_Z.nextInt(2) != 0) continue;
                            this.useItemToBlock(m.func_178782_a().func_177982_a(x, y, z), item, itemDamage, dummyPlayer);
                        }
                    }
                }
            }
            this.func_70106_y();
        }
    }

    private void useItemToBlock(BlockPos blockpos, Item item, int itemDamage, EntityPlayer dummyPlayer) {
        dummyPlayer.field_70165_t = (double)blockpos.func_177958_n() + 0.5;
        dummyPlayer.field_70163_u = (double)blockpos.func_177956_o() + 2.5;
        dummyPlayer.field_70161_v = (double)blockpos.func_177952_p() + 0.5;
        dummyPlayer.field_70177_z = this.field_70146_Z.nextInt(360);
        IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos);
        Block block = iblockstate.func_177230_c();
        Material blockMat = iblockstate.func_185904_a();
        if (block != W_Blocks.field_150350_a && blockMat != Material.field_151579_a) {
            if (item == W_Item.getItemByName("water_bucket")) {
                if (MCH_Config.Collision_DestroyBlock.prmBool) {
                    if (blockMat == Material.field_151581_o) {
                        this.field_70170_p.func_175698_g(blockpos);
                    } else if (blockMat == Material.field_151587_i) {
                        int metadata = block.func_176201_c(iblockstate);
                        if (metadata == 0) {
                            this.field_70170_p.func_175656_a(blockpos, ForgeEventFactory.fireFluidPlaceBlockEvent((World)this.field_70170_p, (BlockPos)blockpos, (BlockPos)blockpos, (IBlockState)W_Blocks.field_150343_Z.func_176223_P()));
                        } else if (metadata <= 4) {
                            this.field_70170_p.func_175656_a(blockpos, ForgeEventFactory.fireFluidPlaceBlockEvent((World)this.field_70170_p, (BlockPos)blockpos, (BlockPos)blockpos, (IBlockState)W_Blocks.field_150347_e.func_176223_P()));
                        }
                    }
                }
            } else if (item.onItemUseFirst(dummyPlayer, this.field_70170_p, blockpos, EnumFacing.UP, (float)blockpos.func_177958_n(), (float)blockpos.func_177956_o(), (float)blockpos.func_177952_p(), EnumHand.MAIN_HAND) == EnumActionResult.PASS && item.func_180614_a(dummyPlayer, this.field_70170_p, blockpos, EnumHand.MAIN_HAND, EnumFacing.UP, (float)blockpos.func_177958_n(), (float)blockpos.func_177956_o(), (float)blockpos.func_177952_p()) == EnumActionResult.PASS) {
                item.func_77659_a(this.field_70170_p, dummyPlayer, EnumHand.MAIN_HAND);
            }
        }
    }

    @Override
    public void sprinkleBomblet() {
        if (!this.field_70170_p.field_72995_K) {
            MCH_EntityDispensedItem e = new MCH_EntityDispensedItem(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70159_w, this.field_70181_x, this.field_70179_y, this.field_70146_Z.nextInt(360), 0.0f, this.acceleration);
            e.setParameterFromWeapon(this, this.shootingAircraft, this.shootingEntity);
            e.setName(this.func_70005_c_());
            float RANDOM = this.getInfo().bombletDiff;
            e.field_70159_w = this.field_70159_w * 1.0 + (double)((this.field_70146_Z.nextFloat() - 0.5f) * RANDOM);
            e.field_70181_x = this.field_70181_x * 1.0 / 2.0 + (double)((this.field_70146_Z.nextFloat() - 0.5f) * RANDOM / 2.0f);
            e.field_70179_y = this.field_70179_y * 1.0 + (double)((this.field_70146_Z.nextFloat() - 0.5f) * RANDOM);
            e.setBomblet();
            this.field_70170_p.func_72838_d((Entity)e);
        }
    }

    @Override
    public MCH_BulletModel getDefaultBulletModel() {
        return MCH_DefaultBulletModels.Bomb;
    }
}

