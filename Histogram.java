package sample;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Histogram {
	private static double[] mapping = new double[256];

	/**
	 * Gets the red values for a histogram
	 * 
	 * @param image Input image
	 * @return Array of red values
	 */
	public static int[] getRedHisto(Image image) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		int[] output = new int[256];

		PixelReader imageReader = image.getPixelReader();

		// Loop through every pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color colour = imageReader.getColor(x, y);
				output[(int) (colour.getRed() * 255)]++;

			}
		}

		return output;
	}

	/**
	 * Gets the green values for a histogram
	 * 
	 * @param image Input image
	 * @return Array of green values
	 */
	public static int[] getGreenHisto(Image image) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		int[] output = new int[256];

		PixelReader imageReader = image.getPixelReader();

		// Loop through every pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color colour = imageReader.getColor(x, y);
				output[(int) (colour.getGreen() * 255)]++;

			}
		}

		return output;
	}

	/**
	 * Gets the blue values for a histogram
	 * 
	 * @param image Input image
	 * @return Array of blue values
	 */
	public static int[] getBlueHisto(Image image) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		int[] output = new int[256];

		PixelReader imageReader = image.getPixelReader();

		// Loop through every pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color colour = imageReader.getColor(x, y);
				output[(int) (colour.getBlue() * 255)]++;
			}
		}

		return output;
	}

	/**
	 * Gets the brightness values for a histogram
	 * 
	 * @param image Input image
	 * @return Array of brightness values
	 */
	public static int[] getBrightHisto(Image image) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		int[] output = new int[101];

		PixelReader imageReader = image.getPixelReader();

		// Loop through every pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color colour = imageReader.getColor(x, y);
				output[(int) (Math.round(colour.getBrightness() * 100.0))]++;
			}
		}

		return output;
	}

	/**
	 * Gets the red values for a value chartt
	 * 
	 * @param image Input image
	 * @return Array of red values
	 */
	public static int[] getRedAmounts(Image image) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		int[] output = new int[256];

		PixelReader imageReader = image.getPixelReader();

		// Loop through every pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color colour = imageReader.getColor(x, y);
				output[(int) (colour.getRed() * 255)]++;
			}
		}

		for (int i = 1; i < output.length; i++) {
			output[i] = output[i] + output[i - 1];
		}

		return output;
	}

	/**
	 * Gets the green values for a value chartt
	 * 
	 * @param image Input image
	 * @return Array of green values
	 */
	public static int[] getGreenAmounts(Image image) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		int[] output = new int[256];

		PixelReader imageReader = image.getPixelReader();

		// Loop through every pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color colour = imageReader.getColor(x, y);
				output[(int) (colour.getGreen() * 255)]++;

			}
		}

		for (int i = 1; i < output.length; i++) {
			output[i] = output[i] + output[i - 1];
		}

		return output;
	}

	/**
	 * Gets the blue values for a value chartt
	 * 
	 * @param image Input image
	 * @return Array of blue values
	 */
	public static int[] getBlueAmounts(Image image) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		int[] output = new int[256];

		PixelReader imageReader = image.getPixelReader();

		// Loop through every pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color colour = imageReader.getColor(x, y);
				output[(int) (colour.getBlue() * 255)]++;
			}
		}

		for (int i = 1; i < output.length; i++) {
			output[i] = output[i] + output[i - 1];
		}

		return output;
	}

	/**
	 * Gets the brightness values for a value chartt
	 * 
	 * @param image Input image
	 * @return Array of brightness values
	 */
	public static int[] getBrightAmounts(Image image) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		int[] output = new int[101];

		PixelReader imageReader = image.getPixelReader();

		// Loop through every pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color colour = imageReader.getColor(x, y);
				output[(int) (colour.getBrightness() * 100)]++;
			}
		}

		for (int i = 1; i < output.length; i++) {
			output[i] = output[i] + output[i - 1];
		}

		return output;
	}

	/**
	 * Equalises the supplied image
	 * 
	 * @param input Image to equalise
	 * @return Equalised image
	 */
	public static Image equaliseImage(Image input) {
		int width = (int) input.getWidth();
		int height = (int) input.getHeight();
		int[] cumulativeFreq = new int[256];
		Image greyImage = greyScale(input);

		// Open writer on new image, reader on old image
		WritableImage output = new WritableImage(width, height);
		PixelWriter greyWriter = output.getPixelWriter();
		PixelReader imageReader = greyImage.getPixelReader();

		// Get the cumulative frequency of the image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color colour = imageReader.getColor(x, y);
				cumulativeFreq[(int) (colour.getBlue() * 255)]++;
			}
		}

		for (int i = 1; i < cumulativeFreq.length; i++) {
			cumulativeFreq[i] = cumulativeFreq[i] + cumulativeFreq[i - 1];
		}

		// Now create mapping
		createMapping(cumulativeFreq, width * height);

		// Reassign colours values to new intensity
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color colour = imageReader.getColor(x, y);
				double newIntensity = mapping[(int) (colour.getRed() * 255)];
				Color newColour = new Color(newIntensity, newIntensity,
						newIntensity, 1);

				greyWriter.setColor(x, y, newColour);
			}
		}

		return output;
	}

	/**
	 * Produces a greyscale version of an image
	 * 
	 * @param image Image to greyscale
	 * @return Greyscaled image
	 */
	public static Image greyScale(Image image) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();

		// New image to return
		WritableImage grey = new WritableImage(width, height);

		// Open writer on new image, reader on old image
		PixelWriter greyWriter = grey.getPixelWriter();
		PixelReader imageReader = image.getPixelReader();

		// Loop through every pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color colour = imageReader.getColor(x, y);
				double red = colour.getRed();
				double green = colour.getGreen();
				double blue = colour.getBlue();

				double average = (red + green + blue) / 3.0;
				Color newColor = new Color(average, average, average, 1);
				greyWriter.setColor(x, y, newColor);
			}
		}

		return grey;
	}

	/**
	 * Creates the equalisation mapping
	 * 
	 * @param histogram Histogram to map
	 * @param size      Size of the image
	 */
	private static void createMapping(int[] histogram, double size) {
		for (int i = 0; i < histogram.length; i++) {
			mapping[i] = (histogram[i] / size);
		}
	}
}