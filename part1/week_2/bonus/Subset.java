import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        int i = 1;
        int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            // first k elements got into the queue (temporarily)
            if (i <= k) {
                q.enqueue(str);
            }
            else {
                // make switch based on random number
                if (StdRandom.uniform() > (i - k) / (double) i) {
                    String nouse = q.dequeue();
                    q.enqueue(str);
                }
            }
            i++;
        }
        for (i = 0; i < k; i++) System.out.println(q.dequeue());
    }
}