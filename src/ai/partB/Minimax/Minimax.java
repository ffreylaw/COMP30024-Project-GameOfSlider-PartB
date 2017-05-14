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
		// get all legal moves of me
		ArrayList<MinimaxMove> moves = getMoves(player);
		if (moves.isEmpty()) {
			// no legal moves
			return null;
		}
		
		// get the first move and start iterate each moves and start recursion
		MinimaxMove bestMove = moves.get(0);
		Score bestScore = new Score(Integer.MIN_VALUE, 0.0, 0.0, 0.0);
		for (MinimaxMove move: moves) {
			move.perform(board, player);
			Score score = min(depth-1, Integer.MIN_VALUE, Integer.MAX_VALUE);
			if (score.score > bestScore.score) {
				bestMove = move;
				bestScore = score;
			}
			move.undo(board, player);
		}
		
		TDLeafLambda tdll = TDLeafLambda.getInstance();
		tdll.addEvalC1(bestScore.eval_c1);
		tdll.addEvalC2(bestScore.eval_c2);
		tdll.addEvalC3(bestScore.eval_c3);
		
		return bestMove;
	}
	
	private Score min(int depth, int alpha, int beta) {
		// enemy's turn
		char turn = player == 'H' ? 'V' : 'H';
		
		if (depth == 0) {
			// reached bottom
			return evaluate();
		}
		
		// get all legal moves of enemy
		ArrayList<MinimaxMove> moves = getMoves(turn);
		if (moves.isEmpty()) {
			// no legal moves
			return evaluate();
		}
		
		// start recursion expanding nodes
		Score bestScore = new Score(Integer.MAX_VALUE, 0.0, 0.0, 0.0);
		for (MinimaxMove move: moves) {
			move.perform(board, turn);
			Score score = max(depth-1, alpha, beta);
			if (score.score < bestScore.score) {
				bestScore = score;
			}
			move.undo(board, turn);
			// alpha-beta pruning
			if (score.score < beta) {
				beta = score.score;
			}
			if (beta <= alpha) {
				// cut-off
				break;
			}
		}
		return bestScore;
	}
	
	private Score max(int depth, int alpha, int beta) {
		// my turn
		char turn = player;
		
		if (depth == 0) {
			// reached bottom
			return evaluate();
		}
		
		// get all legal moves of me
		ArrayList<MinimaxMove> moves = getMoves(turn);
		if (moves.isEmpty()) {
			// no legal moves
			return evaluate();
		}
		
		// start recursion expanding nodes
		Score bestScore = new Score(Integer.MIN_VALUE, 0.0, 0.0, 0.0);
		for (MinimaxMove move: moves) {
			move.perform(board, turn);
			Score score = min(depth-1, alpha, beta);
			if (score.score > bestScore.score) {
				bestScore = score;
			}
			move.undo(board, turn);
			// alpha-beta pruning
			if (score.score > alpha) {
				alpha = score.score;
			}
			if (beta <= alpha) {
				// cut-off
				break;
			}
		}
		return bestScore;
	}
	
	private Score evaluate() {
		int hScore = 0;		// total number of moves away from leading edge of all H pieces
		int vScore = 0;		// total number of moves away from bottom edge of all V pieces
		int hEdges = 0;		// total number of H pieces at trailing edge
		int vEdges = 0;		// total number of V pieces at top edge
		// calculate score for H
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
		// calculate score for V
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
		
		TDLeafLambda tdll = TDLeafLambda.getInstance();
		double eval_c1 = 0.0;	// feature 1: how close to edge
		double eval_c2 = 0.0;	// feature 2: num pieces at edge
		double eval_c3 = 0.0;	// feature 3: num pieces off edge
		int score = 0;
		switch (player) {
		case 'H':
			eval_c1 = (hScore - vScore);
			eval_c2 = (hEdges - vEdges);
			eval_c3 = (board.getAllVPieces().size()-board.getAllHPieces().size())*board.size()*board.size();
			score = (int) (eval_c1*tdll.getWeight(0) + eval_c2*tdll.getWeight(1) + eval_c3*tdll.getWeight(2));
			break;
		case 'V':
			eval_c1 = (vScore - hScore);
			eval_c2 = (vEdges - hEdges);
			eval_c3 = (board.getAllHPieces().size()-board.getAllVPieces().size())*board.size()*board.size();
			score = (int) (eval_c1*tdll.getWeight(0) + eval_c2*tdll.getWeight(1) + eval_c3*tdll.getWeight(2));
			break;
		}
		return new Score(score, eval_c1, eval_c2, eval_c3);
	}
	
	private ArrayList<MinimaxMove> getMoves(char turn) {
		// get all legal moves of current turn
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
	
	/** class for storing a score and associate features
	 */
	private static class Score {
		
		private int score;
		
		private double eval_c1;
		private double eval_c2;
		private double eval_c3;
		
		public Score(int score, double eval_c1, double eval_c2, double eval_c3) {
			this.score = score;
			this.eval_c1 = eval_c1;
			this.eval_c2 = eval_c2;
			this.eval_c3 = eval_c3;
		}
	}
	
}
