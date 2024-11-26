/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  net.minecraft.util.ResourceLocation
 */
package mcheli.__helper.addon;

import com.google.common.base.Strings;
import java.util.Locale;
import mcheli.__helper.MCH_Utils;
import net.minecraft.util.ResourceLocation;

public class AddonResourceLocation
extends ResourceLocation {
    public static final AddonResourceLocation EMPTY_LOCATION = new AddonResourceLocation();
    public static final String SHARE_DOMAIN = "<!mcheli_share_domain>";
    public static final char SEPARATOR = '|';
    private final String addonDomain;
    private final boolean isEmpty;

    private AddonResourceLocation() {
        super(0, new String[]{"empty", "empty"});
        this.addonDomain = "@empty";
        this.isEmpty = true;
    }

    protected AddonResourceLocation(int unused, String ... resourceName) {
        super(unused, new String[]{resourceName[0], resourceName[2]});
        this.addonDomain = resourceName[1];
        this.isEmpty = false;
    }

    public AddonResourceLocation(String resourceName) {
        this(0, AddonResourceLocation.parsePath(resourceName));
    }

    public AddonResourceLocation(String resourceName, String addonDomainIn) {
        this(0, AddonResourceLocation.parsePath(addonDomainIn, resourceName));
    }

    public AddonResourceLocation(ResourceLocation resourceLocation, String addonDomainIn) {
        this(resourceLocation.toString(), addonDomainIn);
    }

    public AddonResourceLocation(String resourceDomainIn, String addonDomainIn, String resourcePathIn) {
        this(0, AddonResourceLocation.parsePath(resourceDomainIn + ":" + addonDomainIn + '|' + resourcePathIn));
    }

    protected static String[] parsePath(String pathIn) {
        String[] spl = AddonResourceLocation.func_177516_a((String)pathIn);
        String[] ret = new String[]{spl[0], null, spl[1]};
        int i = ret[2].indexOf(124);
        if (i >= 0) {
            ret[1] = ret[2].substring(0, i);
            ret[2] = ret[2].substring(i + 1);
        }
        String string = ret[1] = Strings.isNullOrEmpty((String)ret[1]) ? SHARE_DOMAIN : ret[1].toLowerCase(Locale.ROOT);
        if (ret[0].equals("minecraft")) {
            ret[0] = "mcheli";
        } else if (!spl[0].equals("mcheli")) {
            MCH_Utils.logger().warn("Invalid mod domain '{}', replace at '{}'. path:'{}'", (Object)ret[0], (Object)"mcheli", (Object)ret[2]);
            ret[0] = "mcheli";
        }
        return ret;
    }

    protected static String[] parsePath(String addonDomain, String pathIn) {
        String[] spl = AddonResourceLocation.func_177516_a((String)pathIn);
        String[] ret = new String[]{spl[0], addonDomain, spl[1]};
        return ret;
    }

    public static AddonResourceLocation share(ResourceLocation location) {
        return AddonResourceLocation.share(location.func_110624_b(), location.func_110623_a());
    }

    public static AddonResourceLocation share(String modid, String location) {
        return new AddonResourceLocation(modid, SHARE_DOMAIN, location);
    }

    public String getAddonDomain() {
        return this.addonDomain;
    }

    public String getAddonLocation() {
        return this.addonDomain + '|' + this.field_110625_b;
    }

    public ResourceLocation asVanillaLocation() {
        return new ResourceLocation(this.field_110626_a, this.getAddonLocation());
    }

    public ResourceLocation asVanillaDomainPath() {
        return new ResourceLocation(this.field_110626_a, this.field_110625_b);
    }

    public boolean isShareDomain() {
        return this.addonDomain.equals(SHARE_DOMAIN);
    }

    public boolean isEmptyLocation() {
        return this.isEmpty || this.equals((Object)EMPTY_LOCATION);
    }

    public int hashCode() {
        return 31 * (31 * super.hashCode() + this.addonDomain.hashCode()) + Boolean.hashCode(this.isEmpty);
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof AddonResourceLocation && super.equals(p_equals_1_)) {
            AddonResourceLocation location = (AddonResourceLocation)((Object)p_equals_1_);
            return location.addonDomain.equals(this.addonDomain) && location.isEmpty == this.isEmpty;
        }
        return false;
    }

    public boolean equalsIgnore(ResourceLocation location) {
        if (this == location) {
            return true;
        }
        if (super.equals((Object)location)) {
            if (this.isShareDomain()) {
                return true;
            }
            if (location instanceof AddonResourceLocation) {
                AddonResourceLocation other = (AddonResourceLocation)location;
                if (other.isShareDomain()) {
                    return true;
                }
                return other.addonDomain.equals(this.addonDomain);
            }
            return false;
        }
        return false;
    }

    public int compareTo(ResourceLocation p_compareTo_1_) {
        int i = this.field_110626_a.compareTo(p_compareTo_1_.func_110624_b());
        if (i == 0 && p_compareTo_1_ instanceof AddonResourceLocation) {
            i = this.addonDomain.compareTo(((AddonResourceLocation)p_compareTo_1_).addonDomain);
        }
        if (i == 0) {
            i = this.field_110625_b.compareTo(p_compareTo_1_.func_110623_a());
        }
        return i;
    }

    public String toString() {
        return this.field_110626_a + ":" + this.addonDomain + '|' + this.field_110625_b;
    }
}

