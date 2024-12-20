/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col2Expression;
import mcheli.eval.eval.exp.ShareExpValue;

public class DivExpression
extends Col2Expression {
    public DivExpression() {
        this.setOperator("/");
    }

    protected DivExpression(DivExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new DivExpression(this, s);
    }

    @Override
    protected long operateLong(long vl, long vr) {
        return vl / vr;
    }

    @Override
    protected double operateDouble(double vl, double vr) {
        return vl / vr;
    }

    @Override
    protected Object operateObject(Object vl, Object vr) {
        return this.share.oper.div(vl, vr);
    }
}

