package ai.partB.Minimax;

import java.util.ArrayList;

import aiproj.slider.Move.Direction;

/**
 * Minimax algorithm
 */
public class Minimax {
	
	private Board board;
	private char player;
	
//	private TDLeafLambda tdll;
	
	public Minimax(Board board, char player) {
		this.board = board;
		this.player = player;
	}
	
//	public Minimax(Board board, char player, TDLeafLambda tdll) {
//		this.board = board;
//		this.player = player;
//		this.tdll = tdll;
//	}
	
	public MinimaxMove run(int depth) {
		// get all legal moves of me
		ArrayList<MinimaxMove> moves = getMoves(player);
		if (moves.isEmpty()) {
			// no legal moves
			return null;
		}
		
		// get the first move and start iterate each moves and start recursion
		MinimaxMove bestMove = moves.get(0);
		Score bestScore = new Score(Integer.MIN_VALUE, 0.0, 0.0, 0.0, 0.0);
		for (MinimaxMove move: moves) {
			move.perform(board, player);
			Score score = min(depth-1, Integer.MIN_VALUE, Integer.MAX_VALUE);
			if (score.score > bestScore.score) {
				bestMove = move;
				bestScore = score;
			}
			move.undo(board, player);
		}
		
//		tdll.addEvalC1(bestScore.eval_c1);
//		tdll.addEvalC2(bestScore.eval_c2);
//		tdll.addEvalC3(bestScore.eval_c3);
//		tdll.addEvalC4(bestScore.eval_c4);
		
		return bestMove;
	}
	
	private Score min(int depth, double alpha, double beta) {
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
		Score bestScore = new Score(Integer.MAX_VALUE, 0.0, 0.0, 0.0, 0.0);
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
				// alpha cut-off
				break;
			}
		}
		return bestScore;
	}
	
	private Score max(int depth, double alpha, double beta) {
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
		Score bestScore = new Score(Integer.MIN_VALUE, 0.0, 0.0, 0.0, 0.0);
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
				// beta cut-off
				break;
			}
		}
		return bestScore;
	}
	
	private Score evaluate() {
		int hSteps = 0;		// total number of moves away from leading edge of all H pieces
		int vSteps = 0;		// total number of moves away from bottom edge of all V pieces
		int hBlocks = 0;	// total number of H pieces that being blocked
		int vBlocks = 0;	// total number of V pieces that being blocked
		int hEdges = 0;		// total number of H pieces at trailing edge
		int vEdges = 0;		// total number of V pieces at top edge
		// calculate score for H
		for (Piece p: board.getAllHPieces()) {
			hSteps += p.getX();
			if ((p.getX() + 1 < board.size()) && 
				((board.get(p.getX()+1, p.getY()).getState() == State.VSLIDER) ||
				 (board.get(p.getX()+1, p.getY()).getState() == State.BLOCK))) {
				hBlocks -= 1;
			}
			if (p.getX() == board.size()-1) {
				hEdges += 1;
			}
		}
		// calculate score for V
		for (Piece p: board.getAllVPieces()) {
			vSteps += p.getY();
			if ((p.getY() + 1 < board.size()) && 
				((board.get(p.getX(), p.getY()+1).getState() == State.HSLIDER) ||
				 (board.get(p.getX(), p.getY()+1).getState() == State.BLOCK))) {
				vBlocks -= 1;
			}
			if (p.getY() == board.size()-1) {
				vEdges += 1;
			}
		}
		
		double eval_c1 = 0.0;	// feature 1: how close to edge
		double eval_c2 = 0.0;	// feature 2: num pieces get blocked
		double eval_c3 = 0.0;	// feature 3: num pieces at edge
		double eval_c4 = 0.0; 	// feature 4: num pieces off edge
		double score = 0;
		switch (player) {
		case 'H':
			eval_c1 = (hSteps - vSteps);
			eval_c2	= (hBlocks - vBlocks);
			eval_c3 = (hEdges - vEdges);
			eval_c4 = (board.getAllVPieces().size()-board.getAllHPieces().size())*board.size()*board.size();
			score = eval_c1*TDLeafLambda.H_WEIGHT_1 + 
					eval_c2*TDLeafLambda.H_WEIGHT_2 + 
					eval_c3*TDLeafLambda.H_WEIGHT_3 + 
					eval_c4*TDLeafLambda.H_WEIGHT_4;
//			score = eval_c1*tdll.getWeight(0) + 
//					eval_c2*tdll.getWeight(1) + 
//					eval_c3*tdll.getWeight(2) + 
//					eval_c4*tdll.getWeight(3);
			break;
		case 'V':
			eval_c1 = (vSteps - hSteps);
			eval_c2 = (vBlocks - vBlocks);
			eval_c3 = (vEdges - hEdges);
			eval_c4 = (board.getAllHPieces().size()-board.getAllVPieces().size())*board.size()*board.size();
			score = eval_c1*TDLeafLambda.V_WEIGHT_1 + 
					eval_c2*TDLeafLambda.V_WEIGHT_2 + 
					eval_c3*TDLeafLambda.V_WEIGHT_3 + 
					eval_c4*TDLeafLambda.V_WEIGHT_4;
//			score = eval_c1*tdll.getWeight(0) + 
//					eval_c2*tdll.getWeight(1) + 
//					eval_c3*tdll.getWeight(2) + 
//					eval_c4*tdll.getWeight(3);
			break;
		}
		return new Score(score, eval_c1, eval_c2, eval_c3, eval_c4);
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
		
		private double score;
		
		private double eval_c1;
		private double eval_c2;
		private double eval_c3;
		private double eval_c4;
		
		public Score(double score, double eval_c1, double eval_c2, double eval_c3, double eval_c4) {
			this.score = score;
			this.eval_c1 = eval_c1;
			this.eval_c2 = eval_c2;
			this.eval_c3 = eval_c3;
			this.eval_c4 = eval_c4;
		}
	}
	
}
