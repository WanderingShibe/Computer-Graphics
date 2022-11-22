package sample;

public class Coordinate {
	private int x;
	private int y;

	/**
	 * Initialises a coordinate
	 * 
	 * @param x X coord
	 * @param y Y coord
	 */
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the actual X
	 * 
	 * @return X value
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Returns the actual Y
	 * 
	 * @return Y value
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * As the canvas is rotated, these methods are used for drawing on the
	 * canvas
	 * 
	 * @return Y Value
	 */
	public int drawX() {
		return this.y;
	}

	/**
	 * As the canvas is rotated, these methods are used for drawing on the
	 * canvas
	 * 
	 * @return X Value
	 */
	public int drawY() {
		return this.x;
	}
}