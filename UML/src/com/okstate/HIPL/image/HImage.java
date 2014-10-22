package com.okstate.HIPL.image;

import com.okstate.HIPL.header.HImageHeader;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.InputStream;

import org.opencv.core.Mat;

public class HImage {
    /**
     * Constructor to create an HImage which holds the image and header data.
     * @param file - Generates HImage from a File.
     */
    public HImage(File file) {
    
    }
    /**
     * Constructor to create an HImage which holds the image and header data.
     * @param inputstream - Generates HImage from InputStream.
     */
    
    public HImage(InputStream inputstream){
        
    }
    /**
     * Constructor to create an HImage which holds the image and header data.
     */
    public HImage(){
        
    }
    /**
     * Gets the BufferedImage for the current HImage object.
     * @return image pixel data is returned as BufferedImage
     */
    public BufferedImage getBufferedImage() {
        return null;
    }
    /**
     * Gets the Mat for the current HImage object.
     * @return image pixel data is returned as Mat.
     */
    public Mat getMatImage() {
        return null;
    }
    /**
     * Gets the image header data or exif data from the image.
     * @return exif data is returned as HImageHeader
     */
    public HImageHeader getImageHeader() {
        return null;
    }
    /**
     * Sets the Image or adds image to the object
     * @param bufferedImage - adds BufferedImage image data.
     */
    public void setImage(BufferedImage bufferedImage){
        
    }
    /**
     * Sets the Image or adds image to the object
     * @param mat - adds Mat image data.
     */
    public void setImage(Mat mat){
        
    }
    /**
     *Sets the image header or exif data tothe object
     * @param imageheader - adds imageheader to current HImageObject
     */
    public void setImageHeader(HImageHeader imageheader){
        
    }
}
