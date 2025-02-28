package crdt.taskmanager;

import java.io.Serial;
import java.io.Serializable;

public class Board implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String title;
    private RGA<Task> tasks;

    public Board() {
    }

    public Board(String title, RGA<Task> tasks) {
        this.title = title;
        this.tasks = tasks;
    }

    public Board(String title) {
        this.title = title;
        this.tasks = new RGA<Task>();
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

    public void addTask(Task task) {
        int i = tasks.toList().size() - 1;
        tasks.insert(i, new S4Vector(title), task);
    }

    public void removeTask(Task task) {
        int i = 0;

        for (Task t : tasks) {
            if (t.equals(task)) {
                tasks.delete(i);
            }
            
            i++;
        }
    }
}