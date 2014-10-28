/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okstate.HIPL.bundle;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

/**
 *
 * @author sridhar
 */
public class BundleFile {
    private Path _filepath;
    private Configuration _conf;
    public BundleFile(String path, Configuration conf){
        _filepath=new Path(path);
        _conf=conf;
    }
    
    public BundleFile(Path path, Configuration conf){
        _filepath=path;
        _conf=conf;
    }

  
    
    public String getName(){
        return null;
    }
    public String getPathAsString(){
        return null;
    }
    
    public Path getPath(){
        return _filepath;
    }
    public Configuration getConfiguration(){
        return _conf;
    }
}
