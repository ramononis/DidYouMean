package la.sanitycheck;

import com.google.common.collect.ImmutableSet;
import de.learnlib.acex.analyzers.AcexAnalyzers;
import de.learnlib.algorithms.ttt.mealy.TTTLearnerMealy;
import de.learnlib.api.EquivalenceOracle;
import de.learnlib.api.LearningAlgorithm;
import de.learnlib.api.MembershipOracle;
import de.learnlib.api.SUL;
import de.learnlib.counterexamples.LocalSuffixFinders;
import de.learnlib.eqtests.basic.WpMethodEQOracle;
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

/**
 * Minimal example of using learnlib to learn the FSM/Mealy Machine of a SUL (System-Under-Learning).
 */
public class SimpleLearnerMinimal {
    //*****************//
    // SUL information //
    //*****************//
    // Defines the input alphabet.
    private static final Alphabet<String> inputAlphabet
            = new SimpleAlphabet<>(ImmutableSet.of("a", "b", "c"));


    public static void main(String[] args) throws IOException {

        // Define the System Under Learning.
        SUL<String, String> sul = new SimpleSUL();

        // Most testing/learning-algorithms want a membership-oracle instead of a SUL directly
        // in order to optimize system queries.
        MembershipOracle<String, Word<String>> sulOracle = new SULOracle<>(sul);


        // Choosing the EQ oracle
        EquivalenceOracle<MealyMachine<?, String, ?, String>, String, Word<String>> eqOracle
                = new WpMethodEQOracle.MealyWpMethodEQOracle<>(2, sulOracle);

        // Choosing a learning algorithm
        LearningAlgorithm<MealyMachine<?, String, ?, String>, String, Word<String>> learner
                = new TTTLearnerMealy<String, String>(inputAlphabet, sulOracle, AcexAnalyzers.LINEAR_FWD);

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

        Writer w = DOT.createDotWriter(true);
        GraphDOT.write(experiment.getFinalHypothesis(), inputAlphabet, w);
        w.close();

    }


}