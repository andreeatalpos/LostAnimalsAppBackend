package com.example.LostAnimalsApp.util;

import org.springframework.stereotype.Service;
import org.tensorflow.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

@Service
public class AnimalClassifier {
    private final static String IMAGES_FOLDER_PATH = "src/main/resources/images/";

    public String makePrediction(final String filename) {
        try {
            final byte[] imageData = Files.readAllBytes(Path.of(IMAGES_FOLDER_PATH + filename));
            final SavedModelBundle model = SavedModelBundle.load("D:/University_things/An4/Licenta/Licenta/NeuralNetwork/LostAnimalsCNN/models/cats-dogs-classifier111", "serve");
            final Graph graph = model.graph();
            final Iterator<Operation> iter = graph.operations();
            while (iter.hasNext()) {
                final Operation op = iter.next();
            }
            final Session.Runner runner = model.session().runner();
            runner.feed("serving_default_conv2d_4_input:0", preprocessInput(imageData));
            runner.fetch("StatefulPartitionedCall:0");

            final List<Tensor<?>> outputTensors = runner.run();
            if (outputTensors != null && outputTensors.size() > 0) {
                final Tensor<?> predictions = outputTensors.get(0);
                final float[][] predictionsArray = new float[1][1];
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

        final int width = image.getWidth();
        final int height = image.getHeight();
        final int channelsNumber = image.getRaster().getNumBands();

        float[][][][] preprocessedData = new float[1][height][width][channelsNumber];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int[] pixel = image.getRaster().getPixel(j, i, new int[channelsNumber]);
                for (int k = 0; k < channelsNumber; k++) {
                    preprocessedData[0][i][j][k] = normalizePixelValue(pixel[k] & 0xFF);
                }
            }
        }

        return Tensor.create(preprocessedData, Float.class);
    }

    private float normalizePixelValue(final int pixelValue) {
        return pixelValue / 255.0f;
    }


    private String processPredictions(final float[][] predictions) {
        final String[] classes = {"cat", "dog"};
        return predictions[0][0] < 0.5 ? classes[0] : classes[1];
    }

}
