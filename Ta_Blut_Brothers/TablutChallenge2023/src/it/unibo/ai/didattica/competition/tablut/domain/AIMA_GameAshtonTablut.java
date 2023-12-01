package it.unibo.ai.didattica.competition.tablut.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import aima.core.search.adversarial.Game;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
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

public class AIMA_GameAshtonTablut extends GameAshtonTablut implements Game<State, Action, State.Turn>{

	public AIMA_GameAshtonTablut(State state, int repeated_moves_allowed, int cache_size, String logs_folder,
			String whiteName, String blackName) {
		super(state, repeated_moves_allowed, cache_size, logs_folder, whiteName, blackName);
		// TODO Auto-generated constructor stub
	}
	
	public AIMA_GameAshtonTablut( int repeated_moves_allowed, int cache_size, String logs_folder,
			String whiteName, String blackName) {
		super(repeated_moves_allowed, cache_size, logs_folder, whiteName, blackName);
		// TODO Auto-generated constructor stub
	}

	
	
	@Override
	public List<Action> getActions(State state) {
		
		Turn t = state.getTurn();
		int i,j,k;
		String from, to;
		List<Action> actions = new ArrayList<>();
		
		//Cylcing for all cells to find movable pieces
		for(i=0; i<9/*state.getBoard().length*/; i++) {
			for (j=0; j<9/*state.getBoard()[i].length*/; j++) {
				Pawn p = state.getPawn(i, j);
				if (p.toString().compareTo(t.toString())==0 || (p.toString().equals("K") && t.toString().equals("W"))) {
					String columns = "ABCDEFGHI";
					Character c = columns.charAt(j);
					Integer n = i+1;
					from=c.toString() + n.toString(); 
					
					//Checking north
					for(k=i-1; k>=0; k--) {
						Action a=null;
						Character c1 = columns.charAt(j);
						Integer n1 = k+1;
						to=c1.toString() + n1.toString();
						
						try {
							a = new Action(from, to, t);
						}catch (IOException IOE) {
							IOE.printStackTrace();
							System.out.println("Non so come sia potuto succedere");
						}
						
						if(a!=null) {
							try {
								this.checkMove(state, a);
								actions.add(a);
							}catch(DiagonalException de){
								de.printStackTrace();
							/*}catch(StopException se) {
								se.printStackTrace();*/
							}catch(Exception e) {
								//Nothing happens
							}
						}
					}
					
					//Checking east
					for(k=j+1;k<state.getBoard()[i].length; k++) {
						Action a=null;
						Character c1 = columns.charAt(k);
						Integer n1 = i+1;
						to=c1.toString() + n1.toString();
						
						try {
							a = new Action(from, to, t);
						}catch (IOException IOE) {
							IOE.printStackTrace();
							System.out.println("Non so come sia potuto succedere");
						}
						
						if(a!=null) {
							try {
								this.checkMove(state, a);
								actions.add(a);
							}catch(DiagonalException de){
								de.printStackTrace();
							/*}catch(StopException se) {
								se.printStackTrace();*/
							}catch(Exception e) {
								//Nothing happens
							}
						}
					}
					
					//Checking south
					for(k=i+1;k<state.getBoard().length; k++) {
						Action a=null;
						Character c1 = columns.charAt(j);
						Integer n1 = k+1;
						to=c1.toString() + n1.toString();
						
						try {
							a = new Action(from, to, t);
						}catch (IOException IOE) {
							IOE.printStackTrace();
							System.out.println("Non so come sia potuto succedere");
						}
						
						if(a!=null) {
							try {
								this.checkMove(state, a);
								actions.add(a);
							}catch(DiagonalException de){
								de.printStackTrace();
							/*}catch(StopException se) {
								se.printStackTrace();*/
							}catch(Exception e) {
								//Nothing happens
							}
						}
					}
					
					//checking west
					for(k=j-1;k>=0; k--) {
						Action a=null;
						Character c1 = columns.charAt(k);
						Integer n1 = i+1;
						to=c1.toString() + n1.toString();
						
						try {
							a = new Action(from, to, t);
						}catch (IOException IOE) {
							IOE.printStackTrace();
							System.out.println("Non so come sia potuto succedere");
						}
						
						if(a!=null) {
							try {
								this.checkMove(state, a);
								actions.add(a);
							}catch(DiagonalException de){
								de.printStackTrace();
							/*}catch(StopException se) {
								se.printStackTrace();*/
							}catch(Exception e) {
								//Nothing happens
							}
						}
					}
				}
			}
		}
		
		return actions;
	}

	@Override
	public Turn getPlayer(State state) {
		return state.getTurn();
	}

	@Override
	public State getResult(State state, Action action) {
		// Move the pawn
		state = this.movePawn(state.clone(), action);

		// Check the state for any capture
		if (state.getTurn().equalsTurn("W"))
			state = this.checkCaptureBlack(state, action);
		else if (state.getTurn().equalsTurn("B"))
			state = this.checkCaptureWhite(state, action);
		
		return state;
	}

	@Override
	public double getUtility(State state, Turn turn) {		
		Heuristics heuristics = turn.equals(State.Turn.WHITE) ? new HeuristicsWhite(state) : new HeuristicsBlack(state);
		return heuristics.evaluateState();
	}

	@Override
	public boolean isTerminal(State state) {
		return state.getTurn().equals(State.Turn.BLACKWIN) 
				|| state.getTurn().equals(State.Turn.WHITEWIN) 
				|| state.getTurn().equals(State.Turn.DRAW);
	}
	
	// not used
	@Override
	public State getInitialState() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// not used
	@Override
	public Turn[] getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public State checkMove(State state, Action a)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {
		State newState=new StateTablut();

		//this.loggGame.fine(a.toString());
		// controllo la mossa
		if (a.getTo().length() != 2 || a.getFrom().length() != 2) {
			//this.loggGame.warning("Formato mossa errato");
			throw new ActionException(a);
		}
		int columnFrom = a.getColumnFrom();
		int columnTo = a.getColumnTo();
		int rowFrom = a.getRowFrom();
		int rowTo = a.getRowTo();

		// controllo se sono fuori dal tabellone
		if (columnFrom > state.getBoard().length - 1 || rowFrom > state.getBoard().length - 1
				|| rowTo > state.getBoard().length - 1 || columnTo > state.getBoard().length - 1 || columnFrom < 0
				|| rowFrom < 0 || rowTo < 0 || columnTo < 0) {
			//this.loggGame.warning("Mossa fuori tabellone");
			throw new BoardException(a);
		}

		// controllo che non vada sul trono
		if (state.getPawn(rowTo, columnTo).equalsPawn(State.Pawn.THRONE.toString())) {
			//this.loggGame.warning("Mossa sul trono");
			throw new ThroneException(a);
		}

		// controllo la casella di arrivo
		if (!state.getPawn(rowTo, columnTo).equalsPawn(State.Pawn.EMPTY.toString())) {
			//this.loggGame.warning("Mossa sopra una casella occupata");
			throw new OccupitedException(a);
		}
		if (this.citadels.contains(state.getBox(rowTo, columnTo))
				&& !this.citadels.contains(state.getBox(rowFrom, columnFrom))) {
			//this.loggGame.warning("Mossa che arriva sopra una citadel");
			throw new CitadelException(a);
		}
		if (this.citadels.contains(state.getBox(rowTo, columnTo))
				&& this.citadels.contains(state.getBox(rowFrom, columnFrom))) {
			if (rowFrom == rowTo) {
				if (columnFrom - columnTo > 5 || columnFrom - columnTo < -5) {
					//this.loggGame.warning("Mossa che arriva sopra una citadel");
					throw new CitadelException(a);
				}
			} else {
				if (rowFrom - rowTo > 5 || rowFrom - rowTo < -5) {
					//this.loggGame.warning("Mossa che arriva sopra una citadel");
					throw new CitadelException(a);
				}
			}

		}

		// controllo se cerco di stare fermo
		if (rowFrom == rowTo && columnFrom == columnTo) {
			//this.loggGame.warning("Nessuna mossa");
			throw new StopException(a);
		}

		// controllo se sto muovendo una pedina giusta
		if (state.getTurn().equalsTurn(State.Turn.WHITE.toString())) {
			if (!state.getPawn(rowFrom, columnFrom).equalsPawn("W")
					&& !state.getPawn(rowFrom, columnFrom).equalsPawn("K")) {
				//this.loggGame.warning("Giocatore " + a.getTurn() + " cerca di muovere una pedina avversaria");
				throw new PawnException(a);
			}
		}
		if (state.getTurn().equalsTurn(State.Turn.BLACK.toString())) {
			if (!state.getPawn(rowFrom, columnFrom).equalsPawn("B")) {
				//this.loggGame.warning("Giocatore " + a.getTurn() + " cerca di muovere una pedina avversaria");
				throw new PawnException(a);
			}
		}

		// controllo di non muovere in diagonale
		if (rowFrom != rowTo && columnFrom != columnTo) {
			//this.loggGame.warning("Mossa in diagonale");
			throw new DiagonalException(a);
		}

		// controllo di non scavalcare pedine
		if (rowFrom == rowTo) {
			if (columnFrom > columnTo) {
				for (int i = columnTo; i < columnFrom; i++) {
					if (!state.getPawn(rowFrom, i).equalsPawn(State.Pawn.EMPTY.toString())) {
						if (state.getPawn(rowFrom, i).equalsPawn(State.Pawn.THRONE.toString())) {
							//this.loggGame.warning("Mossa che scavalca il trono");
							throw new ClimbingException(a);
						} else {
							//this.loggGame.warning("Mossa che scavalca una pedina");
							throw new ClimbingException(a);
						}
					}
					if (this.citadels.contains(state.getBox(rowFrom, i))
							&& !this.citadels.contains(state.getBox(a.getRowFrom(), a.getColumnFrom()))) {
						//this.loggGame.warning("Mossa che scavalca una citadel");
						throw new ClimbingCitadelException(a);
					}
				}
			} else {
				for (int i = columnFrom + 1; i <= columnTo; i++) {
					if (!state.getPawn(rowFrom, i).equalsPawn(State.Pawn.EMPTY.toString())) {
						if (state.getPawn(rowFrom, i).equalsPawn(State.Pawn.THRONE.toString())) {
							//this.loggGame.warning("Mossa che scavalca il trono");
							throw new ClimbingException(a);
						} else {
							//this.loggGame.warning("Mossa che scavalca una pedina");
							throw new ClimbingException(a);
						}
					}
					if (this.citadels.contains(state.getBox(rowFrom, i))
							&& !this.citadels.contains(state.getBox(a.getRowFrom(), a.getColumnFrom()))) {
						//this.loggGame.warning("Mossa che scavalca una citadel");
						throw new ClimbingCitadelException(a);
					}
				}
			}
		} else {
			if (rowFrom > rowTo) {
				for (int i = rowTo; i < rowFrom; i++) {
					if (!state.getPawn(i, columnFrom).equalsPawn(State.Pawn.EMPTY.toString())) {
						if (state.getPawn(i, columnFrom).equalsPawn(State.Pawn.THRONE.toString())) {
							//this.loggGame.warning("Mossa che scavalca il trono");
							throw new ClimbingException(a);
						} else {
							//this.loggGame.warning("Mossa che scavalca una pedina");
							throw new ClimbingException(a);
						}
					}
					if (this.citadels.contains(state.getBox(i, columnFrom))
							&& !this.citadels.contains(state.getBox(a.getRowFrom(), a.getColumnFrom()))) {
						//this.loggGame.warning("Mossa che scavalca una citadel");
						throw new ClimbingCitadelException(a);
					}
				}
			} else {
				for (int i = rowFrom + 1; i <= rowTo; i++) {
					if (!state.getPawn(i, columnFrom).equalsPawn(State.Pawn.EMPTY.toString())) {
						if (state.getPawn(i, columnFrom).equalsPawn(State.Pawn.THRONE.toString())) {
							//this.loggGame.warning("Mossa che scavalca il trono");
							throw new ClimbingException(a);
						} else {
							//this.loggGame.warning("Mossa che scavalca una pedina");
							throw new ClimbingException(a);
						}
					}
					if (this.citadels.contains(state.getBox(i, columnFrom))
							&& !this.citadels.contains(state.getBox(a.getRowFrom(), a.getColumnFrom()))) {
						//this.loggGame.warning("Mossa che scavalca una citadel");
						throw new ClimbingCitadelException(a);
					}
				}
			}
		}

		// se sono arrivato qui, muovo la pedina
		newState = this.movePawn(state, a);

		// a questo punto controllo lo stato per eventuali catture
		if (newState.getTurn().equalsTurn("W")) {
			newState = this.checkCaptureBlack(state, a);
		} else if (newState.getTurn().equalsTurn("B")) {
			newState = this.checkCaptureWhite(state, a);
		}

		// if something has been captured, clear cache for draws
		if (this.movesWithutCapturing == 0) {
			this.drawConditions.clear();
			//this.loggGame.fine("Capture! Draw cache cleared!");
		}

		// controllo pareggio
		int trovati = 0;
		for (State s : drawConditions) {

			//System.out.println(s.toString());

			if (s.equals(state)) {
				// DEBUG: //
				// System.out.println("UGUALI:");
				// System.out.println("STATO VECCHIO:\t" + s.toLinearString());
				// System.out.println("STATO NUOVO:\t" +
				// state.toLinearString());

				trovati++;
				if (trovati > repeated_moves_allowed) {
					newState.setTurn(State.Turn.DRAW);
					//this.loggGame.fine("Partita terminata in pareggio per numero di stati ripetuti");
					break;
				}
			} else {
				// DEBUG: //
				// System.out.println("DIVERSI:");
				// System.out.println("STATO VECCHIO:\t" + s.toLinearString());
				// System.out.println("STATO NUOVO:\t" +
				// state.toLinearString());
			}
		}
		if (trovati > 0) {
			//this.loggGame.fine("Equal states found: " + trovati);
		}
		if (cache_size >= 0 && this.drawConditions.size() > cache_size) {
			this.drawConditions.remove(0);
		}
		this.drawConditions.add(newState.clone());

		//this.loggGame.fine("Current draw cache size: " + this.drawConditions.size());

		//this.loggGame.fine("Stato:\n" + state.toString());
		//System.out.println("Stato:\n" + newState.toString());

		return newState;
	}
	
	@Override
	protected State movePawn(State state, Action a) {
		State.Pawn pawn = state.getPawn(a.getRowFrom(), a.getColumnFrom());
		State newState= state.clone();
		State.Pawn[][] newBoard = newState.getBoard();
		
		// State newState = new State();
		//this.loggGame.fine("Movimento pedina");
		// libero il trono o una casella qualunque
		if (a.getColumnFrom() == 4 && a.getRowFrom() == 4) {
			newBoard[a.getRowFrom()][a.getColumnFrom()] = State.Pawn.THRONE;
		} else {
			newBoard[a.getRowFrom()][a.getColumnFrom()] = State.Pawn.EMPTY;
		}

		// metto nel nuovo tabellone la pedina mossa
		newBoard[a.getRowTo()][a.getColumnTo()] = pawn;
		// aggiorno il tabellone
		newState.setBoard(newBoard);
		// cambio il turno
		if (state.getTurn().equalsTurn(State.Turn.WHITE.toString())) {
			newState.setTurn(State.Turn.BLACK);
		} else {
			newState.setTurn(State.Turn.WHITE);
		}

		return newState;
	}
	
	@Override
	protected State checkCaptureWhite(State state, Action a) {
		State newState = state.clone();
		// controllo se mangio a destra
		if (a.getColumnTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("B")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("W")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))
								&& !(a.getColumnTo() + 2 == 8 && a.getRowTo() == 4)
								&& !(a.getColumnTo() + 2 == 4 && a.getRowTo() == 0)
								&& !(a.getColumnTo() + 2 == 4 && a.getRowTo() == 8)
								&& !(a.getColumnTo() + 2 == 0 && a.getRowTo() == 4)))) {
			newState.removePawn(a.getRowTo(), a.getColumnTo() + 1);
			this.movesWithutCapturing = -1;
			//this.loggGame.fine("Pedina nera rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
		}
		// controllo se mangio a sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("B")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("W")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
								&& !(a.getColumnTo() - 2 == 8 && a.getRowTo() == 4)
								&& !(a.getColumnTo() - 2 == 4 && a.getRowTo() == 0)
								&& !(a.getColumnTo() - 2 == 4 && a.getRowTo() == 8)
								&& !(a.getColumnTo() - 2 == 0 && a.getRowTo() == 4)))) {
			newState.removePawn(a.getRowTo(), a.getColumnTo() - 1);
			this.movesWithutCapturing = -1;
			//this.loggGame.fine("Pedina nera rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() - 1));
		}
		// controllo se mangio sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("B")
				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("W")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
								&& !(a.getColumnTo() == 8 && a.getRowTo() - 2 == 4)
								&& !(a.getColumnTo() == 4 && a.getRowTo() - 2 == 0)
								&& !(a.getColumnTo() == 4 && a.getRowTo() - 2 == 8)
								&& !(a.getColumnTo() == 0 && a.getRowTo() - 2 == 4)))) {
			newState.removePawn(a.getRowTo() - 1, a.getColumnTo());
			this.movesWithutCapturing = -1;
			//this.loggGame.fine("Pedina nera rimossa in: " + state.getBox(a.getRowTo() - 1, a.getColumnTo()));
		}
		// controllo se mangio sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("B")
				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("W")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
								&& !(a.getColumnTo() == 8 && a.getRowTo() + 2 == 4)
								&& !(a.getColumnTo() == 4 && a.getRowTo() + 2 == 0)
								&& !(a.getColumnTo() == 4 && a.getRowTo() + 2 == 8)
								&& !(a.getColumnTo() == 0 && a.getRowTo() + 2 == 4)))) {
			newState.removePawn(a.getRowTo() + 1, a.getColumnTo());
			this.movesWithutCapturing = -1;
			//this.loggGame.fine("Pedina nera rimossa in: " + state.getBox(a.getRowTo() + 1, a.getColumnTo()));
		}
		// controllo se ho vinto
		if (a.getRowTo() == 0 || a.getRowTo() == state.getBoard().length - 1 || a.getColumnTo() == 0
				|| a.getColumnTo() == state.getBoard().length - 1) {
			if (state.getPawn(a.getRowTo(), a.getColumnTo()).equalsPawn("K")) {
				newState.setTurn(State.Turn.WHITEWIN);
				//this.loggGame.fine("Bianco vince con re in " + a.getTo());
			}
		}
		// TODO: implement the winning condition of the capture of the last
		// black checker

		this.movesWithutCapturing++;
		return newState;
	}
	
	@Override
	protected State checkCaptureBlack(State state, Action a) {
		
		State newState = state.clone();
		this.checkCaptureBlackPawnRight(newState, a);
		this.checkCaptureBlackPawnLeft(newState, a);
		this.checkCaptureBlackPawnUp(newState, a);
		this.checkCaptureBlackPawnDown(newState, a);
		this.checkCaptureBlackKingRight(newState, a);
		this.checkCaptureBlackKingLeft(newState, a);
		this.checkCaptureBlackKingDown(newState, a);
		this.checkCaptureBlackKingUp(newState, a);

		this.movesWithutCapturing++;
		return newState;
	}
	
	@Override
	protected State checkCaptureBlackPawnRight(State state, Action a) {
		// mangio a destra
		if (a.getColumnTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("W")) {
			if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B")) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				this.movesWithutCapturing = -1;
				//this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
			}
			if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				this.movesWithutCapturing = -1;
				//this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
			}
			if (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				this.movesWithutCapturing = -1;
				//this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 2).equals("e5")) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				this.movesWithutCapturing = -1;
				//this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
			}

		}

		return state;
	}

	@Override
	protected State checkCaptureBlackPawnLeft(State state, Action a) {
		// mangio a sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("W")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
						|| (state.getBox(a.getRowTo(), a.getColumnTo() - 2).equals("e5")))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
			this.movesWithutCapturing = -1;
			//this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() - 1));
		}
		return state;
	}

	@Override
	protected State checkCaptureBlackPawnUp(State state, Action a) {
		// controllo se mangio sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("W")
				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("B")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
						|| (state.getBox(a.getRowTo() - 2, a.getColumnTo()).equals("e5")))) {
			state.removePawn(a.getRowTo() - 1, a.getColumnTo());
			this.movesWithutCapturing = -1;
			//this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo() - 1, a.getColumnTo()));
		}
		return state;
	}

	@Override
	protected State checkCaptureBlackPawnDown(State state, Action a) {
		// controllo se mangio sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("W")
				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("B")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
						|| (state.getBox(a.getRowTo() + 2, a.getColumnTo()).equals("e5")))) {
			state.removePawn(a.getRowTo() + 1, a.getColumnTo());
			this.movesWithutCapturing = -1;
			//this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo() + 1, a.getColumnTo()));
		}
		return state;
	}

	
	

}