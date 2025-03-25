package crdt.taskmanager;

import java.io.Serializable;
import java.util.*;

public class RGA<T> implements Serializable, Iterable<T> {
    private static final long serialVersionUID = 1L;
    private Vector<Long> vectorClock;

    RGANode<T> head;
    CustomHashtable<S4Vector, RGANode<T>> RGA;
    private int size = 0;

    public RGA() {
        vectorClock = new Vector<>(Arrays.asList(0L, 0L, 0L));
        head = null;
        RGA = new CustomHashtable<>();
    }

    public Vector<Long> getVectorClock() {
        return vectorClock;
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

    public RGANode<T> findList(int i) {
        RGANode<T> n = head;
        if (i == 0)
            return n;
        int k = 0;
        while (n != null) {
            if (n.value != null)
                if (i == ++k)
                    return n;
            n = n.link;
        }
        return null;
    }

    // local operations
    public RGANode<T> insert(int i, S4Vector so, T c) throws NoSuchElementException {
        System.out.println("local insert");
        if (i == 0) {
            head = new RGANode<T>();
            head.value = c;
            head.sk = so;
            head.sp = so;
            RGA.put(so, head);

            return null;
        }

        RGANode<T> referNode = findList(i);
        if (referNode == null)
            throw new NoSuchElementException();

        RGANode<T> newNode = new RGANode<T>();
        newNode.value = c;
        newNode.sk = so;
        newNode.sp = so;
        referNode.link = newNode;
        referNode.next = newNode;
        RGA.put(so, newNode);

        return referNode;
    }

    public RGANode<T> delete(int i) {
        System.out.println("local delete");
        RGANode<T> targetNode = findList(i);
        if (targetNode != null)
            targetNode.value = null;

        return targetNode;
    }

    public RGANode<T> update(int i, T c) {
        System.out.println("local update");
        RGANode<T> targetNode = findList(i);
        if (targetNode != null)
            targetNode.value = c;

        return targetNode;
    }

    public Object read(int i) {
        RGANode<T> targetNode = findList(i);
        if (targetNode == null)
            return null;

        return targetNode.value;
    }

    // remote operations
    public boolean insert(S4Vector i, S4Vector so, T c) {
        System.out.println("remote insert");
        RGANode<T> ins = null;
        RGANode<T> ref = null;

        if (i != null) {
            ref = RGA.get(i);
            System.out.println(ref.sk.equals(i));
            while (ref != null && !ref.sk.equals(i))
                ref = ref.next;
            if (ref == null)
                throw new NoSuchElementException();
        }

        ins = new RGANode<T>();
        ins.sk = so;
        ins.sp = so;
        ins.value = c;
        ins.next = RGA.get(so);
        RGA.put(so, ins);

        if (i == null) {
            if (head == null || head.sk.precedes(ins.sk)) {
                if (head != null)
                    ins.link = head;
                head = ins;

                return true;
            } else
                ref = head;
        }

        while (ref.link != null && ins.sk.precedes(ref.link.sk))
            ref = ref.link;
        ins.link = ref.link;
        ref.link = ins;

        return true;
    }

    public boolean delete(S4Vector i, S4Vector so) {
        System.out.println("remote delete");
        RGANode<T> n = RGA.get(i);
        while (n != null && !n.sk.equals(i))
            n = n.next;
        if (n == null)
            throw new NoSuchElementException();
        if (n.value != null) {
            n.value = null;
            n.sp = so;
        }

        return true;
    }

    public boolean update(S4Vector i, S4Vector so, T c) {
        System.out.println("remote update");
        RGANode<T> n = RGA.get(i);
        while (n != null && !n.sk.equals(i))
            n = n.next;
        if (n == null)
            throw new NoSuchElementException();
        if (n.value == null)
            return false;
        if (so.precedes(n.sp))
            return false;
        n.value = c;
        n.sp = so;

        return true;
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
