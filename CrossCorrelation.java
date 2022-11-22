package sample;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class CrossCorrelation {
	// Filter
	static int[][] laplacian = new int[5][5];
	private static int[][] redArray;
	private static int[][] greenArray;
	private static int[][] blueArray;

	/**
	 * Hardcoded filter
	 */
	private static void lapArray() {
		laplacian[0][0] = -4;
		laplacian[0][1] = -1;
		laplacian[0][2] = 0;
		laplacian[0][3] = -1;
		laplacian[0][4] = -4;
		laplacian[1][0] = -1;
		laplacian[1][1] = 2;
		laplacian[1][2] = 3;
		laplacian[1][3] = 2;
		laplacian[1][4] = -1;
		laplacian[2][0] = 0;
		laplacian[2][1] = 3;
		laplacian[2][2] = 4;
		laplacian[2][3] = 3;
		laplacian[2][4] = 0;
		laplacian[3][0] = -1;
		laplacian[3][1] = 2;
		laplacian[3][2] = 3;
		laplacian[3][3] = 2;
		laplacian[3][4] = -1;
		laplacian[4][0] = -4;
		laplacian[4][1] = -1;
		laplacian[4][2] = 0;
		laplacian[4][3] = -1;
		laplacian[4][4] = -4;
	}

	/**
	 * Takes an image and cross correlates it using a laplacian matrix (keeps
	 * black edges)
	 * 
	 * @param image Image to cross correlate
	 * @return Cross correlated image
	 */
	public static Image crossCorrel(Image image) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		redArray = new int[height][width];
		greenArray = new int[height][width];
		blueArray = new int[height][width];

		int redSum = 0;
		int greenSum = 0;
		int blueSum = 0;

		int redMin = 99999;
		int redMax = 0;
		int greenMin = 99999;
		int greenMax = 0;
		int blueMin = 99999;
		int blueMax = 0;

		// New image to return
		WritableImage output = new WritableImage(width, height);

		// Open writer on new image, reader on old image
		PixelWriter imageWriter = output.getPixelWriter();
		PixelReader imageReader = image.getPixelReader();

		// Initalise laplacian array
		lapArray();

		// Loop through image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				// Check that the matrix can be centered over the pixel
				if (x > 3 && x < width - 3) {
					if (y > 4 && y < height - 3) {

						// Calculate sum of values from filter on current pixel
						for (int i = y - 2; i < y + 2; i++) {
							for (int c = x - 2; c < x + 2; c++) {
								Color colour = imageReader.getColor(c, i);

								redSum += (int) (colour.getRed() * 255.0);
								greenSum += (int) (colour.getGreen() * 255.0);
								;
								blueSum += (int) (colour.getBlue() * 255.0);
							}
						}

						// Place the sums for this pixel into the array
						redArray[y][x] = redSum;
						greenArray[y][x] = greenSum;
						blueArray[y][x] = blueSum;

						// Reset values
						redSum = 0;
						greenSum = 0;
						blueSum = 0;
					}
				}
			}
		}

		// Find max and min
		for (int y = 2; y < height - 2; y++) {
			for (int x = 0; x < width - 2; x++) {
				if (redArray[y][x] > redMax) {
					redMax = redArray[y][x];
				} else if (redArray[y][x] < redMin) {
					redMin = redArray[y][x];
				}

				if (greenArray[y][x] > greenMax) {
					greenMax = greenArray[y][x];
				} else if (greenArray[y][x] < greenMin) {
					greenMin = greenArray[y][x];
				}

				if (blueArray[y][x] > blueMax) {
					blueMax = blueArray[y][x];
				} else if (blueArray[y][x] < blueMin) {
					blueMin = blueArray[y][x];
				}
			}
		}

		// Normalise each pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Normalise the pixel
				double redNormalised = ((redArray[y][x] - redMin) * 255) / (redMax - redMin);
				double greenNormalised = ((greenArray[y][x] - greenMin) * 255) / (greenMax - greenMin);
				double blueNormalised = ((blueArray[y][x] - blueMin) * 255) / (blueMax - blueMin);

				// Round to 2 d.p.
				redNormalised = Math.round(redNormalised * 100.0) / 100.0;
				greenNormalised = Math.round(greenNormalised * 100.0) / 100.0;
				blueNormalised = Math.round(blueNormalised * 100.0) / 100.0;

				// Make new colour and set
				Color newColour = new Color(redNormalised / 255.0,
						greenNormalised / 255.0,
						blueNormalised / 255.0,
						1);
				imageWriter.setColor(x, y, newColour);
			}
		}

		return output;
	}
}