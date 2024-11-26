/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraftforge.client.event.MouseEvent
 *  net.minecraftforge.client.event.RenderLivingEvent$Post
 *  net.minecraftforge.client.event.RenderLivingEvent$Pre
 *  net.minecraftforge.client.event.RenderLivingEvent$Specials$Post
 *  net.minecraftforge.client.event.RenderLivingEvent$Specials$Pre
 *  net.minecraftforge.client.event.RenderPlayerEvent$Post
 *  net.minecraftforge.client.event.RenderPlayerEvent$Pre
 *  net.minecraftforge.event.entity.EntityJoinWorldEvent
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package mcheli.wrapper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class W_ClientEventHook {
    @SubscribeEvent
    public void onEvent_MouseEvent(MouseEvent event) {
        this.mouseEvent(event);
    }

    public void mouseEvent(MouseEvent event) {
    }

    @SubscribeEvent
    public void onEvent_renderLivingEventSpecialsPre(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        this.renderLivingEventSpecialsPre(event);
    }

    public void renderLivingEventSpecialsPre(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
    }

    @SubscribeEvent
    public void onEvent_renderLivingEventSpecialsPost(RenderLivingEvent.Specials.Post<EntityLivingBase> event) {
        this.renderLivingEventSpecialsPost(event);
    }

    public void renderLivingEventSpecialsPost(RenderLivingEvent.Specials.Post<EntityLivingBase> event) {
    }

    @SubscribeEvent
    public void onEvent_renderLivingEventPre(RenderLivingEvent.Pre<EntityLivingBase> event) {
        this.renderLivingEventPre(event);
    }

    public void renderLivingEventPre(RenderLivingEvent.Pre<EntityLivingBase> event) {
    }

    @SubscribeEvent
    public void onEvent_renderLivingEventPost(RenderLivingEvent.Post<EntityLivingBase> event) {
        this.renderLivingEventPost(event);
    }

    public void renderLivingEventPost(RenderLivingEvent.Post<EntityLivingBase> event) {
    }

    @SubscribeEvent
    public void onEvent_renderPlayerPre(RenderPlayerEvent.Pre event) {
        this.renderPlayerPre(event);
    }

    public void renderPlayerPre(RenderPlayerEvent.Pre event) {
    }

    @SubscribeEvent
    public void Event_renderPlayerPost(RenderPlayerEvent.Post event) {
        this.renderPlayerPost(event);
    }

    public void renderPlayerPost(RenderPlayerEvent.Post event) {
    }

    @SubscribeEvent
    public void onEvent_WorldEventUnload(WorldEvent.Unload event) {
        this.worldEventUnload(event);
    }

    public void worldEventUnload(WorldEvent.Unload event) {
    }

    @SubscribeEvent
    public void onEvent_EntityJoinWorldEvent(EntityJoinWorldEvent event) {
        this.entityJoinWorldEvent(event);
    }

    public void entityJoinWorldEvent(EntityJoinWorldEvent event) {
    }
}

