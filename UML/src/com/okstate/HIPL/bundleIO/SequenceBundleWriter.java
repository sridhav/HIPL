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
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;

/**
 *
 * @author sridhar
 */
public class SequenceBundleWriter implements BundleWriter{

    private SequenceFile.Writer _seqWriter;
    private Config _hConf;
    private long _seqTotal=0;
    private BundleFile _file;
    public SequenceBundleWriter(BundleFile file){
        _file=file;
    }
    
    public SequenceBundleWriter(String path, Configuration conf){
        _hConf=new Config(path, conf);
        _file=new BundleFile(path,conf);
        openToWrite();
    }
    
    public SequenceBundleWriter(Path path, Configuration conf){
        _hConf=new Config(path, conf);
        _file=new BundleFile(path,conf);
        openToWrite();
    }
    
    @Override
    public void openToWrite() {
        try {
            _seqWriter = SequenceFile.createWriter(_hConf.getFileSystem(), _hConf.getConfiguration(), _hConf.getPath(), LongWritable.class, BytesWritable.class);
            _seqTotal=1;
        } catch (IOException ex) {
            Logger.getLogger(SequenceBundleWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void appendImage(HImage himage) {
        try {
            _seqWriter.append(new LongWritable(_seqTotal), new BytesWritable(himage.getImageBytes()));
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
        return _seqTotal;
    }

    @Override
    public void close() {
        try {
            if(_seqWriter!=null){
                _seqWriter.close();
            }
        } catch (IOException ex) {
                Logger.getLogger(SequenceBundleWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @Override
    public void appendBundle(Path path,Configuration conf) {
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
        appendBundle(file.getPath(),file.getConfiguration());
    }
    
    @Override
    public BundleFile getBundleFile(){
        return _file;
    }
    
}
