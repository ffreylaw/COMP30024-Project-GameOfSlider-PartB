package ai.partB.Minimax;

import aiproj.slider.*;

public class MinimaxPlayer implements SliderPlayer {
	
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
		Minimax minimax = new Minimax(board, player);
		MinimaxMove minimaxMove = minimax.run(3);
		if (minimaxMove == null) {
			return null;
		}
		Move move = new Move(minimaxMove.getX(), minimaxMove.getY(), minimaxMove.getDirection());
		this.update(move);
		return move;
	}

}
