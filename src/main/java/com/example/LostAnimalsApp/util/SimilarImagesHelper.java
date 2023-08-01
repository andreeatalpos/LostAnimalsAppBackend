package com.example.LostAnimalsApp.util;

import com.example.LostAnimalsApp.model.Animal;
import com.example.LostAnimalsApp.model.Image;
import com.example.LostAnimalsApp.repository.AnimalRepository;
import com.example.LostAnimalsApp.repository.ImageRepository;
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
	private final AnimalRepository animalRepository;

	@Autowired
	private final ImageRepository imageRepository;


	public List<String> findSimilarImages(final String inputFilename) {
		try {
			final SavedModelBundle modelCats = SavedModelBundle.load("D:/University_things/An4/Licenta/Licenta/NeuralNetwork/LostAnimalsCNN/models/cats-breeds-transfer-learning", "serve");
			final SavedModelBundle modelDogs = SavedModelBundle.load("D:/University_things/An4/Licenta/Licenta/NeuralNetwork/LostAnimalsCNN/models/dogs-breeds-transfer-learning100", "serve");
			byte[] inputImageData = Files.readAllBytes(Path.of(IMAGES_FOLDER_PATH + inputFilename));
			Tensor<Float> inputTensor = preprocessInput(inputImageData);
			final String species = getSpeciesByFilename(inputFilename);
			final float[][] inputFeatures = "cat".equals(species) ? runInferenceCats(modelCats, inputTensor) :
					runInferenceDogs(modelDogs, inputTensor);
			final File datasetDir = new File(IMAGES_FOLDER_PATH);
			final List<String> fileNames = imageRepository.findAll()
					.stream()
					.map(Image::getFileName)
					.toList();
			final File[] imageFiles = datasetDir.listFiles((dir, name) -> {
				String lowercaseName = name.toLowerCase();
				return (lowercaseName.endsWith(".jpg")
						|| lowercaseName.endsWith(".jpeg")
						|| lowercaseName.endsWith(".png")
						|| lowercaseName.endsWith(".gif")
						|| lowercaseName.endsWith(".bmp"))
						&& fileNames.contains(lowercaseName);
			});
			final List<SimilarityPair> similarityPairs = new ArrayList<>();
			final Optional<Animal> searchedAnimal = animalRepository.findAnimalByImageFileName(inputFilename);

			for (File imageFile : imageFiles) {
				byte[] imageData = Files.readAllBytes(imageFile.toPath());
				Optional<Animal> currentAnimal = animalRepository.findAnimalByImageFileName(imageFile.getName());
				if (getSpeciesByFilename(imageFile.getName()).equals(species)
						&& searchedAnimal.isPresent() && currentAnimal.isPresent()
						&& searchedAnimal.get().getIsFound() != currentAnimal.get().getIsFound()
						&& !Objects.equals(searchedAnimal.get().getUser().getUsername(),
						currentAnimal.get().getUser().getUsername())) {
					final Tensor<Float> tensor = preprocessInput(imageData);

					float[][] features = "cat".equals(species) ? runInferenceCats(modelCats, tensor) :
							runInferenceDogs(modelDogs, tensor);
					double similarity = cosineSimilarity(inputFeatures[0], features[0]);

					similarityPairs.add(new SimilarityPair(imageFile.getName(), similarity));
				}
			}
			similarityPairs.sort(Collections.reverseOrder());
			final List<String> similarImages = new ArrayList<>();
			for (SimilarityPair pair : similarityPairs) {
				if (!Objects.equals(pair.getFilename(), inputFilename)) {
					similarImages.add(pair.getFilename());
				}
			}
			modelCats.close();
			inputTensor.close();

			return similarImages;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	private float[][] runInferenceCats(final SavedModelBundle model, final Tensor<Float> inputTensor) {
		// Perform inference
		final Session.Runner runner = model.session().runner();
		runner.feed("serving_default_input_7:0", inputTensor);
		runner.fetch("StatefulPartitionedCall:0");

		List<Tensor<?>> outputTensors = runner.run();
		if (outputTensors != null && outputTensors.size() > 0) {
			final Tensor<?> predictions = outputTensors.get(0);
			float[][] predictionsArray = new float[1][5];
			predictions.copyTo(predictionsArray);
			for (Tensor<?> tensor : outputTensors) {
				tensor.close();
			}
			return predictionsArray;
		} else {
			System.out.println("No output tensors received.");
			return new float[0][0];
		}
	}

	private float[][] runInferenceDogs(final SavedModelBundle model, final Tensor<Float> inputTensor) {
		// Perform inference
		Session.Runner runner = model.session().runner();
		runner.feed("serving_default_input_1:0", inputTensor);
		runner.fetch("StatefulPartitionedCall:0");

		List<Tensor<?>> outputTensors = runner.run();
		if (outputTensors != null && outputTensors.size() > 0) {
			final Tensor<?> predictions = outputTensors.get(0);
			float[][] predictionsArray = new float[1][6];
			predictions.copyTo(predictionsArray);
			for (Tensor<?> tensor : outputTensors) {
				tensor.close();
			}

			return predictionsArray;
		} else {
			System.out.println("No output tensors received.");
			return new float[0][0];
		}
	}

	public String getSpeciesByFilename(final String filename) {
		final Optional<Animal> animal = animalRepository.findAnimalByImageFileName(filename);
		return animal
				.map(Animal::getSpecies)
				.orElse(null);
	}
	private Tensor<Float> preprocessInput(final byte[] imageData) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
		BufferedImage image = ImageIO.read(inputStream);
		int width = image.getWidth();
		int height = image.getHeight();
		int channels = image.getRaster().getNumBands();
		float[][][][] preprocessedData = new float[1][height][width][channels];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int[] pixel = image.getRaster().getPixel(j, i, new int[channels]);
				for (int k = 0; k < channels; k++) {
					preprocessedData[0][i][j][k] = normalizePixelValue(pixel[k] & 0xFF);
				}
			}
		}

		return Tensor.create(preprocessedData, Float.class);
	}

	private double cosineSimilarity(final float[] vectorA, final float[] vectorB) { // similitudinea intre doi vectori cosineSimilarity(A, B) = (A ⋅ B) / (||A|| ⋅ ||B||)
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

	private float normalizePixelValue(final int pixelValue) {
		return pixelValue / 255.0f;
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
		public int compareTo(final SimilarityPair other) {
			return Double.compare(similarity, other.getSimilarity());
		}
	}
}
