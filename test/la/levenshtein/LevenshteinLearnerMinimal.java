package la.levenshtein;

import com.google.common.collect.Lists;
import de.learnlib.algorithms.lstargeneric.ce.ObservationTableCEXHandlers;
import de.learnlib.algorithms.lstargeneric.closing.ClosingStrategies;
import de.learnlib.algorithms.lstargeneric.mealy.ExtensibleLStarMealy;
import de.learnlib.api.EquivalenceOracle;
import de.learnlib.api.LearningAlgorithm;
import de.learnlib.api.MembershipOracle;
import de.learnlib.api.SUL;
import de.learnlib.eqtests.basic.mealy.RandomWalkEQOracle;
import de.learnlib.experiments.Experiment.MealyExperiment;
import de.learnlib.oracles.SULOracle;
import net.automatalib.automata.transout.MealyMachine;
import net.automatalib.commons.dotutil.DOT;
import net.automatalib.util.graphs.dot.GraphDOT;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.SimpleAlphabet;

import java.io.IOException;
import java.io.Writer;
import java.util.Random;
import org.junit.Test;

/**
 * Minimal example of using learnlib to learn the FSM/Mealy Machine of a SUL (System-Under-Learning).
 */
public class LevenshteinLearnerMinimal {
    //*****************//
    // SUL information //
    //*****************//
    // Defines the input alphabet.
    private final Alphabet<Character> inputAlphabet = new SimpleAlphabet<>();

    @Test
    public void test() throws IOException {
        String word = "atlas";
        int distance = 1;

        inputAlphabet.add('*');
        word.chars().forEach(i -> inputAlphabet.add((char)i));
        // Define the System Under Learning.
        SUL<Character, String> sul = new LevenshteinSUL(word, distance);

        // Most testing/learning-algorithms want a membership-oracle instead of a SUL directly
        // in order to optimize system queries.
        MembershipOracle<Character, Word<String>> sulOracle = new SULOracle<>(sul);


        // Choosing the EQ oracle
        EquivalenceOracle<MealyMachine<?, Character, ?, String>, Character, Word<String>> eqOracle
                = new RandomWalkEQOracle<>(
                0.05, // reset SUL w/ this probability before a step
                10000, // max steps (overall)
                true,  // reset step count after counterexample
                new Random(123456L), // make results reproducible
                sul    // system under learning
        );


        // Choosing a learning algorithm
        LearningAlgorithm<MealyMachine<?, Character, ?, String>, Character, Word<String>> learner
                = new ExtensibleLStarMealy<>(
                inputAlphabet, // Input Alphabet
                sulOracle,  // SUL membership oracle
                Lists.newArrayList(),
                ObservationTableCEXHandlers.CLASSIC_LSTAR,
                ClosingStrategies.CLOSE_SHORTEST
        );


        // Setup of the experiment.
        MealyExperiment<Character, String> experiment
                = new MealyExperiment<>(
                learner, // learning algorithm
                eqOracle, // Equivalence Oracle
                inputAlphabet); // input alphabet

        // And finally run the experiment
        experiment.run();


        // report results
        System.out.println("-------------------------------------------------------");

        MealyMachine<Integer, Character, ?, String> result = (MealyMachine<Integer, Character, ?, String>) experiment.getFinalHypothesis();

//        Writer w = DOT.createDotWriter(true);
//        GraphDOT.write(result, inputAlphabet, w);
//        w.close();


        new LevenshteinDFAChecker(word, distance).check(result);

    }


}