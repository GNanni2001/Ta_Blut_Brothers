package it.unibo.ai.didattica.competition.tablut.heuristics;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;

public class HeuristicsBlack extends Heuristics{

	private boolean late;
	
	public HeuristicsBlack(State state) {
		super(state);
		this.late=false;
	}

	@Override
	public double evaluateState() {
		double eval=0;
		
		if (this.state.getTurn().equalsTurn("BW")) {
			return Double.POSITIVE_INFINITY;
		}
		int [] escapes = this.getKingEscapes(this.state, this.kingPosition(this.state));
		boolean canescape=false;
		for(int i=0; i<4; i++) {
			if(escapes[i]==1) {
				canescape=true;
				break;
			}
		}
		if(canescape) {
			return Double.NEGATIVE_INFINITY;
		}
		
		int[] A = this.kingPosition(this.state);
		int B = this.getTotalAdjacentBlackPawns("B");
		int C = this.getTotalAdjacentWhitePawns("B");
		int D = this.getTotalAdjacentCamps("B");
		int G = this.getPawnsOnBoard(this.state, State.Pawn.BLACK);
		int H = this.getPawnsOnBoard(this.state, State.Pawn.WHITE);
		int distance = Math.abs(A[0]-4) + Math.abs(A[1]-4);
		int quadrant = this.getQuadrant(A);
		//black is one move away from winning
		if (this.kingCanBeCaptured() && this.state.getTurn().equalsTurn("W")) {
			eval+=500;
		}
		if(this.isLateGame()) {
			//Heuristics for the late part of the game
			int zeta=50, eta=20, teta=5;
			double  yota=0.5, kappa=0.1;
			if(quadrant!=0) {
				int E = this.getQuadrantPawns(this.state, Pawn.BLACK, quadrant);
				int F = this.getQuadrantPawns(this.state, Pawn.WHITE, quadrant);
				eval+= eta*(E-F);
			}
			eval+=-zeta*distance+teta*(G-H)+yota*(B-C)-kappa*D;
		}else {
			//Heuristics for the early part of the game
			int alpha=50, beta=2, gamma=5, delta=1; //epsilon=0;
			eval+= alpha*(G-H)+beta*(B-C)-gamma*D+delta*distance;
		}
		
		return eval;
	}
	
	private boolean isLateGame() {
		if(!this.late && this.isKingOnCenter(this.state, this.kingPosition(this.state))) {
			this.late = true;
		}
		return this.late;
	}

}
