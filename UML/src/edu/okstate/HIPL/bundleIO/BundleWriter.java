/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.okstate.HIPL.bundleIO;

import edu.okstate.HIPL.bundle.BundleFile;
import edu.okstate.HIPL.image.HImage;
import edu.okstate.HIPL.util.Config;
import java.io.File;
import java.io.InputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

/**
 *
 * @author sridhar
 */
public interface BundleWriter {
    
    void openToWrite();
    
    void appendImage(HImage himage);
    
    void appendImage(InputStream inputstream);
    
    void appendImage(File file);
    
    void appendBundle(Path path,Configuration conf);
    
    Config getConfiguration();
    
    long getImageCount();
    
    void close();

     BundleFile getBundleFile();
    
}
