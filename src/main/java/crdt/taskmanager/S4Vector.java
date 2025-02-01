package crdt.taskmanager;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class S4Vector implements Serializable {
    private static final long serialVersionUID = 1L;
    Long ssn; // Global session number
    int sid; // Unique site ID
    Long sum; // Cumulative sum of the site’s vector clock
    Long seq; // Sequence number at the originating site

    public S4Vector(Long ssn, int sid, Long sum, Long seq) {
        this.ssn = ssn;
        this.sid = sid;
        this.sum = sum;
        this.seq = seq;
    }

    public S4Vector(String data) {
        String[] newData = data.replaceAll("[a-zA-Z]{3}:", "").split(" ");
        System.out.println(Arrays.toString(newData));
        ssn = Long.parseLong(newData[0]);
        sid = Integer.parseInt(newData[1]);
        sum = Long.parseLong(newData[2]);
        seq = Long.parseLong(newData[3]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        S4Vector s4Vector = (S4Vector) o;
        // System.out.println("this key" + System.identityHashCode(this.hashCode()));
        // System.out.println("i key" + System.identityHashCode(s4Vector.hashCode()));
        boolean a = ssn.longValue() == s4Vector.ssn.longValue();
        boolean b = sid == s4Vector.sid;
        boolean c = sum.longValue() == s4Vector.sum.longValue();
        boolean d = seq.longValue() == s4Vector.seq.longValue();
        boolean res = a && b && c && d;
        // return ssn == s4Vector.ssn && sid == s4Vector.sid &&
        // sum == s4Vector.sum && seq == s4Vector.seq;

        return res;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ssn, sid, sum, seq);
    }

    @Override
    public String toString() {
        return "ssn: " + ssn + ", sid: " + sid + ", sum: " + sum + ", seq: " + seq;
    }

    public boolean precedes(S4Vector s4Vector) {
        return ssn < s4Vector.ssn
                || ssn == s4Vector.ssn && sum < s4Vector.sum
                || ssn == s4Vector.ssn && sum == s4Vector.sum && sid < s4Vector.sid;
    }
}