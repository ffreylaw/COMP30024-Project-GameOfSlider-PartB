package ai.partB.Minimax;

import java.util.ArrayList;

import aiproj.slider.Move.Direction;

public class Piece { 
	
	private int x, y;
	private State state;
	private ArrayList<Direction> legalMoves;
	
	public Piece(int x, int y, State state) { 
		this.x = x; 
		this.y = y; 
		this.state = state;
		this.legalMoves = new ArrayList<Direction>();
	}
	
	public ArrayList<Direction> getLegalMoves() {
		return legalMoves;
	}

	public void addLegalMove(Direction d) {
		this.legalMoves.add(d);
	}
	
	public void clearLegalMoves() {
		this.legalMoves.clear();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public State getState() {
		return state;
	}

	public void reset(int x, int y, State state) { 
		this.x = x; 
		this.y = y; 
		this.state = state;
		this.legalMoves.clear();
	}
	
}
