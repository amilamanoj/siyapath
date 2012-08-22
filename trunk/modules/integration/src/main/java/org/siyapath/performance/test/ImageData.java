package org.siyapath.performance.test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ImageData {

    public byte[] getImageData(){

        BufferedImage source;
        byte[] binImage;
        byte[] inputData;

        try {
            source = ImageIO.read(ImageData.class.getResource("/demo_car.jpg"));
            binImage = imageToBinary(source);
            inputData = new byte[binImage.length + 2];
            System.arraycopy(binImage, 0, inputData, 2, binImage.length);
            return inputData;

        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
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
