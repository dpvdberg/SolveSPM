package datastructure;

import java.util.ArrayList;
import java.util.Comparator;

public class MaxHeap<T extends Comparable<T>> extends Heap<T> {
    private final Comparator<T> comparator;

    public MaxHeap(final int size) {
        super(new ArrayList<T>(size));
        comparator = null;
    }

    public MaxHeap(final int size, final Comparator<T> comparator) {
        super(new ArrayList<T>(size));
        this.comparator = comparator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfied(final T a, final T b) {
        if (comparator != null) {
            return comparator.compare(a, b) >= 0;
        }
        return a.compareTo(b) >= 0;
    }
}