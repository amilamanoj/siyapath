/*
 * Distributed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.siyapath.performance.test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains specific methods for using an image edge detection as the task for performance test
 */
public class ImageData {

    /**
     *
     * @return inputData
     */
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

    /**
     *
     * @param image
     * @return byte array after converting given image
     */
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
