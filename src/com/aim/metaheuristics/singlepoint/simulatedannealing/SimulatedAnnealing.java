package com.aim.metaheuristics.singlepoint.simulatedannealing;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;
import uk.ac.nott.cs.aim.searchmethods.SinglePointSearchMethod;

import java.util.Random;

public class SimulatedAnnealing extends SinglePointSearchMethod {
	private final CoolingSchedule oCoolingSchedule;
	
	public SimulatedAnnealing(CoolingSchedule schedule, SAT problem, Random random) {
		super(problem, random);
		this.oCoolingSchedule = schedule;
	}

	/**
	 * ================================================================
	 * NOTE: In the same way as last week's exercise, you only need
	 *       to implement the code WITHIN the loop for runMainLoop().
	 *       Everything else is handled by the framework/Lab_03_Runner.
	 * ================================================================
	 * 
	 * PSEUDOCODE for Simulated Annealing:
	 *
	 * INPUT : T_0 and any other parameters of the cooling schedule
	 * s_0 = generateInitialSolution();
	 * Temp <- T_0;
	 * s_{best} <- s_0;
	 * s' <- s_0;
	 *
	 * REPEAT
	 *     s' <- randomBitFlip(s);
	 *     delta <- f(s') - f(s);
	 *     r <- random \in [0,1];
	 *     IF delta < 0 OR r < P(delta, Temp) THEN
	 *         s <- s';
	 *     ENDIF
	 *     s_{best} <- updateBest(); // NOTE: this step is already handled by the framework!
	 *     Temp <- advanceTemperature(); // DO NOT FORGET THIS STEP!!!
	 *     
	 * UNTIL termination conditions are satisfied;
	 *
	 * RETURN s_{best};
	 * 
	 * 
	 * REMEMBER That the solutions in the CURRENT_SOLUTION_INDEX and BACKUP_SOLUTION_INDEX
	 * 	should be the same before returning from 'runMainLoop()'!
	 * 
	 * Here, P is the probability function e^(-delta/T)
	 */
	protected void runMainLoop() {
		// Get the objective value of the current solution
		double currentObjectiveValue = problem.getObjectiveFunctionValue
				(SATHeuristic.CURRENT_SOLUTION_INDEX);

		// Perform random bit flip on current solution
		randomBitFlip(problem);

		// Get the objective value of the new solution
		double newObjectiveValue = problem.getObjectiveFunctionValue
				(SATHeuristic.CURRENT_SOLUTION_INDEX);

		// Calculate change in objective value
		double delta = newObjectiveValue - currentObjectiveValue;
		// Generate a random number r
		double r = random.nextDouble();

		// Check whether to accept the new solution
		if (delta < 0 || r < Math.exp(-delta / oCoolingSchedule.getCurrentTemperature())) {
			// If the new solution is better or accepted with probability
			// Copy the current solution to the backup solution
			problem.copySolution(SATHeuristic.CURRENT_SOLUTION_INDEX,
					SATHeuristic.BACKUP_SOLUTION_INDEX);
		} else {
			// reject
			problem.copySolution(SATHeuristic.BACKUP_SOLUTION_INDEX,
					SATHeuristic.CURRENT_SOLUTION_INDEX);
		}

		// Update the temperature
		oCoolingSchedule.advanceTemperature();
	}

	private void randomBitFlip(SAT problem) {
		// select a random bit in the solution
		int length = problem.getNumberOfVariables();
		int randomIndex = random.nextInt(length);
		// flip the bit
		problem.bitFlip(randomIndex, SATHeuristic.CURRENT_SOLUTION_INDEX);
	}

	public String toString() {
		return "Simulated Annealing with " + oCoolingSchedule.toString();
	}
}
