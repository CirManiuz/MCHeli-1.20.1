/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval;

import mcheli.eval.eval.ExpRuleFactory;
import mcheli.eval.eval.Rule;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.func.Function;
import mcheli.eval.eval.oper.Operator;
import mcheli.eval.eval.ref.Refactor;
import mcheli.eval.eval.repl.Replace;
import mcheli.eval.eval.srch.Search;
import mcheli.eval.eval.var.Variable;

public abstract class Expression {
    public Variable var;
    public Function func;
    public Operator oper;
    public Search srch;
    public Replace repl;
    protected AbstractExpression ae;

    public static Expression parse(String str) {
        return ExpRuleFactory.getDefaultRule().parse(str);
    }

    public void setVariable(Variable var) {
        this.var = var;
    }

    public void setFunction(Function func) {
        this.func = func;
    }

    public void setOperator(Operator oper) {
        this.oper = oper;
    }

    public abstract long evalLong();

    public abstract double evalDouble();

    public abstract Object eval();

    public abstract void optimizeLong(Variable var1);

    public abstract void optimizeDouble(Variable var1);

    public abstract void optimize(Variable var1, Operator var2);

    public abstract void search(Search var1);

    public abstract void refactorName(Refactor var1);

    public abstract void refactorFunc(Refactor var1, Rule var2);

    public abstract Expression dup();

    public boolean equals(Object obj) {
        if (obj instanceof Expression) {
            AbstractExpression e = ((Expression)obj).ae;
            if (this.ae == null && e == null) {
                return true;
            }
            if (this.ae == null || e == null) {
                return false;
            }
            return this.ae.equals(e);
        }
        return super.equals(obj);
    }

    public int hashCode() {
        if (this.ae == null) {
            return 0;
        }
        return this.ae.hashCode();
    }

    public boolean same(Expression obj) {
        AbstractExpression e = obj.ae;
        if (this.ae == null) {
            return e == null;
        }
        return this.ae.same(e);
    }

    public boolean isEmpty() {
        return this.ae == null;
    }

    public String toString() {
        if (this.ae == null) {
            return "";
        }
        return this.ae.toString();
    }
}

