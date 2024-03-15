package com.aim.metaheuristics.singlepoint.lateacceptance;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;
import uk.ac.nott.cs.aim.searchmethods.SinglePointSearchMethod;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class LateAcceptance extends SinglePointSearchMethod {

    private final int oLength;
    private final Queue<Double> candidateSolutions;

    /**
     * Creates a search method with a population size of 2; one for the current solution,
     * and one for the backup solution. Creating a copy of the current solution in the
     * backup solution index.
     *
     * @param problem The problem to be solved by the single point-based search method.
     * @param random  The random number generator.
     */
    public LateAcceptance(SAT problem, Random random, int length) {
        super(problem, random);
        this.oLength = length;

        // initialise the queue
        this.candidateSolutions = new LinkedList<>();
        double initialObjectiveValue = problem.getObjectiveFunctionValue
                (SATHeuristic.CURRENT_SOLUTION_INDEX);
        for (int i = 0; i < oLength; i++) {
            candidateSolutions.offer(initialObjectiveValue);
        }
    }

    /**
     * In Late Acceptance, an incumbent solution, s', is accepted iff 
     * f(s_{i}^{'}) <= f(s_{i-L}) where L is the length of a list containing
     * the memory of L previously accepted solution costs.
     * 
     * More details are/will be available in lecture 4 on "Move Acceptance". 
     */
    @Override
    protected void runMainLoop() {
        // Get the objective value of the current solution
//        double currentObjectiveValue = problem.getObjectiveFunctionValue
//                (SATHeuristic.CURRENT_SOLUTION_INDEX);
        // Get the objective value of the compared solution
        double comparedObjectiveValue = candidateSolutions.poll();

        // Perform random bit flip on current solution
        randomBitFlip(problem);

        // Get the objective value of the new solution
        double newObjectiveValue = problem.getObjectiveFunctionValue
                (SATHeuristic.CURRENT_SOLUTION_INDEX);

        // Check whether to accept the new solution
        //candidateSolutions.offer(Math.min(newObjectiveValue, comparedObjectiveValue));
        if (newObjectiveValue <= comparedObjectiveValue) {
            candidateSolutions.offer(newObjectiveValue);
            problem.copySolution(SATHeuristic.CURRENT_SOLUTION_INDEX,
                    SATHeuristic.BACKUP_SOLUTION_INDEX);
        } else {
            candidateSolutions.offer(comparedObjectiveValue);
            problem.copySolution(SATHeuristic.BACKUP_SOLUTION_INDEX,
                    SATHeuristic.CURRENT_SOLUTION_INDEX);
        }
    }

    private void randomBitFlip(SAT problem) {
        // select a random bit in the solution
        int length = problem.getNumberOfVariables();
        int randomIndex = random.nextInt(length);
        // flip the bit
        problem.bitFlip(randomIndex, SATHeuristic.CURRENT_SOLUTION_INDEX);
    }

    public String toString() {
        return "Late Acceptance";
	}
}
