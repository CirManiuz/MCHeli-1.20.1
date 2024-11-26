/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col1AfterExpression;
import mcheli.eval.eval.exp.ShareExpValue;

public class DecAfterExpression
extends Col1AfterExpression {
    public DecAfterExpression() {
        this.setOperator("--");
    }

    protected DecAfterExpression(DecAfterExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new DecAfterExpression(this, s);
    }

    @Override
    protected long operateLong(long val) {
        this.exp.let(val - 1L, this.pos);
        return val;
    }

    @Override
    protected double operateDouble(double val) {
        this.exp.let(val - 1.0, this.pos);
        return val;
    }

    @Override
    public Object evalObject() {
        Object val = this.exp.evalObject();
        this.exp.let(this.share.oper.inc(val, -1), this.pos);
        return val;
    }
}

