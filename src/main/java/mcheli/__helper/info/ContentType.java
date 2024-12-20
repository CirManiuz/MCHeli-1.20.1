/*
 * Decompiled with CFR 0.152.
 */
package mcheli.__helper.info;

public enum ContentType {
    HELICOPTER("helicopter", "helicopters"),
    PLANE("plane", "plane"),
    TANK("tank", "tanks"),
    VEHICLE("vehicle", "vehicles"),
    WEAPON("weapon", "weapons"),
    THROWABLE("throwable", "throwable"),
    HUD("hud", "hud");

    public final String type;
    public final String dirName;

    private ContentType(String typeName, String dirName) {
        this.type = typeName;
        this.dirName = dirName;
    }

    public static boolean validateDirName(String dirName) {
        for (ContentType type : ContentType.values()) {
            if (!type.dirName.equals(dirName)) continue;
            return true;
        }
        return false;
    }
}

