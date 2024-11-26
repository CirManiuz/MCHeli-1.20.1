/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package mcheli.debug._v2;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.LinkedList;
import mcheli.__helper.debug.DebugBootstrap;
import mcheli.__helper.info.ContentRegistries;

public class ContentTest {
    public static void main(String[] args) {
        DebugBootstrap.init();
        File debugDir = new File("./run/");
        LinkedList contents = Lists.newLinkedList();
        System.out.println(debugDir.getAbsolutePath());
        ContentRegistries.loadContents(debugDir);
    }
}

