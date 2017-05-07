package ai.partB.Minimax;

import java.util.ArrayList;
import java.util.Random;

import aiproj.slider.Move.Direction;

public class Minimax {
	
	private static final Random random = new Random(30024);	// for testing purpose
	
	private Board board;
	private char player;
	
	public Minimax(Board board, char player) {
		this.board = board;
		this.player = player;
	}
	
	public MinimaxMove run(int depth) {
		char turn = player;
		ArrayList<MinimaxMove> moves = getMoves(turn);
		if (moves.isEmpty()) {
			return null;
		}
		MinimaxMove bestMove = moves.get(0);
		int bestScore = Integer.MIN_VALUE;
		for (MinimaxMove move: moves) {
			move.perform(board, turn);
			int score = min(depth-1);
			if (score > bestScore) {
				bestMove = move;
				bestScore = score;
			}
			move.undo(board, turn);
		}
		return bestMove;
	}
	
	private int min(int depth) {
		if (depth == 0) {
			return evaluate();
		}
		char turn = player == 'H' ? 'V' : 'H';
		ArrayList<MinimaxMove> moves = getMoves(turn);
		if (moves.isEmpty()) {	// dont know why
			return evaluate();
		}
		int bestScore = Integer.MAX_VALUE;
		for (MinimaxMove move: moves) {
			move.perform(board, turn);
			int score = max(depth-1);
			if (score < bestScore) {
				bestScore = score;
			}
			move.undo(board, turn);
		}
		return bestScore;
	}
	
	private int max(int depth) {
		if (depth == 0) {
			return evaluate();
		}
		char turn = player;
		ArrayList<MinimaxMove> moves = getMoves(turn);
		if (moves.isEmpty()) {	// dont know why
			return evaluate();
		}
		int bestScore = Integer.MIN_VALUE;
		for (MinimaxMove move: moves) {
			move.perform(board, turn);
			int score = min(depth-1);
			if (score > bestScore) {
				bestScore = score;
			}
			move.undo(board, turn);
		}
		return bestScore;
	}
	
	private int evaluate() {
//		switch (player) {
//		case 'H':
//			if (board.getAllHPieces().isEmpty()) {
//				return Integer.MAX_VALUE;
//			}
//			if (board.getAllVPieces().isEmpty()) {
//				return Integer.MIN_VALUE;
//			}
//			break;
//		case 'V':
//			if (board.getAllHPieces().isEmpty()) {
//				return Integer.MIN_VALUE;
//			}
//			if (board.getAllVPieces().isEmpty()) {
//				return Integer.MAX_VALUE;
//			}
//			break;
//		}
		int hScore = 0;
		int vScore = 0;
		ArrayList<MinimaxMove> hMoves = getMoves('H');
		ArrayList<MinimaxMove> vMoves = getMoves('V');
		for (MinimaxMove move: hMoves) {
			for (int i = move.getX(); i <= board.size(); i++) {
				hScore -= 1;
			}
		}
		for (MinimaxMove move: vMoves) {
			for (int i = move.getX(); i <= board.size(); i++) {
				vScore -= 1;
			}
		}
		
		int score = 0;
		switch (player) {
		case 'H':
			score = hScore;
			break;
		case 'V':
			score = vScore;
			break;
		}
		return random.nextInt(100);
	}
	
	private ArrayList<MinimaxMove> getMoves(char turn) {
		ArrayList<MinimaxMove> moves = new ArrayList<MinimaxMove>();
		switch (turn) {
		case 'H':
			for (Piece h: board.getAllHPieces()) {
				for (Direction d: h.getLegalMoves()) {
					moves.add(new MinimaxMove(h.getX(), h.getY(), d));
				}
			}
			break;
		case 'V':
			for (Piece v: board.getAllVPieces()) {
				for (Direction d: v.getLegalMoves()) {
					moves.add(new MinimaxMove(v.getX(), v.getY(), d));
				}
			}
			break;
		}
		return moves;
	}
	
}
