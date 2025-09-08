package cache.policy;

import java.util.HashMap;
import java.util.Map;

public class LRUEvictionPolicy implements EvictionPolicy {

    private final Map<String, Node> nodeMap;
    private final Node head;
    private final Node tail;

    public LRUEvictionPolicy() {
        this.nodeMap = new HashMap<>();
        this.head = new Node("head");
        this.tail = new Node("tail");
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    @Override
    public String evict() {
        Node nodeToEvict = tail.prev;
        System.out.println("Evicting: " + nodeToEvict.key);
        removeNode(nodeToEvict);
        nodeMap.remove(nodeToEvict.key);
        return nodeToEvict.key;
    }

    @Override
    public void recordUsage(String key) {
        if (!nodeMap.containsKey(key)) {
            nodeMap.put(key, new Node(key));
            moveToFront(nodeMap.get(key));
        } else {
            Node node = nodeMap.get(key);
            removeNode(node);
            moveToFront(node);
        }
    }

    private void removeNode(Node node) {
        Node nextNode = node.next;
        Node prevNode = node.prev;
        node.prev.next = nextNode;
        nextNode.prev = prevNode;
    }

    private void moveToFront(Node node) {
        Node nextToHead = this.head.next;
        this.head.next = node;
        node.prev = this.head;
        node.next = nextToHead;
        nextToHead.prev = node;
    }
}
