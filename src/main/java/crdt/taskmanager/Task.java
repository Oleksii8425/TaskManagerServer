package crdt.taskmanager;

import java.io.Serial;
import java.io.Serializable;
import java.util.Queue;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String title;
    private RGA content;
    private Vector<Long> vectorClock;

    @JsonCreator
    public Task(@JsonProperty("title") String title,
                @JsonProperty("content") RGA content) {
        this.title = title;
        this.content = content;
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
}