package com.example.LostAnimalsApp.util;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Component
public class ImageResizer {

	public byte[] resizeImage(byte[] imageData, int width, int height) {
		try {
			// Read the image data from the byte array
			ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
			BufferedImage image = ImageIO.read(inputStream);

			// Resize the image to the desired dimensions
			BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = resizedImage.createGraphics();
			g2d.drawImage(image, 0, 0, width, height, null);
			g2d.dispose();

			// Convert the resized image to a byte array
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, "JPEG", outputStream);
			byte[] resizedImageData = outputStream.toByteArray();
			outputStream.close();

			return resizedImageData;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
