package crdt.taskmanager;

import java.util.NoSuchElementException;

public class BoardOperationHandler implements Runnable {
    private RGA<Board> boards;

    public BoardOperationHandler(RGA<Board> boards) {
        this.boards = boards;
    }

    @Override
    public void run() {
        // get causally ready operation
        while (true) {
            Operation<Board> op = boards.getOperation();

            if (op != null) {
                boards.dequeueOperation(op);

                for (int k = 0; k < Server.N; k++) {
                    boards.updateVectorClock(k, Math.max(
                            boards.getVectorClock()[k],
                            op.vectorClock[k]));
                }

                S4Vector so = new S4Vector(
                        op.sessionN,
                        op.siteN,
                        op.vectorClock);

                try {
                    switch (op.type) {
                        case INSERT -> boards.insert(op.i, so, op.getElement());
                        case UPDATE -> boards.update(op.i, so, op.getElement());
                        case DELETE -> boards.delete(op.i, so);
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Error occured while trying to perform a remote " +
                            e.getMessage() + " operation");
                }

                System.out.println("Boards size after insert: " + boards.size());
            } else {
                break;
            }
        }
    }
}
