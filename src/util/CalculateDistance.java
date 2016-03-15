package util;

/**
 * Created by Tim on 3/10/2016.
 */
public class CalculateDistance {

    /**
     * Calculates the Levenshtein Distance between 2 words.
     * @param word1 First word
     * @param word2 Second word
     * @return LD between word1 and word2.
     */
    public static int calculateLD(String word1, String word2){
        //based on http://rosettacode.org/wiki/Levenshtein_distance#Java
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        int[] costs = new int[word2.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= word1.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= word2.length(); j++) {
                int costj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        word1.charAt(i - 1) == word2.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = costj;
            }
        }
        return costs[word2.length()];


    }

    public static void main(String[] args){
        System.out.println(calculateLD("hoi", "hallo"));
        System.out.println(calculateLD("Abba", "baab"));
        System.out.println(calculateLD("fod", "fxod"));
    }
}
