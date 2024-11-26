/*
 * Decompiled with CFR 0.152.
 */
package mcheli.__helper.debug;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.SOURCE)
public @interface Debug {
    public String id() default "";

    public String version();

    public String target() default "";
}

