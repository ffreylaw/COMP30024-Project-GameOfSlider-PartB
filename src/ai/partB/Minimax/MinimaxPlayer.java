package ai.partB.Minimax;

import aiproj.slider.*;
import aiproj.slider.Move.Direction;

/**
 * Player using minimax algorithm
 */
public class MinimaxPlayer implements SliderPlayer {
	
	private Board board;
	private char player;
	
	private int moveCount;
	
	private TDLeafLambda tdll = new TDLeafLambda();
	
	@Override
	public void init(int dimension, String board, char player) {
		this.board = new Board(dimension, board);
		this.player = player;
		this.moveCount = 0;
	}

	@Override
	public void update(Move move) {
		// update the board and re-calculate legal moves
		board.update(player, move);
		board.calculateLegalMoves();
	}

	@Override
	public Move move() {
		// count the moves
		moveCount++;
		
		// check if winning state and make the winning move
		switch (player) {
		case 'H':
			// one move to win
			if ((board.getAllHPieces().size() == 1) && (board.getAllHPieces().get(0).getX() == board.size()-1)) {
				tdll.finalize();
				return new Move(board.getAllHPieces().get(0).getX(), board.getAllHPieces().get(0).getY(), Direction.RIGHT);
			}
			// check if all pieces at edge, just make the winning move
			boolean allHPiecesAtEdge = true;
			for (Piece p: board.getAllHPieces()) {
				if (p.getX() != board.size() - 1) {
					allHPiecesAtEdge = false;
				}
			}
			if (allHPiecesAtEdge) {
				Move move = new Move(board.getAllHPieces().get(0).getX(), board.getAllHPieces().get(0).getY(), Direction.RIGHT);
				this.update(move);
				return move;
			}
			break;
		case 'V':
			// one move to win
			if ((board.getAllVPieces().size() == 1) && (board.getAllVPieces().get(0).getY() == board.size()-1)) {
				tdll.finalize();
				return new Move(board.getAllVPieces().get(0).getX(), board.getAllVPieces().get(0).getY(), Direction.UP);
			}
			// check if all pieces at edge, just make the winning move
			boolean allVPiecesAtEdge = true;
			for (Piece p: board.getAllVPieces()) {
				if (p.getY() != board.size() - 1) {
					allVPiecesAtEdge = false;
				}
			}
			if (allVPiecesAtEdge) {
				Move move = new Move(board.getAllVPieces().get(0).getX(), board.getAllVPieces().get(0).getY(), Direction.UP);
				this.update(move);
				return move;
			}
			break;
		}
		
		// get and apply my strategy
		Strategy s = new Strategy(board, player);
		Move esMove = s.doEarlierStrategy();
		if ((moveCount <= board.size()) && esMove != null) {
			this.update(esMove);
			return esMove;
		}
		
		// if no strategy applied, apply minimax
		Minimax minimax = new Minimax(board, player, tdll);
		MinimaxMove minimaxMove = minimax.run(6);
		if (minimaxMove == null) {
			return null;
		}
		Move move = new Move(minimaxMove.getX(), minimaxMove.getY(), minimaxMove.getDirection());
		this.update(move);
		return move;
	}

}
