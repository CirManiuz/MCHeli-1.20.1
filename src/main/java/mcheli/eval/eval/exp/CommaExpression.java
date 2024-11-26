/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col2OpeExpression;
import mcheli.eval.eval.exp.ShareExpValue;

public class CommaExpression
extends Col2OpeExpression {
    public CommaExpression() {
        this.setOperator(",");
    }

    protected CommaExpression(CommaExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new CommaExpression(this, s);
    }

    @Override
    public long evalLong() {
        this.expl.evalLong();
        return this.expr.evalLong();
    }

    @Override
    public double evalDouble() {
        this.expl.evalDouble();
        return this.expr.evalDouble();
    }

    @Override
    public Object evalObject() {
        this.expl.evalObject();
        return this.expr.evalObject();
    }

    @Override
    protected String toStringLeftSpace() {
        return "";
    }
}

