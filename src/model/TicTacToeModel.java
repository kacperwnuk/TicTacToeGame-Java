package model;

import javafx.util.Pair;

public class TicTacToeModel {

	public enum State{Blank, X, O};
	
	private int moveCount;
	private State turn; 
	
	private State [][]board;
	
	public TicTacToeModel() {
		
		setMoveCount(0);
		setTurn(State.X);
		board = new State[3][3];
		for (int i = 0;i < 3 ;++i) {
			for(int j = 0 ; j < 3; ++j) {
				setMarked(i,j,State.Blank);
			}
		}
	}
	
	
	
	public Pair<Boolean, State> checkGame(int x, int y, State s){
		
		 if(board[x][y] == State.Blank){
	            board[x][y] = s;
	        }
	        moveCount++;

	        //check end conditions

	        //check col
	        for(int i = 0; i < 3; i++){
	            if(board[x][i] != s)
	                break;
	            if(i == 2){
	                System.out.print(s);
	                return new Pair<>(true, s);
	            }
	        }

	        //check row
	        for(int i = 0; i < 3; i++){
	            if(board[i][y] != s)
	                break;
	            if(i == 2){
	            	System.out.print(s);
	            	return new Pair<>(true, s);
	            }
	        }

	        //check diag
	        if(x == y){
	            //we're on a diagonal
	            for(int i = 0; i < 3; i++){
	                if(board[i][i] != s)
	                    break;
	                if(i == 2){
	                	System.out.print(s);
	                	return new Pair<>(true, s);
	                }
	            }
	        }

	        //check anti diag 
	        if(x + y == 2){
	            for(int i = 0; i < 3; i++){
	                if(board[i][(2)-i] != s)
	                    break;
	                if(i == 2){
	                	System.out.print(s);
	                	return new Pair<>(true, s);
	                }
	            }
	        }

	        //check draw
	        if(moveCount == (Math.pow(3, 2))){
	        	System.out.print("draw");
	        	return new Pair<>(true, State.Blank);
	        }
	    

		
		
		return new Pair<>(false,State.Blank);
	}
	
	public void setMarked(int x, int y, State player) {
		board[x][y] = player;
	}
	
	public State isMarked(int x, int y) {
		return board[x][y];
	}
	
	public int getMoveCount() {
		return moveCount;
	}


	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}
	
	public State getTurn() {
		return turn;
	}



	public void setTurn(State turn) {
		this.turn = turn;
	}
	
	
	
	
}
