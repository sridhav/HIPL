/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.okstate.HIPL.process;

import com.okstate.HIPL.util.Random;
import com.okstate.HIPL.bundle.BundleFile;
import com.okstate.HIPL.bundleIO.BundleWriter;
import com.okstate.HIPL.exdown.DownloaderInputFormat;
import com.okstate.HIPL.image.HImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        private static int count=0;
        private static Path path=null;
        private static long size=0;
        private static long blockSize=0;
        private static BundleWriter bw=null;
        private static BundleFile bf=null;
        private static String temp=null;
        @Override
        public void setup(Context jc){
            conf=jc.getConfiguration();
            path=new Path(conf.get("processpath"));
             try {
                createDir(conf.get("processpath")+"2/process/", conf);
            } catch (IOException ex) {
                Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
            temp=conf.get("processpath")+"2/process/"+Random.random()+".out";
            bf=new BundleFile(new Path(conf.get("processpath")+"2/process/"+Random.random()+".out"),conf);
            bw=bf.getBundleWriter();
            blockSize=getBlockSize()-(1024*1024);
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
                    size=size+him.getImageBytes().length;
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
        
        private long getBlockSize() {
            
            String x=conf.get("dfs.blocksize");
            x=x.toLowerCase();
            long z=0;
            if(x.matches("[0-9]+")){
                z=Long.parseLong(x);
            }
            else if(x.matches("[0-9]+[A-Za-z]")){
                
                char y=x.charAt(x.length()-1);
                z=Long.parseLong(x.substring(0, x.length()-1));
                System.out.println(y);
                
                switch(y){
                    case 'k': z=z<<10;
                    break;
                    case 'm': z=z<<20;
                    break;
                    case 'g' : z=z<<30;
                    break;
                    case 't' : z=z<<40;
                    break;
                    case 'p' : z=z<<50;
                    break;
                    case 'e' : z=z<<60;
                    break;
                }
                System.out.println(z);
            }
            if(z>0){
                return z;
            }
            return 134217728;
        }
        
    }
    
    
    public static class ImageProcessReducer extends Reducer<BooleanWritable, Text, BooleanWritable, Text>{
        private Configuration conf;
        private static BundleWriter bw=null;
        private static BundleFile bf=null;
        private static String str=null;
        @Override
        public void setup(Context jc) throws IOException{
            conf=jc.getConfiguration();
            str=conf.get("processpath")+".out";
            bf=new BundleFile(new Path(conf.get("processpath")+".out"), conf);
            bw=bf.getBundleWriter();
        }
        
        @Override
        public void reduce(BooleanWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
         
            FileSystem fs = FileSystem.get(conf);
            FileStatus[] status = fs.listStatus(new Path(conf.get("processpath")+"2/process/"));  
            for (int i=0;i<status.length;i++){
                    Path temp_path = status[i].getPath();
                    bw.appendBundle(temp_path,conf);
            }
            bw.close();
        }        //sbw.appendBundle(new Path(conf.get("downloader.outpath")),conf);
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
    
}
