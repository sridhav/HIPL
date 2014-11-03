/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.okstate.HIPL.extract;

import com.okstate.HIPL.bundleIO.SequenceBundleReader;
import com.okstate.HIPL.image.HImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;

/**
 *
 * @author Sridhar
 */
public class ImageExtractor extends Configured implements Tool {
    
    
    public static class ImageExtractorMapper extends Mapper<LongWritable, BytesWritable, BooleanWritable, Text>{
        
        Path path;
        
        Configuration conf;
        Path outdir;
        @Override
        public void setup(Context jc){
            conf=jc.getConfiguration();
        }
        
        @Override
        public void map(LongWritable key, BytesWritable value, Context context) throws IOException, InterruptedException{
            path=new Path(conf.get("inputpath"));
            outdir=new Path(conf.get("outdir"));
           
            
            Path temp=null;
            FileSystem local = FileSystem.getLocal(conf);
            FSDataOutputStream out=null;
            
            if(value!=null){
                temp=new Path(conf.get("outdir")+key+".jpg");
                out=local.create(temp);
                out.write(value.getBytes());
                context.write(new BooleanWritable(true),new Text("val"+key));
                
                out.hflush();
                out.hsync();
                out.close(); 
            }
            
        }
    }
    
    
    
    @Override
    public int run(String[] strings) throws Exception {
        
        if(strings.length!=2){
            System.out.println("Usage: extracter <inp file> <outfile>");
        }
        
        Configuration conf=new Configuration();
        
        conf.setStrings("inputpath", strings[0]);
        conf.setStrings("outdir", strings[1]);
        
        Job job=new Job(conf,"Extracter");
        
        job.setJarByClass(ImageExtractor.class);
        job.setMapperClass(ImageExtractorMapper.class);
        job.setReducerClass(Reducer.class);
        job.setNumReduceTasks(0);
        
        job.setOutputValueClass(Text.class);
        job.setOutputKeyClass(BooleanWritable.class);
        
        job.setMapOutputKeyClass(BooleanWritable.class);
        job.setMapOutputValueClass(Text.class);
        
        FileOutputFormat.setOutputPath(job, new Path(strings[0]+"_out"));
        
        FileInputFormat.addInputPath(job, new Path(strings[0]));
        
        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        
        return job.waitForCompletion(true)?0:1;
        
    }
    
}
