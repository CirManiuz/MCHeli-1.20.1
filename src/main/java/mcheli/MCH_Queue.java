/*
 * Decompiled with CFR 0.152.
 */
package mcheli;

import java.util.ArrayList;
import java.util.List;

public class MCH_Queue<T> {
    private int current;
    private List<T> list;

    public MCH_Queue(int filterLength, T initVal) {
        if (filterLength <= 0) {
            filterLength = 1;
        }
        this.list = new ArrayList<T>();
        for (int i = 0; i < filterLength; ++i) {
            this.list.add(initVal);
        }
        this.current = 0;
    }

    public void clear(T clearVal) {
        for (int i = 0; i < this.size(); ++i) {
            this.list.set(i, clearVal);
        }
    }

    public void put(T t) {
        this.list.set(this.current, t);
        ++this.current;
        this.current %= this.size();
    }

    private int getIndex(int offset) {
        int index = this.current + (offset %= this.size());
        if (index < 0) {
            return index + this.size();
        }
        return index % this.size();
    }

    public T oldest() {
        return this.list.get(this.getIndex(1));
    }

    public T get(int i) {
        return this.list.get(i);
    }

    public int size() {
        return this.list.size();
    }
}

