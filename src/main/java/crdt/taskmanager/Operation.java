package crdt.taskmanager;

import java.io.Serializable;
import java.util.Vector;

public class Operation<T> implements Serializable {
    String type;
    T element;
    Long sessionN;
    int siteN;
    Vector<Long> vectorClock;
    String board;
    int task;

    public Operation(String type, T element, Long sessionN, int siteN, Vector<Long> vectorClock, String board,
            int task) {
        this.type = type;
        this.element = element;
        this.sessionN = sessionN;
        this.siteN = siteN;
        this.vectorClock = vectorClock;
        this.board = board;
        this.task = task;
    }

    @Override
    public String toString() {
        return "Operation [type=" + type + ", element=" + element.toString() + ", sessionN=" + sessionN + ", siteN=" + siteN
                + ", vectorClock=" + vectorClock + ", board=" + board + ", task=" + task + "]";
    }
}