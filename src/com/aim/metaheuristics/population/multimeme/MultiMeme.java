package com.aim.metaheuristics.population.multimeme;

import com.aim.metaheuristics.population.MemeplexInheritanceMethod;
import com.aim.metaheuristics.population.ParentSelection;
import com.aim.metaheuristics.population.heuristics.BitMutation;
import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.genetics.CrossoverHeuristic;
import uk.ac.nott.cs.aim.satheuristics.genetics.PopulationHeuristic;
import uk.ac.nott.cs.aim.satheuristics.genetics.PopulationReplacement;
import uk.ac.nott.cs.aim.searchmethods.PopulationBasedSearchMethod;

import java.util.Random;

public class MultiMeme extends PopulationBasedSearchMethod {

	/**
	 * The innovation rate setting
	 */
	private final double innovationRate;
	private final int[] optionsPerMeme;
	
	private final CrossoverHeuristic crossover;
	private final BitMutation mutation;
	private final PopulationReplacement replacement;
	private final ParentSelection p1selection;
	private final ParentSelection p2selection;

	private final MemeplexInheritanceMethod inheritance;
	
	/**
	 * The possible local search operators to use.
	 */
	private final PopulationHeuristic[] lss; 
	
	// Constructor used for testing. Please do not remove!
	/**
	 *
	 * @param problem
	 * @param rng
	 * @param populationSize
	 * @param innovationRate
	 * @param crossover
	 * @param mutation
	 * @param replacement
	 * @param p1selection
	 * @param p2selection
	 * @param inheritance
	 * @param lss
	 */
	public MultiMeme(SAT problem, Random rng, int populationSize, double innovationRate,
					 int[] optionsPerMeme, CrossoverHeuristic crossover, BitMutation mutation,
					 PopulationReplacement replacement, ParentSelection p1selection,
					 ParentSelection p2selection, MemeplexInheritanceMethod inheritance,
					 PopulationHeuristic[] lss) {

		super(problem, rng, populationSize);

		this.innovationRate = innovationRate;
		this.optionsPerMeme = optionsPerMeme;
		this.crossover = crossover;
		this.mutation = mutation;
		this.replacement = replacement;
		this.p1selection = p1selection;
		this.p2selection = p2selection;
		this.inheritance = inheritance;
		this.lss = lss;
	}

	/**
	 * Constructor called when using the CUSTOM operator mode.
	 * You need to create each of the components by yourself!
	 * @param problem
	 * @param rng
	 * @param populationSize
	 * @param innovationRate
	 */
	public MultiMeme(SAT problem, Random rng, int populationSize, double innovationRate) {

		this(
				problem,
				rng,
				populationSize,
				innovationRate,
				null,
				null, // TODO - creation of crossover operator
				null, // TODO - creation of mutation operator
				null, // TODO - creation of replacement operator
				null, // TODO - creation of parent selection operator for parent #1
				null, // TODO - creation of parent selection operator for parent #1
				null, // TODO - creation of memeplex inheritance operator
				new PopulationHeuristic[] { // create mapping for local search operators used for meme in meme index 1
						// TODO - create local search heuristics here
				}
		);
	}

	/**
	 * MMA PSEUDOCODE:
	 * 
	 * INPUT: PopulationSize, MaxGenerations, InnovationRate
	 * 
	 * generateInitialPopulation();
	 * FOR 0 -> MaxGenerations
	 * 
	 ####### BEGIN IMPLEMENTING HERE #######
	 *     FOR 0 -> PopulationSize / 2
	 *         select parents using tournament selection with tournament size = 3
	 *         apply crossover to generate offspring
	 *         inherit memeplex using simple inheritance method
	 *         mutate the memes within each memeplex of each child with probability
	 *         		dependent on the innovation rate
	 *         apply mutation to offspring with intensity of mutation set for each solution
	 *         		dependent on its meme option
	 *         apply local search to offspring with choice of operator dependent on
	 *         		each solutions meme option
	 *     ENDFOR
	 *     do population replacement
	 ####### STOP IMPLEMENTING HERE #######
	 * ENDFOR
	 * return s_best;
	 */
	public void runMainLoop() {

		for (int iIterationCount = 0; iIterationCount < POPULATION_SIZE; iIterationCount += 2) {

			// Select unique parents
			int p1Index = p1selection.parentSelection();
			int p2Index = p2selection.parentSelection();
			if ((p1Index == p2Index) && (p2Index == (POPULATION_SIZE - 1))) {
				p2Index = 0;
			} else if (p1Index == p2Index) {
				p2Index += 1;
			}

			// Apply crossover to generate offspring
			int c1Index = POPULATION_SIZE + iIterationCount;
			int c2Index = POPULATION_SIZE + iIterationCount + 1;
			crossover.applyHeuristic(p1Index, p2Index, c1Index, c2Index);

			// Inherit memeplex using simple inheritance method
			inheritance.performMemeticInheritance(p1Index, p2Index, c1Index, c2Index);

			// Mutate the memes for each child
			performMutationOfMemeplex(c1Index);
			performMutationOfMemeplex(c2Index);

			// Apply mutation to offspring with IOM dependent on its meme option
			applyMutationForChildDependentOnMeme(c1Index, 0);
			applyMutationForChildDependentOnMeme(c2Index, 0);

			// Apply local search to offspring dependent on each solution's meme option
			applyLocalSearchForChildDependentOnMeme(c1Index, 1);
			applyLocalSearchForChildDependentOnMeme(c2Index, 1);
		}

		// Do population replacement
		replacement.doReplacement(problem, POPULATION_SIZE);
		
	}


	/**
	 * Applies mutation to the child dependent on its current meme option for mutation.
	 * Mapping of meme option to IOM: IntensityOfMutation <- memeOption;
	 * 
	 * @param childIndex The solution memory index of the child to mutate.
	 * @param memeIndex The meme index used for storing the meme relating to the IOM setting.
	 */
	public void applyMutationForChildDependentOnMeme(int childIndex, int memeIndex) {

		int iom = problem.getMeme(childIndex, memeIndex).getMemeOption();
		mutation.setMutationRate(iom);
		mutation.applyHeuristic(childIndex);

	}
	
	/**
	 * Applies the local search operator to the child as specified by its current meme option.
	 * 
	 * @param childIndex The solution memory index of the child to mutate.
	 * @param memeIndex The meme index used for storing the meme relating to the local search operator setting.
	 */
	public void applyLocalSearchForChildDependentOnMeme(int childIndex, int memeIndex) {

		int lsIndex = problem.getMeme(childIndex, memeIndex).getMemeOption();
		lss[lsIndex].applyHeuristic(childIndex);

	}
	
	/**
	 * Applies mutation to each meme within the memeplex of the specified solution with
	 * probability dependent on the innovation rate.
	 * 
	 * HINT: mutation does not mean bit flip; it only means in this case 
	 * 		that you should MODIFY the current value of the meme option
	 * 		subject to the above definition.
	 * 
	 * @param solutionIndex The solution memory index of the solution to mutate the memeplex of.
	 */
	public void performMutationOfMemeplex(int solutionIndex) {

		for (int i = 0; i < problem.getNumberOfMemes(); i++) {
			if (rng.nextDouble() < innovationRate) {
				int optionBound = optionsPerMeme[i];
				problem.getMeme(solutionIndex, i).setMemeOption(rng.nextInt(optionBound));
			}
		}
		
	}
	
	public String toString() {
		return "Multimeme Memetic Algorithm";
	}
}
