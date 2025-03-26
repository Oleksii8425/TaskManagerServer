package crdt.taskmanager;

import java.io.Serializable;
import java.util.*;

public class RGA<T> implements Serializable, Iterable<T> {
    private static final long serialVersionUID = 1L;

    private Vector<Long> vectorClock;
    private Queue<Operation<T>> opQueue;

    RGANode<T> head;
    CustomHashtable<S4Vector, RGANode<T>> RGA;
    private int size = 0;

    public RGA() {
        vectorClock = new Vector<>(Arrays.asList(0L, 0L, 0L));
        opQueue = new LinkedList<>();
        head = null;
        RGA = new CustomHashtable<>();
    }

    public Vector<Long> getVectorClock() {
        return this.vectorClock;
    }

    public void setVectorClock(Vector<Long> vectorClock) {
        this.vectorClock = vectorClock;
    }

    public void updateVectorClock(int pos, Long val) {
        vectorClock.set(pos, val);
    }

    public void increaseVectorClock(int pos) {
        long c = vectorClock.get(pos);
        updateVectorClock(pos, c + 1);
    }

    public void decreaseVectorClock(int pos) {
        long c = vectorClock.get(pos);
        updateVectorClock(pos, c - 1);
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
