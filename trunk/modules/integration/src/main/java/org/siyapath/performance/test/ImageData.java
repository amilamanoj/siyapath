package org.siyapath.performance.test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ImageData {

    public byte[] getImageData(){

        BufferedImage source;
        byte[] imageBytes = new byte[1];
        try {
            source = ImageIO.read(ImageData.class.getResource("/demo_car.jpg"));
            imageBytes = imageToBinary(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBytes;

    }

    public byte[] imageToBinary(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

}
