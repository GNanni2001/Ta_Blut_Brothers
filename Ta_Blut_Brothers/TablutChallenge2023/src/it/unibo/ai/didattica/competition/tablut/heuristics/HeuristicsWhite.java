package it.unibo.ai.didattica.competition.tablut.heuristics;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

public class HeuristicsWhite extends Heuristics{

	private boolean late;
	
	public HeuristicsWhite(State state) {
		super(state);
		this.late=false;
	}

	@Override
	public double evaluateState() {
		double eval=0;
		
		if (this.state.getTurn().equalsTurn("WW")) {
			return Double.POSITIVE_INFINITY;
		}
		
		if(this.kingCanBeCaptured() && this.state.getTurn().compareTo(Turn.BLACK)==0) {
			return Double.NEGATIVE_INFINITY;
		}
		
		if(!late) {
			this.isLateGame();
		}
		//Prepare relevant values A, B, C, (D), G, H 
		int[] A = this.kingPosition(this.state);
		int B = this.getTotalAdjacentBlackPawns("W");
		int C = this.getTotalAdjacentWhitePawns("W");
		int D = this.getTotalAdjacentCamps("W");
		int G = this.getPawnsOnBoard(this.state, State.Pawn.BLACK);
		int H = this.getPawnsOnBoard(this.state, State.Pawn.WHITE);
		int distance = Math.abs(A[0]-4) + Math.abs(A[1]-4);
		int quadrant = this.getQuadrant(A);
		
		int [] escapes = this.getKingEscapes(this.state, this.kingPosition(this.state));
		boolean canescape=false;
		for(int i=0; i<4; i++) {
			if(escapes[i]==1) {
				canescape=true;
				break;
			}
		}
		//white is one move away from winning
		if(canescape && this.state.getTurn().equalsTurn("B")) {
			eval+=500;
		}
		
		if(this.isLateGame()) {
			//Heuristics for the late part of the game
			int zeta=50, eta=5, theta=5;
			double iota=0.5, kappa=0.1;
			if(quadrant!=0) {
				int E = this.getQuadrantPawns(this.state, Pawn.BLACK, quadrant);
				int F = this.getQuadrantPawns(this.state, Pawn.WHITE, quadrant);
				eval+= zeta*(F-E);
			}
			eval += eta*distance+ theta*(H-G)+ iota*(C-B)-kappa*D;
		}else {
			//Heuristics for the early part of the game
			int alpha=50, beta=5, gamma=5, delta=1;// epsilon=0;
			
			eval = alpha*(H-G) + beta*(C-B) - gamma*D - delta*distance;
			
		}
		
		return eval;
	}
	
	private boolean isLateGame() {
		int blackPawns = this.getPawnsOnBoard(this.state, Pawn.BLACK);
		int whitePawns = this.getPawnsOnBoard(this.state, Pawn.WHITE);
		
		if(whitePawns>=3/2*blackPawns) {
			this.late = true;
		}
		return this.late;
	}

}
