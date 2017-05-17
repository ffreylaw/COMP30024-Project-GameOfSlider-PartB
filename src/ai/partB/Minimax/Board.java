package ai.partB.Minimax;

import java.util.ArrayList;
import java.util.Scanner;

import aiproj.slider.Move;
import aiproj.slider.Move.Direction;

/**
 * Board class for minimax player
 */
public class Board {

	private Piece[][] grid;
	private final int size;

	private ArrayList<Piece> allHPieces;
	private ArrayList<Piece> allVPieces;

	private int numLegalHMoves;
	private int numLegalVMoves;
	
	private Scanner reader;
	
	/**
	 * Initialize a board
	 * @param dimension
	 * @param board
	 */
	public Board(int dimension, String board) {
		reader = new Scanner(board);
		
		// initialize everything
		size = dimension;
		this.grid = new Piece[size][size];
		allHPieces = new ArrayList<Piece>();
		allVPieces = new ArrayList<Piece>();
		
		// setup board
		int j = size - 1;
		while (j >= 0 && reader.hasNextLine()) {
			String line = reader.nextLine();
			String[] splited = line.split(" ");
			for (int i = 0; i < splited.length; i++) {
				set(i, j, splited[i].charAt(0));
			}
			j--;
		}
		
		// calculate legal moves
		numLegalHMoves = 0;
		numLegalVMoves = 0;
		calculateLegalMoves();
	}
	
	/**
	 * Get dimension of the board
	 * @return size
	 */
	public int size() {
		return this.size;
	}
	
	/**
	 * Get the piece of given position
	 * @param x
	 * @param y
	 * @return grid[x][y]
	 */
	public Piece get(int x, int y) {
		return grid[x][y];
	}
	
	/**
	 * Set the board by given piece
	 * @param x
	 * @param y
	 * @param p
	 */
	public void set(int x, int y, Piece p) {
		this.grid[x][y] = p;
	}
	
	/**
	 * Set the board by given player char
	 * @param x
	 * @param y
	 * @param ch
	 */
	private void set(int x, int y, char ch) {
		switch (ch) {
		case 'H':
			grid[x][y] = new Piece(x, y, State.HSLIDER);
			allHPieces.add(grid[x][y]);
			break;
		case 'V':
			grid[x][y] = new Piece(x, y, State.VSLIDER);
			allVPieces.add(grid[x][y]);
			break;
		case 'B':
			grid[x][y] = new Piece(x, y, State.BLOCK);
			break;
		case '+':
			grid[x][y] = new Piece(x, y, State.BLANK);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Add H piece to array
	 * @param p
	 */
	public void addHPiece(Piece p) {
		this.allHPieces.add(p);
	}
	
	/**
	 * Add V piece to array
	 * @param p
	 */
	public void addVPiece(Piece p) {
		this.allVPieces.add(p);
	}
	
	/**
	 * Get H piece array
	 * @return allHPieces
	 */
	public ArrayList<Piece> getAllHPieces() {
		return allHPieces;
	}
	
	/**
	 * Get V piece array
	 * @return allVPieces
	 */
	public ArrayList<Piece> getAllVPieces() {
		return allVPieces;
	}
	
	/**
	 * Calculate and update legal moves
	 */
	public void calculateLegalMoves() {
		numLegalHMoves = 0;
		numLegalVMoves = 0;

		for (Piece h: allHPieces) {
			h.clearLegalMoves();
			int i = h.getX();
			int j = h.getY();
			// right
			if ((i + 1 < size) && (grid[i+1][j].getState() == State.BLANK)) {
				h.addLegalMove(Direction.RIGHT);
				numLegalHMoves++;
			}
			// down
			if ((j - 1 >= 0) && (grid[i][j-1].getState() == State.BLANK)) {
				h.addLegalMove(Direction.DOWN);
				numLegalHMoves++;
			}
			// up
			if ((j + 1 < size) && (grid[i][j+1].getState() == State.BLANK)) {
				h.addLegalMove(Direction.UP);
				numLegalHMoves++;
			}
			// off edge
			if (i + 1 == size) {
				h.addLegalMove(Direction.RIGHT);
				numLegalHMoves++;
			}
			
		}

		for (Piece v: allVPieces) {
			v.clearLegalMoves();
			int i = v.getX();
			int j = v.getY();
			// up
			if ((j + 1 < size) && (grid[i][j+1].getState() == State.BLANK)) {
				v.addLegalMove(Direction.UP);
				numLegalVMoves++;
			}
			// left
			if ((i - 1 >= 0) && (grid[i-1][j].getState() == State.BLANK)) {
				v.addLegalMove(Direction.LEFT);
				numLegalVMoves++;
			}
			// right
			if ((i + 1 < size) && (grid[i+1][j].getState() == State.BLANK)) {
				v.addLegalMove(Direction.RIGHT);
				numLegalVMoves++;
			}
			// off edge
			if (j + 1 == size) {
				v.addLegalMove(Direction.UP);
				numLegalVMoves++;
			}
		}

	}
	
	/**
	 * Get number of legal moves for H
	 * @return numLegalHMoves
	 */
	public int getNumLegalHMoves() {
		return numLegalHMoves;
	}
	
	/**
	 * Get number of legal moves for V
	 * @return numLegalVMoves
	 */
	public int getNumLegalVMoves() {
		return numLegalVMoves;
	}
	
	/**
	 * Update the board by given move
	 * @param player
	 * @param move
	 * @return piece: if there a piece is off edge, for further minimax undo action flagging.
	 */
	public Piece update(char player, Move move) {
		// detect null move (pass)
		if (move == null) {
			return null;
		}

		// where's the piece?
		Piece piece = grid[move.i][move.j];
		
		// where's the next space?
		int toi = move.i, toj = move.j;
		switch(move.d){
			case UP:	toj++; break;
			case DOWN:	toj--; break;
			case RIGHT:	toi++; break;
			case LEFT:	toi--; break;
		}

		// are we advancing a piece off the board?
		if (piece.getState() == State.HSLIDER && toi == this.size) {
			grid[move.i][move.j] = new Piece(move.i, move.j, State.BLANK);
			allHPieces.remove(piece);
			return piece;
		} else if (piece.getState() == State.VSLIDER && toj == this.size){
			grid[move.i][move.j] = new Piece(move.i, move.j, State.BLANK);
			allVPieces.remove(piece);
			return piece;
		}
		
		// no? all good? alright, let's make the move!
		grid[move.i][move.j] = grid[toi][toj];
		grid[move.i][move.j].reset(move.i, move.j, grid[toi][toj].getState());
		grid[toi][toj] = piece;
		grid[toi][toj].reset(toi, toj, piece.getState());
		
		return null;
	}

	@Override
	public String toString() {
		String str = new String();
		for (int j = size - 1; j >= 0; j--) {
			switch (grid[0][j].getState()) {
			case HSLIDER:
				str += 'H';
				break;
			case VSLIDER:
				str += 'V';
				break;
			case BLOCK:
				str += 'B';
				break;
			case BLANK:
				str += '+';
				break;
			}
			for (int i = 1; i < size; i++) {
				str += ' ';
				switch (grid[i][j].getState()) {
				case HSLIDER:
					str += 'H';
					break;
				case VSLIDER:
					str += 'V';
					break;
				case BLOCK:
					str += 'B';
					break;
				case BLANK:
					str += '+';
					break;
				}
			}
			str += '\n';
		}
		return str;
	}
	
}
