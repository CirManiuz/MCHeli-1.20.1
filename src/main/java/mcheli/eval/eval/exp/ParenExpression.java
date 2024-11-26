/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col1Expression;
import mcheli.eval.eval.exp.ShareExpValue;

public class ParenExpression
extends Col1Expression {
    public ParenExpression() {
        this.setOperator("(");
        this.setEndOperator(")");
    }

    protected ParenExpression(ParenExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new ParenExpression(this, s);
    }

    @Override
    protected long operateLong(long val) {
        return val;
    }

    @Override
    protected double operateDouble(double val) {
        return val;
    }

    @Override
    public Object evalObject() {
        return this.exp.evalObject();
    }

    @Override
    public String toString() {
        if (this.exp == null) {
            return "";
        }
        return this.getOperator() + this.exp.toString() + this.getEndOperator();
    }
}

