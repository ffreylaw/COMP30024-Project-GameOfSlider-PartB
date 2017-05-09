package ai.partB.Minimax;

public class AStarCell {
	
	private int fCost;
	private int hCost;
	private int gCost;
	
	private int x, y;
	
	private AStarCell prev;
	
	private char piece;
	
	public AStarCell(int x, int y, char piece) {
		this.fCost = 0;
		this.hCost = 0;
		this.gCost = 0;
		this.x = x;
		this.y = y;
		this.prev = null;
		this.piece = piece;
	}
	
	public void heuristic(AStarCell cell) {
		int score = 0;
		score += Math.abs(this.x - cell.x);
		score += Math.abs(this.y - cell.y);
		hCost = score;
	}
	
	public void updateCost() {
		fCost = hCost + gCost;
	}

	public int getgCost() {
		return gCost;
	}

	public void setgCost(int gCost) {
		this.gCost = gCost;
	}

	public AStarCell getPrev() {
		return prev;
	}

	public void setPrev(AStarCell prev) {
		this.prev = prev;
	}

	public int getfCost() {
		return fCost;
	}

	public int gethCost() {
		return hCost;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public char getPiece() {
		return piece;
	}
	
}
