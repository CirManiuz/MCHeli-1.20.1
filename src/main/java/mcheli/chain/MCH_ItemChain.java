/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.world.World
 */
package mcheli.chain;

import java.util.List;
import javax.annotation.Nullable;
import mcheli.MCH_Config;
import mcheli.aircraft.MCH_EntityHitBox;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.chain.MCH_EntityChain;
import mcheli.parachute.MCH_EntityParachute;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.vehicle.MCH_EntityVehicle;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_Item;
import mcheli.wrapper.W_Lib;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MCH_ItemChain
extends W_Item {
    public MCH_ItemChain(int par1) {
        super(par1);
        this.func_77625_d(1);
    }

    public static void interactEntity(ItemStack item, @Nullable Entity entity, EntityPlayer player, World world) {
        if (!world.field_72995_K && entity != null && !entity.field_70128_L) {
            if (entity instanceof EntityItem) {
                return;
            }
            if (entity instanceof MCH_EntityChain) {
                return;
            }
            if (entity instanceof MCH_EntityHitBox) {
                return;
            }
            if (entity instanceof MCH_EntitySeat) {
                return;
            }
            if (entity instanceof MCH_EntityUavStation) {
                return;
            }
            if (entity instanceof MCH_EntityParachute) {
                return;
            }
            if (W_Lib.isEntityLivingBase(entity)) {
                return;
            }
            if (MCH_Config.FixVehicleAtPlacedPoint.prmBool && entity instanceof MCH_EntityVehicle) {
                return;
            }
            MCH_EntityChain towingChain = MCH_ItemChain.getTowedEntityChain(entity);
            if (towingChain != null) {
                towingChain.func_70106_y();
                return;
            }
            Entity entityTowed = MCH_ItemChain.getTowedEntity(item, world);
            if (entityTowed == null) {
                MCH_ItemChain.playConnectTowedEntity(entity);
                MCH_ItemChain.setTowedEntity(item, entity);
            } else {
                if (W_Entity.isEqual(entityTowed, entity)) {
                    return;
                }
                double diff = entity.func_70032_d(entityTowed);
                if (diff < 2.0 || diff > 16.0) {
                    return;
                }
                MCH_EntityChain chain = new MCH_EntityChain(world, (entityTowed.field_70165_t + entity.field_70165_t) / 2.0, (entityTowed.field_70163_u + entity.field_70163_u) / 2.0, (entityTowed.field_70161_v + entity.field_70161_v) / 2.0);
                chain.setChainLength((int)diff);
                chain.setTowEntity(entityTowed, entity);
                chain.field_70169_q = chain.field_70165_t;
                chain.field_70167_r = chain.field_70163_u;
                chain.field_70166_s = chain.field_70161_v;
                world.func_72838_d((Entity)chain);
                MCH_ItemChain.playConnectTowingEntity(entity);
                MCH_ItemChain.setTowedEntity(item, null);
            }
        }
    }

    public static void playConnectTowingEntity(Entity e) {
        W_WorldFunc.MOD_playSoundEffect(e.field_70170_p, e.field_70165_t, e.field_70163_u, e.field_70161_v, "chain_ct", 1.0f, 1.0f);
    }

    public static void playConnectTowedEntity(Entity e) {
        W_WorldFunc.MOD_playSoundEffect(e.field_70170_p, e.field_70165_t, e.field_70163_u, e.field_70161_v, "chain", 1.0f, 1.0f);
    }

    public void func_77622_d(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
    }

    @Nullable
    public static MCH_EntityChain getTowedEntityChain(Entity entity) {
        List list = entity.field_70170_p.func_72872_a(MCH_EntityChain.class, entity.func_174813_aQ().func_72314_b(25.0, 25.0, 25.0));
        if (list == null) {
            return null;
        }
        for (int i = 0; i < list.size(); ++i) {
            MCH_EntityChain chain = (MCH_EntityChain)((Object)list.get(i));
            if (!chain.isTowingEntity()) continue;
            if (W_Entity.isEqual(chain.towEntity, entity)) {
                return chain;
            }
            if (!W_Entity.isEqual(chain.towedEntity, entity)) continue;
            return chain;
        }
        return null;
    }

    public static void setTowedEntity(ItemStack item, @Nullable Entity entity) {
        NBTTagCompound nbt = item.func_77978_p();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            item.func_77982_d(nbt);
        }
        if (entity != null && !entity.field_70128_L) {
            nbt.func_74768_a("TowedEntityId", W_Entity.getEntityId(entity));
            nbt.func_74778_a("TowedEntityUUID", entity.getPersistentID().toString());
        } else {
            nbt.func_74768_a("TowedEntityId", 0);
            nbt.func_74778_a("TowedEntityUUID", "");
        }
    }

    @Nullable
    public static Entity getTowedEntity(ItemStack item, World world) {
        NBTTagCompound nbt = item.func_77978_p();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            item.func_77982_d(nbt);
        } else if (nbt.func_74764_b("TowedEntityId") && nbt.func_74764_b("TowedEntityUUID")) {
            int id = nbt.func_74762_e("TowedEntityId");
            String uuid = nbt.func_74779_i("TowedEntityUUID");
            Entity entity = world.func_73045_a(id);
            if (entity != null && !entity.field_70128_L && uuid.compareTo(entity.getPersistentID().toString()) == 0) {
                return entity;
            }
        }
        return null;
    }
}

