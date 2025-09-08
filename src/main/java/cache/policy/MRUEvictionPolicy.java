package cache.policy;

import java.util.HashMap;
import java.util.Map;

public class MRUEvictionPolicy implements EvictionPolicy {

    private final Map<String, Node> nodeMap;
    private final Node head;

    public MRUEvictionPolicy() {
        this.nodeMap = new HashMap<>();
        this.head = new Node("head");
        Node tail = new Node("tail");
        this.head.next = tail;
        tail.prev = this.head;
    }

    @Override
    public String evict() {
        Node nodeToEvict = head.next;
        System.out.println("Evicting: " + nodeToEvict.key);
        removeNode(nodeToEvict);
        nodeMap.remove(nodeToEvict.key);
        return nodeToEvict.key;
    }

    private void removeNode(Node node) {
        Node nextNode = node.next;
        Node prevNode = node.prev;
        node.prev.next = nextNode;
        nextNode.prev = prevNode;
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

    private void moveToFront(Node node) {
        Node nextToHead = this.head.next;
        this.head.next = node;
        node.prev = this.head;
        node.next = nextToHead;
        nextToHead.prev = node;
    }
}
