package ai.partB.Minimax;

import java.util.ArrayList;
import java.util.Scanner;

import aiproj.slider.Move;
import aiproj.slider.Move.Direction;

public class Board {

	private Piece[][] grid;
	private final int size;

	private ArrayList<Piece> allHPieces;
	private ArrayList<Piece> allVPieces;

	private int numLegalHMoves;
	private int numLegalVMoves;
	
	private Scanner reader;

	public Board(int dimension, String board) {
		reader = new Scanner(board);
		
		size = dimension;
		this.grid = new Piece[size][size];
		
		allHPieces = new ArrayList<Piece>();
		allVPieces = new ArrayList<Piece>();
		
		int j = size - 1;
		while (j >= 0 && reader.hasNextLine()) {
			String line = reader.nextLine();
			String[] splited = line.split(" ");
			for (int i = 0; i < splited.length; i++) {
				set(i, j, splited[i].charAt(0));
			}
			j--;
		}

		numLegalHMoves = 0;
		numLegalVMoves = 0;
		calculateLegalMoves();
	}
	
	public int size() {
		return this.size;
	}
	
	public Piece get(int x, int y) {
		return grid[x][y];
	}
	
	public void set(int x, int y, Piece p) {
		this.grid[x][y] = p;
	}
	
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
	
	public void addHPiece(Piece p) {
		this.allHPieces.add(p);
	}
	
	public void addVPiece(Piece p) {
		this.allVPieces.add(p);
	}
	
	public ArrayList<Piece> getAllHPieces() {
		return allHPieces;
	}

	public ArrayList<Piece> getAllVPieces() {
		return allVPieces;
	}

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
	
	public int getNumLegalHMoves() {
		return numLegalHMoves;
	}

	public int getNumLegalVMoves() {
		return numLegalVMoves;
	}

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
			for (int i = 0; i < size; i++) {
				switch (grid[i][j].getState()) {
				case HSLIDER:
					str += "H ";
					break;
				case VSLIDER:
					str += "V ";
					break;
				case BLOCK:
					str += "B ";
					break;
				case BLANK:
					str += "+ ";
					break;
				}
			}
			str += '\n';
		}
		return str;
	}
	
}
