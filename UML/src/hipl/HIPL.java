/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hipl;

import com.okstate.HIPL.exdown.Downloader;
import com.okstate.HIPL.extract.ImageExtractor;
import com.okstate.HIPL.process.ImageProcess;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author sridhar
 */
public class HIPL {

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) throws Exception {
        try {
            // TODO code application logic here
           PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
           System.setOut(out);
          
          /* String myargs[]={"/user/cloudera/urls/input","/user/cloudera/urls/outex/img.tmp","100"};
           ToolRunner.run((Tool) new Downloader(), myargs);
           
           String myargs2[]={"/user/cloudera/urls/outex/img.tmp","com.okstate.HIPL.process.GrayAlgorithm"};
           ToolRunner.run((Tool) new ImageProcess(), myargs2);
           
           String myargs3[]={"/user/cloudera/urls/outex/img.tmp.out","/tmp/imgs/"};
           ToolRunner.run((Tool) new ImageExtractor(), myargs3);
          */ 
           
           String myargs[]={"/user/admin/images/input","/user/admin/images/outex/img.seq","10"};
           ToolRunner.run((Tool) new Downloader(), myargs);
           
           String myargs2[]={"/user/admin/images/outex/img.seq","com.okstate.HIPL.process.GrayAlgorithm"};
           ToolRunner.run((Tool) new ImageProcess(), myargs2);
        
           String myargs3[]={"/user/admin/images/outex/img.seq","/tmp/imgs/"};
           ToolRunner.run((Tool) new ImageExtractor(), myargs3);
          
           
        } catch (IOException ex) {
            Logger.getLogger(HIPL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HIPL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HIPL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
