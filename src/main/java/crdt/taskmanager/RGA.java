package crdt.taskmanager;

import java.io.Serializable;
import java.util.*;

public class RGA<T> implements Serializable, Iterable<T> {
    private static final long serialVersionUID = 1L;

    private long[] vectorClock;
    private Queue<Operation<T>> opQueue;

    RGANode<T> head;
    CustomHashtable<S4Vector, RGANode<T>> RGA;
    private int size = 0;

    public RGA() {
        vectorClock = new long[] {0L, 0L, 0L};
        opQueue = new LinkedList<>();
        head = null;
        RGA = new CustomHashtable<>();
    }

    public long[] getVectorClock() {
        return this.vectorClock;
    }

    public void setVectorClock(long[] vectorClock) {
        this.vectorClock = vectorClock;
    }

    public void updateVectorClock(int pos, long val) {
        vectorClock[pos] = val;
    }

    public void increaseVectorClock(int pos) {
        vectorClock[pos]++;
    }

    public void decreaseVectorClock(int pos) {
        vectorClock[pos]--;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        RGANode<T> current = head;

        while (current != null) {
            if (current.value != null) {
                result.append(current.value);
            }
            current = current.link;
        }

        return result.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new RGAIterator<T>(this);
    }

    public ArrayList<T> toList() {
        ArrayList<T> res = new ArrayList<T>();

        for (T val : this) {
            res.add(val);
        }

        return res;
    }

    public T get(int index) throws IndexOutOfBoundsException {
        int i = 0;

        for (T val : this) {
            if (i == index)
                return val;
            i++;
        }

        throw new IndexOutOfBoundsException();
    }

    public int indexOf(T element) throws NoSuchElementException {
        int i = 0;

        for (T val : this) {
            if (val == element)
                return i;
            i++;
        }

        throw new NoSuchElementException();
    }
}
