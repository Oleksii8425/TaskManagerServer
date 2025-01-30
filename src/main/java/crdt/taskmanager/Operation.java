package crdt.taskmanager;

import java.io.Serializable;
import java.util.Vector;

public class Operation implements Serializable {
    String type;
    String character;
    Long sessionN;
    int siteN;
    Vector<Long> vectorClock;
    String board;
    int task;

    public Operation(String type, String character, Long sessionN, int siteN, Vector<Long> vectorClock, String board,
            int task) {
        this.type = type;
        this.character = character;
        this.sessionN = sessionN;
        this.siteN = siteN;
        this.vectorClock = vectorClock;
        this.board = board;
        this.task = task;
    }

    @Override
    public String toString() {
        return "Operation [type=" + type + ", character=" + character + ", sessionN=" + sessionN + ", siteN=" + siteN
                + ", vectorClock=" + vectorClock + ", board=" + board + ", task=" + task + "]";
    }
}
