package ai.partB.Minimax;

import java.util.ArrayList;
import java.util.LinkedList;

import aiproj.slider.Move;
import aiproj.slider.Move.Direction;

public class Strategy {
	
	Board board;
	char player;
	
	public Strategy(Board board, char player) {
		this.board = board;
		this.player = player;
	}
	
	public Move doEarlierStrategy() {
		AStar aStar = new AStar(board);
		switch (player) {
		case 'H':
			if (!isTopRightOccupied()) {
				Piece minHPiece = null;
				int minHPath = Integer.MAX_VALUE;
				for (Piece p: board.getAllHPieces()) {
					LinkedList<AStarCell> path = (LinkedList<AStarCell>) aStar.findPath(p.getX(), p.getY(), board.size()-1, board.size()-1);
					if (!path.isEmpty() && path.size() < minHPath) {
						minHPiece = p;
						minHPath = path.size();
					}
				}
				int minVPath = Integer.MAX_VALUE;
				for (Piece p: board.getAllVPieces()) {
					LinkedList<AStarCell> path = (LinkedList<AStarCell>) aStar.findPath(p.getX(), p.getY(), board.size()-1, board.size()-1);
					if (!path.isEmpty() && path.size() < minVPath) {
						minVPath = path.size();
					}
				}
				if (minHPath < minVPath) {
					if ((!isBottomLeftOccupied()) && (board.get(1, 0).getState() == State.VSLIDER)) {
						return new Move(0, 1, Direction.RIGHT);
					}
					return new Move(minHPiece.getX(), minHPiece.getY(), Direction.RIGHT);
				} else if (minHPath == minVPath) {
					return new Move(minHPiece.getX(), minHPiece.getY(), Direction.RIGHT);
				}
			}
			if ((!isBottomLeftOccupied()) && (board.get(1, 0).getState() == State.VSLIDER)) {
				return new Move(0, 1, Direction.RIGHT);
			}
			break;
		case 'V':
			if (!isTopRightOccupied()) {
				Piece minVPiece = null;
				int minVPath = Integer.MAX_VALUE;
				for (Piece p: board.getAllHPieces()) {
					LinkedList<AStarCell> path = (LinkedList<AStarCell>) aStar.findPath(p.getX(), p.getY(), board.size()-1, board.size()-1);
					if (!path.isEmpty() && path.size() < minVPath) {
						minVPiece = p;
						minVPath = path.size();
					}
				}
				int minHPath = Integer.MAX_VALUE;
				for (Piece p: board.getAllVPieces()) {
					LinkedList<AStarCell> path = (LinkedList<AStarCell>) aStar.findPath(p.getX(), p.getY(), board.size()-1, board.size()-1);
					if (!path.isEmpty() && path.size() < minHPath) {
						minHPath = path.size();
					}
				}
				if (minVPath < minHPath) {
					if ((!isBottomLeftOccupied()) && (board.get(0, 1).getState() == State.HSLIDER)) {
						return new Move(1, 0, Direction.UP);
					}
					return new Move(minVPiece.getX(), minVPiece.getY(), Direction.UP);
				} else if (minVPath == minHPath) {
					return new Move(minVPiece.getX(), minVPiece.getY(), Direction.UP);
				}
			}
			if ((!isBottomLeftOccupied()) && (board.get(0, 1).getState() == State.HSLIDER)) {
				return new Move(1, 0, Direction.UP);
			}
			break;
		} 
		return null;
	}
	
	private boolean isTopRightOccupied() {
		if (board.get(board.size()-1, board.size()-1).getState() == State.BLANK) {
			return false;
		}
		return true;
	}
	
	private boolean isBottomLeftOccupied() {
		if (board.get(1, 1).getState() == State.BLANK) {
			return false;
		}
		return true;
	}
	
	private ArrayList<Piece> getAttack() {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		switch (player) {
		case 'H':
			for (Piece p: board.getAllHPieces()) {
				int x = p.getX();
				int y = p.getY();
				if (((x + 1 < board.size()) && (y - 1 >= 0)) && (board.get(x+1, y-1).getState() == State.VSLIDER)) {
					pieces.add(p);
				}
			}
			break;
		case 'V':
			for (Piece p: board.getAllVPieces()) {
				int x = p.getX();
				int y = p.getY();
				if (((x - 1 >= 0) && (y + 1 < board.size())) && (board.get(x-1, y+1).getState() == State.HSLIDER)) {
					pieces.add(p);
				}
			}
			break;
		}
		return pieces;
	}
	
	private ArrayList<Piece> getDefence() {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		switch (player) {
		case 'H':
			for (Piece p: board.getAllHPieces()) {
				int x = p.getX();
				int y = p.getY();
				if ((y - 1 >= 0) && (board.get(x, y-1).getState() == State.VSLIDER)) {
					pieces.add(p);
				}
			}
			break;
		case 'V':
			for (Piece p: board.getAllVPieces()) {
				int x = p.getX();
				int y = p.getY();
				if ((x - 1 >= 0) && (board.get(x-1, y).getState() == State.HSLIDER)) {
					pieces.add(p);
				}
			}
			break;
		}
		return pieces;
	}

}
