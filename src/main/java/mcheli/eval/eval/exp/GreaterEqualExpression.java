/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col2Expression;
import mcheli.eval.eval.exp.ShareExpValue;

public class GreaterEqualExpression
extends Col2Expression {
    public GreaterEqualExpression() {
        this.setOperator(">=");
    }

    protected GreaterEqualExpression(GreaterEqualExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new GreaterEqualExpression(this, s);
    }

    @Override
    protected long operateLong(long vl, long vr) {
        return vl >= vr ? 1L : 0L;
    }

    @Override
    protected double operateDouble(double vl, double vr) {
        return vl >= vr ? 1.0 : 0.0;
    }

    @Override
    protected Object operateObject(Object vl, Object vr) {
        return this.share.oper.greaterEqual(vl, vr);
    }
}
