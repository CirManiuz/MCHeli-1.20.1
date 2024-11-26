/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.ParenExpression;
import mcheli.eval.eval.exp.ShareExpValue;
import mcheli.eval.eval.lex.Lex;
import mcheli.eval.eval.rule.ShareRuleValue;

public abstract class AbstractRule {
    public AbstractRule nextRule;
    protected ShareRuleValue share;
    private final Map<String, AbstractExpression> opes = new HashMap<String, AbstractExpression>();
    public int prio;

    public AbstractRule(ShareRuleValue share) {
        this.share = share;
    }

    public final void addExpression(AbstractExpression exp) {
        if (exp == null) {
            return;
        }
        String ope = exp.getOperator();
        this.addOperator(ope, exp);
        this.addLexOperator(exp.getEndOperator());
        if (exp instanceof ParenExpression) {
            this.share.paren = exp;
        }
    }

    public final void addOperator(String ope, AbstractExpression exp) {
        this.opes.put(ope, exp);
        this.addLexOperator(ope);
    }

    public final String[] getOperators() {
        ArrayList<String> list = new ArrayList<String>();
        Iterator<String> i = this.opes.keySet().iterator();
        while (i.hasNext()) {
            list.add(i.next());
        }
        return list.toArray(new String[list.size()]);
    }

    public final void addLexOperator(String ope) {
        if (ope == null) {
            return;
        }
        int n = ope.length() - 1;
        if (this.share.opeList[n] == null) {
            this.share.opeList[n] = new ArrayList<String>();
        }
        this.share.opeList[n].add(ope);
    }

    protected final boolean isMyOperator(String ope) {
        return this.opes.containsKey(ope);
    }

    protected final AbstractExpression newExpression(String ope, ShareExpValue share) {
        try {
            AbstractExpression org = this.opes.get(ope);
            AbstractExpression n = org.dup(share);
            n.setPriority(this.prio);
            n.share = share;
            return n;
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void initPriority(int prio) {
        this.prio = prio;
        if (this.nextRule != null) {
            this.nextRule.initPriority(prio + 1);
        }
    }

    protected abstract AbstractExpression parse(Lex var1);
}

