/*
 * Decompiled with CFR 0.152.
 */
package mcheli.aircraft;

import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_SeatRackInfo;

public interface MCH_IEntityCanRideAircraft {
    public boolean isSkipNormalRender();

    public boolean canRideAircraft(MCH_EntityAircraft var1, int var2, MCH_SeatRackInfo var3);
}

