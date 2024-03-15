
package com.aim.metaheuristics.population.memetic;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.genetics.PopulationReplacement;

public class BasicReplacement extends PopulationReplacement {

	/**
	 * Replaces the current population with the offspring population.
	 *
	 * @return The indices of the solutions to use in the next generation.
	 *
	 * PSEUDOCODE
	 *
	 * INPUT current_pop, offspring_pop
	 * OUTPUT: offspring_pop; // return the indices of the next population
	 */
	@Override
	protected int[] getNextGeneration(SAT oProblem, int iPopulationSize) {

		int[] offspring = new int[iPopulationSize];

		for (int i = 0; i < iPopulationSize; i++) {
			offspring[i] = iPopulationSize + i;
		}

        return offspring;
	}
}
