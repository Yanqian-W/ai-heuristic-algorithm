package com.aim.pseudorandom;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;

import java.util.Random;


/**
 * A heuristic to flip a random bit.
 * @author Warren G. Jackson
 */
public class RandomBitFlipHeuristic extends SATHeuristic {
	public RandomBitFlipHeuristic(Random random) {
		super(random);
	}

	@Override
	public void applyHeuristic(SAT problem) {
		// select a random bit in the solution
		int length = problem.getNumberOfVariables();
		int randomIndex = random.nextInt(length);
		// flip the bit
		problem.bitFlip(randomIndex, SATHeuristic.CURRENT_SOLUTION_INDEX);
	}

	@Override
	public String getHeuristicName() {
		return "Random Bit Flip";
	}
}
