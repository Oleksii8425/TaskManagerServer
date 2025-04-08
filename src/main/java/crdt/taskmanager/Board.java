package crdt.taskmanager;

import java.io.Serializable;

public class Board implements Serializable, Titleable {
    private static final long serialVersionUID = 1L;
    
    private String title;
    private RGA<Task> tasks;

    public Board() {
    }

    public Board(String title) {
        this.title = title;
        this.tasks = new RGA<Task>();
    }

    public Board(String title, RGA<Task> tasks) {
        this.title = title;
        this.tasks = tasks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RGA<Task> getTasks() {
        return tasks;
    }

    public void setTasks(RGA<Task> tasks) {
        this.tasks = tasks;
    }
}
