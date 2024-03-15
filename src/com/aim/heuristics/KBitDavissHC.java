package com.aim.heuristics;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;

import java.util.Random;

public class KBitDavissHC extends SATHeuristic {
	private final int k;

	public KBitDavissHC(int k, Random random) {
		super(random);
		this.k = k;
	}

	/**
	 *  This heuristic is similar to Davis's Bit Hill Climbing
	 *  with one key difference. Note that we now have a variable 'k'.
	 *  This 'k' is used to determine the number of bit flips to
	 *  perform at each iteration before checking whether the solution
	 *  was improved.
	 *
	 *  For each iteration 'i', the bits perm[i] to perm[i+k-1] should
	 *  be flipped. Be careful of the edge case!
	  */
	public void applyHeuristic(SAT problem) {
		// evaluate the initial solution
		double bestEval = problem.getObjectiveFunctionValue
				(SATHeuristic.CURRENT_SOLUTION_INDEX);
		int length = problem.getNumberOfVariables();
		int[] perm = createRandomPermutation(length);

		// ensure i does not exceed the array range
		for (int i = 0; i < length-k+1; i++) {
			// flips k bits starting from perm[i]
			for (int j = 0; j < k; j++) {
				problem.bitFlip(perm[i+j], SATHeuristic.CURRENT_SOLUTION_INDEX);
			}
			// evaluate the modified solution
			double tmpEval = problem.getObjectiveFunctionValue
					(SATHeuristic.CURRENT_SOLUTION_INDEX);

			// compare and accept only improving moves
			if (tmpEval < bestEval) {
				bestEval = tmpEval;
			} else {
				// go back
				for (int j = 0; j < k; j++) {
					problem.bitFlip(perm[i+j], SATHeuristic.CURRENT_SOLUTION_INDEX);
				}
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
		return "kBDHC";
	}
}
