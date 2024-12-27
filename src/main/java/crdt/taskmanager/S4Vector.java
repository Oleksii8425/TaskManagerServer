package crdt.taskmanager;

import java.util.Arrays;
import java.util.Objects;

public class S4Vector {
    int ssn; // Global session number
    int sid; // Unique site ID
    int sum; // Cumulative sum of the site’s vector clock
    int seq; // Sequence number at the originating site=

    public S4Vector(int ssn, int sid, int sum, int seq) {
        this.ssn = ssn;
        this.sid = sid;
        this.sum = sum;
        this.seq = seq;
    }

    public S4Vector(String data) {
        String[] newData = data.replaceAll("[a-zA-Z]{3}:", "").split(" ");
        System.out.println(Arrays.toString(newData));
        ssn = Integer.parseInt(newData[0]);
        sid = Integer.parseInt(newData[1]);
        sum = Integer.parseInt(newData[2]);
        seq = Integer.parseInt(newData[3]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        S4Vector s4Vector = (S4Vector) o;
        return ssn == s4Vector.ssn && sid == s4Vector.sid &&
                sum == s4Vector.sum && seq == s4Vector.seq;
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
