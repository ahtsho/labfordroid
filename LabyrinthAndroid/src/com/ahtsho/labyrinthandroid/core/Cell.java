package com.ahtsho.labyrinthandroid.core;

public class Cell {

	public static final char WEST = 'W';
	public static final char EAST = 'E';
	public static final char SOUTH = 'S';
	public static final char NORTH = 'N';
	
	String name;
	private boolean north;
	private boolean south;
	private boolean west;
	private boolean east;
	private int row, col;

	public Cell(boolean N, boolean S, boolean W, boolean E, String string,int x, int y){
		this(N,S,W,E, string);
		row = x;
		col = y;
	}
	public Cell(boolean N, boolean S, boolean W, boolean E, String string) {
		north = N;
		south = S;
		west = W;
		east = E;
		name = string;
	}

	public boolean isNorth() {
		return north;
	}

	public void setNorth(boolean north) {
		this.north = north;
	}

	public boolean isSouth() {
		return south;
	}

	public void setSouth(boolean south) {
		this.south = south;
	}

	public boolean isWest() {
		return west;
	}

	public void setWest(boolean west) {
		this.west = west;
	}

	public boolean isEast() {
		return east;
	}

	public void setEast(boolean east) {
		this.east = east;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public String getName() {
		return name;
	}

	/***
	 * Sets the wall of a given direction to false
	 * 
	 * @param direction
	 *            = N, S, W, E
	 * @return true if succeded, flase if failed
	 */
	public boolean breakWall(char direction) {
		if (direction == Cell.NORTH) {
			this.setNorth(false);
			return true;
		}
		if (direction == Cell.WEST) {
			this.setWest(false);
			return true;
		}
		if (direction == Cell.SOUTH) {
			this.setSouth(false);
			return true;
		}
		if (direction == Cell.EAST) {
			this.setEast(false);
			return true;
		}
		return false;
	}

	/***
	 * Returns the first wall that is false checking them in the NSWE order
	 * 
	 * @return N, S, W, E if one is open, empty char if all walls are there.
	 */
	public char getFirstOpenWallNSWE() throws Exception {
		if (!this.north)
			return Cell.NORTH;
		if (!this.south)
			return Cell.SOUTH;
		if (!this.west)
			return Cell.WEST;
		if (!this.east)
			return Cell.EAST;
		throw new Exception("No open walls");
	}

	/**
	 * Compares the given cell with current cell's coordinates and name
	 * @param cell
	 * @return true if match, false otherwise
	 */
	public boolean equals(Cell c) {
		if (this.getRow() == c.getRow() 
				&& this.getCol() == c.getCol()
				&& this.getName() == c.getName())
			return true;
		return false;
	}
	
	public boolean getWallForDirection(char direction) throws Exception{
		if (direction == Cell.NORTH) {
			if (this.isNorth()) {
				return true;
			} else {
				return false;
			}
		}else if (direction == Cell.SOUTH) {
			if (this.isSouth()) {
				return true;
			} else {
				return false;
			}
		}else if (direction == Cell.WEST) {
			if (this.isWest()) {
				return true;
			} else {
				return false;
			}
		} else if (direction == Cell.EAST) {
			if (this.isEast()) {
				return true;
			} else {
				return false;
			}
		}
		throw new Exception ("No such direction");
	}

}
