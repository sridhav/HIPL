/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okstate.HIPL.bundleIO;

import com.okstate.HIPL.bundle.BundleFile;
import com.okstate.HIPL.image.HImage;
import com.okstate.HIPL.util.Config;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader.Option;

/**
 *
 * @author sridhar
 */
public class SequenceBundleReader implements BundleReader{

    private SequenceFile.Reader _seqReader;
    private long _seqTotal=0;
    private Config _hConf;
    private BundleFile _file;
     private Configuration conf;
    private Path path;
    long _tempKey;
    HImage _tempImage;
    private BytesWritable _tempImageBytes;
    
    public SequenceBundleReader(BundleFile file){
        _file=file;
    }
    
    public SequenceBundleReader(String path, Configuration conf){
        _hConf=new Config(path,conf);
        _file=new BundleFile(path, conf);
        this.path=new Path(path);
        this.conf=conf;
        openToRead();
    }
    
    public SequenceBundleReader(Path path, Configuration conf){
        _hConf=new Config(path,conf);
        _file=new BundleFile(path, conf);
        this.path=path;
        this.conf=conf;
        openToRead();
    }
    
    @Override
    public void openToRead() {
        try {
            Option opt1=SequenceFile.Reader.file(path);
            //Option opt2=SequenceFile.Reader.keyClass(LongWritable.class);
            //Option opt3=SequenceFile.Writer.valueClass(BytesWritable.class);
            _seqReader=new SequenceFile.Reader(conf,opt1);
            
           // _seqReader=new SequenceFile.Reader(_hConf.getFileSystem(), _hConf.getPath(), _hConf.getConfiguration());
        } catch (IOException ex) {
            Logger.getLogger(SequenceBundleReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean hasNext() {
        try {
            LongWritable key=new LongWritable();
            BytesWritable image=new BytesWritable();
            
            if(_seqReader.next(key,image)){
                _tempKey=key.get();   
                _tempImageBytes=image;
                _tempImage=new HImage(image.getBytes());
                return true;
            }
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

    @Override
    public HImage next() {
        _seqTotal++;
        return _tempImage;
    }

    @Override
    public void close() {
        try {
            if(_seqReader!=null){ 
                    _seqReader.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(SequenceBundleReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public long getReturnCount() {
        return _seqTotal;
    }

    @Override
    public Config getConfiguration() {
        return _hConf;
    }
    
    public BytesWritable getValue(){
        return _tempImageBytes;
    }
}
