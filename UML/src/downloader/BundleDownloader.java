/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader;

import com.okstate.HIPL.util.Config;
import java.io.IOException;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

/**
 *
 * @author LSAdmin
 */
public class BundleDownloader {
    
    public static class Map extends MapReduceBase implements Mapper<IntWritable, Text, BooleanWritable,Text>{
       
        @Override
        public void map(IntWritable k1, Text v1, OutputCollector<BooleanWritable, Text> oc, Reporter rprtr) throws IOException {
        
        }
        
        
    }
    
}
