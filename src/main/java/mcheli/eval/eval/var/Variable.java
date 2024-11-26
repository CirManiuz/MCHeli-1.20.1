/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.var;

public interface Variable {
    public void setValue(Object var1, Object var2);

    public Object getObject(Object var1);

    public long evalLong(Object var1);

    public double evalDouble(Object var1);

    public Object getObject(Object var1, int var2);

    public void setValue(Object var1, int var2, Object var3);

    public Object getObject(Object var1, String var2);

    public void setValue(Object var1, String var2, Object var3);
}

