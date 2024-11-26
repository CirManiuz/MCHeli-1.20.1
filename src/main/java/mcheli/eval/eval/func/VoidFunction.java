/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.func;

import mcheli.eval.eval.func.Function;

public class VoidFunction
implements Function {
    @Override
    public long evalLong(Object object, String name, Long[] args) throws Throwable {
        System.out.println(object + "." + name + "\u95a2\u6570\u304c\u547c\u3070\u308c\u305f(long)");
        for (int i = 0; i < args.length; ++i) {
            System.out.println("arg[" + i + "] " + args[i]);
        }
        return 0L;
    }

    @Override
    public double evalDouble(Object object, String name, Double[] args) throws Throwable {
        System.out.println(object + "." + name + "\u95a2\u6570\u304c\u547c\u3070\u308c\u305f(double)");
        for (int i = 0; i < args.length; ++i) {
            System.out.println("arg[" + i + "] " + args[i]);
        }
        return 0.0;
    }

    @Override
    public Object evalObject(Object object, String name, Object[] args) throws Throwable {
        System.out.println(object + "." + name + "\u95a2\u6570\u304c\u547c\u3070\u308c\u305f(Object)");
        for (int i = 0; i < args.length; ++i) {
            System.out.println("arg[" + i + "] " + args[i]);
        }
        return null;
    }
}

