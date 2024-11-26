/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.rule;

import mcheli.eval.eval.EvalException;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col1Expression;
import mcheli.eval.eval.exp.Col2Expression;
import mcheli.eval.eval.exp.FunctionExpression;
import mcheli.eval.eval.lex.Lex;
import mcheli.eval.eval.rule.AbstractRule;
import mcheli.eval.eval.rule.ShareRuleValue;

public class Col1AfterRule
extends AbstractRule {
    public AbstractExpression func;
    public AbstractExpression array;
    public AbstractExpression field;

    public Col1AfterRule(ShareRuleValue share) {
        super(share);
    }

    @Override
    public AbstractExpression parse(Lex lex) {
        AbstractExpression x = this.nextRule.parse(lex);
        block3: while (true) {
            switch (lex.getType()) {
                case 0x7FFFFFF2: {
                    String ope = lex.getOperator();
                    int pos = lex.getPos();
                    if (this.isMyOperator(ope)) {
                        if (lex.isOperator(this.func.getOperator())) {
                            x = this.parseFunc(lex, x);
                            continue block3;
                        }
                        if (lex.isOperator(this.array.getOperator())) {
                            x = this.parseArray(lex, x, ope, pos);
                            continue block3;
                        }
                        if (lex.isOperator(this.field.getOperator())) {
                            x = this.parseField(lex, x, ope, pos);
                            continue block3;
                        }
                        x = Col1Expression.create(this.newExpression(ope, lex.getShare()), lex.getString(), pos, x);
                        lex.next();
                        continue block3;
                    }
                    return x;
                }
            }
            break;
        }
        return x;
    }

    protected AbstractExpression parseFunc(Lex lex, AbstractExpression x) {
        AbstractExpression a = null;
        lex.next();
        if (!lex.isOperator(this.func.getEndOperator())) {
            a = this.share.funcArgRule.parse(lex);
            if (!lex.isOperator(this.func.getEndOperator())) {
                throw new EvalException(1001, new String[]{this.func.getEndOperator()}, lex);
            }
        }
        lex.next();
        x = FunctionExpression.create(x, a, this.prio, lex.getShare());
        return x;
    }

    protected AbstractExpression parseArray(Lex lex, AbstractExpression x, String ope, int pos) {
        AbstractExpression y = this.share.topRule.parse(lex.next());
        if (!lex.isOperator(this.array.getEndOperator())) {
            throw new EvalException(1001, new String[]{this.array.getEndOperator()}, lex);
        }
        lex.next();
        x = Col2Expression.create(this.newExpression(ope, lex.getShare()), lex.getString(), pos, x, y);
        return x;
    }

    protected AbstractExpression parseField(Lex lex, AbstractExpression x, String ope, int pos) {
        AbstractExpression y = this.nextRule.parse(lex.next());
        x = Col2Expression.create(this.newExpression(ope, lex.getShare()), lex.getString(), pos, x, y);
        return x;
    }
}

