/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.common.collect.Collections2
 *  com.google.common.collect.Lists
 *  net.minecraft.block.properties.PropertyEnum
 */
package mcheli.__helper.block.properties;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import mcheli.__helper.block.EnumDirection8;
import net.minecraft.block.properties.PropertyEnum;

public class PropertyDirection8
extends PropertyEnum<EnumDirection8> {
    protected PropertyDirection8(String name, Collection<EnumDirection8> allowedValues) {
        super(name, EnumDirection8.class, allowedValues);
    }

    public static PropertyDirection8 create(String name) {
        return PropertyDirection8.create(name, (Predicate<EnumDirection8>)Predicates.alwaysTrue());
    }

    public static PropertyDirection8 create(String name, Predicate<EnumDirection8> filter) {
        return PropertyDirection8.create(name, Collections2.filter((Collection)Lists.newArrayList((Object[])EnumDirection8.values()), filter));
    }

    public static PropertyDirection8 create(String name, Collection<EnumDirection8> allowedValues) {
        return new PropertyDirection8(name, allowedValues);
    }
}

