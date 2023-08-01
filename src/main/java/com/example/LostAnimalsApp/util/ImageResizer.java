package com.example.LostAnimalsApp.util;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ImageResizer {

	public byte[] resizeImage(final byte[] imageData, final int width, final int height) {
		try {
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
			final BufferedImage image = ImageIO.read(inputStream);
			final BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			final Graphics2D g2d = resizedImage.createGraphics();
			g2d.drawImage(image, 0, 0, width, height, null);
			g2d.dispose();

			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, "JPEG", outputStream);
			final byte[] resizedImageData = outputStream.toByteArray();
			outputStream.close();

			return resizedImageData;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
