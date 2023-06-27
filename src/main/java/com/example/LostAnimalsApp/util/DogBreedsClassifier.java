package com.example.LostAnimalsApp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class DogBreedsClassifier {
	private final static String IMAGES_FOLDER_PATH = "src/main/resources/images/";

	@Autowired
	private ImageResizer imageResizer;

	public String makePrediction(final String filename) {
		try {
			byte[] imageData = Files.readAllBytes(Path.of(IMAGES_FOLDER_PATH + filename));
			// Load the saved model
			SavedModelBundle model = SavedModelBundle.load("D:/University_things/An4/Licenta/Licenta/NeuralNetwork/LostAnimalsCNN/models/dogs-breeds-transfer-learning100", "serve");
			// Perform inference
			Session.Runner runner = model.session().runner();
			runner.feed("serving_default_input_1:0", preprocessInput(imageData));
			runner.fetch("StatefulPartitionedCall:0");

			List<Tensor<?>> outputTensors = runner.run();
			if (outputTensors != null && outputTensors.size() > 0) {
				Tensor<?> predictions = outputTensors.get(0);

				// Convert the TensorFlow Tensor to float[][]
				float[][] predictionsArray = new float[1][6];
				predictions.copyTo(predictionsArray);


				// Close the output tensors
				for (Tensor<?> tensor : outputTensors) {
					tensor.close();
				}

				// Process the predictions
				return processPredictions(predictionsArray);

			} else {
				// Handle the case when no output tensors are returned
				// Display an error message or take appropriate action
				System.out.println("No output tensors received.");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	private Tensor<Float> preprocessInput(final byte[] imageData) throws IOException {
		// Read the image from the byte array
		ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
		BufferedImage image = ImageIO.read(inputStream);

		// Extract the width, height, and number of channels from the image
		int width = image.getWidth();
		int height = image.getHeight();
		int channels = image.getRaster().getNumBands();

		// Convert the BufferedImage to a TensorFlow Tensor
		float[][][][] preprocessedData = new float[1][height][width][channels];

		// Iterate over each pixel and extract the pixel values
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


	private String processPredictions(float[][] predictions) {
		String[] classes = {"Beagle", "Chihuahua", "Husky", "Pug", "Samoyed", "Yorkshire Terrier"}; // Add more class labels as per your model

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

	public void getSimilarImages(final String fileName) throws IOException, InterruptedException {
		ProcessBuilder pythonCaller = new ProcessBuilder("python",
				"D:/University_things/An4/Licenta/Licenta/NeuralNetwork/LostAnimalsCNN/similar-images.py",
				"D:/University_things/An4/Licenta/Licenta/LostAnimalsAppBackend/src/main/resources/images/animal_63f8610a.jpeg",
				"D:/University_things/An4/Licenta/Licenta/LostAnimalsAppBackend/src/main/resources/images");

		Process process = pythonCaller.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		int exitCode = process.waitFor();
		System.out.println("Exit Code: " + exitCode);

	}
}
