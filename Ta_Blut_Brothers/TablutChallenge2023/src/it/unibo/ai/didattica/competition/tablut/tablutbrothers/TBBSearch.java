package it.unibo.ai.didattica.competition.tablut.tablutbrothers;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;

/**
 * Implementation of AIMA Iterative Deepening MinMax search with Alpha-Beta Pruning.
 * Maximal computation time specified in seconds.
 * This configuration redefines the method eval() using getUtility() method in {@link GameAshtonTablut}.
 *
 * @see aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch
 */
public class TBBSearch extends IterativeDeepeningAlphaBetaSearch<State, Action, State.Turn> {

    public static final String TEAM_NAME = "Ta_Blut_Brothers";
    public static final String PLAYER_NAME = "Bod";

    public TBBSearch(Game<State, Action, State.Turn> game, double utilMin, double utilMax, int time) {
        super(game, utilMin, utilMax, time);
    }

    /**
     * Method that estimates the value for states. This implementation returns the utility value for
     * terminal states and heuristic value for non-terminal states.
     *
     * @param state the current state
     * @param player the player who has to make the next move (turn)
     *
     * @return the score of this state (double)
     */
    @Override
    protected double eval(State state, State.Turn player) {
        super.eval(state, player);

        // Return heuristic value for the given state
        return this.game.getUtility(state, player);
    }

    /**
     * Method controlling the search. It is based on minmax with iterative deepening and tries to make to a good decision in limited time.
     * It is overrided to print metrics.
     *
     * @param state the current state
     *
     * @return the chosen action
     */
    @Override
    public Action makeDecision(State state) {
        Action a = super.makeDecision(state);
        System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME + " dice che ha esplorato " + getMetrics().get(METRICS_NODES_EXPANDED) + " nodi, raggiungendo una profondità di " + getMetrics().get(METRICS_MAX_DEPTH));

        return a;
    }

}