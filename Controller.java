package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Controller {
	// The original image, no edits
	private Image originalImage;

	@FXML
	private ImageView imagePort;

	@FXML
	private Slider gammaSlider;

	@FXML
	private Label gamValLbl;

	@FXML
	private Canvas contGraph;

	@FXML
	private AnchorPane redPane;

	@FXML
	private AnchorPane bluePane;

	@FXML
	private AnchorPane greenPane;

	@FXML
	private AnchorPane lightPane;

	// The graph emulator object used in contrast stretching
	private GraphEm graph;

	// False = Show histograms, otherwise show value charts
	private boolean histogramMode = true;

	/**
	 * Sets up the window and adds listeners to certain components to get
	 * values from them.
	 * 
	 * @throws FileNotFoundException
	 */

	public void initialize() throws FileNotFoundException {
		// Load image
		Image image = new Image(new FileInputStream("raytrace.jpg"));
		imagePort.setImage(image);
		originalImage = image;

		/**
		 * Listener for the gamma slider to update as it is moved
		 */
		gammaSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double gamma = Math.round(gammaSlider.getValue() * 10) / 10.0;

				Image gamIm = GammaCorrect.correctGamma(originalImage,
						gamma);

				imagePort.setImage(gamIm);
				gamValLbl.setText("Value: " + gamma);
			}
		});

		// Create the graphs
		graph = new GraphEm(contGraph);
		imagePort.setImage(graph.updateContrast(imagePort.getImage()));

		gammaSlider.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				update();
			}
		});

		/**
		 * Listener for the graph, moves the point if one is clicked on and
		 * dragged
		 */
		contGraph.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// Checks boundaries
				if ((event.getY() <= 255 && event.getY() >= 0) && (event.getX() <= 255 && event.getX() >= 0)) {
					// Coordinates are put in the other way around here as the
					// canvas has been rotated so they are swapped around
					graph.movePoint(new Coordinate((int) event.getY(),
							(int) event.getX()));
				}
			}
		});

		/**
		 * Listener for when the mouse is released to update the graphs. This
		 * reduces stutters compared to updating everytime the point is moved
		 */
		contGraph.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				// Once the mouse button is released, update the image
				imagePort.setImage(graph.updateContrast(originalImage));
				update();
			}
		});

		update();
	}

	/**
	 * This method hardcodes every graph to be displayed on the canvas
	 */
	public void doGraphs() {
		// Clear graphs
		redPane.getChildren().clear();
		bluePane.getChildren().clear();
		greenPane.getChildren().clear();
		lightPane.getChildren().clear();

		// Red Axis
		final NumberAxis redYAxis = new NumberAxis();
		final NumberAxis redXAxis = new NumberAxis(0, 255, 5);

		// Green Axis
		final NumberAxis greenYAxis = new NumberAxis();
		final NumberAxis greenXAxis = new NumberAxis(0, 255, 5);

		// Blue Axis
		final NumberAxis blueAxis = new NumberAxis();
		final NumberAxis blueXAxis = new NumberAxis(0, 255, 5);

		// Brightness axis
		final NumberAxis brightAxis = new NumberAxis();
		final NumberAxis brightXAxis = new NumberAxis(0, 100, 5);

		// Red Chart
		final AreaChart<Number, Number> redChart = new AreaChart<Number, Number>(redXAxis,
				redYAxis);
		redChart.setMaxHeight(340);
		redChart.setMaxWidth(500);
		redPane.getChildren().add(redChart);
		redChart.setCreateSymbols(false);
		redChart.setLegendVisible(false);

		// Green Chart
		final AreaChart<Number, Number> greenChart = new AreaChart<Number, Number>(greenXAxis,
				greenYAxis);
		greenChart.setMaxHeight(340);
		greenChart.setMaxWidth(500);
		greenPane.getChildren().add(greenChart);
		greenChart.setCreateSymbols(false);
		greenChart.setLegendVisible(false);

		// Blue Chart
		final AreaChart<Number, Number> blueChart = new AreaChart<Number, Number>(blueXAxis,
				blueAxis);
		blueChart.setMaxHeight(340);
		blueChart.setMaxWidth(500);
		bluePane.getChildren().add(blueChart);
		blueChart.setCreateSymbols(false);
		blueChart.setLegendVisible(false);

		// Brightness Chart
		final AreaChart<Number, Number> brightChart = new AreaChart<Number, Number>(brightXAxis,
				brightAxis);
		brightChart.setMaxHeight(340);
		brightChart.setMaxWidth(500);
		lightPane.getChildren().add(brightChart);
		brightChart.setCreateSymbols(false);
		brightChart.setLegendVisible(false);

		// Get values for amount of each colour at each intensity
		XYChart.Series red = new XYChart.Series();
		XYChart.Series green = new XYChart.Series();
		XYChart.Series blue = new XYChart.Series();
		XYChart.Series bright = new XYChart.Series();

		int[] redValues;
		int[] greenValues;
		int[] blueValues;
		int[] brightValues;

		// Get numbers for each value depending on the histogram mode
		if (histogramMode) {
			redValues = Histogram.getRedHisto(imagePort.getImage());
			greenValues = Histogram.getGreenHisto(imagePort.getImage());
			blueValues = Histogram.getBlueHisto(imagePort.getImage());
			brightValues = Histogram.getBrightHisto(imagePort.getImage());
		} else {
			redValues = Histogram.getRedAmounts(imagePort.getImage());
			greenValues = Histogram.getGreenAmounts(imagePort.getImage());
			blueValues = Histogram.getBlueAmounts(imagePort.getImage());
			brightValues = Histogram.getBrightAmounts(imagePort.getImage());
		}

		// Fill data sets from arrays
		for (int i = 0; i < redValues.length; i++) {
			red.getData().add(new XYChart.Data(i, redValues[i]));
		}

		for (int i = 0; i < greenValues.length; i++) {
			green.getData().add(new XYChart.Data(i, greenValues[i]));
		}

		for (int i = 0; i < blueValues.length; i++) {
			blue.getData().add(new XYChart.Data(i, blueValues[i]));
		}

		for (int i = 0; i < brightValues.length; i++) {
			bright.getData().add(new XYChart.Data(i, brightValues[i]));
		}

		// Add to graph
		redChart.getData().add(red);
		greenChart.getData().add(green);
		blueChart.getData().add(blue);
		brightChart.getData().add(bright);
	}

	/**
	 * Resets the image in the viewport to the original image
	 */
	public void handleReset() throws FileNotFoundException {
		// Reset Slider
		gammaSlider.setValue(1.0);
		gamValLbl.setText("Value: 1.0");

		// Load image
		Image image = new Image(new FileInputStream("raytrace.jpg"));
		originalImage = image;
		imagePort.setImage(originalImage);
		update();
	}

	/**
	 * Inverts the image when the invert button is pressed
	 */
	public void handleInvert() {
		Image invIm = GammaCorrect.ImageInverter(imagePort.getImage());
		originalImage = invIm;
		imagePort.setImage(invIm);
		update();
	}

	/**
	 * Switches the chart mode when the button is pressed
	 */
	public void handleHistogramBtn() {
		histogramMode = !histogramMode;
		update();
	}

	/**
	 * Equalises the original image when the button is pressed
	 */
	public void handleEqualiseBtn() {
		Image equalised = Histogram.equaliseImage(originalImage);
		imagePort.setImage(equalised);
		update();
	}

	/**
	 * Greyscales the original image when pressed
	 */
	public void handleGrey() {
		Image grey = Histogram.greyScale(originalImage);
		imagePort.setImage(grey);
		update();
	}

	/**
	 * Cross correlates the current image in the view port
	 */
	public void handleCorrelate() {
		Image correlate = CrossCorrelation.crossCorrel(imagePort.getImage());
		imagePort.setImage(correlate);
		update();
	}

	/**
	 * Updates the graphs and their values
	 */
	public void update() {
		doGraphs();
	}
}