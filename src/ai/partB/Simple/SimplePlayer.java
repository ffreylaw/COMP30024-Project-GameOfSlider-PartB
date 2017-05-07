package ai.partB.Simple;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import aiproj.slider.*;
import aiproj.slider.Move.Direction;

public class SimplePlayer implements SliderPlayer {
	
	private static final Random random = new Random(30024);	// for testing purpose

	private Board board;
	private char player;
	
	@Override
	public void init(int dimension, String board, char player) {
		this.board = new Board(dimension, board);
		this.player = player;
	}

	@Override
	public void update(Move move) {
		board.update(player, move);
	}

	@Override
	public Move move() {
		// random select a piece
		Piece piece = null;
		int i = 0;
		int j = 0;
		Move.Direction d = null;
		switch (player) {
		case 'H': 
			if (board.numLegalHMoves == 0) {
				return null;
			}
			while (true) {
				piece = board.allHPieces.get(random.nextInt(board.allHPieces.size()));
				if (piece.legalMoves.size() == 0) {
					continue;
				}
				i = piece.x;
				j = piece.y;
				if (piece.legalMoves.contains(Direction.RIGHT)) {
					d = Move.Direction.RIGHT;
					break;
				} else if (piece.legalMoves.contains(Direction.DOWN)) {
					d = Move.Direction.DOWN;
					break;
				} else if (piece.legalMoves.contains(Direction.UP)) {
					d = Move.Direction.UP;
					break;
				} else {
					continue;
				}
			}
			break;
		case 'V': 
			if (board.numLegalVMoves == 0) {
				return null;
			}
			while (true) {
				piece = board.allVPieces.get(random.nextInt(board.allVPieces.size()));
				if (piece.legalMoves.size() == 0) {
					continue;
				}
				i = piece.x;
				j = piece.y;
				if (piece.legalMoves.contains(Direction.UP)) {
					d = Move.Direction.UP;
					break;
				} else if (piece.legalMoves.contains(Direction.LEFT)) {
					d = Move.Direction.LEFT;
					break;
				} else if (piece.legalMoves.contains(Direction.RIGHT)) {
					d = Move.Direction.RIGHT;
					break;
				} else {
					continue;
				}
			}
			break;
		}
		Move move = new Move(i, j, d);
		board.update(player, move);
		return move;
	}
	
	private static enum State { BLANK, BLOCK, HSLIDER, VSLIDER, };
	
	private static class Piece { 
		
		private int x, y;
		private State state;
		private ArrayList<Direction> legalMoves;
		
		public Piece(int x, int y, State state) { 
			this.x = x; 
			this.y = y; 
			this.state = state;
			this.legalMoves = new ArrayList<Direction>();
		}
		
		private void set(int x, int y, State state) { 
			this.x = x; 
			this.y = y; 
			this.state = state;
			this.legalMoves.clear();
		}
		
	}
	
	private static class Board {

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
		
		private void calculateLegalMoves() {
			numLegalHMoves = 0;
			numLegalVMoves = 0;

			for (Piece h: allHPieces) {
				h.legalMoves.clear();
				int i = h.x;
				int j = h.y;
				// right
				if ((i + 1 < size) && (grid[i+1][j].state == State.BLANK)) {
					h.legalMoves.add(Direction.RIGHT);
					numLegalHMoves++;
				}
				// down
				if ((j - 1 >= 0) && (grid[i][j-1].state == State.BLANK)) {
					h.legalMoves.add(Direction.DOWN);
					numLegalHMoves++;
				}
				// up
				if ((j + 1 < size) && (grid[i][j+1].state == State.BLANK)) {
					h.legalMoves.add(Direction.UP);
					numLegalHMoves++;
				}
				// off edge
				if (i + 1 == size) {
					h.legalMoves.add(Direction.RIGHT);
					numLegalHMoves++;
				}
				
			}

			for (Piece v: allVPieces) {
				v.legalMoves.clear();
				int i = v.x;
				int j = v.y;
				// up
				if ((j + 1 < size) && (grid[i][j+1].state == State.BLANK)) {
					v.legalMoves.add(Direction.UP);
					numLegalVMoves++;
				}
				// left
				if ((i - 1 >= 0) && (grid[i-1][j].state == State.BLANK)) {
					v.legalMoves.add(Direction.LEFT);
					numLegalVMoves++;
				}
				// right
				if ((i + 1 < size) && (grid[i+1][j].state == State.BLANK)) {
					v.legalMoves.add(Direction.RIGHT);
					numLegalVMoves++;
				}
				// off edge
				if (j + 1 == size) {
					v.legalMoves.add(Direction.UP);
					numLegalVMoves++;
				}
			}

		}
		
		public void update(char player, Move move) {
			// detect null move (pass)
			if (move == null) {
				return;
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
			if (piece.state == State.HSLIDER && toi == this.size) {
				grid[move.i][move.j] = new Piece(move.i, move.j, State.BLANK);
				allHPieces.remove(piece);
				return;

			} else if (piece.state == State.VSLIDER && toj == this.size){
				grid[move.i][move.j] = new Piece(move.i, move.j, State.BLANK);
				allVPieces.remove(piece);
				return;
			}
			
			// no? all good? alright, let's make the move!
			grid[move.i][move.j] = grid[toi][toj];
			grid[move.i][move.j].set(move.i, move.j, grid[toi][toj].state);
			grid[toi][toj] = piece;
			grid[toi][toj].set(toi, toj, piece.state);
			
			calculateLegalMoves();
			return;
		}

		@Override
		public String toString() {
			String str = new String();
			for (int j = size - 1; j >= 0; j--) {
				for (int i = 0; i < size; i++) {
					switch (grid[i][j].state) {
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

}
