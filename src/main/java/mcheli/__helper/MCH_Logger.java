/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Logger
 */
package mcheli.__helper;

import org.apache.logging.log4j.Logger;

public class MCH_Logger {
    private static Logger logger;

    public static void setLogger(Logger loggerIn) {
        if (logger == null) {
            logger = loggerIn;
        }
    }

    public static Logger get() {
        return logger;
    }
}

