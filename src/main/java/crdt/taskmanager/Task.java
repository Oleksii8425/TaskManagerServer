package crdt.taskmanager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private Vector<Long> vectorClock;
    private Queue<Operation> opQueue;
    private String title;
    private RGA content;

    @JsonCreator
    public Task(@JsonProperty("title") String title,
                @JsonProperty("content") RGA content) {
        opQueue = new LinkedList<>();
        this.title = title;
        this.content = content;
    }

    public void enqueueOperation(Operation op) {
        opQueue.add(op);
    }

    public void dequeueOperation(Operation op) {
        opQueue.remove(op);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RGA getContent() {
        return content;
    }

    public Vector<Long> getVectorClock() {
        return vectorClock;
    }

    public void setVectorClock(Vector<Long> vectorClock) {
        this.vectorClock = vectorClock;
    }

    public void updateVectorClock(int pos, Long val) {
        vectorClock.set(pos, val);
    }

    public void increaseVectorClock(int pos) {
        long c = vectorClock.get(pos);
        updateVectorClock(pos, c + 1);
    }

    public void decreaseVectorClock(int pos) {
        long c = vectorClock.get(pos);
        updateVectorClock(pos, c - 1);
    }
}