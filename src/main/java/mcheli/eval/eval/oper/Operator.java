/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.oper;

public interface Operator {
    public Object power(Object var1, Object var2);

    public Object signPlus(Object var1);

    public Object signMinus(Object var1);

    public Object plus(Object var1, Object var2);

    public Object minus(Object var1, Object var2);

    public Object mult(Object var1, Object var2);

    public Object div(Object var1, Object var2);

    public Object mod(Object var1, Object var2);

    public Object bitNot(Object var1);

    public Object shiftLeft(Object var1, Object var2);

    public Object shiftRight(Object var1, Object var2);

    public Object shiftRightLogical(Object var1, Object var2);

    public Object bitAnd(Object var1, Object var2);

    public Object bitOr(Object var1, Object var2);

    public Object bitXor(Object var1, Object var2);

    public Object not(Object var1);

    public Object equal(Object var1, Object var2);

    public Object notEqual(Object var1, Object var2);

    public Object lessThan(Object var1, Object var2);

    public Object lessEqual(Object var1, Object var2);

    public Object greaterThan(Object var1, Object var2);

    public Object greaterEqual(Object var1, Object var2);

    public boolean bool(Object var1);

    public Object inc(Object var1, int var2);
}

