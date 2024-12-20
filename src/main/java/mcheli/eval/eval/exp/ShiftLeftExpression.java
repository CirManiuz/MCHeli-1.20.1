/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col2Expression;
import mcheli.eval.eval.exp.ShareExpValue;

public class ShiftLeftExpression
extends Col2Expression {
    public ShiftLeftExpression() {
        this.setOperator("<<");
    }

    protected ShiftLeftExpression(ShiftLeftExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new ShiftLeftExpression(this, s);
    }

    @Override
    protected long operateLong(long vl, long vr) {
        return vl << (int)vr;
    }

    @Override
    protected double operateDouble(double vl, double vr) {
        return vl * Math.pow(2.0, vr);
    }

    @Override
    protected Object operateObject(Object vl, Object vr) {
        return this.share.oper.shiftLeft(vl, vr);
    }
}

