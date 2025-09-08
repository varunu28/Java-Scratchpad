package problemsolving;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class MaxStack {

    private final Stack<int[]> stack;
    private final Queue<int[]> queue;
    private final Set<Integer> removedIds;
    private int id;

    public MaxStack() {
        stack = new Stack<>();
        queue = new PriorityQueue<>((a, b) -> b[0] - a[0] == 0 ? b[1] - a[1] : b[0] - a[0]);
        removedIds = new HashSet<>();
    }

    public void push(int x) {
        stack.add(new int[] {x, id});
        queue.add(new int[] {x, id});
        id++;
    }

    public int pop() {
        updateStack();
        int[] top = stack.pop();
        removedIds.add(top[1]);
        return top[0];
    }

    public int top() {
        updateStack();
        return stack.peek()[0];
    }

    public int peekMax() {
        updateQueue();
        return queue.peek()[0];
    }

    public int popMax() {
        updateQueue();
        int[] top = queue.poll();
        removedIds.add(top[1]);
        return top[0];
    }

    private void updateStack() {
        while (removedIds.contains(stack.peek()[1])) {
            stack.pop();
        }
    }

    private void updateQueue() {
        while (removedIds.contains(queue.peek()[1])) {
            queue.poll();
        }
    }
}