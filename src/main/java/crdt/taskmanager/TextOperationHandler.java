package crdt.taskmanager;

import java.util.NoSuchElementException;

public class TextOperationHandler implements Runnable {
    private RGA<Character> taskContent;

    public TextOperationHandler(RGA<Character> taskContent) {
        this.taskContent = taskContent;
    }

    @Override
    public void run() {
        // get causally ready operation
        while (true) {
            Operation<Character> op = taskContent.getOperation();

            if (op != null) {
                taskContent.dequeueOperation(op);

                for (int k = 0; k < Server.N; k++) {
                    taskContent.updateVectorClock(k, Math.max(
                            taskContent.getVectorClock()[k],
                            op.vectorClock[k]));
                }

                S4Vector so = new S4Vector(
                        op.sessionN,
                        op.siteN,
                        op.vectorClock);

                try {
                    switch (op.type) {
                        case INSERT -> taskContent.insert(op.i, so, op.getCharacter());
                        case UPDATE -> taskContent.update(op.i, so, op.getCharacter());
                        case DELETE -> taskContent.delete(op.i, so);
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Error occured while trying to perform a remote " +
                            e.getMessage() + " operation");
                }

                Server.save();
            } else {
                break;
            }
        }
    }
}