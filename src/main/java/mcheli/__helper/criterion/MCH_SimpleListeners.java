/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.minecraft.advancements.ICriterionTrigger$Listener
 *  net.minecraft.advancements.PlayerAdvancements
 *  net.minecraft.advancements.critereon.AbstractCriterionInstance
 *  net.minecraft.util.ResourceLocation
 */
package mcheli.__helper.criterion;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.util.ResourceLocation;

public class MCH_SimpleListeners {
    private final Set<ICriterionTrigger.Listener<SimpleInstance>> listeners = Sets.newHashSet();
    private final PlayerAdvancements playerAdvancements;

    public MCH_SimpleListeners(PlayerAdvancements playerAdvancements) {
        this.playerAdvancements = playerAdvancements;
    }

    public boolean isEmpty() {
        return this.listeners.isEmpty();
    }

    public void add(ICriterionTrigger.Listener<SimpleInstance> listener) {
        this.listeners.add(listener);
    }

    public void remove(ICriterionTrigger.Listener<SimpleInstance> listener) {
        this.listeners.remove(listener);
    }

    public void trigger() {
        for (ICriterionTrigger.Listener<SimpleInstance> listener : this.listeners) {
            listener.func_192159_a(this.playerAdvancements);
        }
    }

    static class SimpleInstance
    extends AbstractCriterionInstance {
        public SimpleInstance(ResourceLocation criterionIn) {
            super(criterionIn);
        }
    }
}

