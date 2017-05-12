package ai.partB.Minimax;

import aiproj.slider.*;
import aiproj.slider.Move.Direction;

public class MinimaxPlayer implements SliderPlayer {
	
	private Board board;
	private char player;
	
	private int moveCount;
	
	@Override
	public void init(int dimension, String board, char player) {
		this.board = new Board(dimension, board);
		this.player = player;
		this.moveCount = 0;
	}

	@Override
	public void update(Move move) {
		board.update(player, move);
		board.calculateLegalMoves();
	}

	@Override
	public Move move() {
		moveCount++;
		switch (player) {
		case 'H':
			if ((board.getAllHPieces().size() == 1) && (board.getAllHPieces().get(0).getX() == board.size()-1)) {
				return new Move(board.getAllHPieces().get(0).getX(), board.getAllHPieces().get(0).getY(), Direction.RIGHT);
			}
			break;
		case 'V':
			if ((board.getAllVPieces().size() == 1) && (board.getAllVPieces().get(0).getY() == board.size()-1)) {
				return new Move(board.getAllVPieces().get(0).getX(), board.getAllVPieces().get(0).getY(), Direction.UP);
			}
			break;
		}
		Strategy s = new Strategy(board, player);
		Move esMove = s.doEarlierStrategy();
		if ((moveCount <= board.size()) && esMove != null) {
			this.update(esMove);
			return esMove;
		}
		
		Minimax minimax = new Minimax(board, player);
		MinimaxMove minimaxMove = minimax.run(4);
		if (minimaxMove == null) {
			return null;
		}
		Move move = new Move(minimaxMove.getX(), minimaxMove.getY(), minimaxMove.getDirection());
		this.update(move);
		return move;
	}

}
