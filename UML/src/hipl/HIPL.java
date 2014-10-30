/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hipl;

import com.okstate.HIPL.exdown.Downloader;
import downloader.BundleDownloader;
import java.io.IOException;
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
    public static void main(String[] args) throws Exception {
        try {
            // TODO code application logic here
           ToolRunner.run((Tool) new Downloader(), args);
        } catch (IOException ex) {
            Logger.getLogger(HIPL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HIPL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HIPL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
