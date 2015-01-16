/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.okstate.HIPL.process;

import com.okstate.HIPL.bundleIO.SequenceBundleWriter;
import com.okstate.HIPL.exdown.DownloaderInputFormat;
import com.okstate.HIPL.image.HImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
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
        private static SequenceBundleWriter sbw=null;
        @Override
        public void setup(Context jc){
            conf=jc.getConfiguration();
            path=new Path(conf.get("processpath"));
            sbw=new SequenceBundleWriter(new Path(conf.get("processpath")+".out"),conf);
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
                    sbw.appendImage(him);
                } catch (        ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            /*if(key.get()%100==0){
                sbw.close();
                context.write(new BooleanWritable(true), new Text(sbw.getBundleFile().getPath().toString()));
                sbw=null;
                
            }*/
        }
        
        
    }
    
    
    public static class ImageProcessReducer extends Reducer<BooleanWritable, Text, BooleanWritable, Text>{
        private Configuration conf;
        
        @Override
        public void setup(Context jc) throws IOException{
            conf=jc.getConfiguration();
        }
        
        @Override
        public void reduce(BooleanWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
            if(key.get()){
                SequenceBundleWriter sbw = new SequenceBundleWriter(new Path(conf.get("processpath")+".out"), conf);
                for (Text temp_string : values) {
                    Path temp_path = new Path(temp_string.toString());
                    //BundleFile bf=new BundleFile(temp_path,conf);
                    sbw.appendBundle(temp_path,conf);
                    context.write(new BooleanWritable(true), new Text(sbw.getBundleFile().getPath().toString()));
                    context.progress();
                }
                //sbw.appendBundle(new Path(conf.get("downloader.outpath")),conf);
                sbw.close();
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
    
}
