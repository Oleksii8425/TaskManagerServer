package crdt.taskmanager;

import java.io.Serializable;

public class RGANode<T> implements Serializable {
    T value;
    S4Vector sk; //Sequence Key
    S4Vector sp; //Sequence Predecessor
    RGANode<T> next; //the next Node used for the hash table chaining
    RGANode<T> link; //the next Node in the linked list
}