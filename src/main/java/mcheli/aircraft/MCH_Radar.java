/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.world.World
 */
package mcheli.aircraft;

import java.util.ArrayList;
import java.util.List;
import mcheli.MCH_Vector2;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

public class MCH_Radar {
    private World worldObj;
    private ArrayList<MCH_Vector2> entityList = new ArrayList();
    private ArrayList<MCH_Vector2> enemyList = new ArrayList();

    public ArrayList<MCH_Vector2> getEntityList() {
        return this.entityList;
    }

    public ArrayList<MCH_Vector2> getEnemyList() {
        return this.enemyList;
    }

    public MCH_Radar(World world) {
        this.worldObj = world;
    }

    public void clear() {
        this.entityList.clear();
        this.enemyList.clear();
    }

    public void updateXZ(Entity centerEntity, int range) {
        if (!this.worldObj.field_72995_K) {
            return;
        }
        this.clear();
        List list = centerEntity.field_70170_p.func_72839_b(centerEntity, centerEntity.func_174813_aQ().func_72314_b((double)range, (double)range, (double)range));
        for (int i = 0; i < list.size(); ++i) {
            double z;
            double x;
            Entity entity = (Entity)list.get(i);
            if (!(entity instanceof EntityLiving) || !((x = entity.field_70165_t - centerEntity.field_70165_t) * x + (z = entity.field_70161_v - centerEntity.field_70161_v) * z < (double)(range * range))) continue;
            int y = 1 + (int)entity.field_70163_u;
            if (y < 0) {
                y = 1;
            }
            int blockCnt = 0;
            while (y < 200 && (W_WorldFunc.getBlockId(this.worldObj, (int)entity.field_70165_t, y, (int)entity.field_70161_v) == 0 || ++blockCnt < 5)) {
                ++y;
            }
            if (blockCnt >= 5) continue;
            if (entity instanceof EntityMob) {
                this.enemyList.add(new MCH_Vector2(x, z));
                continue;
            }
            this.entityList.add(new MCH_Vector2(x, z));
        }
    }
}

