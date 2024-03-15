
package com.aim.metaheuristics.population.memetic;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.genetics.PopulationReplacement;

public class TransGenerationalReplacementWithElitistReplacement extends PopulationReplacement {

	/**
	 * Replaces the current population with the offspring and replaces the worst
	 * offspring with the best solution if the best is not contained in the offspring.
	 *
	 * @return The indices of the solutions to use in the next generation.
	 *
	 * PSEUDOCODE
	 *
	 * INPUT current_pop, offspring_pop
	 * fitness <- evaluate( current_pop U offspring_pop );
	 * best <- min( fitness );
	 * next_pop <- indicesOf( offspring_pop );
	 * IF best \notin offspring_pop THEN
	 *     next_pop.replace( worst, best );
	 * ENDIF
	 * OUTPUT: next_pop; // return the indices of the next population
	 */
	@Override
	protected int[] getNextGeneration(SAT oProblem, int iPopulationSize) {
		
		// evaluate and find the best solution
		int bestIndex = -1;
		double bestValue = Double.MAX_VALUE;
		double currentValue;

		for (int i = 0; i < iPopulationSize * 2; i++) {
			currentValue = oProblem.getObjectiveFunctionValue(i);
			if (currentValue < bestValue) {
				bestValue = currentValue;
				bestIndex = i;
			}
		}

		// copy indexes of offspring into next_pop
		int[] nextPop = new int[iPopulationSize];
		boolean ifBestInOffspring = false;
		int worstPosition = -1;
		double worstValue = Double.MIN_VALUE;

		for (int i = 0; i < iPopulationSize; i++) {
			nextPop[i] = i + iPopulationSize;
			currentValue = oProblem.getObjectiveFunctionValue(nextPop[i]);

			if (nextPop[i] == bestIndex) {
				ifBestInOffspring = true;
			}
			if (currentValue > worstValue) {
				worstValue = currentValue;
				worstPosition = i;
			}
		}

		// check if bestIndex is in the offspring
		if (!ifBestInOffspring) {
			// next_pop.replace( worst, best )
			nextPop[worstPosition] = bestIndex;
		}

		return nextPop;
	}
}
