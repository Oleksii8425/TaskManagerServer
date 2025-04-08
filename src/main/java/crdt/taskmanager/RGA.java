package crdt.taskmanager;

import java.io.Serializable;
import java.util.*;

public class RGA<T> implements Iterable<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private long[] vectorClock;

    private Queue<Operation<T>> opQueue;

    private RGANode<T> head;
    private CustomHashtable<S4Vector, RGANode<T>> RGA;
    private int size = 0;

    public RGA() {
        vectorClock = new long[] { 0L, 0L, 0L };
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
        System.out.println(vectorClock);
        vectorClock[pos]--;
    }

    public RGANode<T> getHead() {
        return this.head;
    }

    public Queue<Operation<T>> getOpQueue() {
        return this.opQueue;
    }

    public CustomHashtable<S4Vector, RGANode<T>> getRGA() {
        return this.RGA;
    }

    public void enqueueOperation(Operation<T> op) {
        opQueue.add(op);
    }

    public void dequeueOperation(Operation<T> op) {
        opQueue.remove(op);
    }

    // return a causally ready operation
    public Operation<T> getOperation() {
        for (Operation<T> op : opQueue) {
            if (op.sessionN < Server.sessionN) {
                return op;
            }
            if (op.sessionN == Server.sessionN &&
                    op.vectorClock[op.siteN] == (vectorClock[op.siteN] + 1)) {
                boolean ready = true;

                for (int k = 0; k < Server.N; k++) {
                    if (k == op.siteN)
                        continue;

                    if (op.vectorClock[k] > vectorClock[k]) {
                        ready = false;
                        break;
                    }
                }

                if (ready)
                    return op;
            }
        }

        return null;
    }

    public RGANode<T> findList(int i) {
        RGANode<T> n = head;
        int k = 0;

        while (n != null) {
            if (n.value != null)
                if (i == ++k)
                    return n;
            n = n.link;
        }

        return null;
    }

    // remote operations
    public boolean insert(S4Vector i, S4Vector so, T value) {
        System.out.println("remote insert");

        RGANode<T> ins = null;
        RGANode<T> ref = null;

        if (i != null) {
            ref = RGA.get(i);
            while (ref != null && !ref.sk.equals(i))
                ref = ref.next;
            if (ref == null) {
                System.out.println("NoSuchElementException");
                throw new NoSuchElementException("insert");
            }
        }

        ins = new RGANode<T>();
        ins.sk = so;
        ins.sp = so;
        ins.value = value;
        ins.next = RGA.get(so);
        RGA.put(so, ins);
        size++;

        if (i == null) {
            if (head == null || head.sk.precedes(ins.sk)) {
                if (head != null)
                    ins.link = head;
                head = ins;

                Server.save();

                return true;
            } else
                ref = head;
        }

        while (ref.link != null && ins.sk.precedes(ref.link.sk))
            ref = ref.link;

        ins.link = ref.link;
        ref.link = ins;

        Server.save();

        return true;
    }

    public boolean delete(S4Vector i, S4Vector so) {
        System.out.println("remote delete");

        RGANode<T> n = RGA.get(i);

        while (n != null && !n.sk.equals(i))
            n = n.next;

        if (n == null)
            throw new NoSuchElementException("delete");

        if (n.value != null) {
            n.value = null;
            n.sp = so;
        }

        Server.save();

        return true;
    }

    public boolean update(S4Vector i, S4Vector so, T value) {
        System.out.println("remote update");

        RGANode<T> n = RGA.get(i);

        while (n != null && !n.sk.equals(i))
            n = n.next;

        if (n == null)
            throw new NoSuchElementException("update");

        if (n.value == null)
            return false;

        if (so.precedes(n.sp))
            return false;

        n.value = value;
        n.sp = so;

        Server.save();

        return true;
    }

    public T get(String title) {
        System.out.println("head: " + head);

        for (T item : this) {
            if (item instanceof Titleable &&
                    ((Titleable) item).getTitle().equals(title)) {
                return item;
            }
        }
        return null;
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

    @SuppressWarnings("unchecked")
    public int indexOf(Object element) {
        if (head == null || element == null)
            return -1;

        RGANode<T> castedElement = (RGANode<T>) element;

        RGANode<T> current = head;

        for (int i = 0; i < size; i++) {
            if (current != null && castedElement.value.equals(current.value)) {
                return i;
            }

            current = current.link;
        }

        return -1;
    }

    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        RGANode<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.link;
        }

        return current.value;
    }

    public int size() {
        return this.size;
    }

    @Override
    public Iterator<T> iterator() {
        return new RGAIterator<>(this);
    }
}
