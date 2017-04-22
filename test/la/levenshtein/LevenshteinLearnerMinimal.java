package la.levenshtein;

import de.learnlib.acex.analyzers.AcexAnalyzers;
import de.learnlib.algorithms.ttt.mealy.TTTLearnerMealy;
import de.learnlib.api.EquivalenceOracle;
import de.learnlib.api.LearningAlgorithm;
import de.learnlib.api.MembershipOracle;
import de.learnlib.api.SUL;
import de.learnlib.eqtests.basic.WpMethodEQOracle;
import de.learnlib.experiments.Experiment.MealyExperiment;
import de.learnlib.oracles.ResetCounterSUL;
import de.learnlib.oracles.SULOracle;
import de.learnlib.oracles.SymbolCounterSUL;
import net.automatalib.automata.transout.MealyMachine;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Minimal example of using learnlib to learn the FSM/Mealy Machine of a SUL (System-Under-Learning).
 */
public class LevenshteinLearnerMinimal {

    @Test
    public void testDifferentChars() {
        LinkedList<String> strings = new LinkedList<>();
        String string = "the quick brown fox jumps over a lazy dog";
        char start = 'a';
        char end = 'z';
        strings.add(string);
        while(start <= end) {
            string = string.replace(end, ' ');
            strings.addFirst(string);
            end--;
        }
        System.out.println(strings.toString());
        for(String s : strings) {
            long[] result = test(s, 1);
            System.out.println(s);
            System.out.printf("States: %s, Symbols: %s, Resets: %s", result[0], result[1], result[2]);
        }
    }

    /**
     * @param word input word
     * @param distance max edit distance
     * @return {states, symbols, resets}
     */
    @SuppressWarnings("unchecked")
    public long[] test(String word, int distance) {
        Set<String> letters = new HashSet<>();
        word.chars().forEach(i -> letters.add("" + ((char) i)));
        letters.add("*");
        Alphabet<String> inputAlphabet = Alphabets.fromCollection(letters);

        // Define the System Under Learning.
        SUL<String, String> sul = new LevenshteinSUL(word, distance);

        final SymbolCounterSUL symbolCounterSUL = new SymbolCounterSUL("Counter", sul);
        final ResetCounterSUL resetCounterSUL = new ResetCounterSUL("Counter", symbolCounterSUL);

        sul = resetCounterSUL;
        // Most testing/learning-algorithms want a membership-oracle instead of a SUL directly
        // in order to optimize system queries.
        MembershipOracle<String, Word<String>> sulOracle = new SULOracle<>(sul);


        // Choosing the EQ oracle
//        EquivalenceOracle<MealyMachine<?, Character, ?, String>, Character, Word<String>> eqOracle
//                = new RandomWalkEQOracle<>(
//                0.05, // reset SUL w/ this probability before a step
//                10000, // max steps (overall)
//                true,  // reset step count after counterexample
//                new Random(123456L), // make results reproducible
//                sul    // system under learning
//        );
        EquivalenceOracle<MealyMachine<?, String, ?, String>, String, Word<String>> eqOracle
                = new WpMethodEQOracle.MealyWpMethodEQOracle<>(2, sulOracle);

        // Choosing a learning algorithm
        LearningAlgorithm<MealyMachine<?, String, ?, String>, String, Word<String>> learner
                = new TTTLearnerMealy<>(inputAlphabet, sulOracle, AcexAnalyzers.LINEAR_FWD);
        // = new ExtensibleLStarMealy<>(inputAlphabet, sulOracle, Lists.<Word<String>>newArrayList(), ObservationTableCEXHandlers.CLASSIC_LSTAR, ClosingStrategies.CLOSE_SHORTEST);

        // Setup of the experiment.
        MealyExperiment<String, String> experiment
                = new MealyExperiment<>(
                learner, // learning algorithm
                eqOracle, // Equivalence Oracle
                inputAlphabet); // input alphabet

        // And finally run the experiment
        experiment.run();


        // report results
        System.out.println("-------------------------------------------------------");

        MealyMachine<?, String, ?, String> result = experiment.getFinalHypothesis();

        System.out.println("States: " + result.size());

        long symbols = symbolCounterSUL.getStatisticalData().getCount();
        long resets = resetCounterSUL.getStatisticalData().getCount();
        System.out.println(experiment.getRounds());
        new LevenshteinDFAChecker(word, distance).check(result);
        return new long[]{result.size(), symbols, resets};
    }


}