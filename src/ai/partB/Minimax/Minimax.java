package ai.partB.Minimax;

import java.util.ArrayList;
import java.util.LinkedList;
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
		AStar aStar = new AStar(board);
		int hScore = 0;
		int vScore = 0;
		for (Piece p: board.getAllHPieces()) {
			int min = Integer.MAX_VALUE;
			for (int edge = 0; edge < board.size(); edge++) {
				if (board.get(board.size()-1, edge).getState() != State.BLANK) {
					continue;
				}
				LinkedList<AStarCell> path = (LinkedList<AStarCell>) aStar.findPath(p.getX(), p.getY(), board.size()-1, edge);
				if (path.size() < min) {
					min = path.size();
				}
			}
			hScore += min;
		}
		for (Piece p: board.getAllVPieces()) {
			int min = Integer.MAX_VALUE;
			for (int edge = 0; edge < board.size(); edge++) {
				if (board.get(edge, board.size()-1).getState() != State.BLANK) {
					continue;
				}
				LinkedList<AStarCell> path = (LinkedList<AStarCell>) aStar.findPath(p.getX(), p.getY(), edge, board.size()-1);
				if (path.size() < min) {
					min = path.size();
				}
			}
			vScore += min;
		}
		
		int score = 0;
		switch (player) {
		case 'H':
			score = (vScore - hScore) + (board.size() - 1 - board.getAllHPieces().size())*board.size();
			break;
		case 'V':
			score = (hScore - vScore) + (board.size() - 1 - board.getAllVPieces().size())*board.size();
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
