package crdt.taskmanager;

public class RGANode {
    Object value;
    S4Vector sk; //Sequence Key
    S4Vector sp; //Sequence Predecessor
    RGANode next; //the next Node used for the hash table chaining
    RGANode link; //the next Node in the linked list
}
