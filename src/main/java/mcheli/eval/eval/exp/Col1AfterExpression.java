/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col1Expression;
import mcheli.eval.eval.exp.ShareExpValue;

public abstract class Col1AfterExpression
extends Col1Expression {
    protected Col1AfterExpression() {
    }

    protected Col1AfterExpression(Col1Expression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    protected AbstractExpression replace() {
        this.exp = this.exp.replaceVar();
        return this.share.repl.replaceVar1(this);
    }

    @Override
    protected AbstractExpression replaceVar() {
        return this.replace();
    }

    @Override
    public String toString() {
        if (this.exp == null) {
            return this.getOperator();
        }
        StringBuffer sb = new StringBuffer();
        if (this.exp.getPriority() > this.prio) {
            sb.append(this.exp.toString());
            sb.append(this.getOperator());
        } else if (this.exp.getPriority() == this.prio) {
            sb.append(this.exp.toString());
            sb.append(' ');
            sb.append(this.getOperator());
        } else {
            sb.append(this.share.paren.getOperator());
            sb.append(this.exp.toString());
            sb.append(this.share.paren.getEndOperator());
            sb.append(this.getOperator());
        }
        return sb.toString();
    }
}

