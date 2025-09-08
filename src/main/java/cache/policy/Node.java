package cache.policy;

public class Node {

    String key;
    Node next;
    Node prev;

    public Node(String key) {
        this.key = key;
        this.next = null;
        this.prev = null;
    }
}
