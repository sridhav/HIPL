package com.okstate.HIPL.rip;

import java.io.File;

public class FolderRipper implements Ripper {
    
    File file=null;
    
    public FolderRipper(String path){
        this.file=new File(path);
    }
    public FolderRipper(File file){
        this.file=file;
    }
    
    @Override
    public String[] getURLs() {
        // TODO Implement this method
        File[] files=null;
        if(file.isDirectory()){
            files=file.listFiles();
        }
        else{
            System.out.println("INVALID DIRECTORY");
        }
        String[] out=new String[files.length];
        for(int i=0;i<files.length;i++){
            out[i]=files[i].getAbsolutePath();
        }
        return new String[0];
    }

    @Override
    public void writeURLFile(File file) {
        // TODO Implement this method
    }
}
