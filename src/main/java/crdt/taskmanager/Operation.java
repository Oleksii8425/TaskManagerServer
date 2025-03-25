package crdt.taskmanager;

import java.io.Serializable;
import java.util.Vector;

public class Operation<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    OperationType type;
    T element;
    Long sessionN;
    int siteN;
    Vector<Long> vectorClock;
    String boardTitle;
    String taskTitle;

    public Operation(OperationType type, T element, Long sessionN, int siteN,
    Vector<Long> vectorClock, String boardTitle, String taskTitle) {
        this.type = type;
        this.element = element;
        this.sessionN = sessionN;
        this.siteN = siteN;
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
        + ", sessionN=" + sessionN + ", siteN=" + siteN + ", vectorClock="
        + vectorClock + ", boardTitle=" + boardTitle + ", taskTitle=" + taskTitle + "]";
    }
}