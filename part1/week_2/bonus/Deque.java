/*
Dequeue. A double-ended queue or deque (pronounced "deck") is a generalization 
of a stack and a queue that supports adding and removing items from either the 
front or the back of the data structure. Create a generic data type Deque 
that implements the following API:
*/
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private int N;  // size of the deque
    private class Node {
        private Item item;
        private Node nextNode;
        private Node prevNode;
    }
    private Node firstNode;
    private Node lastNode;
    // construct an empty deque
    public Deque() {
        N = 0;
        firstNode = null;
        lastNode = null;
    }
    // is the deque empty?
    public boolean isEmpty() {
        return (N == 0);
    }
    // return the number of items on the deque
    public int size() {
        return N;
    }
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new java.lang.NullPointerException("null item");
        Node node = new Node();
        node.item = item;
        node.prevNode = null;
        // always work even the first node is null (empty deque)
        node.nextNode = firstNode;  
        // only update the first node when not empty
        if (firstNode != null) firstNode.prevNode = node;  
        firstNode = node;
        if (N == 0) lastNode = node;  // if empty, need to set last node as well
        N++;
    }
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new java.lang.NullPointerException("null item");
        Node node = new Node();
        node.item = item;
        node.nextNode = null;
        // always work even the last node is null (empty deque)
        node.prevNode = lastNode;  
        // only update the last node when not empty
        if (lastNode != null) lastNode.nextNode = node;  
        lastNode = node;
        if (N == 0) firstNode = node; // if empty, need to set first node as well
        N++;
    }
    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException("empty deque");
        Node node = firstNode;
        // if only one element
        if (node.nextNode == null) {
            firstNode = null;
            lastNode = null;
        } else {
            node.nextNode.prevNode = null;
            firstNode = node.nextNode;
        }
        N--;
        return node.item;
    }
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException("empty deque");
        Node node = lastNode;
        // if only one element
        if (node.prevNode == null) {
            firstNode = null;
            lastNode = null;
        } else {
            node.prevNode.nextNode = null;
            lastNode = node.prevNode;
        }
        N--;
        return node.item;
    }
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    private class DequeIterator implements Iterator<Item> {
        private Node current = firstNode;
        public boolean hasNext() { return current != null; }
        public void remove() { 
            throw new java.lang.UnsupportedOperationException("not supported");
        };
        public Item next() {
            if (current == null) 
                throw new java.util.NoSuchElementException("no next");
            Item item = current.item;
            current = current.nextNode;
            return item;
        }
    }
    /* for debugging purpose
    public String toString() {
        Node current = firstNode;
        System.out.println(firstNode);
        while (current != null) {
            System.out.println(current.item);
            System.out.println(current);
            System.out.println(current.prevNode);
            System.out.println(current.nextNode);
            current = current.nextNode;
        }
        System.out.println(lastNode);
        return "----";
    }
    */

    // unit testing
    public static void main(String[] args) {
        String[] test = {"a", "b", "c"};
        Deque<String> a = new Deque<String>();
        System.out.println(a);
        a.addLast(test[0]);
        System.out.println(a);
        a.addLast(test[1]);
        System.out.println(a);
        a.addLast(test[2]);
        System.out.println(a);
        for (String i : a) System.out.println(i);
    }
}