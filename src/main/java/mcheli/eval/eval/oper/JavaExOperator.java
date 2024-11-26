/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.oper;

import java.math.BigDecimal;
import java.math.BigInteger;
import mcheli.eval.eval.oper.Operator;

public class JavaExOperator
implements Operator {
    boolean inLong(Object x) {
        if (x instanceof Long) {
            return true;
        }
        if (x instanceof Integer) {
            return true;
        }
        if (x instanceof Short) {
            return true;
        }
        if (x instanceof Byte) {
            return true;
        }
        if (x instanceof BigInteger) {
            return true;
        }
        return x instanceof BigDecimal;
    }

    long l(Object x) {
        return ((Number)x).longValue();
    }

    boolean inDouble(Object x) {
        if (x instanceof Double) {
            return true;
        }
        return x instanceof Float;
    }

    double d(Object x) {
        return ((Number)x).doubleValue();
    }

    Object n(long n, Object x) {
        if (x instanceof Long) {
            return new Long(n);
        }
        if (x instanceof Double) {
            return new Double(n);
        }
        if (x instanceof Integer) {
            return new Integer((int)n);
        }
        if (x instanceof Short) {
            return new Short((short)n);
        }
        if (x instanceof Byte) {
            return new Byte((byte)n);
        }
        if (x instanceof Float) {
            return new Float(n);
        }
        if (x instanceof BigInteger) {
            return BigInteger.valueOf(n);
        }
        if (x instanceof BigDecimal) {
            return BigDecimal.valueOf(n);
        }
        if (x instanceof String) {
            return String.valueOf(n);
        }
        return new Long(n);
    }

    Object n(long n, Object x, Object y) {
        if (x instanceof Byte || y instanceof Byte) {
            return new Byte((byte)n);
        }
        if (x instanceof Short || y instanceof Short) {
            return new Short((short)n);
        }
        if (x instanceof Integer || y instanceof Integer) {
            return new Integer((int)n);
        }
        if (x instanceof Long || y instanceof Long) {
            return new Long(n);
        }
        if (x instanceof BigInteger || y instanceof BigInteger) {
            return BigInteger.valueOf(n);
        }
        if (x instanceof BigDecimal || y instanceof BigDecimal) {
            return BigDecimal.valueOf(n);
        }
        if (x instanceof Float || y instanceof Float) {
            return new Float(n);
        }
        if (x instanceof Double || y instanceof Double) {
            return new Double(n);
        }
        if (x instanceof String || y instanceof String) {
            return String.valueOf(n);
        }
        return new Long(n);
    }

    Object n(double n, Object x) {
        if (x instanceof Float) {
            return new Float(n);
        }
        if (x instanceof String) {
            return String.valueOf(n);
        }
        return new Double(n);
    }

    Object n(double n, Object x, Object y) {
        if (x instanceof Float || y instanceof Float) {
            return new Float(n);
        }
        if (x instanceof Number || y instanceof Number) {
            return new Double(n);
        }
        if (x instanceof String || y instanceof String) {
            return String.valueOf(n);
        }
        return new Double(n);
    }

    Object nn(long n, Object x) {
        if (x instanceof BigDecimal) {
            return BigDecimal.valueOf(n);
        }
        if (x instanceof BigInteger) {
            return BigInteger.valueOf(n);
        }
        return new Long(n);
    }

    Object nn(long n, Object x, Object y) {
        if (x instanceof BigDecimal || y instanceof BigDecimal) {
            return BigDecimal.valueOf(n);
        }
        if (x instanceof BigInteger || y instanceof BigInteger) {
            return BigInteger.valueOf(n);
        }
        return new Long(n);
    }

    Object nn(double n, Object x) {
        if (this.inLong(x)) {
            return new Long((long)n);
        }
        return new Double(n);
    }

    Object nn(double n, Object x, Object y) {
        if (this.inLong(x) && this.inLong(y)) {
            return new Long((long)n);
        }
        return new Double(n);
    }

    RuntimeException undefined(Object x) {
        String c = null;
        if (x != null) {
            c = x.getClass().getName();
        }
        return new RuntimeException("\u672a\u5b9a\u7fa9\u5358\u9805\u6f14\u7b97\uff1a" + c);
    }

    RuntimeException undefined(Object x, Object y) {
        String c1 = null;
        String c2 = null;
        if (x != null) {
            c1 = x.getClass().getName();
        }
        if (y != null) {
            c2 = y.getClass().getName();
        }
        return new RuntimeException("\u672a\u5b9a\u7fa9\u4e8c\u9805\u6f14\u7b97\uff1a" + c1 + " , " + c2);
    }

    @Override
    public Object power(Object x, Object y) {
        if (x == null && y == null) {
            return null;
        }
        return this.nn(Math.pow(this.d(x), this.d(y)), x, y);
    }

    @Override
    public Object signPlus(Object x) {
        return x;
    }

    @Override
    public Object signMinus(Object x) {
        if (x == null) {
            return null;
        }
        if (this.inLong(x)) {
            return this.n(-this.l(x), x);
        }
        if (this.inDouble(x)) {
            return this.n(-this.d(x), x);
        }
        if (x instanceof Boolean) {
            return x;
        }
        throw this.undefined(x);
    }

    @Override
    public Object plus(Object x, Object y) {
        if (x == null && y == null) {
            return null;
        }
        if (this.inLong(x) && this.inLong(y)) {
            return this.nn(this.l(x) + this.l(y), x, y);
        }
        if (this.inDouble(x) && this.inDouble(y)) {
            return this.nn(this.d(x) + this.d(y), x, y);
        }
        if (x instanceof String || y instanceof String) {
            return String.valueOf(x) + String.valueOf(y);
        }
        if (x instanceof Character || y instanceof Character) {
            return String.valueOf(x) + String.valueOf(y);
        }
        throw this.undefined(x, y);
    }

    @Override
    public Object minus(Object x, Object y) {
        if (x == null && y == null) {
            return null;
        }
        if (this.inLong(x) && this.inLong(y)) {
            return this.nn(this.l(x) - this.l(y), x, y);
        }
        if (this.inDouble(x) && this.inDouble(y)) {
            return this.nn(this.d(x) - this.d(y), x, y);
        }
        throw this.undefined(x, y);
    }

    @Override
    public Object mult(Object x, Object y) {
        if (x == null && y == null) {
            return null;
        }
        if (this.inLong(x) && this.inLong(y)) {
            return this.nn(this.l(x) * this.l(y), x, y);
        }
        if (this.inDouble(x) && this.inDouble(y)) {
            return this.nn(this.d(x) * this.d(y), x, y);
        }
        String s = null;
        int ct = 0;
        boolean str = false;
        if (x instanceof String && y instanceof Number) {
            s = (String)x;
            ct = (int)this.l(y);
            str = true;
        } else if (y instanceof String && x instanceof Number) {
            s = (String)y;
            ct = (int)this.l(x);
            str = true;
        }
        if (str) {
            StringBuffer sb = new StringBuffer(s.length() * ct);
            for (int i = 0; i < ct; ++i) {
                sb.append(s);
            }
            return sb.toString();
        }
        throw this.undefined(x, y);
    }

    @Override
    public Object div(Object x, Object y) {
        if (x == null && y == null) {
            return null;
        }
        if (this.inLong(x) && this.inLong(y)) {
            return this.nn(this.l(x) / this.l(y), x);
        }
        if (this.inDouble(x) && this.inDouble(y)) {
            return this.nn(this.d(x) / this.d(y), x);
        }
        if (x instanceof String && y instanceof String) {
            String s = (String)x;
            String r = (String)y;
            return s.split(r);
        }
        throw this.undefined(x, y);
    }

    @Override
    public Object mod(Object x, Object y) {
        if (x == null && y == null) {
            return null;
        }
        if (this.inLong(x) && this.inLong(y)) {
            return this.nn(this.l(x) % this.l(y), x);
        }
        if (this.inDouble(x) && this.inDouble(y)) {
            return this.nn(this.d(x) % this.d(y), x);
        }
        throw this.undefined(x, y);
    }

    @Override
    public Object bitNot(Object x) {
        if (x == null) {
            return null;
        }
        if (x instanceof Number) {
            return this.n(this.l(x) ^ 0xFFFFFFFFFFFFFFFFL, x);
        }
        throw this.undefined(x);
    }

    @Override
    public Object shiftLeft(Object x, Object y) {
        if (x == null && y == null) {
            return null;
        }
        if (this.inLong(x) && this.inLong(y)) {
            return this.n(this.l(x) << (int)this.l(y), x);
        }
        if (this.inDouble(x) && this.inDouble(y)) {
            return this.n(this.d(x) * Math.pow(2.0, this.d(y)), x);
        }
        throw this.undefined(x, y);
    }

    @Override
    public Object shiftRight(Object x, Object y) {
        if (x == null && y == null) {
            return null;
        }
        if (this.inLong(x) && this.inLong(y)) {
            return this.n(this.l(x) >> (int)this.l(y), x);
        }
        if (this.inDouble(x) && this.inDouble(y)) {
            return this.n(this.d(x) / Math.pow(2.0, this.d(y)), x);
        }
        throw this.undefined(x, y);
    }

    @Override
    public Object shiftRightLogical(Object x, Object y) {
        if (x == null && y == null) {
            return null;
        }
        if (x instanceof Byte && y instanceof Number) {
            return this.n((this.l(x) & 0xFFL) >>> (int)this.l(y), x);
        }
        if (x instanceof Short && y instanceof Number) {
            return this.n((this.l(x) & 0xFFFFL) >>> (int)this.l(y), x);
        }
        if (x instanceof Integer && y instanceof Number) {
            return this.n((this.l(x) & 0xFFFFFFFFFFFFFFFFL) >>> (int)this.l(y), x);
        }
        if (this.inLong(x) && y instanceof Number) {
            return this.n(this.l(x) >>> (int)this.l(y), x);
        }
        if (this.inDouble(x) && y instanceof Number) {
            double t = this.d(x);
            if (t < 0.0) {
                t = -t;
            }
            return this.n(t / Math.pow(2.0, this.d(y)), x);
        }
        throw this.undefined(x, y);
    }

    @Override
    public Object bitAnd(Object x, Object y) {
        if (x == null && y == null) {
            return null;
        }
        if (x instanceof Number && y instanceof Number) {
            return this.n(this.l(x) & this.l(y), x);
        }
        throw this.undefined(x, y);
    }

    @Override
    public Object bitOr(Object x, Object y) {
        if (x == null && y == null) {
            return null;
        }
        if (x instanceof Number && y instanceof Number) {
            return this.n(this.l(x) | this.l(y), x);
        }
        throw this.undefined(x, y);
    }

    @Override
    public Object bitXor(Object x, Object y) {
        if (x == null && y == null) {
            return null;
        }
        if (x instanceof Number && y instanceof Number) {
            return this.n(this.l(x) ^ this.l(y), x);
        }
        throw this.undefined(x, y);
    }

    @Override
    public Object not(Object x) {
        if (x == null) {
            return null;
        }
        if (x instanceof Boolean) {
            return (Boolean)x != false ? Boolean.FALSE : Boolean.TRUE;
        }
        if (x instanceof Number) {
            if (this.l(x) == 0L) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        throw this.undefined(x);
    }

    int compare(Object x, Object y) {
        Class<?> yc;
        if (x == null && y == null) {
            return 0;
        }
        if (x == null && y != null) {
            return -1;
        }
        if (x != null && y == null) {
            return 1;
        }
        if (this.inLong(x) && this.inLong(y)) {
            long c = this.l(x) - this.l(y);
            if (c == 0L) {
                return 0;
            }
            if (c < 0L) {
                return -1;
            }
            return 1;
        }
        if (x instanceof Number && y instanceof Number) {
            double n = this.d(x) - this.d(y);
            if (n == 0.0) {
                return 0;
            }
            return n < 0.0 ? -1 : 1;
        }
        Class<?> xc = x.getClass();
        if (xc.isAssignableFrom(yc = y.getClass()) && x instanceof Comparable) {
            return ((Comparable)x).compareTo(y);
        }
        if (yc.isAssignableFrom(xc) && y instanceof Comparable) {
            return -((Comparable)y).compareTo(x);
        }
        if (x.equals(y)) {
            return 0;
        }
        throw this.undefined(x, y);
    }

    @Override
    public Object equal(Object x, Object y) {
        return this.compare(x, y) == 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public Object notEqual(Object x, Object y) {
        return this.compare(x, y) != 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public Object lessThan(Object x, Object y) {
        return this.compare(x, y) < 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public Object lessEqual(Object x, Object y) {
        return this.compare(x, y) <= 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public Object greaterThan(Object x, Object y) {
        return this.compare(x, y) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public Object greaterEqual(Object x, Object y) {
        return this.compare(x, y) >= 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public boolean bool(Object x) {
        if (x == null) {
            return false;
        }
        if (x instanceof Boolean) {
            return (Boolean)x;
        }
        if (x instanceof Number) {
            return ((Number)x).longValue() != 0L;
        }
        return Boolean.valueOf(x.toString());
    }

    @Override
    public Object inc(Object x, int inc) {
        if (x == null) {
            return null;
        }
        if (this.inLong(x)) {
            return this.n(this.l(x) + (long)inc, x);
        }
        if (this.inDouble(x)) {
            return this.n(this.d(x) + (double)inc, x);
        }
        throw this.undefined(x);
    }
}

