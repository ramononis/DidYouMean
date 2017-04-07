package la.levenshtein;

import basiclearner.util.UserEQOracle;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import de.learnlib.acex.analyzers.AcexAnalyzers;
import de.learnlib.algorithms.kv.mealy.KearnsVaziraniMealy;
import de.learnlib.algorithms.lstargeneric.ce.ObservationTableCEXHandlers;
import de.learnlib.algorithms.lstargeneric.closing.ClosingStrategies;
import de.learnlib.algorithms.lstargeneric.mealy.ExtensibleLStarMealy;
import de.learnlib.algorithms.ttt.mealy.TTTLearnerMealy;
import de.learnlib.api.EquivalenceOracle;
import de.learnlib.api.LearningAlgorithm;
import de.learnlib.api.MembershipOracle;
import de.learnlib.api.SUL;
import de.learnlib.counterexamples.LocalSuffixFinders;
import de.learnlib.eqtests.basic.WMethodEQOracle;
import de.learnlib.eqtests.basic.WpMethodEQOracle;
import de.learnlib.eqtests.basic.mealy.RandomWalkEQOracle;
import de.learnlib.experiments.Experiment.MealyExperiment;
import de.learnlib.oracles.ResetCounterSUL;
import de.learnlib.oracles.SULOracle;
import de.learnlib.oracles.SymbolCounterSUL;
import net.automatalib.automata.transout.MealyMachine;
import net.automatalib.commons.dotutil.DOT;
import net.automatalib.util.graphs.dot.GraphDOT;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;
import net.automatalib.words.impl.SimpleAlphabet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

import nl.utwente.fmt.etfexporter.ETF;
import org.junit.Test;

import javax.annotation.concurrent.Immutable;

/**
 * Minimal example of using learnlib to learn the FSM/Mealy Machine of a SUL (System-Under-Learning).
 */
public class LevenshteinLearnerMinimal {
    //*****************//
    // SUL information //
    //*****************//
    // Defines the input alphabet.
    private Alphabet<String> inputAlphabet;

    @Test
    public void test() throws IOException {
        String word = "the quick brown fox jumps over the lazy dog";
        int distance = 1;
        Set<String> letters = new HashSet<>();
        for (char c : word.toCharArray()) {
            letters.add("" + c);
        }
        letters.add("*");
        inputAlphabet = Alphabets.fromCollection(letters);

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
                    = new ExtensibleLStarMealy<String, String>(inputAlphabet, sulOracle, Lists.<Word<String>>newArrayList(), ObservationTableCEXHandlers.CLASSIC_LSTAR, ClosingStrategies.CLOSE_SHORTEST);

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

        System.out.println(symbolCounterSUL.getStatisticalData().toString());
        System.out.println(resetCounterSUL.getStatisticalData().toString());
        System.out.println(experiment.getRounds());
        if (true) {
            Writer w = DOT.createDotWriter(true);
            GraphDOT.write(result, inputAlphabet, w);
            w.close();
        }
        ETF.export(result, inputAlphabet, new PrintWriter(new File("result.etf")));

        new LevenshteinDFAChecker(word, distance).check(result);
        System.out.println("DFA CHECKER SUCCESFULL");
    }


}