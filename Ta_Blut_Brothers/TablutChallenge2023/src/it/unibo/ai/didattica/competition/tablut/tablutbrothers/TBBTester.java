package it.unibo.ai.didattica.competition.tablut.tablutbrothers;

import java.util.ArrayList;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.AIMA_GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.exceptions.ActionException;
import it.unibo.ai.didattica.competition.tablut.exceptions.BoardException;
import it.unibo.ai.didattica.competition.tablut.exceptions.CitadelException;
import it.unibo.ai.didattica.competition.tablut.exceptions.ClimbingCitadelException;
import it.unibo.ai.didattica.competition.tablut.exceptions.ClimbingException;
import it.unibo.ai.didattica.competition.tablut.exceptions.DiagonalException;
import it.unibo.ai.didattica.competition.tablut.exceptions.OccupitedException;
import it.unibo.ai.didattica.competition.tablut.exceptions.PawnException;
import it.unibo.ai.didattica.competition.tablut.exceptions.StopException;
import it.unibo.ai.didattica.competition.tablut.exceptions.ThroneException;
import it.unibo.ai.didattica.competition.tablut.heuristics.Heuristics;
import it.unibo.ai.didattica.competition.tablut.heuristics.HeuristicsBlack;
import it.unibo.ai.didattica.competition.tablut.heuristics.HeuristicsWhite;

public class TBBTester {
	public static void main (String args[]) {
		
		
		/*
		 * Initial Board
		 * 
		 *  ABCDEFGHI
		 * 1OOOBBBOOO
		 * 2OOOOBOOOO
		 * 3OOOOWOOOO
		 * 4BOOOWOOOB
		 * 5BBWWKWWBB
		 * 6BOOOWOOOB
		 * 7OOOOWOOOO
		 * 8OOOOBOOOO
		 * 9OOOBBBOOO
		 * 
		 * 
		 */
		
		Pawn [][] board= new Pawn[9][9];
		
		//First row
		board[0][0]= Pawn.fromString("O");
		board[0][1]= Pawn.fromString("O");
		board[0][2]= Pawn.fromString("O");
		board[0][3]= Pawn.fromString("B");
		board[0][4]= Pawn.fromString("B");
		board[0][5]= Pawn.fromString("B");
		board[0][6]= Pawn.fromString("O");
		board[0][7]= Pawn.fromString("O");
		board[0][8]= Pawn.fromString("O");
		
		//Second row
		board[1][0]= Pawn.fromString("O");
		board[1][1]= Pawn.fromString("O");
		board[1][2]= Pawn.fromString("O");
		board[1][3]= Pawn.fromString("O");
		board[1][4]= Pawn.fromString("B");
		board[1][5]= Pawn.fromString("O");
		board[1][6]= Pawn.fromString("O");
		board[1][7]= Pawn.fromString("O");
		board[1][8]= Pawn.fromString("O");
		
		//Third row
		board[2][0]= Pawn.fromString("O");
		board[2][1]= Pawn.fromString("O");
		board[2][2]= Pawn.fromString("O");
		board[2][3]= Pawn.fromString("O");
		board[2][4]= Pawn.fromString("W");
		board[2][5]= Pawn.fromString("O");
		board[2][6]= Pawn.fromString("O");
		board[2][7]= Pawn.fromString("O");
		board[2][8]= Pawn.fromString("O");
		
		//Fourth row
		board[3][0]= Pawn.fromString("B");
		board[3][1]= Pawn.fromString("O");
		board[3][2]= Pawn.fromString("O");
		board[3][3]= Pawn.fromString("O");
		board[3][4]= Pawn.fromString("O");
		board[3][5]= Pawn.fromString("O");
		board[3][6]= Pawn.fromString("W");
		board[3][7]= Pawn.fromString("B");
		board[3][8]= Pawn.fromString("B");
		
		//Fifth row
		board[4][0]= Pawn.fromString("B");
		board[4][1]= Pawn.fromString("B");
		board[4][2]= Pawn.fromString("W");
		board[4][3]= Pawn.fromString("W");
		board[4][4]= Pawn.fromString("K");
		board[4][5]= Pawn.fromString("W");
		board[4][6]= Pawn.fromString("W");
		board[4][7]= Pawn.fromString("O");
		board[4][8]= Pawn.fromString("B");
		
		//Sixth row
		board[5][0]= Pawn.fromString("B");
		board[5][1]= Pawn.fromString("O");
		board[5][2]= Pawn.fromString("O");
		board[5][3]= Pawn.fromString("O");
		board[5][4]= Pawn.fromString("W");
		board[5][5]= Pawn.fromString("O");
		board[5][6]= Pawn.fromString("O");
		board[5][7]= Pawn.fromString("O");
		board[5][8]= Pawn.fromString("B");
		
		//Seventh row
		board[6][0]= Pawn.fromString("O");
		board[6][1]= Pawn.fromString("O");
		board[6][2]= Pawn.fromString("O");
		board[6][3]= Pawn.fromString("O");
		board[6][4]= Pawn.fromString("W");
		board[6][5]= Pawn.fromString("O");
		board[6][6]= Pawn.fromString("O");
		board[6][7]= Pawn.fromString("O");
		board[6][8]= Pawn.fromString("O");
		
		//Eighth row
		board[7][0]= Pawn.fromString("O");
		board[7][1]= Pawn.fromString("O");
		board[7][2]= Pawn.fromString("O");
		board[7][3]= Pawn.fromString("O");
		board[7][4]= Pawn.fromString("B");
		board[7][5]= Pawn.fromString("O");
		board[7][6]= Pawn.fromString("O");
		board[7][7]= Pawn.fromString("O");
		board[7][8]= Pawn.fromString("O");
		
		//Ninth row
		board[8][0]= Pawn.fromString("O");
		board[8][1]= Pawn.fromString("O");
		board[8][2]= Pawn.fromString("O");
		board[8][3]= Pawn.fromString("B");
		board[8][4]= Pawn.fromString("B");
		board[8][5]= Pawn.fromString("B");
		board[8][6]= Pawn.fromString("O");
		board[8][7]= Pawn.fromString("O");
		board[8][8]= Pawn.fromString("O");
		
		
		State s = new StateTablut();
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		/*try {
		Action a = new Action("I5", "G5", Turn.BLACK);
		GameAshtonTablut gt =new GameAshtonTablut(s, 3,-1, "no", "no", "no");
		s=gt.checkMove(s, a);
		}catch(Exception e) {
			System.out.println("Really???");
		}*/
		
		Heuristics hw = new HeuristicsWhite(s);
		Heuristics hb = new HeuristicsBlack(s);
		
		AIMA_GameAshtonTablut agat = new AIMA_GameAshtonTablut(s, 3,-1, "no", "no", "no");
		List<Action> actions = agat.getActions(s);
		List<State> states = new ArrayList<>();
		
		int quadrantPawnTest = hw.getQuadrantPawns(s, Pawn.BLACK, 4);
		
		System.out.println("test metodo: " + quadrantPawnTest);
		System.out.println("--------------------------------------------------------"+actions.size());
		
		for(Action a : actions) {
			//System.out.println(a);
			try {
				states.add(agat.checkMove(s, a));
			} catch (BoardException | ActionException | StopException | PawnException | DiagonalException
					| ClimbingException | ThroneException | OccupitedException | ClimbingCitadelException
					| CitadelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(State st : states) {
		long start, finish;
		start = System.currentTimeMillis();
		System.out.println("Heuristics for white player for this board is: " + hw.evaluateState());
		System.out.println("Heuristics for black player for this board is: " + hb.evaluateState());
		finish = System.currentTimeMillis();
		System.out.println("Millis: " + (finish-start) );
		}
	}
	
	
}
