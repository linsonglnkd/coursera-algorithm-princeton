import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int sumDist = Integer.MIN_VALUE;
        int result = -1;
        for (int i = 0; i < nouns.length; i++) {
            int sum = 0;
            for (int j = 0; j < nouns.length; j++) {
                sum = sum + wordnet.distance(nouns[i], nouns[j]);
            }
            if (sum > sumDist) {
                sumDist = sum;
                result = i;
            }
        }
        return nouns[result];
    }
    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}