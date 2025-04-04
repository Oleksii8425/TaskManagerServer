package crdt.taskmanager;

import java.io.Serializable;
import java.util.Vector;

public class Operation<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    OperationType type;
    T element;
    long sessionN;
    int siteN;
    S4Vector i;
    long[] vectorClock;
    String boardTitle;
    String taskTitle;

    public Operation(OperationType type, T element, Long sessionN, int siteN,
            S4Vector i, long[] vectorClock, String boardTitle, String taskTitle) {
        this.type = type;
        this.element = element;
        this.sessionN = sessionN;
        this.siteN = siteN;
        this.i = i;
        this.vectorClock = vectorClock;
        this.boardTitle = boardTitle;
        this.taskTitle = taskTitle;
    }

    public T getElement() {
        return this.element;
    }

    @Override
    public String toString() {
        return "Operation [type=" + type + ", element=" + element.toString()
                + ", siteN=" + siteN + ", i=" + i + ", vectorClock=" + vectorClock +
                ", boardTitle=" + boardTitle + ", taskTitle=" + taskTitle + "]";
    }
}