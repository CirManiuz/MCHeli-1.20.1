/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col2Expression;
import mcheli.eval.eval.exp.ShareExpValue;

public class PowerExpression
extends Col2Expression {
    public PowerExpression() {
        this.setOperator("**");
    }

    protected PowerExpression(PowerExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new PowerExpression(this, s);
    }

    @Override
    protected long operateLong(long vl, long vr) {
        return (long)Math.pow(vl, vr);
    }

    @Override
    protected double operateDouble(double vl, double vr) {
        return Math.pow(vl, vr);
    }

    @Override
    protected Object operateObject(Object vl, Object vr) {
        return this.share.oper.power(vl, vr);
    }
}

