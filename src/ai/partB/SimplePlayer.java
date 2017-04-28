package ai.partB;

import aiproj.slider.*;
import ai.partB.Board.*;

public class SimplePlayer implements SliderPlayer {

	Board board;
	char player;

	public SimplePlayer() {
		
	}

	@Override
	public void init(int dimension, String board, char player) {
		this.board = new Board(dimension, board);
		this.player = player;
	}

	@Override
	public void update(Move move) {
		board.move(player, move);
	}

	@Override
	public Move move() {

		return null;
	}

}
