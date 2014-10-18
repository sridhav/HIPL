package com.okstate.HIPL.bundle;

import com.okstate.HIPL.image.HImage;
import com.okstate.HIPL.image.HImageEncoder;

import java.io.File;
import java.io.InputStream;

import java.nio.file.Path;

import java.util.Collection;

public class MapImageBundle implements ImageBundle {
    /**
     * @associates <{uml.ImageHeader}>
     */
    private Collection newAtt;

    @Override
    public void addImage(HImage himage) {
        // TODO Implement this method
    }

    @Override
    public void addImage(InputStream inputStream) {
        // TODO Implement this method
    }

    @Override
    public void addImage(File file) {
        // TODO Implement this method
    }

    @Override
    public void addImage(Path path) {
        // TODO Implement this method
    }

    @Override
    public void addImage(HImage himage, HImageEncoder encoder, int type) {
        // TODO Implement this method

    }

    @Override
    public void addImage(HImage himage, HImageEncoder encoder) {
        // TODO Implement this method

    }

    @Override
    public void addImage(HImage himage, int type) {
        // TODO Implement this method

    }

    @Override
    public HImage[] getImages() {
        // TODO Implement this method
        return new HImage[0];
    }

    @Override
    public int getImageCount() {
        // TODO Implement this method
        return 0;
    }

    @Override
    public void close() {
        // TODO Implement this method
    }

    @Override
    public Path getPath() {
        // TODO Implement this method
        return null;
    }

    @Override
    public boolean hasNext() {
        // TODO Implement this method
        return false;
    }

    @Override
    public HImage getCurrentImage() {
        // TODO Implement this method
        return null;
    }

    @Override
    public void openToRead() {
        // TODO Implement this method
    }

    @Override
    public void openToWrite() {
        // TODO Implement this method
    }

    @Override
    public HImage getImage() {
        // TODO Implement this method
        return null;
    }

    @Override
    public void open(int mode, int overwrite) {
        // TODO Implement this method

    }

    @Override
    public void open(int mode) {
        // TODO Implement this method
    }
}
