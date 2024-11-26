/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col2OpeExpression;
import mcheli.eval.eval.exp.ShareExpValue;

public class AndExpression
extends Col2OpeExpression {
    public AndExpression() {
        this.setOperator("&&");
    }

    protected AndExpression(AndExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new AndExpression(this, s);
    }

    @Override
    public long evalLong() {
        long val = this.expl.evalLong();
        if (val == 0L) {
            return val;
        }
        return this.expr.evalLong();
    }

    @Override
    public double evalDouble() {
        double val = this.expl.evalDouble();
        if (val == 0.0) {
            return val;
        }
        return this.expr.evalDouble();
    }

    @Override
    public Object evalObject() {
        Object val = this.expl.evalObject();
        if (!this.share.oper.bool(val)) {
            return val;
        }
        return this.expr.evalObject();
    }
}

