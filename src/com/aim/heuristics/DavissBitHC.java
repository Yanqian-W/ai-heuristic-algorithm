package com.aim.heuristics;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;

import java.util.Random;

public class DavissBitHC extends SATHeuristic {
    
    public DavissBitHC(Random random) {
        super(random);
    }

    /**
     * DAVIS's BIT HILL CLIMBING LECTURE SLIDE PSEUDO-CODE
     *
     * bestEval = evaluate(currentSolution);
     * perm = createRandomPermutation();
     *
     * for(j = 0; j < length[currentSolution]; j++) {
     *     bitFlip(currentSolution, perm[j]); 	// flips j^th bit from permutation of solution producing s' from s
     *     tmpEval = evaluate(currentSolution);
     *
     *     if(tmpEval < bestEval) { 		// if there is improvement (strict improvement)
     *         bestEval = tmpEval; 			// accept the best flip
     *     } else { 						// if there is no improvement, reject the current bit flip
     *         bitFlip(solution, perm[j]); 	// go back to s from s'
     *     }
     * }
     *
     * @param problem The problem to be solved.
     */
    public void applyHeuristic(SAT problem) {
        // evaluate the current solution
        double bestEval = problem.getObjectiveFunctionValue
                (SATHeuristic.CURRENT_SOLUTION_INDEX);
        int length = problem.getNumberOfVariables();
        int[] perm = createRandomPermutation(length);

        for(int i = 0; i < length; i++) {
            // flips i^th bit from permutation of solution
            problem.bitFlip(perm[i], SATHeuristic.CURRENT_SOLUTION_INDEX);
            // evaluate the modified solution
            double tmpEval = problem.getObjectiveFunctionValue
                    (SATHeuristic.CURRENT_SOLUTION_INDEX);

            // compare and accept only improving moves
            if (tmpEval < bestEval) {
                bestEval = tmpEval;
            } else {
                // go back
                problem.bitFlip(perm[i], SATHeuristic.CURRENT_SOLUTION_INDEX);
            }
        }
    }

    public int[] createRandomPermutation(int n) {
        // Initialise the array as a continuous integer from 0 to n-1
        int[] permutation = new int[n];
        for (int i = 0; i < n; i++) {
            permutation[i] = i;
        }

        // Fisher Yates shuffling algorithm, randomly shuffling the order of array elements
        for (int i = n-1; i > 0; i--) {
            // generate random integer between [0, i]
            int j = random.nextInt(i+1);
            // exchange ith and jth
            int temp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = temp;
        }

        return permutation;
    }

    @Override
    public String getHeuristicName() {
        return "DBHC";
    }
}
