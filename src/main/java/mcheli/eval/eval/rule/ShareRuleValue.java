/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.rule;

import java.util.List;
import mcheli.eval.eval.EvalException;
import mcheli.eval.eval.Expression;
import mcheli.eval.eval.Rule;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.ShareExpValue;
import mcheli.eval.eval.lex.Lex;
import mcheli.eval.eval.lex.LexFactory;
import mcheli.eval.eval.oper.Operator;
import mcheli.eval.eval.ref.Refactor;
import mcheli.eval.eval.rule.AbstractRule;
import mcheli.eval.eval.srch.Search;
import mcheli.eval.eval.var.Variable;

public class ShareRuleValue
extends Rule {
    public AbstractRule topRule;
    public AbstractRule funcArgRule;
    public LexFactory lexFactory;
    protected List<String>[] opeList = new List[4];
    public AbstractExpression paren;

    @Override
    public Expression parse(String str) {
        if (str == null) {
            return null;
        }
        if (str.trim().length() <= 0) {
            return new EmptyExpression();
        }
        ShareExpValue exp = new ShareExpValue();
        AbstractExpression x = this.parse(str, exp);
        exp.setAbstractExpression(x);
        return exp;
    }

    public AbstractExpression parse(String str, ShareExpValue exp) {
        if (str == null) {
            return null;
        }
        Lex lex = this.lexFactory.create(str, this.opeList, this, exp);
        lex.check();
        AbstractExpression x = this.topRule.parse(lex);
        if (lex.getType() != Integer.MAX_VALUE) {
            throw new EvalException(1005, lex);
        }
        return x;
    }

    class EmptyExpression
    extends Expression {
        EmptyExpression() {
        }

        @Override
        public long evalLong() {
            return 0L;
        }

        @Override
        public double evalDouble() {
            return 0.0;
        }

        @Override
        public Object eval() {
            return null;
        }

        @Override
        public void optimizeLong(Variable var) {
        }

        @Override
        public void optimizeDouble(Variable var) {
        }

        @Override
        public void optimize(Variable var, Operator oper) {
        }

        @Override
        public void search(Search srch) {
        }

        @Override
        public void refactorName(Refactor ref) {
        }

        @Override
        public void refactorFunc(Refactor ref, Rule rule) {
        }

        @Override
        public Expression dup() {
            return new EmptyExpression();
        }

        @Override
        public boolean same(Expression obj) {
            return obj instanceof EmptyExpression;
        }

        @Override
        public String toString() {
            return "";
        }
    }
}

