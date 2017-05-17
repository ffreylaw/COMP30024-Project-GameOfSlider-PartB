package ai.partB.Minimax;

import java.util.ArrayList;

import aiproj.slider.Move.Direction;

/**
 * Piece class for board
 */
public class Piece { 
	
	private int x, y;
	private State state;
	private ArrayList<Direction> legalMoves;
	
	/**
	 * Initialize a piece
	 * @param x
	 * @param y
	 * @param state
	 */
	public Piece(int x, int y, State state) { 
		this.x = x; 
		this.y = y; 
		this.state = state;
		this.legalMoves = new ArrayList<Direction>();
	}
	
	/**
	 * Get all legal moves of a piece
	 * @return legalMoves
	 */
	public ArrayList<Direction> getLegalMoves() {
		return legalMoves;
	}
	
	/**
	 * Add a legal move
	 * @param d: direction
	 */
	public void addLegalMove(Direction d) {
		this.legalMoves.add(d);
	}
	
	/**
	 * Clear legal moves array
	 */
	public void clearLegalMoves() {
		this.legalMoves.clear();
	}
	
	/**
	 * Get x position
	 * @return x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Get y position
	 * @return y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Get state of a piece
	 * @return state
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * Reset a piece
	 * @param x
	 * @param y
	 * @param state
	 */
	public void reset(int x, int y, State state) { 
		this.x = x; 
		this.y = y; 
		this.state = state;
		this.legalMoves.clear();
	}
	
}
