package ai.partB.Minimax;

import java.util.LinkedList;

import aiproj.slider.*;
import aiproj.slider.Move.Direction;

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
		Minimax minimax = new Minimax(board, player);
		MinimaxMove minimaxMove = minimax.run(5);
		if (minimaxMove == null) {
			return null;
		}
		Move move = new Move(minimaxMove.getX(), minimaxMove.getY(), minimaxMove.getDirection());
		this.update(move);
		return move;
	}

}
