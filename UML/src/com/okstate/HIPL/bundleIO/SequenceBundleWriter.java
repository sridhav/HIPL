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
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.SequenceFile.Writer.Option;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;

/**
 *
 * @author sridhar
 */
public final class SequenceBundleWriter implements BundleWriter{

    private SequenceFile.Writer _seqWriter;
    private Config _hConf;
    private long _seqTotal=0;
    private BundleFile _file;
    private Configuration conf;
    private Path path;
    
    public SequenceBundleWriter(BundleFile file){
        _file=file;
        openToWrite();
    }
    
    public SequenceBundleWriter(String path, Configuration conf){
        _hConf=new Config(path, conf);
        _file=new BundleFile(path,conf);
        this.path=new Path(path);
        this.conf=conf;
        openToWrite();
    }
    
    public SequenceBundleWriter(Path path, Configuration conf){
        _hConf=new Config(path, conf);
        _file=new BundleFile(path,conf);
        this.conf=conf;
        this.path=path;
        openToWrite();
    }
    
    @Override
    public void openToWrite() {
        try {
            if(conf==null || path==null){
                System.out.println("Invalid Path or Conf");
            }
            System.out.println(path.toString());
            Option opt1=SequenceFile.Writer.file(path);
            Option opt2=SequenceFile.Writer.keyClass(LongWritable.class);
            Option opt3=SequenceFile.Writer.valueClass(BytesWritable.class);
            
            _seqWriter = SequenceFile.createWriter(conf,opt1,opt2,opt3);
            _seqTotal=1;
        } catch (IOException ex) {
            Logger.getLogger(SequenceBundleWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void appendImage(HImage himage) {
        try {
            _seqWriter.append(new LongWritable(_seqTotal), new BytesWritable(himage.getImageBytes()));
            _seqTotal++;
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
        System.out.println("MERGE STARTED: "+_hConf.getPath()+" and "+path +":"+System.currentTimeMillis());
        long start=System.currentTimeMillis();
        long end=System.currentTimeMillis();
        System.out.println("MERGE ENDED : "+_hConf.getPath()+" and "+path+" in "+(end-start)+" ms");
        
    }
    
    public void appendBundle(BundleFile file){
        appendBundle(file.getPath(),file.getConfiguration());
    }
    
    @Override
    public BundleFile getBundleFile(){
        return _file;
    }
    
}
