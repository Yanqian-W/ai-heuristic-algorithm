package com.aim.heuristics;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;

import java.util.Random;

public class SteepestDescentHC extends SATHeuristic {
	public SteepestDescentHC(Random random) {
		super(random);
	}

	/**
	  * STEEPEST DESCENT HILL CLIMBING LECTURE SLIDE PSEUDO-CODE
	  *
	  * bestEval = evaluate(currentSolution);
	  * improved = false;
	  * 
	  * for(j = 0; j < length[currentSolution]; j++) {
	  *
	  *     bitFlip(currentSolution, j); // flips j^th bit of current solution
	  *     tmpEval = evaluate(currentSolution);
	  *
	  *     if(tmpEval < bestEval) { 	// remember the bit which yields the best value after evaluation
	  *         bestIndex = j;
	  *         bestEval = tmpEval; 	// record best achievable solution objective value
	  *         improved = true;
	  *     }
	  *     bitFlip(currentSolution, j); // go back to the initial current solution
	  * }
	  *
	  * if(improved) { bitFlip(currentSolution, bestIndex); }
	  *
	  * @param problem The problem to be solved.
	  */
	public void applyHeuristic(SAT problem) {
		// evaluate the current solution
		double bestEval = problem.getObjectiveFunctionValue
				(SATHeuristic.CURRENT_SOLUTION_INDEX);
		boolean improved = false;
		int bestIndex = 0;

		for(int i = 0; i < problem.getNumberOfVariables(); i++) {
			// flips i^th bit of current solution
			problem.bitFlip(i, SATHeuristic.CURRENT_SOLUTION_INDEX);
			// evaluate the modified solution
			double tmpEval = problem.getObjectiveFunctionValue
					(SATHeuristic.CURRENT_SOLUTION_INDEX);

			// compare and accept only improving moves
			if (tmpEval < bestEval) {
				// record best achievable solution objective value
				bestIndex = i;
	            bestEval = tmpEval;
	            improved = true;
			}
			// go back to the initial current solution
			problem.bitFlip(i, SATHeuristic.CURRENT_SOLUTION_INDEX);
		}

		if(improved) {
			problem.bitFlip(bestIndex, SATHeuristic.CURRENT_SOLUTION_INDEX);
		}
	}

	public String getHeuristicName() {
		return "Steepest Descent HC";
	}
}
