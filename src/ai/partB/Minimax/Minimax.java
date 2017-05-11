package ai.partB.Minimax;

import java.util.ArrayList;
import java.util.LinkedList;

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
				return Integer.MAX_VALUE;
			} else if ((board.getAllHPieces().size() == 1) && (board.getAllHPieces().get(0).getX() == board.size()-1)) {
				return Integer.MAX_VALUE-1;
			} else if ((board.getAllVPieces().size() == 0)) {
				return Integer.MIN_VALUE;
			} else if ((board.getAllVPieces().size() == 1) && (board.getAllVPieces().get(0).getY() == board.size()-1)) {
				return Integer.MIN_VALUE+1;
			}
			break;
		case 'V':
			if ((board.getAllHPieces().size() == 0)) {
				return Integer.MIN_VALUE;
			} else if ((board.getAllHPieces().size() == 1) && (board.getAllHPieces().get(0).getX() == board.size()-1)) {
				return Integer.MIN_VALUE+1;
			} else if ((board.getAllVPieces().size() == 0)) {
				return Integer.MAX_VALUE;
			} else if ((board.getAllVPieces().size() == 1) && (board.getAllVPieces().get(0).getY() == board.size()-1)) {
				return Integer.MAX_VALUE-1;
			}
			break;
		}
		AStar aStar = new AStar(board);
		int hScore = 0;
		int vScore = 0;
		int hEdges = 0;
		int vEdges = 0;
		for (Piece p: board.getAllHPieces()) {
			int min = Integer.MAX_VALUE;
			for (int edge = 0; edge < board.size(); edge++) {
				if ((p.getX() == board.size()-1) && (p.getY() == edge)) {
					min = 0;
					break;
				}
				if (board.get(board.size()-1, edge).getState() != State.BLANK) {
					continue;
				}
				LinkedList<AStarCell> path = (LinkedList<AStarCell>) aStar.findPath(p.getX(), p.getY(), board.size()-1, edge);
				if (path.size() < min) {
					min = path.size();
				}
			}
			hScore += board.size() - min;
			if (p.getX() == board.size()-1) {
				hEdges += 1;
			}
		}
		for (Piece p: board.getAllVPieces()) {
			int min = Integer.MAX_VALUE;
			for (int edge = 0; edge < board.size(); edge++) {
				if ((p.getX() == edge) && (p.getY() == board.size()-1)) {
					min = 0;
					break;
				}
				if (board.get(edge, board.size()-1).getState() != State.BLANK) {
					continue;
				}
				LinkedList<AStarCell> path = (LinkedList<AStarCell>) aStar.findPath(p.getX(), p.getY(), edge, board.size()-1);
				if (path.size() < min) {
					min = path.size();
				}
			}
			vScore += board.size() - min;
			if (p.getY() == board.size()-1) {
				vEdges += 1;
			}
		}
		int score = 0;
		switch (player) {
		case 'H':
			score = (hScore - vScore) + (hEdges - vEdges)*4 + (board.getAllVPieces().size()-board.getAllHPieces().size())*16;
			break;
		case 'V':
			score = (vScore - hScore) + (vEdges - hEdges)*4 + (board.getAllHPieces().size()-board.getAllVPieces().size())*16;
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
