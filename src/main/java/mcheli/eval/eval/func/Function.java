/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.func;

public interface Function {
    public long evalLong(Object var1, String var2, Long[] var3) throws Throwable;

    public double evalDouble(Object var1, String var2, Double[] var3) throws Throwable;

    public Object evalObject(Object var1, String var2, Object[] var3) throws Throwable;
}

