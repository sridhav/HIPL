/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okstate.HIPL.bundle;

import com.okstate.HIPL.bundleIO.BundleWriter;
import com.okstate.HIPL.bundleIO.HARBundleWriter;
import com.okstate.HIPL.bundleIO.MapBundleWriter;
import com.okstate.HIPL.bundleIO.SequenceBundleWriter;
import com.okstate.HIPL.util.Config;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

/**
 *
 * @author sridhar
 */
public class BundleFile {
    
    private Path _filepath;
    private Configuration _conf;
    private int type=0;
    private String str=null;
    public BundleFile(String path, Configuration conf){
        _filepath=new Path(path);
        _conf=conf;
        str=path;
    }
    
    public BundleFile(Path path, Configuration conf){
        _filepath=path;
        _conf=conf;
    }
  
    public Config getHConfig(){
        return new Config(_filepath,_conf);
    } 
    
    public String getName(){
        return null;
    }
    public String getPathAsString(){
        return str;
    }
    
    public Path getPath(){
        return _filepath;
    }
    public Configuration getConfiguration(){
        return _conf;
    }
    
    public int getType(){
        type=generateType();
        return type;
    }
    
    private int generateType() {
        String temp=_filepath.toString();
        int x=temp.lastIndexOf(".");
        String y=temp.substring(x+1);
        y=y.toLowerCase();
        switch(y){
            case "seq": return 0;
            case "map": return 1;
            case "har": return 2;
        }
        return 0;
    }
    
    public BundleWriter getBundleWriter(){
        String temp=_filepath.toString();
        int x=temp.lastIndexOf(".");
        String y=temp.substring(x+1);
        y=y.toLowerCase();
        switch(y){
            case "seq": return new SequenceBundleWriter(this);
            case "map": return new MapBundleWriter(this);
            case "har": return new HARBundleWriter(this);
        }
        return new SequenceBundleWriter(this);
        
    }
}
