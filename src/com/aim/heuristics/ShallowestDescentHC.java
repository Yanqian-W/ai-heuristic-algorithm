package com.aim.heuristics;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;

import java.util.Random;

public class ShallowestDescentHC extends SATHeuristic {
	public ShallowestDescentHC(Random random) {
		super(random);
	}

	/**
	  * This heuristic is similar to Steepest Descent Hill Climbing
	  * but the difference here is that we want to flip the bit that
	  * results in the least improvement (note this does not include
	  * no improvement).
	  */
	public void applyHeuristic(SAT problem) {
		// evaluate the current solution
		double bestEval = problem.getObjectiveFunctionValue
				(SATHeuristic.CURRENT_SOLUTION_INDEX);
		boolean improved = false;
		int leastIndex = 0;
		double minImprovement = Double.MAX_VALUE;

		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
			// flips i^th bit of current solution
			problem.bitFlip(i, SATHeuristic.CURRENT_SOLUTION_INDEX);
			// evaluate the modified solution
			double tmpEval = problem.getObjectiveFunctionValue(SATHeuristic.CURRENT_SOLUTION_INDEX);

			// Calculate improvement value
			double improvement = bestEval - tmpEval;
			if ((improvement < minImprovement) && (improvement > 0)) {
				// record solution with the least improvement and its index
				leastIndex = i;
				minImprovement = improvement;
				improved = true;
			}
			// go back to the initial current solution
			problem.bitFlip(i, SATHeuristic.CURRENT_SOLUTION_INDEX);
		}

		if (improved) {
			// flip the bit that results in the least improvement
			problem.bitFlip(leastIndex, SATHeuristic.CURRENT_SOLUTION_INDEX);
		}
	}

	public String getHeuristicName() {
		return "Shallowest Descent HC";
	}
}
