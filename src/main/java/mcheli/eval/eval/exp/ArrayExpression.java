/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.EvalException;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col2OpeExpression;
import mcheli.eval.eval.exp.ShareExpValue;

public class ArrayExpression
extends Col2OpeExpression {
    public ArrayExpression() {
        this.setOperator("[");
        this.setEndOperator("]");
    }

    protected ArrayExpression(ArrayExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new ArrayExpression(this, s);
    }

    @Override
    public long evalLong() {
        try {
            return this.share.var.evalLong(this.getVariable());
        }
        catch (EvalException e) {
            throw e;
        }
        catch (Exception e) {
            throw new EvalException(2201, this.toString(), this.string, this.pos, e);
        }
    }

    @Override
    public double evalDouble() {
        try {
            return this.share.var.evalDouble(this.getVariable());
        }
        catch (EvalException e) {
            throw e;
        }
        catch (Exception e) {
            throw new EvalException(2201, this.toString(), this.string, this.pos, e);
        }
    }

    @Override
    public Object evalObject() {
        return this.getVariable();
    }

    @Override
    protected Object getVariable() {
        Object obj = this.expl.getVariable();
        if (obj == null) {
            throw new EvalException(2104, this.expl.toString(), this.string, this.pos, null);
        }
        int index = (int)this.expr.evalLong();
        try {
            return this.share.var.getObject(obj, index);
        }
        catch (EvalException e) {
            throw e;
        }
        catch (Exception e) {
            throw new EvalException(2201, this.toString(), this.string, this.pos, e);
        }
    }

    @Override
    protected void let(Object val, int pos) {
        Object obj = this.expl.getVariable();
        if (obj == null) {
            throw new EvalException(2104, this.expl.toString(), this.string, pos, null);
        }
        int index = (int)this.expr.evalLong();
        try {
            this.share.var.setValue(obj, index, val);
        }
        catch (EvalException e) {
            throw e;
        }
        catch (Exception e) {
            throw new EvalException(2202, this.toString(), this.string, pos, e);
        }
    }

    @Override
    protected AbstractExpression replaceVar() {
        this.expl = this.expl.replaceVar();
        this.expr = this.expr.replace();
        return this.share.repl.replaceVar2(this);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.expl.toString());
        sb.append('[');
        sb.append(this.expr.toString());
        sb.append(']');
        return sb.toString();
    }
}

