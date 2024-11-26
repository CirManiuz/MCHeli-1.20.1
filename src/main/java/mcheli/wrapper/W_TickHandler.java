/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 *  net.minecraftforge.fml.common.gameevent.TickEvent$PlayerTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$RenderTickEvent
 */
package mcheli.wrapper;

import mcheli.wrapper.ITickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class W_TickHandler
implements ITickHandler {
    protected Minecraft mc;

    public W_TickHandler(Minecraft m) {
        this.mc = m;
    }

    public void onPlayerTickPre(EntityPlayer player) {
    }

    public void onPlayerTickPost(EntityPlayer player) {
    }

    public void onRenderTickPre(float partialTicks) {
    }

    public void onRenderTickPost(float partialTicks) {
    }

    public void onTickPre() {
    }

    public void onTickPost() {
    }

    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.onPlayerTickPre(event.player);
        }
        if (event.phase == TickEvent.Phase.END) {
            this.onPlayerTickPost(event.player);
        }
    }

    @SubscribeEvent
    public void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.onTickPre();
        }
        if (event.phase == TickEvent.Phase.END) {
            this.onTickPost();
        }
    }

    @SubscribeEvent
    public void onRenderTickEvent(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.onRenderTickPre(event.renderTickTime);
        }
        if (event.phase == TickEvent.Phase.END) {
            this.onRenderTickPost(event.renderTickTime);
        }
    }

    static enum TickType {
        RENDER,
        CLIENT;

    }
}

