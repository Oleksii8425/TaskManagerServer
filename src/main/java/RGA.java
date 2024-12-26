import java.io.Serializable;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RGA implements Serializable {
    RGANode head;
    Hashtable<S4Vector, RGANode> RGA;
    S4Vector s0;

    public RGA() {
        head = null;
        RGA = new Hashtable<>();
    }

    @JsonCreator
    public RGA(
            @JsonProperty("head") RGANode head,
            @JsonProperty("RGA") Hashtable<S4Vector, RGANode> RGA,
            @JsonProperty("s0") S4Vector s0
    ) {
        this.head = head;
        this.RGA = RGA != null ? RGA : new Hashtable<>();
        this.s0 = s0;
    }

    // local operations
    public RGANode findList(int i) {
        RGANode n = head;
        int k = 0;
        while (n != null) {
            if (n.value != null)
                if (i == ++k) return n;
            n = n.link;
        }
        return null;
    }

    public RGANode findLink(RGANode n) {
        if (n.value == null) return null;
        else return n;
    }

    public boolean insert(int i, Character c) {
        RGANode referNode = findList(i);
        if (referNode == null) return false;
        RGANode newNode = new RGANode();
        newNode.value = c;
        referNode.next = newNode;
        return true;
    }

    public boolean delete(int i) {
        RGANode targetNode = findList(i);
        if (targetNode == null) return false;
        targetNode.value = null;
        return true;
    }

    public boolean update(int i, Character c) {
        RGANode targetNode = findList(i);
        if (targetNode == null) return false;
        targetNode.value = c;
        return true;
    }

    public Character read (int i) {
        RGANode targetNode = findList(i);
        if (targetNode == null) return null;
        return targetNode.value;
    }

    //remote operations
    public boolean insert(S4Vector i, Character c) {
        RGANode ins;
        RGANode ref;

        if (i != null) {
            ref = RGA.get(i);
            while (ref != null && ref.sk != i) ref = ref.next;
            if (ref == null) throw new NoSuchElementException();
        }

        ins = new RGANode();
        //ins.sk = s0;
        //ins.sp = s0;
        ins.value = c;
        //ins.next = RGA.get(s0);
        //RGA.put(s0, ins);

        if (i == null) {
            if (head == null || head.sk.precedes(ins.sk)) {
                if (head != null) ins.link = head;
                head = ins;
                return true;
            } else ref = head;

            while (ref.link != null && ins.sk.precedes(ref.link.sk))
                ref = ref.link;
            ins.link = ref.link;
            ref.link = ins;
            return true;
        }

        return true;
    }

    public boolean delete(S4Vector i) {
        RGANode n = RGA.get(i);
        while (n != null && n.sk != i) n = n.next;
        if (n == null) throw new NoSuchElementException();
        if (n .value != null) {
            n.value = null;
            //n.sp = s0;
        }
        return true;
    }

    public boolean update(S4Vector i, Character c) {
        RGANode n = RGA.get(i);
        while (n != null && n.sk != i) n = n.next;
        if (n == null) throw new NoSuchElementException();
        if (n.value == null) return false;
        //if (s0.precedes(n.sp)) return false;
        n.value = c;
        //n.sp = s0;
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
