package com.aim.metaheuristics.singlepoint.iteratedlocalsearch;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;
import uk.ac.nott.cs.aim.searchmethods.SinglePointSearchMethod;

import java.util.Random;

public class IteratedLocalSearch extends SinglePointSearchMethod {

	// local search / intensification heuristic
	private final SATHeuristic oLocalSearchHeuristic;
	
	// mutation / perturbation heuristic
	private final SATHeuristic oMutationHeuristic;

	private final int iIntensityOfMutation;  // iom parameter setting
	private final int iDepthOfSearch;		   // dos parameter setting
	
	/**
	 * 
	 * @param oProblem The problem to be solved.
	 * @param oRandom The random number generator, use this one, not your own!
	 * @param oMutationHeuristic The mutation heuristic.
	 * @param oLocalSearchHeuristic The local search heuristic.
	 * @param iIntensityOfMutation The parameter setting for intensity of mutation.
	 * @param iDepthOfSearch The parameter setting for depth of search.
	 */
	public IteratedLocalSearch(SAT oProblem, Random oRandom, SATHeuristic oMutationHeuristic, 
			SATHeuristic oLocalSearchHeuristic, int iIntensityOfMutation, int iDepthOfSearch) {
		super(oProblem, oRandom);
		this.oMutationHeuristic = oMutationHeuristic;
		this.oLocalSearchHeuristic = oLocalSearchHeuristic;
		this.iIntensityOfMutation = iIntensityOfMutation;
		this.iDepthOfSearch = iDepthOfSearch;
	}

	/**
	 * 
	 * Main loop for ILS. The experiment framework will continually call this
	 * loop until the allocated time has expired.
	 * 
	 * -- ITERATED LOCAL SEARCH PSEUDO CODE --
	 * 
	 * // The solutions in both CURRENT and BACKUP memory indices
	 * // are invariant the same at the start of each loop.
	 * s' <- s
	 * 
	 * // apply mutation heuristic "iIntensityOfMutation" times
	 * REPEAT intensityOfMutation TIMES:
	 *     s' <- mutation(s')
	 * 
	 * // apply local search heuristic "iDepthOfSearch" times
	 * REPEAT depthOfSearch TIMES:
	 *     s' <- localSearch(s')
	 * 
	 * // HINT: Remember that the solutions in the CURRENT and BACKUP memory indices
	 * //       should be the SAME after each application of the "runMainLoop()"!
	 *
	 * IF f(s') <= f(s) THEN
	 *     accept();
	 * ELIF
	 *     reject();
	 * FI
	 */
	protected void runMainLoop() {
		// Get the objective value of the current solution
		double currentObjectiveValue = problem.getObjectiveFunctionValue
				(SATHeuristic.CURRENT_SOLUTION_INDEX);

		// Apply mutation heuristic "iIntensityOfMutation" times
		for (int i = 0; i < iIntensityOfMutation; i++) {
			oMutationHeuristic.applyHeuristic(problem);
		}

		// Apply local search heuristic "iDepthOfSearch" times
		for (int i = 0; i < iDepthOfSearch; i++) {
			oLocalSearchHeuristic.applyHeuristic(problem);
		}

		// Get the objective value of the modified solution
		double modifiedObjectiveValue = problem.getObjectiveFunctionValue
				(SATHeuristic.CURRENT_SOLUTION_INDEX);

		// Check whether to accept the modified solution
		if (modifiedObjectiveValue <= currentObjectiveValue) {
			// Accept the modified solution
			// Copy the current solution to the backup solution
			problem.copySolution(SATHeuristic.CURRENT_SOLUTION_INDEX,
					SATHeuristic.BACKUP_SOLUTION_INDEX);
		} else {
			// Reject the modified solution, rollback to the backup solution
			problem.copySolution(SATHeuristic.BACKUP_SOLUTION_INDEX,
					SATHeuristic.CURRENT_SOLUTION_INDEX);
		}
	}

	public String toString() {
		return "Iterated Local Search";
	}
}
