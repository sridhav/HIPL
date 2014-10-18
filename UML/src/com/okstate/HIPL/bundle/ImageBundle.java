package com.okstate.HIPL.bundle;

import com.okstate.HIPL.image.HImage;

import com.okstate.HIPL.image.HImageEncoder;

import com.okstate.HIPL.image.HImageType;

import java.io.File;
import java.io.InputStream;

import java.nio.file.Path;

import java.util.Collection;

public interface ImageBundle {
    /**
     * Adds Image to the ImageBundle. HImage is taken as 
     */
    void addImage(HImage himage);
    
    void addImage(InputStream inputStream);
    
    void addImage(File file);
    
    void addImage(Path path);
    
    public HImage[] getImages();

    int getImageCount();

    public void close();

    Path getPath();

    boolean hasNext();

    HImage getCurrentImage();

    void openToRead();

    void openToWrite();

    HImage getImage();

    void open(int mode, int overwrite);

    void open(int mode);

    void addImage(HImage himage, HImageEncoder encoder, int type);

    void addImage(HImage himage, HImageEncoder encoder);
    
    void addImage(HImage himage, int type);
}
