/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.PowerExpression;
import mcheli.eval.eval.exp.ShareExpValue;

public class LetPowerExpression
extends PowerExpression {
    public LetPowerExpression() {
        this.setOperator("**=");
    }

    protected LetPowerExpression(LetPowerExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new LetPowerExpression(this, s);
    }

    @Override
    public long evalLong() {
        long val = super.evalLong();
        this.expl.let(val, this.pos);
        return val;
    }

    @Override
    public double evalDouble() {
        double val = super.evalDouble();
        this.expl.let(val, this.pos);
        return val;
    }

    @Override
    public Object evalObject() {
        Object val = super.evalObject();
        this.expl.let(val, this.pos);
        return val;
    }

    @Override
    protected AbstractExpression replace() {
        this.expl = this.expl.replaceVar();
        this.expr = this.expr.replace();
        return this.share.repl.replaceLet(this);
    }
}

