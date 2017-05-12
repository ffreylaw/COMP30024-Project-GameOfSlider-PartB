package ai.partB.Minimax;

import java.util.ArrayList;

import aiproj.slider.Move.Direction;

public class Minimax {
	
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
		char turn = player == 'H' ? 'V' : 'H';
		if (depth == 0) {
			return evaluate();
		}
		ArrayList<MinimaxMove> moves = getMoves(turn);
		if (moves.isEmpty()) {
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
		char turn = player;
		if (depth == 0) {
			return evaluate();
		}
		ArrayList<MinimaxMove> moves = getMoves(turn);
		if (moves.isEmpty()) {
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
		switch (player) {
		case 'H':
			if ((board.getAllHPieces().size() == 0)) {
				return Integer.MAX_VALUE-1;
			} else if ((board.getAllHPieces().size() == 1) && (board.getAllHPieces().get(0).getX() == board.size()-1)) {
				return Integer.MAX_VALUE-2;
			} else if ((board.getAllVPieces().size() == 0)) {
				return Integer.MIN_VALUE+1;
			} else if ((board.getAllVPieces().size() == 1) && (board.getAllVPieces().get(0).getY() == board.size()-1)) {
				return Integer.MIN_VALUE+2;
			}
			break;
		case 'V':
			if ((board.getAllHPieces().size() == 0)) {
				return Integer.MIN_VALUE+1;
			} else if ((board.getAllHPieces().size() == 1) && (board.getAllHPieces().get(0).getX() == board.size()-1)) {
				return Integer.MIN_VALUE+2;
			} else if ((board.getAllVPieces().size() == 0)) {
				return Integer.MAX_VALUE-1;
			} else if ((board.getAllVPieces().size() == 1) && (board.getAllVPieces().get(0).getY() == board.size()-1)) {
				return Integer.MAX_VALUE-2;
			}
			break;
		}
		int hScore = 0;
		int vScore = 0;
		int hEdges = 0;
		int vEdges = 0;
		for (Piece p: board.getAllHPieces()) {
			int pathSize = p.getX();
			if (p.getX() + 1 < board.size()) {
				for (int x = p.getX() + 1; x < board.size(); x++) {
					if (board.get(x, p.getY()).getState() != State.BLANK) {
						pathSize--;
					}
				}
			}
			hScore += pathSize;
			if (p.getX() == board.size()-1) {
				hEdges += 1;
			}
		}
		for (Piece p: board.getAllVPieces()) {
			int pathSize = p.getY();
			if (p.getY() + 1 < board.size()) {
				for (int y = p.getY() + 1; y < board.size(); y++) {
					if (board.get(p.getX(), y).getState() != State.BLANK) {
						pathSize--;
					}
				}
			}
			vScore += pathSize;
			if (p.getY() == board.size()-1) {
				vEdges += 1;
			}
		}
		int score = 0;
		switch (player) {
		case 'H':
			score = (hScore - vScore) + (hEdges - vEdges)*board.size() + (board.getAllVPieces().size()-board.getAllHPieces().size())*board.size()*board.size();
			break;
		case 'V':
			score = (vScore - hScore) + (vEdges - hEdges)*board.size() + (board.getAllHPieces().size()-board.getAllVPieces().size())*board.size()*board.size();
			break;
		}
		return score;
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
