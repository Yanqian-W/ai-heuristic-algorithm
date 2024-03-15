package com.aim.metaheuristics.population.memetic;

import com.aim.metaheuristics.population.ParentSelection;
import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.genetics.CrossoverHeuristic;
import uk.ac.nott.cs.aim.satheuristics.genetics.PopulationHeuristic;
import uk.ac.nott.cs.aim.satheuristics.genetics.PopulationReplacement;
import uk.ac.nott.cs.aim.searchmethods.PopulationBasedSearchMethod;

import java.util.Random;

/**
 * Memetic Algorithm ( local search should to be added per the report exercise ).
 * 
 * @author Warren G. Jackson
 *
 */
public class MemeticAlgorithm extends PopulationBasedSearchMethod {

	private static final int TOURNAMENT_SIZE = 3;

	private final CrossoverHeuristic crossover;

	private final PopulationHeuristic mutation;

	private final PopulationHeuristic localSearch;

	private final PopulationReplacement replacement;

	private final ParentSelection p1Selection;

	private final ParentSelection p2Selection;

	public MemeticAlgorithm(SAT problem, Random rng, int populationSize, CrossoverHeuristic crossover, 
			PopulationHeuristic mutation, PopulationHeuristic localSearch, ParentSelection p1Selection,
			ParentSelection p2Selection, PopulationReplacement replacement) {
		
		super(problem, rng, populationSize);
		
		this.crossover = crossover;		 // UniformXO
		this.mutation = mutation;		 // BitMutation
		this.localSearch = localSearch;
		this.replacement = replacement;

		this.p1Selection = p1Selection;
		this.p2Selection = p2Selection;
	}

	/**
	  * Memetic Algorithm pseudocode
	  * Note there is no exact pseudocode since the purpose of this
	  * exercise is that you experiment with applying local search
	  * in different places of the MA.
	  *
	  * BASIC PSEUDO CODE (GA) not MA
	  * (population already initialised)
	  * 
	  * FOR 0 -> populationSize / 2
	  *		select unique parents using the respective selection method
	  *			- if the parents are the same, then select parent 2 as the following parent along
	  *     apply crossover to generate offspring
	  *     apply mutation to offspring
	  * ENDFOR
	  *
	  * do population replacement
	  *
	  */
	public void runMainLoop() {

		for(int iIterationCount = 0; iIterationCount < POPULATION_SIZE; iIterationCount += 2) {

			// select unique parents
			int p1Index = p1Selection.parentSelection();
			int p2Index = p2Selection.parentSelection();
			if ((p1Index == p2Index) && (p2Index == (POPULATION_SIZE - 1))) {
				p2Index = 0;
			} else if(p1Index == p2Index) {
				p2Index += 1;
			}

			// apply crossover to generate offspring
			int c1Index = POPULATION_SIZE + iIterationCount;
			int c2Index = POPULATION_SIZE + iIterationCount + 1;
			crossover.applyHeuristic(p1Index, p2Index, c1Index, c2Index);

			// apply mutation to offspring
			mutation.applyHeuristic(c1Index);
			mutation.applyHeuristic(c2Index);

			// apply local search to offspring
			localSearch.applyHeuristic(c1Index);
			localSearch.applyHeuristic(c2Index);
		}

		// do population replacement
		replacement.doReplacement(problem, POPULATION_SIZE);
	}
	
	@Override
	public String toString() {
		return "Memetic Algorithm";
	}
}
