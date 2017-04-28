package ai.partB.Board;

public class Square {

	private int x;
	private int y;
	private boolean isEmpty;

	public Square(int x, int y, boolean isEmpty) {
		this.x = x;
		this.y = y;
		this.isEmpty = isEmpty;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return this.isEmpty ? "+" : this instanceof Block ? "B" : this instanceof HPiece ? "H" : "V";
	}

}
