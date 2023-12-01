package it.unibo.ai.didattica.competition.tablut.heuristics;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class Heuristics {

    protected State state;

    // Matrix of camps
    private final int[][] camps = {
            		{0,3}, {0,4}, {0,5},
            			   {1,4},

        {3,0},                                 {3,8},
        {4,0}, {4,1},      {4,4},       {4,7}, {4,8},
        {5,0},                                 {5,8},

            			   {7,4},
            		{8,3}, {8,4}, {8,5}
};

    public Heuristics(State state) {
        this.state = state;
    }

    public abstract double evaluateState();
    
    /**
     * @return true if a position is occupied, false otherwise
     */
    public boolean isPositionOccupied(State state,int[] position){
        return !state.getPawn(position[0], position[1]).equals(State.Pawn.EMPTY);
    }
    
    public boolean isPositionOccupiedBlack(State state,int[] position){
        return state.getPawn(position[0], position[1]).equals(State.Pawn.BLACK);
    }
    
    public boolean isPositionOccupiedWhite(State state,int[] position){
        return state.getPawn(position[0], position[1]).equals(State.Pawn.WHITE);
    }

    public boolean isPositionOccupiedKing(State state,int[] position){
        return state.getPawn(position[0], position[1]).equals(State.Pawn.KING);
    }
    
    public boolean isPositionThrone(State state,int[] position){
        return state.getPawn(position[0], position[1]).equals(State.Pawn.THRONE);
    }

    /**
     * @return the position of the King
     */
    public int[] kingPosition(State state) {
        int[] pos = new int[2];

        State.Pawn[][] board = state.getBoard();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (state.getPawn(i, j).equalsPawn("K")) {
                    pos[0] = i;
                    pos[1] = j;

                    return pos;
                }
            }
        }
        return pos;
    }

    /**
     * @return true if king is on throne, false otherwise
     */
    public boolean isKingOnThrone(State state){
        return state.getPawn(4, 4).equalsPawn("K");
    }

    /**
     * @return the number of adjacent pawns that are target(B, W or K)
     */
    public int checkAdjacentPawns(State state, int[] pos, String target) {
        int count = 0;
        int i=pos[0], j=pos[1];
        State.Pawn[][] board = state.getBoard();
        
        if(i>0 && i<8 && j>0 && j<8) {
        
        	if (board[i-1][j].equalsPawn(target))
        		count++;
        	if (board[i+1][j].equalsPawn(target))
        		count++;
        	if (board[i][j-1].equalsPawn(target))
        		count++;
        	if (board[i][j+1].equalsPawn(target))
        		count++;
        }
        
        if(i==0 && j!=0 && j!=8) {
        	if (board[i+1][j].equalsPawn(target))
        		count++;
        	if (board[i][j-1].equalsPawn(target))
        		count++;
        	if (board[i][j+1].equalsPawn(target))
        		count++;
        }
        
        if(i==8 && j!=0 && j!=8) {
        	if (board[i-1][j].equalsPawn(target))
        		count++;
        	if (board[i][j-1].equalsPawn(target))
        		count++;
        	if (board[i][j+1].equalsPawn(target))
        		count++;
        }
        
        if(j==0 && i!=0 && i!=8) {
        	if (board[i-1][j].equalsPawn(target))
        		count++;
        	if (board[i+1][j].equalsPawn(target))
        		count++;
        	if (board[i][j+1].equalsPawn(target))
        		count++;
        }
        
        if(j==8 && i!=0 && i!=8) {
        	if (board[i-1][j].equalsPawn(target))
        		count++;
        	if (board[i+1][j].equalsPawn(target))
        		count++;
        	if (board[i][j-1].equalsPawn(target))
        		count++;
        }
        
        if(i==0 && j==0) {
        	if (board[i+1][j].equalsPawn(target))
        		count++;
        	if (board[i][j+1].equalsPawn(target))
        		count++;
        }
        
        if(i==8 && j==0) {
        	if (board[i-1][j].equalsPawn(target))
        		count++;
        	if (board[i][j+1].equalsPawn(target))
        		count++;
        }
        
        if(i==0 && j==8) {
        	if (board[i+1][j].equalsPawn(target))
        		count++;
        	if (board[i][j-1].equalsPawn(target))
        		count++;
        }
        
        if(i==8 && j==8) {
        	if (board[i-1][j].equalsPawn(target))
        		count++;
        	if (board[i][j-1].equalsPawn(target))
        		count++;
        }

        
        
        
        
        return count;
    }

    /**
     * @return the adjacent positions occupied by target
     */
    protected List<int[]> adjacentPositionsOccupied(State state,int[] position, String target){
        List<int[]> occupiedPositions = new ArrayList<int[]>();
        int[] pos = new int[2];

        State.Pawn[][] board = state.getBoard();
        if (board[position[0]-1][position[1]].equalsPawn(target)) {
            pos[0] = position[0] - 1;
            pos[1] = position[1];
            occupiedPositions.add(pos);
        }
        if (board[position[0]+1][position[1]].equalsPawn(target)) {
            pos[0] = position[0] + 1;
            pos[1] = position[1];
            occupiedPositions.add(pos);
        }
        if (board[position[0]][position[1]-1].equalsPawn(target)) {
            pos[0] = position[0];
            pos[1] = position[1] - 1;
            occupiedPositions.add(pos);
        }
        if (board[position[0]][position[1]+1].equalsPawn(target)) {
            pos[0] = position[0];
            pos[1] = position[1] + 1;
            occupiedPositions.add(pos);
        }

        return occupiedPositions;
    }
    
    /**
     * @return if there's an adjacent camp
     */
    // Potrebbe essere boolean
    public int checkAdjacentCamp(int[] pos) {
        int count = 0;
        List<int[]> possibleCamps = Arrays.asList(camps);
        
        int[] posToCheck = new int[] {pos[0]-1, pos[1]};
        
        if (possibleCamps.contains(posToCheck))
            count++;
        posToCheck[0] = pos[0];
        posToCheck[1] = pos[1]+1;
        if (possibleCamps.contains(posToCheck))
            count++;
        posToCheck[0] = pos[0]+1;
        posToCheck[1] = pos[1];
        if (possibleCamps.contains(posToCheck))
            count++;
        posToCheck[0] = pos[0];
        posToCheck[1] = pos[1]-1;
        if (possibleCamps.contains(posToCheck))
            count++;
        return count;
    }

    /**
     * @return true if king is adjacent
     */
    protected boolean checkAdjacentKing(State state, int[] position) {
        return checkAdjacentPawns(state, position, "K") > 0;
    }

    /**
     * @return true if king is on a center tile
     */
    public boolean isKingOnCenter(State state,int[] kingPosition) {
        return (kingPosition[0] > 2 && kingPosition[0] < 6 &&
        		kingPosition[1] > 2 && kingPosition[1] < 6 &&
        		!(kingPosition[0]==3 && kingPosition[1]==3) &&
        		!(kingPosition[0]==3 && kingPosition[1]==5) &&
        		!(kingPosition[0]==5 && kingPosition[1]==3) &&
        		!(kingPosition[0]==5 && kingPosition[1]==5));
    }

    /**
     * @return number of free columns from given position [ up, down ]
     */
    public int[] countFreeColumn(State state,int[] position){
        //System.out.println("Sono Dentro!");
    	int row = position[0];
        int column = position[1];
        int[] currentPosition = new int[2];
        int[] freeWays = new int[2];

        freeWays[0] = 1;
        freeWays[1] = 1;

        currentPosition[1]=column;
        // upside
        for(int i=row-1; i>=0; i--) {
            currentPosition[0]=i;
            if (isPositionOccupied(state, currentPosition) || Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, currentPosition))){
                freeWays[0] = 0;
                break;
            }
        }

        // downside
        for(int i=row+1; i<=8; i++) {
            currentPosition[0]=i;
            if (isPositionOccupied(state, currentPosition) || Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, currentPosition))) {
                freeWays[1] = 0;
                break;
            }
        }

        return freeWays;
    }
    
    /**
     * @return free rows from given position [ left, right ]
     */
    public int[] countFreeRow(State state, int[] position) {
        int row = position[0];
        int column = position[1];
        int[] currentPosition = new int[2];
        int[] freeWays = new int[2];

        freeWays[0] = 1;
        freeWays[1] = 1;

        currentPosition[0] = row;
        // left side
        for (int i = column-1; i >= 0; i--) {
            currentPosition[1] = i;
            if (isPositionOccupied(state, currentPosition) || Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, currentPosition))){
                freeWays[0] = 0;
                break;
            }
        }

        // right side
        for (int i = column+1; i <= 8; i++) {
            currentPosition[1] = i;
            if (isPositionOccupied(state, currentPosition) || Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, currentPosition))) {
                freeWays[1] = 0;
                break;
            }
        }

        return freeWays;
    }
    
    /**
     * @return the escapes which king can reach in the order up, down, left, right
     */
    public int[] getKingEscapes(State state, int[] kingPosition) {
        int[] escapes = new int[4];

        if (!isKingOnCenter(state, kingPosition)) {
            if ((!(kingPosition[1] > 2 && kingPosition[1] < 6)) && (!(kingPosition[0] > 2 && kingPosition[0] < 6))) {
                int[] tempV = countFreeColumn(state, kingPosition);
                int[] tempH = countFreeRow(state, kingPosition);
                escapes[0] = tempV[0];
                escapes[1] = tempV[1];
                escapes[2] = tempH[0];
                escapes[3] = tempH[1];
            }
            if ((kingPosition[1] > 2 && kingPosition[1] < 6)) {
                int[] tempH = countFreeRow(state, kingPosition);
                escapes[2] = tempH[0];
                escapes[3] = tempH[1];
            }
            if ((kingPosition[0] > 2 && kingPosition[0] < 6)) {
                int[] tempV = countFreeColumn(state, kingPosition);
                escapes[0] = tempV[0];
                escapes[1] = tempV[1];
            }
            return escapes;
        }

        return escapes;
    }

    /**
     * @return number of positions needed to eat king in the current state
     */
    public int getNumbToEatKing(State state){
        int[] kingPosition = kingPosition(state);

        if (kingPosition[0] == 4 && kingPosition[1] == 4){
            return 4;
        } else if (isKingOnCenter(state, kingPosition)) {
            return 3;
        } else if (checkAdjacentCamp(kingPosition)>0) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * @return number of Target pawns in given quadrant
     */
    public int getQuadrantPawns(State state, State.Pawn target, int quadrant) {
        int[] rowRange = new int[2];
        int[] columnRange = new int[2];
        int count = 0;

        if (quadrant == 1 || quadrant == 2) {
            rowRange[0] = 0;
            rowRange[1] = 3;
        } else {
            rowRange[0] = 5;
            rowRange[1] = 8;
        }

        if(quadrant == 1 || quadrant == 3) {
            columnRange[0] = 0;
            columnRange[1] = 3;
        } else {
            columnRange[0] = 5;
            columnRange[1] = 8;
        }

        for(int i = rowRange[0]; i <= rowRange[1]; i++) {
            for(int j = columnRange[0] ; j <= columnRange[1]; j++) {
                if(state.getBoard()[i][j].equalsPawn(target.toString()))
                    count++;
            }
        }

        return count;
    }
    
    /**
     * @return the number of pawn of target type in the entire board
     */

     public int getPawnsOnBoard(State state, State.Pawn target){
        int i,j, count=0;
        for (i = 0; i < state.getBoard().length; i++){
            for (j = 0; j < state.getBoard()[i].length; j++){
                if(state.getBoard()[i][j].equalsPawn(target.toString())){
                    count++;
                }
            }
        }

        return count;
     }
	/*
	* @return true if position can be reached by at least one black pawn
	*/

    public boolean reachableByBlack(int[] position){
        int k;
        int[] check = new int[2];
        //Check if there's a black pawn upward
        check[1] = position[1];
        for(k=position[0]; k>=0; k--){
            check[0]=k;
            if(this.isPositionOccupiedWhite(this.state, check) || this.isPositionOccupiedKing(this.state, check)){
                break;
            }
            
            if(this.isPositionOccupiedBlack(this.state, check)){
                return true;
            }
        }
        //Check if there's a black pawn downward
        check[1] = position[1];
        for(k=position[0]; k<9; k++){
            check[0]=k;
            if(this.isPositionOccupiedWhite(this.state, check) || this.isPositionOccupiedKing(this.state, check)){
                break;
            }
            if(this.isPositionOccupiedBlack(this.state, check)){
                return true;
            }
        }
        //Check if there's a black pawn left
        check[0] = position[0];
        for(k=position[1]; k>=0; k--){
            check[1]=k;
            if(this.isPositionOccupiedWhite(this.state, check) || this.isPositionOccupiedKing(this.state, check)){
                break;
            }
            if(this.isPositionOccupiedBlack(this.state, check)){
                return true;
            }
        }
        //Check if there's a black pawn right
        check[0] = position[0];
        for(k=position[1]; k<9; k++){
            check[1]=k;
            if(this.isPositionOccupiedWhite(this.state, check) || this.isPositionOccupiedKing(this.state, check)){
                break;
            }
            if(this.isPositionOccupiedBlack(this.state, check)){
                return true;
            }
        }
        
        return false;
    }

    	/*
		* @Returns true if the king can be captured within one move from this state
		*/ 
     public boolean kingCanBeCaptured() {
    	 int []  kingPos = this.kingPosition(state);
		 
    	 int []  up;
    	 if(kingPos[0]!=0) {
    	 up = new int[2];
		 up[0]=kingPos[0]-1;
		 up[1]=kingPos[1];
    	 }else up=null;
    	 
    	 int []  down;
    	 if(kingPos[0]!=8) {
		 down = new int[2];
		 down[0]=kingPos[0]+1;
		 down[1]=kingPos[1];
    	 }else down= null;
		 
    	 int [] left;
		 if(kingPos[1]!=0) {
    	 left = new int[2];
		 left[0]=kingPos[0];
		 left[1]=kingPos[1]-1;
		 }else left= null;
		 
		 int [] right;
		 if(kingPos[1]!=8) {
		 right = new int[2];
		 right[0]=kingPos[0];
		 right[1]=kingPos[1]+1;
		 }else right = null;
		 
    	 if(this.getNumbToEatKing(this.state)==4) {
            //We need 4 pawns to capture the king
    	
    		 if(this.checkAdjacentPawns(this.state, this.kingPosition(this.state), "B")==3) {
                //We know that 3 of the adjacent positions are occupied
                 
                 if(up!=null && !this.isPositionOccupiedBlack(this.state, up) && this.reachableByBlack(up)) {
    				 //If the upward position is free and can be reached by black returns true
                     return true;
    			 }
    			 if(down!=null && !this.isPositionOccupiedBlack(this.state, down) && this.reachableByBlack(down)) {
    				 //If the downward position is free and can be reached by black returns true
                     return true;
    			 }
    			 if(right!=null &&!this.isPositionOccupiedBlack(this.state, right) && this.reachableByBlack(right)) {
    				  //If the right position is free and can be reached by black returns true
                     return true;
    			 }
    			 if(left!=null &&!this.isPositionOccupiedBlack(this.state, left) && this.reachableByBlack(left)) {
    				  //If the left position is free and can be reached by black returns true
                     return true;
    			 }
    		 }else{
                //Less than 3 adjacent positions are occupied but 4 are needed to capture king
                return false;
             }
    	 }
    	 if(this.getNumbToEatKing(this.state)==3) {
            //We need three pawns to capture the king, i.e. the king is near the throne
    		
    		 if(this.checkAdjacentPawns(this.state, this.kingPosition(this.state), "B")>=2) {
                 //We know that 2 of the adjacent positions are occupied
                  
                  if(!this.isPositionOccupiedBlack(this.state, up) && this.isPositionThrone(state, down) && this.reachableByBlack(up)) {
     				 //If the downward is the throne and the upward position is free and can be reached by black returns true
                      return true;
     			 }
     			 if(!this.isPositionOccupiedBlack(this.state, down) && this.isPositionThrone(state, up) && this.reachableByBlack(down)) {
     				//If the upward is the throne and the downward position is free and can be reached by black returns true
                      return true;
     			 }
     			 if(!this.isPositionOccupiedBlack(this.state, right) && this.isPositionThrone(state, left) && this.reachableByBlack(right)) {
     				//If the left is the throne and the right position is free and can be reached by black returns true
                      return true;
     			 }
     			 if(!this.isPositionOccupiedBlack(this.state, left) && this.isPositionThrone(state, right) && this.reachableByBlack(left)) {
     				//If the right is the throne and the left position is free and can be reached by black returns true
                      return true;
     			 }
     		 }else{
                 //Less than 2 adjacent positions are occupied but 3 are needed to capture king
                 return false;
              }
    		 	
    	 }
    	 if(this.getNumbToEatKing(this.state)==2) {
    		 //We need two pawns to capture the king, i.e. the king is far from throne and camps
    		 
    		 if(this.checkAdjacentPawns(this.state, this.kingPosition(this.state), "B")>=1) {
                 //We know that at least 1 of the adjacent positions is occupied
                  
                  if(down!=null && up!=null && !this.isPositionOccupiedBlack(this.state, up) && this.reachableByBlack(down)) {
     				 //If the upward position is occupied by black and the downward can be reached by black return true
                      return true;
     			 }
                  if(up!=null && down!=null &&!this.isPositionOccupiedBlack(this.state, down) && this.reachableByBlack(up)) {
      				 //If the downward position is occupied by black and the upward can be reached by black return true
                       return true;
      			 }
                  if(left!=null && right!=null &&!this.isPositionOccupiedBlack(this.state, right) && this.reachableByBlack(left)) {
      				 //If the right position is occupied by black and the left can be reached by black return true
                       return true;
      			 }
                  if(right!=null && left!=null &&!this.isPositionOccupiedBlack(this.state, left) && this.reachableByBlack(right)) {
      				 //If the left position is occupied by black and the right can be reached by black return true
                       return true;
      			 }
     		 }else{
                 //Less than 1 adjacent positions are occupied but 2 are needed to capture king
                 return false;
              }
    	 }
    	 if(this.getNumbToEatKing(this.state)==1) {
            //We need one pawn to capture the king, i.e. the king is far from the throne but near a camp
    		 List<int[]> listcamp = Arrays.asList(this.camps);
    		 
    		 if(down!=null &&listcamp.contains(up) && this.reachableByBlack(down)) {
 				 //If the upward position is a camp and the downward can be reached by black return true
                  return true;
 			 }
    		 if(up!=null &&listcamp.contains(down) && this.reachableByBlack(up)) {
 				 //If the downward position is a camp and the upward can be reached by black return true
                  return true;
 			 }
    		 if(left!=null &&listcamp.contains(right) && this.reachableByBlack(left)) {
 				 //If the right position is a camp and the left can be reached by black return true
                  return true;
 			 }
    		 if(right!=null &&listcamp.contains(left) && this.reachableByBlack(right)) {
 				 //If the left position is a camp and the right can be reached by black return true
                  return true;
 			 }
		 }
    	 
    	 //There is no way the king can be captured within the next move
    	 return false;
    	 
     }

     
     /*
		* @Returns the total number of white pawns adjacent to all the pawns of type target
		*/ 
     public int getTotalAdjacentWhitePawns(String target) {
    	 int tot=0, i, j;
    	 int [] pos = new int[2];
    	 for (i=0; i<9; i++) {
    		 for (j=0; j<9; j++) {
    			 if(this.state.getPawn(i, j).toString().compareTo(target)==0) {
    				 pos[0]=i;
    				 pos[1]=j;
    				 tot += this.checkAdjacentPawns(this.state, pos, Pawn.WHITE.toString());
    			 }
    		 }
    	 }
    	 return tot;
     }
     
     
     /*
		* @Returns the total number of black pawns adjacent to all the pawns of type target
		*/ 
  public int getTotalAdjacentBlackPawns(String target) {
 	 int tot=0, i, j;
 	 int [] pos = new int[2];
 	 for (i=0; i<9; i++) {
 		 for (j=0; j<9; j++) {
 			 if(this.state.getPawn(i, j).toString().compareTo(target)==0) {
 				 pos[0]=i;
 				 pos[1]=j;
 				 tot += this.checkAdjacentPawns(this.state, pos, Pawn.BLACK.toString());
 			 }
 		 }
 	 }
 	 return tot;
  }
     
     /*
		* @Returns the total number of camps adjacent to all the pawns of type target
		*/ 
     
     public int getTotalAdjacentCamps(String target) {
    	 int tot=0, i, j;
    	 int [] pos = new int[2];
    	 for (i=0; i<9; i++) {
    		 for (j=0; j<9; j++) {
    			 if(this.state.getPawn(i, j).toString().compareTo(target)==0) {
    				 pos[0]=i;
    				 pos[1]=j;
    				 tot += this.checkAdjacentCamp(pos);
    			 }
    		 }
    	 }
    	 return tot;
     }
     
     public int getQuadrant(int[] pos) {
    	 if(pos[0]>4) {
    		 if(pos[1]>4) {
    			 return 4;
    		 }
    		 else if (pos[1]<4) {
    			 return 3;
    		 }
    	 }
    	 else {
    		 if(pos[1]>4) {
    			 return 2;
    		 }
    		 else if (pos[1]<4) {
    			 return 1;
    		 }
    	 }
    	 return 0;
    	 
     }
}
