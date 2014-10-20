package com.okstate.HIPL.bundle;

import com.okstate.HIPL.image.HImage;
import com.okstate.HIPL.image.HImageEncoder;

import java.io.File;
import java.io.InputStream;

import java.nio.file.Path;

import java.util.Collection;

public class SequenceImageBundle implements ImageBundle {
    /**
     * @associates <{uml.ImageHeader}>
     */
    private Collection newAtt;

     /**
     * Adds Image to the SequenceImageBundle.
     * @param himage HImage class object holds image and image header data.
     * @see com.okstate.HIPL.image.HImage
     */
     @Override
    public void addImage(HImage himage) {

    }
     /**
     *  Adds Image to the SequenceImageBundle.
     *  @param inputStream reads data from InputStream
     *  @see java.io.InputStream
     */
     @Override
    public void addImage(InputStream inputStream) {

    }
     /**
     *Adds Image to the SequenceImageBundle
     * @param file reads data from File
     * @see java.io.File
     */
     @Override
    public void addImage(File file) {

    }
     /**
     *Adds Image to the SequenceImageBundle
     * @param path reads data from the path
     * @see @link java.nio.file.Path
     */
     @Override
    public void addImage(Path path) {

    }
     /**
      * Gets Images from the SequenceImageBundle
      * @return Returns HImage Array from the Image Bundle
      * @see com.okstate.HIPL.image.HImage
      */
     @Override
     public HImage[] getImages(){
      
        HImage[] l=new HImage[10];
        return l;
     }
     /**
     *Gets the image count in the SequenceImageBundle
     * @return returns image count
     *
     */
     @Override
    public int getImageCount() {
        return 10;
    }
     /**
      * closes the stream for this current SequenceImageBundle
      *
      */
     @Override
     public void close(){
         
     }
     /**
     *Gets the current SequenceImageBundle path of the HDFS
     * @return returns the imagebundle path
     * @see java.nio.file.Path
     */
     @Override
    public Path getPath() {
        Path x=null;
        return x;
    }
     
     /**
     *Checks whether if there are any images in the SequenceImageBundle.
     * @return a boolean indicating if there are any images there in SequenceImageBundle
     */
     @Override
    public boolean hasNext() {
        return false;
    }
     /**
     * Gets the current Image from the SequenceImageBundle
     *@param himage HImage class object holds image and image header data.
     * @return the current image of the SequenceImageBundle
     * @see com.okstate.HIPL.image.HImage
     *
     */
     @Override
    public HImage getCurrentImage() {
        return new HImage();
    }
     /**
     * Method to open SequenceImageBundle in Read Mode
     */
     @Override
    public void openToRead() {

    }
     /**
     * Method to open SequenceImageBundle in Write Mode
     */
     @Override
    public void openToWrite() {

    }
     /**
     * Gets HImage from the SequenceImageBundle
     * @param himage HImage class object holds image and image header data.
     * @return the current image of the SequenceImageBundle
     * @see com.okstate.HIPL.image.HImage
     *
     */
     @Override
    public HImage getImage() {
        return new HImage();
    }
     /**
     * Method to open SequenceImageBundle
     * @param mode determines to read or write
     * @param overwrite overwrites file
     */
     @Override
    public void open(int mode, int overwrite) {

    }
     /**
     *Method to open SequenceImageBundle
     * @param mode mode determines to read or write
     */
     @Override
    public void open(int mode) {

    }
     /**
     *
     * @param himage Image class object holds image and image header data.
     * @param encoder used to encode the image.
     * @param type defines the image type PNG, JPEG
     */
     @Override
    public void addImage(HImage himage, HImageEncoder encoder, int type) {

    }
     /**
     *@param himage Image class object holds image and image header data.
     * @param encoder used to encode the image.
     */
     @Override
     public void addImage(HImage himage, HImageEncoder encoder){
         
     }
     /**
     *
     * @param himage Image class object holds image and image header data.
     * @param type defines the image type PNG, JPEG
     */
     @Override
    public void addImage(HImage himage, int type) {

    }
}
