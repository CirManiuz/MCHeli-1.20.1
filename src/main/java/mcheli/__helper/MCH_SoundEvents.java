/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  javax.annotation.Nullable
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.world.World
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.registry.ForgeRegistries
 */
package mcheli.__helper;

import com.google.common.collect.Sets;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import mcheli.MCH_Lib;
import mcheli.__helper.MCH_Utils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber(modid="mcheli")
public class MCH_SoundEvents {
    private static final Set<ResourceLocation> registryWrapper = Sets.newLinkedHashSet();

    @SubscribeEvent
    static void onSoundEventRegisterEvent(RegistryEvent.Register<SoundEvent> event) {
        for (ResourceLocation soundLocation : registryWrapper) {
            event.getRegistry().register(new SoundEvent(soundLocation).setRegistryName(soundLocation));
        }
    }

    public static void registerSoundEventName(String name) {
        MCH_SoundEvents.registerSoundEventName(MCH_Utils.suffix(name));
    }

    public static void registerSoundEventName(ResourceLocation name) {
        registryWrapper.add(name);
    }

    public static void playSound(World w, double x, double y, double z, String name, float volume, float pitch) {
        SoundEvent sound = MCH_SoundEvents.getSound(name);
        w.func_184148_a(null, x, y, z, sound, SoundCategory.MASTER, volume, pitch);
    }

    public static SoundEvent getSound(String name) {
        SoundEvent sound = MCH_SoundEvents.getSound(new ResourceLocation(name));
        return Objects.requireNonNull(sound);
    }

    @Nullable
    public static SoundEvent getSound(ResourceLocation location) {
        SoundEvent sound = (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(location);
        if (sound == null) {
            MCH_Lib.Log("[WARNING] Sound event does not found. event name= " + location, new Object[0]);
        }
        return sound;
    }
}

