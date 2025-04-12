package crdt.taskmanager;

import java.io.Serializable;

public class Operation<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    OperationType type;
    OperationTarget target;
    long sessionN;
    int siteN;
    S4Vector i;
    long[] vectorClock;
    String boardTitle;
    String taskTitle;
    Character character;

    public Operation(OperationType type, OperationTarget target, Long sessionN, int siteN,
            S4Vector i, long[] vectorClock, String boardTitle, String taskTitle, Character character) {
        this.type = type;
        this.target = target;
        this.sessionN = sessionN;
        this.siteN = siteN;
        this.i = i;
        this.vectorClock = vectorClock.clone();
        this.boardTitle = boardTitle;
        this.taskTitle = taskTitle;
        this.character = character;
    }

    public OperationTarget getTarget() {
        return this.target;
    }

    public String getBoardTitle() {
        return this.boardTitle;
    }

    public String getTaskTitle() {
        return this.taskTitle;
    }

    public Character getCharacter() {
        return this.character;
    }

    @Override
    public String toString() {
        return "Operation [type=" + type + ", target=" + target
                + ", siteN=" + siteN + ", i=" + i + ", vectorClock="
                + vectorClock[0] + " " + vectorClock[1] + " " + vectorClock[2]
                + " " + ", boardTitle=" + boardTitle + ", taskTitle=" + taskTitle
                + ", charcter=" + character + "]";
    }
}