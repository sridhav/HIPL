/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader;

import com.okstate.HIPL.bundleIO.BundleWriter;
import com.okstate.HIPL.bundleIO.MapBundleWriter;
import com.okstate.HIPL.bundleIO.SequenceBundleWriter;
import com.okstate.HIPL.util.Config;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

/**
 *
 * @author LSAdmin
 */
public class BundleDownloader {
    static String filePath;
    static String filetype;
    public BundleDownloader(String path, String type){
        filePath=path;
        filetype=type;
    }
    
    
    public static class Map extends Mapper<IntWritable, Text, BooleanWritable,Text>{
        public static Configuration conf;
        
        @Override
        public void setup(Context c){
            conf=c.getConfiguration();
        }
        
        public void map(IntWritable k1, Text v1, OutputCollector<BooleanWritable, Text> oc, Reporter rprtr) throws IOException {
            String temppath=BundleDownloader.filePath+k1.get()+"."+BundleDownloader.filetype+".tmp";
            System.out.println(temppath);
            BundleWriter bw = null;
            if(BundleDownloader.filetype=="seq"){
                bw=new SequenceBundleWriter(new Path(temppath), conf);
            }
            else if(BundleDownloader.filetype=="map"){
                bw=new MapBundleWriter(new Path(temppath), conf);
            }
            else{
                bw=new SequenceBundleWriter(new Path(temppath), conf);
            }
            String word=v1.toString();
            
            BufferedReader reader=new BufferedReader(new StringReader(word));
            
            String temp;
            int i=k1.get();
            int prev=i;
            while((temp=reader.readLine())!=null){
                if(i>=prev){
                    bw.close();
                    oc.collect(new BooleanWritable(true), new Text(temppath));
                    temppath=BundleDownloader.filePath+i+".tmp";
                    if(BundleDownloader.filetype=="seq"){
                        bw=new SequenceBundleWriter(new Path(temppath), conf);
                    }
                    else if(BundleDownloader.filetype=="map"){
                        bw=new MapBundleWriter(new Path(temppath), conf);
                    }
                    else{
                        bw=new SequenceBundleWriter(new Path(temppath), conf);
                    }
                    prev=i;
                }
                
                long start=System.currentTimeMillis();
                long end=0;
                
                String type=null;
                URLConnection conn;
                
                URL link=new URL(temp);
                System.out.println("Image Downloading "+link.toString());
                conn=link.openConnection();
                conn.connect();
                
                if(type!=null && type.compareTo("image/jpeg")==0)
                    bw.appendImage(conn.getInputStream());
                
                i++;
                
                end=System.currentTimeMillis();
                
                System.out.println("TOOK :"+(float)((end-start)/1000.0)+" seconds\n");
               
                reader.close();
                bw.close();
            }
                        
        }
        
        
    }
    
}
