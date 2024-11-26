/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package mcheli;

import java.util.HashMap;
import net.minecraft.entity.Entity;

public class MCH_DamageFactor {
    private HashMap<Class<? extends Entity>, Float> map = new HashMap();

    public void clear() {
        this.map.clear();
    }

    public void add(Class<? extends Entity> c, float value) {
        this.map.put(c, Float.valueOf(value));
    }

    public float getDamageFactor(Class<? extends Entity> c) {
        if (this.map.containsKey(c)) {
            return this.map.get(c).floatValue();
        }
        return 1.0f;
    }

    public float getDamageFactor(Entity e) {
        return e != null ? this.getDamageFactor(e.getClass()) : 1.0f;
    }
}

