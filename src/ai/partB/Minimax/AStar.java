package ai.partB.Minimax;

import java.util.LinkedList;
import java.util.List;

public class AStar {
	
	private AStarCell[][] grid;
	private int size;
	
	private LinkedList<AStarCell> open;
	private LinkedList<AStarCell> closed;
	
	private char player;
	
	public AStar(Board board) {
		size = board.size();
		grid = new AStarCell[size][size];
        for (int i = 0; i < board.size(); i++) {
        	for (int j = 0; j < board.size(); j++) {
        		switch (board.get(i, j).getState()) {
        		case HSLIDER: 	grid[i][j] = new AStarCell(i, j, 'H'); break;
        		case VSLIDER:	grid[i][j] = new AStarCell(i, j, 'V'); break;
        		case BLOCK:		grid[i][j] = new AStarCell(i, j, 'B'); break;
        		case BLANK:		grid[i][j] = new AStarCell(i, j, '+'); break;
        		}
        		
        	}
        }
        open = new LinkedList<AStarCell>();
        closed = new LinkedList<AStarCell>();
	}
	
	public final List<AStarCell> findPath(int sx, int sy, int tx, int ty) {
        open.add(grid[sx][sy]);
        player = grid[sx][sy].getPiece();

        boolean done = false;
        AStarCell current;
        while (!done) {
            current = getLowestCostCell();
            closed.add(current);
            open.remove(current);
            
            //System.out.println("curr: x:"+current.getX()+" y:"+current.getY()+" piece:"+current.getPiece());

            if ((current.getX() == tx) && (current.getY() == ty)) {
                return calculatePath(grid[sx][sy], current);
            }

            List<AStarCell> neighborNodes = getNeighbor(current);
            for (AStarCell neighbor: neighborNodes) {
            	if (closed.contains(neighbor)) {
            		continue;
            	}
            	int tentative_gScore = current.getgCost() + 1;
                if (!open.contains(neighbor)) {
                    open.add(neighbor);
                } else if (tentative_gScore >= neighbor.getgCost()) {
                    continue;
                }
                neighbor.setPrev(current);
            	neighbor.setgCost(tentative_gScore);
            	neighbor.setfCost(neighbor.getgCost() + neighbor.heuristic(grid[tx][ty]));
            }

            if (open.isEmpty()) {
                return new LinkedList<AStarCell>();
            }
        }
        return null;
    }
	
	private AStarCell getLowestCostCell() {
		AStarCell lowest = open.getFirst();
		int lowestCost = lowest.getfCost();
		for (AStarCell cell: open) {
			if (cell.getfCost() < lowestCost) {
				lowest = cell;
				lowestCost = lowest.getfCost();
			}
		}
		return lowest;
	}
	
	private List<AStarCell> getNeighbor(AStarCell cell) {
		LinkedList<AStarCell> list = new LinkedList<AStarCell>();
		int x = cell.getX();
		int y = cell.getY();
		switch (player) {
		case 'H':
			if ((x+1 < size) && (grid[x+1][y].getPiece() == '+')) {
				list.add(grid[x+1][y]);
			}
			if ((y-1 >= 0) && (grid[x][y-1].getPiece() == '+')) {
				list.add(grid[x][y-1]);
			}
			if ((y+1 < size) && (grid[x][y+1].getPiece() == '+')) {
				list.add(grid[x][y+1]);
			}
			break;
		case 'V':
			if ((x-1 >= 0) && (grid[x-1][y].getPiece() == '+')) {
				list.add(grid[x-1][y]);
			}
			if ((x+1 < size) && (grid[x+1][y].getPiece() == '+')) {
				list.add(grid[x+1][y]);
			}
			if ((y+1 < size) && (grid[x][y+1].getPiece() == '+')) {
				list.add(grid[x][y+1]);
			}
			break;
		}
		return list;
	}
	
	private List<AStarCell> calculatePath(AStarCell start, AStarCell end) {
		LinkedList<AStarCell> list = new LinkedList<AStarCell>();
		AStarCell cell = end;
		while (cell.getPrev() != null) {
			list.add(cell);
			cell = cell.getPrev();
		}
		return list;
	}

}
