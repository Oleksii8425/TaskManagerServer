package crdt.taskmanager;

import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RGA implements Serializable {
    private static final long serialVersionUID = 1L;

    RGANode head;
    CustomHashtable<S4Vector, RGANode> RGA;

    public RGA() {
        head = null;
        RGA = new CustomHashtable<>();
    }

    @JsonCreator
    public RGA(
            @JsonProperty("head") RGANode head,
            @JsonProperty("RGA") CustomHashtable<S4Vector, RGANode> RGA) {
        this.head = head;
        this.RGA = RGA != null ? RGA : new CustomHashtable<>();
    }

    public RGANode findList(int i) {
        RGANode n = head;
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
    public RGANode insert(int i, S4Vector so, String c) throws NoSuchElementException {
        System.out.println("local insert");
        if (i == 0) {
            head = new RGANode();
            head.value = c;
            return null;
        }

        RGANode referNode = findList(i);
        if (referNode == null)
            throw new NoSuchElementException();

        RGANode newNode = new RGANode();
        newNode.value = c;
        newNode.sk = so;
        newNode.sp = so;
        referNode.link = newNode;
        referNode.next = newNode;
        RGA.put(so, newNode);

        return referNode;
    }

    public RGANode delete(int i) {
        System.out.println("local delete");
        RGANode targetNode = findList(i);
        if (targetNode == null)
            return null;
        targetNode.value = null;

        return targetNode;
    }

    public boolean update(int i, String c) {
        System.out.println("local update");
        RGANode targetNode = findList(i);
        if (targetNode == null)
            return false;
        targetNode.value = c;

        return true;
    }

    public String read(int i) {
        RGANode targetNode = findList(i);
        if (targetNode == null)
            return null;

        return targetNode.value;
    }

    // remote operations
    public boolean insert(S4Vector i, S4Vector so, String c) {
        System.out.println("remote insert");
        RGANode ins;
        RGANode ref;

        if (i != null) {
            ref = RGA.get(i);
            while (ref != null && ref.sk != i)
                ref = ref.next;
            if (ref == null)
                throw new NoSuchElementException();
        }

        ins = new RGANode();
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

            while (ref.link != null && ins.sk.precedes(ref.link.sk))
                ref = ref.link;
            ins.link = ref.link;
            ref.link = ins;
            return true;
        }

        return true;
    }

    public boolean delete(S4Vector i, S4Vector so) {
        System.out.println("remote delete");
        RGANode n = RGA.get(i);
        while (n != null && n.sk != i)
            n = n.next;
        if (n == null)
            throw new NoSuchElementException();
        if (n.value != null) {
            n.value = null;
            n.sp = so;
        }

        return true;
    }

    public boolean update(S4Vector i, S4Vector so, String c) {
        System.out.println("remote update");
        RGANode n = RGA.get(i);
        while (n != null && n.sk != i)
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
        RGANode current = head;
        while (current != null) {
            result.append(current.value);
            current = current.next;
        }

        return result.toString();
    }
}
