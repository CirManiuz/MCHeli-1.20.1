/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.Expression;
import mcheli.eval.eval.Rule;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.OptimizeDouble;
import mcheli.eval.eval.exp.OptimizeLong;
import mcheli.eval.eval.exp.OptimizeObject;
import mcheli.eval.eval.exp.Replace4RefactorGetter;
import mcheli.eval.eval.exp.Search4RefactorName;
import mcheli.eval.eval.func.InvokeFunction;
import mcheli.eval.eval.oper.JavaExOperator;
import mcheli.eval.eval.oper.Operator;
import mcheli.eval.eval.ref.Refactor;
import mcheli.eval.eval.repl.Replace;
import mcheli.eval.eval.srch.Search;
import mcheli.eval.eval.var.MapVariable;
import mcheli.eval.eval.var.Variable;

public class ShareExpValue
extends Expression {
    public AbstractExpression paren;

    public void setAbstractExpression(AbstractExpression ae) {
        this.ae = ae;
    }

    public void initVar() {
        if (this.var == null) {
            this.var = new MapVariable();
        }
    }

    public void initOper() {
        if (this.oper == null) {
            this.oper = new JavaExOperator();
        }
    }

    public void initFunc() {
        if (this.func == null) {
            this.func = new InvokeFunction();
        }
    }

    @Override
    public long evalLong() {
        this.initVar();
        this.initFunc();
        return this.ae.evalLong();
    }

    @Override
    public double evalDouble() {
        this.initVar();
        this.initFunc();
        return this.ae.evalDouble();
    }

    @Override
    public Object eval() {
        this.initVar();
        this.initOper();
        this.initFunc();
        return this.ae.evalObject();
    }

    @Override
    public void optimizeLong(Variable var) {
        this.optimize(var, new OptimizeLong());
    }

    @Override
    public void optimizeDouble(Variable var) {
        this.optimize(var, new OptimizeDouble());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void optimize(Variable var, Operator oper) {
        Operator bak = this.oper;
        this.oper = oper;
        try {
            this.optimize(var, new OptimizeObject());
        }
        finally {
            this.oper = bak;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void optimize(Variable var, Replace repl) {
        Variable bak = this.var;
        if (var == null) {
            var = new MapVariable();
        }
        this.var = var;
        this.repl = repl;
        try {
            this.ae = this.ae.replace();
        }
        finally {
            this.var = bak;
        }
    }

    @Override
    public void search(Search srch) {
        if (srch == null) {
            throw new NullPointerException();
        }
        this.srch = srch;
        this.ae.search();
    }

    @Override
    public void refactorName(Refactor ref) {
        if (ref == null) {
            throw new NullPointerException();
        }
        this.srch = new Search4RefactorName(ref);
        this.ae.search();
    }

    @Override
    public void refactorFunc(Refactor ref, Rule rule) {
        if (ref == null) {
            throw new NullPointerException();
        }
        this.repl = new Replace4RefactorGetter(ref, rule);
        this.ae.replace();
    }

    @Override
    public boolean same(Expression obj) {
        if (obj instanceof ShareExpValue) {
            AbstractExpression p = ((ShareExpValue)obj).paren;
            return this.paren.same(p) && super.same(obj);
        }
        return false;
    }

    @Override
    public Expression dup() {
        ShareExpValue n = new ShareExpValue();
        n.ae = this.ae.dup(n);
        n.paren = this.paren.dup(n);
        return n;
    }
}

