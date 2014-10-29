/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okstate.HIPL.bundleIO;

import com.okstate.HIPL.bundle.BundleFile;
import com.okstate.HIPL.image.HImage;
import com.okstate.HIPL.util.Config;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;

/**
 *
 * @author sridhar
 */
public class MapBundleWriter implements BundleWriter{
    private Config _hConf;
    private MapFile.Writer _mapWriter;
    private long _total;
    private BundleFile _file;
    public MapBundleWriter(BundleFile file){
        _file=file;
    }
    
    public MapBundleWriter(String path, Configuration conf){
        _hConf=new Config(path, conf);
        _file=new BundleFile(path,conf);
        openToWrite();
    }
    
    public MapBundleWriter(Path path, Configuration conf){
        _hConf=new Config(path, conf);
        _file=new BundleFile(path,conf);
        openToWrite();
    }
    
    @Override
    public void openToWrite() {
        try {
            _mapWriter =new MapFile.Writer(_hConf.getConfiguration(),_hConf.getFileSystem(), _hConf.getPath().toString(), LongWritable.class, BytesWritable.class);
            _total=1;
        } catch (IOException ex) {
            Logger.getLogger(SequenceBundleWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void appendImage(HImage himage) {
        try {
            _mapWriter.append(new LongWritable(_total), new BytesWritable(himage.getImageBytes()));
        } catch (IOException ex) {
            Logger.getLogger(SequenceBundleWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void appendImage(InputStream inputstream) {
        HImage temp=new HImage(inputstream);
        appendImage(temp);
    }

    @Override
    public void appendImage(File file) {
        HImage temp=new HImage(file);
        appendImage(temp);
    }

    @Override
    public Config getConfiguration() {
        return _hConf;
    }

    @Override
    public long getImageCount() {
        return _total;
    }

    @Override
    public void close() {
        try {
            if(_mapWriter!=null){ 
                    _mapWriter.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(SequenceBundleReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void appendBundle(Path path) {
        try {
            System.out.println("MERGE STARTED: "+_hConf.getPath()+" and "+path +":"+System.currentTimeMillis());
            long start=System.currentTimeMillis();
            FileUtil.copyMerge(_hConf.getFileSystem(), _file.getPath(), _hConf.getFileSystem(), path, true, null, null);
            long end=System.currentTimeMillis();
            System.out.println("MERGE ENDED : "+_hConf.getPath()+" and "+path+" in "+(end-start)+" ms");
        
        } catch (IOException ex) {
            Logger.getLogger(SequenceBundleWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void appendBundle(BundleFile file){
        appendBundle(file.getPath());
    }
    
    @Override
    public BundleFile getBundleFile(){
        return _file;
    }
}
