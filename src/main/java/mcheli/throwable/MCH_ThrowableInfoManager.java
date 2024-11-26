/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 */
package mcheli.throwable;

import mcheli.__helper.info.ContentRegistries;
import mcheli.throwable.MCH_ThrowableInfo;
import net.minecraft.item.Item;

public class MCH_ThrowableInfoManager {
    public static MCH_ThrowableInfo get(String name) {
        return ContentRegistries.throwable().get(name);
    }

    public static MCH_ThrowableInfo get(Item item) {
        return ContentRegistries.throwable().findFirst(info -> info.item == item);
    }
}

