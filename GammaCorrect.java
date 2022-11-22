package sample;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class GammaCorrect {

	/**
	 * Corrects the gamma of the supplied image using the input gamma
	 * 
	 * @param image The image to correct
	 * @param gamma The gamma value
	 * @return Gamma corrected image
	 */
	public static Image correctGamma(Image image, double gamma) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();

		// Get index table
		double[] vTable = populateIndex(gamma);

		// New image to return
		WritableImage gammaCorrected = new WritableImage(width, height);

		// Open writer on new image, reader on old image
		PixelWriter gammaCorWriter = gammaCorrected.getPixelWriter();
		PixelReader imageReader = image.getPixelReader();

		// Loop through every pixel in the image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Read pixel from the image
				Color color = imageReader.getColor(x, y);

				// Get gamma corrected values from table
				double red = vTable[(int) (color.getRed() * 255)];
				double green = vTable[(int) (color.getGreen() * 255)];
				double blue = vTable[(int) (color.getBlue() * 255)];

				// Create new pixel and change pixel
				color = Color.color(red, green, blue);
				gammaCorWriter.setColor(x, y, color);
			}
		}
		return gammaCorrected;
	}

	// Example function of invert
	public static Image ImageInverter(Image image) {
		// Find the width and height of the image to be process
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		// Create a new image of that width and height
		WritableImage inverted_image = new WritableImage(width, height);
		// Get an interface to write to that image memory
		PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
		// Get an interface to read from the original image passed as the parameter to
		// the function
		PixelReader image_reader = image.getPixelReader();

		// Iterate over all pixels
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// For each pixel, get the colour
				Color color = image_reader.getColor(x, y);
				// Do something (in this case invert) - the getColor function returns colours as
				// 0..1 doubles (we could multiply by 255 if we want 0-255 colours)
				color = Color.color(1.0 - color.getRed(), 1.0 - color.getGreen(), 1.0 - color.getBlue());
				// Note: for gamma correction you may not need the divide by 255 since getColor
				// already returns 0-1, nor may you need multiply by 255 since the Color.color
				// function consumes 0-1 doubles.

				// Apply the new colour
				inverted_image_writer.setColor(x, y, color);
			}
		}
		return inverted_image;
	}

	/**
	 * Populates the lookup table (index) with values
	 * 
	 * @param gamma The gamma value from the slider
	 * @return The lookup table
	 */
	private static double[] populateIndex(double gamma) {
		final int POSSIBLE_VALS = 256;
		double[] vTable = new double[POSSIBLE_VALS];

		// Loop through 0-255 and insert what the value would be for each possible
		// colour
		for (int x = 0; x < vTable.length; x++) {
			vTable[x] = Math.pow(x / 255.0, 1.0 / gamma);
		}

		return vTable;
	}

}
