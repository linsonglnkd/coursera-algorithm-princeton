import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int N;
    private Item[] s;
    // construct an empty randomized queue
    public RandomizedQueue() {
        N = 0;
        s = (Item []) new Object[1];
    }
    // help function
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) copy[i] = s[i];
        s = copy;
    }
    // is the queue empty?
    public boolean isEmpty() {
        return (N == 0);
    }
    // return the number of items on the queue
    public int size() {
        return N;
    }
    // add the item
    public void enqueue(Item item) {
        //System.out.println(item);
        if (item == null) throw new java.lang.NullPointerException("null item");
        if (N == s.length) resize(2 * s.length);
        s[N++] = item;
    }
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException("empty queue");
        // switch index with N-1 (last one) and then return the last one
        int index = StdRandom.uniform(N);
        Item swap = s[index];
        s[index] = s[N - 1];
        s[N - 1] = null;
        N--;
        if (N > 0 && N == s.length/4) resize(s.length/2);
        return swap;
    }
    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException("empty queue");
        int index = StdRandom.uniform(N);
        return s[index];
    }
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandQIterator();
    }
    // Iterator
    private class RandQIterator implements Iterator<Item> {
        private Item [] shuffleArray;
        private int index;
        RandQIterator() {
            // create a copy and shuffle
            shuffleArray = (Item []) new Object[N];
            for (int i = 0; i < N; i++) 
                shuffleArray[i] = s[i];
            StdRandom.shuffle(shuffleArray);
            index = 0;
        }

        public boolean hasNext() { 
            return (index < N && shuffleArray[index] != null); 
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException("not supported");
        };
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("no next");
            Item item = shuffleArray[index++];
            return item;
        }
    }
    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        String [] a = {"a", "b", "c", "d", "e"};
        for (String i : a) {
            q.enqueue(i);
        }

        for (String i : q) System.out.println(i);
    }
}
