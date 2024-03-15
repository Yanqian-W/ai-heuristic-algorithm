package com.aim.metaheuristics.population.memetic;

import com.aim.metaheuristics.population.ParentSelection;
import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;

import java.util.Random;

/**
 * @author Warren G. Jackson
 */
public class TournamentSelection extends ParentSelection {

	private final int tournamentSize;

	public TournamentSelection(SAT problem, Random rng, int POPULATION_SIZE, int tournamentSize) {
		super(problem, rng, POPULATION_SIZE);
		this.tournamentSize = tournamentSize;
	}

	/**
	  * @return The index of the chosen parent solution.
	  *
	  * PSEUDOCODE
	  *
	  * INPUT: parent_pop, tournament_size
	  * solutions = getUniqueRandomSolutions(tournament_size); 
	  * bestSolution = getBestSolution(solutions);
	  * index = indexOf(bestSolution);
	  * return index;
	  */
	public int parentSelection() {

		// getUniqueRandomSolutions(tournament_size)
		int[] tournament = new int[tournamentSize];

		for (int i = 0; i < tournamentSize; i++) {
			tournament[i] = rng.nextInt(POPULATION_SIZE);
			// check if this solution has existed
			for (int j = 0; j < i; j++) {
				if ((tournament[i] == tournament[j]) && (tournament[i] != (POPULATION_SIZE - 1))) {
					tournament[i] += 1;
					break;
				}
			}
		}

		// getBestSolution(solutions)
		int bestIndex = -1;
		double bestValue = Double.MAX_VALUE;

		for (int i = 0; i < tournamentSize; i++) {
			double currentValue = problem.getObjectiveFunctionValue(tournament[i]);
			if (currentValue < bestValue) {
				bestValue = currentValue;
				bestIndex = tournament[i];
			}
		}

		return bestIndex;
	}
}
