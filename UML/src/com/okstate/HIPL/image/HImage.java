package com.okstate.HIPL.image;

import com.okstate.HIPL.header.HImageHeader;

import java.awt.Frame;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class HImage {
    /**
     * Constructor to create an HImage which holds the image and header data.
     * @param file - Generates HImage from a File.
     */
    static{ System.load("/home/sridhar/NetBeansProjects/HIPL/UML/src/lib/libopencv_java249.so");}
    
    private byte[] imagebytes=null;
    private BufferedImage bufferedImage=null;
    private Mat mat=null;
    private HImageHeader imageheader=null;
    
    public HImage(File file) {
        FileInputStream fis=null;
        try {
            fis = new FileInputStream(file);
            imagebytes=new byte[(int)file.length()];
            fis.read(imagebytes);
        } catch (FileNotFoundException e) {
            System.out.println("FILE INPUT STREAM NOT FOUND");
        } catch (IOException e) {
            System.out.println("IO NOT FOUND");
        }
    }
    /**
     * Constructor to create an HImage which holds the image and header data.
     * @param inputstream - Generates HImage from InputStream.
     */
    public HImage(InputStream inputstream){
        ByteArrayOutputStream baos=null;
        byte buffer[]=new byte[4096];
        int read=0;
        try {
            baos=new ByteArrayOutputStream();
            while ((read = inputstream.read(buffer)) != -1) {
                baos.write(buffer);
            }
            imagebytes=baos.toByteArray();
        } catch (IOException e) {
            System.out.println("IO NOT FOUND");
        }
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
        if(bufferedImage==null){
            generateBufferedImage();
        }
        return bufferedImage;
    }
    /**
     * Gets the Mat for the current HImage object.
     * @return image pixel data is returned as Mat.
     */
    public Mat getMatImage() {
        if(mat==null){
            generateMatImage();
        }
        return mat;
    }
    /**
     * Gets the image header data or exif data from the image.
     * @return exif data is returned as HImageHeader
     */
    public HImageHeader getImageHeader() {
        if(imageheader==null){
            generateImageHeader();
        }
        return imageheader;
    }
    /**
     * Sets the Image or adds image to the object
     * @param bufferedImage - adds BufferedImage image data.
     */
    public void setImage(BufferedImage bufferedImage){
        this.bufferedImage=bufferedImage;
    }
    /**
     * Sets the Image or adds image to the object
     * @param mat - adds Mat image data.
     */
    public void setImage(Mat mat){
        this.mat=mat;
    }
    /**
     *Sets the image header or exif data tothe object
     * @param imageheader - adds imageheader to current HImageObject
     */
    public void setImageHeader(HImageHeader imageheader){
    
    }
    
    private void generateMatImage(){
        if(bufferedImage==null)
            generateBufferedImage();
        byte[] temp=((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
        mat=new Mat(bufferedImage.getWidth(),bufferedImage.getHeight(),CvType.CV_8UC3);
        mat.put(0,0,temp);
    }
    
    private void generateBufferedImage(){
        InputStream in=new ByteArrayInputStream(imagebytes);
        try {
            bufferedImage = ImageIO.read(in);
        } catch (IOException e) {
            System.out.println("IO NOT FOUND");
        }

    }

    private void generateImageHeader() {
        imageheader=new HImageHeader();
    }
    
    public static void main(String args[]) {    
            /* Create and display the form */
        HImage x=new HImage(new File("./sri.jpg"));
       Mat y=x.getMatImage();
        BufferedImage z=x.getBufferedImage();
        
        System.out.println(z.getHeight());
        
    }
}
