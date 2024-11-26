/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.EvalException;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col2Expression;
import mcheli.eval.eval.exp.FieldExpression;
import mcheli.eval.eval.exp.FunctionExpression;
import mcheli.eval.eval.exp.VariableExpression;
import mcheli.eval.eval.exp.WordExpression;
import mcheli.eval.eval.ref.Refactor;
import mcheli.eval.eval.srch.SearchAdapter;

public class Search4RefactorName
extends SearchAdapter {
    protected Refactor ref;

    Search4RefactorName(Refactor ref) {
        this.ref = ref;
    }

    @Override
    public void search0(WordExpression exp) {
        String name;
        if (exp instanceof VariableExpression && (name = this.ref.getNewName(null, exp.getWord())) != null) {
            exp.setWord(name);
        }
    }

    @Override
    public boolean search2_2(Col2Expression exp) {
        if (exp instanceof FieldExpression) {
            AbstractExpression exp1 = exp.expl;
            Object obj = exp1.getVariable();
            if (obj == null) {
                throw new EvalException(2104, this.toString(), exp1.string, exp1.pos, null);
            }
            AbstractExpression exp2 = exp.expr;
            String name = this.ref.getNewName(obj, exp2.getWord());
            if (name != null) {
                exp2.setWord(name);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean searchFunc_2(FunctionExpression exp) {
        String name;
        Object obj = null;
        if (exp.target != null) {
            obj = exp.target.getVariable();
        }
        if ((name = this.ref.getNewFuncName(obj, exp.name)) != null) {
            exp.name = name;
        }
        return false;
    }
}

