/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col2Expression;
import mcheli.eval.eval.exp.ShareExpValue;

public class BitOrExpression
extends Col2Expression {
    public BitOrExpression() {
        this.setOperator("|");
    }

    protected BitOrExpression(BitOrExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new BitOrExpression(this, s);
    }

    @Override
    protected long operateLong(long vl, long vr) {
        return vl | vr;
    }

    @Override
    protected double operateDouble(double vl, double vr) {
        return (long)vl | (long)vr;
    }

    @Override
    protected Object operateObject(Object vl, Object vr) {
        return this.share.oper.bitOr(vl, vr);
    }
}

