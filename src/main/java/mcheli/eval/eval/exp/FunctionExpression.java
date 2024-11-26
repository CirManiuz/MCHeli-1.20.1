/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import java.util.ArrayList;
import java.util.List;
import mcheli.eval.eval.EvalException;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col1Expression;
import mcheli.eval.eval.exp.FieldExpression;
import mcheli.eval.eval.exp.ShareExpValue;
import mcheli.eval.eval.exp.VariableExpression;

public class FunctionExpression
extends Col1Expression {
    protected AbstractExpression target;
    String name;

    public static AbstractExpression create(AbstractExpression x, AbstractExpression args, int prio, ShareExpValue share) {
        AbstractExpression obj;
        if (x instanceof VariableExpression) {
            obj = null;
        } else if (x instanceof FieldExpression) {
            FieldExpression f = (FieldExpression)x;
            obj = f.expl;
            x = f.expr;
        } else {
            throw new EvalException(1101, x.toString(), x.string, x.pos, null);
        }
        String name = x.getWord();
        FunctionExpression f = new FunctionExpression(obj, name);
        f.setExpression(args);
        f.setPos(x.string, x.pos);
        f.setPriority(prio);
        f.share = share;
        return f;
    }

    public FunctionExpression() {
        this.setOperator("(");
        this.setEndOperator(")");
    }

    public FunctionExpression(AbstractExpression obj, String word) {
        this();
        this.target = obj;
        this.name = word;
    }

    protected FunctionExpression(FunctionExpression from, ShareExpValue s) {
        super(from, s);
        if (from.target != null) {
            this.target = from.target.dup(s);
        }
        this.name = from.name;
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new FunctionExpression(this, s);
    }

    @Override
    public long evalLong() {
        Object obj = null;
        if (this.target != null) {
            obj = this.target.getVariable();
        }
        List<Long> args = this.evalArgsLong();
        try {
            Long[] arr = new Long[args.size()];
            return this.share.func.evalLong(obj, this.name, args.toArray(arr));
        }
        catch (EvalException e) {
            throw e;
        }
        catch (Throwable e) {
            throw new EvalException(2401, this.name, this.string, this.pos, e);
        }
    }

    @Override
    public double evalDouble() {
        Object obj = null;
        if (this.target != null) {
            obj = this.target.getVariable();
        }
        List<Double> args = this.evalArgsDouble();
        try {
            Double[] arr = new Double[args.size()];
            return this.share.func.evalDouble(obj, this.name, args.toArray(arr));
        }
        catch (EvalException e) {
            throw e;
        }
        catch (Throwable e) {
            throw new EvalException(2401, this.name, this.string, this.pos, e);
        }
    }

    @Override
    public Object evalObject() {
        Object obj = null;
        if (this.target != null) {
            obj = this.target.getVariable();
        }
        List<Object> args = this.evalArgsObject();
        try {
            Object[] arr = new Object[args.size()];
            return this.share.func.evalObject(obj, this.name, args.toArray(arr));
        }
        catch (EvalException e) {
            throw e;
        }
        catch (Throwable e) {
            throw new EvalException(2401, this.name, this.string, this.pos, e);
        }
    }

    private List<Long> evalArgsLong() {
        ArrayList<Long> args = new ArrayList<Long>();
        if (this.exp != null) {
            this.exp.evalArgsLong(args);
        }
        return args;
    }

    private List<Double> evalArgsDouble() {
        ArrayList<Double> args = new ArrayList<Double>();
        if (this.exp != null) {
            this.exp.evalArgsDouble(args);
        }
        return args;
    }

    private List<Object> evalArgsObject() {
        ArrayList<Object> args = new ArrayList<Object>();
        if (this.exp != null) {
            this.exp.evalArgsObject(args);
        }
        return args;
    }

    @Override
    protected Object getVariable() {
        return this.evalObject();
    }

    @Override
    protected long operateLong(long val) {
        throw new RuntimeException("\u3053\u306e\u95a2\u6570\u304c\u547c\u3070\u308c\u3066\u306f\u3044\u3051\u306a\u3044\u3002\u30b5\u30d6\u30af\u30e9\u30b9\u3067\u5b9f\u88c5\u8981");
    }

    @Override
    protected double operateDouble(double val) {
        throw new RuntimeException("\u3053\u306e\u95a2\u6570\u304c\u547c\u3070\u308c\u3066\u306f\u3044\u3051\u306a\u3044\u3002\u30b5\u30d6\u30af\u30e9\u30b9\u3067\u5b9f\u88c5\u8981");
    }

    @Override
    protected void search() {
        this.share.srch.search(this);
        if (this.share.srch.end()) {
            return;
        }
        if (this.share.srch.searchFunc_begin(this)) {
            return;
        }
        if (this.share.srch.end()) {
            return;
        }
        if (this.target != null) {
            this.target.search();
            if (this.share.srch.end()) {
                return;
            }
        }
        if (this.share.srch.searchFunc_2(this)) {
            return;
        }
        if (this.share.srch.end()) {
            return;
        }
        if (this.exp != null) {
            this.exp.search();
            if (this.share.srch.end()) {
                return;
            }
        }
        this.share.srch.searchFunc_end(this);
    }

    @Override
    protected AbstractExpression replace() {
        if (this.target != null) {
            this.target = this.target.replace();
        }
        if (this.exp != null) {
            this.exp = this.exp.replace();
        }
        return this.share.repl.replaceFunc(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FunctionExpression) {
            FunctionExpression e = (FunctionExpression)obj;
            return this.name.equals(e.name) && FunctionExpression.equals(this.target, e.target) && FunctionExpression.equals(this.exp, e.exp);
        }
        return false;
    }

    private static boolean equals(AbstractExpression e1, AbstractExpression e2) {
        if (e1 == null) {
            return e2 == null;
        }
        if (e2 == null) {
            return false;
        }
        return e1.equals(e2);
    }

    @Override
    public int hashCode() {
        int t = this.target != null ? this.target.hashCode() : 0;
        int a = this.exp != null ? this.exp.hashCode() : 0;
        return this.name.hashCode() ^ t ^ a * 2;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (this.target != null) {
            sb.append(this.target.toString());
            sb.append('.');
        }
        sb.append(this.name);
        sb.append('(');
        if (this.exp != null) {
            sb.append(this.exp.toString());
        }
        sb.append(')');
        return sb.toString();
    }
}

