package crdt.taskmanager;

import java.util.NoSuchElementException;

public class TaskOperationHandler implements Runnable {
    private RGA<Task> tasks;

    public TaskOperationHandler(RGA<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        // get causally ready operation
        while (true) {
            Operation<Task> op = tasks.getOperation();

            if (op != null) {
                tasks.dequeueOperation(op);

                for (int k = 0; k < Server.N; k++) {
                    Long localClock = tasks
                            .getVectorClock()[k];

                    tasks.updateVectorClock(k, Math.max(
                            localClock,
                            op.vectorClock[k]));
                }

                S4Vector so = new S4Vector(
                        op.sessionN,
                        op.siteN,
                        op.vectorClock);

                try {
                    switch (op.type) {
                        case INSERT -> tasks.insert(op.i, so, new Task(op.getElement().getTitle()));
                        case UPDATE -> tasks.update(op.i, so, op.getElement());
                        case DELETE -> tasks.delete(op.i, so);
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Error occured while trying to perform a remote " +
                            e.getMessage() + " operation");
                }
                
                System.out.println("Tasks size after insert: " + tasks.size());
            } else {
                break;
            }
        }
    }
}
