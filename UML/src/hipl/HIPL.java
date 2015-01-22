/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hipl;

import com.okstate.HIPL.exdown.Downloader;
import com.okstate.HIPL.extract.ImageExtractor;
import com.okstate.HIPL.process.ImageProcess;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
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
           
           PrintStream out = new PrintStream(new FileOutputStream("output.txt",true));
           System.setOut(out);
          
          /* String myargs[]={"/user/cloudera/urls/input","/user/cloudera/urls/outex/img.tmp","100"};
           ToolRunner.run((Tool) new Downloader(), myargs);
           
           String myargs2[]={"/user/cloudera/urls/outex/img.tmp","com.okstate.HIPL.process.GrayAlgorithm"};
           ToolRunner.run((Tool) new ImageProcess(), myargs2);
           
           String myargs3[]={"/user/cloudera/urls/outex/img.tmp.out","/tmp/imgs/"};
           ToolRunner.run((Tool) new ImageExtractor(), myargs3);
          */ 
           Calendar cal=Calendar.getInstance();
           cal.setTime(new Date());
           
           if(args.length!=3){
               System.out.println("Usage: hadoop jar HIPL.jar <input folder> <output folder> <file name with ext>");
               System.exit(1);
           }
           
           String date=cal.get(Calendar.DATE)+"-"+cal.get(Calendar.MONTH+1)+"-"+cal.get(Calendar.YEAR);
           String time=cal.get(Calendar.HOUR)+"-"+cal.get(Calendar.MINUTE);
           
           if(args[1].charAt(args[1].length()-1)!='/'){
               args[1]=args[1]+"/";
           }
           
           String outPath=args[1]+date+"/"+time+"/"+args[2];
           
           String myargs[]={args[0],outPath,"10"};
           ToolRunner.run((Tool) new Downloader(), myargs);
           
           String myargs2[]={outPath,"com.okstate.HIPL.process.GrayAlgorithm"};
           ToolRunner.run((Tool) new ImageProcess(), myargs2);
        
           String myargs3[]={outPath+".out","/tmp/imgs/"};
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
