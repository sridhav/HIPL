/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.okstate.HIPL.util;

import com.okstate.HIPL.image.HImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sridhar
 */
public class NativeUtil {
    
    public static void loadFromJar(String path) throws IOException{
       
        String[] parts=path.split("/");
      //  System.out.println(parts.length);
        String filename=(parts.length>1)?parts[parts.length-1]:null;
        String prefix="";
        String suffix=null;
        Logger.getLogger("FILENAME "+filename).log(Level.WARNING, null, filename);
        //System.out.println(filename);
        if (filename != null) {
            parts = filename.split(".");
            prefix = parts[0];
            suffix = (parts.length < 1) ? "."+parts[parts.length - 1] : null;
        }
         Logger.getLogger("parts "+parts.length).log(Level.WARNING, null, filename);
        File temp=File.createTempFile(prefix, suffix);
        temp.deleteOnExit();
        
        byte[] buffer =new byte[1024];
        int read;
        
        InputStream is=NativeUtil.class.getResourceAsStream(path);
         Logger.getLogger("parts "+NativeUtil.class.getCanonicalName()).log(Level.WARNING, null, filename);
         
        if (is == null) {
            throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }
        
        OutputStream os=new FileOutputStream(temp);
        if(is!=null){
            while((read=is.read())!=-1){
                os.write(buffer,0,read);
            }
            os.close();
            is.close();
        }
        System.load(temp.getAbsolutePath());
    }
    
    public static void main(String args[]) throws IOException{
        InputStream x=(NativeUtil.class.getResourceAsStream("lib/x86/opencv_java249.dll"));
        
       
        NativeUtil.loadFromJar("./src/lib/x86/opencv_java249.dll");
    }
    
}


