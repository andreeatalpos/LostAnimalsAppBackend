package com.example.LostAnimalsApp.util;

import com.example.LostAnimalsApp.model.Animal;
import com.example.LostAnimalsApp.repository.AnimalRepository;
import com.example.LostAnimalsApp.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Component
@RequiredArgsConstructor
public class SimilarImagesHelper {
	private final static String IMAGES_FOLDER_PATH = "src/main/resources/images/";

	@Autowired
	private final AnimalClassifier animalClassifier;

	@Autowired
	private final AnimalRepository animalRepository;


	public List<String> findSimilarImages(final String inputFilename) {
		try {
			// Load the saved model
			SavedModelBundle model = SavedModelBundle.load("D:/University_things/An4/Licenta/Licenta/NeuralNetwork/LostAnimalsCNN/models/cats-breeds-transfer-learning", "serve");
			SavedModelBundle model2 = SavedModelBundle.load("D:/University_things/An4/Licenta/Licenta/NeuralNetwork/LostAnimalsCNN/models/cats-dogs-classifier111", "serve");

			// Read and preprocess the input image
			byte[] inputImageData = Files.readAllBytes(Path.of(IMAGES_FOLDER_PATH + inputFilename));
			Tensor<Float> inputTensor = preprocessInput(inputImageData);

			// Compute the feature vector for the input image
			float[][] inputFeatures = runInference(model, inputTensor);
			float[][] inputFeatures2 = runInference2(model2, inputTensor);
			// Get all image files from the dataset directory
			File datasetDir = new File(IMAGES_FOLDER_PATH);
			File[] imageFiles = datasetDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg")
					|| name.toLowerCase().endsWith(".jpeg")
					|| name.toLowerCase().endsWith(".png")
					|| name.toLowerCase().endsWith(".gif")
					|| name.toLowerCase().endsWith(".bmp"));

			// Calculate the similarity between the input image and dataset images
			List<SimilarityPair> similarityPairs = new ArrayList<>();
			final String species = getSpeciesByFilename(inputFilename);
			final Optional<Animal> searchedAnimal = animalRepository.findAnimalByImageFileName(inputFilename);

			for (File imageFile : imageFiles) {
				byte[] imageData = Files.readAllBytes(imageFile.toPath());
				Optional<Animal> currentAnimal = animalRepository.findAnimalByImageFileName(imageFile.getName());
				if (getSpeciesByFilename(imageFile.getName()).equals(species)
						&& searchedAnimal.isPresent() && currentAnimal.isPresent()
						&& searchedAnimal.get().getIsFound() != currentAnimal.get().getIsFound()) {
					Tensor<Float> tensor = preprocessInput(imageData);

					float[][] features = runInference(model, tensor);

					// Calculate the similarity between the input features and dataset features
					double similarity = cosineSimilarity(inputFeatures[0], features[0]);

					similarityPairs.add(new SimilarityPair(imageFile.getName(), similarity));
				}
			}

			// Sort the similarity pairs in descending order
			similarityPairs.sort(Collections.reverseOrder());

			// Get the filenames of the most similar images
			List<String> similarImages = new ArrayList<>();
			for (SimilarityPair pair : similarityPairs) {
				if (!Objects.equals(pair.getFilename(), inputFilename)) {

					similarImages.add(pair.getFilename());
				}
			}
			// Close the model and input tensor
			model.close();
			inputTensor.close();

			return similarImages;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	private float[][] runInference(SavedModelBundle model, Tensor<Float> inputTensor) {
		// Perform inference
		Session.Runner runner = model.session().runner();
		runner.feed("serving_default_input_7:0", inputTensor);
		runner.fetch("StatefulPartitionedCall:0");

		List<Tensor<?>> outputTensors = runner.run();
		if (outputTensors != null && outputTensors.size() > 0) {
			Tensor<?> predictions = outputTensors.get(0);

			// Convert the TensorFlow Tensor to float[][]
			float[][] predictionsArray = new float[1][5];
			predictions.copyTo(predictionsArray);

			// Close the output tensors
			for (Tensor<?> tensor : outputTensors) {
				tensor.close();
			}

			return predictionsArray;
		} else {
			// Handle the case when no output tensors are returned
			// Display an error message or take appropriate action
			System.out.println("No output tensors received.");

			return new float[0][0];
		}
	}

	public String getSpeciesByFilename(final String filename) {
		Optional<Animal> animal = animalRepository.findAnimalByImageFileName(filename);
		return animal
				.map(Animal::getSpecies)
				.orElse(null);
	}

	private float[][] runInference2(SavedModelBundle model, Tensor<Float> inputTensor) {
		// Perform inference
		Session.Runner runner = model.session().runner();
		runner.feed("serving_default_conv2d_input", inputTensor);
		runner.fetch("StatefulPartitionedCall");

		List<Tensor<?>> outputTensors = runner.run();
		if (outputTensors != null && outputTensors.size() > 0) {
			Tensor<?> predictions = outputTensors.get(0);

			// Convert the TensorFlow Tensor to float[][]
			float[][] predictionsArray = new float[1][1];
			predictions.copyTo(predictionsArray);

			// Close the output tensors
			for (Tensor<?> tensor : outputTensors) {
				tensor.close();
			}

			return predictionsArray;
		} else {
			// Handle the case when no output tensors are returned
			// Display an error message or take appropriate action
			System.out.println("No output tensors received.");

			return new float[0][0];
		}
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

	private double cosineSimilarity(float[] vectorA, float[] vectorB) {
		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;

		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			normA += Math.pow(vectorA[i], 2);
			normB += Math.pow(vectorB[i], 2);
		}

		normA = Math.sqrt(normA);
		normB = Math.sqrt(normB);

		if (normA * normB == 0.0) {
			return 0.0;
		}

		return dotProduct / (normA * normB);
	}

	private static class SimilarityPair implements Comparable<SimilarityPair> {
		private String filename;
		private double similarity;

		public SimilarityPair(String filename, double similarity) {
			this.filename = filename;
			this.similarity = similarity;
		}

		public String getFilename() {
			return filename;
		}

		public double getSimilarity() {
			return similarity;
		}

		@Override
		public int compareTo(SimilarityPair other) {
			return Double.compare(similarity, other.getSimilarity());
		}
	}
}
