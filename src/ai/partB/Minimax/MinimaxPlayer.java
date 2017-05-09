package ai.partB.Minimax;

import java.util.LinkedList;

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
		AStar aStar = new AStar(board);
		LinkedList<AStarCell> path = (LinkedList<AStarCell>) aStar.findPath(0, 4, 4, 1);
		System.out.println(path.size());
		for (AStarCell cell: path) {
			System.out.println("x:"+cell.getX()+" y:"+cell.getY());
		}
		
//		Minimax minimax = new Minimax(board, player);
//		MinimaxMove minimaxMove = minimax.run(3);
//		if (minimaxMove == null) {
//			return null;
//		}
//		Move move = new Move(minimaxMove.getX(), minimaxMove.getY(), minimaxMove.getDirection());
//		this.update(move);
//		return move;
		return null;
	}

}
