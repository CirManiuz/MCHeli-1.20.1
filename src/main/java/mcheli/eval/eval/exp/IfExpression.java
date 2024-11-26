/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col3Expression;
import mcheli.eval.eval.exp.ShareExpValue;

public class IfExpression
extends Col3Expression {
    public IfExpression() {
        this.setOperator("?");
        this.setEndOperator(":");
    }

    protected IfExpression(IfExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new IfExpression(this, s);
    }

    @Override
    public long evalLong() {
        if (this.exp1.evalLong() != 0L) {
            return this.exp2.evalLong();
        }
        return this.exp3.evalLong();
    }

    @Override
    public double evalDouble() {
        if (this.exp1.evalDouble() != 0.0) {
            return this.exp2.evalDouble();
        }
        return this.exp3.evalDouble();
    }

    @Override
    public Object evalObject() {
        if (this.share.oper.bool(this.exp1.evalObject())) {
            return this.exp2.evalObject();
        }
        return this.exp3.evalObject();
    }
}

