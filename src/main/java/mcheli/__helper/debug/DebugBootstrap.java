/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package mcheli.__helper.debug;

import mcheli.MCH_ClientProxy;
import mcheli.MCH_MOD;
import mcheli.__helper.MCH_Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DebugBootstrap {
    private static final Logger LOGGER = LogManager.getLogger((String)"Debug log");

    public static void init() {
        MCH_Logger.setLogger(LOGGER);
        MCH_MOD.instance = new MCH_MOD();
        MCH_MOD.proxy = new MCH_ClientProxy();
    }
}

