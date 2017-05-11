package ai.partB.Minimax;

import aiproj.slider.Move;
import aiproj.slider.Move.Direction;

public class MinimaxMove {
	
	private int x;
	private int y;
	private Direction d;
	private Piece p;
	
	public MinimaxMove(int x, int y, Direction d) { 
		this.x = x;
		this.y = y;
		this.d = d;
		this.p = null;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Direction getDirection() {
		return d;
	}
	
	public void perform(Board board, char turn) {
		Move move = new Move(x, y, d);
		p = board.update(turn, move);
		board.calculateLegalMoves();
	}
	
	public void undo(Board board, char turn) {
		int i = x;
		int j = y;
		Direction back = null;
		switch (d) {
		case UP: 	j++; back=Direction.DOWN; 	break;
		case DOWN: 	j--; back=Direction.UP;		break;
		case RIGHT:	i++; back=Direction.LEFT;	break;
		case LEFT:	i--; back=Direction.RIGHT;	break;
		}
		if (p != null) {	// previously off edge
			board.set(p.getX(), p.getY(), p);
			switch (p.getState()) {
			case HSLIDER:
				board.addHPiece(p);
				break;
			case VSLIDER:
				board.addVPiece(p);
				break;
			default:
				break;
			}
			board.calculateLegalMoves();
			p = null;
			return;
		}
		Move move = new Move(i, j, back);
		board.update(turn, move);
		board.calculateLegalMoves();
	}
	
}
