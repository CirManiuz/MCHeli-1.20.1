/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.rule;

import mcheli.eval.eval.EvalException;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.CharExpression;
import mcheli.eval.eval.exp.Col1Expression;
import mcheli.eval.eval.exp.NumberExpression;
import mcheli.eval.eval.exp.StringExpression;
import mcheli.eval.eval.exp.VariableExpression;
import mcheli.eval.eval.lex.Lex;
import mcheli.eval.eval.rule.AbstractRule;
import mcheli.eval.eval.rule.ShareRuleValue;

public class PrimaryRule
extends AbstractRule {
    public PrimaryRule(ShareRuleValue share) {
        super(share);
    }

    @Override
    public final AbstractExpression parse(Lex lex) {
        switch (lex.getType()) {
            case 0x7FFFFFF1: {
                AbstractExpression n = NumberExpression.create(lex, this.prio);
                lex.next();
                return n;
            }
            case 0x7FFFFFF0: {
                AbstractExpression w = VariableExpression.create(lex, this.prio);
                lex.next();
                return w;
            }
            case 0x7FFFFFF3: {
                AbstractExpression s = StringExpression.create(lex, this.prio);
                lex.next();
                return s;
            }
            case 0x7FFFFFF4: {
                AbstractExpression c = CharExpression.create(lex, this.prio);
                lex.next();
                return c;
            }
            case 0x7FFFFFF2: {
                String ope = lex.getOperator();
                int pos = lex.getPos();
                if (this.isMyOperator(ope)) {
                    if (ope.equals(this.share.paren.getOperator())) {
                        return this.parseParen(lex, ope, pos);
                    }
                    return Col1Expression.create(this.newExpression(ope, lex.getShare()), lex.getString(), pos, this.parse(lex.next()));
                }
                throw new EvalException(1002, lex);
            }
            case 0x7FFFFFFF: {
                throw new EvalException(1004, lex);
            }
        }
        throw new EvalException(1003, lex);
    }

    protected AbstractExpression parseParen(Lex lex, String ope, int pos) {
        AbstractExpression s = this.share.topRule.parse(lex.next());
        if (!lex.isOperator(this.share.paren.getEndOperator())) {
            throw new EvalException(1001, new String[]{this.share.paren.getEndOperator()}, lex);
        }
        lex.next();
        return Col1Expression.create(this.newExpression(ope, lex.getShare()), lex.getString(), pos, s);
    }
}

