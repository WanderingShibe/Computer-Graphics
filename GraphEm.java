package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class GraphEm {
	/**
	 * This arraylist contains all points the user has made
	 * and place on the graph
	 */
	private ArrayList<Coordinate> points = new ArrayList<>();

	Coordinate point1 = new Coordinate(50, 50);
	Coordinate point2 = new Coordinate(200, 200);

	/**
	 * Canvas to draw on
	 */
	private Canvas canvas;

	/**
	 * Canvas to turn into a graph (MUST BE 255 / 255 in size)
	 * 
	 * @param canvas
	 */
	public GraphEm(Canvas canvas) {
		this.canvas = canvas;
		drawGraph();
	}

	/**
	 * Moves one of the two points on the graph
	 * 
	 * @param coords New updated coordinates
	 */
	public boolean movePoint(Coordinate coords) {
		if (coords.getX() > point1.getX() - 10 && coords.getX() < point1.getX() + 10) {
			// If X coordinates match for point 1
			if (coords.getY() > point1.getY() - 10 && coords.getY() < point1.getY() + 10) {
				// If Y coordinates match for point 1
				if (coords.getX() < point2.getX()) {
					// Point 1 cannot be placed right of point 2
					point1 = coords;
					drawGraph();
					return true;
				}
			}
		}

		if (coords.getX() > point2.getX() - 10 && coords.getX() < point2.getX() + 10) {
			// If X coordinates match for point 2
			if (coords.getY() > point2.getY() - 10 && coords.getY() < point2.getY() + 10) {
				// If Y coordinates match for point 2
				if (coords.getX() > point1.getX()) {
					// Point 2 cannot be behind point 1
					point2 = coords;
					drawGraph();
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Update the contrast of an image input by the current state of the graph
	 * 
	 * @param image Image to contrast stretch
	 * @return Contrast stretched image
	 */
	public Image updateContrast(Image image) {
		int height = (int) image.getHeight();
		int width = (int) image.getWidth();

		// New image to return
		WritableImage contrastStret = new WritableImage(width, height);

		// Open writer on new image, reader on old image
		PixelWriter contrastWriter = contrastStret.getPixelWriter();
		PixelReader imageReader = image.getPixelReader();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Read pixel from the image
				Color colour = imageReader.getColor(x, y);

				// Get gamma corrected values from table
				double red = getValue(colour.getRed() * 255.0);
				double green = getValue(colour.getGreen() * 255.0);
				double blue = getValue(colour.getBlue() * 255.0);

				// If statements to prevent error where values would go below 0
				if (red < 0) {
					red = 0;
				}

				if (green < 0) {
					green = 0;
				}

				if (blue < 0) {
					blue = 0;
				}

				// Create new pixel and change pixel
				colour = Color.color(red / 255.0, green / 255.0, blue / 255.0);
				contrastWriter.setColor(x, y, colour);
			}
		}

		return contrastStret;
	}

	/**
	 * Read through all point coordinates and draw points on graph
	 */
	private void drawGraph() {
		int prevX = 0;
		int prevY = 0;

		canvas.getGraphicsContext2D().clearRect(0, 0, 255, 255);

		// Draw the lines
		canvas.getGraphicsContext2D().strokeLine(0, 0,
				point1.drawX(),
				point1.drawY());
		canvas.getGraphicsContext2D().strokeLine(point1.drawX(),
				point1.drawY(),
				point2.drawX(),
				point2.drawY());

		// Draw squares at each point
		canvas.getGraphicsContext2D().strokeRect(point1.drawX() - 3,
				point1.drawY() - 3, 6, 6);

		canvas.getGraphicsContext2D().strokeRect(point2.drawX() - 3,
				point2.drawY() - 3, 6, 6);

		// Draw final line from point 2 to the end
		canvas.getGraphicsContext2D().strokeLine(point2.drawX(),
				point2.drawY(),
				255, 255);

	}

	/**
	 * Get stretched value from graph
	 * 
	 * @param input Original value
	 * @return Stretched value
	 */
	private double getValue(double input) {
		double xOne;
		double yOne;
		double xTwo;
		double yTwo;

		// Work out where the input is and set variables appropriately
		if (input > point1.getX()) {
			if (input > point2.getX()) {
				// After point 2
				xOne = point2.getX();
				yOne = point2.getY();
				xTwo = 255.0;
				yTwo = 255.0;

			} else {
				// Inbetween the points
				xOne = point1.getX();
				yOne = point1.getY();
				xTwo = point2.getX();
				yTwo = point2.getY();

			}
		} else {
			// Before point 1
			xOne = 0.0;
			yOne = 0.0;
			xTwo = point1.getX();
			yTwo = point1.getY();

		}

		// Calculate the gradient
		double gradient = (yTwo - yOne) / (xTwo - xOne);

		// Calculate new value
		double result = gradient * (input - xTwo) + yTwo;

		return result;
	}
}