/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonObject
 *  net.minecraft.advancements.ICriterionTrigger
 *  net.minecraft.advancements.ICriterionTrigger$Listener
 *  net.minecraft.advancements.PlayerAdvancements
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.util.ResourceLocation
 */
package mcheli.__helper.criterion;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.Map;
import mcheli.__helper.criterion.MCH_SimpleListeners;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class MCH_SimpleTrigger
implements ICriterionTrigger<MCH_SimpleListeners.SimpleInstance> {
    private final Map<PlayerAdvancements, MCH_SimpleListeners> listeners = Maps.newHashMap();
    private final ResourceLocation id;

    public MCH_SimpleTrigger(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation func_192163_a() {
        return this.id;
    }

    public void func_192165_a(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<MCH_SimpleListeners.SimpleInstance> listener) {
        MCH_SimpleListeners listeners = this.listeners.get(playerAdvancementsIn);
        if (listeners == null) {
            listeners = new MCH_SimpleListeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, listeners);
        }
        listeners.add(listener);
    }

    public void func_192164_b(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<MCH_SimpleListeners.SimpleInstance> listener) {
        MCH_SimpleListeners listeners = this.listeners.get(playerAdvancementsIn);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    public void func_192167_a(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    public MCH_SimpleListeners.SimpleInstance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new MCH_SimpleListeners.SimpleInstance(this.id);
    }

    public void trigger(EntityPlayerMP player) {
        MCH_SimpleListeners listener = this.listeners.get(player.func_192039_O());
        if (listener != null) {
            listener.trigger();
        }
    }
}

