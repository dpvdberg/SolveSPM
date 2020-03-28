package datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract class Heap<T extends Comparable<T>> {
    private final ArrayList<T> A;
    private int nextSlot;
    private Map<T, Integer> itemIndex = new HashMap<>();

    public abstract boolean isSatisfied(T a, T b);

    public Heap(final ArrayList<T> A) {
        this.A = A;
        this.nextSlot = 0;
    }

    public int left(final int i) {
        return 2 * i + 1;
    }

    public int right(final int i) {
        return 2 * i + 2;
    }

    public int parent(final int i) {
        return i > 0 ? (i - 1) / 2 : -1;
    }

    public int size() {
        return nextSlot;
    }

    public boolean isEmpty() {
        return nextSlot <= 0;
    }

    public boolean contains(T object) {
        return itemIndex.containsKey(object);
    }

    private void swap(final int i, final int j) {
        if (i != j) {
            final T oldi = A.get(i);
            final T oldj = A.get(j);

            A.set(i, oldj);
            itemIndex.put(oldj, i);
            A.set(j, oldi);
            itemIndex.put(oldi, j);
        }
    }

    public void heapify(T object) {
        heapify(itemIndex.get(object));
    }

    public void heapify(final int index) {
        if (index > nextSlot) {
            return;
        }
        final int p = parent(index);
        while (p >= 0 && !isSatisfied(A.get(p), A.get(index))) {
            swap(p, index);
            heapify(p);
        }
    }

    public void insert(final T key) {
        A.add(nextSlot, key);
        itemIndex.put(key, nextSlot);

        heapify(nextSlot);
        nextSlot++;
    }

    public T extract() {
        if (nextSlot > 0) {
            final T removed = A.get(0);
            final T newRoot = A.get(nextSlot - 1);

            A.set(0, newRoot);
            itemIndex.put(newRoot, 0);

            A.remove(nextSlot - 1);
            itemIndex.remove(removed);

            nextSlot--;
            heapify(0);

            return removed;
        }
        return null;
    }

    public T top() {
        if (nextSlot > 0) {
            return A.get(0);
        } else {
            return null;
        }
    }
}
