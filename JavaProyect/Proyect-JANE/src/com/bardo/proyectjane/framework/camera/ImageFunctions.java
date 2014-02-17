package com.bardo.proyectjane.framework.camera;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageFunctions {
	public static final int STD_CAMERA_WIDTH = 640;
	public static final int STD_CAMERA_HEGHT = 320;

	public static float[][][] fromByte2Matrix(byte[] bytes) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			System.out.println("ModuleCamera: Error treating with the image");
		}

		float[][][] image = new float[STD_CAMERA_HEGHT][STD_CAMERA_WIDTH][3];

		for (int i = 0; i < STD_CAMERA_WIDTH; i++) {
			for (int j = 0; j < STD_CAMERA_HEGHT; j++) {
				image[i][j][0] = (img.getRGB(i, j) >> 16) & 0xff;
				image[i][j][1] = (img.getRGB(i, j) >> 8) & 0xff;
				image[i][j][2] = img.getRGB(i, j) & 0xFF;
			}
		}
		return image;
	}

	public static float[][] fromByte2MatrixGrey(byte[] bytes) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			System.out.println("ModuleCamera: Error treating with the image");
		}

		float[][] image = new float[STD_CAMERA_HEGHT][STD_CAMERA_WIDTH];

		for (int i = 0; i < STD_CAMERA_WIDTH; i++) {
			for (int j = 0; j < STD_CAMERA_HEGHT; j++) {
				int r = (img.getRGB(i, j) >> 16) & 0xff;
				int g = (img.getRGB(i, j) >> 8) & 0xff;
				int b = img.getRGB(i, j) & 0xFF;

				image[i][j] = (float) (0.4 * r + 0.3 * g + 0.3 * b);
			}
		}
		return image;
	}

	public static float[][] changesImage(float[][] img, float[][] lastImg) {

		float[][] resImg = new float[STD_CAMERA_HEGHT][STD_CAMERA_WIDTH];

		for (int i = 0; i < STD_CAMERA_HEGHT; i++) {
			for (int j = 0; j < STD_CAMERA_WIDTH; j++) {
				resImg[i][j] = Math.abs(img[i][j] - lastImg[i][j]);
			}
		}

		return null;
	}
}
