package com.example.LostAnimalsApp.util;

import org.springframework.stereotype.Service;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class CatBreedsClassifier {
	private final static String IMAGES_FOLDER_PATH = "src/main/resources/images/";

	public String makePrediction(final String filename) {
		try {
			final byte[] imageData = Files.readAllBytes(Path.of(IMAGES_FOLDER_PATH + filename));
			final SavedModelBundle model = SavedModelBundle.load("D:/University_things/An4/Licenta/Licenta/NeuralNetwork/LostAnimalsCNN/models/cats-breeds-transfer-learning100", "serve");
			final Session.Runner runner = model.session().runner();
			runner.feed("serving_default_input_1:0", preprocessInput(imageData));
			runner.fetch("StatefulPartitionedCall:0");

			List<Tensor<?>> outputTensors = runner.run();
			if (outputTensors != null && outputTensors.size() > 0) {
				Tensor<?> predictions = outputTensors.get(0);
				float[][] predictionsArray = new float[1][5];
				predictions.copyTo(predictionsArray);
				for (Tensor<?> tensor : outputTensors) {
					tensor.close();
				}
				return processPredictions(predictionsArray);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	private Tensor<Float> preprocessInput(final byte[] imageData) throws IOException {
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
		final BufferedImage image = ImageIO.read(inputStream);
		int width = image.getWidth();
		int height = image.getHeight();
		int channels = image.getRaster().getNumBands();

		float[][][][] preprocessedData = new float[1][height][width][channels];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int[] pixel = image.getRaster().getPixel(j, i, new int[channels]);
				for (int k = 0; k < channels; k++) {
					preprocessedData[0][i][j][k] = pixel[k] & 0xFF;
				}
			}
		}

		return Tensor.create(preprocessedData, Float.class);
	}


	private String processPredictions(final float[][] predictions) {
		final String[] classes = {"Bombay", "British shorthair", "Persian cat", "Siamese cat", "Sphynx"};
		int maxIndex = 0;
		float maxPrediction = predictions[0][0];

		for (int i = 1; i < predictions[0].length; i++) {
			if (predictions[0][i] > maxPrediction) {
				maxPrediction = predictions[0][i];
				maxIndex = i;
			}
		}
		return classes[maxIndex];
	}

}
