package crdt.taskmanager;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class S4Vector implements Serializable, Comparable<S4Vector> {
    private static final long serialVersionUID = 1L;

    long ssn; // Global session number
    int sid; // Unique site ID
    long sum; // Cumulative sum of the site’s vector clock
    long seq; // Sequence number at the originating site

    public S4Vector(long ssn, int sid, long sum, long seq) {
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
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        S4Vector s4Vector = (S4Vector) other;

        return ssn == s4Vector.ssn &&
                sid == s4Vector.sid &&
                sum == s4Vector.sum &&
                seq == s4Vector.seq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ssn, sid, sum, seq);
    }

    @Override
    public String toString() {
        return "ssn: " + ssn + ", sid: " + sid + ", sum: " + sum + ", seq: " + seq;
    }

    public boolean precedes(S4Vector other) {
        return compareTo(other) < 0;
    }

    @Override
    public int compareTo(S4Vector other) {
        if (ssn != other.ssn) {
            return (int) (ssn - other.ssn); // Lower ssn comes first
        }

        if (sum != other.sum) {
            return (int) (sum - other.sum); // Lower sum comes first
        }

        return Integer.compare(sid, other.sid); // Lower sid comes first
    }
}