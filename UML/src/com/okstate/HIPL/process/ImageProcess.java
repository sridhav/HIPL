/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.okstate.HIPL.process;

import com.okstate.HIPL.util.Random;
import com.okstate.HIPL.bundle.BundleFile;
import com.okstate.HIPL.bundleIO.BundleWriter;
import com.okstate.HIPL.image.HImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

/**
 *
 * @author Sridhar
 */
public class ImageProcess extends Configured implements Tool{
    
    public static class ImageProcessMapper extends Mapper<LongWritable, BytesWritable, BooleanWritable, Text>{
        private static Configuration conf;
        private static BundleWriter bw=null;
        private static BundleFile bf=null;
        private static String temp=null;
        @Override
        public void setup(Context jc){
            conf=jc.getConfiguration();
            temp= conf.get("processpath")+"_tmp/";
            
            try {
                createDir(temp, conf);
            } catch (IOException ex) {
                Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
           // temp=temp+Random.random()+".out";
            bf=new BundleFile(new Path(temp+Random.random()+".out"),conf);
            bw=bf.getBundleWriter();
        }
        
        @Override
        public void map(LongWritable key, BytesWritable value, Context context) throws IOException, InterruptedException{
            
            if(value!=null){
                try {
                    HImage him=new HImage(value.getBytes());
                    //ProcessImage pi=new ProcessImage(him,ProcessImage.GRAY);
                    GenericAlgorithm pi=(GenericAlgorithm)Class.forName(conf.get("proclass")).newInstance();
                    // GenericAlgorithm pi=(GenericAlgorithm) o;
                    pi.setHImage(him);
                    pi.run();
                    him=pi.getProcessedImage();
                    bw.appendImage(him);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
                }
                /*       if(size>=blockSize){
                bw.close();
                context.write(new BooleanWritable(true), new Text(temp));
                temp=conf.get("processpath")+"."+Random.random()+".out";
                bf=new BundleFile(new Path(conf.get("processpath")+"."+Random.random()+".out"),conf);
                bw=bf.getBundleWriter();
                size=0;
                }*/
            }
        }
        
        @Override
        public void cleanup(Context context){
            try {
                context.write(new BooleanWritable(true), new Text(temp));
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public static class ImageProcessReducer extends Reducer<BooleanWritable, Text, BooleanWritable, Text>{
        private Configuration conf;
        private static BundleWriter bw=null;
        private static BundleFile bf=null;
        private static String temp=null;
        @Override
        public void setup(Context jc) throws IOException{
            conf=jc.getConfiguration();
            temp= conf.get("processpath")+"_tmp/";
            bf=new BundleFile(new Path(conf.get("processpath")+".out"), conf);
            bw=bf.getBundleWriter();
        }
        
        @Override
        public void reduce(BooleanWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
            FileSystem fs = FileSystem.get(conf);
            FileStatus[] status = fs.listStatus(new Path(temp));
            for (int i=0;i<status.length;i++){
                Path temp_path = status[i].getPath();
                bw.appendBundle(temp_path,conf);
            }
            bw.close();
            
        }
        
        @Override
        public void cleanup(Context context){
            try {
                removeDir(temp, conf);
            } catch (IOException ex) {
                Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
    @Override
    public int run(String[] args) throws Exception {
        if (args.length !=2)
        {
            System.out.println("Usage: downloader <input file> <output file> <nodes> <outtype>");
            System.exit(0);
        }
        
        
        Configuration conf=new Configuration();
        conf.set("mapreduce.map.java.opts", "-Xmx6000m");
        conf.set("mapreduce.reduce.java.opts", "-Xmx6000m");
        
        String inpfile=args[0];
        
        conf.setStrings("processpath", inpfile);
        conf.setStrings("proclass", args[1]);
        
        Job job=new Job(conf,"Processor");
        job.setJarByClass(ImageProcess.class);
        job.setMapperClass(ImageProcessMapper.class);
        job.setReducerClass(ImageProcessReducer.class);
        job.setNumReduceTasks(1);
        
        job.setOutputKeyClass(BooleanWritable.class);
        job.setOutputValueClass(Text.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        
        job.setMapOutputKeyClass(BooleanWritable.class);
        job.setMapOutputValueClass(Text.class);
        FileOutputFormat.setOutputPath(job, new Path(inpfile + "_process"));
        
        FileInputFormat.addInputPath(job, new Path(inpfile));
        
        return job.waitForCompletion(true)?0:1;
    }
    
    public static void createDir(String path, Configuration conf) throws IOException {
        Path output_path = new Path(path);
        
        FileSystem fs = FileSystem.get(conf);
        
        if (!fs.exists(output_path)) {
            fs.mkdirs(output_path);
        }
    }
    
    public static void removeDir(String path, Configuration conf) throws IOException {
        Path output_path = new Path(path);
        FileSystem fs = FileSystem.get(conf);
        if (!fs.exists(output_path)) {
            fs.delete(output_path,true);
        }
    }
}
